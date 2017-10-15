package com.indexer;

import org.junit.Test;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestPackage {
	
	@Test
	public void testPackageWithNoDependencies() {
		Package p = new Package("A");
		assertThat(p.getName(), is(equalTo("A")));
		assertThat(p.getDependecies().size(), is(0));
	}
	
	@Test
	public void testPackageWithDependencies() {
		Package p = new Package("A");
		
		ArrayList<String> dependencies = new ArrayList<String>();
		dependencies.add("B");
		dependencies.add("C");
		
		p.setDependecies(dependencies);
		
		assertThat(p.getName(), is(equalTo("A")));
		assertThat(p.getDependecies().size(), is(2));
	}

	@Test
	public void testPackageWithDependents() {
		Package d = new Package("D");
		Package p = new Package("A");
		
		ArrayList<String> dependencies = new ArrayList<String>();
		dependencies.add("D");
		p.setDependecies(dependencies);
		
		ArrayList<String> dependants = new ArrayList<String>();
		dependants.add("A");
		d.setDependants(dependants);
		
		// check values for D
		assertThat(d.getName(), is(equalTo("D")));
		assertThat(d.getDependants().size(), is(1));
		assertThat(d.getDependants().get(0), is(equalTo("A")));
	}
}
