package com.jonastalk.chat.v1.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonastalk.chat.v1.api.field.WebsocketChatIncomingMessageRequest;
import com.jonastalk.chat.v1.api.field.WebsocketChatOutgoingMessageResponse;
import com.jonastalk.chat.v1.api.field.WebsocketConnectionRequest;
import com.jonastalk.common.component.RedisPublisher;
import com.jonastalk.common.component.RedisSubscriberManager;
import com.jonastalk.common.component.ValidationComponent;
import com.jonastalk.common.feign.AuthFeignClient;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @name ChatWebSocketHandler.java
 * @brief Spring Boot Websocket Handler
 * @author Jonas Lim
 * @date May 26, 2025
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
	
	@Autowired
	AuthFeignClient authFeignClient;
	
	@Autowired
	ChatWebSocketConnectionManager chatWebSocketConnectionManager;
	
	@Autowired
	WebSocketSessionStore webSocketSessionStore;
	
	@Autowired
	ValidationComponent validationComponent;
	
	@Autowired
	RedisSubscriberManager redisSubscriberManager;
	
	@Autowired
	RedisPublisher redisPublisher;
	
    /**
	 * @name WebsocketMessageType
	 * @brief WebSocket Message Type
	 * @author Jonas Lim
	 * @date June 3, 2025
     */
    @Getter
    public enum WebsocketMessageType {
    	CHAT					("chat")
    	;
    	private String value;
    	private WebsocketMessageType(String value) {
    		this.value = value;
    	}
    }

    /**
     * @brief Connection Establish Callback
     * @author Jonas Lim
     * @date June 3, 2025
     * @param session
     * @return
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
         
        // Authorization
        Map<String, String> data = getDataFromQuery(session);
        final String accessToken = data.get(WebsocketConnectionRequest.ACCESS_TOKEN.getName());
        final String username = data.get(WebsocketConnectionRequest.USERNAME.getName());
        if (data == null || !isTokenValid(accessToken, username)) {
            log.warn("Invalid or missing token. Closing session.");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        
        // Session Save
        final String sessionId = session.getId();
        final boolean savedSession = chatWebSocketConnectionManager.putChatWebSocketSession(username, sessionId); // Redis Save
        if (savedSession) {
        	webSocketSessionStore.put(sessionId, session); // Local Save
        } else {
        	session.close(new CloseStatus(4001, "Failed to save the session"));
            log.error("WebSocket connection fail: username: {}, sessionId: {}, savedSession: {}", username, sessionId, savedSession);
            return;
        }
        
        // Subscribe Redis Chat Channel
        boolean subscribedRedisChatChannel = false;
        final String channelName = RedisSubscriberManager.RedisPubSubChannel.CHAT.getValue() + username;
    	if (redisSubscriberManager.getChannels() == null || !redisSubscriberManager.getChannels().contains(channelName)) { // If it doesn't subscribe, subscribe the channel
    		subscribedRedisChatChannel = redisSubscriberManager.subscribe(channelName, (channel, msg) -> {
    	        final Set<String> sessionIds = chatWebSocketConnectionManager.getChatWebSocketSessionIdsByUsername(username);
    	        for (String sid: sessionIds) {
    	        	final WebSocketSession toSession = webSocketSessionStore.get(sid);
    	        	if (toSession != null && toSession.isOpen()) {
    	        		try {
							toSession.sendMessage(new TextMessage(msg));
						} catch (IOException e) {
							log.error("WebSocket message send failed. sessionId={}, message={}", toSession.getId(), msg, e);
						}
    	        	}
    	        }
    		});
    	}
        
        log.info("WebSocket connected: username: {}, sessionId: {}, savedSession: {}, subscribedRedisChatChannel: {}", username, sessionId, savedSession, subscribedRedisChatChannel);
    }
    
    /**
     * @brief Connection Closure Callback
     * @author Jonas Lim
     * @date June 3, 2025
     * @param session, status
     * @return
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String sessionId = session.getId();
        int code = status.getCode();
        
        // Logging
        if (code == 1000 || code == 1001) {
        	log.info("WebSocket connection closed gracefully: {}", status);
        } else {
            log.error("WebSocket connection terminated unexpectedly or due to an error: {}", status);
        }
        
        // Delete Session
        final String username = chatWebSocketConnectionManager.getChatWebSocketUsernameBySessionId(sessionId);
        final boolean deletedSession = chatWebSocketConnectionManager.deleteChatWebSocketSession(username, sessionId); // Redis Delete
        webSocketSessionStore.remove(sessionId); // Local Delete
        
        // Unsubscribe Redis Chat Channel
        boolean unsubscribedRedisChatChannel = false;
        final String channelName = RedisSubscriberManager.RedisPubSubChannel.CHAT.getValue() + username;
    	if (redisSubscriberManager.getChannels() != null || redisSubscriberManager.getChannels().contains(channelName)) {
    		Set<String> sessionIds = chatWebSocketConnectionManager.getChatWebSocketSessionIdsByUsername(username);
    		boolean readyToUnsubscribe = true; // Unsubscribe if this service doesn't have the valid sessions for the user
    		if (sessionIds != null) {
    			for (String sid: sessionIds) {
    				if (webSocketSessionStore.get(sid) != null) {
    					readyToUnsubscribe = false;
    					break;
    				}
    			}
    		}
    		if (readyToUnsubscribe) {
    			unsubscribedRedisChatChannel = redisSubscriberManager.unsubscribe(channelName);
    		}
    	}
        
        log.info("WebSocket disconnected: username: {}, sessionId: {}, deletedSession: {}, unsubscribedRedisChatChannel: {}", username, sessionId, deletedSession, unsubscribedRedisChatChannel);
    }

    /**
     * @brief Token Validation
     * @author Jonas Lim
     * @date June 3, 2025
     * @param token, username
     * @return
     */
    private boolean isTokenValid(String token, String username) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(WebsocketConnectionRequest.ACCESS_TOKEN.getName(), token);
            data.put(WebsocketConnectionRequest.USERNAME.getName(), username);

            Map<String, Object> request = new HashMap<>();
            request.put("common", new HashMap<>());
            request.put("data", data);

            log.info("authFeignClient: {}", authFeignClient);
            ResponseEntity<?> responseEntity = authFeignClient.validateAccessToken(request);
            log.info("response data: {}", responseEntity);
            Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
            if (response != null) {
            	if (response.get("data") instanceof Map) {
            		Map<String, Object> result = (Map<String, Object>)response.get("data");
            		return (Boolean)result.get("validation");
            	}
            }
            return false;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * @brief Get Data From Query String
     * @author Jonas Lim
     * @date June 3, 2025
     * @param session
     * @return
     */
    private Map<String, String> getDataFromQuery(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> result = new HashMap<>();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && (WebsocketConnectionRequest.ACCESS_TOKEN.getName().equals(pair[0]) || WebsocketConnectionRequest.USERNAME.getName().equals(pair[0])) ) {
                	result.put(pair[0], pair[1]);
                }
            }
        }
        return result;
    }

    /**
     * @brief Message Handler
     * @author Jonas Lim
     * @date June 3, 2025
     * @param session, message
     * @return
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*
         * Example of incoming message
			{
			  "type": "chat",
			  "toUserIds": [
			  	"df8123a8-df9a-4a8f-b29f-7d8cb3d7d123" // Normally it's userId like UUID, but currently it's username
			  ],
			  "message": "Hello"
			}
         */
        // Process incoming message
    	final String incomingMessage = message.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> messageMap = objectMapper.readValue(incomingMessage, Map.class);
        validationComponent.validateApiRequestData(messageMap, WebsocketChatIncomingMessageRequest.TYPE);
        
        // Chat
        if (WebsocketMessageType.CHAT.getValue().equals( ((String)messageMap.get(WebsocketChatIncomingMessageRequest.TYPE.getName())).toLowerCase() )) {
            final String fromUserId = chatWebSocketConnectionManager.getChatWebSocketUsernameBySessionId(session.getId());
            log.info("[RECEIVED] chat message to send: {'{}'} -> {'{}'}, message: {}", fromUserId, String.join("','", (List) messageMap.get(WebsocketChatIncomingMessageRequest.TO_USER_IDS.getName()) ), incomingMessage);
        
	        
	        // Save message to database
	        
	        
	
	        /*
	         * Example of outgoing message
				{
				  "type": "chat",
				  "fromUserId": "df8123a8-df9a-4a8f-b29f-7d8cb3d7d123", // Normally it's userId like UUID, but currently it's username
				  "message": "Hello"
				}
	         */
	        // Process outgoing message
	        final List<String> toUserIdsList = (List)messageMap.get(WebsocketChatIncomingMessageRequest.TO_USER_IDS.getName());
	        Set<String> toUserIds = new HashSet<>(toUserIdsList);
	        messageMap.remove(WebsocketChatIncomingMessageRequest.TO_USER_IDS.getName());
	        messageMap.put(WebsocketChatOutgoingMessageResponse.FROM_USER_ID.getName(), fromUserId);
	        final String outgoingMessage = objectMapper.writeValueAsString(messageMap);
	        
	        
	        // Publish to the type channel
	        for (String toUserId: toUserIds) {
	            final String channelName = RedisSubscriberManager.RedisPubSubChannel.CHAT.getValue() + toUserId;
	            redisPublisher.publish(channelName, outgoingMessage);
	        }
        }
    }
}
