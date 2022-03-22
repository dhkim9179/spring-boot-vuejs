package com.dhkim.prj.admin.core.support.eatcom;

public abstract class ExternalSystemException extends RuntimeException {

	private static final long serialVersionUID = 1205437117847704965L;

	/**
	 * 외부시스템
	 */
//	private E externalSystem;
	private CodeEnum system;
	/**
	 * 외부시스템 에러코드
	 */
	private String code;
	/**
	 * 외부시스템 exception 정보와 같이 포함하고 싶은 
	 */
//	private Object[] args;
	
//	public ExternalSystemException(CodeEnum system, String code, String message) {
//		this(system, code, message, null);
//	}
	
	public ExternalSystemException(CodeEnum system, String code, String message) {//, Object[] args) {
		super(message);
		this.system = system;
		this.code = code;
//		this.args = args;
	}

	public CodeEnum getSystem() {
		return system;
	}

	public String getCode() {
		return code;
	}

//	public Object[] getArgs() {
////		return args;
//		return ArrayUtils.clone(args);
//	}
	
	/**
	 * 외부시스템의 에러코드에 따른 내부 에러코드를 매핑하여 리턴
	 * @return
	 */
	public abstract CodeEnum resolveResultCode();
	
	/**
	 * 외부시스템의 에러코드와 관계없이 기본적으로 매핑할 내부 에러코드 리턴
	 * @return
	 */
	public abstract CodeEnum getDefaultResultCode();
	
}
