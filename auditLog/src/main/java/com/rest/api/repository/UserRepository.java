package com.rest.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rest.api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> 
{

	User findByUsername(String username);
	
	List<User> findByUsernameOrEmail(String usrename, String email);
	List<User> findByDeletedTrue();
	List<User> findAll();
	

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
}
