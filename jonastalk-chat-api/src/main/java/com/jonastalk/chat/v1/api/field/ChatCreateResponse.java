package com.jonastalk.chat.v1.api.field;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name ChatCreateResponse.java
 * @brief Chat Create Response Data
 * @author Jonas Lim
 * @date Jun 27, 2025
 */
@Getter
public enum ChatCreateResponse implements IResponseParam {

	CHAT_ID
		("chatId",
			"Chat Id",
			EnumApiParamType.STRING
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private ChatCreateResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}