package util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Anagha.Ingle
 *
 */
public class EmailUtil {
	
	static Properties mailProp = null;
	static Session session = null;
	static Message message = null;
	

	/**
	 * @param host
	 * @param port
	 * @param from
	 * @param password
	 * @param subject
	 * @param sendTo
	 * @param messageBody
	 * @param inlineImages
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public static void messageSend(String host, String port, final String from, final String password, String subject, String sendTo, String messageBody, Map<String, String> inlineImages) throws AddressException, MessagingException {
		
		//Set MailProperties
		mailProp = new Properties();
		mailProp.put("mail.smtp.host", host);
		mailProp.put("mail.smtp.port", port);
		mailProp.put("mail.smtp.auth", "true");
		mailProp.put("mail.smtp.starttls.enable", "true");
		mailProp.put("mail.user", from);
		mailProp.put("mail.password", password);
		
		// Set Session 
		Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
		session = Session.getDefaultInstance(mailProp, auth);
		
		//set message
		message = new MimeMessage(session);
				
		message.setFrom(new InternetAddress(from));
		InternetAddress[] sendToMail = { new InternetAddress(sendTo) };
		message.setRecipients(Message.RecipientType.TO, sendToMail);
		
		message.setSubject(subject);
		message.setSentDate(new Date());
		
		// creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(messageBody, "text/html ; charset=utf-8");
 
        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        
        //Add images to mailBody
        if(inlineImages != null && inlineImages.size()>0) {
        	Set<String> setImageID = inlineImages.keySet();
            
            for (String contentId : setImageID) {               

                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);         
      
                String imageFilePath = inlineImages.get(contentId);
                try {
                	imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
               //add image to multipart
                multipart.addBodyPart(imagePart);
            }
        }
        message.setContent(multipart);
		Transport.send(message);
        
	}
}
