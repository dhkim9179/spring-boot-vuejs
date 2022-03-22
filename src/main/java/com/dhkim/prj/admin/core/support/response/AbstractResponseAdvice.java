package com.dhkim.prj.admin.core.support.response;

import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.dhkim.prj.admin.core.support.eatcom.CodeEnum;
import com.dhkim.prj.admin.core.support.eatcom.ExternalSystemException;
import com.dhkim.prj.admin.core.support.eatcom.RestException;
import com.dhkim.prj.admin.core.support.eatcom.WebUtils;

/**
 * <p>Rest 응답 advice</p>
 * <p>Controller 리턴타입이 void인 메소드까지 커버하기 위해 {@link AbstractMappingJacksonResponseBodyAdvice}를 extends 하지 않고 {@link ResponseBodyAdvice}를 implements 함.</p>
 * <p>* 주의 : Controller 리턴타입이 {@link Object}이고 실제 리턴값이 null이면 이 클래스 안 거침.</p>
 * @author 김규남
 */
public abstract class AbstractResponseAdvice implements ResponseBodyAdvice<Object> {

	private MessageSource messageSource;
	
	public AbstractResponseAdvice(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (body instanceof Exception) {
			return generateExceptionResponse((Exception) body);
		}
		
		return generateSuccessResponse(body);
	}

	/**
	 * 성공 {@link Response} 생성
	 * @param body
	 * @return
	 */
	protected Response generateSuccessResponse(Object body) {
		Result result = new Result(
				getSuccessCode().getCode(), 
				messageSource.getMessage(getSuccessCode().getCode(), null, WebUtils.getLocale()), 
				null);
		
		return generateResponse(result, body);
	}
	
	/**
	 * 실패 {@link Response} 생성
	 * @param ex
	 * @return
	 */
	protected Response generateExceptionResponse(Exception ex) {
		CodeEnum code = null;
		Object[] messageArgs = null;
		Object body = null;
		ExternalSystemException externalSystemException = null;
		
		if (ex instanceof RestException) {
			RestException restEx = (RestException) ex;
			code = restEx.getCode();
			messageArgs = restEx.getMessageArgs();
			body = restEx.getBody();
		}
		else if (ex instanceof ExternalSystemException) {
			ExternalSystemException externalEx = (ExternalSystemException) ex;
			code = externalEx.resolveResultCode();
			externalSystemException = externalEx;
		}
		else {
			code = resolveErrorCode(ex);
		}
		
		Result result = new Result(
				code.getCode(),
//				messageSource.getMessage(code.getCode(), messageArgs, null), 
				generateExceptionResponseMessage(code, messageArgs, externalSystemException),
				externalSystemException == null ? null : new ExternalSystemErrorResult(
						externalSystemException.getSystem().getCode(), 
						externalSystemException.getCode(), 
						externalSystemException.getMessage()));
				
		return generateResponse(result, body);
	}
	
	protected String generateExceptionResponseMessage(CodeEnum code, Object[] messageArgs, ExternalSystemException externalSystemException) {
		return messageSource.getMessage(code.getCode(), messageArgs, WebUtils.getLocale());
	}
	
	/**
	 * {@link Response} 생성
	 * @param result
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Response generateResponse(Result result, Object body) {
		if (body instanceof Map) {
			return new Response(result, (Map<String, ?>) body);
		}
		
		return new Response(result, body);
	}
	
	/**
	 * 성공 코드 추출
	 * @return
	 */
	protected abstract CodeEnum getSuccessCode();
	
	/**
	 * 실패 코드 추출({@link RestException}, {@link ExternalSystemException} 일때는 제외)
	 * @return
	 */
	protected abstract CodeEnum resolveErrorCode(Exception ex);
}
