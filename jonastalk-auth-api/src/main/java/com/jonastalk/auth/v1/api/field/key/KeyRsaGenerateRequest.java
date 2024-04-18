package com.jonastalk.auth.v1.api.field.key;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.component.EncryptionComponent.RsaPurpose;
import com.jonastalk.common.consts.EnumApiParamType;

import lombok.Getter;

/**
 * @name KeyRsaGenerateRequest.java
 * @brief Key Rsa Generate Request Data
 * @author Jonas Lim
 * @date 2023.12.02
 */
@Getter
public enum KeyRsaGenerateRequest implements IRequestParam {

	PURPOSE
		("purpose",
			"Rsa Key Purpose",
			EnumApiParamType.STRING,
			-1,
			true,
			null,
			null
		)
	;
	
	public static final String REQUEST_URI = "/v1/key/rsa/generate";

	private String name;
	private String explanation;
	private EnumApiParamType type;
	private int maxLength;
	private boolean required;
	private Object defaultValue;
	private Object[] validationValues;
	
	private KeyRsaGenerateRequest(String name, String explanation, EnumApiParamType type, int maxLength, boolean required, Object defaultValue, Object[] validationValues) {
		this.name				= name;
		this.explanation 		= explanation;
		this.type 				= type;
		this.maxLength			= maxLength;
		this.required			= required;	
		this.defaultValue		= defaultValue;	
		this.validationValues	= RsaPurpose.getStringArray();
	}

	@Override
	public IRequestParam[] getValues() {
		return KeyRsaGenerateRequest.values();
	}
}
