package com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.service;

import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowConfirm;
import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowRequest;

public interface TwoStepService {

	void createWorkflow(TwoStepWorkflowRequest request);

	void transition(TwoStepWorkflowConfirm confirm);
}
