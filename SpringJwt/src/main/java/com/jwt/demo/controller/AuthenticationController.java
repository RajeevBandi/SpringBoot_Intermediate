package com.jwt.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jwt.demo.model.AuthenticationResponse;
import com.jwt.demo.model.User;
import com.jwt.demo.service.AuthenticationService;

@Controller
public class AuthenticationController {
	
	private final AuthenticationService authService;

	public AuthenticationController(AuthenticationService authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
			@RequestBody User request){
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(
			@RequestBody User request){
		return ResponseEntity.ok(authService.authenticate(request));
	}
	

}
