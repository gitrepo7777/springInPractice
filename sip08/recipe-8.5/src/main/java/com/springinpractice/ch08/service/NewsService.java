/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.service;

import java.util.List;

import com.springinpractice.ch08.model.NewsItem;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface NewsService {
	
	/**
	 * <p>
	 * Returns a list of recent news items in descending order by publication date. The meaning of "recent" is
	 * implementation-dependent.
	 * </p>
	 * 
	 * @return list of recent news items
	 */
	List<NewsItem> getRecentNews();
}
