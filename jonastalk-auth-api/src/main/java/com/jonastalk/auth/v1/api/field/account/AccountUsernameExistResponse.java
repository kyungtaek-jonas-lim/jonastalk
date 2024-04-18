package com.jonastalk.auth.v1.api.field.account;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name AccountUsernameExistResponse.java
 * @brief Account Username Exist Response Data
 * @author Jonas Lim
 * @date 2023.12.02
 */
@Getter
public enum AccountUsernameExistResponse implements IResponseParam {

	USERNAME
		("username",
			"Username",
			EnumApiParamType.STRING
		)
	,EXIST
		("exist",
			"Existance",
			EnumApiParamType.BOOLEAN
		)
	
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private AccountUsernameExistResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}