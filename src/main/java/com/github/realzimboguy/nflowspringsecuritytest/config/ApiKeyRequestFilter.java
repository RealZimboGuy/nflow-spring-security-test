package com.github.realzimboguy.nflowspringsecuritytest.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ApiKeyRequestFilter extends OncePerRequestFilter {

	private final AppConfig appConfig;

	public ApiKeyRequestFilter(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


		try {

			if (request.getHeader("X-API-KEY") != null) {

				String apiKey = request.getHeader("X-API-KEY");

				if (!apiKey.equals(appConfig.getApiKey())) {
					logger.warn("Invalid API Key:" + apiKey);
					//return 401
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

				UserDetails userDetails = new ApiUserDetails(apiKey);
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails,
								null,
								userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Exception occurred ", e);
		}
		filterChain.doFilter(request, response);

	}
}
