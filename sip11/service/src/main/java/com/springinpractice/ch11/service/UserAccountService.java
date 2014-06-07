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
package com.springinpractice.ch11.service;

import org.springframework.social.github.api.GitHubUserProfile;

import com.springinpractice.ch11.model.UserAccount;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface UserAccountService extends CIService<UserAccount> {
	
	/**
	 * @return current GitHub user profile, or null if the user isn't authenticated to GitHub
	 */
	GitHubUserProfile getCurrentUserProfile();
}
