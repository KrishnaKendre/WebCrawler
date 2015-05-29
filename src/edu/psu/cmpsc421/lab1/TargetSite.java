package edu.psu.cmpsc421.lab1;

import java.util.regex.Pattern;


/**
 * An object with information about a site to be visited, including the
 * host name, and regular expressions used to determine if a target URL 
 * is located on the same host and if a target URL is on the disallow 
 * list in the site's robots.txt file.
 * 
 * @author jjb24
 *
 */

public class TargetSite {
	String hostName;
	Pattern hostPrefix;
	Pattern robots;

	
	/**
	 * Constructor for the TargetSite class
	 * 
	 * @param hostName  The host name of the target web site
	 * @param hostPrefix  A Regex pattern that will match the prefix of
	 * 					  any page on the same site
	 * @param robots	  A Regex pattern that will match any page on the
	 *                    disallow list of the robots.txt file 
	 */
	TargetSite(String hostName, Pattern hostPrefix, Pattern robots) {
		this.hostName = hostName;
		this.hostPrefix = hostPrefix;
		this.robots = robots;
	}
}
