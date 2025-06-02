package com.jonastalk.chat.v1.handler;

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
	RedissonService redissonService;
    
    @Value("${ws.redis.prefix.common}")
    private String redisPrefixCommon;
    
    @Value("${ws.redis.prefix.chat}")
    private String redisPrefixChat;

//    @Getter
//    @Value("${ws.redis.key.chat.value.session}")
//    private String redisKeyChatValueSession;

    @Value("${ws.chat.ttl-mins}")
    private Long chatTtlMins;
    

 	/**
 	 * @name putChatWebSocketSession
 	 * @brief Put Chat Web Socket Session
 	 * @author Jonas Lim
 	 * @date 2025.06.02
 	 */
     public boolean putChatWebSocketSession(String username, String sessionId) {
     	if (!StringUtils.hasText(username) || !StringUtils.hasText(sessionId)) return false;
     	String key = redisPrefixCommon + redisPrefixChat + username;
     	return redissonService.putDataWithLock(key, sessionId, chatTtlMins);
     }
   

	/**
	 * @name getChatWebSocketSessions
	 * @brief Get Chat Web Socket Sessions
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public String getChatWebSocketSessions(String username) {
    	if (!StringUtils.hasText(username)) return null;
    	String key = redisPrefixCommon + redisPrefixChat + username;
		return redissonService.getDataWithLock(key);
    }

	/**
	 * @name deleteChatWebSocketSession
	 * @brief Delete Chat Web Socket Session 
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public boolean deleteChatWebSocketSession(String username, String sessionId) {
    	if (!StringUtils.hasText(username) || !StringUtils.hasText(sessionId)) return false;
    	String key = redisPrefixCommon + redisPrefixChat + username + "::" + sessionId;
		return redissonService.deleteData(key);
    }

	/**
	 * @name clearChatWebSocketSessions
	 * @brief Clear Chat Web Socket Sessions 
	 * @author Jonas Lim
	 * @date 2025.06.02
	 */
    public boolean clearChatWebSocketSessions(String username) {
    	if (!StringUtils.hasText(username)) return false;
    	String key = redisPrefixCommon + redisPrefixChat + username;
		return redissonService.deleteData(key);
    }
}
