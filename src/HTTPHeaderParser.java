
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
	
	
	public HTTPHeaderParser(String request) throws BadRequestException{
		
		boolean headerEndFound = false;
		boolean getLineFound = false;
		boolean headersOK = true;
		
		Scanner reqScanner = new Scanner(request);
		ArrayList<String> a = new ArrayList<>();
		while (reqScanner.hasNextLine()){
			String nextLine = reqScanner.nextLine();
			a.add(nextLine);
		}
		Matcher getMatcher = getPattern.matcher(a.get(0));
		if (getMatcher.find()){
			this.requestedPath = Paths.get(getMatcher.group(1));
			try {
				this.httpType = Double.parseDouble(getMatcher.group(2));
			} catch (Exception e){
				throw new BadRequestException();
			}
			getLineFound = true;
		}
		if (a.get(a.size()-1).equals("")) {
			headerEndFound = true;
		}
		a.remove(0); a.remove(a.size()-1); //remove first and last.
		
		for (String headerLine : a) { //check that each header is of the form "field:value"
			String[] fieldValue = headerLine.split(":");
			//if (fieldValue.length != 2) headersOK = false;
			break;
		}
		
		
		wellFormed = (headerEndFound && getLineFound && headersOK);
		if (!wellFormed) throw new BadRequestException();
		return;
		
	}


	
}
