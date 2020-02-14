package java_server;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WillhabenBot implements Runnable {

    private String link;
    private int interval;
    private int noListings;
    private Properties mailConfiguration;
    private Properties botConfig;

    public WillhabenBot(Properties botConfig, Properties mailConfiguration) {
        this.botConfig = botConfig;
        String botId = botConfig.getProperty("botId");
        String name = botConfig.getProperty("name");
        this.link = botConfig.getProperty("link");
        this.interval = Integer.parseInt(botConfig.getProperty("interval"));
        this.mailConfiguration = mailConfiguration;
        this.noListings = this.updateNumberOfListings();
    }

    /**
     * Method first called when new Thread is started, runs in a loop until thread
     * is Interrupted
     */
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Uncomment next Line for Testing sendMail()
                //this.sendMail();
                this.isNew();
                this.startTimer(this.interval);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread Exit by Exception");
        }
        Thread.currentThread().interrupt();
        System.out.println("Thread Exit");
    }

    /**
     * Checks if Link is not Null
     *
     * @return The Number of Listings extracted by extractNumberOfListings()
     */
    private String prepareLink() {
        if (this.link != null && !Objects.equals(this.link, "")) {
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
     * @param link The current link searched by WillhabenBot
     * @return The Number of Listings extractet from Site Sourcecode.
     */
    private int extractNumberOfListings(String link) {
        try {
            if (link == null || Objects.equals(link, ""))
                throw new Exception("Link Null");
            org.jsoup.nodes.Document website = Jsoup.connect(link).get();
            Elements strippedHtml = website.select("script");
            Pattern noListingsPattern = Pattern.compile("(search_results_number\":\")(.{1,4}\\d)");
            Matcher mat = noListingsPattern.matcher(strippedHtml.toString());
            if (!mat.find()) {
                System.out.println("mat.find Error");
                this.run();
            }
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
     * to the changes and calls sendMail if there is a new Listing.
     */
    private void isNew() throws Exception {

        System.out.println("isNew Called");
        System.out.println("Current Number of Listings: " + this.noListings);
        int newNumberOfListings = updateNumberOfListings();
        if (this.noListings < newNumberOfListings && newNumberOfListings > -1) {
            this.noListings = newNumberOfListings;
            this.sendMail();
            System.out.println(this.noListings);
            System.out.println("Mail should have been sent");
        } else if (this.noListings >= newNumberOfListings && newNumberOfListings > -1) {
            this.noListings = newNumberOfListings;
            System.out.println("Mail not sent");
        } else {
            throw new Exception("newNumberOfListings was " + newNumberOfListings);
        }
    }

    /**
     * Creates Mail Object, Configures and Sends Email and then calls startTimer to
     * wait
     *
     */
    private void sendMail() {
        new SendMail(this.mailConfiguration, this.botConfig.getProperty("link"));
    }

    /**
     * Puts Thread to sleep for given amount of Time
     *
     * @param interval Intervall in seconds between Willhaben Searches
     */
    private void startTimer(int interval) throws InterruptedException {
        System.out.println("Timer Started: " + interval + " seconds");
        Thread.sleep(interval * 1000);
    }
}
