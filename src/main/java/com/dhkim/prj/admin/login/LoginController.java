package com.dhkim.prj.admin.login;

import javax.inject.Inject;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dhkim.prj.admin.core.support.security.user.User;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Inject
	private LoginService loginService;
	
	/**
	 * 로그인 페이지 이동
	 * @param user
	 * @return
	 */
	@GetMapping
	public String login(@AuthenticationPrincipal User user) {
		if (user != null) {
			return "redirect:/";
		}
		return "login";
	}
	
	/**
	 * 아이디찾기 페이지 이동
	 * @param model
	 * @param username
	 * @param mobilePhoneNumber
	 * @return
	 */
	@GetMapping("/find/id")
	public String findId(Model model, String username, String mobilePhoneNumber) {
		
		if (username != null && mobilePhoneNumber != null) {
			model.addAttribute("userList", loginService.findUserIdList(username, mobilePhoneNumber));
			model.addAttribute("userCount", loginService.countUserIdList(username, mobilePhoneNumber));
		}
		
		return "findId";
	}
	
	/**
	 * 비밀번호찾기 페이지 이동
	 * @return
	 */
	@GetMapping("/find/password")
	public String findPassword() {
		return "findPassword";
	}
	
	/**
	 * 비밀번호 초기화 처리
	 * @param user
	 */
	@PostMapping("/find/password")
	@ResponseBody
	public void findPassword(@RequestBody LoginUser user) {
		loginService.initPassword(user.getUserId(), user.getMobilePhoneNumber());
	}
	
	/**
	 * 비밀번호 업데이트 처리
	 * @param user
	 */
	@PostMapping("/update/password")
	@ResponseBody
	public void updatePassword(@RequestBody LoginUser user) {
		loginService.updatePassword(user.getUserId(), user.getPassword());
	}
	
}
