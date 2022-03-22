package com.dhkim.prj.admin.common.code;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping("/commoncode")
public class CommoncodeController {
	
	@Inject
	private CommoncodeService commoncodeService;
	
	/**
	 * 공통상세코드 조회
	 * @param codeId 공통코드 ID
	 * @return
	 */
	@GetMapping("/{codeId}")
	public Object getCommoncodeDetailList(@PathVariable String codeId) {
		return new ImmutableMap.Builder<String, Object>()
				.put("codeList", commoncodeService.getCommoncodeDetailList(codeId))
				.build();
	}
	
	
}
