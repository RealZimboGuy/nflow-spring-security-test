package com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.workflow;

import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowRequest;
import io.nflow.engine.workflow.definition.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static io.nflow.engine.workflow.definition.WorkflowStateType.*;

@Component
public class TwoStepWorkflow extends WorkflowDefinition {

	Logger logger = LoggerFactory.getLogger(TwoStepWorkflow.class);

	public static final String TYPE = "TwoStepWorkflow";

	public enum State implements WorkflowState {
		validate(start, "validate"),
		process(normal, "process"),
		waitForConfirm(wait, "waitForConfirm"),
		timeout(end, "timeout"),
		processSecondSteps(normal, "processSecondSteps"),
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

	public TwoStepWorkflow() {
		super(TYPE, State.validate, State.error);
		permit(State.validate, State.process);
		permit(State.process, State.waitForConfirm);
		permit(State.waitForConfirm, State.processSecondSteps);
		permit(State.processSecondSteps, State.done);
	}

	public NextAction validate(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) TwoStepWorkflowRequest request) {
		logger.info("Validating request: {}", request);
		return NextAction.moveToStateAfter(State.process, DateTime.now().plusSeconds(10), "validate done");
	}
	public NextAction process(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) TwoStepWorkflowRequest request) {
		logger.info("process request: {}", request);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return NextAction.moveToStateAfter(State.waitForConfirm, DateTime.now().plusMinutes(30),"validate done");
	}
	public NextAction waitForConfirm(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) TwoStepWorkflowRequest request) {
		logger.info("timeout request: {}", request);

		return NextAction.moveToState(State.timeout, "waitForConfirm timeout");
	}
	public NextAction processSecondSteps(StateExecution execution,
	                           @StateVar(StateVars._1_INPUT_ONE) TwoStepWorkflowRequest request) {

		return NextAction.moveToState(State.done, "processSecondSteps done");
	}
}
