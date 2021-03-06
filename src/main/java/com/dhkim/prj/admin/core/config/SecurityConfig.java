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
			// ????????? ????????? ??????
			.antMatchers(GlobalConstants.LOGIN_URL + "/**").permitAll()
			.anyRequest().authenticated()	
		.and()
		/**
		 * formLogin(): ????????? ??????
		 * loginPage: ????????? ????????? URL
		 * loginPorcessUrl: ????????? ???????????? ????????? ?????? URL
		 * successHandler: ????????? ?????? ??? ?????????
		 * failureHandler: ????????? ?????? ??? ?????????
		 * defaultSuccessUrl??? successHandler??? ?????? ???????????? ?????????.
		 */
			.formLogin()
			.loginPage(GlobalConstants.LOGIN_URL)
			.loginProcessingUrl(GlobalConstants.LOGIN_PROCESS_URL)
			.successHandler(userAuthenticationSuccessHandler())
			.failureHandler(userAuthenticationFailureHandler())
		.and()
			/**
			 * logout(): ???????????? ??????
			 * logoutUrl: ???????????? URL
			 * logoutSuccessHandler: ???????????? ?????? ??? ?????????
			 * logoutSuccessUrl: ???????????? ?????? ??? ???????????? ?????????
			 */
			.logout()
			.logoutUrl(GlobalConstants.LOGOUT_URL)
			.logoutSuccessHandler(userLogoutHandler())
			.logoutSuccessUrl(GlobalConstants.LOGOUT_SUCCESS_URL)
		.and()
			.csrf().disable()
			.sessionManagement()
				/**
				 * ????????? ???????????? ?????? ??????????????? ??????, ?????? ?????? ????????? ????????? ???????????? ??????(migration)?????? ?????? ?????? ?????? ??????
				 */
				.maximumSessions(1)
					.maxSessionsPreventsLogin(false)
					.expiredUrl(GlobalConstants.LOGIN_URL)
			.and()
			;
	
	}
	
	/**
	 * ????????? ?????? ??????
	 * 	1) userDetailsService
	 * 		- ????????? ?????? ??? ?????? ??????
	 * 
	 * 	2) passwordEncoder
	 * 		- ????????? ???????????? ????????????
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
