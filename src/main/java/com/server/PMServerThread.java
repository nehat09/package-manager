package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.indexer.PackageManager;

/**
 * A thread class to handle multi-threaded requests on a socket.
 */
public class PMServerThread implements Runnable{ //extends Thread {
	private Socket socket;
	private int clientNumber;
	private PackageManager manager;

	public PMServerThread(Socket socket, int clientNumber, PackageManager pm) {
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.manager = pm;
		log("New connection with client# " + clientNumber + " at " + socket);
	}

	public void run() {
		PrintWriter out = null;
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			) {

			out	= new PrintWriter(socket.getOutputStream(), true);
			
			PMMessageHandler handler = new PMMessageHandler(this.manager);

			while (true) {
				String input = in.readLine();
				if (input == null) {
					break;
				}
				// process input
				String output = handler.processInput(input);
				out.println(output);
			}
		} catch (IOException e) {
			log("Error handling client# " + clientNumber + ": " + e);
			out.close();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				log("Error closing a socket.");
			}
			log("Connection with client# " + clientNumber + " closed");
		}
	}

	private void log(String message) {
		System.out.println(message);
	}
}