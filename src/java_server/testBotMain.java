package java_server;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class testBotMain {

	public static void main(String[] args) {
		
		Properties botConfig = new Properties();
		Properties mailConfig = new Properties();
		botConfig.put("link", "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+m365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+m365");
		botConfig.put("name", "TestName");
		botConfig.put("interval", "5");
		botConfig.put("botId", "111");
		BotHandler botHandler = new BotHandler();
		botHandler.createBot(botConfig, mailConfig);
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		botHandler.stopBot("111");
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		botHandler.startBot("111");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		botHandler.stopBot("111");
		}
	}


