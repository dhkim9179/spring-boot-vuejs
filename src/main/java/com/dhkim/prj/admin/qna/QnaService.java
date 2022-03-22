package com.dhkim.prj.admin.qna;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dhkim.prj.admin.core.support.security.user.User;
import com.dhkim.prj.admin.qna.QnaController.SearchQna;

@Service
public class QnaService {
	
	@Inject
	private QnaMapper qnaMapper;
	
	public List<Qna> getQnaList(SearchQna searchQna, Pageable pageable) {
		return qnaMapper.getQnaList(searchQna, pageable);
	}
	
	public int countQnaList(SearchQna qna) {
		return qnaMapper.countQnaList(qna);
	}
	
	public Qna getQna(String qnaSno) {
		return qnaMapper.getQna(qnaSno);
	}
	
	public int registerQna(Qna qna, User user) {
		qna.setCreateUserId(user.getUsername());
		return qnaMapper.registerQna(qna);
	}
	
	public int modifyQna(String qnaSno, Qna qna, User user) {
		qna.setQnaSno(qnaSno);
		qna.setAnswerUserId(user.getUsername());
		return qnaMapper.modifyQna(qna);
	}
	
}
