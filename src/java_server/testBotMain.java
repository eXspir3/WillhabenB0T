package java_server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.NoSuchPaddingException;

public class testBotMain {

	public static void main(String[] args) {

		// Fill Configurations
		Properties botConfig = new Properties();
		Properties mailConfig = new Properties();
		botConfig.put("link",
				"https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=Xiaomi+m365&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=Xiaomi+m365");
		botConfig.put("name", "TestName");
		botConfig.put("interval", "30");
		botConfig.put("botId", "111");
		mailConfig.put("startTLS", "true");
		mailConfig.put("mailPort", "587");
		mailConfig.put("mailHost", "mail.ipax.at");
		mailConfig.put("mailSender", "");
		mailConfig.put("mailRecepient", "");
		String user = "";
		String password = "";
		// ACHTUNG
		// Passwörter und User müssen ab jetzt Base64 encoded sein um zu
		// funktionieren!!!!
		String userBase64 = Base64.getEncoder().encodeToString(user.getBytes());
		String passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes());
		mailConfig.put("user", userBase64);
		mailConfig.put("password", passwordBase64);
		// plainPassword String for BotHandler encryption / decryption
		try {
			String plainPassword = "GeheimesPasswort";
			BotHandler botHandler = new BotHandler(plainPassword);
			botHandler.createBot(botConfig, mailConfig);
			Thread.sleep(8000);
			botHandler.stopBot("111");
			botHandler = new BotHandler(plainPassword);
			Thread.sleep(2000);
			botHandler.startBot("111");
			botConfig.put("botId", "112");
			botHandler.createBot(botConfig, mailConfig);
			botConfig.put("botId", "113");
			botHandler.createBot(botConfig, mailConfig);
			System.out.println(botHandler.getConfigurations());
		} catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | InvalidKeySpecException | InterruptedException | ValidationException e) {
			System.out.println(e);
		}
	}
}
