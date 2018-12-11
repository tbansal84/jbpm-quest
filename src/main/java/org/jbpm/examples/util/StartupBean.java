/**
 * Copyright 2015, Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.examples.util;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.jbpm.services.task.identity.DefaultUserInfo;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.task.api.UserInfo;

@Singleton
@Startup
public class StartupBean {

	public static final String DEPLOYMENT_ID = null;
	@PersistenceUnit(unitName = "org.jbpm.domain")
	private EntityManagerFactory emf;

	@Produces
	public EntityManagerFactory produceEntityManagerFactory() {
		if (this.emf == null) {
			this.emf = Persistence
					.createEntityManagerFactory("org.jbpm.domain");
		}
		return this.emf;
	}

	@Produces
	@org.kie.internal.runtime.manager.cdi.qualifier.Singleton
	public RuntimeEnvironment produceEnvironment(EntityManagerFactory emf) {
		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory
				.get()
				.newDefaultBuilder()
				.entityManagerFactory(emf)
				.addAsset(
						ResourceFactory.newClassPathResource("MpSearch.bpmn2"),
						ResourceType.BPMN2).get();
		return environment;
	}

	@Produces
	public UserInfo produceUserInfo() {
		// default implementation will load userinfo.properties file on the
		// classpath
		return new DefaultUserInfo(true);
	}

	@Produces
	public UserGroupCallback produceUserGroupCallback() {
		return new RewardsUserGroupCallback();
	}

}
