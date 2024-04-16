package com.jonastalk.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.jonastalk.common.api.field.CommonParams;
import com.jonastalk.common.component.JonasTimeUtil;

import lombok.Getter;

/**
 * @name CustomExceptionResponse.java
 * @brief CustomExceptionResponse
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Getter
public class CustomExceptionResponse {

    private int status;
    private String errorMessage;
    private String errorCode;
    private String currentDatetime;

    public CustomExceptionResponse(int status, String errorMessage, String errorCode){
        this.status 		= status;
        this.errorMessage 	= errorMessage;
        this.errorCode 		= errorCode;
		currentDatetime	= JonasTimeUtil.convertZonedDateTimeToISO8601String(JonasTimeUtil.getCurrentZonedDateTime(null));
    }

	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>();
		map.put(CommonParams.STATUS.getValue(), status);
		map.put(CommonParams.ERROR_MESSAGE.getValue(), errorMessage);
		map.put(CommonParams.ERROR_CODE.getValue(), errorCode);
		map.put(CommonParams.CURRENT_DATETIME.getValue(), currentDatetime);
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}
    
}

