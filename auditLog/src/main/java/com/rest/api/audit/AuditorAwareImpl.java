package com.rest.api.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.rest.api.entity.User;
import com.rest.api.service.IAuthenticationFacadeImpl;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		
		
		IAuthenticationFacade iAuthenticationFacade = new IAuthenticationFacadeImpl();
		return Optional.of(iAuthenticationFacade.getUserame());
		
//		Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
//		String username = loggedInUser.getName();
//		
//		return Optional.of(username);
	}

}
