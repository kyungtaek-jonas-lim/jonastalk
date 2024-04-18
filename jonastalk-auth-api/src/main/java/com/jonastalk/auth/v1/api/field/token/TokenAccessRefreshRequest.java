package com.jonastalk.auth.v1.api.field.token;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name TokenAccessRefreshRequest.java
 * @brief Token Access Refresh Request Data
 * @author Jonas Lim
 * @date 2023.12.02
 */
@Getter
public enum TokenAccessRefreshRequest implements IRequestParam {

	REFRESH_TOKEN
		("refreshToken",
			"Jwt Refresh Token",
			EnumApiParamType.STRING,
			-1,
			true,
			null,
			null
		)
	;
	
	public static final String REQUEST_URI = "/v1/token/access/refresh";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private TokenAccessRefreshRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
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
		return TokenAccessRefreshRequest.values();
	}
}
