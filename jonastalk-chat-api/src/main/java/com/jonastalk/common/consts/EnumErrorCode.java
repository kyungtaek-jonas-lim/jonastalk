package com.jonastalk.common.consts;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * @name EnumErrorCode.java
 * @brief EnumErrorCode
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Getter
public enum EnumErrorCode {

	ERR001				(HttpStatus.INTERNAL_SERVER_ERROR,		"System error has occured")
	,ERR002				(HttpStatus.BAD_REQUEST,				"Bad request data: {}")
	,ERR003				(HttpStatus.NOT_FOUND,					"Wrong api")
	,ERR004				(HttpStatus.UNAUTHORIZED,				"Authentication token expired")
	,ERR005				(HttpStatus.UNAUTHORIZED,				"Authentication token invalid")
	,ERR006				(HttpStatus.UNAUTHORIZED,				"Access is denied")
	,ERR007				(HttpStatus.UNAUTHORIZED,				"Disabled")
	,ERR008				(HttpStatus.UNAUTHORIZED,				"Wrong password")
	,ERR009				(HttpStatus.BAD_REQUEST,				"User not found with username: {}")
	,ERR010				(HttpStatus.BAD_REQUEST,				"The refresh limit was reached! Please, athenticate again.")
	
	,ERR_CHAT001		(HttpStatus.CONFLICT,					"A chat already exists here. Cannot create another.")
	;
	
	private HttpStatus status;
	private String errorMessage;

	private EnumErrorCode(HttpStatus status, String errorMessage) {
		this.status 			= status;
		this.errorMessage 		= errorMessage;
	}
	
}
