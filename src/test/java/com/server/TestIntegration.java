package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestIntegration {

	static final int MAX_THREADS = 100;
	static final int TEST_PORT = 9092;
	Executor e = Executors.newFixedThreadPool(MAX_THREADS + 1);

	/**
	 * Load test for server. Start the server and send messages from multiple
	 * clients (multi-threaded).
	 */
	@Test
	public void testServer() throws Exception {
		final PMApplicationServer server = PMApplicationServer.getInstance();
		new Thread() {
			public void run() {
				try {
					server.start(TEST_PORT);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}.start();

		// create client requests
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < MAX_THREADS; i++) {
			Client client = new Client();
			client.start();
			threads.add(client);
		}

		// wait for all threads to complete
		for (Thread thread : threads) {
			thread.join();
		}
	}

	/**
	 * Client that supports multi-thread behavior.
	 */
	public static class Client extends Thread {

		public void run() {
			String hostName = "localhost";
			int portNumber = TEST_PORT;

			// System.out.println("Client started..");
			try (Socket socket = new Socket(hostName, portNumber);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

				// send indexing requests
				for (int i = 0; i < 500; i++) {
					out.println("INDEX|aaa" + i + "|");
					Thread.sleep(1);
					in.readLine();
				}

				// send indexing requests with deps
				for (int i = 0; i < 500; i++) {
					out.println("INDEX|bbb" + i + "|" + generateRandomDeps());
					Thread.sleep(1);
					in.readLine();
				}
				
				// send remove requests
				for (int i = 0; i < 500; i++) {
					out.println("REMOVE|bbb" + i + "|" + generateRandomDeps());
					Thread.sleep(1);
					in.readLine();
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		String generateRandomDeps() {
			StringBuffer d = new StringBuffer();
			int random = (int) Math.random() % 10;
			int i;
			for (i = 1; i < random; i++) {
				d.append("aaa" + i + ",");
			}
			d.append("aaa" + i);
			return d.toString();
		}
	}
}
