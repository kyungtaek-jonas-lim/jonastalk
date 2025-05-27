package com.jonastalk.chat.v1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.jonastalk.chat.v1.handler.ChatWebSocketHandler;

import lombok.RequiredArgsConstructor;

/**
 * @name WebSocketConfig.java
 * @brief Spring Boot Websocket Configuration
 * @author Jonas Lim
 * @date May 26, 2025
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
	
    private final ChatWebSocketHandler chatWebSocketHandler;

	/**
	 * @name registerWebSocketHandlers
	 * @author Jonas Lim
	 * @date May 26, 2025
	 * @brief This method registers the WebSocket endpoint("/v1/chat") that clients will use to connect.
	 */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/v1/chat")
                .setAllowedOriginPatterns("*");
    }
}
