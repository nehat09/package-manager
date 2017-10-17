package com.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.indexer.PackageManager;

public class PMApplicationServer {
	private static final int DEFAULT_PORT = 8080;
	private static PMApplicationServer _theServer = null;
	private final static ExecutorService service = Executors.newCachedThreadPool();

	private PackageManager manager;

	private PMApplicationServer() {
		this.manager = new PackageManager();
	}

	/**
	 * Application server which is multi-threaded.
	 */
	public static void main(String[] args) throws Exception {
		int port = DEFAULT_PORT;
		if (args.length > 0){
			port = Integer.parseInt(args[0]);
		}
		
		PMApplicationServer server = PMApplicationServer.getInstance();
		server.start(port);
	}
	
	/**
	 * GetInstance method to ensure a singleton instance
	 * is created if not already created. */
	public static PMApplicationServer getInstance(){
		if (_theServer == null){
			_theServer = new PMApplicationServer();
		}
		return _theServer;
	}

	/**
	 * Server start method. Begin a server socket and listens for
	 * new connections. */
	public void start(int port) throws Exception {
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