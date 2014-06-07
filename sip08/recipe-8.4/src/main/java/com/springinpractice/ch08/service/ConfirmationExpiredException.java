/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@SuppressWarnings("serial")
public class ConfirmationExpiredException extends ConfirmationFailedException {
	
	public ConfirmationExpiredException() {
		super("Confirmation expired");
	}
	
	public ConfirmationExpiredException(String msg) {
		super(msg);
	}
}
