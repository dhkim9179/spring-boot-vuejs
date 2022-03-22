package com.dhkim.prj.admin.core.support.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;

public class Response {
	
	public interface ResponseView {}
	
	/**
	 * 
	 */
	@JsonView(ResponseView.class)
	private Result result;
	/**
	 * 
	 */
	@JsonUnwrapped
	@JsonView(ResponseView.class)
	private Object pojoData;
	/**
	 * 
	 */
	private Map<String, ?> mapData;
	
	//json -> object(with jackson)시를 위한 생성자
	public Response() {
		
	}
	
	public Response(Result result, Map<String, ?> mapData) {
		this.result = result;
		this.mapData = mapData;
	}
	
	public Response(Result result, Object pojoData) {
		this.result = result;
		this.pojoData = pojoData;
	}
	
	public Result getResult() {
		return result;
	}
	
	public Object getPojoData() {
		return pojoData;
	}
	
	@JsonAnyGetter
	@JsonView(ResponseView.class)
	public Map<String, ?> getMapData() {
		return mapData;
	}
}