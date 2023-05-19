package com.rest.api.entity;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.rest.api.audit.Auditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "userdetails", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class UserDetails extends Auditable<String>
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String email;
    private int mobNum;
    private String address;
	private String state;
	private String country;
	
	private boolean deleted = Boolean.FALSE;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_info",
        joinColumns = @JoinColumn(name = "userdetails_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	private Set<User> users;
	
}
