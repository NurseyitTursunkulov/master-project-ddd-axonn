package com.masterproject.ddd.user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(scanBasePackages = { "com.masterproject.ddd.common", "com.masterproject.ddd.user.service" })
@SpringBootApplication(scanBasePackages = { "com.masterproject.ddd.*.*" })
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
