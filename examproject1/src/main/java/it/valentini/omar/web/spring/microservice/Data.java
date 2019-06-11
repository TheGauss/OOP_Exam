package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.Map;

public interface Data {
	public ArrayList<Map<String, String>> getMetadata();
	public Map<String, Object> getData();

}
