package com.dhkim.prj.admin.common.resource;

import lombok.Data;

@Data
public class Resource {
	
	
	/**
	 * 리소스 ID
	 */
	private String resourceId;
	
	/**
	 * 리소스 이름
	 */
	private String resourceName;
	
	/**
	 * 리소스 URL
	 */
	private String url;
	
	/**
	 * 리소스 권한
	 */
	private String authority;
}
