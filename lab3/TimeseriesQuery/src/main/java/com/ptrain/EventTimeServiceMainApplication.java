package com.ptrain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SuppressWarnings("javadoc")
@SpringBootApplication
public class EventTimeServiceMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventTimeServiceMainApplication.class, args);
	}

	/*@Component
	public class MyBean implements CommandLineRunner {

		@Autowired
		EventTimeserviceDataService eventTimeserviceDataService;
		
		public void run(String... args) {
			eventTimeserviceDataService.maintenanceRecords(null, null, null);
			System.out.println("Hellow....");
		}

	}*/

}
