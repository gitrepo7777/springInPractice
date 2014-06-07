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

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Service;

import com.springinpractice.ch11.model.Application;
import com.springinpractice.ch11.model.Environment;
import com.springinpractice.ch11.model.Farm;
import com.springinpractice.ch11.model.Instance;
import com.springinpractice.ch11.repository.FarmRepository;
import com.springinpractice.ch11.service.FarmService;
import com.springinpractice.ch11.util.CollectionsUtil;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class FarmServiceImpl extends AbstractCIService<Farm> implements FarmService {
	@Inject private FarmRepository farmRepo;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<Farm> getRepository() { return farmRepo; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractEntityServiceImpl#findOne(java.lang.Long)
	 */
	@Override
	public Farm findOne(Long id) {
		notNull(id);
		
		// See http://springinpractice.com/2011/12/28/initializing-lazy-loaded-collections-with-spring-data-neo4j/
		Farm farm = super.findOne(id);
		Iterable<Instance> instances = farm.getInstances();
		for (Instance instance : instances) {
			neo4jTemplate.fetch(instance);
		}
		return farm;
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.FarmService#findByApplication(com.springinpractice.ch11.model.Application)
	 */
	@Override
	public List<Farm> findByApplication(Application application) {
		notNull(application);
		return CollectionsUtil.asSortedList(farmRepo.findByApplication(application));
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.FarmService#findByEnvironment(com.springinpractice.ch11.model.Environment)
	 */
	@Override
	public List<Farm> findByEnvironment(Environment environment) {
		notNull(environment);
		return CollectionsUtil.asSortedList(farmRepo.findByEnvironment(environment));
	}
}
