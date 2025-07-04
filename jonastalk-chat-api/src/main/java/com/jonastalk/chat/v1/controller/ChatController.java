package com.jonastalk.chat.v1.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

import com.jonastalk.chat.v1.api.field.ChatCreateRequest;
import com.jonastalk.chat.v1.api.field.ChatCreateResponse;
import com.jonastalk.chat.v1.service.ChatService;
import com.jonastalk.common.api.field.CommonParams;

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

	   	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final List<String> userIds = (List) param.get(ChatCreateRequest.TO_USER_IDS.getName());
    	final String username = (String) param.get(CommonParams.USERNAME.getValue());
//    	final List<String> userRoles = (List) param.get(CommonParams.USER_ROLES.getValue());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------
    	final String chatId = chatService.createChat(username, new HashSet<>(userIds));
    	
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	Map<String, Object> responseData = new HashMap<>();
    	responseData.put(ChatCreateResponse.CHAT_ID.getName(), chatId);
    	return ResponseEntity.ok(responseData);
	}


}
