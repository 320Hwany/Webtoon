package com.webtoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WebtoonApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebtoonApplication.class, args);
	}
}
