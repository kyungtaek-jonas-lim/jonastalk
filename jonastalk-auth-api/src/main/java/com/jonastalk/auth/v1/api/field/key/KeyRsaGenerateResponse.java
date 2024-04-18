
package com.jonastalk.auth.v1.api.field.key;

import com.jonastalk.common.api.field.IResponseParam;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name KeyRsaGenerateResponse.java
 * @brief Key Rsa Generate Response Data
 * @author Jonas Lim
 * @date 2023.12.03
 */
@Getter
public enum KeyRsaGenerateResponse implements IResponseParam {

	SUCCESS
		("success",
			"SuccessYn",
			EnumApiParamType.BOOLEAN
		)
	;

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private KeyRsaGenerateResponse (String name, String explanation, EnumApiParamType type) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
	}
}