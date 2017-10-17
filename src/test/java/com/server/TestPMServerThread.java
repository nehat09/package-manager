package com.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

public class TestPMServerThread {
	
	static final int TEST_PORT = 9091;

	/**
	 * Sanity check for server. Start the server and send message.
	 * Server responds, and no exception thrown.
	 */
	@Test
	public void testServer() throws Exception {
		final PMApplicationServer server = new PMApplicationServer(TEST_PORT);
		new Thread() {
			public void run() {
				try {
					server.start();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}.start();

		Client client = new Client();
		client.start();
	}

	public static class Client {

		public void start() {
			String hostName = "localhost";
			int portNumber = TEST_PORT;

			System.out.println("Client started..");
			try (Socket socket = new Socket(hostName, portNumber);
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

				String serverOutput;
				out.println("INDEX|aaa|");
				Thread.sleep(1000);

				serverOutput = in.readLine();
				System.out.println("Server: " + serverOutput);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
