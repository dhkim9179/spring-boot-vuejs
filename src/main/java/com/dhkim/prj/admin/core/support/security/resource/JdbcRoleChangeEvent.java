package com.dhkim.prj.admin.core.support.security.resource;

import org.springframework.context.ApplicationEvent;

public class JdbcRoleChangeEvent extends ApplicationEvent {

	private static final long serialVersionUID = 5966712273188614771L;

	public JdbcRoleChangeEvent(Object source) {
		super(source);
	}
}
