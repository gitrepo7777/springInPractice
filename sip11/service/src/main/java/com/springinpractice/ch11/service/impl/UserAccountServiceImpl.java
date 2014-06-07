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
package com.springinpractice.ch11.service.impl;

import javax.inject.Inject;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.github.api.impl.GitHubTemplate;
import org.springframework.stereotype.Service;

import com.springinpractice.ch11.model.UserAccount;
import com.springinpractice.ch11.repository.UserAccountRepository;
import com.springinpractice.ch11.service.UserAccountService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class UserAccountServiceImpl extends AbstractCIService<UserAccount> implements UserAccountService {
	@Inject private UserAccountRepository userAccountRepository;
	
	// FIXME Centralize this, as we call it from ApplicationServiceImpl too. May want to use Java config.
	@Inject private ConnectionRepository connectionRepo;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<UserAccount> getRepository() { return userAccountRepository; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.UserAccountService#getCurrentUserProfile()
	 */
	@Override
	public GitHubUserProfile getCurrentUserProfile() {
		if (gitHub().isAuthorized()) {
			return gitHub().userOperations().getUserProfile();
		} else {
			return null;
		}
	}
	
	private GitHub gitHub() {
		Connection<GitHub> conn = connectionRepo.findPrimaryConnection(GitHub.class);
		return (conn != null ? conn.getApi() : new GitHubTemplate());
	}
	
}
