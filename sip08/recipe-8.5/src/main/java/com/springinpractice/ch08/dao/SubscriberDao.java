/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.dao;

import com.springinpractice.ch08.model.Subscriber;
import com.springinpractice.dao.Dao;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface SubscriberDao extends Dao<Subscriber> {
	
	/**
	 * Deletes the subscriber having the given e-mail address.
	 * 
	 * @param email email
	 */
	void deleteByEmail(String email);
}
