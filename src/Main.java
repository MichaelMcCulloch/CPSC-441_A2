
public class Main {

	public static void main(String[] args){
		String testString = "GET /somedir/page.html HTTP/1.1\r\nHost: www.test.com\r\n\r\n";
		
		HTTPHeaderParser p = new HTTPHeaderParser(testString);
	}
}
