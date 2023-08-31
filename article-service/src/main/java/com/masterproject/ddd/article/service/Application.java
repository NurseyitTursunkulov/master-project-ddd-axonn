package com.masterproject.ddd.article.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(scanBasePackages = { "com.masterproject.ddd.common", "com.masterproject.ddd.article.service" })
@SpringBootApplication(scanBasePackages = { "com.masterproject.ddd.*.*" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
