package com.dhkim.prj.admin.common.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
	
	@Autowired
	private ResourceMapper mapper;
	
	/**
	 * 리소스 조회
	 * @return
	 */
	public List<Resource> findResourceList(Object authority) {
		return mapper.findResourceList(authority);
	}
}
