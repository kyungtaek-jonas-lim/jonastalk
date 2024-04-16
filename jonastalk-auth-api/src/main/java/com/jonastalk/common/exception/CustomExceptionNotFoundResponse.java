package com.jonastalk.common.exception;

import lombok.Getter;

/**
 * @name CustomExceptionNotFoundResponse.java
 * @brief CustomExceptionNotFoundResponse
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Getter
public class CustomExceptionNotFoundResponse extends CustomExceptionResponse {

    private String requestHeaders;
    private String requestURL;
    private String httpMethod;

    public CustomExceptionNotFoundResponse(int status, String errorMessage, String errorCode,
    		String requestHeaders, String httpMethod, String requestURL){
        super(status, errorMessage, errorCode);
        this.requestHeaders = requestHeaders;
        this.httpMethod 	= httpMethod;
        this.requestURL 	= requestURL;
    }
}

