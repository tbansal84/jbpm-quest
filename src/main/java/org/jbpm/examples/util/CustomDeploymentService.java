package org.jbpm.examples.util;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.jbpm.services.api.DeploymentEventListener;
import org.jbpm.services.api.DeploymentService;
import org.jbpm.services.api.ListenerSupport;
import org.jbpm.services.api.model.DeployedUnit;
import org.jbpm.services.api.model.DeploymentUnit;
import org.kie.api.runtime.manager.RuntimeManager;

// plain CustomDeploymentService solves:
// Caused by: org.jboss.weld.exceptions.DeploymentException: WELD-001408 Unsatisfied dependencies for type [DeploymentService]
// with qualifiers [@Default] at injection point [[field] @Inject private org.jbpm.kie.services.impl.form.FormProviderServiceImpl.deploymentService]
@ApplicationScoped
public class CustomDeploymentService implements DeploymentService,
		ListenerSupport {

	public void deploy(DeploymentUnit deploymentUnit) {
	}

	public void undeploy(DeploymentUnit deploymentUnit) {
	}

	public RuntimeManager getRuntimeManager(String s) {
		return null;
	}

	public DeployedUnit getDeployedUnit(String s) {
		return null;
	}

	public Collection<DeployedUnit> getDeployedUnits() {
		return null;
	}

	public void addListener(DeploymentEventListener deploymentEventListener) {

	}

	public void removeListener(DeploymentEventListener deploymentEventListener) {

	}

	public Collection<DeploymentEventListener> getListeners() {
		return null;
	}

	public void activate(String deploymentId) {
		// TODO Auto-generated method stub
		
	}

	public void deactivate(String deploymentId) {
		// TODO Auto-generated method stub
		
	}

	public boolean isDeployed(String deploymentUnitId) {
		// TODO Auto-generated method stub
		return false;
	}

}
