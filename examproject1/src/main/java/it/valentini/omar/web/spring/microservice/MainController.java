package it.valentini.omar.web.spring.microservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/metadata")
	@ResponseBody
	public String getMetadata() {
		return DatasetHelper.getMetadata().toString();
	}
}
