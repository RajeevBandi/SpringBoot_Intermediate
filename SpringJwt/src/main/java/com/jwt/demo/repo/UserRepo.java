package com.jwt.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.demo.model.User;

public interface UserRepo  extends JpaRepository<User, Integer>{
	
	Optional<User> findByUsername(String username);

}
