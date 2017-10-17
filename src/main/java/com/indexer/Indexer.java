package com.indexer;

import java.util.List;

public interface Indexer {

	 boolean index(String pkg, List<String> deps);

	boolean remove(String name);

	boolean query(String name);
}
