package com.dhkim.prj.admin.login;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	
	@Inject
	private LoginMapper loginMapper;
	
	@Inject
	private BCryptPasswordEncoder encoder;
	
	public List<LoginUser> findUserIdList(String username, String mobilePhoneNumber) {
		return loginMapper.findUserIdList(username, mobilePhoneNumber); 
	}
	
	public int countUserIdList(String username, String mobilePhoneNumber) {
		return loginMapper.countFindUserId(username, mobilePhoneNumber);
	}
	
	public LoginUser findUser(String userId) {
		return loginMapper.findUser(userId);
	}
	
	public void initPassword(String userId, String mobilePhoneNumber) {
		// 임시 비밀번호 8자리 생성(영문+숫자+특수문자)
		
		// 저장된 이메일로 전송
	}
	
	public int updatePassword(String userId, String password) {
		return loginMapper.updatePassword(userId, encoder.encode(password));
	}
	
}
