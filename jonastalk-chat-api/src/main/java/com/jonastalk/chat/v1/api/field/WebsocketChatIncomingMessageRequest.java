package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name WebsocketChatIncomingMessageRequest.java
 * @brief Websocket Chat Incoming Message Request Data
 * @author Jonas Lim
 * @date June 3, 2025
 */
@Getter
public enum WebsocketChatIncomingMessageRequest implements IRequestParam {

	TYPE
		("type",
			"Type",
			EnumApiParamType.STRING,
			10,
			true,
			null,
			null
		)
	,TO_USER_IDS
		("toUserIds",
			"ToUserIds",
			EnumApiParamType.OBJECT_ARRAY,
			-1,
			true,
			null,
			null
		)
	,MESSAGE
		("message",
			"Message",
			EnumApiParamType.STRING,
			1000,
			true,
			null,
			null
		)
	;
	
	public static final String REQUEST_URI = "/v1/chat";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private WebsocketChatIncomingMessageRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
		this.maxLength			= maxLength;
		this.required			= required;	
		this.defaultValue		= defaultValue;	
		this.validationValues	= validationValues;	
	}

	@Override
	public IRequestParam[] getValues() {
		return WebsocketChatIncomingMessageRequest.values();
	}
}
