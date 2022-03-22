package com.dhkim.prj.admin.qna;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.dhkim.prj.admin.qna.QnaController.SearchQna;

@Mapper
public interface QnaMapper {
	
	public List<Qna> getQnaList(@Param("sc") SearchQna qna, @Param("pageable") Pageable pageable);
	public int countQnaList(@Param("sc") SearchQna qna);
	public Qna getQna(String qnaSno);
	public int registerQna(Qna qna);
	public int modifyQna(Qna qna);
	
}
