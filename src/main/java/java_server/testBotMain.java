package java_server;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Properties;

public class testBotMain {

    public static void main(String[] args) {

        // Fill Configurations
        Properties botConfig = new Properties();
        Properties mailConfig = new Properties();
        botConfig.put("link",
                "https://www.willhaben.at/iad/kaufen-und-verkaufen/marktplatz?keyword=gtx+1080+ti&attribute_tree_level_0=&attribute_tree_level_1=&typedKeyword=gtx+1080+ti");
        botConfig.put("name", "TestName");
        botConfig.put("interval", "40");
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
        try {
            UserAuthentication userAuth = new UserAuthentication();
            userAuth.login("Admin", "defaultwillhaben");

            //userAuth.login("testUser1", "IchBinEinPasswort!");
            //userAuth.removeUser("testUser1");
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException
                | ClassNotFoundException | AuthenticationException | IOException | ValidationException e) {
            // TODO Auto-generated catch block
            System.out.println(e.toString());
            e.printStackTrace();
        }
        String userBase64 = Base64.getEncoder().encodeToString(user.getBytes());
        String passwordBase64 = Base64.getEncoder().encodeToString(password.getBytes());
        mailConfig.put("user", userBase64);
        mailConfig.put("password", passwordBase64);
        // plainPassword String for BotHandler encryption / decryption
        try {
            String plainPassword = "GeheimesPasswort";
            BotHandler botHandler = new BotHandler(plainPassword, "Ensi");
            botHandler.updateMap();
//			botHandler.createBot(botConfig, mailConfig);
            botHandler.startBot("0");
            Thread.sleep(2000);
//			botConfig.put("botId", "112");
//			botHandler.createBot(botConfig, mailConfig);
//			botConfig.put("botId", "113");
//			botHandler.createBot(botConfig, mailConfig);

        } catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeySpecException | InterruptedException | ValidationException e) {
            System.out.println(e);
        }
    }
}

