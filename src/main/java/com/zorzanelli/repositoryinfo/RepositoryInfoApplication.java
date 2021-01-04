package com.zorzanelli.repositoryinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RepositoryInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepositoryInfoApplication.class, args);
	}

}
