package com.indexer;

import java.util.ArrayList;
import java.util.List;

public class Package {

	String name;
	List<String> dependencies;
	List<String> dependents;

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

	public List<String> getDependecies() {
		return dependencies;
	}

	public void setDependecies(List<String> dependecies) {
		this.dependencies = dependecies;
	}

	public List<String> getDependants() {
		return dependents;
	}

	public void setDependants(List<String> dependants) {
		this.dependents = dependants;
	}
}
