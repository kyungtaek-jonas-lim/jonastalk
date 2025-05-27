package com.jonastalk.common.consts;

import lombok.Getter;

/**
 * @name EnumValidationErrorCode.java
 * @brief EnumValidationErrorCode
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Getter
public enum EnumValidationErrorCode {

	NO_REQUEST_DATA		("no request data")
	,REQUIRED			("required parameter(s) ({})")
	,INVALID_VALUE		("invalid values ({}) - valied values ({})")
	;
	
	private String errorMessage;

	private EnumValidationErrorCode(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}