package com.server;

import java.util.ArrayList;
import java.util.Arrays;

import com.indexer.PackageManager;

public class PMMessageHandler {

	PackageManager manager;

	public PMMessageHandler(PackageManager pm) {
		this.manager = pm;
	}

	public String processInput(String input) {
		String output = ResponseCode.OK.toString();

		// exit early on invalid input
		if (!isValidInput(input)) {
			return ResponseCode.ERROR.toString();
		}

		// process the input
		boolean status = processCommand(input);
		if (status) {
			output = ResponseCode.OK.toString();
		} else {
			output = ResponseCode.FAIL.toString();
		}
		return output;
	}

	boolean processCommand(String input){
		String command[] = input.split("\\|");
		ArrayList<String> deps = null;
		if (command.length == 3) {
			deps = getDependenciesFromInput(command[2]);
		}
		// switch based on command
		boolean status = performIndexing(command[0], command[1], deps);
		return status;
	}
	
	ArrayList<String> getDependenciesFromInput(String depString){
		ArrayList<String> deps = null;
		deps = new ArrayList<String>();
		for (String d : depString.split(",")) {
			deps.add(d);
		}
		return deps;
	}
	
	boolean performIndexing(String command, String pkg, ArrayList<String> deps){
		boolean status = false;
		switch (command) {
		case "INDEX":
			status = this.manager.index(pkg, deps);
			break;
		case "REMOVE":
			status = this.manager.remove(pkg);
			break;
		case "QUERY":
			status = this.manager.query(pkg);
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
		boolean isValid = name.matches("[-_\\p{Alnum}]+[+]*") || name.equals("gtk+3")
				|| name.equals("dvd+rw-tools");
		return isValid;
	}

	boolean isValidCommand(String command) {
		String commandList[] = { "INDEX", "QUERY", "REMOVE" };
		return Arrays.asList(commandList).contains(command);
	}
	
	/**
	 * Enum for storing response codes.
	 * */
	private static enum ResponseCode{
		OK, FAIL, ERROR
	}
}
