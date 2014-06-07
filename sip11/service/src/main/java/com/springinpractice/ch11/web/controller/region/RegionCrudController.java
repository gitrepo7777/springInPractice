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
package com.springinpractice.ch11.web.controller.region;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springinpractice.ch11.model.DataCenter;
import com.springinpractice.ch11.model.Region;
import com.springinpractice.ch11.service.CIService;
import com.springinpractice.ch11.service.RegionService;
import com.springinpractice.ch11.web.controller.AbstractCrudController;
import com.springinpractice.ch11.web.view.JitNode;

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/regions")
public class RegionCrudController extends AbstractCrudController<Region> {
	private static final String[] ALLOWED_FIELDS = new String[] { "name" };
	
	@Inject private RegionService regionService;
	
	public CIService<Region> getService() { return regionService; }

	/* (non-Javadoc)
	 * @see com.springinpractice.ch11.web.controller.AbstractEntityFormController#getAllowedFields()
	 */
	@Override
	protected String[] getAllowedFields() { return ALLOWED_FIELDS; }
	
	/**
	 * @param id
	 * @param res
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "format=jit")
	@ResponseBody
	public JitNode getDetailsAsJit(@PathVariable Long id) {
		Region region = regionService.findOne(id);
		
		JitNode regionNode = new JitNode();
		regionNode.setId(region.getId().toString());
		regionNode.setName(region.getName());
		
		Set<DataCenter> dataCenters = region.getDataCenters();
		Set<JitNode> dataCenterNodes = new HashSet<JitNode>();
		for (DataCenter dataCenter : dataCenters) {
			JitNode dataCenterNode = new JitNode();
			dataCenterNode.setId(dataCenter.getId().toString());
			dataCenterNode.setName(dataCenter.getName());
			dataCenterNodes.add(dataCenterNode);
			
			Set<JitNode> farmNodes = new HashSet<JitNode>();
			for (int i = 0; i < 8; i++) {
				JitNode farmNode = new JitNode();
				farmNode.setId(dataCenter.getId() + "_" + i);
				farmNode.setName("Farm " + (i + 1));
				farmNodes.add(farmNode);
			}
			dataCenterNode.setChildren(farmNodes);
		}
		regionNode.setChildren(dataCenterNodes);
		
		return regionNode;
	}
}
