/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.dao.hbn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.springinpractice.ch08.dao.NewsItemDao;
import com.springinpractice.ch08.model.NewsItem;
import com.springinpractice.dao.hibernate.AbstractHbnDao;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Repository
public class HbnNewsItemDao extends AbstractHbnDao<NewsItem> implements NewsItemDao {

	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.dao.NewsItemDao#getListByDateDesc()
	 */
	@SuppressWarnings("unchecked")
	public List<NewsItem> getListByDateDesc() {
		return (List<NewsItem>) getSession()
			.getNamedQuery("findAllNewsItems")
			.setMaxResults(10)
			.list();
	}
}
