package com.github.realzimboguy.nflowspringsecuritytest.example.workflow;

import com.github.realzimboguy.nflowspringsecuritytest.example.model.ExampleWorkflowRequest;
import io.nflow.engine.workflow.definition.*;
import org.joda.time.DateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static io.nflow.engine.workflow.definition.WorkflowStateType.*;

@Component
public class ExampleWorkflow extends WorkflowDefinition {

	Logger logger = LoggerFactory.getLogger(ExampleWorkflow.class);

	public static final String TYPE = "ExampleWorkflow";

	public enum State implements io.nflow.engine.workflow.definition.WorkflowState {
		validate(start, "validate"),
		process(normal, "process"),
		done(end, "done"),
		error(manual, "Error state");

		private WorkflowStateType type;
		private String description;

		State(WorkflowStateType type, String description) {
			this.type = type;
			this.description = description;
		}

		@Override
		public WorkflowStateType getType() {
			return type;
		}

		@Override
		public String getDescription() {
			return description;
		}
	}

	public static class StateVars {

		public static final String _1_INPUT_ONE    = "1_INPUT_ONE";
		public static final String _2_INPUT_TWO = "2_INPUT_TWO";
	}

	public ExampleWorkflow() {
		super(TYPE, State.validate, State.error);
		permit(State.validate, State.process);
		permit(State.process, State.done);
	}

	public NextAction validate(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) ExampleWorkflowRequest request) {
		logger.info("Validating request: {}", request);
		return NextAction.moveToStateAfter(State.process, DateTime.now().plusSeconds(10), "validate done");
	}
	public NextAction process(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) ExampleWorkflowRequest request) {
		logger.info("process request: {}", request);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return NextAction.moveToState(State.done, "validate done");
	}
}
