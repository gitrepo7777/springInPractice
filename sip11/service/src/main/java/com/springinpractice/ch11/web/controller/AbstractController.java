/* 
 * Copyright 2011-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springinpractice.ch11.web.controller;

import static org.springframework.util.Assert.notNull;

import javax.inject.Inject;

import org.springframework.ui.Model;

import com.springinpractice.ch11.web.sitemap.Sitemap;
import com.springinpractice.ch11.web.sitemap.navigation.Navigation;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class AbstractController {
	@Inject protected Sitemap sitemap;
	
	/**
	 * @param model model
	 * @param nodeId node ID
	 * @return node ID
	 */
	protected String addNavigation(Model model, String nodeId) {
		notNull(model);
		notNull(nodeId);
		model.addAttribute(new Navigation(sitemap, nodeId, model.asMap()));
		return nodeId;
	}
}
