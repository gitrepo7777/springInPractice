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
package com.springinpractice.ch11.web.controller.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubCommit;
import org.springframework.social.github.api.GitHubUser;
import org.springframework.social.github.api.RepoOperations;
import org.springframework.ui.Model;

import com.springinpractice.ch11.model.Application;
import com.springinpractice.ch11.model.GitHubScm;
import com.springinpractice.ch11.service.ApplicationService;
import com.springinpractice.ch11.web.controller.application.ApplicationScmController;
import com.springinpractice.ch11.web.sitemap.Sitemap;
import com.springinpractice.ch11.web.sitemap.SitemapNode;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class ApplicationScmControllerTests {
	private static final String GITHUB_USER = "williewheeler";
	private static final String GITHUB_REPO = "skybase";
	
	@InjectMocks private ApplicationScmController controller;
	
	@Mock private ApplicationService applicationService;
	@Mock private GitHub gitHub;
	@Mock private RepoOperations repoOperations;
	@Mock private Sitemap sitemap;
	
	@Mock private SitemapNode node;
	@Mock private Model model;
	@Mock private Application application;
	@Mock private GitHubScm scm;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.controller = new ApplicationScmController();
		MockitoAnnotations.initMocks(this);
		when(applicationService.findOne(anyLong())).thenReturn(application);
		when(applicationService.findOneWithScm(anyLong())).thenReturn(application);
		when(sitemap.getNode(anyString())).thenReturn(node);
		when(gitHub.repoOperations()).thenReturn(repoOperations);
		when(repoOperations.getCollaborators(anyString(), anyString())).thenReturn(new ArrayList<GitHubUser>());
		when(repoOperations.getCommits(anyString(), anyString())).thenReturn(new ArrayList<GitHubCommit>());
		when(repoOperations.getWatchers(anyString(), anyString())).thenReturn(new ArrayList<GitHubUser>());
		when(application.getScm()).thenReturn(scm);
		when(scm.getUser()).thenReturn(GITHUB_USER);
		when(scm.getRepo()).thenReturn(GITHUB_REPO);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetScm() {
		String viewName = controller.getScm(1L, model);
		assertEquals("applicationScm", viewName);
		verify(model, times(1)).addAttribute(application);
		verify(model, times(1)).addAttribute("entity", application);
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetCollaborators() {
		String viewName = controller.getCollaborators(1L, model);
		assertEquals("applicationScmCollaborators", viewName);
		verify(model, times(1)).addAttribute(application);
		verify(model, times(1)).addAttribute("entity", application);
		verify(model, times(1)).addAttribute(eq("collaboratorList"), anyObject());
		verify(model, times(1)).addAttribute(eq("collaboratorRows"), anyObject());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetCommits() {
		String viewName = controller.getCommits(1L, model);
		assertEquals("applicationScmCommits", viewName);
		verify(model, times(1)).addAttribute(application);
		verify(model, times(1)).addAttribute("entity", application);
		verify(model, times(1)).addAttribute(eq("commitList"), anyObject());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetWatchers() {
		String viewName = controller.getWatchers(1L, model);
		assertEquals("applicationScmWatchers", viewName);
		verify(model, times(1)).addAttribute(application);
		verify(model, times(1)).addAttribute("entity", application);
		verify(model, times(1)).addAttribute(eq("watcherList"), anyObject());
		verify(model, times(1)).addAttribute(eq("watcherRows"), anyObject());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetHooks() throws Exception {
		String viewName = controller.getHooks(1L, model);
		assertEquals("applicationScmHooks", viewName);
//		verify(repoOperations, times(1)).getHooks(GITHUB_USER, GITHUB_REPO);
		verify(model, times(1)).addAttribute(application);
		verify(model, times(1)).addAttribute("entity", application);
//		verify(model, times(1)).addAttribute(eq("hookList"), anyObject());
	}
}
