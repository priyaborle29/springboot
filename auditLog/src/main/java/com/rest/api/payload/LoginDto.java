package com.rest.api.payload;

import java.io.Serializable;
import java.util.List;

import com.rest.api.entity.User;

//import io.swagger.annotations.ApiModelProperty;

public class LoginDto implements Serializable {

	private static final long serialVersionUID = 1L;

//	@ApiModelProperty(example = "user")
	public String username;
	
//	@ApiModelProperty(example = "password")
	public String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<User> getUsername(String username2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}