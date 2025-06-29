package com.jonastalk.chat.v1.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonastalk.chat.v1.service.ChatService;

import io.swagger.annotations.Api;

/**
 * @name ChatController.java
 * @brief Chat Controller
 * @author Jonas Lim
 * @date June 27, 2025
 */
@RestController
@RequestMapping("/v1/chat")
@Api(tags = "Chat API")
public class ChatController {
	
	@Autowired
	ChatService chatService;
	
	/**
	 * @name createChat(@RequestBody Map<String, Object> param)
	 * @brief Create chat
	 * @author Jonas Lim
	 * @date June 27, 2025
	 * @param param
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/create")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createChat(@RequestBody Map<String, Object> param) throws Exception {
    	return ResponseEntity.ok(chatService.createChat(param));
	}


}
