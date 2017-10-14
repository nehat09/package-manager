package com.indexer;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageManager {
	Map<String, Package> map = new ConcurrentHashMap<String, Package>();

	public PackageManager(){
	}

	public boolean index(String pkg, ArrayList<String> deps){
		Package p = new Package(pkg, new ArrayList<String>());
		
		// add if deps are not null
		if (deps != null){
			// check deps
			boolean depsPresent = isDepsPresent(deps);
			if (!depsPresent){
				return false;
			}else{
				
				if (map.containsKey(pkg)){
					p = map.get(pkg);
					p.setDependecies(deps);
				}else{
					p = new Package(pkg, deps);
				}
				updateDependentsOnIndex(pkg, deps);
			}
		}
		map.put(pkg, p);
		return true;
	}
	
	public boolean remove(String name){
		if (!map.containsKey(name)){
			return true;
		}
		if (map.get(name).getDependants().size() != 0){
			return false;
		}else{
			
			updateDependentsOnRemove(name);
			map.remove(name);
		}			
		return true;
	}
	
	public boolean query(String name){
		return map.containsKey(name);
	}
	
	boolean isDepsPresent(ArrayList<String> deps){
		for (String d:deps){
			if (!map.containsKey(d)){
				return false;
			}
		}
		return true;
	}
	
	void updateDependentsOnIndex(String pkg, ArrayList<String> deps){
		for (String dep:deps){
			Package p = map.get(dep);
			ArrayList<String> dependents = p.getDependants();
			dependents.add(pkg);
			p.setDependants(dependents);
			map.put(dep, p);
		}
	}
	
	void updateDependentsOnRemove(String pkg){
		// for all dependencies of pkg , remove pkg from their dependents
		ArrayList<String> deps = map.get(pkg).getDependecies();		
		
		for (String dep:deps){
			Package p = map.get(dep);
			ArrayList<String> dependents = p.getDependants();
			dependents.remove(pkg);
			p.setDependants(dependents);
			map.put(dep, p);
		}
	}
}
