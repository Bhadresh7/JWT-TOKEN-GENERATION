package com.java.JWT.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.java.JWT.service.customUserDetailsService;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class webSecurity {

	@Autowired
	private JwtFilter jwtFilter;	

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(customize -> customize.disable())
				.httpBasic(Customizer.withDefaults()) // used for form login 
				.authorizeHttpRequests(request -> 
				request
				.requestMatchers("/login", "/register").permitAll()
				.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}



	// authentication manager talks to the authentication provider
	@Bean 	 
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	//this is for custome user name and password
	@Bean
	UserDetailsService userDetailsService() {
		return new customUserDetailsService();	 
	}





	// here the authentication provider uses DAO provider to 
	//verify the username and password in the database
	@Bean
	AuthenticationProvider provider () {
		DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		provider.setUserDetailsService(userDetailsService());
		return provider;

	}
}

