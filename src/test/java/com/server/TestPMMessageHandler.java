package com.server;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.indexer.PackageManager;

public class TestPMMessageHandler {
	
	PackageManager m = new PackageManager();
	PMMessageHandler p = new PMMessageHandler(m);

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
	}
	
	@Test
	public void testIsValidDependenciesList(){
		assertThat(p.isValidDependenciesList("pkg,hj"), is(true));
		assertThat(p.isValidDependenciesList("pkg"), is(true));
		assertThat(p.isValidDependenciesList("pkg,hj,klm"), is(true));
		assertThat(p.isValidDependenciesList("pkg,hj,"), is(false));
		assertThat(p.isValidDependenciesList("emac elips"), is(false));
	}
	
	@Test
	public void testIsValidPackageName(){
		assertThat(p.isValidPackageName("emacs"), is(true));
		assertThat(p.isValidPackageName("emacs-11"), is(true));
		assertThat(p.isValidPackageName("em-acs"), is(true));
		assertThat(p.isValidPackageName("11emacs"), is(true));
		
		assertThat(p.isValidPackageName("emacs "), is(false));
		assertThat(p.isValidPackageName("hi emacs"), is(false));
		assertThat(p.isValidPackageName("elips+"), is(false));
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
}
