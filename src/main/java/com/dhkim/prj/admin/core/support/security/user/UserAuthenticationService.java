package com.dhkim.prj.admin.core.support.security.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class UserAuthenticationService extends JdbcUserDetailsManager {
	
	private String language;
	
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(getUsersByUsernameQuery(), this::mapToUser, username);
	}
	
	@Override
	protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery,
			List<GrantedAuthority> combinedAuthorities) {
		return new User(userFromUserQuery.getUsername(), userFromUserQuery.getPassword(), this.language, combinedAuthorities);
	}

	private UserDetails mapToUser(ResultSet rs, int rowNum) throws SQLException {
		String userName = rs.getString(1);
		String password = rs.getString(2);	
		this.language = rs.getString(3);
		return new User(userName, password, this.language, AuthorityUtils.NO_AUTHORITIES);
	}

}
