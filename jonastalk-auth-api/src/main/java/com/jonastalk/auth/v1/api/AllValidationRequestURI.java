package com.jonastalk.auth.v1.api;

import java.util.HashMap;
import java.util.Map;

import com.jonastalk.auth.v1.api.field.account.AccountUsernameExistRequest;
import com.jonastalk.auth.v1.api.field.key.KeyRsaGenerateRequest;
import com.jonastalk.auth.v1.api.field.key.KeyRsaReadRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessGenerateRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessRefreshRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessValidateRequest;
import com.jonastalk.common.api.field.IRequestParam;

import lombok.Getter;

/**
 * @name AllValidationRequestURI.java
 * @brief All Request URI For Parameter Validation
 * @goal Validate Request Data by Request URI
 * @author Jonas Lim
 * @date 2023.12.04
 */
@Getter
public enum AllValidationRequestURI {

	ACCOUNT_USERNAME_EXIST_REQUEST		(AccountUsernameExistRequest.REQUEST_URI,			AccountUsernameExistRequest.USERNAME,		ConrollerParamType.MAP_COMMON_DATA)
	
	,KEY_RSA_GENERATE_REQUEST			(KeyRsaGenerateRequest.REQUEST_URI,					KeyRsaGenerateRequest.PURPOSE,				ConrollerParamType.MAP_COMMON_DATA)
	,KEY_RSA_READ_REQUEST				(KeyRsaReadRequest.REQUEST_URI,						KeyRsaReadRequest.PURPOSE,					ConrollerParamType.MAP_COMMON_DATA)
	
	,TOKEN_ACCESS_GENERATE_REQUEST		(TokenAccessGenerateRequest.REQUEST_URI,			TokenAccessGenerateRequest.USERNAME,		ConrollerParamType.MAP_COMMON_DATA)
	,TOKEN_ACCESS_VALIDATE_REQUEST		(TokenAccessValidateRequest.REQUEST_URI,			TokenAccessValidateRequest.USERNAME,		ConrollerParamType.MAP_COMMON_DATA)
	,TOKEN_REFRESH_GENERATE_REQUEST		(TokenAccessRefreshRequest.REQUEST_URI,				TokenAccessRefreshRequest.REFRESH_TOKEN,	ConrollerParamType.MAP_COMMON_DATA)
	;
	
	public enum ConrollerParamType {
		MAP_COMMON_DATA
		,MAP
		;
	}
	
	private static Map<String, AllValidationRequestURI> map;

	private String uri;
	private IRequestParam requestParam;
	private ConrollerParamType dataType;
	private AllValidationRequestURI(String uri, IRequestParam requestParam, ConrollerParamType dataType) {
		this.uri = uri;
		this.requestParam = requestParam;
		this.dataType = dataType;
	}
	
	public static Map<String, AllValidationRequestURI> getAllValidationRequestURI() {
		if (map == null) {
			map = new HashMap<>();
			for (AllValidationRequestURI allValidationRequestURI : AllValidationRequestURI.values()) {
				map.put(allValidationRequestURI.getUri(), allValidationRequestURI);
			}
		}
		return map;
	}
}
