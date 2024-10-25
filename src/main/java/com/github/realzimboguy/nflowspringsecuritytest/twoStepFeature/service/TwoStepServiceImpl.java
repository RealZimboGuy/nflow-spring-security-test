package com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.service;

import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowConfirm;
import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowRequest;
import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.workflow.TwoStepWorkflow;
import io.nflow.engine.internal.dao.WorkflowInstanceDao;
import io.nflow.engine.service.WorkflowInstanceService;
import io.nflow.engine.workflow.instance.QueryWorkflowInstances;
import io.nflow.engine.workflow.instance.WorkflowInstance;
import io.nflow.engine.workflow.instance.WorkflowInstanceAction;
import io.nflow.engine.workflow.instance.WorkflowInstanceFactory;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static io.nflow.engine.workflow.instance.WorkflowInstanceAction.WorkflowActionType.externalChange;
import static org.joda.time.DateTime.now;

@Service
public class TwoStepServiceImpl implements TwoStepService {

	private Logger logger = LoggerFactory.getLogger(TwoStepServiceImpl.class);

	private final WorkflowInstanceService workflowInstanceService;
	private final     WorkflowInstanceFactory workflowInstanceFactory;

	@Inject
	private WorkflowInstanceDao     workflowInstanceDao;
	@Inject
	private WorkflowInstanceService workflowInstances;

	public TwoStepServiceImpl(WorkflowInstanceService workflowInstanceService, WorkflowInstanceFactory workflowInstanceFactory) {

		this.workflowInstanceService = workflowInstanceService;
		this.workflowInstanceFactory = workflowInstanceFactory;
	}

	@Override
	public void createWorkflow(TwoStepWorkflowRequest request) {

		long id = workflowInstanceService.insertWorkflowInstance(
				workflowInstanceFactory
						.newWorkflowInstanceBuilder()
						.setType(TwoStepWorkflow.TYPE)
						.putStateVariable(TwoStepWorkflow.StateVars._1_INPUT_ONE, request)
						.setExternalId(request.getId())
						.setBusinessKey(request.getName())
						.build());

				logger.info("Created workflow instance with id: {}", id);

	}

	@Override
	public void transition(TwoStepWorkflowConfirm confirm) {

		QueryWorkflowInstances query = (new QueryWorkflowInstances.Builder())
				.addTypes(TwoStepWorkflow.TYPE)
				.setExternalId(confirm.getId())
				.setIncludeCurrentStateVariables(true)
				.build();

		Collection<WorkflowInstance> instances = workflowInstanceService.listWorkflowInstances(query);

		if (instances.isEmpty()) {
			logger.error("No workflow instances found for external id: {}", confirm.getId());
			return;
		}
		WorkflowInstance instance = instances.iterator().next();

		WorkflowInstance.Builder builder = workflowInstanceFactory.newWorkflowInstanceBuilder().setId(instance.id)
				.putStateVariable(TwoStepWorkflow.StateVars._2_INPUT_TWO,confirm)
				.setNextActivation(now());
		builder.setState(TwoStepWorkflow.State.processSecondSteps);
		instance = builder.build();
		instance.getChangedStateVariables().forEach(workflowInstanceDao::checkStateVariableValueLength);


		workflowInstances.updateWorkflowInstance(instance,
				new WorkflowInstanceAction.Builder(instance)
				.setType(externalChange)
				.setStateText("transition called from external")
				.setExecutionEnd(now())
				.build());


	}
}
