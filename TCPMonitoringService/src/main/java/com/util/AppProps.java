package com.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.pojo.DownTime;
import com.pojo.Server;

public class AppProps {

	public static ArrayList<Server> serverlist = new ArrayList<>();

	public static int queryInterval = 0;

	public static int queryTimeOut = 0;
	
	public static String emailusername;
	
	public static String emailpass;
	
	public static void loadConfigurartion(String file) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(file));
			document.getDocumentElement().normalize();
			
			Node gennode = document.getElementsByTagName("general").item(0);
			if (gennode.getNodeType() == Node.ELEMENT_NODE) {
				Element genele = (Element) gennode;
				queryInterval = Integer.parseInt(genele.getElementsByTagName("queryInterval").item(0).getTextContent());
				queryTimeOut = Integer.parseInt(genele.getElementsByTagName("queryTimeOut").item(0).getTextContent());
				emailusername = genele.getElementsByTagName("emailuser").item(0).getTextContent();
				emailpass = genele.getElementsByTagName("emailpass").item(0).getTextContent();
			}
			
			NodeList nList = document.getElementsByTagName("server");
			Server serverObj = null;
			Element serverElement = null;
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					serverElement = (Element) node;
					// Create new server object
					serverObj = new Server();

					serverObj.setName(serverElement.getElementsByTagName("name").item(0).getTextContent());
					serverObj.setHostname(serverElement.getElementsByTagName("hostname").item(0).getTextContent());
					serverObj.setPort(
							Integer.valueOf(serverElement.getElementsByTagName("port").item(0).getTextContent()));
					serverObj.setPollingfreq(
							Integer.valueOf(serverElement.getElementsByTagName("poolingfreq").item(0).getTextContent()));
					serverObj.setGracetime(
							Integer.valueOf(serverElement.getElementsByTagName("gracetime").item(0).getTextContent()));
					
					Node emailnode = serverElement.getElementsByTagName("sendAlertTo").item(0);
					if (emailnode !=null && node.getNodeType() == Node.ELEMENT_NODE) {
							Element emailEle = (Element) emailnode;
							serverObj.getAlertlist()
									.add(emailEle.getElementsByTagName("email").item(0).getTextContent());
						}
					}

					NodeList dtList = serverElement.getElementsByTagName("downTime");
					Node dtnode = dtList.item(0);
					if (dtnode !=null  && node.getNodeType() == Node.ELEMENT_NODE) {
						Element dtele = (Element) dtnode;

						DownTime dtObj = new DownTime();
						dtObj.setStart(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(dtele.getElementsByTagName("start").item(0).getTextContent()));
						dtObj.setEnd(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
								.parse(dtele.getElementsByTagName("end").item(0).getTextContent()));
						serverObj.setMaintainance(dtObj);
					}
					System.out.println("Server Obj----->> " + serverObj);
					serverlist.add(serverObj);
				}
		}
		catch(ParserConfigurationException|SAXException|IOException|DOMException|ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
