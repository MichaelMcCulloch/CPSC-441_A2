
/**
 * HTTPHeaderParser
 * Michael McCulloch
 * Assignement 1, CPSC 441
 * 30016991 
 *
 */

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Process HTTP headers.
 * @author micha
 *
 */
class HTTPHeaderParser {
	private boolean wellFormed = true;
	
	private Path requestedPath;
	public boolean isWellFormed() {
		return wellFormed;
	}


	public Path getRequestedPath() {
		return requestedPath;
	}


	private double httpType = 0.0;

	private Pattern getPattern = Pattern.compile("GET (.*) HTTP/(1.[10])", Pattern.CASE_INSENSITIVE);
	private Pattern hostPattern = Pattern.compile("Host:[ ](.*)", Pattern.CASE_INSENSITIVE);
	
	
	public HTTPHeaderParser(String request) throws BadRequestException{
		
		boolean headerEndFound = false;
		boolean getLineFound = false;
		boolean hostLineFound = false;
		
		Scanner reqScanner = new Scanner(request);
		ArrayList<String> a = new ArrayList<>();
		while (reqScanner.hasNextLine()){
			String nextLine = reqScanner.nextLine();
			a.add(nextLine);
		}
		Matcher getMatcher = getPattern.matcher(a.get(0));
		if (getMatcher.find()){
			this.requestedPath = Paths.get(getMatcher.group(1));
			this.httpType = Double.parseDouble(getMatcher.group(2));
			getLineFound = true;
		}
		
		
		for (String headerLine : a) {
			Matcher hostMatcher = hostPattern.matcher(headerLine);
			if (hostMatcher.find()){
				hostLineFound = true;
			}
		}
		
		if (a.get(a.size()-1).equals("")) {
			headerEndFound = true;
		}
		wellFormed = (headerEndFound && getLineFound && hostLineFound);
		if (!wellFormed) throw new BadRequestException();
		return;
		
	}


	
}
