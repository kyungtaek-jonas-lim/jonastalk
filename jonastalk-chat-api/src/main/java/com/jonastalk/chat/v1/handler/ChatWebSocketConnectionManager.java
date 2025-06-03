package com.jonastalk.chat.v1.handler;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jonastalk.common.service.RedissonService;

import lombok.extern.slf4j.Slf4j;

/**
 * @name ChatWebSocketConnectionManager.java
 * @brief Chat WebSocket Connection Manager
 * @author Jonas Lim
 * @date 2025.06.02
 */
@Slf4j
@Component
public class ChatWebSocketConnectionManager {

	@Lazy
	@Autowired
	private RedissonService redissonService;
    
    @Value("${ws.redis.prefix.common}")
    private String redisPrefixCommon;
    
    @Value("${ws.redis.prefix.chat}")
    private String redisPrefixChat;

    @Value("${ws.redis.ttl-secs.chat}")
    private Long ttlRedisSecsChat;
    

 	/**
 	 * @name putChatWebSocketSession
 	 * @brief Put Chat Web Socket Session
 	 * @author Jonas Lim
 	 * @date 2025.06.02
 	 */
     public boolean putChatWebSocketSession(String username, String sessionId) {
     	if (!StringUtils.hasText(username) || !StringUtils.hasText(sessionId)) return false;
     	final String stringKey = redisPrefixCommon + redisPrefixChat + sessionId;
     	final String setKey = redisPrefixCommon + redisPrefixChat + username;
     	boolean result = redissonService.putDataWithLock(stringKey, username, ttlRedisSecsChat); // String (key: sessionId, value: key)
     	if (result) {
     		result = redissonService.addToSetWithLock(setKey, sessionId, ttlRedisSecsChat); // Set (key: username, value: sessionIds)
     		if (!result) redissonService.deleteData(stringKey);
     	}
 		return result;
     }
     

 	/**
 	 * @name getChatWebSocketUsernameBySessionId
 	 * @brief Get Chat Web Socket Username By SessionId
 	 * @author Jonas Lim
 	 * @date 2025.06.02
 	 */
     public String getChatWebSocketUsernameBySessionId(String sessionId) {
     	if (!StringUtils.hasText(sessionId)) return null;
     	final String key = redisPrefixCommon + redisPrefixChat + sessionId;
 		return redissonService.getDataWithLock(key);
     }
   

	/**
	 * @name getChatWebSocketSessionIdsByUsername
	 * @brief Get Chat Web Socket Session Ids By Username
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public Set<String> getChatWebSocketSessionIdsByUsername(String username) {
    	if (!StringUtils.hasText(username)) return null;
    	final String key = redisPrefixCommon + redisPrefixChat + username;
		return redissonService.getSetWithLock(key);
    }

	/**
	 * @name deleteChatWebSocketSession
	 * @brief Delete Chat Web Socket Session
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public boolean deleteChatWebSocketSession(String username, String sessionId) {
    	if (!StringUtils.hasText(username) || !StringUtils.hasText(sessionId)) return false;
    	final String stringKey = redisPrefixCommon + redisPrefixChat + sessionId;
    	final String setKey = redisPrefixCommon + redisPrefixChat + username;
     	boolean result = redissonService.deleteData(stringKey); // String (key: sessionId, value: key)
     	if (result) {
     		result = redissonService.removeDataFromSet(setKey, sessionId); // Set (key: username, value: sessionIds)
     		if (!result) redissonService.putDataWithLock(stringKey, sessionId, ttlRedisSecsChat);
     	}
 		return result;
    }

	/**
	 * @name clearChatWebSocketSessionsByUsername
	 * @brief Clear Chat Web Socket Sessions By Username
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public boolean clearChatWebSocketSessionsByUsername(String username) {
    	if (!StringUtils.hasText(username)) return false;

    	final String setKey = redisPrefixCommon + redisPrefixChat + username;
     	final Set<String> set = redissonService.getSetWithLock(setKey);
     	
     	for (String sessionId: set) {
     		final String stringKey = redisPrefixCommon + redisPrefixChat + sessionId;
    		if (!redissonService.deleteData(stringKey)) { // String (key: sessionId, value: key)
    			log.info("Failed to delete sessionId from Redis - sessionId: {}", sessionId);
    		}
     	}
     	
     	return redissonService.removeSet(setKey); // Set (key: username, value: sessionIds)
    }
}
