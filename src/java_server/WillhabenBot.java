package java_server;

import org.jsoup.*;	
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class WillhabenBot {

	private String link = "";
	private String[] keywords = null;
	private String name = null;
	private ArrayList<String> emails = null;
	private int interval = -1;
	private int noListings = 0;

	public WillhabenBot(String name,  String[] keywords, int interval,  ArrayList<String> emails) {
		this.name = name;
		this.link = "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=";
		this.keywords = keywords;
		this.emails = emails;
		this.interval = interval;
		this.noListings = this.updateNumberOfListings();
	}

	public WillhabenBot(String name, String link, int interval,  ArrayList<String> emails) {
		this.name = name;
		this.link = link;
		this.keywords = null;
		this.emails = emails;
		this.interval = interval;
		this.noListings = this.updateNumberOfListings();
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
	private int extractNumberOfListings(String link) {
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
	private boolean isNew() {
		int newNumberOfListings = updateNumberOfListings();
		if (this.noListings < newNumberOfListings) {
			this.noListings = newNumberOfListings;
			return true;
		} else if (this.noListings > newNumberOfListings) {
			this.noListings = newNumberOfListings;
		}
		return false;
	}
	
	private void sendMail() throws Exception {
		String mailRecepient = "";
		//mailSender / Username / Password / SMTP CONFIG will be retrieved from Property 
		//File when implemented - currently hardcoded for testing
		String mailSender = "sender@mail.com";
		final String username = "username";
		final String password = "password";
		String host = "placeholder.testsmtp.net";
		for(String element:this.emails) {
			mailRecepient = mailRecepient + "," + element;
		}
		
	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject("Testing Subject");

		   // Send the actual HTML message, as big as you like
		   message.setContent(
	              "<h1>This is actual message embedded in HTML tags</h1>",
	             "text/html");

		   // Send message
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e);
	      }
		
		

		
		
		
		
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

	public ArrayList<String> getEmails() {
		return emails;
	}

	public String getEmail(int index) throws Exception {
		return emails.get(index);
	}

	public void setEmails(ArrayList<String> emails) {
		this.emails = emails;
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
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

//For Testing - change extractNumberOfListings to public static
//	public static void main(String[] args) {
//		extractNumberOfListings(
//				"https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+M365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+M365");
//	}

}
