package com.jonastalk.chat.v1.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.jonastalk.common.feign.AuthFeignClient;

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

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
         Map<String, String> data = getDataFromQuery(session);
         
        if (data == null || !isTokenValid(data.get("accessToken"), data.get("username"))) {
            log.warn("Invalid or missing token. Closing session.");
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }

        log.info("WebSocket connected: {}", session.getId());
    }

    private boolean isTokenValid(String token, String username) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            data.put("username", username);

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
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private Map<String, String> getDataFromQuery(WebSocketSession session) {
        String query = session.getUri().getQuery();
        Map<String, String> result = new HashMap<>();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2 && ("accessToken".equals(pair[0]) || "username".equals(pair[0])) ) {
                	result.put(pair[0], pair[1]);
                }
            }
        }
        return result;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: {}", message.getPayload());
        session.sendMessage(new TextMessage("[Echo] " + message.getPayload()));
    }
}
