package com.java.JWT.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.JWT.entity.User;
import com.java.JWT.entity.UserPrincipal;
import com.java.JWT.repository.UserRepository;


@Service
public class customUserDetailsService implements UserDetailsService  {

	@Autowired
	private UserRepository userRepo;



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found ");
		}
		return new UserPrincipal(user);
	}

}
