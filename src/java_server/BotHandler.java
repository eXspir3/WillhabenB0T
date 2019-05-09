package java_server;

import java.util.Properties;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class BotHandler {

	private String threadId;
	private Map<String, HashMap<String, String>> outerMap = new HashMap<String, HashMap<String, String>>();
	private HashMap<String, String> innerMap = new HashMap<String, String>();

	public BotHandler() {
		this.threadId = threadId;
		this.outerMap = outerMap;
		this.innerMap = innerMap;
	}

	public void createBot(Properties botConfig, Properties mailConfig) {
		Thread newBotThread = new Thread(new WillhabenBot(botConfig, mailConfig));
		this.threadId = String.valueOf(newBotThread.getId());
		System.out.print(this.threadId);
		this.addThreadToMap(botConfig, mailConfig, this.threadId);
		newBotThread.start();
	}

	public void stopBot(String botId) {
		this.innerMap = this.outerMap.get(botId);
		interruptThread(this.innerMap.get("threadId"));
	}

	private void interruptThread(String threadId) {
		Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
		System.out.println(setOfThread.toString());
		for (Thread thread : setOfThread) {
			if (thread.getId() == Long.parseLong(threadId)) {
				thread.interrupt();
				System.out.print("Interrupted Thread " + threadId + "via BotHandler");
			}
		}

	}
	private Map<String, String> convertToMap(Properties props) {
		Map<String, String> propMap = new HashMap(props);
		return propMap;
	}
	
	private Properties convertToProperty(Map<?, ?> map) {
		Properties props = new Properties();
		props.putAll(map);
		return props;
	}
	
	private void addThreadToMap(Properties botConfig, Properties mailConfig, String threadId) {
		
		this.innerMap = new HashMap<String, String>();
		innerMap.putAll(convertToMap(botConfig));
		innerMap.putAll(convertToMap(mailConfig));
		innerMap.put("threadId", threadId);
		outerMap.put(botConfig.getProperty("botId"), innerMap);
		System.out.println(outerMap.toString());
		System.out.println(innerMap.toString());
	}

}
