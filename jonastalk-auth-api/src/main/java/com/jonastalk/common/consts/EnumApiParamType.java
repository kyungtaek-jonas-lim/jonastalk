package com.jonastalk.common.consts;

import lombok.Getter;

/**
 * @name EnumApiParamType.java
 * @brief EnumApiParamType
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Getter
public enum EnumApiParamType {

	STRING			("String")
	,INTEGER 		("Integer")
	,BOOLEAN 		("Boolean")
	,DATETIME 		("Datetime")
	,OBJECT			("Object")
	,OBJECT_ARRAY	("ObjectArray")
	;
	
	private String type;

	private EnumApiParamType(String type) {
		this.type 		= type;
	}
}
