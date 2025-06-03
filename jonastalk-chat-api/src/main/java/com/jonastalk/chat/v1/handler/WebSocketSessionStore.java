package com.jonastalk.chat.v1.handler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/**
 * @name WebSocketSessionStore.java
 * @brief Websocket Session Store
 * @author Jonas Lim
 * @date June 3, 2025
 */
@Component
public class WebSocketSessionStore {
	
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    public void put(String sessionId, WebSocketSession session) {
        sessionMap.put(sessionId, session);
    }

    public WebSocketSession get(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void remove(String sessionId) {
        sessionMap.remove(sessionId);
    }

    public Collection<WebSocketSession> all() {
        return sessionMap.values();
    }
}