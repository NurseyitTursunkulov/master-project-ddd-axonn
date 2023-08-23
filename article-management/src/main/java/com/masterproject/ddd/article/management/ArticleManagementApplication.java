package com.masterproject.ddd.article.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(scanBasePackages = ("com.masterproject.ddd.common", "com.masterproject.ddd.user.service"))
@SpringBootApplication(scanBasePackages = { "com.masterproject.ddd.*.*" })
public class ArticleManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticleManagementApplication.class, args);
	}

}
