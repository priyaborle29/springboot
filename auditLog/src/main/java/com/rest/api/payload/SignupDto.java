package com.rest.api.payload;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
	
	private String email;
    private String username;
    private String password;
    private String confirmPass;

}
