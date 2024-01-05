package com.accounts.household.web.user;

import java.io.Serializable;

import org.springframework.web.context.annotation.SessionScope;

@SessionScope
public class UserManagementForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userId;
	private String userName;
	private String password;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
