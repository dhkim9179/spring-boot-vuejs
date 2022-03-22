package com.dhkim.prj.admin.core.support.eatcom;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public abstract class WebUtils {

	private static Logger logger = LoggerFactory.getLogger(WebUtils.class);
	
	/**
	 * 현재 {@link HttpServletRequest}를 가져온다.
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	}
	
	/**
	 * 현재 {@link HttpServletResponse}를 가져온다.
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
	}
	
	/**
	 * 주어진 {@link HttpServletRequest}의 헤더 정보를 출력한다.
	 * @param request
	 */
	public static void printRequestHeaders(HttpServletRequest request) {
		for (Enumeration<String> em = request.getHeaderNames(); em.hasMoreElements();) {
			String headerName = em.nextElement();
			logger.debug("[Request header] {} = {}", headerName, request.getHeader(headerName));
		}
	}

	/**
	 * 주어진 {@link HttpServletRequest}의 쿠키 정보를 출력한다.
	 * @param request
	 */
	public static void printRequestCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		
		if (ArrayUtils.isEmpty(cookies)) {
			logger.debug("[Request cookie] No cookies.");
		}
		else {
			for (Cookie cookie : cookies) {
				logger.debug("[Request cookie] {} = {}", cookie.getName(), cookie.getValue());
			}
		}
	}
	
	/**
	 * 주어진 {@link HttpServletRequest}의 파라미터 정보를 출력한다.
	 * @param request
	 */
	public static void printRequestParams(HttpServletRequest request) {
		for (Enumeration<String> em = request.getParameterNames(); em.hasMoreElements();) {
			String paramName = em.nextElement();
			logger.debug("[Request param] {} = {}", paramName, request.getParameter(paramName));
		}
	}
	
	/**
	 * 주어진 {@link HttpServletResponse}의 헤더 정보를 출력한다.
	 * @param response
	 */
	public static void printResponseHeaders(HttpServletResponse response) {
		for (String headerName : response.getHeaderNames()) {
			logger.debug("[Response header] {} = {}", headerName, response.getHeader(headerName));
		}
	}
	
	/**
	 * 현재 {@link HttpServletRequest}의 {@link Locale}을 가져온다.
	 * @return
	 */
	public static Locale getLocale() {
		return RequestContextUtils.getLocale(getRequest());
	}
	
	/**
	 * <p>클라이언트 실제 IP 주소 정보를 추출한다.(가능하면)</p>
	 * @return
	 */
	public static String getRealRemoteAddrIfPossible() {
		return getRealRemoteAddrIfPossible(getRequest());
	}
	
	/**
	 * Full context path 정보를 추출한다.
	 * @return (예) http://eatcom.com:8080/common
	 */
	public static String getFullContextPath() {
		//1.
//		HttpServletRequest request = getRequest();
//		
//		return new StringBuilder()
//				.append(request.getScheme())
//				.append("://")
//				.append(request.getServerName())
//				.append(":")
//				.append(request.getServerPort())
//				.append(request.getContextPath())
//				.toString();
		//2.
		//사용법 예제
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	}
	
	/**
	 * <p>클라이언트 실제 IP 주소 정보를 추출한다.(가능하면)</p>
	 * https://www.lesstif.com/pages/viewpage.action?pageId=20775886 참조
	 * @param request
	 * @return
	 */
	private static String getRealRemoteAddrIfPossible(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_CLIENT_IP"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
		} 
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
			ip = request.getRemoteAddr(); 
		}
		return ip;
	}
}
