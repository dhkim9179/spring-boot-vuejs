package com.dhkim.prj.admin.core.support.aspect;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1010)	
public class RequestMappingMethodParameterLoggingAspect {
	
private final ToStringStyle MULTI_LINE_RECURSIVE_STYLE = new MultilineRecursiveToStringStyle();
	
	private boolean reflectionToStringBuilderExcludeNullValues;
	
//	@Before("@within(org.springframework.stereotype.Controller) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//	@Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	@Before("@annotation(org.springframework.web.bind.annotation.RequestMapping) or "
			+ "@annotation(org.springframework.web.bind.annotation.GetMapping) or "
			+ "@annotation(org.springframework.web.bind.annotation.PostMapping) or "
			+ "@annotation(org.springframework.web.bind.annotation.PutMapping) or "
			+ "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public void log(JoinPoint joinPoint) {
		if (log.isDebugEnabled()) {
			Signature signature = joinPoint.getSignature();
			MethodSignature methodSignature = (MethodSignature) signature;
			String[] names = methodSignature.getParameterNames();
			Class<?>[] types = methodSignature.getParameterTypes();
			Object[] args = joinPoint.getArgs();
//			Method method = methodSignature.getMethod();
//			Annotation[][] paramAnnotations = method.getParameterAnnotations();
			
			for (int i = 0; i < args.length; i++) {
				String name = names[i];
				Class<?> type = types[i];
				Object arg = args[i];
//				Annotation[] annotations = paramAnnotations[i];
				String logValue = null;
				
				if (arg != null) {
					if (
							HttpServletRequest.class.isAssignableFrom(type) || 
							HttpServletResponse.class.isAssignableFrom(type) || 
							HttpSession.class.isAssignableFrom(type) ||
							BeanUtils.isSimpleValueType(type)
					) {
						logValue = arg.toString();
					} 
					else if (
							Map.class.isAssignableFrom(type) || 
							ObjectUtils.isArray(type)
					) {
						logValue = ArrayUtils.toString(arg);
					} 
					else {
//						logValue = ReflectionToStringBuilder.toString(arg, ToStringStyle.MULTI_LINE_STYLE);
//						logValue = ReflectionToStringBuilder.toString(arg, MULTI_LINE_RECURSIVE_STYLE);
						try {
							logValue = ReflectionToStringBuilder.toString(arg, MULTI_LINE_RECURSIVE_STYLE, false, false, reflectionToStringBuilderExcludeNullValues, null);
						}
						//드물게 java.lang.StringIndexOutOfBoundsException 발생함. 원인은 모르겠음 ㅠㅠ
						catch (Exception ignore) {
							log.error("", ignore);
							logValue = "[FAILED]";
						}
					}
				}
			
				log.debug("{}. {} : {} = {}", new Object[]{i, name, type.getName(), logValue});		
			}	
		}
	}

	public void setReflectionToStringBuilderExcludeNullValues(boolean reflectionToStringBuilderExcludeNullValues) {
		this.reflectionToStringBuilderExcludeNullValues = reflectionToStringBuilderExcludeNullValues;
	}
	
}
