package com.indexer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;


public class TestPackageManager {
	
	Executor e = Executors.newFixedThreadPool(5);

	@Test
	public void testIndex() {
		PackageManager manager = new PackageManager();
		
		// add packages with no deps
		assertThat(manager.index("aaa", null), is(true));
		assertThat(manager.index("bbb", null), is(true));
		assertThat(manager.index("ccc", null), is(true));
		
		// add package with deps
		ArrayList<String> deps = new ArrayList<String>();
		deps.add("aaa");
		assertThat(manager.index("ddd", deps), is(true));
		
		// fails when eee is present
		deps.add("eee");
		assertThat(manager.index("ddd", deps), is(false));
		
		// after adding eee
		assertThat(manager.index("eee", null), is(true));
		assertThat(manager.index("ddd", deps), is(true));
				
	}

	@Test
	public void testRemove() {
		PackageManager manager = new PackageManager();
		manager.index("aaa", null);
		manager.index("bbb", null);
		manager.index("ccc", null);
		ArrayList<String> deps = new ArrayList<String>();
		deps.add("aaa");
		manager.index("ddd", deps);
		
		boolean status = manager.remove("aaa");
		assertThat(status, is(false));
		
		manager.remove("ddd");
		
		status = manager.remove("aaa");
		assertThat(status, is(true));
	}
	
	@Test
	public void testQuery(){
		PackageManager manager = new PackageManager();
		manager.index("aaa", null);
		
		assertThat(manager.query("aaa"), is(true));
		assertThat(manager.query("zzz"), is(false));
	}
	
	@Test
	public void testConcurrency(){
		final PackageManager manager = new PackageManager();
		for(int i =0; i < 5000; i++){
	           ((ExecutorService) e).submit(new Runnable(){
	               public void run(){
	                    manager.map.put("a", new Package("a", new ArrayList<String>()));
	               } 
	           });
	       }
	    
		assertThat(manager.map.size(), is(1));
	}
		
}
