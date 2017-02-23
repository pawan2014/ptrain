package com.ptrain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:my.properties")
public class EventSimulatorMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventSimulatorMainApplication.class, args);
	}
}
