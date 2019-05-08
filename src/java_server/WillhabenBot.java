package java_server;

import org.jsoup.*;
import org.jsoup.select.Elements;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WillhabenBot implements Runnable{

	private String link = null;
	private String name = null;
	private int interval = -1;
	private int noListings = 0;
	private Properties mailConfiguration;
	private Properties botConfig;

	public WillhabenBot(Properties botConfig, Properties mailConfiguration) {
		this.botConfig = botConfig;
		this.setBotConfig(botConfig);
		this.mailConfiguration = mailConfiguration;
		this.noListings = this.updateNumberOfListings();
	}

	/**
	 * Checks if Link is not Null
	 * 
	 * @return The Number of Listings extracted by extractNumberOfListings()
	 */
	private String prepareLink() {
		if (this.link != "" && this.link != null) {
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
			org.jsoup.nodes.Document website = Jsoup.connect(link).get();
			Elements strippedHtml = website.select("script");
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
	 * to the changes and calls sendMail. Calls startTimer if no new Listings where found.
	 * 
	 * @return True if there are new Listings available, False if otherwise
	 * @throws Exception
	 */
	private void isNew() throws Exception {
		System.out.println("isNew Called.");
		System.out.println(this.noListings);
		int newNumberOfListings = updateNumberOfListings();
		if (this.noListings < newNumberOfListings && newNumberOfListings > -1) {
			this.noListings = newNumberOfListings;
			//this.sendMail();
			System.out.println(this.noListings);
			System.out.println("Mail would have been sent");
		} else if (this.noListings >= newNumberOfListings && newNumberOfListings > -1) {
			this.noListings = newNumberOfListings;
			System.out.println("Mail not sent");
			this.startTimer(interval);
		} else {
			throw new Exception("newNumberOfListings was " + newNumberOfListings);
		}
	}

	/**
	 * Creates Mail Object, Configures and Sends Email and then calls startTimer to
	 * wait
	 * 
	 * @throws Exception
	 */
	private void sendMail() throws Exception {
		SendMail mail = new SendMail();
		mail.setConfiguration(this.mailConfiguration);
		mail.sendMail();
		this.startTimer(this.interval);
	}
	/**
	 * Puts Thread to sleep for given amount of Time
	 * @param interval
	 * @throws Exception
	 */
	private void startTimer(int interval) throws Exception {
		System.out.println("Timer Started");
		    Thread.sleep(interval * 1000);
		    this.isNew();
	}
	/**
	 * Method first called when new Thread is started
	 */
	public void run() {
		try {
			this.isNew();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBotConfig(Properties botConfig) {
		this.name = botConfig.getProperty("name");
		this.link = botConfig.getProperty("link");
		this.interval = Integer.parseInt(botConfig.getProperty("interval"));
	}

	public Properties getBotConfig() {
		return this.botConfig;
	}

	public void setMailConfig(Properties mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}

	public Properties getMailConfig() {
		return this.mailConfiguration;
	}
}
