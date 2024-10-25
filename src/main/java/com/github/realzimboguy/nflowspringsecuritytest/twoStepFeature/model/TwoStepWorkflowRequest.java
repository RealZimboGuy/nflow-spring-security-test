package com.github.realzimboguy.nflowspringsecuritytest.twoStepFeature.model;

public class TwoStepWorkflowRequest {

	private String id;
	private String name;
	private String description;

	public TwoStepWorkflowRequest() {

	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getId() {

		return id;
	}

	public void setId(String id) {

		this.id = id;
	}

	@Override
	public String toString() {

		return "ExampleWorkflowRequest{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
