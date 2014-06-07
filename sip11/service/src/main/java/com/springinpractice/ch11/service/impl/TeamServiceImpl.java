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

import static org.springframework.util.Assert.notNull;

import javax.inject.Inject;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;

import com.springinpractice.ch11.model.Team;
import com.springinpractice.ch11.repository.TeamRepository;
import com.springinpractice.ch11.service.TeamService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class TeamServiceImpl extends AbstractCIService<Team> implements TeamService {
	@Inject private TeamRepository teamRepository;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<Team> getRepository() { return teamRepository; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.TeamService#findByName(java.lang.String)
	 */
	@Override
	public Team findByName(String name) {
		notNull(name);
		return teamRepository.findByName(name);
	}

}
