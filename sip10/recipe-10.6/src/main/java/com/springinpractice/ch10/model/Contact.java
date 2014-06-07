/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch10.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.springinpractice.util.StringUtils;

/**
 * <p>
 * A simple domain object for contacts.
 * </p>
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Entity
@Table(name = "contact")
public class Contact {
	private Long id;
	private String lastName;
	private String firstName;
	private String middleInitial;
	private String email;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() { return id; }
	
	public void setId(Long id) { this.id = id; }
	
	@NotNull
	@Length(min = 1, max = 40)
	@Column(name = "last_name")
	public String getLastName() { return lastName; }
	
	public void setLastName(String lastName) {
		this.lastName = StringUtils.cleanup(lastName);
	}
	
	@NotNull
	@Length(min = 1, max = 40)
	@Column(name = "first_name")
	public String getFirstName() { return firstName; }
	
	public void setFirstName(String firstName) {
		this.firstName = StringUtils.cleanup(firstName);
	}
	
	@Length(max = 1)
	@Column(name = "mi")
	public String getMiddleInitial() { return middleInitial; }
	
	public void setMiddleInitial(String mi) {
		this.middleInitial = StringUtils.cleanup(mi);
	}
	
	@Email
	@Column(name = "email")
	public String getEmail() { return email; }
	
	public void setEmail(String email) {
		this.email = StringUtils.cleanup(email);
	}
	
	@Transient
	public String getFullName() {
		String fullName = lastName + ", " + firstName;
		if (! (middleInitial == null || "".equals(middleInitial.trim()))) {
			fullName += " " + middleInitial + ".";
		}
		return fullName;
	}
}
