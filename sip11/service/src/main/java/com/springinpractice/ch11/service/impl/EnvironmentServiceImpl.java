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
import org.springframework.stereotype.Service;

import com.springinpractice.ch11.model.Environment;
import com.springinpractice.ch11.repository.EnvironmentRepository;
import com.springinpractice.ch11.service.EnvironmentService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class EnvironmentServiceImpl extends AbstractCIService<Environment> implements EnvironmentService {
	@Inject private EnvironmentRepository environmentRepository;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<Environment> getRepository() {
		return environmentRepository;
	}
}
