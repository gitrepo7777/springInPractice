/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service;

import com.springinpractice.ch08.model.UserMessage;

/**
 * <p>
 * Service interface for the "contact us" form.
 * </p>
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ContactService {
	
	/**
	 * <p>
	 * Saves the user's message in the database, forwards it to the admin, and
	 * sends a confirmation message to the user.
	 * </p>
	 * 
	 * @param msg
	 *            user message
	 * @throws IllegalArgumentException
	 *             if <code>msg</code> is <code>null</code>
	 */
	void saveUserMessage(UserMessage msg);
}
