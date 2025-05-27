package com.jonastalk.common.api.field;

import com.jonastalk.common.consts.EnumApiParamType;

/**
 * @name IRequestParam.java
 * @brief IRequestParam
 * @author Jonas Lim
 * @date 2023.10.31
 */
public interface IRequestParam {

	IRequestParam[] getValues();

	String getName();
	String getExplanation();
	EnumApiParamType getType();
	boolean isRequired();
	int getMaxLength();
	Object getDefaultValue();
	Object[] getValidationValues();
}
