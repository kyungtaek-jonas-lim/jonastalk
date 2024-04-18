package com.jonastalk.auth.v1.api.field.key;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name KeyRsaReadResponse.java
 * @brief Key Rsa Read Response Data
 * @author Jonas Lim
 * @date 2023.12.03
 */
@Getter
public enum KeyRsaReadResponse implements IResponseParam {

	SUCCESS
		("success",
			"SuccessYn",
			EnumApiParamType.BOOLEAN
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private KeyRsaReadResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}