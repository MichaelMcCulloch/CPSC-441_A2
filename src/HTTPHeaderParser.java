
/**
 * HTTPHeaderParser
 * Michael McCulloch
 * Assignement 1, CPSC 441
 * 30016991 
 *
 */

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Process HTTP headers.
 * @author micha
 *
 */
class HTTPHeaderParser {
		private int status;
		private String type;
		private String subtype;
		private String lastModified;
		private int length;

		private String statusRE = "HTTP.* ([0-9]*) .*"; 
		private Pattern statusPattern = Pattern.compile(statusRE, Pattern.CASE_INSENSITIVE);
		
		private String lastModifiedRE = "Last-Modified:[ ]*(.*)";
		private Pattern lastModifiedPattern = Pattern.compile(lastModifiedRE, Pattern.CASE_INSENSITIVE);
		
		private String typeRE = "Content-Type:[ ]*(([\\w]*)/([\\w]*))";
		private Pattern typePattern = Pattern.compile(typeRE, Pattern.CASE_INSENSITIVE);
		
		private String lengthRE = "Content-Length:[ ]*(\\d*)";
		private Pattern lengthPattern = Pattern.compile(lengthRE, Pattern.CASE_INSENSITIVE);
		
		public HTTPHeaderParser(String[] headers) {
			
			Matcher httpStatusMatcher = statusPattern.matcher(headers[0]);
			if (httpStatusMatcher.find()){
				this.status = Integer.parseInt(httpStatusMatcher.group(1));
			}
			if (this.status != 200){
				return;
			}
			
			for (int i = 1; i < headers.length; i++) {
				if (headers[i] == null) continue;
				if (headers[i].contains("Last-Modified:")){
					Matcher lmMatcher = lastModifiedPattern.matcher(headers[i]);
					if (lmMatcher.find()){
						this.lastModified = lmMatcher.group(1);
					}
				} else if (headers[i].contains("Content-Type:")){
					Matcher typeMatcher = typePattern.matcher(headers[i]);
					if (typeMatcher.find()){
						this.type = typeMatcher.group(2);
						this.subtype = typeMatcher.group(3);
					}
				} else if (headers[i].contains("Content-Length:")){
					Matcher lengthMatcher = lengthPattern.matcher(headers[i]);
					if (lengthMatcher.find()){
						this.length = Integer.parseInt(lengthMatcher.group(1));
					}
				}
				
			}
		}
		
		

		public int getStatus() {
			return status;
		}

		public String getSubtype() {
			return subtype;
		}
		public String getType() {
			return type;
		}

		public String getLastModified() {
			return lastModified;
		}
		
		public int getLength() {
			return length;
		}
	}
