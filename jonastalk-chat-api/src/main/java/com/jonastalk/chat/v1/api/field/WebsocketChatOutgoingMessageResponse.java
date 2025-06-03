package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name WebsocketChatOutgoingMessageResponse.java
 * @brief Websocket Chat Outgoing Message Response Data
 * @author Jonas Lim
 * @date June 3, 2025
 */
@Getter
public enum WebsocketChatOutgoingMessageResponse implements IResponseParam {

	TYPE
		("type",
			"Type",
			EnumApiParamType.STRING
		)
	,FROM_USER_ID
		("fromUserId",
			"FromUserId",
			EnumApiParamType.STRING
		)
	,MESSAGE
		("message",
			"Message",
			EnumApiParamType.STRING
		)
	
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private WebsocketChatOutgoingMessageResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}