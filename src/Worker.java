import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Worker implements Runnable{
	
	private Socket socket;
	private String request="";

	public Worker(Socket socket) {
		this.socket = socket;
		int k = 0;
		try {
			InputStream is = socket.getInputStream();
			byte[] buffer = new byte[65535];
			k = is.read(buffer);
			byte[] headerBuffer = new byte[k];
			System.arraycopy(buffer, 0, headerBuffer, 0, k);
			request = new String(headerBuffer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NegativeArraySizeException e){
			//Sometimes the socket input stream is empty. So it get an empty request? It doesn't seem to affect the end output in <<Insert your favorite web browser here>>, so w/e
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		String myServerName;
		try {
			myServerName = Inet4Address.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			myServerName = "Unknown";
		}
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		Date d = new Date();
		String dateString = format.format(d);
		byte[] response = null;
		try {
			//Parse Request
			//Ensure well-formatted
			HTTPHeaderParser hhp = new HTTPHeaderParser(request);
			//Get the object
			File toSend = getTheFile(hhp.getRequestedPath());
			
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(toSend.lastModified());
			String lastModifiedString = format.format(cal.getTime()); 
			
			String message = "HTTP/1/1 200 OK\r\n"
					+ "Date: " + dateString + "\r\n"
					+ "Server: " + myServerName + "\r\n"
					+ "Last-Modified: " + lastModifiedString + "\r\n"
					+ "Content-Length: " + toSend.length() + "\r\n"
					+ "Content-Type: " + Files.probeContentType(toSend.toPath())  + "\r\n"
					+ "Connection: close\r\n" 
					+ "\r\n";
			
			FileInputStream fin = new FileInputStream(toSend);
			byte[] fileContent = new byte[(int)toSend.length()];
			fin.read(fileContent);
			byte[] messageBytes = message.getBytes();
			
			response = new byte[(int)(messageBytes.length + fileContent.length)];
			System.arraycopy(messageBytes, 0, response, 0, messageBytes.length);
			System.arraycopy(fileContent, 0, response, messageBytes.length, fileContent.length);
			
			
			
		} catch (IOException e) {
			String message = "HTTP/1/1 404 Not Found\r\n"
					+ "Date: " + dateString + "\r\n"
					+ "Server: " + myServerName + "\r\n"
					+ "Connection: close\r\n" 
					+ "\r\n";
			byte[] messageBytes = message.getBytes();
			
			response = new byte[(int)(messageBytes.length)];
			System.arraycopy(messageBytes, 0, response, 0, messageBytes.length);
			
		} catch (BadRequestException e) {
			String message = "HTTP/1/1 400 Bad Request\r\n"
					+ "Date: " + dateString + "\r\n"
					+ "Server: " + myServerName + "\r\n"
					+ "Connection: close\r\n" 
					+ "\r\n";
			byte[] messageBytes = message.getBytes();
			
			response = new byte[(int)(messageBytes.length)];
			System.arraycopy(messageBytes, 0, response, 0, messageBytes.length);
		}
		
		try {
			//transmit
			//close
			socket.getOutputStream().write(response);
			socket.close();
		} catch (IOException e) {
			System.out.println("Unable to send Response");
		}
		
		
	}
	
	private File getTheFile(Path toFile) throws FileNotFoundException {
		
		Path fullPath = Paths.get(System.getProperty("user.dir"), toFile.toString());
		File file = fullPath.toFile();
		return file;
		
	}
}
