/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Entity
@Table(name = "subscriber")
@NamedQuery(
	name = "deleteSubscriberByEmail",
	query = "delete from Subscriber where email = :email")
public final class Subscriber {
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 40)
	private String firstName;
	
	@NotNull
	@Size(min = 1, max = 40)
	private String lastName;
	
	@NotNull
	@Size(min = 1, max = 80)
	@Email
	private String email;
	
	private boolean confirmed = false;
	private String ipAddress;
	private Date dateCreated;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() { return id; }
	
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }
	
	@Column(name = "first_name")
	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	@Column(name = "last_name")
	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }

	@Column(name = "email")
	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }
	
	@Column(name = "confirmed")
	public boolean isConfirmed() { return confirmed; }
	
	public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }
	
	@Column(name = "ip_addr")
	public String getIpAddress() { return ipAddress; }

	public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
	
	@Column(name = "date_created")
	public Date getDateCreated() { return dateCreated; }
	
	public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("firstName", firstName)
			.append("lastName", lastName)
			.append("email", email)
			.append("confirmed", confirmed)
			.append("ipAddress", ipAddress)
			.append("dateCreated", dateCreated)
		.toString();
	}
}
