package com.dhkim.prj.admin.core.support.security.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.dhkim.prj.admin.core.support.GlobalConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("login failed...");
		response.sendRedirect(request.getContextPath() + GlobalConstants.LOGIN_URL + "?error");
	}

}
