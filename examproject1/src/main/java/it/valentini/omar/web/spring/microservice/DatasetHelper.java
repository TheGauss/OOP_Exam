package it.valentini.omar.web.spring.microservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;



public class DatasetHelper{
	private static final String DatasetJSON_URL = "https://www.dati.gov.it/api/3/action/package_show?id=a1dee418-ddd7-40c6-ad6c-7b35aa31f61a";
	private static final String JSONkey0 = "result";
	private static final String JSONkey1 = "resources";
	private static final String JSONkey2 = "url";
	private static final String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";
	private static final int JSONindex = 0;
	private static HashSet<RiverDomain> Dataset = new HashSet<>();
	private static String getStreamData(String path) throws IOException {
		HttpURLConnection connection = null;
		try {
			//Connecting to the server
			URL url = new URL(path);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("User-Agent", UserAgent);
            return datasetBuilder(connection.getInputStream());
		}
		catch(IOException e) {
			e.printStackTrace();
			throw new IOException();
		}
		finally {
			if (connection != null) connection.disconnect();
		}
	}
	private static String fetch() {
		File file = new File("data.csv");
		try {
			return datasetBuilder(new BufferedInputStream(new FileInputStream(file)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			try {
				//Navigating JSON
				JSONObject DatasetJSON = new JSONObject(getStreamData(DatasetJSON_URL));
				DatasetJSON = DatasetJSON.getJSONObject(JSONkey0);
				JSONArray temp = DatasetJSON.getJSONArray(JSONkey1);
				JSONObject DatasetJson = temp.getJSONObject(JSONindex);
				//Downloading CSV
				String data = getStreamData(DatasetJson.getString(JSONkey2));
				//Saving the data set+
				FileWriter out = new FileWriter(file);
				out.write(data);				
				return data;
			}
			catch(IOException e) {
				System.out.println("Failed to download dataset");
				e.printStackTrace();
			}
		}
		return null;
	}
	private static void parse(String input) {
			String[] data = input.split("\n");
			System.out.println("Downloaded dataset size: " + data.length + " rows");
			for (int i = 0; i < data.length; i++) {
				try {
					Dataset.add(new RiverDomain(data[i].split(";", -1)));
				}catch (MalformedRowException e) {
					System.out.println("Could not parse the row '" + data[i] +"'");
				}
			}
			System.out.println("Parsed dataset size: " + Dataset.size() + " rows");
	}
	public static void initialize() {
		parse(fetch());
	}
	public static float numberFinder(String input) {
		//Default = 0
		if (input == null) return 0;
		// Converting thousands/float notation
		String[] temp = input.split(",");
		temp[0].replace(".", ",");
		String[] data = null;
		if (temp.length>1) data = (temp[0]+"."+temp[1]).split(" ");
		else data = temp[0].split(" ");
		//Finding the number (if any)
		for (int i = 0; i<data.length; i++) {
			try {
				return Float.parseFloat(data[i]);
			}catch (Exception e) {}
		}
		//No number = return default
		return 0;
	}
	public static ArrayList<Map<String, String>> getMetadata() {
		return Dataset.iterator().next().getMetadata();
	}

	public static ArrayList<Map<String, Object>> getData() {
		// Initializing iterator
		Iterator<RiverDomain> dataiterator = Dataset.iterator();
		ArrayList<Map<String, Object>> response = new ArrayList<>();
		//Getting data
		while (dataiterator.hasNext()) {
			RiverDomain record = (RiverDomain) dataiterator.next();
			Map<String, Object> recordvalues = record.getData();
			response.add(recordvalues);
		}
		return response;
	}
	public static Map<String, Object> getStats(String fieldname) throws FieldNotFoundException{
		// Initializing returned stats
		HashMap<String, Object> returned = new HashMap<>();
		returned.put("field", fieldname);
		try {
			//Getting the first object
			java.lang.reflect.Method method = RiverDomain.class.getMethod("get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1));
			Iterator<RiverDomain> iterator = Dataset.iterator();
			RiverDomain record = iterator.next();
			Object obj = method.invoke(record,(Object[]) null);
			//If the first object is a number, then generates number stats
			if (obj instanceof Number) {
				double total = 0, min = Double.MIN_VALUE, max = Double.MAX_VALUE;
				Number number = ((Number) obj).doubleValue();
				double value = number.doubleValue();
				ArrayList<Double> numbers = new ArrayList<>();
				if (value != 0) {
					numbers.add(Double.valueOf(value));
					total += value;
					min = value;
					max = value;
				}
				while (iterator.hasNext()) {
					record = iterator.next();
					obj = method.invoke(record,(Object[]) null);
					number = ((Number) obj).doubleValue();
					value = number.doubleValue();
					if (value!=0) {
						numbers.add(Double.valueOf(value));
						total+=value;
						if (value>max) max = value;
						if (value<min) min = value;
					}
				}
				double avg = total/numbers.size();
				returned.put("sum", total);
				returned.put("avg", avg);
				returned.put("max", max);
				returned.put("min", min);
				returned.put("count", numbers.size());
				double var = 0;
				for (int i = 0; i<numbers.size(); i++) {
					double dev = numbers.get(i).doubleValue();
					var += dev*dev;
				}
				var /= numbers.size();
				returned.put("std", Math.sqrt(var));
				}
			// If the first object is not a number
			else {
				//Put everything not null in an array: instanceof always returns false for nulls
				ArrayList<Object> notNulls = new ArrayList<>();
				if (obj != null) notNulls.add(obj);
				while (iterator.hasNext()) {
					 record = iterator.next();
					 obj = method.invoke(record, (Object[]) null);
					 if (obj!=null) notNulls.add(obj);
				}
				//If everything is null or the field is not a string field, can't do anything with that
				if (notNulls.isEmpty()) throw new FieldNotFoundException();
				if (!(notNulls.get(0) instanceof String)) throw new FieldNotFoundException();
				//If it's a string, get string stats
				Map<String, Integer> strings = new HashMap<>();
				for (int i = 0; i < notNulls.size(); i++) {
					String string = (String) notNulls.get(i);
					if (!strings.containsKey(string)) strings.put(string, 1);
					else strings.put(string, strings.get(string).intValue()+1);
				}
				for (int i = 0; i < notNulls.size(); i++) returned.put((String) notNulls.get(i), strings.get(notNulls.get(i)));
			}
			return returned;
		} catch (Exception e) {
			throw new FieldNotFoundException();
		}
	}
	private static String datasetBuilder(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuilder streamdata = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            streamdata.append(line).append('\n');
        }
        return streamdata.toString();
	}
}
