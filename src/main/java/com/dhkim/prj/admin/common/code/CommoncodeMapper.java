package com.dhkim.prj.admin.common.code;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommoncodeMapper {
	
	public List<Commoncode> getCommoncodeDetailList(String codeId);
	
}
