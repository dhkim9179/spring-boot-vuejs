package com.dhkim.prj.admin.core.support.security.resource;

import org.springframework.context.ApplicationListener;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcRoleHierarchyImpl extends RoleHierarchyImpl implements ApplicationListener<JdbcRoleChangeEvent> {

	private JdbcRoleService jdbcResourcesAndRolesService;
	
	public JdbcRoleHierarchyImpl(JdbcRoleService jdbcResourcesAndRolesService) {
		super.setHierarchy(jdbcResourcesAndRolesService.getRoleHierarchy());
		this.jdbcResourcesAndRolesService = jdbcResourcesAndRolesService;
	}
	
	@Override
	public void onApplicationEvent(JdbcRoleChangeEvent event) {
		super.setHierarchy(jdbcResourcesAndRolesService.getRoleHierarchy());
		
		log.info("RoleHierarchy is reloaded!");
	}
}
