package it.valentini.omar.web.spring.microservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;


public class DatasetInitializer {
	private static final String DatasetJSON_URL = "https://www.dati.gov.it/api/3/action/package_show?id=a1dee418-ddd7-40c6-ad6c-7b35aa31f61a";
	private static final String JSONkey1 = "resources";
	private static final String JSONkey2 = "url";
	private static final int JSONindex = 0;
	private static String getStreamData(String path) throws IOException {
		HttpsURLConnection connection = null;
		try {
			//Connecting to the server
			URL url = new URL(path);
			connection = (HttpsURLConnection) url.openConnection();
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
	private static void parse(String data) {
		
	}
	public static void initialize() {
		parse(fetch());
	}
}
