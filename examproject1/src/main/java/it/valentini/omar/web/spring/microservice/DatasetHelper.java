package it.valentini.omar.web.spring.microservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;


public class DatasetHelper {
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
			InputStream in = new BufferedInputStream(connection.getInputStream());
			//Reading input stream
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder streamdata = new StringBuilder();
            for (String line; (line = reader.readLine()) != null; ) {
                streamdata.append(line).append('\n');
            }
            return streamdata.toString();
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
		try {
			//Navigating JSON
			JSONObject DatasetJSON = new JSONObject(getStreamData(DatasetJSON_URL));
			DatasetJSON = DatasetJSON.getJSONObject(JSONkey0);
			JSONArray temp = DatasetJSON.getJSONArray(JSONkey1);
			JSONObject DatasetJson = temp.getJSONObject(JSONindex);
			//Downloading CSV
			return getStreamData(DatasetJson.getString(JSONkey2));
		}
		catch(IOException e) {
			System.out.println("Failed to download dataset");
			e.printStackTrace();
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
	public static JSONArray getMetadata() {
		ArrayList<String[]> metadata = RiverDomain.getMetadata();
		JSONArray obj = new JSONArray();
		for (int i = 0; i<metadata.size(); i++) {
			JSONObject temp = new JSONObject();
			temp.put("Alias", metadata.get(i)[0]);
			temp.put("sourceField", metadata.get(i)[1]);
			temp.put("Type", metadata.get(i)[2]);
			obj.put(temp);
		}
		return obj;
	}
	public static String[] metadataRow(String Alias, String sourceField, String Type) {
		String[] temp = new String[3];
		temp[0] = Alias;
		temp[1] = sourceField;
		temp[2] = Type;
		return temp;
	}
	public static JSONArray getData() {
		// TODO Auto-generated method stub
		Iterator<RiverDomain> dataiterator = Dataset.iterator();
		JSONArray response = new JSONArray();
		while (dataiterator.hasNext()) {
			RiverDomain record = (RiverDomain) dataiterator.next();
			ArrayList<String[]> recordvalues = record.getData();
			JSONObject obj = new JSONObject();
			for (int i = 0; i <recordvalues.size(); i ++) obj.put(recordvalues.get(i)[0], recordvalues.get(i)[1]);
			response.put(obj);
		}
		return response;
	}
	public static JSONObject getStats(String fieldname) throws FieldNotFoundException{
		// TODO Auto-generated method stub
		if (!RiverDomain.containsField(fieldname)) throw new FieldNotFoundException();
		JSONObject returned = new JSONObject();
		returned.put("Call status", "Field found");
		return returned;
	}

}
