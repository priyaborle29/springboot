package com.rest.api.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	private String email;
	
    private String username;
	
    private String password;
    
    private String confirmPass;
    
    private boolean deleted = Boolean.FALSE;

    @ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_role", 
				joinColumns = @JoinColumn(name = "userId"), 
				inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> role;

    private String token;

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", confirmPass=" + confirmPass + ", deleted=" + deleted + ", role=" + role + "]";
	}
	
	


}
