package com.java.JWT.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import com.java.JWT.entity.User;
import com.java.JWT.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private UserRepository userRepo;
	
	
	
	@Autowired
	private JWTService jwtService;



	// this is for username and password login

	public String verify(User user) {
		try {

			System.out.println("Attempting to authenticate user: " + user.getUsername());

			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
					);

			if (authentication.isAuthenticated()) {
				System.out.println("Authentication successful for user: " + user.getUsername());
				return "success";
			} else {
				System.out.println("Authentication failed for user: " + user.getUsername());
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			return "error";
		}
	}


	// this is for JWT token generation once the user is logged in 
	public String tokenVerification(User user) {
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(auth.isAuthenticated()) {
			return jwtService.generateToken(user.getUsername());
		}else {
			return"fail";
		}
	}

	public User save(User user) {
		return userRepo.save(user);
	}

	public List<User> getAll(){
		return userRepo.findAll();
	}


}
