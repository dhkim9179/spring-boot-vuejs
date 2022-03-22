package com.dhkim.prj.admin.core.support.exception;

import org.apache.commons.lang3.ArrayUtils;

import com.dhkim.prj.admin.core.support.response.ResultCode;

public class AdminException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ResultCode resultCode;
	
	private transient Object[] args;
	
	private transient Object additionalData;
	
	public AdminException(ResultCode resultCode) {
		this(resultCode, null);
	}
	
	public AdminException(ResultCode resultCode, Object[] args) {
		this(resultCode, args, null);
	}
	
	public AdminException(ResultCode resultCode, Object[] args, Object additionalData) {
		super(resultCode.getCode());
		this.resultCode = resultCode;
		this.args = args;
		this.additionalData = additionalData;
	}
	
	public ResultCode getResultCode() {
		return resultCode;
	}

	public Object[] getArgs() {
		return ArrayUtils.clone(args);
	}

	public Object getAdditionalData() {
		return additionalData;
	}
	
}
