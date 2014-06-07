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
package com.springinpractice.ch11.web.controller.application;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springinpractice.ch11.model.Application;
import com.springinpractice.ch11.model.Module;
import com.springinpractice.ch11.model.Package;
import com.springinpractice.ch11.service.ApplicationService;
import com.springinpractice.ch11.service.ModuleService;
import com.springinpractice.ch11.service.PackageService;
import com.springinpractice.ch11.util.CollectionsUtil;
import com.springinpractice.ch11.web.controller.AbstractController;

// TODO Hm, we want CRUD operations here, but this isn't a top-level CI. Need to think about how best to handle this.

/**
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 */
@Controller
@RequestMapping("/applications")
public class ApplicationModuleController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(ApplicationModuleController.class);
	
	@Inject private ApplicationService applicationService;
	@Inject private ModuleService moduleService;
	@Inject private PackageService packageService;
	
	/**
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.setAllowedFields("name", "shortDescription", "groupId", "moduleId");
	}
	
	/**
	 * @param id application ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/modules/new", method = RequestMethod.GET)
	public String newModule(@PathVariable Long id, Model model) {
		model.addAttribute(new Application(id));
		model.addAttribute(new Module());
		return "createApplicationModuleForm";
	}
	
	/**
	 * @param id application ID
	 * @param module
	 * @param result
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/{id}/modules", method = RequestMethod.POST)
	public String createModule(
			@PathVariable Long id,
			@ModelAttribute @Valid Module module,
			BindingResult result,
			Model model,
			HttpServletResponse response) {
		
		log.debug("Creating module: {}", module);

		applicationService.addModule(id, module, result);
		
		if (result.hasErrors()) {
			log.debug("Invalid module");
			model.addAttribute(new Application(id));
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "createApplicationModuleForm";
		}
		
		return null;
	}
	
	/**
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{id}/modules", method = RequestMethod.GET)
	public String getModules(@PathVariable Long id, Model model) {
		Application app = applicationService.findOneWithModules(id);
		
		model.addAttribute(app);
		model.addAttribute(CollectionsUtil.asSortedList(app.getModules()));
		
		return addNavigation(model, "applicationModuleList");
	}
	
	/**
	 * @param applicationId
	 * @param moduleId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{applicationId}/modules/{moduleId}", method = RequestMethod.GET)
	public String getModule(@PathVariable Long applicationId, @PathVariable Long moduleId, Model model) {
		Application application = applicationService.findOne(applicationId);
		Module module = moduleService.findOne(moduleId);
		List<Package> packages = packageService.findByModule(module);
		Collections.sort(packages);
		
		model.addAttribute(application);
		model.addAttribute(module);
		model.addAttribute(packages);
		
		return addNavigation(model, "applicationModule");
	}
}
