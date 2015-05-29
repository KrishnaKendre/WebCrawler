package edu.psu.cmpsc421.lab1;



/**
 * A URL to be visited
 * 
 * @author jjb24
 */
public class TargetURL {
	String url;		// The complete URL, e.g. http://psu.edu/path/target.html?as=df
	String host;    // The host of the URL, e.g. http://psu.edu
	String path;    // The path of the URL, e.g. /path/target.html
	String queryString; // The query string of the URL, e.g. ?as=df

	/**
	 * Create object from the URL, parsing out the host, path, and query string
	 * @param url	The URL to be visited
	 */
	public TargetURL(String url) {
		this.url = url;
		path = "";
		queryString = "";

		if (url.length() <= 7 ||
				url.substring(0,7).equalsIgnoreCase("http://") == false)
			throw new IllegalArgumentException("Malformed URL (does not start with 'http://': '" + url + "'");

		int pathStartPos = url.indexOf('/',7);
		if (pathStartPos == -1) {
			host = url;
		}
		else {
			host = url.substring(0, pathStartPos);
			int queryStringStart = url.indexOf('?', pathStartPos);
			if (queryStringStart < 0) {
				path = url.substring(pathStartPos);
			} else {
				path = url.substring(pathStartPos, queryStringStart);
				queryString = url.substring(queryStringStart);				
			}
		}
	}

	
	/**
	 * Returns true if the path portion of two URL's are equal
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (other.getClass() != this.getClass()) return false;
		TargetURL that = (TargetURL) other;

		return that.path.equals(this.path);
	}

	/**
	 * Calculate hash code based only on the path of the URL
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode()
	{
		return path.hashCode();
	}

	/**
	 * Return the URL 
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return url;
	}					
}
