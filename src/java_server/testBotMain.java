package java_server;

import java.util.Properties;

public class testBotMain {

	public static void main(String[] args) {
		Properties botConfig = new Properties();
		Properties mailConfig = new Properties();
		botConfig.put("link", "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+m365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+m365");
		botConfig.put("name", "TestName");
		botConfig.put("interval", "9");
		WillhabenBot test = new WillhabenBot(botConfig, mailConfig);
		Thread myThread = new Thread(test);
		myThread.start();
		
	}

}
