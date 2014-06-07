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
package com.springinpractice.ch11.web.controller.person;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUser;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springinpractice.ch11.model.Person;
import com.springinpractice.ch11.repository.PersonRepository;
import com.springinpractice.ch11.web.controller.AbstractController;
import com.springinpractice.ch11.web.view.ViewUtil;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/people")
public class PersonScmController extends AbstractController {
	@Inject private PersonRepository personRepository;
//	@Inject private GitHub gitHub;
	
	// FIXME Move to service
	private GitHub gitHub = new GitHubTemplate();
	
	/**
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/scm/followers", method = RequestMethod.GET)
	public String getFollowers(@PathVariable Long id, Model model) {
		Person person = personRepository.findOne(id);
		model.addAttribute(person);
		model.addAttribute("entity", person);
		
		String gitHubUser = person.getGitHubUser();
		if (gitHubUser != null) {
			List<GitHubUser> followers = gitHub.userOperations().getFollowers(gitHubUser);
			List<List<GitHubUser>> followerRows = ViewUtil.toRows(followers, 3);
			model.addAttribute("followerList", followers);
			model.addAttribute("followerRows", followerRows);
		}
		
		return addNavigation(model, "personScmFollowers");
	}
	
	@RequestMapping(value = "/{id}/scm/following", method = RequestMethod.GET)
	public String getFollowing(@PathVariable Long id, Model model) {
		Person person = personRepository.findOne(id);
		model.addAttribute(person);
		model.addAttribute("entity", person);
		
		String gitHubUser = person.getGitHubUser();
		if (gitHubUser != null) {
			List<GitHubUser> following = gitHub.userOperations().getFollowing(gitHubUser);
			List<List<GitHubUser>> followingRows = ViewUtil.toRows(following, 3);
			model.addAttribute("followingList", following);
			model.addAttribute("followingRows", followingRows);
		}
		
		return addNavigation(model, "personScmFollowing");
	}
}
