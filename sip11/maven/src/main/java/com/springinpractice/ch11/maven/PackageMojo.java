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
package com.springinpractice.ch11.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.springframework.web.client.RestTemplate;

import com.springinpractice.ch11.client.ZkybaseClient;
import com.springinpractice.ch11.model.Module;
import com.springinpractice.ch11.model.Package;

/**
 * Package goal mojo.
 * 
 * @author Willie Wheeler (willie.wheeler@gmail.com)
 * @goal package
 */
public class PackageMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${skybaseUrl}"
	 */
	private String skybaseUrl;
	
	/**
	 * @parameter expression="${package.module}"
	 */
	private Module module;
	
	/**
	 * @parameter expression="${package.version}"
	 */
	private String version;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		ZkybaseClient client = new ZkybaseClient(new RestTemplate(), skybaseUrl);
		client.createPackage(new Package(module, version));
	}
}
