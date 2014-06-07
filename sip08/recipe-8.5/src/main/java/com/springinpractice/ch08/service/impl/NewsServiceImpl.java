/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.springinpractice.ch08.dao.NewsItemDao;
import com.springinpractice.ch08.model.NewsItem;
import com.springinpractice.ch08.service.NewsService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
@Transactional(
	propagation = Propagation.REQUIRED,
	isolation = Isolation.DEFAULT,
	readOnly = true)
public class NewsServiceImpl implements NewsService {
	@Inject private NewsItemDao newsItemDao;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.service.NewsService#getRecentNews()
	 */
	@Override
	public List<NewsItem> getRecentNews() {
		return newsItemDao.getListByDateDesc();
	}
}
