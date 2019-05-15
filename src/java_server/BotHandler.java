package java_server;

import java.util.Properties;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BotHandler {
	private final String mailSenderRecepientRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]"
			+ "+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21"
			+ "\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)"
			+ "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]"
			+ "|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01"
			+ "-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private final String mailHostRegex = "^((\\*)|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]"
			+ "|[01]?[0-9][0-9]?)|((\\*\\.)?([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,63}?))$";
	private final String mailPortRegex = "^[0-9]{1,5}$";
	private final String botIntervalRegex = "([4-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8]"
			+ "[0-9]{2}|99[0-8][0-9]|999[0-9]|[1-8][0-9]{4}|9[0-8][0-9]{3}|99[0-8][0-9]{2}|999[0-8][0-9]|9999[0-9]"
			+ "|[1-5][0-9]{5}|60[0-3][0-9]{3}|604[0-7][0-9]{2}|604800)";
	private final String botLinkRegex = "(https?:\\/\\/(.+?\\.)?willhaben\\.at(\\/[A-Za-z0-9\\"
			+ "-\\._~:\\/\\?#\\[\\]@!$&'\\(\\)\\*\\+,;\\=]*)?)";
	private final String botBotIdRegex = "^[0-9]{1,3}$";
	private final String botNameRegex = "^[a-zA-Z0-9]{1,32}$";

	private String threadId;
	private String fileName;
	private SecretKey secretKey;
	private Map<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> innerMap = new HashMap<String, String>();
	private StreamCrypterAES fileCrypter = new StreamCrypterAES();
	private ConfigValidator configValidator = null;

	public BotHandler(String password) throws IOException, ClassNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		this.fileName = "botHandler.ini";
		this.secretKey = fileCrypter.getKey(password);
		File f = new File(this.fileName);
		if (f.isFile() && f.canRead()) {
			this.restoreMap();
		}
	}

	/**
	 * Creates a new Bot with the Given Configuration and starts it in its own
	 * Thread. Configuration and associated threadId are saved to Hashmap via
	 * Function addThreadToMap
	 * 
	 * @param botConfig  Bot Configuration consisting of 'link', 'name', 'botId',
	 *                   'interval'
	 * @param mailConfig mailConfiguration constisting of 'startTLS', 'mailHost',
	 *                   'mailPort', 'mailSender', 'mailRecepient', 'link', (Base64)
	 *                   'user', (Base64) 'password'
	 * @throws Exception
	 * @throws ValidationException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public void createBot(Properties botConfig, Properties mailConfig) throws ValidationException, IOException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.validateConfigs(botConfig, mailConfig);
		if (existsBot(botConfig.getProperty("botId"))) {
			throw new IOException("Tried to Create Bot that already exists, Bot ID: " + botConfig.getProperty("botId"));
		}
		Thread newBotThread = new Thread(new WillhabenBot(botConfig, mailConfig));
		this.threadId = String.valueOf(newBotThread.getId());
		if (this.threadId == "" | this.threadId == null) {
			throw new IOException(
					"threadId was null or empty after creating Thread for Bot ID: " + botConfig.getProperty("botId"));
		}
		this.addThreadToMap(botConfig, mailConfig, this.threadId);
		newBotThread.start();
		System.out.println("BotHandler Created Bot with ThreadId " + this.threadId);
	}

	/**
	 * Function to change options of Existing Bot - same Functionality as createBot,
	 * but stops Bot before overwriting Config
	 * 
	 * @param botConfig  Bot Configuration consisting of 'link', 'name', 'botId',
	 *                   'interval'
	 * @param mailConfig mailConfiguration constisting of 'startTLS', 'mailHost',
	 *                   'mailPort', 'mailSender', 'mailRecepient', 'link', (Base64)
	 *                   'user', (Base64) 'password'
	 * @throws Exception
	 * @throws ValidationException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public void changeBot(Properties botConfig, Properties mailConfig) throws ValidationException, IOException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.validateConfigs(botConfig, mailConfig);
		if (existsBot(botConfig.getProperty("botId"))) {
			stopBot(botConfig.getProperty("botId"));
		}
		Thread newBotThread = new Thread(new WillhabenBot(botConfig, mailConfig));
		this.threadId = String.valueOf(newBotThread.getId());
		if (this.threadId == "" | this.threadId == null) {
			throw new IOException(
					"ThreadId was null or empty after creating Thread for Bot ID: " + botConfig.getProperty("botId"));
		}
		this.addThreadToMap(botConfig, mailConfig, this.threadId);
		newBotThread.start();
		System.out.println("BotHandler Changed Bot with ThreadId " + this.threadId);

	}

	/**
	 * Deletes a Bot by deleting the Reference from outer HashMap
	 * 
	 * @param botId
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public void deleteBot(String botId)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.stopBot(botId);
		outerMap.remove(botId);
		System.out.println("Deleted Bot with ID: " + botId);
		this.saveMap();
	}

	/**
	 * Stops a running Bot by matching the BotId to the corresponding threadId and
	 * Interrupting the Thread via interruptThread() Function
	 * 
	 * @param botId generated by the GUI Client
	 * @throws IOException
	 */
	public void stopBot(String botId) throws IOException {
		this.innerMap = this.outerMap.get(botId);
		if (this.innerMap == null) {
			System.out.println("InnerMap was null at stopBot!");
			throw new IOException("Tried to stop Bot with ID: " + botId + " does not exist!");
		} else {
			interruptThread(this.innerMap.get("threadId"));
		}
	}

	/**
	 * Start an existing Bot by referencing its id and loading the configurations
	 * from the HashMap
	 * 
	 * @param botId generated by the GUI Client
	 * @throws Exception
	 * @throws ValidationException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public void startBot(String botId) throws ValidationException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		this.innerMap = this.outerMap.get(botId);
		if (this.innerMap == null) {
			System.out.println("InnerMap was null at startBot!");
			throw new IOException("Tried to start Bot with ID: " + botId + " does not exist!");
		}
		Properties botConfig = convertToBotConfig(this.innerMap);
		Properties mailConfig = convertToMailConfig(this.innerMap);
		System.out.println("Starting Existing Bot with ID " + botId);
		this.changeBot(botConfig, mailConfig);
	}

	/**
	 * Getter for current Configuration
	 * 
	 * @return HashMap with all Configurations
	 */
	public Map<String, HashMap<String, String>> getConfigurations() {
		return this.outerMap;
	}

	/**
	 * Validate botConfig and mailConfig - e.g. only valid Email-Formats allowed in
	 * mailSender
	 * 
	 * @param botConfig
	 * @param mailConfig
	 * @throws ValidationException
	 * @throws Exception
	 */
	private void validateConfigs(Properties botConfig, Properties mailConfig) throws ValidationException {
		final String[] botRegexKeys = { "botId", "name", "link", "interval" };
		final String[] mailRegexKeys = { "mailPort", "mailHost", "mailSender", "mailRecepient" };
		final String[] botConfigRegex = { botBotIdRegex, botNameRegex, botLinkRegex, botIntervalRegex };
		final String[] mailConfigRegex = { mailPortRegex, mailHostRegex, mailSenderRecepientRegex,
				mailSenderRecepientRegex };
		
		configValidator = new ConfigValidator(botRegexKeys, botConfigRegex);
		configValidator.validateProperty(botConfig);
		configValidator = new ConfigValidator(mailRegexKeys, mailConfigRegex);
		configValidator.validateProperty(mailConfig);
	}

	/**
	 * Saves the Bot Configuration in HashMap outerMap to File
	 * 
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private void saveMap() throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		ObjectOutputStream outputStreamMap = new ObjectOutputStream(
				fileCrypter.encryptFile(this.secretKey, this.fileName));
		outputStreamMap.writeObject(this.outerMap);
		outputStreamMap.close();
	}

	/**
	 * Creates Bot Configuration HashMap outerMap by reading from File
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	@SuppressWarnings("unchecked")
	private void restoreMap() throws IOException, ClassNotFoundException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException {
		System.out.println("Restoring Map ....");
		ObjectInputStream inputStreamMap = new ObjectInputStream(
				fileCrypter.decryptFile(this.secretKey, this.fileName));
		this.outerMap = (Map<String, HashMap<String, String>>) inputStreamMap.readObject();
		inputStreamMap.close();
	}

	/**
	 * Checks if a Bot already exists in HashMap
	 * 
	 * @param botId
	 * @return True if Exists, False otherwise
	 */
	private boolean existsBot(String botId) {
		if (this.outerMap.isEmpty() || this.outerMap == null || this.outerMap.toString().equals("{}")) {
			return false;
		}
		return this.outerMap.containsKey(botId);
	}

	/**
	 * Creates a set of all running Threads, iterates through the set until the
	 * threadId matches the passed argument threadId and then sends interrupt to the
	 * Thread
	 * 
	 * @param threadId ThreadId of Thread to kill
	 */
	private void interruptThread(String threadId) {
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
		for (Thread thread : setOfThread) {
			if (thread.getId() == Long.parseLong(threadId)) {
				thread.interrupt();
				System.out.println("Interrupted Thread " + threadId + " via BotHandler");
			}
		}

	}

	/**
	 * Converts a given Property to HashMap
	 * 
	 * @param props
	 * @return Converted HashMap
	 */
	private Map<String, String> convertToMap(Properties props) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, String> propMap = new HashMap(props);
		return propMap;
	}

	/**
	 * Converts a map in the correct Format to the botConfig Property for use with
	 * WillhabenBot Class
	 * 
	 * @param map
	 * @return Converted botConfig Property
	 */
	private Properties convertToBotConfig(Map<?, ?> map) {
		String[] botConfigItems = { "link", "name", "botId", "interval" };
		Properties botConfig = new Properties();
		for (int i = 0; i < botConfigItems.length; i++) {
			botConfig.put(botConfigItems[i], map.get(botConfigItems[i]));
		}
		return botConfig;
	}

	/**
	 * Converts a map in the correct Format to the mailConfig Property for use with
	 * WillhabenBot Class
	 * 
	 * @param map
	 * @return Converted mailConfig Property
	 */
	private Properties convertToMailConfig(Map<?, ?> map) {
		String[] mailConfigItems = { "startTLS", "user", "password", "mailSender", "mailRecepient", "mailHost",
				"mailPort", "link" };
		Properties mailConfig = new Properties();
		for (int i = 0; i < mailConfigItems.length; i++) {
			mailConfig.put(mailConfigItems[i], map.get(mailConfigItems[i]));
		}
		return mailConfig;
	}

	/**
	 * Adds the Bot Configurations and threadId to map, then adds this generated map
	 * to outerMap which is access by the key *botId*
	 * 
	 * @param botConfig  botConfig Property
	 * @param mailConfig botConfig Property
	 * @param threadId   ID of Thread this Bot is running in
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private void addThreadToMap(Properties botConfig, Properties mailConfig, String threadId)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		this.innerMap = new HashMap<String, String>();
		this.innerMap.putAll(convertToMap(botConfig));
		this.innerMap.putAll(convertToMap(mailConfig));
		this.innerMap.put("threadId", threadId);
		this.outerMap.put(botConfig.getProperty("botId"), this.innerMap);
		this.saveMap();
	}
}
