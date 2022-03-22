package com.dhkim.prj.admin.core.support.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dhkim.prj.admin.core.support.GlobalConstants;
import com.dhkim.prj.admin.login.LoginService;
import com.dhkim.prj.admin.login.LoginUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordInterceptor implements HandlerInterceptor  {
	
	@Autowired
	private LoginService loginService;
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String uri = request.getRequestURI();
		AntPathMatcher matcher = new AntPathMatcher();
		
		log.info("URI: {}", uri);
		
		/**
		 * 로그인 페이지는 넘어간다.
		 */
		if(matcher.match("/admin/login/**", uri)) {
			return true;
		}
		
		/**
		 * 로그인 관련 페이지를 제외한 모든 페이지 접속 시 패스워드 초기화가 'Y'인 경우 패스워드를 강제로 변경해야됨.
		 */
		if(uri.matches(request.getContextPath())) {
			if(request.authenticate(response)) {
				if(!(request.getRequestURI().equals(request.getContextPath() + GlobalConstants.UPDATE_PASSWORD))) {
					// 사용자 ID 확인
					if(request.getRemoteUser() != null) {
						LoginUser user = loginService.findUser(request.getRemoteUser());
						// 패스워드 초기화 여부 확인
						if(user.getPasswordInitYn().equals("Y")) {
							response.sendRedirect(request.getContextPath() + GlobalConstants.UPDATE_PASSWORD + "?new");
							return false;
						}
					}
				}
			}
		}

		return true;
	}
	
}