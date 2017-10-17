package com.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.indexer.PackageManager;

public class PMApplicationServer {

	int port;
	PackageManager manager;
	
	final static ExecutorService service = Executors.newCachedThreadPool();

	public PMApplicationServer(int port) {
		this.port = port;
		this.manager = new PackageManager();
	}

	/**
	 * Application server which is multi-threaded.
	 */
	public static void main(String[] args) throws Exception {
		int port = Integer.parseInt(args[0]);
		PMApplicationServer server = new PMApplicationServer(port);
		server.start();
	}

	public void start() throws Exception {
		System.out.println("The package manager server is running.");
		int clientNumber = 0;
		ServerSocket listener = new ServerSocket(port, 1000);

		try {
			while (true) {
				service.submit(new PMServerThread(listener.accept(), 
						clientNumber++, manager));
			}
		} finally {
			listener.close();
		}
	}
}