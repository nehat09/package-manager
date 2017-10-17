package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestIntegration {

	static final int MAX_THREADS = 100;
	static final int TEST_PORT = 9092;
	Executor e = Executors.newFixedThreadPool(MAX_THREADS+1);

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

		for (int i = 0; i < MAX_THREADS; i++) {
			((ExecutorService) e).submit(new Runnable() {
				public void run() {
					Client client = new Client();
					client.start();
				}
			});
		}

	}

	/**
	 * Client that supports multi-thread behavior.
	 */
	public static class Client extends Thread {

		public void run() {
			String hostName = "localhost";
			int portNumber = TEST_PORT;

			System.out.println("Client started..");
			try (Socket socket = new Socket(hostName, portNumber);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));) {

				for (int i = 0; i < 500; i++) {
					out.println("INDEX|aaa" + i + "|");
					Thread.sleep(1000);
				}

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
