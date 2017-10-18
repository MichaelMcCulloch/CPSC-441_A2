

/**
 * WebServer Class
 * 
 */

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;



public class WebServer extends Thread {
	private volatile boolean stop = false;
	private int port;
    /**
     * Default constructor to initialize the web server
     * 
     * @param port 	The server port at which the web server listens > 1024
     * 
     */
	public WebServer(int port) {
		this.port = port;
	}

	
    /**
     * The main loop of the web server
     *   Opens a server socket at the specified server port
	 *   Remains in listening mode until shutdown signal
	 * 
     */
	public void run() {
		ExecutorService ex = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		ServerSocket ss;
		try {
			ss = new ServerSocket(port);
			ss.setSoTimeout(1000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		while (!stop) {
			//listen for connection requests
			try {
				
				//accept new connection 
				Socket socket = ss.accept();
				ex.execute(new Worker(socket));
				//spawn a worker
			} catch (SocketTimeoutException e) {
				// It's ok
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			ss.close();
			ex.shutdown();
			if (!ex.awaitTermination(5, TimeUnit.SECONDS)){
				ex.shutdownNow();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
    /**
     * Signals the server to shutdown.
	 *
     */
	public void shutdown() {
		stop = true;
	}

	
	/**
	 * A simple driver.
	 */
	public static void main(String[] args) {
		int serverPort = 2225;

		// parse command line args
		if (args.length == 1) {
			serverPort = Integer.parseInt(args[0]);
		}
		
		if (args.length >= 2) {
			System.out.println("wrong number of arguments");
			System.out.println("usage: WebServer <port>");
			System.exit(0);
		}
		
		System.out.println("starting the server on port " + serverPort);
		
		WebServer server = new WebServer(serverPort);
		
		server.start();
		System.out.println("server started. Type \"quit\" to stop");
		System.out.println(".....................................");

		Scanner keyboard = new Scanner(System.in);
		while ( !keyboard.next().equals("quit") );
		
		System.out.println();
		System.out.println("shutting down the server...");
		server.shutdown();
		System.out.println("server stopped");
	}
	
}
