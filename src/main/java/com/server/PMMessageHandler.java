package com.server;

import java.util.ArrayList;
import java.util.Arrays;

import com.indexer.PackageManager;

public class PMMessageHandler {

	PackageManager manager;

	public PMMessageHandler(PackageManager pm) {
		this.manager = pm;
	}

	public String processInput(String theInput) {
		String theOutput = "OK";

		if (!isValidInput(theInput)) {
			theOutput = "ERROR";
			return theOutput;
		}

		// process the input
		boolean status = doCommand(theInput);

		if (status) {
			theOutput = "OK";
		} else {
			theOutput = "FAIL";
		}
		return theOutput;
	}

	boolean doCommand(String theInput){
		String command[] = theInput.split("\\|");
		ArrayList<String> deps = null;
		if (command.length == 3) {
			deps = new ArrayList<String>();
			for (String d : command[2].split(",")) {
				deps.add(d);
			}
		}
		// switch based on command
		boolean status = false;

		switch (command[0]) {
		case "INDEX":
			status = this.manager.index(command[1], deps);
			break;
		case "REMOVE":
			status = this.manager.remove(command[1]);
			break;
		case "QUERY":
			status = this.manager.query(command[1]);
			break;
		}
		return status;
	}
	
	boolean isValidInput(String in) {
		String parts[] = in.split("\\|");

		if (parts.length < 2 || parts.length > 3) {
			return false;
		}
		if (!isValidCommand(parts[0])) {
			return false;
		}
		if (!isValidPackageName(parts[1])) {
			return false;
		}
		if (parts.length == 3 && !isValidDependenciesList(parts[2])) {
			return false;
		}
		return true;
	}

	boolean isValidDependenciesList(String deps) {
		if (deps.charAt(deps.length() - 1) == ',') {
			return false;
		}

		String list[] = deps.split(",");
		for (String s : list) {
			if (!isValidPackageName(s)) {
				return false;
			}
		}
		return true;
	}

	boolean isValidPackageName(String name) {
		return name.matches("[-\\p{Alnum}]+");
	}

	boolean isValidCommand(String command) {
		String commandList[] = { "INDEX", "QUERY", "REMOVE" };
		return Arrays.asList(commandList).contains(command);
	}
}
