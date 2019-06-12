package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RiverDomain implements Data {
	private String Name;
	private String Surname;
	private String company;
	private String locationName;
	private String locationCode;
	private String BodyofWaterName;
	private float surface;
	private float surface2;
	int duration;
	/**
	 * 
	 * @param data each string contains a part of the data, in this order: name, surname, company, locationName, locationCode, BodyofWater, surface, surface 2, duration
	 * @throws MalformedRowException if could not parse the data
	 */
	RiverDomain(String[] data)throws MalformedRowException{
		if (data.length<9) throw new MalformedRowException();
		//Looks like in the data set there are a lot of companies that want to be persons
		if(containsCompanyName(data[0])) {
			data[2]=data[0];
			data[0]= "";
			data[1]= "";
			}
		if(containsCompanyName(data[1])) {
			data[2]=data[0]+ " " + data[1];
			data[0]= "";
			data[1]= "";
			}
		//Returning an exception if the raw misses crucial data
		if ((data[0].length()<2 || data[1].length()<2) && data[2].length()<2) throw new MalformedRowException();
		if (data[5].length()<2) throw new MalformedRowException();
		//Actually initializing the river domain
		if (data[0].length()>2 && data[1].length()>2) {
			Name = data[0];
			Surname = data[1];
		}
		else {
			Name = null;
			Surname = null;
		}
		if (data[2].length()>2) company = data[2];
		else company = null;
		if (data[3].length()>2) locationCode = data[3];
		else locationCode = null;
		if (data[4].length()>2) locationName = data[4];
		else locationName = null;
		BodyofWaterName = data[5];
		surface = DatasetHelper.numberFinder(data[6]);
		surface2 = DatasetHelper.numberFinder(data[7]);
		duration = (int) DatasetHelper.numberFinder(data[8]);
	}
	private boolean containsCompanyName(String a) {
		if(a.contains("SRL") || a.contains("S.R.L.") || a.contains("s.r.l.") || a.contains("srl")) return true;
		if(a.contains("SPA") || a.contains("S.P.A.") || a.contains("s.p.a.") || a.contains("spa")) return true;
		if(a.contains("SNC") || a.contains("S.N.C")|| a.contains("s.n.c.") || a.contains("snc")) return true;
		return false;
	}
	@Override
	public ArrayList<Map<String, String>> getMetadata() {
		// TODO Auto-generated method stub
		ArrayList<Map<String, String>> list = new ArrayList<>();
		list.add(metadataRow("Name", "Nome", "String"));
		list.add(metadataRow("Surname", "Cognome", "String"));
		list.add(metadataRow("Company", "Ragione Sociale", "String"));
		list.add(metadataRow("locationCode", "ID_Comune", "String"));
		list.add(metadataRow("locatioName", "Comune del bene oggetto di Concessione", "String"));
		list.add(metadataRow("BodyofWater/Name", "Denominazione_Luogo", "String"));
		list.add(metadataRow("BodyofWater/Surface", "superficie", "float"));
		list.add(metadataRow("BodyofWater/Surface2", "superficie specchio acqua", "float"));
		list.add(metadataRow("duration", "Durata concessione", "int"));
		return list;
	}
	
	private static Map<String, String> metadataRow(String Alias, String sourceField, String Type) {
		Map<String, String> temp = new HashMap<>();
		temp.put("Alias", Alias);
		temp.put("Source Field", sourceField);
		temp.put("Type", Type);
		return temp;
	}
	@Override
	public Map<String, Object> getData() {
		// TODO Auto-generated method stub
		Map<String, Object> data = new HashMap<>();
		if (Name != null) {
			data.put("Name", Name);
			data.put("Surname", Surname);
		}
		if (company != null) data.put("Company", company);
		if (locationCode != null) data.put("locationCode", locationCode);
		if (locationName != null) data.put("locationName", locationName);
		data.put("BodyofWaterName", BodyofWaterName);
		if (surface>0)data.put("Surface", surface);
		if (surface2>0)data.put("Surface2", surface2);
		if (duration>0) data.put("Duration", duration);
		return data;
	}
	public String getName() {
		return Name;
	}
	public String getSurname() {
		return Surname;
	}
	public String getCompany() {
		return company;
	}
	public String getLocationname() {
		return locationName;
	}
	public String getLocationcode() {
		return locationCode;
	}
	public String getBodyofwatername() {
		return BodyofWaterName;
	}
	public float getSurface() {
		return surface;
	}
	public float getSurface2() {
		return surface2;
	}
	public int getDuration() {
		return duration;
	}
}
