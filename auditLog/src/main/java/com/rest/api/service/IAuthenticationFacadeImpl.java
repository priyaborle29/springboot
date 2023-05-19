package com.rest.api.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rest.api.audit.IAuthenticationFacade;

public class IAuthenticationFacadeImpl implements IAuthenticationFacade {

	@Override
	public String getUserame() {
		
		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
		return loggedInUser.getName();
	}

	@Override
	public String getUserRole() {
		return null;
	}

}
