package java_server;

import java.io.File;
import java.io.IOException;

import org.jsoup.*;

public class WillhabenB0T {

	private String link = "";
	private String[] keywords = null;
	private String email = "";
	private int intervall = -1;
	private int noListings = 0;

	public WillhabenB0T(String email, int intervall, String[] keywords) {
		this.link = "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=";
		this.keywords = keywords;
		this.email = email;
		this.intervall = intervall;
	}

	public WillhabenB0T(String email, int intervall, String link) {
		this.link = link;
		this.keywords = null;
		this.email = email;
		this.intervall = intervall;
	}

	/**
	 * Prepares Link for the Extraction of Number of Listings by adding the Keywords
	 * to the Link or only using the supplied Link. Then Calls
	 * extractNumberOfListings() with the prepared Link.
	 * 
	 * @return The Number of Listings extracted by extractNumberOfListings()
	 */
	private int prepareLink() {
		if (this.keywords[0] != null) {
			for (int i = 0; i < keywords.length; i++) {
				if (i == keywords.length - 1) {
					this.link = this.link + keywords[i];
				} else {
					this.link = this.link + keywords[i] + "+";
				}
			}
			return extractNumberOfListings(this.link);
		} else if (this.link != "") {
			return extractNumberOfListings(this.link);
		} else {
			System.out.println("No Link / Keywords provided");
			return -1;
		}
	}

	private int extractNumberOfListings(String link) {
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect("http://example.com").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateNumberOfListings() {
		this.noListings = prepareLink();
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

	public int getIntervall() {
		return intervall;
	}

	public void setIntervall(int intervall) {
		this.intervall = intervall;
	}

	public int getNoListings() {
		return noListings;
	}

//	public void setNoListings(int noListings) {
//		this.noListings = noListings;
//	}

}
