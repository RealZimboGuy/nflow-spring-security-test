package com.github.realzimboguy.nflowspringsecuritytest.example.controller;

import com.github.realzimboguy.nflowspringsecuritytest.example.model.ExampleWorkflowRequest;
import com.github.realzimboguy.nflowspringsecuritytest.example.service.ExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/example")
public class ExampleController {

	Logger logger = LoggerFactory.getLogger(ExampleController.class);

	private final ExampleService exampleService;

	public ExampleController(ExampleService exampleService) {

		this.exampleService = exampleService;
	}

	@PostMapping("/createWorkflow")
	public ResponseEntity createWorkflow(@RequestBody ExampleWorkflowRequest request) {
		logger.info("Received request to create workflow: {}", request);
		// Create workflow
		exampleService.createWorkflow(request);

		return ResponseEntity.ok().build();
	}

}
