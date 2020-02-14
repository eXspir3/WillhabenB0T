package java_server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

class SendMail {
    boolean startTLS = false;
    int mailPort = 25;
    String mailHost = "";
    String mailSender = "willhabenbot@noaddresseconfigured.net";
    String mailRecepient = "";
    String user = "";
    String password = "";
    String link = "No-Link-Entered";

    SendMail(Properties mailConfiguration, String link) {
        this.setConfiguration(mailConfiguration, link);
        this.sendMail();
    }

    /**
     * Decodes Auth-Data from Base64
     *
     * @param encodedString Base64 encoded String
     * @return Base64 decoded String
     */
    private String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }

    /**
     * Sets all Configurations for SendMail Class (called by Constructor)
     *
     * @param mailConfiguration Configuration for SMTP Header
     * @param link              Link of the current WIllhaben-Search
     */
    private void setConfiguration(Properties mailConfiguration, String link) {
        this.startTLS = Boolean.parseBoolean(mailConfiguration.getProperty("startTLS"));
        this.mailPort = Integer.parseInt(mailConfiguration.getProperty("mailPort"));
        this.mailHost = mailConfiguration.getProperty("mailHost");
        this.mailSender = mailConfiguration.getProperty("mailSender");
        this.mailRecepient = mailConfiguration.getProperty("mailRecepient");
        this.user = mailConfiguration.getProperty("user");
        this.password = mailConfiguration.getProperty("password");
        this.link = link;
    }

    /**
     * Loads the mailTemplate.html from File into String and alters content
     * accordingly
     *
     * @param link Link to new Listings
     * @return The complete HTML-Mail as String
     */
    private String forgeMail(String link) throws IOException {
        StringBuilder mailBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader("mailTemplate.html"));
        String str;
        while ((str = in.readLine()) != null) {
            mailBuilder.append(str);
        }
        in.close();
        String mail = mailBuilder.toString();
        mail = mail.replaceAll("UrlToBeReplaced", link);
        return mail;
    }

    /**
     * Creates SMTP Session and authenticates to Server, then sends Mail
     */
    private void sendMail() {

        Properties mailAuthProps = new Properties();
        mailAuthProps.put("mail.smtp.auth", "true");
        mailAuthProps.put("mail.smtp.starttls.enable", this.startTLS);
        mailAuthProps.put("mail.smtp.host", this.mailHost);
        mailAuthProps.put("mail.smtp.port", this.mailPort);
        Session session = Session.getInstance(mailAuthProps, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(decodeBase64(user), decodeBase64(password));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.mailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.mailRecepient));
            message.setSubject("WillhabenBot - New Listings found!");
            message.setContent(forgeMail(this.link), "text/html");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
