package com.dhkim.prj.admin.core.support.response;

import com.dhkim.prj.admin.core.support.eatcom.CodeEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResultCode implements CodeEnum {
	
	/**
	 * 0000
	 */
	SUCCESS("0000"),
	//http 표준 코드 관련 ===========================================================================
	/**
	 * 0400
	 */
	BAD_REQUEST("0400"),
	/**
	 * 0401
	 */
	UNAUTHORIZED("0401"),
	/**
	 * 0403
	 */
	HTTP_FORBIDDEN("403"),
	FORBIDDEN("0403"),
	
	/**
	 * 9999
	 */
	UNKNOWN_ERROR("9999")
	
	;
	
	private String code;
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
