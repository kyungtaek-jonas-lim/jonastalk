package com.jonastalk.auth.v1.api.field.account;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name AccountUsernameExistRequest.java
 * @brief Account Username Exist Request Data
 * @author Jonas Lim
 * @date 2023.12.02
 */
@Getter
public enum AccountUsernameExistRequest implements IRequestParam {

	USERNAME
		("username",
			"Username",
			EnumApiParamType.STRING,
			50,
			true,
			null,
			null
		)
	;
	
	public static final String REQUEST_URI = "/v1/account/username/exist";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private AccountUsernameExistRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
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
		return AccountUsernameExistRequest.values();
	}
}
