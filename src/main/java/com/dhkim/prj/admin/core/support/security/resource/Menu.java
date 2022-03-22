package com.dhkim.prj.admin.core.support.security.resource;

import java.util.List;

import lombok.Data;

@Data
public class Menu {

	private String menuId;

	private String parentMenuId;
	
	private String menuName;

	private String menuUrl;

	private String menuImage;

	private List<Menu> children;
	
	public Menu(String menuId, String menuName, String menuUrl) {
		this(menuId, null, menuName, menuUrl, null);
	}
	
	public Menu(String menuId, String parentMenuId, String menuName, String menuUrl, String menuImage) {
		this.menuId = menuId;
		this.parentMenuId = parentMenuId;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.menuImage = menuImage;
	}
}
