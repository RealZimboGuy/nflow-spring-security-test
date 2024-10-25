package com.github.realzimboguy.nflowspringsecuritytest.config.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	//for /explorer return a redirect to       /explorer/index.html
	@RequestMapping("/explorer")
	public String explorer() {
		return "redirect:/explorer/index.html";
	}
}
