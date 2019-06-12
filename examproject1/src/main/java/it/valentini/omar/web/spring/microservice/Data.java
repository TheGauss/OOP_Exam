package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.Map;
/**
 * This represents 1 single record of the data set
 * 
 * @author Valentini Omar
 */
public interface Data {

	/**
	 * @return Returns all the metadata that is present in that row
	 * Example: object 1 of the arraylist:
	 * Key: "Field Name" Value: "Distance"
	 * Key: "Type" Value: "int"
	 */
	public ArrayList<Map<String, String>> getMetadata();
	
	/**
	 * @return Gets the data in that particular record
	 */
	public Map<String, Object> getData();

}
