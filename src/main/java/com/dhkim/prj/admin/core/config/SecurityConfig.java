package com.dhkim.prj.admin.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.support.ServletContextAttributeExporter;

import com.dhkim.prj.admin.core.support.GlobalConstants;
import com.dhkim.prj.admin.core.support.security.resource.JdbcFilterInvocationSecurityMetadataSource;
import com.dhkim.prj.admin.core.support.security.resource.JdbcFilterSecurityInterceptor;
import com.dhkim.prj.admin.core.support.security.resource.JdbcMenuService;
import com.dhkim.prj.admin.core.support.security.resource.JdbcRoleHierarchyImpl;
import com.dhkim.prj.admin.core.support.security.resource.JdbcRoleService;
import com.dhkim.prj.admin.core.support.security.user.UserAuthenticationFailureHandler;
import com.dhkim.prj.admin.core.support.security.user.UserAuthenticationService;
import com.dhkim.prj.admin.core.support.security.user.UserAuthenticationSuccessHandler;
import com.dhkim.prj.admin.core.support.security.user.UserLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Inject
	private DataSource dataSource;
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/images/**", 
				"/css/**", 
				"/webjars/**", 
				"/js/**", 
				"/vue/**",
				"/html/**",
				"/font/**",
				"/favicon.*",
				"/error");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			// 사용자 페이지 허용
			.antMatchers(GlobalConstants.LOGIN_URL + "/**").permitAll()
			.anyRequest().authenticated()	
		.and()
		/**
		 * formLogin(): 로그인 설정
		 * loginPage: 로그인 페이지 URL
		 * loginPorcessUrl: 스프링 시큐리티 로그인 처리 URL
		 * successHandler: 로그인 성공 시 핸들러
		 * failureHandler: 로그인 실패 시 핸들러
		 * defaultSuccessUrl과 successHandler는 같이 사용하지 않는다.
		 */
			.formLogin()
			.loginPage(GlobalConstants.LOGIN_URL)
			.loginProcessingUrl(GlobalConstants.LOGIN_PROCESS_URL)
			.successHandler(userAuthenticationSuccessHandler())
			.failureHandler(userAuthenticationFailureHandler())
		.and()
			/**
			 * logout(): 로그아웃 설정
			 * logoutUrl: 로그아웃 URL
			 * logoutSuccessHandler: 로그아웃 성공 시 핸들러
			 * logoutSuccessUrl: 로그아웃 성공 시 이동하는 페이지
			 */
			.logout()
			.logoutUrl(GlobalConstants.LOGOUT_URL)
			.logoutSuccessHandler(userLogoutHandler())
			.logoutSuccessUrl(GlobalConstants.LOGOUT_SUCCESS_URL)
		.and()
			.csrf().disable()
			.sessionManagement()
				/**
				 * 동일한 계정으로 다시 로그인하는 경우, 이전 세션 정보를 새로운 세션으로 이관(migration)하고 이전 세션 정보 삭제
				 */
				.maximumSessions(1)
					.maxSessionsPreventsLogin(false)
					.expiredUrl(GlobalConstants.LOGIN_URL)
			.and()
			;
	
	}
	
	/**
	 * 사용자 인증 정의
	 * 	1) userDetailsService
	 * 		- 사용자 인증 및 권한 확인
	 * 
	 * 	2) passwordEncoder
	 * 		- 사용자 비밀번호 암복호화
	 * @param auth
	 * @throws Exception
	 */
	@Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth
         .userDetailsService(userDetailsService())
         .passwordEncoder(new BCryptPasswordEncoder())
        ;
    }

	@Override
	protected UserDetailsService userDetailsService() {
		UserAuthenticationService userDetailsService = new UserAuthenticationService();
		userDetailsService.setDataSource(dataSource);
		userDetailsService.setUsersByUsernameQuery(
	            "SELECT USER_ID, USER_PASSWORD, LANGUAGE " +
	            "FROM USER " +
	            "WHERE USER_ID = ?"	
		);
		userDetailsService.setAuthoritiesByUsernameQuery(
				"SELECT ua.USER_ID, ua.AUTHORITY_ID " +
			    "FROM USER_AUTHORITY ua, USER u " +
			    "WHERE u.USER_ID = ua.USER_ID " +
			    "AND u.USER_ID = ?"
		);
		
		return userDetailsService;
	}	
	
	@Bean
	public UserAuthenticationFailureHandler userAuthenticationFailureHandler() {
		return new UserAuthenticationFailureHandler();
	}
	
	@Bean
	public UserAuthenticationSuccessHandler userAuthenticationSuccessHandler() {
		UserAuthenticationSuccessHandler userAuthenticationSuccessHandler =  new UserAuthenticationSuccessHandler();
		userAuthenticationSuccessHandler.setDefaultTargetUrl(GlobalConstants.LOGIN_SUCCESS_URL);
		return userAuthenticationSuccessHandler;
	}
	
	@Bean
	public UserLogoutHandler userLogoutHandler() {
		return new UserLogoutHandler();
	}
	
	@Bean
	public JdbcRoleService jdbcRoleService() {
		JdbcRoleService jdbcRoleService = new JdbcRoleService(dataSource);
		jdbcRoleService.setEnableRoleHierarchy(enableRoleHierarchy());
		if (getResourcesAndRolesQuery() != null) jdbcRoleService.setResourcesAndRolesQuery(getResourcesAndRolesQuery());
		if (getRoleHierarchyQuery() != null) jdbcRoleService.setRoleHierarchyQuery(getRoleHierarchyQuery());
		
		return jdbcRoleService;
	}
	
	@Bean
	public JdbcRoleHierarchyImpl jdbcRoleHierarchy() {
		return new JdbcRoleHierarchyImpl(jdbcRoleService());
	}
	
	@Bean
	public JdbcFilterInvocationSecurityMetadataSource jdbcFilterInvocationSecurityMetadataSource() {
		return new JdbcFilterInvocationSecurityMetadataSource(jdbcRoleService());
	}
	
	@Bean
	public JdbcFilterSecurityInterceptor jdbcFilterSecurityInterceptor() throws Exception {
		//AccessDecisionManager
		RoleVoter accessDecisionVoter = new RoleHierarchyVoter(jdbcRoleHierarchy());
		if (getRolePrefix() != null) accessDecisionVoter.setRolePrefix(getRolePrefix());

		List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
		accessDecisionVoters.add(accessDecisionVoter);
		
		AffirmativeBased accessDecisionManager = new AffirmativeBased(accessDecisionVoters);

		//FilterSecurityInterceptor
		JdbcFilterSecurityInterceptor filter = new JdbcFilterSecurityInterceptor();
		filter.setSecurityMetadataSource(jdbcFilterInvocationSecurityMetadataSource());
		filter.setAccessDecisionManager(accessDecisionManager);
		filter.setAuthenticationManager(super.authenticationManagerBean());
		
		return filter;
	}
	
	@Bean(initMethod = "init")
	public JdbcMenuService jdbcMenuService() {
		JdbcMenuService menuService = new JdbcMenuService(dataSource, jdbcRoleHierarchy());
		if (getRoleMenusQuery() != null) menuService.setRoleMenusQuery(getRoleMenusQuery());
		if (getResourcesAndMenusQuery() != null) menuService.setResourcesAndMenusQuery(getResourcesAndMenusQuery());
		
		return menuService;
	}
	
	@Bean
	public ServletContextAttributeExporter servletContextAttributeExporter(ServletContext servletContext) {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("menuService", jdbcMenuService());
		
		ServletContextAttributeExporter exporter = new ServletContextAttributeExporter();
		exporter.setAttributes(attributes);
		
		return exporter;
	}
	
	protected String getResourcesAndRolesQuery() {
		return "SELECT R.RESOURCE_PATTERN, R.RESOURCE_PATTERN_TYPE, R.RESOURCE_HTTP_METHOD, AR.AUTHORITY_ID " +
				"FROM RESOURCES R, AUTHORITY_RESOURCES AR " +
				"WHERE R.RESOURCE_ID = AR.RESOURCE_ID";
	}
	
	protected String getRoleHierarchyQuery() {
		return null;
	}
	
	protected String getRoleMenusQuery() {
		return "SELECT DISTINCT RLT.MENU_ID, RLT.MENU_PARENT_ID, RLT.MENU_NAME, RLT.MENU_URL, RLT.MENU_ORDER " +
				"FROM MENU RLT INNER JOIN ( " + 
						"SELECT " + 
							 "M2.MENU_ID AS M_LV1 " + 
					        ",M3.MENU_ID AS M_LV2 " + 
						"FROM " +
							"(SELECT MENU_ID "+
								"FROM AUTHORITY_RESOURCES A1 INNER JOIN RESOURCES A2 "+
									"ON A1.RESOURCE_ID = A2.RESOURCE_ID AND A1.AUTHORITY_ID IN (:roles) " +
							") M1 " +
							"INNER JOIN MENU M2 ON M1.MENU_ID = M2.MENU_ID " +
							"LEFT  JOIN MENU M3 ON M2.MENU_PARENT_ID = M3.MENU_ID " +
						") SUB " +
					    "ON RLT.MENU_ID = SUB.M_LV1 " +
					    "OR RLT.MENU_ID = SUB.M_LV2 " +
					"WHERE RLT.USE_YN = 'Y'" + 
					"ORDER BY MENU_ID ASC";
	}
	
	protected String getResourcesAndMenusQuery() {
		return "SELECT R.RESOURCE_PATTERN, R.RESOURCE_HTTP_METHOD, R.MENU_ID, M.MENU_NAME, M.MENU_URL " +
				"FROM RESOURCES R, MENU M " +
				"WHERE R.MENU_ID = M.MENU_ID";
	}
	
	protected String getRolePrefix() {
		return null;
	}
	
	protected boolean enableRoleHierarchy() {
		return true;
	}

}
