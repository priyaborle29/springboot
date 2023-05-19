package com.rest.api.controller;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.entity.User;
import com.rest.api.repository.UserRepository;
import com.rest.api.service.UserReport;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/jasper")
public class JasperReportController {

	@Autowired
	private UserReport userReport;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/report/{format}")
	public String generateReport(@PathVariable String format) throws FileNotFoundException, JRException, ClassNotFoundException, SQLException
	{
		return userReport.exportReport(format);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<User>> findAll()
	{
		List<User> user = userRepository.findAll();
		return ResponseEntity.ok(user);
	}
}
