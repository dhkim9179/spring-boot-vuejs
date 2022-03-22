package com.dhkim.prj.admin.core.support.response;

import com.dhkim.prj.admin.core.support.response.Response.ResponseView;
import com.fasterxml.jackson.annotation.JsonView;

public class Result {
	/**
	 * 
	 */
	//object -> json뿐만 아니라 json -> object에도 사용하기 위해 CodeEnum에서 String 타입으로 변경함
	//json -> object시에 CodeEnum 인스턴스를 생성할 수 없기 때문
	@JsonView(ResponseView.class)
//	private CodeEnum code;
	private String code;
	/**
	 * 
	 */
	@JsonView(ResponseView.class)
	private String message;
	/**
	 * 
	 */
	@JsonView(ResponseView.class)
	private ExternalSystemErrorResult externalError;
	
	//json -> object(with jackson)시를 위한 생성자
	public Result() {
		
	}
	
	public Result(String code, String message, ExternalSystemErrorResult externalError) {
		this.code = code;
		this.message = message;
		this.externalError = externalError;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public ExternalSystemErrorResult getExternalError() {
		return externalError;
	}
	
}
