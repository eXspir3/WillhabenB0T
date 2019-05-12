package java_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;

public class testBotMain {

	public static void main(String[] args) {
		
		Properties botConfig = new Properties();
		Properties mailConfig = new Properties();
		botConfig.put("link", "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+m365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+m365");
		botConfig.put("name", "TestName");
		botConfig.put("interval", "30");
		botConfig.put("botId", "111");
		mailConfig.put("startTLS", "true");
		mailConfig.put("mailPort", "587");
		mailConfig.put("mailHost", "mail.ipax.at");
		mailConfig.put("mailSender", "willhabenbot@ensinger.eu");
		mailConfig.put("mailRecepient", "philippensi2@gmail.com");
		String user = "willhabenbot@ensinger.eu";
		String password = "bot"; 
		// ACHTUNG
		// Passw�rter und User m�ssen ab jetzt Base64 encoded sein um zu funktionieren!!!!
		String userBase64 = Base64.getEncoder().encodeToString(user.getBytes());
		String passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes());
		mailConfig.put("user", userBase64);
		mailConfig.put("password", passwordBase64);
		
		BotHandler botHandler = null;
		try {
			//Erstellen des Bothandlers und erzeugen eines neuen Bots
			botHandler = new BotHandler();
			botHandler.createBot(botConfig, mailConfig);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
}

