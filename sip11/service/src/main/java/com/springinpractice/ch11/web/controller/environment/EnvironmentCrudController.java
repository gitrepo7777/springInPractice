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
package com.springinpractice.ch11.web.controller.environment;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springinpractice.ch11.model.Environment;
import com.springinpractice.ch11.service.CIService;
import com.springinpractice.ch11.service.EnvironmentService;
import com.springinpractice.ch11.service.FarmService;
import com.springinpractice.ch11.web.controller.AbstractCrudController;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/environments")
public class EnvironmentCrudController extends AbstractCrudController<Environment> {
	private static final String[] ALLOWED_FIELDS = new String[] { "name" };
	
	@Inject private EnvironmentService environmentService;
	@Inject private FarmService farmService;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#getService()
	 */
	@Override
	public CIService<Environment> getService() { return environmentService; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#getAllowedFields()
	 */
	@Override
	protected String[] getAllowedFields() { return ALLOWED_FIELDS; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#doGetDetails(java.lang.Long, org.springframework.ui.Model)
	 */
	@Override
	protected Environment doGetDetails(Long id, Model model) {
		model.addAttribute(farmService.findByEnvironment(new Environment(id)));
		return environmentService.findOne(id);
	}
}
