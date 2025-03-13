package com.coursework_backend.creatingbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseworkBackendApplication implements CommandLineRunner {

	// Inject the TicketingSystemService to manage ticketing operations
	@Autowired
	private TicketingSystemService ticketingSystemService;

	public static void main(String[] args) {
		SpringApplication.run(CourseworkBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			TicketingSystemService.configuration = TicketingSystemService.loadOrPromptConfiguration();
		}
		catch (Exception e){}
	}
}
