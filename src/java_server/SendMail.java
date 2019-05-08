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
	String mailSender = "";
	String mailRecepient = "";
	String username = "";
	String password = "";

//	SendMail(boolean startTLS, int mailPort, String mailHost, String mailSender, ArrayList<String> mailRecepient, String username,
//			String password) {
//		this.startTLS = startTLS;
//		this.mailPort = mailPort;
//		this.mailHost = mailHost;
//		this.mailSender = mailSender;
//		this.username = username;
//		this.password = password;
//		this.mailRecepient = "";
//		for (String element : mailRecepient) {
//			if (this.mailRecepient == "") {
//				this.mailRecepient = element;
//			} else {
//				this.mailRecepient = this.mailRecepient + "," + element;
//			}
//		}
//	}

	SendMail() {
		this.startTLS = startTLS;
		this.mailPort = mailPort;
		this.mailHost = mailHost;
		this.mailSender = mailSender;
		this.mailRecepient = mailRecepient;
		this.username = username;
		this.password = password;
	}

	void setConfiguration(Properties mailConfiguration) {
		// NOT IMPLEMENTED
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
				return new PasswordAuthentication(username, password);
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
