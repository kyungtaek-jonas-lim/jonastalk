package com.jonastalk.auth.v1.api.field.token;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name TokenAccessGenerateResponse.java
 * @brief Token Access Generate Response Data
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Getter
public enum TokenAccessGenerateResponse implements IResponseParam {

	ACCESS_TOKEN
		("accessToken",
			"Access Token",
			EnumApiParamType.STRING
		)
	,REFRESH_TOKEN
		("refreshToken",
			"Refresh Token",
			EnumApiParamType.STRING
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private TokenAccessGenerateResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}