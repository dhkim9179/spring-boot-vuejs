package com.dhkim.prj.admin.login;

import lombok.Data;

@Data
public class LoginUser {

	private String userId;
	private String password;
	private String mobilePhoneNumber;
	private String passwordInitYn;
	
}
