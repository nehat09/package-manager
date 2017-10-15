package com.indexer;

import java.util.ArrayList;

public class Package {

	String name;
	ArrayList<String> dependencies;
	ArrayList<String> dependents;

	public Package(String name) {
		this.name = name;
		this.dependencies = new ArrayList<String>();
		this.dependents = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getDependecies() {
		return dependencies;
	}

	public void setDependecies(ArrayList<String> dependecies) {
		this.dependencies = dependecies;
	}

	public ArrayList<String> getDependants() {
		return dependents;
	}

	public void setDependants(ArrayList<String> dependants) {
		this.dependents = dependants;
	}
}
