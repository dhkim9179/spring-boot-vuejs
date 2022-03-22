package com.dhkim.prj.admin.qna;

import lombok.Data;

@Data
public class Qna {
	
	private String qnaSno;
	private String questionCategory;
	private String questionTitle;
	private String questionContent;
	private String createDatetime;
	private String createUserId;
	private String answer;
	private String answerDatetime;
	private String answerUserId;
	
}
