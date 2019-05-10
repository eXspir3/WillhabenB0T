package java_server;

import java.io.IOException;
import java.util.Properties;

public class testBotMain {

	public static void main(String[] args) {
		
		Properties botConfig = new Properties();
		Properties mailConfig = new Properties();
		botConfig.put("link", "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+m365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+m365");
		botConfig.put("name", "TestName");
		botConfig.put("interval", "5");
		botConfig.put("botId", "111");
		mailConfig.put("startTLS", "true");
		mailConfig.put("mailPort", "587");
		mailConfig.put("mailHost", "mail.ipax.at");
		mailConfig.put("mailSender", "BLANK");
		mailConfig.put("mailRecepient", "BLANK");
		mailConfig.put("user", "BLANK");
		mailConfig.put("password", "BLANK");
		BotHandler botHandler = null;
		try {
			//Erstellen des Bothandlers und erzeugen eines neuen Bots
			botHandler = new BotHandler();
			botHandler.createBot(botConfig, mailConfig);
			Thread.sleep(12000);
			botHandler.stopBot(botConfig.getProperty("botId"));
			System.out.println("------------------------------------");
			//Bot wurde gestoppt sollte aber nun in File gespeichert sein -- neuer Bothandler wird erzeugt und startBot aufgerufen
			Thread.sleep(3000);
			botHandler = new BotHandler();
			botHandler.startBot(botConfig.getProperty("botId"));
			Thread.sleep(3000);
			botHandler.deleteBot(botConfig.getProperty("botId"));
			Thread.sleep(3000);
			System.out.println();
			//Bot wurde gelöscht , es wird nun versucht diesen Bot zu starten aber er existiert nicht
			botHandler.startBot(botConfig.getProperty("botId"));
		} catch (IOException | InterruptedException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
}

