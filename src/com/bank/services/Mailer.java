package com.bank.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;

public class Mailer {
	public static void send(String to, String subject, String msg) {

		final String user = "gauthamvgtm@gmail.com";// change accordingly
		final String pass = "8951726486";

		// 1st step) Get the session object

		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		System.out.println("properties done");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});

		System.out.println("SESSION Done");
		// 2nd step)compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(msg);
//			StringWriter writer = new StringWriter();
//			try {
//				IOUtils.copy(new FileInputStream(new File("register_email.jsp")), writer);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			message.setContent(writer.toString(), "text/html");
			

			// 3rd step)send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}
}