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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Entity
@Table(name = "user_message")
public final class UserMessage {
	private Long id;
	
	@NotNull
	@Length(min = 1, max = 80)
	private String name;
	
	@NotNull
	@Size(min = 1, max = 80)
	@Email
	private String email;
	
	@NotNull
	@Size(min = 1, max = 4000)
	private String text;
	
	private String ipAddr;
	private String acceptLanguage;
	private String referer;
	private String userAgent;
	private Date dateCreated; 
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() { return id; }
	
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }
	
	@Column(name = "name")
	public String getName() { return name; }

	public void setName(String name) { this.name = name; }
	
	@Column(name = "email")
	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	@Column(name = "message_text")
	public String getText() { return text; }

	public void setText(String text) { this.text = text; }
	
	@Column(name = "ip_addr")
	public String getIpAddress() { return ipAddr; }
	
	public void setIpAddress(String ipAddr) { this.ipAddr = ipAddr; }
	
	@Column(name = "accept_language")
	public String getAcceptLanguage() { return acceptLanguage; }
	
	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	
	@Column(name = "referer")
	public String getReferer() { return referer; }
	
	public void setReferer(String referer) { this.referer = referer; }
	
	@Column(name = "user_agent")
	public String getUserAgent() { return userAgent; }

	public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
	
	@Column(name = "date_created")
	public Date getDateCreated() { return dateCreated; }
	
	public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }

	public String toString() {
		return new ToStringBuilder(this)
			.append("name", name)
			.append("email", email)
			.append("text", text)
			.append("ipAddress", ipAddr)
			.append("acceptLanguage", acceptLanguage)
			.append("referer", referer)
			.append("userAgent", userAgent)
			.append("dateCreated", dateCreated)
			.toString();
	}
}
