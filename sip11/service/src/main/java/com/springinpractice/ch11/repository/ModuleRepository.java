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
package com.springinpractice.ch11.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.springinpractice.ch11.model.Module;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public interface ModuleRepository extends GraphRepository<Module> {
	
	// Can't I return a single Module if I know there's just one? Guess not...
	
	/**
	 * @param groupId group ID
	 * @param moduleId module ID
	 * @return module
	 */
	// Hm, for whatever reason, I need this query here or else SDN generates multiple copies of the module in question.
	@Query("start module=node:__types__(className='com.springinpractice.ch11.model.Module') where module.groupId={0} and module.moduleId={1} return module")
	Module findByGroupIdAndModuleId(String groupId, String moduleId);
}
