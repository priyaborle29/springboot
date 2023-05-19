package com.rest.api;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.rest.api.audit.AuditorAwareImpl;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ApiApplication {

	@Bean
	public AuditorAware<String>auditorAware()
	{
		return new AuditorAwareImpl();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
}
