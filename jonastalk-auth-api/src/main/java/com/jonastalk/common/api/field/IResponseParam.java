package com.jonastalk.common.api.field;

import com.jonastalk.common.consts.EnumApiParamType;

/**
 * @name IResponseParam.java
 * @brief IResponseParam
 * @author Jonas Lim
 * @date 2023.12.05
 */
public interface IResponseParam {

	String getName();
	String getExplanation();
	EnumApiParamType getType();
}
