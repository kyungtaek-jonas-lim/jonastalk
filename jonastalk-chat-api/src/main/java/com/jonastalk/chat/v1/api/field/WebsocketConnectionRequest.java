package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name WebsocketConnectionRequest.java
 * @brief Websocket Connection Request Data
 * @author Jonas Lim
 * @date 2025.05.27
 */
@Getter
public enum WebsocketConnectionRequest implements IRequestParam {

	USERNAME
		("username",
			"Username",
			EnumApiParamType.STRING,
			50,
			true,
			null,
			null
		)
	,ACCESS_TOKEN
		("accessToken",
			"Access Token",
			EnumApiParamType.STRING,
			-1,
			true,
			null,
			null
		)
	;
	
	public static final String REQUEST_URI = "/v1/ws";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private WebsocketConnectionRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
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
		return WebsocketConnectionRequest.values();
	}
}
