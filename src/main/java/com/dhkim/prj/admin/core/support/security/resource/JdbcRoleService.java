package com.dhkim.prj.admin.core.support.security.resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JdbcRoleService extends JdbcDaoSupport {

    public static final String DEF_RESOURCES_AND_ROLES_QUERY =
    		"select r.resource_pattern, r.pattern_type, r.resource_method, rr.role_id " +
    		"from resources r, role_resources rr " +
    		"where r.resource_id = rr.resource_id " +
    		"order by r.sort_order";
    
    public static final String DEF_ROLE_HIERARCHY_QUERY =
    		"select parent_role_id, child_role_id " +
    		"from AUTHORITY_HIERARCHY";    
    
	private String resourcesAndRolesQuery;
	
	private String roleHierarchyQuery;
	
	private boolean enableRoleHierarchy = true;
	
    public JdbcRoleService(DataSource dataSource) {
    	setDataSource(dataSource);
    	resourcesAndRolesQuery = DEF_RESOURCES_AND_ROLES_QUERY;
    	roleHierarchyQuery = DEF_ROLE_HIERARCHY_QUERY;
    }
	
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getRequestMap() {
    	LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
    	
    	List<ResourceAndRole> resourceAndRoles = getJdbcTemplate().query(resourcesAndRolesQuery, new RowMapper<ResourceAndRole>() {
            public ResourceAndRole mapRow(ResultSet rs, int rowNum) throws SQLException {
                String resourcePattern = rs.getString(1);
                String partternType = rs.getString(2);
                String resourceMethod = rs.getString(3);
                String role = rs.getString(4);
                return new ResourceAndRole(resourcePattern, partternType, resourceMethod, role);
            }
        });
    	
    	if (resourceAndRoles != null) {
    		for (ResourceAndRole rr : resourceAndRoles) {
    			String resourcePattern = rr.getResourcePattern();
    			String patternType = rr.getPatternType();
    			String resourceMethod = rr.getResourceMethod();
    			
    			//RequestMatcher
    			RequestMatcher requestMatcher;
    			if ("regex".equalsIgnoreCase(patternType)) {
    				//1. /admins/?form => \A/admins\/?\?form\Z
    				requestMatcher = new RegexRequestMatcher(resourcePattern, resourceMethod);
    			}
    			else {
    				requestMatcher = new AntPathRequestMatcher(resourcePattern, resourceMethod);
    			}

    			//ConfigAttributes
    			Collection<ConfigAttribute> attributes;
    			if (requestMap.containsKey(requestMatcher)) {
    				attributes = requestMap.get(requestMatcher);
    			}
    			else {
    				attributes = new LinkedList<ConfigAttribute>();
    			}
    			attributes.add(new SecurityConfig(rr.getRole()));

    			//
    			requestMap.put(requestMatcher, attributes);
    		}
    	}
    	
    	return requestMap;
    }
    
    public String getRoleHierarchy() {
    	if (!enableRoleHierarchy) {
    		return "";
    	}

    	List<RoleHierarchy> hierarchies = getJdbcTemplate().query(roleHierarchyQuery, new RowMapper<RoleHierarchy>() {
            public RoleHierarchy mapRow(ResultSet rs, int rowNum) throws SQLException {
                String parentRole = rs.getString(1);
                String childRole = rs.getString(2);
                return new RoleHierarchy(parentRole, childRole);
            }
        });
    	
		StringBuilder sb = new StringBuilder();
    	if (hierarchies != null) {
    		for (RoleHierarchy h : hierarchies) {
    			sb.append(h.getParentRole())
    			.append(" > ")
    			.append(h.getChildRole())
//    			.append(SystemUtils.LINE_SEPARATOR)
    			.append(System.lineSeparator())
    			;
    		}
    	}
    	
    	return sb.toString();
    }
    
	public void setRoleHierarchyQuery(String roleHierarchyQuery) {
		this.roleHierarchyQuery = roleHierarchyQuery;
	}

	public void setResourcesAndRolesQuery(String resourcesAndRolesQuery) {
		this.resourcesAndRolesQuery = resourcesAndRolesQuery;
	}
	
	public void setEnableRoleHierarchy(boolean enableRoleHierarchy) {
		this.enableRoleHierarchy = enableRoleHierarchy;
	}	
	
	//~ Inner class ================================================================================================
	
	private class ResourceAndRole {
		private String resourcePattern;
		private String patternType;
		private String resourceMethod;
		private String role;

		private ResourceAndRole(String resourcePattern, String patternType, String resourceMethod, String role) {
			this.resourcePattern = resourcePattern;
			this.patternType = patternType;
			this.resourceMethod = resourceMethod;
			this.role = role;
		}
		
		public String getResourcePattern() {
			return resourcePattern;
		}
		public String getResourceMethod() {
			return resourceMethod;
		}
		public String getRole() {
			return role;
		}
		public String getPatternType() {
			return patternType;
		}
	}
	
	private class RoleHierarchy {
		private String parentRole;
		private String childRole;

		private RoleHierarchy(String parentRole, String childRole) {
			this.parentRole = parentRole;
			this.childRole = childRole;
		}
		
		public String getParentRole() {
			return parentRole;
		}
		public String getChildRole() {
			return childRole;
		}
	}
}