package com.dhkim.prj.admin.common.code;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class CommoncodeService {
	
	@Inject
	private CommoncodeMapper commoncodeMapper;
	
	public List<Commoncode> getCommoncodeDetailList(String codeId) {
		return commoncodeMapper.getCommoncodeDetailList(codeId);
	}
	
}
