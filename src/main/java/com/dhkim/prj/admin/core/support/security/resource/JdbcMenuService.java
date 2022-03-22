package com.dhkim.prj.admin.core.support.security.resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcMenuService extends NamedParameterJdbcDaoSupport implements ApplicationListener<JdbcRoleChangeEvent> {
	
    public static final String DEF_ROLE_MENUS_QUERY =
    		"SELECT DISTINCT RLT.MENU_ID, RLT.PARENT_MENU_ID, RLT.MENU_NAME, RLT.MENU_URL, RLT.MENU_IMAGE "	+ 
    		"FROM MENUS RLT INNER JOIN ( " +
		    "	SELECT " +
		    "		M2.MENU_ID AS M_LV1 , " +
		    "		M3.MENU_ID AS M_LV2 , " +
		    "		M4.MENU_ID AS M_LV3 " +
//		    "		,M5.MENU_ID AS M_LV4 " +
		    "	FROM " +
		    "		(SELECT MENU_ID " +
		    "      		FROM ROLE_RESOURCES A1 INNER JOIN RESOURCES A2 " +
		    "          		ON A1.RESOURCE_ID = A2.RESOURCE_ID AND A1.ROLE_ID IN (:roles) " +
		    "	) M1 " +
		    "	INNER JOIN MENUS M2 ON M1.MENU_ID = M2.MENU_ID " +
		    "  	LEFT JOIN MENUS M3 ON M2.PARENT_MENU_ID = M3.MENU_ID " +
		    "  	LEFT JOIN MENUS M4 ON M3.PARENT_MENU_ID = M4.MENU_ID " +
//		    "  	LEFT JOIN MENUS M5 ON M4.PARENT_MENU_ID = M5.MENU_ID " +
		  	") SUB " +
			"ON RLT.MENU_ID = SUB.M_LV1  " +
			"	OR RLT.MENU_ID = SUB.M_LV2  " +
			"   OR RLT.MENU_ID = SUB.M_LV3  " +
//			"   OR RLT.MENU_ID = SUB.M_LV4  " +
			"ORDER BY MENU_ID ASC";
    public static final String DEF_RESOURCES_AND_MENUS_QUERY = 
    		"select r.resource_pattern, r.resource_method, r.menu_id, m.menu_name, m.menu_url " + 
    		"from resources r, menus m " +
    		"where r.menu_id = m.menu_id " +
    		"order by r.sort_order";
    private String roleMenusQuery;
    private String resourcesAndMenusQuery;
	private RoleHierarchy roleHierarchy;
	private final Map<Collection<GrantedAuthority>, List<Menu>> authorityMenus = new ConcurrentHashMap<Collection<GrantedAuthority>, List<Menu>>();
	private final Map<RequestMatcher, Menu> resourceMenus = new LinkedHashMap<RequestMatcher, Menu>();
	
	public JdbcMenuService(DataSource dataSource, RoleHierarchy roleHierarchy) {
		setDataSource(dataSource);
		this.roleHierarchy = roleHierarchy;
		this.roleMenusQuery = DEF_ROLE_MENUS_QUERY;
		this.resourcesAndMenusQuery = DEF_RESOURCES_AND_MENUS_QUERY;
	}
	
	public void init() {
		getResourceMenus();
	}
	
	
	public List<Menu> getMenus(Collection<GrantedAuthority> authorities) { 
		if (authorityMenus.containsKey(authorities)) {
			return authorityMenus.get(authorities);
		}
		
		List<String> roles = new ArrayList<String>();
		for (GrantedAuthority authority : roleHierarchy.getReachableGrantedAuthorities(authorities)) {			
			roles.add(authority.getAuthority());
		}

		List<Menu> menus = getNamedParameterJdbcTemplate().query(roleMenusQuery, Collections.singletonMap("roles", roles), new RowMapper<Menu>() {
            public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
                String menuId = rs.getString(1);
                String parentMenuId = rs.getString(2);
                String menuName = rs.getString(3);
                String menuUrl = rs.getString(4);
                String menuImage = rs.getString(5);                
                return new Menu(menuId, parentMenuId, menuName, menuUrl, menuImage);
            }
        });
		
		for (Menu menu : menus) {
			menu.setChildren(getChildrenMenus(menu.getMenuId(), menus));
		}
		
		for (ListIterator<Menu> it = menus.listIterator(); it.hasNext();) {
			Menu menu = it.next();
			if (menu.getParentMenuId() != null) {
				it.remove();
			}
		}
		
		authorityMenus.put(authorities, menus);
		
		return menus;
	}
	
	private List<Menu> getChildrenMenus(String myMenuId, List<Menu> menus) {
		List<Menu> children = new ArrayList<>();
		for (Menu menu : menus) {
			if (myMenuId.equals(menu.getParentMenuId())) {
				children.add(menu);
			}
		}
		return children;
	}

	public Menu getCurrentMenu(HttpServletRequest request) {
		for (Map.Entry<RequestMatcher, Menu> entry : resourceMenus.entrySet()) {
			if (entry.getKey().matches(request)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void setRoleMenusQuery(String roleMenusQuery) {
		this.roleMenusQuery = roleMenusQuery;
	}

	public void setResourcesAndMenusQuery(String resourcesAndMenusQuery) {
		this.resourcesAndMenusQuery = resourcesAndMenusQuery;
	}

	@Override
	public void onApplicationEvent(JdbcRoleChangeEvent event) {
		authorityMenus.clear();
		log.info("RoleMenu is reloaded!");
	}
	
	private void getResourceMenus() {
		List<Resource> resources = getJdbcTemplate().query(resourcesAndMenusQuery, new RowMapper<Resource>() {
            public Resource mapRow(ResultSet rs, int rowNum) throws SQLException {
                String resourcePattern = rs.getString(1);
                String resourceMethod = rs.getString(2);
                String menuId = rs.getString(3);
                String menuName = rs.getString(4);
                String menuUrl = rs.getString(5);                
                return new Resource(resourcePattern, resourceMethod, new Menu(menuId, menuName, menuUrl));
            }
        });
		
		if (resources != null) {
			for (Resource r : resources) {
    			RequestMatcher requestMatcher = new AntPathRequestMatcher(r.getResourcePattern(), StringUtils.isBlank(r.getResourceMethod()) ? null : r.getResourceMethod().toUpperCase());
    			Menu menu = r.getMenu();
    			
    			resourceMenus.put(requestMatcher, menu);
			}
		}
	}
	
	//~ Inner class ================================================================================================
	
	private class Resource {
		private String resourcePattern;
		private String resourceMethod;
		private Menu menu;
		
		
		private Resource(String resourcePattern, String resourceMethod, Menu menu) {
			this.resourcePattern = resourcePattern;
			this.resourceMethod = resourceMethod;
			this.menu = menu;
		}

		public Menu getMenu() {
			return menu;
		}
		public String getResourcePattern() {
			return resourcePattern;
		}
		public String getResourceMethod() {
			return resourceMethod;
		}
	}
}
