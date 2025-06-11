package com.liwanag.gamification;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GamificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamificationServiceApplication.class, args);
	}
}