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

import java.util.Set;

import javax.inject.Inject;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.springinpractice.ch11.model.Person;
import com.springinpractice.ch11.repository.PersonRepository;
import com.springinpractice.ch11.service.PersonService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class PersonServiceImpl extends AbstractCIService<Person> implements PersonService {
	@Inject private PersonRepository personRepo;
	@Inject private Neo4jTemplate template;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<Person> getRepository() { return personRepo; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractEntityServiceImpl#findOne(java.lang.Long)
	 */
	@Override
	public Person findOne(Long id) {
		notNull(id);
		Person person = personRepo.findOne(id);
		
		// For now this is how you do it.
		// http://stackoverflow.com/questions/8218864/fetch-annotation-in-sdg-2-0-fetching-strategy-questions
		// http://springinpractice.com/2011/12/28/initializing-lazy-loaded-collections-with-spring-data-neo4j/
		Set<Person> reports = person.getDirectReports();
		for (Person report : reports) {
			template.fetch(report);
		}
		
		return person;
	}
}
