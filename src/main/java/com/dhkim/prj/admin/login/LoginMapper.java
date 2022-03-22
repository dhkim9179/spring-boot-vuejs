package com.dhkim.prj.admin.login;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginMapper {
	
	public List<LoginUser> findUserIdList(@Param("username") String username, @Param("mobilePhoneNumber") String mobilePhoneNumber);
	public int countFindUserId(@Param("username") String username, @Param("mobilePhoneNumber") String mobilePhoneNumber);
	public LoginUser findUser(String userId);
	public int updatePassword(String userId, String password);
	
}
