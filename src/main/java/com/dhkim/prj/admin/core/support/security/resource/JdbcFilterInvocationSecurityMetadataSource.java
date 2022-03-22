package com.dhkim.prj.admin.core.support.security.resource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, ApplicationListener<JdbcRoleChangeEvent> {

	private JdbcRoleService jdbcRoleService;
	
	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;
	
	public JdbcFilterInvocationSecurityMetadataSource(JdbcRoleService jdbcRoleService) {
		// 리소스 권한 
		this.requestMap = jdbcRoleService.getRequestMap();
		this.jdbcRoleService = jdbcRoleService;
	}
	
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) {
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			if (entry.getKey().matches(request)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void onApplicationEvent(JdbcRoleChangeEvent event) {
		requestMap.clear();
		
		Map<RequestMatcher, Collection<ConfigAttribute>> reoladedRequestMap = jdbcRoleService.getRequestMap();
		if (reoladedRequestMap != null) {
			for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : reoladedRequestMap.entrySet()) {
				requestMap.put(entry.getKey(), entry.getValue());
			}	
		}
		
		log.info("FilterInvocationSecurityMetadataSource is reloaded!");
	}

}
