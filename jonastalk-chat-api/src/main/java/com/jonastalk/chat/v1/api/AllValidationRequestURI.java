package com.jonastalk.chat.v1.api;

import java.util.HashMap;
import java.util.Map;

import com.jonastalk.chat.v1.api.field.ChatCreateRequest;
import com.jonastalk.chat.v1.api.field.WebsocketConnectionRequest;
import com.jonastalk.common.api.field.IRequestParam;

import lombok.Getter;

/**
 * @name AllValidationRequestURI.java
 * @brief All Request URI For Parameter Validation
 * @goal Validate Request Data by Request URI
 * @author Jonas Lim
 * @date 2025.05.27
 */
@Getter
public enum AllValidationRequestURI {

	WEBSOCKET_CONNECTION_REQUEST		(WebsocketConnectionRequest.REQUEST_URI,			WebsocketConnectionRequest.USERNAME,		ConrollerParamType.MAP_COMMON_DATA)
	,CHAT_CREATE_REQUEST				(ChatCreateRequest.REQUEST_URI,						ChatCreateRequest.TO_USER_IDS,				ConrollerParamType.MAP_COMMON_DATA)
	;
	
	public enum ConrollerParamType {
		MAP_COMMON_DATA
		,MAP
		;
	}
	
	private static Map<String, AllValidationRequestURI> map;

	private String uri;
	private IRequestParam requestParam;
	private ConrollerParamType dataType;
	private AllValidationRequestURI(String uri, IRequestParam requestParam, ConrollerParamType dataType) {
		this.uri = uri;
		this.requestParam = requestParam;
		this.dataType = dataType;
	}
	
	public static Map<String, AllValidationRequestURI> getAllValidationRequestURI() {
		if (map == null) {
			map = new HashMap<>();
			for (AllValidationRequestURI allValidationRequestURI : AllValidationRequestURI.values()) {
				map.put(allValidationRequestURI.getUri(), allValidationRequestURI);
			}
		}
		return map;
	}
}
