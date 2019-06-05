package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;

public class RiverDomain {
	private Person person;
	private String company;
	private String locationName;
	private String locationCode;
	private BodyofWater water;
	int duration;
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
		if (data[0].length()>2 && data[1].length()>2)person = new Person(data[0], data[1]);
		else person = null;
		if (data[2].length()>2) company = data[2];
		else company = null;
		if (data[3].length()>2) locationCode = data[3];
		else locationCode = null;
		if (data[4].length()>2) locationName = data[4];
		else locationName = null;
		water = new BodyofWater(data[5], DatasetHelper.numberFinder(data[6]), DatasetHelper.numberFinder(data[7]));
		duration = (int) DatasetHelper.numberFinder(data[8]);
	}
	@Override
	public String toString() {
		String returned = "River Domain: { ";
		if (person != null) returned += person.toString() + ", ";
		if (company != null) returned += " Company: " + company + ", ";
		if (locationCode != null) returned += " Location code: " + locationCode + ", ";
		if (locationName != null) returned += " Location name: " + locationName + ", ";
		if (duration != 0) returned += " Duration: " + duration + "}";
		else returned += " Duration: not determinated}";
		return returned;
	}
	private boolean containsCompanyName(String a) {
		if(a.contains("SRL") || a.contains("S.R.L.") || a.contains("s.r.l.") || a.contains("srl")) return true;
		if(a.contains("SPA") || a.contains("S.P.A.") || a.contains("s.p.a.") || a.contains("spa")) return true;
		if(a.contains("SNC") || a.contains("S.N.C")|| a.contains("s.n.c.") || a.contains("snc")) return true;
		return false;
	}
	public static ArrayList<String[]> getMetadata() {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<>();
		list.addAll(Person.getMetadata());
		list.add(DatasetHelper.metadataRow("Company", "Ragione Sociale", "String"));
		list.add(DatasetHelper.metadataRow("locationCode", "ID_Comune", "String"));
		list.add(DatasetHelper.metadataRow("locatioName", "Comune del bene oggetto di Concessione", "String"));
		list.addAll(BodyofWater.getMetadata());
		list.add(DatasetHelper.metadataRow("duration", "Durata concessione", "int"));
		return list;
	}
	public static String[] metadataRow(String A, String B, String C) {
		String[] temp = new String[3];
		temp[0] = A;
		temp[1] = B;
		temp[2] = C;
		return temp;
	}
}
