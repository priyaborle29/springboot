package com.rest.api.controller;

import org.springframework.security.core.Authentication;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.Registration.Signing;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entity.ERole;
import com.rest.api.entity.Role;
import com.rest.api.entity.User;
import com.rest.api.jwt.JwtUtility;
//import com.rest.api.jwt.Profiles;
import com.rest.api.payload.JwtResponse;
import com.rest.api.payload.LoginDto;
import com.rest.api.payload.SignupDto;
import com.rest.api.repository.RoleRepository;
import com.rest.api.repository.UserRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

//@Profile(Profiles.JWT_AUTH)
@Tag(description = "API", name = "Auth Controller")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@PostMapping("/hello")
	public String hello()
	{
		return "hello"+"\n"+"byyy";
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupDto signupDto)
	{
//		to check username and email in DB already exists or not
		if(userRepository.existsByUsername(signupDto.getUsername()))
		{
			return new ResponseEntity<>("Oops, Username is already taken...!",HttpStatus.BAD_REQUEST);
		}
		
		if(userRepository.existsByEmail(signupDto.getEmail())) 
		{
			return new ResponseEntity<>("Oops, Email ID is already taken...!",HttpStatus.BAD_REQUEST);
		}
		
		User user = new User();
		user.setEmail(signupDto.getEmail());
		user.setUsername(signupDto.getUsername());
		user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
		user.setConfirmPass(passwordEncoder.encode(signupDto.getConfirmPass()));
		
		Role role = roleRepository.findByName(ERole.ROLE_USER).get();	
		user.setRole(Collections.singleton(role));
		
		if(!signupDto.getPassword().equals(signupDto.getConfirmPass()))
			return new ResponseEntity<>("Oops, password is not matching...",HttpStatus.BAD_REQUEST);

		if(passwordEncoder.matches(signupDto.getPassword(), passwordEncoder.encode(signupDto.getConfirmPass())))
			userRepository.save(user);	
		
		else
			return new ResponseEntity<>("Oops, password is not matching...",HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>("User Registered Successfully....",HttpStatus.OK);
	}
	
	@PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginDto loginDto)
    {
    	try
    	{
    		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
    				loginDto.getUsername(), loginDto.getPassword()));

    		SecurityContextHolder.getContext().setAuthentication(authentication);
    		
    		User user = userRepository.findByUsername(loginDto.getUsername());
    		final String token = jwtUtility.generate(user);
    		user.setToken(token);
    		return ResponseEntity.ok(new JwtResponse(token, loginDto.getUsername())); 
    		
    	}
    	catch(BadCredentialsException ex) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    	}
    	
    	//return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }
	
}
