package com.rest.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rest.api.entity.Address;
import com.rest.api.entity.UserDetails;
import com.rest.api.payload.UserDetailsResponse;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> 
{
	Optional<UserDetails> findByEmail(String email);
	
	Boolean existsByEmail(String email);


}
