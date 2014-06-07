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
public class ConfirmationFailedException extends Exception {
	
	public ConfirmationFailedException() {
		super("Confirmation failed");
	}
	
	public ConfirmationFailedException(String msg) {
		super(msg);
	}
}
