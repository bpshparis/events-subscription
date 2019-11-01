package com.ekaly.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ekaly.tools.Group;
import com.ekaly.tools.Tools;
import com.ekaly.tools.User;

public class Main5 {

	public static void main(String[] args) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		// TODO Auto-generated method stub

		Path path = Paths.get("/opt/wks/stidlls/WebContent/sounds/contacts.xml");
		
		if(Files.exists(path)) {
			
			String xml = IOUtils.toString(new FileInputStream(path.toFile()));
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));
			
			XPathFactory xfact = XPathFactory.newInstance();
			XPath xpath = xfact.newXPath();
			
			NodeList nodeList = (NodeList) xpath.evaluate("/imClientConfig/contactList/group", document, XPathConstants.NODESET);
			
			List<Group> contacts = new ArrayList<Group>();
			
			for(int index = 0; index < nodeList.getLength(); index++){
				Node node = nodeList.item(index);
				Group group = new Group();
				group.setName(getAttrValue(node, "name"));
				NodeList childNodes = node.getChildNodes();
				List<User> users = new ArrayList<User>();
				for(int i = 0; i < childNodes.getLength(); i++){
					Node childNode = childNodes.item(i);
					if(childNode.getNodeName().equalsIgnoreCase("user")){
						User user = new User();
						user.setDisplayName(getAttrValue(childNode, "displayName"));
						user.setId(getAttrValue(childNode, "id"));
						users.add(user);
					}
				}
				group.setUsers(users);
				contacts.add(group);
			}
			
			System.out.println(Tools.toJSON(contacts));
			
		}
		
		
	}
	
	static private String getAttrValue(Node node,String attrName) {
	    if ( ! node.hasAttributes() ) return "";
	    NamedNodeMap nmap = node.getAttributes();
	    if ( nmap == null ) return "";
	    Node n = nmap.getNamedItem(attrName);
	    if ( n == null ) return "";
	    return n.getNodeValue();
	}
	
	@SuppressWarnings("unused")
	static private String getTextContent(Node parentNode,String childName) {
	    NodeList nlist = parentNode.getChildNodes();
	    for (int i = 0 ; i < nlist.getLength() ; i++) {
		    Node n = nlist.item(i);
		    String name = n.getNodeName();
		    if ( name != null && name.equals(childName) ) return n.getTextContent();
	    }
	    return "";
	}		

}
