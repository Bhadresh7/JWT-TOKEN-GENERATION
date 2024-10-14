package com.java.JWT.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.JWT.entity.User;
import com.java.JWT.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/private")
	public String link() {
		return "This is a private link";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody User user) {
		return userService.tokenVerification(user);
	}
	
	@PostMapping("/register")
	public User register(@RequestBody User user) {
		return userService.save(user);
	}
	
	@GetMapping("/all")
	public List<User> fetchAllUser() {
		return userService.getAll();
	}
	
}
