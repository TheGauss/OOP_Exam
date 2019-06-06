package it.valentini.omar.web.spring.microservice;

import java.util.ArrayList;
import java.util.Collection;

public class Person {
	private String Name;
	private String Surname;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		result = prime * result + ((Surname == null) ? 0 : Surname.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		if (Surname == null) {
			if (other.Surname != null)
				return false;
		} else if (!Surname.equals(other.Surname))
			return false;
		return true;
	}
	Person(String Name, String Surname){
		this.Name = Name;
		this.Surname = Surname;
	}
	@Override
	public String toString() {
		return "Person [Name:" + Name + ", Surname:" + Surname + "]";
	}
	public String getName() {
		return Name;
	}
	public String getSurname() {
		return Surname;
	}
	public static ArrayList<String[]> getMetadata() {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<>();
		list.add(DatasetHelper.metadataRow("Person.Name", "Nome", "String"));
		list.add(DatasetHelper.metadataRow("Person.Surname", "Cognome", "String"));
		return list;
	}
	public ArrayList<String[]> getData() {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<>();
		list.add(RiverDomain.dataObjBuilder("Name", Name));
		list.add(RiverDomain.dataObjBuilder("Surname", Surname));
		return list;
	}
}
