package com.dhkim.prj.admin.core.support.response;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhkim.prj.admin.core.support.eatcom.CodeEnum;
import com.dhkim.prj.admin.core.support.eatcom.RestException;
import com.dhkim.prj.admin.core.support.exception.AdminException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ResponseAdvice extends AbstractResponseAdvice {
	
	@Inject
	public ResponseAdvice(MessageSource messageSource) {
		super(messageSource);
	}

	
	@ExceptionHandler
	@ResponseBody
	public Object handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		return ex;
	}
	
	@Override
	protected CodeEnum getSuccessCode() {
		return ResultCode.SUCCESS;
	}

	@Override
	protected CodeEnum resolveErrorCode(Exception ex) {
		if (ex instanceof MissingServletRequestParameterException || ex instanceof BindException || ex instanceof IllegalArgumentException) {
			return ResultCode.BAD_REQUEST;
		}
		else if (ex instanceof IllegalStateException || ex instanceof HttpRequestMethodNotSupportedException) {
			return ResultCode.BAD_REQUEST;
		}
		else if (ex instanceof RestException) {
			return (ResultCode) ((RestException) ex).getCode();
		}	
		else if( ex instanceof AdminException) {
			return (ResultCode) ((AdminException) ex).getResultCode();
		}
		else {
			return ResultCode.UNKNOWN_ERROR;
		}
	}
}