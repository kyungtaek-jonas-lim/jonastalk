package com.jonastalk.common.api.field;

import lombok.Getter;

/**
 * @name CommonParams.java
 * @brief Common Param Data (Depth 1)
 * @author Jonas Lim
 * @date 2023.12.04
 */
@Getter
public enum CommonParams {
	
	COMMON					("common")
	,DATA					("data")
	
	,TRANSACTION_ID			("transactionId")
	,CURRENT_DATETIME		("currentDatetime")

	,STATUS					("status")
	,ERROR_CODE				("errorCode")
	,ERROR_MESSAGE			("errorMessage")
	
	,REQUEST_HEADERS		("requestHeaders")
	,REQUEST_URL			("requestURL")
	,HTTP_METHOD			("httpMethod")
	
	,USERNAME				("username")
	,USER_ROLES				("userRoles")
	;
	
	private String value;
	private CommonParams(String value) {
		this.value = value;
	}
}
