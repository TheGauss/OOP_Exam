package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	@GetMapping(value = "/metadata", produces = "application/json")
	@ResponseBody
	public static ArrayList<Map<String, String>> getMetadata() {
		return DatasetHelper.getMetadata();
	}
	@GetMapping(value = "/data", produces = "application/json")
	@ResponseBody
	public static ArrayList<Map<String, Object>> getData() {
		return DatasetHelper.getData();
	}
	@GetMapping(value="/stats", produces = "application/json")
	@ResponseBody
	public static Map<String, Object> getStats(@RequestParam(name = "field") String field) throws ResourceNotFoundException {
		try {
			return DatasetHelper.getStats(field);
		} catch (FieldNotFoundException e) {
			// TODO Auto-generated catch block
			throw new ResourceNotFoundException();
		}
	}
}
