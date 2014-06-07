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

import com.springinpractice.ch11.exception.DuplicateCIException;
import com.springinpractice.ch11.model.Module;
import com.springinpractice.ch11.model.Package;
import com.springinpractice.ch11.repository.PackageRepository;
import com.springinpractice.ch11.service.PackageService;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Service
public class PackageServiceImpl extends AbstractCIService<Package> implements PackageService {
	@Inject private PackageRepository packageRepo;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#getRepository()
	 */
	@Override
	protected GraphRepository<Package> getRepository() {
		return packageRepo;
	}
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.impl.AbstractCIService#checkForDuplicate(com.springinpractice.ch11.model.CI)
	 */
	@Override
	protected void checkForDuplicate(Package pkg) {
		notNull(pkg);
		Package duplicate = packageRepo.findByModuleAndVersion(pkg.getModule(), pkg.getVersion());
		if (duplicate != null) {
			throw new DuplicateCIException(pkg);
		}
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.PackageService#findByModule(com.springinpractice.ch11.model.Module)
	 */
	@Override
	public List<Package> findByModule(Module module) {
		return packageRepo.findByModule(module);
	}

	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.service.PackageService#findByModuleAndVersion(com.springinpractice.ch11.model.Module, java.lang.String)
	 */
	@Override
	public Package findByModuleAndVersion(Module module, String version) {
		return packageRepo.findByModuleAndVersion(module, version);
	}
}
