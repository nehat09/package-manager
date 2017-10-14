package com.indexer;
import java.util.ArrayList;

public class Package {
	
	String name;
	ArrayList<String> dependecies;
	ArrayList<String> dependants;
	
	public Package(String name, ArrayList<String> deps){
		this.name = name;
		this.dependecies = deps;	
		this.dependants = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getDependecies() {
		return dependecies;
	}

	public void setDependecies(ArrayList<String> dependecies) {
		this.dependecies = dependecies;
	}

	public ArrayList<String> getDependants() {
		return dependants;
	}

	public void setDependants(ArrayList<String> dependants) {
		this.dependants = dependants;
	}

}
