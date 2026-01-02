package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name ChatGetRequest.java
 * @brief Chat Get Request Data
 * @author Jonas Lim
 * @date July 7, 2025
 */
@Getter
public enum ChatGetRequest implements IRequestParam {

	// TO_USER_IDS
	// 	("toUserIds",
	// 		"To User IDs",
	// 		EnumApiParamType.OBJECT_ARRAY,
	// 		50,
	// 		true,
	// 		null,
	// 		null
	// 	)
	;
	
	public static final String REQUEST_URI = "/v1/chat";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private ChatGetRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
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
		return ChatGetRequest.values();
	}
}
