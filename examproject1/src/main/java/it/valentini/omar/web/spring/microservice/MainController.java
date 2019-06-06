package it.valentini.omar.web.spring.microservice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping("/metadata")
	@ResponseBody
	public static String getMetadata() {
		return DatasetHelper.getMetadata().toString();
	}
	@GetMapping("/data")
	@ResponseBody
	public static String getData() {
		return DatasetHelper.getData().toString();
	}
	@GetMapping("/stats")
	@ResponseBody
	public static String getStats() {
		return "Work in progress";
	}
}
