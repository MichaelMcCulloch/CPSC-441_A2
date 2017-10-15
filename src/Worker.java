import java.io.*;
import java.net.Socket;

public class Worker implements Runnable{
	
	private Socket socket;
	private String request;

	public Worker(Socket socket, String request) {
		this.socket = socket;
		this.request = request;
	}
	
	public void run(){
		//Parse Request
		//Ensure well-formatted
		//Get the object
		//transmit
		//close
	}
	
	private void parseRequest(String request) throws FileNotFoundException {
		
	}
}
