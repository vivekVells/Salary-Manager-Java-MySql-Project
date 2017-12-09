package Utility;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendEmail {
	
	public static void sendEmailToUser(String toEmailSend, String toUsername, String toPassword) {
    	// before this goto the following link and switch it on
    	// link: https://www.google.com/settings/security/lesssecureapps
    	// https://stackoverflow.com/questions/3649014/send-email-using-java
    	
        final String username = "vivekkevin22@gmail.com";
        final String password = "Testingmail";

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("vivekkevin22@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmailSend));
            message.setSubject("Salary Manager User Account Recovery Information");
            message.setText("Dear " + toUsername +"\n" 
            		+"\nPlease find your user account information below\n\n"
            		+ "Username: " + toUsername
            		+ "\nPassword : " + toPassword
            		+ "\n\nThank you for using our app"
            		+ "\n\nHave a great day"
            		+ "\n\nSincerely,"
            		+ "\nVivek Vellaiyappan Surulimuthu"
            		+ "\nSoftware Engineer"
            		+ "\nSalary Manager Team");

            Transport.send(message);

            System.out.println("Email has been sent successfully...");

        } catch (MessagingException e) {
        	System.out.println("Email was unsuccessfull...");
            throw new RuntimeException(e);
        }
	}
    public static void main(String[] args) {  }
}