package com.dhkim.prj.admin.core.support.eatcom;

import org.apache.commons.lang3.ArrayUtils;

public class RestException extends RuntimeException {

	private static final long serialVersionUID = -7708298797497291497L;
	/**
	 * Exception 코드 정보
	 */
	private CodeEnum code;
	/**
	 * Exception 메시지에 추가하고자 하는 arguments
	 */
	private Object[] messageArgs;
	/**
	 * Exception 코드 정보와 같이 내릴 body 정보
	 */
	private Object body;
	
	public RestException(CodeEnum code) {
		this(code, null);
	}
	
	public RestException(CodeEnum code, Object[] messageArgs) {
		this(code, messageArgs, null);
	}

	public RestException(CodeEnum code, Object[] messageArgs, Object body) {
		super("");
		this.code = code;
		this.messageArgs = messageArgs;
		this.body = body;
	}
	
	public CodeEnum getCode() {
		return code;
	}

	public Object[] getMessageArgs() {
		return ArrayUtils.clone(messageArgs);
	}
	
	public Object getBody() {
		return body;
	}
	
}
