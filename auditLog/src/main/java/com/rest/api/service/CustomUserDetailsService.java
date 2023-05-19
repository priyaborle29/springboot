package com.rest.api.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rest.api.entity.Role;
import com.rest.api.entity.User;
import com.rest.api.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) 
	{
		this.userRepository=userRepository;
	}
	
	private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		if(username == null)
		{
			throw new UsernameNotFoundException(username);
		}
	
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
	}
	
//	public UserDetails findByFirstName2(String firstName) throws UsernameNotFoundException
//	{
//		User user = (User) userRepository.findByFirstName(firstName);
//		if(firstName == null)
//		{
//			throw new UsernameNotFoundException(firstName);
//		}
//		return new org.springframework.security.core.userdetails.User(user.getFirstName(),user.getUsername() , null);
//	}

	
	public List<User> findAll()
	{
		return userRepository.findAll();
	}
	
}
