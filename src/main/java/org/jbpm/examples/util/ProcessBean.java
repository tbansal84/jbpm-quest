/**
 * Copyright 2014 JBoss Inc
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jbpm.services.task.exception.PermissionDeniedException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.runtime.manager.context.EmptyContext;

@Startup
@javax.ejb.Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcessBean implements ProcessLocal {

	private RuntimeManager singletonManager;

	@Inject
	private RuntimeManagerFactory managerFactory;

	@Inject
	@org.kie.internal.runtime.manager.cdi.qualifier.Singleton
	RuntimeEnvironment environment;

	@PostConstruct
	public void configure() {
		// use toString to make sure CDI initializes the bean
		// this makes sure that RuntimeManager is started asap,
		// otherwise after server restart complete task won't move process
		// forward
		singletonManager = managerFactory
				.newPerRequestRuntimeManager(environment);

		singletonManager.toString();
	}

	public long startProcess(String recipient) throws Exception {

		RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
				.get());
		KieSession ksession = runtime.getKieSession();

		long processInstanceId = -1;

		try {
			// start a new process instance
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("recipient", recipient);
			ProcessInstance processInstance = ksession.startProcess(
					"org.epo.pgs.platform.workflowmanager.test.MpSearch-1.0.0",
					params);

			processInstanceId = processInstance.getId();

			System.out.println("Process started ... : processInstanceId = "
					+ processInstanceId);

		} catch (Exception e) {
			System.out.println("--- " + e);
			throw e;
		}

		return processInstanceId;
	}

	public void approveTask(String actorId, long taskId) throws Exception {

		RuntimeEngine runtime = singletonManager.getRuntimeEngine(EmptyContext
				.get());
		TaskService taskService = runtime.getTaskService();

		try {
			System.out.println("approveTask (taskId = " + taskId + ") by "
					+ actorId);
			taskService.start(taskId, actorId);
			taskService.complete(taskId, actorId, null);

			// Thread.sleep(10000); // To test OptimisticLockException

		} catch (PermissionDeniedException e) {
			System.out.println("--- " + e);
			// Probably the task has already been started by other users
			throw new ProcessOperationException("The task (id = " + taskId
					+ ") has likely been started by other users ", e);
		} catch (Exception e) {
			System.out.println("--- " + e);
			throw new RuntimeException(e);
		}
	}
}
