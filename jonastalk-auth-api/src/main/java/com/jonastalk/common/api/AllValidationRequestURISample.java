package com.jonastalk.common.api;

import java.util.HashMap;
import java.util.Map;

import com.jonastalk.common.api.field.IRequestParam;

import lombok.Getter;

/**
 * @name AllValidationRequestURISample.java
 * @brief All Request URI For Parameter Validation Sample
 * @goal Validate Request Data by Request URI
 * @author Jonas Lim
 * @date 2023.12.06
 */
@Getter
public enum AllValidationRequestURISample {

//	ACCOUNT_USERNAME_EXIST_REQUEST		(AccountUsernameExistRequest.REQUEST_URI,			AccountUsernameExistRequest.USERNAME,		ConrollerParamType.MAP_COMMON_DATA)
	;
	
	public enum ConrollerParamType {
		MAP_COMMON_DATA
		,MAP
		;
	}
	
	private static Map<String, AllValidationRequestURISample> map;

	private String uri;
	private IRequestParam requestParam;
	private ConrollerParamType dataType;
	private AllValidationRequestURISample(String uri, IRequestParam requestParam, ConrollerParamType dataType) {
		this.uri = uri;
		this.requestParam = requestParam;
		this.dataType = dataType;
	}
	
	public static Map<String, AllValidationRequestURISample> getAllValidationRequestURI() {
		if (map == null) {
			map = new HashMap<>();
			for (AllValidationRequestURISample allValidationRequestURI : AllValidationRequestURISample.values()) {
				map.put(allValidationRequestURI.getUri(), allValidationRequestURI);
			}
		}
		return map;
	}
}
