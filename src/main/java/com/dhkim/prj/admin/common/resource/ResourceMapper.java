package com.dhkim.prj.admin.common.resource;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResourceMapper {
	public List<Resource> findResourceList(Object authority);
}
