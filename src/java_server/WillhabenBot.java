package java_server;

import org.jsoup.*;
import org.jsoup.select.Elements;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WillhabenBot {

	private String link = "";
	private String[] keywords = null;
	private String email = "";
	private int interval = -1;
	private int noListings = 0;

	public WillhabenBot(String email, int interval, String[] keywords) {
		this.link = "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=";
		this.keywords = keywords;
		this.email = email;
		this.interval = interval;
	}

	public WillhabenBot(String email, int interval, String link) {
		this.link = link;
		this.keywords = null;
		this.email = email;
		this.interval = interval;
	}

	/**
	 * Prepares Link for the Extraction of Number of Listings by adding the Keywords
	 * to the Link or only using the supplied Link. Then Calls
	 * extractNumberOfListings() with the prepared Link.
	 * 
	 * @return The Number of Listings extracted by extractNumberOfListings()
	 */
	private String prepareLink() {
		if (this.keywords[0] != null) {
			for (int i = 0; i < keywords.length; i++) {
				if (i == keywords.length - 1) {
					this.link = this.link + keywords[i];
				} else {
					this.link = this.link + keywords[i] + "+";
				}
			}
			return this.link;
		} else if (this.link != "") {
			return this.link;
		} else {
			System.out.println("No Link / Keywords provided");
			return null;
		}
	}

	/**
	 * Extracts the Number of Listings from a given Willhaben Link by Parsing it
	 * through HTML Parser JSoup and Regular Expressions.
	 * 
	 * @param link
	 * @return The Number of Listings extractet from Site Sourcecode.
	 */
	public static int extractNumberOfListings(String link) {
		try {
			if (link == null)
				throw new Exception("Link Null");
			// Downloads Website Source and strips Html to only script Tags
			org.jsoup.nodes.Document website = Jsoup.connect(link).get();
			Elements strippedHtml = website.select("script");
			// Matches Regex Pattern to extract Number of Listings
			Pattern noListingsPattern = Pattern.compile("(search_results_number\":\")(.{1,4}\\d)");
			Matcher mat = noListingsPattern.matcher(strippedHtml.toString());
			mat.find();

			return Integer.parseInt(mat.group(2));
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Calls extractNumberOfListings and prepareLink to start extraction of Number
	 * of Listings from prepared Link
	 * 
	 * @return Number of Current Listings
	 */
	private int updateNumberOfListings() {
		return extractNumberOfListings(prepareLink());
	}

	/**
	 * Checks if there are new Listings available and sets this.noListings according
	 * to the changes.
	 * 
	 * @return True if there are new Listings available, False if otherwise
	 */
	public boolean isNew() {
		int newNumberOfListings = updateNumberOfListings();
		if (this.noListings < newNumberOfListings) {
			this.noListings = newNumberOfListings;
			return true;
		} else if (this.noListings > newNumberOfListings) {
			this.noListings = newNumberOfListings;
		}
		return false;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getNoListings() {
		return noListings;
	}

//	public void setNoListings(int noListings) {
//		this.noListings = noListings;
//	}

	public static void main(String[] args) {
		extractNumberOfListings(
				"https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+M365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+M365");
	}

}
