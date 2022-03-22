package com.dhkim.prj.admin.main;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhkim.prj.admin.common.code.CommoncodeService;
import com.dhkim.prj.admin.common.resource.ResourceService;
import com.dhkim.prj.admin.core.support.security.resource.JdbcMenuService;
import com.dhkim.prj.admin.core.support.security.user.User;
import com.google.common.collect.ImmutableMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {
	
	@Inject
	private JdbcMenuService jdbcMenuService;
	
	@Inject
	private CommoncodeService commoncodeService;
	
	@Inject
	private ResourceService resourceService;
	
	@GetMapping
	public String main(@AuthenticationPrincipal User user) {
		
		if (user == null) {
			return "redirect:/login";
		}
		
		log.info("username: {}", user.getUsername());
		log.info("getPassword: {}", user.getPassword());
		log.info("getAuthorities: {}", user.getAuthorities());
		
		return "main";
		
	}
	
	/**
	 * 권한에 따른 메뉴 조회
	 * @param user
	 * @return
	 */
	@GetMapping("menus")
	@ResponseBody
	public Object readMenus(@AuthenticationPrincipal User user) {
		return new ImmutableMap.Builder<String, Object>()
				.put("menus", jdbcMenuService.getMenus(user.getAuthorities()))
				.build();
	}
	
	/**
	 * 언어 조회
	 * @param code
	 * @return
	 */
	@GetMapping("languages")
	@ResponseBody
	// 강제로 Http status를 리턴
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object readLanguages() {
		return new ImmutableMap.Builder<String, Object>()
				.put("languages", commoncodeService.getCommoncodeDetailList("LANGUAGE"))
				.build();
	}
	
	/**
	 * 에러페이지
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("error")
	public String getError(ModelMap model, HttpServletRequest request) {
		log.debug("error");
		model.addAttribute("status", request.getParameter("code"));
		return "error";
	}
	
	/*
	 * 리소스 조회
	 */
	@GetMapping("resources")
	@ResponseBody
	public Object readResources(@AuthenticationPrincipal User user) {
		return new ImmutableMap.Builder<String, Object>()
				.put("resources", resourceService.findResourceList(user.getAuthorities().toArray()[0]))
				.build();
	}
}
