package com.github.realzimboguy.nflowspringsecuritytest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Value("${nflow.apiKey}")
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

}
