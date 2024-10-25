package com.github.realzimboguy.nflowspringsecuritytest;

import io.nflow.rest.config.RestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RestConfiguration.class)
public class NflowSpringSecurityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(NflowSpringSecurityTestApplication.class, args);
	}

}
