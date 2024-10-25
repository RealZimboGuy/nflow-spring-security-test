package com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.controller;

import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowConfirm;
import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model.TwoStepWorkflowRequest;
import com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.service.TwoStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/twostep")
public class TwoStepController {

	Logger logger = LoggerFactory.getLogger(TwoStepController.class);

	private final TwoStepService twoStepService;

	public TwoStepController(TwoStepService twoStepService) {

		this.twoStepService = twoStepService;
	}

	@PostMapping("/createWorkflow")
	public ResponseEntity createWorkflow(@RequestBody TwoStepWorkflowRequest request) {
		logger.info("Received request to create workflow: {}", request);
		// Create workflow
		twoStepService.createWorkflow(request);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/transition")
	public ResponseEntity transition(@RequestBody TwoStepWorkflowConfirm confirm) {
		logger.info("Received request to transition workflow: {}", confirm);
		// Create workflow
		twoStepService.transition(confirm);

		return ResponseEntity.ok().build();
	}

}
