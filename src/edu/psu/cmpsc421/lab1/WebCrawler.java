package edu.psu.cmpsc421.lab1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * The class containing the entry point of the Web Crawler program, and 
 * the code to crawl the target sites.
 * 
 * @author jjb24
 *
 */

public class WebCrawler {

	// The list of sites to crawl
	private static ArrayList<TargetSite> targetSites = new ArrayList<TargetSite>();

	// The maximum number of recipes to catalog on a site
	private static int MAX_PROCESSED = 10;

	/**
	 * The entry point to the program
	 * 
	 * @param args The command line arguments are currently ignored
	 * 
	 */
	public static void main(String[] args) {
		initializeTargetSites();
		for (TargetSite ts: targetSites)
			crawl(ts);
	}

	/**
	 * Crawl a target site
	 * 
	 * @param targetSite The site to catalog
	 */
	private static void crawl(TargetSite targetSite) {
		// The queue of web pages to visit
		Queue<TargetURL> q = new LinkedList<TargetURL>();
		TargetURL start = new TargetURL(targetSite.hostName);
		q.add(start);		

		// The set of web pages already enqueued (so we do not visit duplicates)
		Set<TargetURL> set = new HashSet<TargetURL>();
		set.add(start);

		int processed = 0;
		while (q.size() > 0 && processed < MAX_PROCESSED) { 
			TargetURL targetURL = q.remove();

			try {
				// Attempt to retrieve URL
				Document doc = Jsoup.connect(targetURL.toString()).get();

				// To Do:
				// Look for a recipe on the page
				// If it is found display it via System.out.println
				Elements recipe = doc.getElementsByAttributeValue("itemtype", "http://schema.org/Recipe");
				
				// If it is found display it via System.out.println
				if(!recipe.isEmpty())
				{
					for(Element e:recipe){
						
						// Create new Recipe
						Recipe newRecipe = new Recipe(targetURL, e);
						
						// Print out the new Recipe
						System.out.println(newRecipe.toString());	
						
						// Update processed to fetch next URL
						processed++;			
					}
				}

				// Find all anchor tags with an href attribute
				Elements links = doc.select("a[href]");
				for(Element link: links){

					// Get the URL of the target
					String sLinkURL = link.attr("abs:href");

					// Check to make sure the target is on the same site
					Matcher matcher = targetSite.hostPrefix.matcher(sLinkURL);
					if (matcher.find()) {
						TargetURL linkURL = new TargetURL(sLinkURL);

						// Make sure that the target has not already been visited and that it
						// is not disallowed by the robots.txt file
						if (set.contains(linkURL) == false && isAllowed(linkURL, targetSite))
						{
							q.add(linkURL);
							set.add(linkURL);
						}
					}
				}
			}
			catch (HttpStatusException hse) {
				System.out.println(hse);
			}
			catch (IOException ioe) {
				System.out.println(ioe);
			}

			// Be polite - wait before getting the next page
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException ie) {
				// this will not occur
				System.exit(-1);
			}
		}

		System.out.println("Processed: " + processed);
	}

	/**
	 * Check if a URL is ok to visit based on a site's robots.txt file.
	 * 
	 * @param url  The URL to visit
	 * @param targetSite  The web site containing the URL
	 * @return true iff the target URL is not disallowed by the robots.txt file
	 */
	private static boolean isAllowed(TargetURL url, TargetSite targetSite) {
		Matcher m = targetSite.robots.matcher(url.path);
		return !m.find();
	}


	/**
	 * Initialize the list of target sites to visit
	 */
	private static void initializeTargetSites() {
		targetSites.add(new TargetSite("http://www.food.com/",
				Pattern.compile("^http://.*food\\.com"),
				Pattern.compile("(/recipeprint\\.do)|"+
						"(/about/tour/)|" +
						"(/admin/)|" + 
						"(/alt/)|" +
						"(/mail/)|" +
						"(/members/photos\\.php)|" +
						"(/recipe/photo\\.php)|" +
						"(/bb/posting\\.zsp)|" +
						"(/pushdownAd\\.do)|" +
						"(/global/header\\.jsp)|" +
						"(/global/pickle-header\\.jsp)|" +
						"(/footer\\.jsp)|" +
						"(/headerbreadcrumbs\\.do)|" +
						"(/rzfoodservices/web/device-info/getDeviceInfo)|" +
						"(/getrzuserstats\\.do)|" +
						"(/seasonal-header\\.do)|" +
						"(/email\\.zsp)|" +
						"(/members/subm/)|" +
						"(/bb/mytopics\\.zsp)|" +
						"(/mail/compose)|" +
						"(/cp_asset_stats\\.do)|" +
						"(/ingredient-finder/)|" +
						"(/cookbook-finder/)|" +
						"(/menu-finder/)|" +
						"(/slideshow/)")));

		targetSites.add(new TargetSite("http://allrecipes.com/",
				Pattern.compile("^http://.*allrecipes\\.com"),
				Pattern.compile("(/logout\\.ashx)|" +
						"(/Logout\\.ashx)|" +
						"(/Controls/)|" +
						"(/controls/)|" +
						"(/My/)|" +
						"(/my/)|" +
						"(/Dev/)|" +
						"(/dev/)|" +
						"(/Help/AboutUs/Press/Logos/)|" +
						"(/help/aboutus/press/logos/)|" +
						"(/Membership/)|" +
						"(/membership/)|" +
						"(/SupportingMembership/)|" +
						"(/supportingmembership/)|" +
						"(/Help/aboutus/legal\\.aspx)|" +
						"(/help/aboutus/legal\\.aspx)|" +
						"(/Help/AboutUs/Legal\\.aspx)|" +
						"(/Help/aboutus/Privacy\\.aspx)|" +
						"(/help/aboutus/privacy\\.aspx)|" +
						"(/Help/AboutUs/Privacy\\.aspx)|" +
						"(/Newsletters/ChangeNewsletters\\.aspx)|" +
						"(/newsletters/changenewsletters\\.aspx)|" +
						"(/Newsletters/ChangeEmail\\.aspx)|" +
						"(/newsletters/changeemail\\.aspx)|" +
						"(/Handlers/)|" +
						"(/handlers/)|" +
						"(/Recipe-Tools/)|" +
						"(/recipe-tools/)|" +
						"(/PersonalRecipe/)|" +
						"(/personalrecipe/)|" +
						"(/CustomRecipe/)|" +
						"(/customrecipe/)|" +
						"(/WebLink/)|" +
						"(/weblink/)|" +
						"(/Reference/)|" +
						"(/reference/)|" +
						"(/Search/)|" +
						"(/search/)|" +
						"(/_PersonalRecipe/)|" +
						"(/_personalrecipe/)|" +
						"(/_CustomRecipe/)|" +
						"(/_customrecipe/)|" +
						"(/_WebLink/)|" +
						"(/_weblink/)|" +
						"(/_Reference/)|" +
						"(/_reference/)|" +
						"(/dynamiclogic/)|" +
						"(/eyeblaster/)|" +
						"(/pointroll/)|" +
						"(/bin/)|" +
						"(/eyewonder/)|" +
						"(/doubleclick/)|" +
						"(/\\_)")));
	}
}