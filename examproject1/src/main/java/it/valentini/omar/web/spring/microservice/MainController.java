package it.valentini.omar.web.spring.microservice;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@GetMapping(value="/stats")
	@ResponseBody
	public static String getStats(@RequestParam(name = "field") String field) throws ResourceNotFoundException {
		try {
			return DatasetHelper.getStats(field).toString();
		} catch (FieldNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ResourceNotFoundException();
		}
	}
}
