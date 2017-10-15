package com.indexer;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageManager {
	Map<String, Package> map;

	public PackageManager() {
		this.map = new ConcurrentHashMap<String, Package>(100, (float) 0.75, 100);
	}

	/**
	 * Index function for adding to map of packages. If successful indexing occurs,
	 * function returns true. On failure, returns false. */
	public boolean index(String pkg, ArrayList<String> deps) {
		Package p = new Package(pkg);

		// add if deps are not null
		if (deps != null) {
			// check deps
			boolean depsPresent = isDepsPresent(deps);
			if (!depsPresent) {
				return false;
			}
			if (map.containsKey(pkg)) {
				p = map.get(pkg);
			}
			p.setDependecies(deps);
			updateDependentsOnIndex(pkg, deps);
		}
		map.put(pkg, p);
		return true;
	}

	/**
	 * Remove function for removing a package from map. On successful removal, 
	 * function returns true. On failure returns false. */
	public boolean remove(String name) {
		if (!map.containsKey(name)) {
			return true;
		}
		if (map.get(name).getDependants().size() != 0) {
			return false;
		} else {
			updateDependentsOnRemove(name);
			map.remove(name);
		}
		return true;
	}

	/**
	 * Query function returns whether package exists or not.
	 */
	public boolean query(String name) {
		return map.containsKey(name);
	}

	/**
	 * Private Function for checking if dependencies provided are present
	 * in the map.
	 */
	boolean isDepsPresent(ArrayList<String> deps) {
		for (String d : deps) {
			if (!map.containsKey(d)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Private Function for updating the state of dependents on indexing.
	 */
	void updateDependentsOnIndex(String pkg, ArrayList<String> deps) {
		for (String dep : deps) {
			Package p = map.get(dep);
			ArrayList<String> dependents = p.getDependants();
			dependents.add(pkg);
			p.setDependants(dependents);
			map.put(dep, p);
		}
	}

	/**
	 * Private Function for updating the state of dependents on removal.
	 */
	void updateDependentsOnRemove(String pkg) {
		// for all dependencies of pkg , remove pkg from their dependents
		ArrayList<String> deps = map.get(pkg).getDependecies();

		for (String dep : deps) {
			Package p = map.get(dep);
			ArrayList<String> dependents = p.getDependants();
			dependents.remove(pkg);
			p.setDependants(dependents);
			map.put(dep, p);
		}
	}
}
