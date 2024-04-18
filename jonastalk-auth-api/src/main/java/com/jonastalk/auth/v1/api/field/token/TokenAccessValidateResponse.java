package com.jonastalk.auth.v1.api.field.token;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name TokenAccessValidateResponse.java
 * @brief Token Access Validate Response Data
 * @author Jonas Lim
 * @date 2024.02.21
 */
@Getter
public enum TokenAccessValidateResponse implements IResponseParam {

	VALIDATION
		("validation",
			"Validation",
			EnumApiParamType.BOOLEAN
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private TokenAccessValidateResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}