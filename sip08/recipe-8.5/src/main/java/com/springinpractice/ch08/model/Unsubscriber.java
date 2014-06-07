/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public final class Unsubscriber {
	
	@NotNull
	@Size(min = 1, max = 80)
	@Email
	private String email;
	
	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }
}
