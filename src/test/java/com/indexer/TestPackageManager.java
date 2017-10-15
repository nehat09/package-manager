package com.indexer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for PackageManager. Each test uses new PackageManager so that it
 * starts with fresh state as tests are run in parallel.
 */
public class TestPackageManager {

	static ArrayList<String> deps = new ArrayList<String>();

	Executor e = Executors.newFixedThreadPool(5);

	@BeforeClass
	public static void setup() {
		deps.add("aaa");
		deps.add("ccc");
	}

	@Test
	public void testIndex() {
		PackageManager manager = new PackageManager();

		// fails when deps are not present
		assertThat(manager.index("ddd", deps), is(false));

		// add packages with no deps
		assertThat(manager.index("aaa", null), is(true));
		assertThat(manager.index("bbb", null), is(true));
		assertThat(manager.index("ccc", null), is(true));

		// after adding all deps
		assertThat(manager.index("ddd", deps), is(true));

	}

	@Test
	public void testRemove() {
		PackageManager manager = new PackageManager();
		manager.index("aaa", null);
		manager.index("bbb", null);
		manager.index("ccc", null);

		manager.index("ddd", deps);

		// cannot remove package with dependents
		assertThat(manager.remove("aaa"), is(false));

		// after removing dependents
		manager.remove("ddd");
		assertThat(manager.remove("aaa"), is(true));
	}

	@Test
	public void testQuery() {
		PackageManager manager = new PackageManager();
		manager.index("aaa", null);

		assertThat(manager.query("aaa"), is(true));
		assertThat(manager.query("zzz"), is(false));
	}

	@Test
	public void testConcurrency() {
		final PackageManager manager = new PackageManager();
		for (int i = 0; i < 5000; i++) {
			((ExecutorService) e).submit(new Runnable() {
				public void run() {
					manager.map.put("a", new Package("a"));
				}
			});
		}

		assertThat(manager.map.size(), is(1));
	}

}
