package java_server;

import java.util.Properties;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BotHandler {

	private String threadId;
	private String fileName;
	private String password;
	private Map<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> innerMap = new HashMap<String, String>();
	private EncryptorAES fileEncrypter = new EncryptorAES();
	private ConfigValidator configValidator = null;

	public BotHandler(String password, String user) throws IOException, ClassNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		this.fileName = user + "_config.ween";
		this.password = password;
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
	 * @throws InvalidKeySpecException
	 */
	public void createBot(Properties botConfig, Properties mailConfig) throws ValidationException, IOException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
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
	 * @throws InvalidKeySpecException
	 */
	public void changeBot(Properties botConfig, Properties mailConfig) throws ValidationException, IOException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
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
	 * @throws InvalidKeySpecException
	 */
	public void deleteBot(String botId) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException {
		this.stopBot(botId);
		outerMap.remove(botId);
		System.out.println("Deleted Bot with ID: " + botId);
		this.updateMap();
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
	 * @throws InvalidKeySpecException
	 */
	public void startBot(String botId) throws ValidationException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
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
		final String[] botConfigRegex = { Regex.botBotIdRegex, Regex.botNameRegex, Regex.botLinkRegex,
				Regex.botIntervalRegex };
		final String[] mailConfigRegex = { Regex.mailPortRegex, Regex.mailHostRegex, Regex.mailSenderRecepientRegex,
				Regex.mailSenderRecepientRegex };

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
	 * @throws InvalidKeySpecException
	 */
	private void saveMap() throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				fileEncrypter.encryptFile(this.fileName, this.password));
		objectOutputStream.writeObject(this.outerMap);
		objectOutputStream.close();
	}

	/**
	 * Creates Bot Configuration HashMap outerMap by reading from File
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 */
	@SuppressWarnings("unchecked")
	private void restoreMap() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, IOException, ClassNotFoundException {
		System.out.println("Restoring Map ....");
		ObjectInputStream inputStreamMap = new ObjectInputStream(
				fileEncrypter.decryptFile(this.fileName, this.password));
		this.outerMap = (Map<String, HashMap<String, String>>) inputStreamMap.readObject();
		inputStreamMap.close();
	}
	
	/**
	 * UpdateMap assures that the HashMap<Key: botID, Value: Bot/MailConfiguration> have a botId in ascending order (beginning with 1)
	 * When a bot is removed via the delteBot() Function this Method is called an all botId´s are corrected 
	 * 
	 * Example: Keys of HashMap with 5 Bots: 				1,2,3,4,5
	 * 			1. Bot with Key 3 is removed
	 * 				--> the keys are not in order:			1,2,4,5
	 * 
	 * 			2. updateMap is called and order corrected	1,2,3,4
	 * 
	 * This is mandatory as the gui client labels bots in ascending order and closes holes
	 * 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public void updateMap() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		int keys[] = new int[this.outerMap.size()];
		int i = 0;
		for (Map.Entry<String, HashMap<String, String>> entry : this.outerMap.entrySet()) {
			keys[i] = Integer.parseInt(entry.getKey());
			i++;
		}
		Arrays.sort(keys);
		for (i = 0; i < keys.length; i++) {
			if (keys[i] != i) {
				this.outerMap.put(String.valueOf(i), this.outerMap.get(String.valueOf(keys[i])));
				this.outerMap.remove(String.valueOf(keys[i]));
			}
		}
		this.saveMap();
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
	 * @throws InvalidKeySpecException
	 */
	private void addThreadToMap(Properties botConfig, Properties mailConfig, String threadId) throws IOException,
			InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
		this.innerMap = new HashMap<String, String>();
		this.innerMap.putAll(convertToMap(botConfig));
		this.innerMap.putAll(convertToMap(mailConfig));
		this.innerMap.put("threadId", threadId);
		this.outerMap.put(botConfig.getProperty("botId"), this.innerMap);
		this.saveMap();
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
		Set<Thread> setOfThread = new HashSet<Thread>(Thread.getAllStackTraces().keySet());
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
}
