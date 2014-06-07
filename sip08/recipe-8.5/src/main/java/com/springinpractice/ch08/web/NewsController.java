/* 
 * Copyright (c) 2013 Manning Publications Co.
 * 
 * Book: http://manning.com/wheeler/
 * Blog: http://springinpractice.com/
 * Code: https://github.com/springinpractice
 */
package com.springinpractice.ch08.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springinpractice.ch08.service.NewsService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
public final class NewsController {
	@Inject private NewsService newsService;
	
	@RequestMapping("/news.rss")
	public String rss(Model model) {
		model.addAttribute(newsService.getRecentNews());
		return "news";
	}
}
