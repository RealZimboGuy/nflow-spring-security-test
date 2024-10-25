package com.github.realzimboguy.nflowspringsecuritytest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	private final Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);

	private final AppConfig appConfig;

	public SpringSecurityConfig(AppConfig appConfig) {

		this.appConfig = appConfig;
	}


	@Bean
	@Order(1)
	public SecurityFilterChain springNflowSecurityFilterChain(HttpSecurity http) throws Exception {

		http.securityMatcher(new OrRequestMatcher(antMatcher("/nflow/**"),
						antMatcher("/explorer/**"),
						antMatcher("/login"))
				)
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(
								"/login",
								"/error/**",
								"/error"
						).permitAll()
						.anyRequest().authenticated()
				).formLogin((form) -> form
						.successForwardUrl("/explorer")
						.permitAll()
				).logout((logout) -> logout
						.logoutUrl("/logout")
						.permitAll());

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers(
								"nflow/**",
								"login",
								"explorer/**").permitAll()
						.anyRequest().authenticated()
				).sessionManagement((session) -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				).exceptionHandling((exception) -> exception.authenticationEntryPoint(
						(request, response, authException) -> {
							//something went wrong
							logger.error("Error", authException);
						}

				));

		http.addFilterBefore(authenticateApiKey(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	@Bean
	public ApiKeyRequestFilter authenticateApiKey() {

		return new ApiKeyRequestFilter(appConfig);
	}


}
