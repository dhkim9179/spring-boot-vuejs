package com.dhkim.prj.admin.qna;

import javax.inject.Inject;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhkim.prj.admin.core.support.security.user.User;
import com.google.common.collect.ImmutableMap;

import lombok.Data;

@RestController
@RequestMapping("/qna")
public class QnaController {
	
	@Inject
	private QnaService qnaService;
	
	@GetMapping
	public Object getQnaList(SearchQna qna, Pageable pageable) {
		return new ImmutableMap.Builder<String, Object>()
				.put("qnaList", qnaService.getQnaList(qna, pageable))
				.put("totalCount", qnaService.countQnaList(qna))
				.build();
	}
	
	@GetMapping("/{qnaSno}")
	public Object getQna(@PathVariable String qnaSno) {
		return new ImmutableMap.Builder<String, Object>()
				.put("qna", qnaService.getQna(qnaSno))
				.build();
	}
	
	@PostMapping
	public void registerQna(@RequestBody Qna qna, @AuthenticationPrincipal User user) {
		qnaService.registerQna(qna, user);
	}
	
	@PutMapping("/{qnaSno}")
	public void modifyQna(@PathVariable String qnaSno, @RequestBody Qna qna, @AuthenticationPrincipal User user) {
		qnaService.modifyQna(qnaSno, qna, user);
	}
	
	@Data
	public static class SearchQna {
		private String questionTitle;
		private String answer;
		private String startDate;
		private String endDate;
	}
	
}
