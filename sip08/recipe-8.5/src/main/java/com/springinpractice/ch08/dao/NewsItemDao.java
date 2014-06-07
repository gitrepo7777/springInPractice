/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.dao;

import java.util.List;

import com.springinpractice.ch08.model.NewsItem;
import com.springinpractice.dao.Dao;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface NewsItemDao extends Dao<NewsItem> {
	
	/**
	 * @param maxResults 
	 * @return
	 */
	List<NewsItem> getListByDateDesc();
}
