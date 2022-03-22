package com.dhkim.prj.admin.core.support.response;

import com.dhkim.prj.admin.core.support.response.Response.ResponseView;
import com.fasterxml.jackson.annotation.JsonView;

public class ExternalSystemErrorResult {
	/**
	 * 외부시스템
	 */
	@JsonView(ResponseView.class)
//	private CodeEnum system;
	private String system;
	/**
	 * 외부시스템 에러코드
	 */
	@JsonView(ResponseView.class)
	private String code;
	/**
	 * 외부시스템 에러메시지
	 */
	@JsonView(ResponseView.class)
	private String message;
	
	//json -> object(with jackson)시를 위한 생성자
	public ExternalSystemErrorResult() {
		
	}
	
	public ExternalSystemErrorResult(String system, String code, String message) {
		this.system = system;
		this.code = code;
		this.message = message;
	}

	public String getSystem() {
		return system;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
