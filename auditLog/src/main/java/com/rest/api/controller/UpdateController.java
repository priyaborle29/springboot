package com.rest.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entity.Address;
import com.rest.api.entity.User;
import com.rest.api.entity.UserDetails;
import com.rest.api.exception.ResourceNotFoundException;
import com.rest.api.jwt.JwtUtility;
import com.rest.api.payload.AddressDto;
import com.rest.api.payload.UserDetailsRequest;
import com.rest.api.payload.UserDetailsResponse;
import com.rest.api.repository.AddressRepository;
import com.rest.api.repository.UserDetailsRepository;
import com.rest.api.repository.UserRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(description = "API", name = "Test Controller")
@RestController
@RequestMapping("/update")
public class UpdateController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtility jwtUtility;

	@GetMapping("/demo")
	public String demo() {
		return "yeap, demo is working...";
	}

	@PostMapping("/addDetails")
	public ResponseEntity<Object> addUserDetails(@RequestBody UserDetailsRequest userDetails) {
		
		if(userRepository.existsByEmail(userDetails.getEmail()))
		{	
			if(Boolean.TRUE.equals(userDetailsRepository.existsByEmail(userDetails.getEmail())))
				return new ResponseEntity<>("Oops, Email already exists...!",HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>("Sorry, Email is invalid...!",HttpStatus.BAD_REQUEST);
		
		
		UserDetails addUserDetails = new UserDetails();
		addUserDetails.setEmail(userDetails.getEmail());
		addUserDetails.setMobNum(userDetails.getMobNum());
		addUserDetails.setAddress(userDetails.getAddress());
		addUserDetails.setState(userDetails.getState());
		addUserDetails.setCountry(userDetails.getCountry());
		userDetailsRepository.save(addUserDetails);
		return ResponseEntity.ok(addUserDetails);
	}
	
	@PostMapping("/addAddress/{id}")
	public ResponseEntity<Object> addUserAddress(@PathVariable long id, @RequestBody AddressDto addressDto) {
		if (!userDetailsRepository.existsById(id)) {
			System.out.println(id);
			return new ResponseEntity<>("Oops, No user with this id....!", HttpStatus.BAD_REQUEST);
		}
		
		Address address = new Address();
		
		address.setDistrict(addressDto.getDistrict());
		address.setState(addressDto.getState());
		address.setCountry(addressDto.getCountry());
		address.setPinCode(addressDto.getPinCode());
		
		addressRepository.save(address);
		return ResponseEntity.ok(address);
	}
	
	@PutMapping("/updateAddress/{id}")
	public ResponseEntity<?> updateUserAddress(@PathVariable long id, @RequestBody AddressDto addressDto) {
		Address address = addressRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exists with id" + id));

//		if (address.isDeleted()) {
//			return new ResponseEntity<>("User is already Deleted", HttpStatus.OK);
//		}

		address.setDistrict(addressDto.getDistrict());
		address.setState(addressDto.getState());
		address.setCountry(addressDto.getCountry());
		address.setPinCode(addressDto.getPinCode());

		addressRepository.save(address);

		return ResponseEntity.ok(address);

	}

	@PutMapping("/updateDetails/{id}")
	public ResponseEntity<?> updateUserDetails(@PathVariable long id, @RequestBody UserDetailsRequest userDetails) {
		UserDetails updateUser = userDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exists with id" + id));

		if (updateUser.isDeleted()) {
			return new ResponseEntity<>("User is already Deleted", HttpStatus.OK);
		}

		updateUser.setEmail(userDetails.getEmail());
		updateUser.setMobNum(userDetails.getMobNum());
		updateUser.setAddress(userDetails.getAddress());
		updateUser.setState(userDetails.getState());
		updateUser.setCountry(userDetails.getCountry());

		userDetailsRepository.save(updateUser);

		return ResponseEntity.ok(updateUser);
	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User userDetails,
			HttpServletRequest request) {
		User updateUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not exists with id" + id));

		if (updateUser.isDeleted() == true) {
			return new ResponseEntity<>("Sorry, User is already Deleted...", HttpStatus.OK);
		}

		updateUser.setEmail(userDetails.getEmail());
		updateUser.setUsername(userDetails.getUsername());
		updateUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
		updateUser.setConfirmPass(passwordEncoder.encode(userDetails.getConfirmPass()));

		final String authHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authHeader != null && authHeader.startsWith("Bearer")) {
			jwt = authHeader.substring(7);
			username = jwtUtility.getUsernameFromToken(jwt);
		}

		if (updateUser.getUsername().equals(username)) {
			updateUser.setUsername(userDetails.getUsername());
			userRepository.save(updateUser);
		} else {
			return new ResponseEntity<>("Oops, Invalid User...", HttpStatus.BAD_GATEWAY);
		}
		return ResponseEntity.ok(updateUser);

	}

	@PutMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id, HttpServletRequest request) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id : " + id));

		final String authHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authHeader != null && authHeader.startsWith("Bearer")) {
			jwt = authHeader.substring(7);
			username = jwtUtility.getUsernameFromToken(jwt);
		}

		if (user.isDeleted() == true) {
			return new ResponseEntity<>("Oops, User is already deleted...", HttpStatus.OK);
		}

		if (user.getUsername().equals(username)) {
			System.out.println(user.getUsername());
			System.out.println(username);
			user.setDeleted(true);
			userRepository.save(user);
		} else {

			return new ResponseEntity<>("Invalid user...", HttpStatus.OK);

		}
		return new ResponseEntity<>("User deleted successfully...", HttpStatus.OK);
	}

	// ................

//	@PostMapping("/addDetails")
//	public ResponseEntity<Object> addUserDetail(@RequestBody UserDetailsRequest userDetailsRequest)
//	{	
//		if(userRepository.existsByEmail(userDetailsRequest.getEmail()))
//		{	
//			if(Boolean.FALSE.equals(userDetailsRepository.existsByEmail(userDetailsRequest.getEmail())))
//				return new ResponseEntity<>("Oops, Email already exists...!",HttpStatus.BAD_REQUEST);
//		}
//		else
//			return new ResponseEntity<>("Sorry, Email is invalid...!",HttpStatus.BAD_REQUEST);
//		
//		UserDetails userDetails= new UserDetails();
//		
//		userDetails.setEmail(userDetailsRequest.getEmail());
//		userDetails.setMobNum(userDetailsRequest.getMobNum());
//		userDetails.setState(userDetailsRequest.getState());
//		userDetails.setAddress(userDetailsRequest.getAddress());
//		userDetails.setCountry(userDetailsRequest.getCountry());
//		
//		Address address = new Address();
//		AddressDto addressDto = (AddressDto) userDetailsRequest.getAddressDto();
//		
//		address.setDistrict(addressDto.getDistrict());
//		address.setState(addressDto.getState());
//		address.setCountry(addressDto.getCountry());
//		address.setPinCode(addressDto.getPinCode());
//		
//		userDetailsRepository.save(userDetails);
//		
//		
//		return ResponseEntity.ok(userDetails);
//
//	}
}
