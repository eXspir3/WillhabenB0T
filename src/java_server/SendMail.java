package java_server;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class SendMail {
	boolean startTLS = false;
	int mailPort = 25;
	String mailHost = "";
	String mailSender = "willhabenbot@noaddressentered.net";
	String mailRecepient = "";
	String username = "";
	String password = "";
	String link = "No-Link-Entered";

	SendMail(Properties mailConfiguration, String link) throws Exception {
		this.setConfiguration(mailConfiguration);
		this.forgeMail(link);
		this.sendMail();
	}

	void setConfiguration(Properties mailConfiguration) {
		this.startTLS = Boolean.parseBoolean(mailConfiguration.getProperty("startTLS"));
		this.mailPort = Integer.parseInt(mailConfiguration.getProperty("mailPort"));
		this.mailHost = mailConfiguration.getProperty("mailHost");
		this.mailSender = mailConfiguration.getProperty("mailSender");
		this.mailRecepient = mailConfiguration.getProperty("mailRecepient");
		username = mailConfiguration.getProperty("username");
		password = mailConfiguration.getProperty("password");
	}
	
	void forgeMail(String link) {
		//NOT IMPLEMENTED
	}

	void sendMail() throws Exception {

		Properties mailAuthProps = new Properties();
		mailAuthProps.put("mail.smtp.auth", "true");
		mailAuthProps.put("mail.smtp.starttls.enable", this.startTLS);
		mailAuthProps.put("mail.smtp.host", this.mailHost);
		mailAuthProps.put("mail.smtp.port", this.mailPort);

		// Get the Session object.
		Session session = Session.getInstance(mailAuthProps, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("willhabenbot@ensinger.eu", "bot");
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.mailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.mailRecepient));
			message.setSubject("Testing Subject");

			// Send the actual HTML message will be imported form File in Future
			message.setContent("<h1>This is actual message embedded in HTML tags</h1>", "text/html");
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
