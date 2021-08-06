package com.learn.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HttpclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpclientApplication.class, args);
	}

}
