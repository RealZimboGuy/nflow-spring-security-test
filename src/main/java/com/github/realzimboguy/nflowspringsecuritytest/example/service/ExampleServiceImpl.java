package com.github.realzimboguy.nflowspringsecuritytest.example.service;

import com.github.realzimboguy.nflowspringsecuritytest.example.model.ExampleWorkflowRequest;
import com.github.realzimboguy.nflowspringsecuritytest.example.workflow.ExampleWorkflow;
import io.nflow.engine.internal.dao.WorkflowInstanceDao;
import io.nflow.engine.service.WorkflowInstanceService;
import io.nflow.engine.workflow.instance.WorkflowInstanceFactory;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl implements ExampleService {

	private Logger logger = LoggerFactory.getLogger(ExampleServiceImpl.class);

	private final WorkflowInstanceService workflowInstanceService;
	private final     WorkflowInstanceFactory workflowInstanceFactory;

	@Inject
	private WorkflowInstanceDao     workflowInstanceDao;
	@Inject
	private WorkflowInstanceService workflowInstances;

	public ExampleServiceImpl(WorkflowInstanceService workflowInstanceService, WorkflowInstanceFactory workflowInstanceFactory) {

		this.workflowInstanceService = workflowInstanceService;
		this.workflowInstanceFactory = workflowInstanceFactory;
	}

	@Override
	public void createWorkflow(ExampleWorkflowRequest request) {

		long id = workflowInstanceService.insertWorkflowInstance(
				workflowInstanceFactory
						.newWorkflowInstanceBuilder()
						.setType(ExampleWorkflow.TYPE)
						.putStateVariable(ExampleWorkflow.StateVars._1_INPUT_ONE, request)
						.setExternalId(request.getId())
						.setBusinessKey(request.getName())
						.build());

				logger.info("Created workflow instance with id: {}", id);

	}
}
