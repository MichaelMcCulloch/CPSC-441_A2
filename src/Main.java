
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String requestTest = "GET /test.html HTTP/1.1\r\n"
				+ "Host: test.com\r\n"
				+ "\r\n";
		
		try {
			HTTPHeaderParser hhp = new HTTPHeaderParser(requestTest);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
