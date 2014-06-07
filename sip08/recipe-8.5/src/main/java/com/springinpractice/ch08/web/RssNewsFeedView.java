/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.springinpractice.ch08.model.NewsItem;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class RssNewsFeedView extends AbstractRssFeedView {
	private String feedTitle;
	private String feedDesc;
	private String feedLink;
	
	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}
	
	public void setFeedDescription(String feedDesc) {
		this.feedDesc = feedDesc;
	}
	
	public void setFeedLink(String feedLink) {
		this.feedLink = feedLink;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.feed.AbstractFeedView#buildFeedMetadata(java.util.Map,
	 * com.sun.syndication.feed.WireFeed, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void buildFeedMetadata(
			@SuppressWarnings("rawtypes") Map model, Channel feed, HttpServletRequest request) {
		
		feed.setTitle(feedTitle);
		feed.setDescription(feedDesc);
		feed.setLink(feedLink);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.feed.AbstractRssFeedView#buildFeedItems(java.util.Map,
	 * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected List<Item> buildFeedItems(
			@SuppressWarnings("rawtypes") Map model,
			HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {
		
		@SuppressWarnings("unchecked")
		List<NewsItem> newsItems = (List<NewsItem>) model.get("newsItemList");
		
		// Build the list of feed items. Check for non-null since an empty list will show up as null here. (Spring
		// can't autogenerate a model key for an empty list.)
		List<Item> feedItems = new ArrayList<Item>();
		if (newsItems != null) {
			for (NewsItem newsItem : newsItems) {
				Item feedItem = new Item();
				feedItem.setTitle(newsItem.getTitle());
				feedItem.setAuthor(newsItem.getAuthor());
				feedItem.setPubDate(newsItem.getDatePublished());
				feedItem.setLink(newsItem.getLink());
				
				Description desc = new Description();
				desc.setType("text/html");
				desc.setValue(newsItem.getDescription());
				feedItem.setDescription(desc);
				
				feedItems.add(feedItem);
			}
		}
		
		return feedItems;
	}
}
