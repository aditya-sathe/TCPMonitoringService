package com.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pojo.Server;
import com.pojo.Server.State;

public class QueryUtil {
	/**
	 * Query the tcp host and port of the server
	 * @param srv
	 * @return
	 */
	public static boolean queryTCP(Server srv) {
		boolean result = false;
		try {
			Socket s = new Socket();
			s.setReuseAddress(true);
			SocketAddress sa = new InetSocketAddress(srv.getHostname(), srv.getPort());
			s.connect(sa, AppProps.queryTimeOut);
			if (s.isConnected()) {
				s.close();
				result = true;
			}
		} catch (IOException e) {
			srv.setError("Bad response : " + e.getLocalizedMessage());
		}

		return result;
	}
	
	/**
	 * Assuming we have configured gmail for sending emails
	 * @param alerts 
	 * @param s 
	 * @param serverName 
	 **/
	public static void sendNotification(ArrayList<String> alerts, String serverName, State s) {
		
		final String username = AppProps.emailusername;   // Gmail username
		final String password = AppProps.emailpass;  // Gmail password
		
		// Get system properties
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			Message message = new MimeMessage(session);

			// header field of the header.
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(alerts.get(0)));
			message.setSubject("Server Status Notification");
			message.setText("Server " + serverName + " status has changed to " + s );

			Transport.send(message); // send Message

			System.out.println("Message send to " + alerts.get(0));

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}


