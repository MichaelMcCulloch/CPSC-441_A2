import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worker implements Runnable{
	
	private Socket socket;
	private String request;

	public Worker(Socket socket, String request) {
		this.socket = socket;
		this.request = request;
	}
	
	public void run(){
		//Parse Request
		HTTPHeaderParser hhp = new HTTPHeaderParser(request);
		//Ensure well-formatted
		if (hhp.isWellFormed()){
			//Get the object
			//transmit			
		}
		//close
	}
	
	private File getTheFile(Path toFile) throws FileNotFoundException {
		
		Path fullPath = Paths.get(System.getProperty("user.dir"), toFile.toString());
		File file = fullPath.toFile();
		return file;
		
	}
}
