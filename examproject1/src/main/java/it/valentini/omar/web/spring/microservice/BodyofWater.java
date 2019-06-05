package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.Collection;

public class BodyofWater {
	private String Name;
	private float Surface;
	private float Surface2;
	@Override
	public String toString() {
		return "BodyofWater [Name=" + Name + ", Surface=" + Surface + ", Surface2=" + Surface2 + "]";
	}
	BodyofWater(String name){
		this.Name = name;
	}
	BodyofWater(String name, float surface, float surface2){
		this.Name = name;
		this.Surface = surface;
		this.Surface2 = surface2;
	}
	
	public String getName() {
		return Name;
	}
	public float getSurface() {
		return Surface;
	}
	public float getSurface2() {
		return Surface2;
	}
	public static ArrayList<String[]> getMetadata() {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<>();
		list.add(DatasetHelper.metadataRow("BodyofWater.Name", "Denominazione_Luogo", "String"));
		list.add(DatasetHelper.metadataRow("BodyofWater.Surface", "superficie", "float"));
		list.add(DatasetHelper.metadataRow("BodyofWater.Surface", "superficie specchio acqua", "float"));
		return list;
	}
}
