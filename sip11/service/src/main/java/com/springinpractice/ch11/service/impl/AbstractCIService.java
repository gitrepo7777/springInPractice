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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.springinpractice.ch11.exception.DuplicateCIException;
import com.springinpractice.ch11.exception.NoSuchCIException;
import com.springinpractice.ch11.model.CI;
import com.springinpractice.ch11.service.CIService;
import com.springinpractice.ch11.util.CollectionsUtil;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Transactional
public abstract class AbstractCIService<T extends CI<T>> implements CIService<T> {
	private static final Logger log = LoggerFactory.getLogger(AbstractCIService.class);
	
	@Inject protected Neo4jTemplate neo4jTemplate;
	
	protected abstract GraphRepository<T> getRepository();
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#create(com.springinpractice.ch11.model.CI)
	 */
	@Override
	public void create(T ci) {
		notNull(ci);
		createAddDate(ci);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#create(com.springinpractice.ch11.model.CI, org.springframework.validation.Errors)
	 */
	@Override
	public void create(T ci, Errors errors) {
		notNull(ci);
		notNull(errors);
		
		if (!errors.hasErrors()) {
			try {
				createAddDate(ci);
			} catch (DuplicateCIException e) {
				log.debug("Duplicate CI");
				errors.reject("error.duplicateCI");
			}
		}
		
		if (errors.hasErrors()) {
			log.debug("Invalid CI; not saving");
		}
	}
	
	private void createAddDate(T ci) {
		checkForDuplicate(ci);
		ci.setDateCreated(new Date());
		getRepository().save(ci);
	}
	
	/**
	 * @param ci CI
	 * @throws DuplicateCIException if a duplicate exists
	 */
	protected void checkForDuplicate(T ci) { }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#findAll()
	 */
	@Override
	public List<T> findAll() {
		return CollectionsUtil.asSortedList(getRepository().findAll());
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#findOne(java.lang.Long)
	 */
	@Override
	public T findOne(Long id) {
		notNull(id);
		T ci = getRepository().findOne(id);
		if (ci == null) { throw new NoSuchCIException(); }
		return ci;
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#update(com.springinpractice.ch11.model.CI)
	 */
	@Override
	public void update(T ci) {
		notNull(ci);
		updateAddDate(ci);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#update(com.springinpractice.ch11.model.CI, org.springframework.validation.Errors)
	 */
	@Override
	public void update(T ci, Errors errors) {
		notNull(ci);
		
		if (errors == null || !errors.hasErrors()) {
			updateAddDate(ci);
		}
		
		// TODO Need to have a way to generate new errors here. For example, if the user changes the name of the CI to
		// conflict with an existing CI, that would generate an error.
	}
	
	private void updateAddDate(T ci) {
		ci.setDateModified(new Date());
		getRepository().save(ci);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#delete(com.springinpractice.ch11.model.CI)
	 */
	@Override
	public void delete(T ci) {
		notNull(ci);
		getRepository().delete(ci);
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.CIService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		notNull(id);
		getRepository().delete(id);
	}
}
