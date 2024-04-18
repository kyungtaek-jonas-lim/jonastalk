package com.jonastalk.auth.v1.api.field.token;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name TokenAccessRefreshResponse.java
 * @brief Token Access Refresh Response Data
 * @author Jonas Lim
 * @date 2023.11.28
 */
@Getter
public enum TokenAccessRefreshResponse implements IResponseParam {

	ACCESS_TOKEN
		("accessToken",
			"Access Token",
			EnumApiParamType.STRING
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private TokenAccessRefreshResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}