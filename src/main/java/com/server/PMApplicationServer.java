package com.server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.indexer.PackageManager;

public class PMApplicationServer {
	
	static PackageManager manager = new PackageManager();
	
	final static ExecutorService service = Executors.newCachedThreadPool();

    /**
     * Application server which is multi-threaded.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The package manager server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9090, 100);
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