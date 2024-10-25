package com.github.realzimboguy.nflowspringsecuritytest.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ApiUserDetails implements UserDetails {

	private String apiKey;

	public ApiUserDetails(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of();
	}

	@Override
	public String getPassword() {

		return "";
	}

	@Override
	public String getUsername() {

		return "";
	}
}
