/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service.impl;

import static org.springframework.util.Assert.notNull;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.springinpractice.ch08.dao.UserMessageDao;
import com.springinpractice.ch08.model.UserMessage;
import com.springinpractice.ch08.service.ContactService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
@Transactional(
	propagation = Propagation.REQUIRED,
	isolation = Isolation.DEFAULT,
	readOnly = true)
public class ContactServiceImpl implements ContactService {
	@Inject private UserMessageDao userMsgDao;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.ContactService#saveUserMessage
	 * (com.springinpractice.ch08.model.UserMessage)
	 */
	@Transactional(readOnly = false)
	public void saveUserMessage(UserMessage userMsg) {
		notNull(userMsg, "userMsg can't be null");
		userMsgDao.create(userMsg);
	}
}
