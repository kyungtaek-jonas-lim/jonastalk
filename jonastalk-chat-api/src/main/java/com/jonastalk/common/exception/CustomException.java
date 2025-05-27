package com.jonastalk.common.exception;

import org.springframework.http.HttpStatus;

import com.jonastalk.common.consts.EnumErrorCode;

/**
 * @name CustomException.java
 * @brief CustomException
 * @author Jonas Lim
 * @date 2023.10.31
 */
public class CustomException extends RuntimeException {
	
	private static final long serialVersionUID = 8010681602123395812L;
	
	private EnumErrorCode enumErrorCode;
	private String[] extraErrorMessage;
	private Object apiResponseData;
	
	public CustomException(EnumErrorCode enumErrorCode) {
		this.enumErrorCode = enumErrorCode;
	}
	
	public CustomException(EnumErrorCode enumErrorCode, String extraErrorMessage) {
		super();
		this.enumErrorCode 		= enumErrorCode;
		if (extraErrorMessage != null) {
			this.extraErrorMessage 		= new String[1];
			this.extraErrorMessage[0] 	= extraErrorMessage;
		}
	}
	
	public CustomException(EnumErrorCode enumErrorCode, String[] extraErrorMessage) {
		super();
		this.enumErrorCode 		= enumErrorCode;
		this.extraErrorMessage 	= extraErrorMessage;
	}
	
	
	/**
	 * @brief Get Error Code Status
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @return HttpStatus
	 */
	public HttpStatus getStatus() {
		if (enumErrorCode == null) return HttpStatus.INTERNAL_SERVER_ERROR;
		return enumErrorCode.getStatus();
	}

	
	/**
	 * @brief Get Error Message
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @return
	 */
	public String getErrorMessage() {
		String errorMessage = enumErrorCode.getErrorMessage();
		if (extraErrorMessage != null) {
			for (int i = 0 ; i < extraErrorMessage.length; i++) {
				if (!errorMessage.contains("{}")) break;
				if (extraErrorMessage[i] == null) continue;
				errorMessage = errorMessage.replaceFirst("\\{\\}", extraErrorMessage[i]);
			}
		}
		return errorMessage;
	}

	
	/**
	 * @brief Get Error Code
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @return
	 */
	public EnumErrorCode getErrorCode() {
		if (enumErrorCode == null) return EnumErrorCode.ERR001;
		return enumErrorCode;
	}

	
	/**
	 * @brief Get API Response Data
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @return
	 */
	public Object getApiResponseData() {
		if (apiResponseData == null) {
			return "";
		}
		return apiResponseData;
	}

	
	/**
	 * @brief Set API Response Data
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param apiResponseData
	 */
	public void setApiResponseData(Object apiResponseData) {
		this.apiResponseData = apiResponseData;
	}
}

