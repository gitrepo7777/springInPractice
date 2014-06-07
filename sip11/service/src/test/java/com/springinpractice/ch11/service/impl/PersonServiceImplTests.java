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

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neo4j.helpers.collection.ClosableIterable;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.springinpractice.ch11.model.Person;
import com.springinpractice.ch11.repository.PersonRepository;
import com.springinpractice.ch11.service.impl.AbstractCIService;
import com.springinpractice.ch11.service.impl.PersonServiceImpl;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class PersonServiceImplTests extends AbstractEntityServiceImplTests<Person> {
	@InjectMocks private PersonServiceImpl personService;
	@Mock private PersonRepository personRepo;
	
	// Test objects
	@Mock private Person person;
	@Mock private ClosableIterable<Person> people;
	@Mock private Iterator<Person> personIterator;

	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractEntityServiceImplTests#getRepository()
	 */
	@Override
	protected GraphRepository<Person> getRepository() { return personRepo; }

	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractEntityServiceImplTests#getService()
	 */
	@Override
	protected AbstractCIService<Person> getService() { return personService; }
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.personService = new PersonServiceImpl();
		
		MockitoAnnotations.initMocks(this);
		
		when(personRepo.findOne(anyLong())).thenReturn(person);
		when(personRepo.findAll()).thenReturn(people);
		when(people.iterator()).thenReturn(personIterator);
	}

}
