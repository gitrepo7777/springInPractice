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
package com.springinpractice.ch11.web.controller.datacenter;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springinpractice.ch11.model.DataCenter;
import com.springinpractice.ch11.service.CIService;
import com.springinpractice.ch11.service.DataCenterService;
import com.springinpractice.ch11.service.RegionService;
import com.springinpractice.ch11.web.controller.AbstractCrudController;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/datacenters")
public class DataCenterCrudController extends AbstractCrudController<DataCenter> {
	private static final String[] ALLOWED_FIELDS = new String[] { "name", "region" };
	
	@Inject private DataCenterService dataCenterService;
	@Inject private RegionService regionService;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#getService()
	 */
	@Override
	public CIService<DataCenter> getService() { return dataCenterService; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#getAllowedFields()
	 */
	@Override
	protected String[] getAllowedFields() { return ALLOWED_FIELDS; }
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractCrudController#populateReferenceData(org.springframework.ui.Model)
	 */
	@Override
	protected void populateReferenceData(Model model) {
		model.addAttribute(regionService.findAll());
	}
}
