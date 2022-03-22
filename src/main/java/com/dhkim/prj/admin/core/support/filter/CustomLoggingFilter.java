package com.dhkim.prj.admin.core.support.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.isrsal.logging.RequestWrapper;
import com.github.isrsal.logging.ResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoggingFilter extends OncePerRequestFilter {
	
	private final String STOP_WATCH_KEY = getClass().getName() + "_stopWatch";
	
	private boolean requestHeadersLogging = false;
	private boolean requestCookiesLogging = false;
	private boolean requestParametersLogging = false;
	
	private String[] responseContentTypePrefixesExcludeLogging = new String[] {};
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (log.isDebugEnabled()) {
			request = new RequestWrapper(0L, request);
			response = new ResponseWrapper(0L, response);			
		}
		
		if (request.getRequestURI().contains(".ajax") || !request.getRequestURI().contains("webjars")) {
			//start 로깅
			logStart(request);
			
			//
			Exception exception = null;
			try {
				filterChain.doFilter(request, response);	
			}
			catch (Exception ex) {
				exception = ex;
			}
			finally {
				if (logger.isDebugEnabled()) {
					logRequestBody((RequestWrapper) request);
					logResponseBody((ResponseWrapper) response);
				}
			}
			
			//end 로깅
			logEnd(request, response, exception);
		} else {
			filterChain.doFilter(request, response);
		}
		
	}
	
	protected void logStart(HttpServletRequest request) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		request.setAttribute(STOP_WATCH_KEY, stopWatch);
		
		// IP, Session ID, Scheme, Method, URI
		log.info("Connected: [{}][{}][{}][{}][{}]",
				request.getRemoteAddr(),
				request.getSession().getId(),
				request.getScheme(),
				getMethod(request),
				request.getRequestURI()
		);
		
		if (requestHeadersLogging) {
			logHeaders(request);
		}
		
		if (requestCookiesLogging) {
			logCookies(request);
		}
		
		if (requestParametersLogging) {
			logParameters(request);
		}
	}
	
	protected void logHeaders(HttpServletRequest request) {
		for (Enumeration<String> em = request.getHeaderNames(); em.hasMoreElements();) {
			String headerName = em.nextElement();
			log.debug("[Request header] {} = {}", headerName, request.getHeader(headerName));
		}
	}
	
	protected void logCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				log.debug("[Request cookie] name={}, value={}", cookie.getName(), cookie.getValue());
			} 
		} else {
			log.debug("[Request cookie] No cookies");
		}
	}
	
	protected void logParameters(HttpServletRequest request) {
		for (Enumeration<String> em = request.getParameterNames(); em.hasMoreElements();) {
			String parameterName = em.nextElement();
			log.debug("[Request parameter] {} = {}", parameterName, request.getParameter(parameterName));
		}
	}
	
	protected void logRequestBody(RequestWrapper request) throws UnsupportedEncodingException, JsonProcessingException {
		String json = new String(request.toByteArray(), "UTF-8");
		if (json != null && !json.equals("")) {
			if (!isMultipartRequest(request) && !isBinaryContentRequest(request)) {
				log.debug("[Request body]: {}", toPrettyPrintJson(json));
			}
		}
	}
	
	protected void logResponseBody(ResponseWrapper response) throws UnsupportedEncodingException, JsonProcessingException {
		if (!isBinaryContentResponse(response) && !org.apache.commons.lang3.StringUtils.startsWithAny(response.getContentType(), responseContentTypePrefixesExcludeLogging)) {
			log.debug("[Response body]: {}", toPrettyPrintJson(new String(response.toByteArray(), "UTF-8")));
		}
	}
	
	protected void logEnd(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		if (exception != null) {
			log.error(exception.getMessage(), exception);
		}
		
		StopWatch stopWatch = (StopWatch) request.getAttribute(STOP_WATCH_KEY);
		stopWatch.stop();
		
		log.info("Completed: [{}][{}][{}][{}][{}][{}][{}]",
				request.getRemoteAddr(),
				request.getSession().getId(),
				request.getScheme(),
				getMethod(request),
				request.getRequestURI(),
				response.getStatus(),
				stopWatch.toString()
		);
	}
	
	public void setRequestHeadersLogging(boolean requestHeadersLogging) {
		this.requestHeadersLogging = requestHeadersLogging;
	}
	
	public void setRequestCookiesLogging(boolean requestCookiesLogging) {
		this.requestCookiesLogging = requestCookiesLogging;
	}
	
	public void setRequestParametersLogging(boolean requestParametersLogging) {
		this.requestParametersLogging = requestParametersLogging;
	}
	
	public void setResponseContentTypePrefixesExcludeLogging(String... responseContentTypePrefixesExcludeLogging) {
		this.responseContentTypePrefixesExcludeLogging = responseContentTypePrefixesExcludeLogging;
	}
	
	private boolean isBinaryContentRequest(HttpServletRequest request) {
		return isBinaryContent(request.getContentType());
	}
	
	private boolean isBinaryContentResponse(HttpServletResponse response) {
		return isBinaryContent(response.getContentType());
	}
	
	private boolean isBinaryContent(String contentType) {
		return contentType != null && (contentType.startsWith("image") || contentType.startsWith("video") || contentType.startsWith("audio"));
	}
	
	private boolean isMultipartRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && (contentType.startsWith("multipart/form-data"));
	}
	
	private String getMethod(HttpServletRequest request) {
		String method = request.getMethod();
		String paramValue = request.getParameter("_method");
		
		if ( "POST".equals(method) && StringUtils.hasLength(paramValue) ) {
			method = paramValue.toUpperCase();
		}
		
		return method;
	}
	
	private String toPrettyPrintJson(String json) throws JsonProcessingException {
		if (json == null || json.equals("")) {
			return json;
		}
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.readValue(json, Object.class));
	}

}
