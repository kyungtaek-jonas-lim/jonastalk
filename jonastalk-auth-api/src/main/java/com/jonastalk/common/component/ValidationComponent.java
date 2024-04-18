package com.jonastalk.common.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jonastalk.common.api.field.IRequestParam;
import com.jonastalk.common.consts.EnumApiParamType;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.consts.EnumValidationErrorCode;
import com.jonastalk.common.exception.CustomException;

import lombok.Getter;

/**
 * @name ValidationComponent.java
 * @brief ValidationComponent
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Component
public class ValidationComponent {
	
	/**
	 * @brief BlankValueParam
	 * @author Jonas Lim
	 * @date 2023.10.31
	 */
	@Getter
	public enum BlankValueParam {
		PARAM_NAME	
		,VALIDATION_VALUE	
		;
	}

	/**
	 * @brief Validate API Request Data
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param map
	 * @param requestParam
	 */
	public void validateApiRequestData(Map<String, Object> map, IRequestParam requestParam) {
		
		if (map == null) {
			makeValidationError(EnumErrorCode.ERR002, EnumValidationErrorCode.NO_REQUEST_DATA, null);
		}
		
		for (IRequestParam apiRequestParam : requestParam.getValues()) {
			
			boolean checkValidationValue = false;
			
			// ------
			// Trim (String)
			if (map.get(apiRequestParam.getName()) instanceof String) {
				map.put(apiRequestParam.getName(), ((String)map.get(apiRequestParam.getName())).trim());
			}
			
			// ------
			// Max Length (String)
			if (apiRequestParam.getMaxLength() > 0
					&& map.get(apiRequestParam.getName()) instanceof String) {
				if (((String) map.get(apiRequestParam.getName())).length() > apiRequestParam.getMaxLength()) {
					StringBuffer validationValueStringBuffer = new StringBuffer();
					validationValueStringBuffer.append("(its length)").append("<=").append(apiRequestParam.getMaxLength());
					Map<ValidationComponent.BlankValueParam, String> mapBlankValues = new HashMap<>();
					mapBlankValues.put(ValidationComponent.BlankValueParam.PARAM_NAME, 			apiRequestParam.getName());
					mapBlankValues.put(ValidationComponent.BlankValueParam.VALIDATION_VALUE, 	validationValueStringBuffer.toString());
					makeValidationError(EnumErrorCode.ERR002, EnumValidationErrorCode.INVALID_VALUE, mapBlankValues);
				}
			}
			
			// ------
			// Required
			if (apiRequestParam.isRequired()) {
				if (map.get(apiRequestParam.getName()) == null) {
					Map<ValidationComponent.BlankValueParam, String> mapBlankValues = new HashMap<>();
					mapBlankValues.put(ValidationComponent.BlankValueParam.PARAM_NAME, 			apiRequestParam.getName());
					makeValidationError(EnumErrorCode.ERR002, EnumValidationErrorCode.REQUIRED, mapBlankValues);
				} else if ((String.valueOf(map.get(apiRequestParam.getName()))).isEmpty()) {
					Map<ValidationComponent.BlankValueParam, String> mapBlankValues = new HashMap<>();
					mapBlankValues.put(ValidationComponent.BlankValueParam.PARAM_NAME, 			apiRequestParam.getName());
					makeValidationError(EnumErrorCode.ERR002, EnumValidationErrorCode.REQUIRED, mapBlankValues);
				}
			}
			
			// ------
			// Not Required
			else if ((map.get(apiRequestParam.getName()) == null || String.valueOf(map.get(apiRequestParam.getName())).isEmpty())
						&& apiRequestParam.getDefaultValue() != null) {
				map.put(apiRequestParam.getName(), apiRequestParam.getDefaultValue());
				checkValidationValue = true;
			}
			
			// ------
			// Validation Values
			if (map.get(apiRequestParam.getName()) == null) {
				checkValidationValue = true;
			}
			
			StringBuffer validationValueStringBuffer = new StringBuffer();
			if (!checkValidationValue) {
				Object[] objValidationValues = apiRequestParam.getValidationValues();
				if (objValidationValues != null || EnumApiParamType.BOOLEAN.equals(apiRequestParam.getType())) {
					if (EnumApiParamType.STRING.equals(apiRequestParam.getType())) {
						for (int i = 0 ; i < objValidationValues.length; i++) {
							if (objValidationValues[i] instanceof String) {
								if (((String)map.get(apiRequestParam.getName())).equals((String)objValidationValues[i])) {
									checkValidationValue = true;
									break;
								}
								if (validationValueStringBuffer.length() != 0) {
									validationValueStringBuffer.append(",");
								}
								validationValueStringBuffer.append((String)objValidationValues[i]);
							}
						}
					} else if (EnumApiParamType.BOOLEAN.equals(apiRequestParam.getType())) {
						
						String value = String.valueOf(map.get(apiRequestParam.getName()));
						
						if (value != null && !value.isEmpty()) {
							value = value.toLowerCase();
							if (value.equals("true") || value.equals("false")) {
								checkValidationValue = true;
							}
						}
						if (!checkValidationValue) {
							validationValueStringBuffer.append("true,false");
						}
						
					} else if (EnumApiParamType.INTEGER.equals(apiRequestParam.getType())) {

						long minValue = 0;
						long maxValue = 0;
						for (int i = 0 ; i < objValidationValues.length; i++) {
							if (i == 0 && objValidationValues[i] instanceof Long) {
								minValue = (Long) objValidationValues[i];
							} else if (i == 1 && objValidationValues[i] instanceof Long) {
								maxValue = (Long) objValidationValues[i];
							}
						}
						if ((!(String.valueOf(map.get(apiRequestParam.getName())).isEmpty()))
								&& ((Long.valueOf(String.valueOf(map.get(apiRequestParam.getName())))) >= minValue)
								&& ((Long.valueOf(String.valueOf(map.get(apiRequestParam.getName())))) <= maxValue)) {
							checkValidationValue = true;
						} else {
							validationValueStringBuffer.append(minValue).append("-").append(maxValue);
						}
					}
					
					if (!checkValidationValue) {
						Map<ValidationComponent.BlankValueParam, String> mapBlankValues = new HashMap<>();
						mapBlankValues.put(ValidationComponent.BlankValueParam.PARAM_NAME, 			apiRequestParam.getName());
						mapBlankValues.put(ValidationComponent.BlankValueParam.VALIDATION_VALUE, 	validationValueStringBuffer.toString());
						makeValidationError(EnumErrorCode.ERR002, EnumValidationErrorCode.INVALID_VALUE, mapBlankValues);
					}
				}
			}
		}
	}
	

	/**
	 * @brief Generate Validation Error
	 * @author Jonas Lim
	 * @date 2023.10.31
	 * @param enumErrorCode
	 * @param enumValidationErrorCode
	 * @param blankValues
	 * @throws CustomException
	 */
	private void makeValidationError(EnumErrorCode enumErrorCode, EnumValidationErrorCode enumValidationErrorCode, Map<BlankValueParam, String> blankValues) throws CustomException {

		// ---------------------------
		// No Error Code
		// ---------------------------
		if (enumErrorCode == null) {
			throw new CustomException(EnumErrorCode.ERR001);
		}

		// ---------------------------
		// Error Code
		// ---------------------------
		if (enumValidationErrorCode == null) {
			throw new CustomException(enumErrorCode);
		} else if (enumValidationErrorCode.equals(EnumValidationErrorCode.NO_REQUEST_DATA)) {
			throw new CustomException(enumErrorCode, enumValidationErrorCode.getErrorMessage());
		} else if (enumValidationErrorCode.equals(EnumValidationErrorCode.REQUIRED)) {
			String paramName = "";
			if (blankValues != null && blankValues.get(BlankValueParam.PARAM_NAME) != null) {
				paramName = blankValues.get(BlankValueParam.PARAM_NAME);
			}
			String errorMessage = enumValidationErrorCode.getErrorMessage();
			errorMessage = errorMessage.replaceFirst("\\{\\}", paramName);
			throw new CustomException(enumErrorCode, errorMessage);
			
		} else if (enumValidationErrorCode.equals(EnumValidationErrorCode.INVALID_VALUE)) {
			String paramName = "";
			if (blankValues != null && blankValues.get(BlankValueParam.PARAM_NAME) != null) {
				paramName = blankValues.get(BlankValueParam.PARAM_NAME);
			}
			String errorMessage = enumValidationErrorCode.getErrorMessage();
			errorMessage = errorMessage.replaceFirst("\\{\\}", paramName);
			String validationValue = "";
			if (blankValues != null && blankValues.get(BlankValueParam.VALIDATION_VALUE) != null) {
				validationValue = blankValues.get(BlankValueParam.VALIDATION_VALUE);
			}
			errorMessage = errorMessage.replaceFirst("\\{\\}", validationValue);
			throw new CustomException(enumErrorCode, errorMessage);
		}
		throw new CustomException(enumErrorCode);
	}
}
