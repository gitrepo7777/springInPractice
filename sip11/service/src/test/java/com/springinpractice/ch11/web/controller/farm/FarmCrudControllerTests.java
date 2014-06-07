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
package com.springinpractice.ch11.web.controller.farm;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import org.mockito.Mock;

import com.springinpractice.ch11.model.Farm;
import com.springinpractice.ch11.service.DataCenterService;
import com.springinpractice.ch11.service.EnvironmentService;
import com.springinpractice.ch11.service.FarmService;
import com.springinpractice.ch11.web.controller.AbstractCrudControllerTests;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
public class FarmCrudControllerTests extends AbstractCrudControllerTests<Farm> {
	
	// Dependencies
	@Mock private DataCenterService dataCenterService;
	@Mock private EnvironmentService environmentService;
	@Mock private FarmService farmService;
	
	// Test objects
	@Mock private Farm farm;
	
	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractEntityNoFormControllerTests#doSetUp()
	 */
	@Override
	protected void doSetUp() throws Exception {
		when(farmService.findOne(anyLong())).thenReturn(farm);
	}
}
