/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.dao.hbn;

import static org.springframework.util.Assert.notNull;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.springinpractice.ch08.dao.SubscriberDao;
import com.springinpractice.ch08.model.Subscriber;
import com.springinpractice.dao.hibernate.AbstractHbnDao;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Repository
public class HbnSubscriberDao extends AbstractHbnDao<Subscriber> implements SubscriberDao {
		
	/* (non-Javadoc)
	 * @see com.springinpractice.ch08.dao.SubscriberDao#deleteByEmail(java.lang.String)
	 */
	public void deleteByEmail(String email) {
		notNull(email, "email required");
//		Query q = getSession().createQuery("delete Subscriber where email = :email");
		Query q = getSession().getNamedQuery("deleteSubscriberByEmail");
		q.setString("email", email);
		q.executeUpdate();
	}
}
