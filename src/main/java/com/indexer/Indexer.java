package com.indexer;

import java.util.List;

/**
 * Interface Indexer for handling all indexing operations,
 * namely, index, remove and query.
 */
public interface Indexer {

	boolean index(String pkg, List<String> deps);

	boolean remove(String name);

	boolean query(String name);
}
