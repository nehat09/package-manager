package com.server;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import com.indexer.PackageManager;

public class TestPMMessageHandler {
	
	static PackageManager m = new PackageManager();
	static PMMessageHandler p = new PMMessageHandler(m);
	
	static final String[] listOfPackages = {"jpeg", "libtiff", "lzlib", "proj", "gmp",
			"isl", "pkg-config"}; 
	
	@BeforeClass
	public static void setup(){
		for (String s: listOfPackages){
			m.index(s,null);
		}
	}

	@Test
	public void testIsValidInput(){
		assertThat(p.isValidInput("INDEX|cloog|gmp,isl,pkg-config"), is(true));
		assertThat(p.isValidInput("INDEX|cloog|"), is(true));
		assertThat(p.isValidInput("REMOVE|cloog|"), is(true));
		assertThat(p.isValidInput("QUERY|cloog|"), is(true));
		
		assertThat(p.isValidInput("INDEX|"), is(false));
		assertThat(p.isValidInput("INDEX|emac elips"), is(false));
		assertThat(p.isValidInput("INDEX|emac elips|cloog"), is(false));
		assertThat(p.isValidInput("INX|emac|cloog"), is(false));
		assertThat(p.isValidInput("rm|emac|cloog"), is(false));
		assertThat(p.isValidInput("INDEX|emacs☃elisp"), is(false));
	}
	
	@Test
	public void testIsValidDependenciesList(){
		assertThat(p.isValidDependenciesList("cloog,gmp"), is(true));
		assertThat(p.isValidDependenciesList("cloog"), is(true));
		assertThat(p.isValidDependenciesList("gmp,isl,pkg-config"), is(true));
		
		assertThat(p.isValidDependenciesList("gmp,cloog,"), is(false));
		assertThat(p.isValidDependenciesList("emac elips"), is(false));
	}
	
	@Test
	public void testIsValidPackageName(){
		assertThat(p.isValidPackageName("emacs"), is(true));
		assertThat(p.isValidPackageName("aircrack-ng"), is(true));
		assertThat(p.isValidPackageName("artifactory-cli-go"), is(true));
		assertThat(p.isValidPackageName("box2d"), is(true));
		assertThat(p.isValidPackageName("crf++"), is(true)); 
		assertThat(p.isValidPackageName("gtk+3"), is(true)); 
		assertThat(p.isValidPackageName("dvd+rw-tools"), is(true)); 
		
		
		
		assertThat(p.isValidPackageName("emacs+elisp"), is(false)); 
		assertThat(p.isValidPackageName("emacs "), is(false));
		assertThat(p.isValidPackageName("hi emacs"), is(false));
		assertThat(p.isValidPackageName("333#"), is(false));
		assertThat(p.isValidPackageName("$$$"), is(false));
	}
	
	@Test
	public void testIsValidCommand(){
		assertThat(p.isValidCommand("INDEX"), is(true));
		assertThat(p.isValidCommand("REMOVE"), is(true));
		assertThat(p.isValidCommand("QUERY"), is(true));
		
		assertThat(p.isValidCommand("IND E"), is(false));
		assertThat(p.isValidCommand("index"), is(false));
		assertThat(p.isValidCommand("ReMOVE"), is(false));
		assertThat(p.isValidCommand("Qu 1"), is(false));
		assertThat(p.isValidCommand("1Que?"), is(false));
	}
	
	@Test
	public void testProcessInput(){
		assertThat(p.processInput("INDEX|libgeotiff|jpeg,libtiff,lzlib,proj"), is(equalTo("OK")));
		assertThat(p.processInput("INDEX|cloog|gmp,isl,pkg-config"), is(equalTo("OK")));
		assertThat(p.processInput("INDEX|emacs|"), is(equalTo("OK")));
		assertThat(p.processInput("REMOVE|emacs|"), is(equalTo("OK")));
		assertThat(p.processInput("QUERY|cloog|"), is(equalTo("OK")));
		assertThat(p.processInput("QUERY|isl|"), is(equalTo("OK")));
		
		assertThat(p.processInput("REMOVE|libtiff|"), is(equalTo("FAIL")));
		assertThat(p.processInput("REMOVE|gmp|"), is(equalTo("FAIL")));
		assertThat(p.processInput("REMOVE|cloog|"), is(equalTo("OK")));
		assertThat(p.processInput("REMOVE|gmp|"), is(equalTo("OK")));
		
		assertThat(p.processInput("INDEX|gs mp|"), is(equalTo("ERROR")));
		assertThat(p.processInput("INDEXES|gsmp|"), is(equalTo("ERROR")));
		assertThat(p.processInput("rm|gs mp|"), is(equalTo("ERROR")));
	}
	
	@Test
	public void testGetDependenciesFromInput(){
		assertThat(p.getDependenciesFromInput("gmp,isl,pkg-config"), 
				hasItems("gmp","isl","pkg-config"));
		assertThat(p.getDependenciesFromInput("jpeg,libtiff,lzlib,proj"), 
				hasItems("jpeg","libtiff","lzlib","proj"));
		assertThat(p.getDependenciesFromInput("jpeg"), 
				hasItems("jpeg"));
	}
}
