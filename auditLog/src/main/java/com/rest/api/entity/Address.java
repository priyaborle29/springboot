package com.rest.api.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.rest.api.audit.Auditable;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Address extends Auditable<String> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long addressId;
	
	private String district;
	
	private String state;
	
	private String country;
	
	private long pinCode;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_address",
    	joinColumns = @JoinColumn(name = "user_address", referencedColumnName = "addressId"),
    	inverseJoinColumns = @JoinColumn(name = "userDetails_id", referencedColumnName = "id")
    )
	private UserDetails userDetails;

}
