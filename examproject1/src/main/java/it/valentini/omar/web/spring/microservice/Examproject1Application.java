package it.valentini.omar.web.spring.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Examproject1Application {

	public static void main(String[] args) {
		DatasetHelper.initialize();
		SpringApplication.run(Examproject1Application.class, args);
	}
	
}
