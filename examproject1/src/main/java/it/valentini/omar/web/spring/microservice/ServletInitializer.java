package it.valentini.omar.web.spring.microservice;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		DatasetInitializer.initialize();
		return application.sources(Examproject1Application.class);
	}

}
