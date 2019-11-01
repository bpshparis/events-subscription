package com.ekaly.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ekaly.tools.Group;
import com.ekaly.tools.Tools;
import com.ekaly.tools.User;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Servlet implementation class GetImportedKeysServlet
 */
@WebServlet("/RA")
public class RecognizeAudioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecognizeAudioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();

		result.put("SESSIONID", request.getSession().getId());		
		result.put("CLIENT", request.getRemoteAddr() + ":" + request.getRemotePort());
		result.put("SERVER", request.getLocalAddr() + ":" + request.getLocalPort());
		result.put("FROM", this.getServletName());
		
		try{

			String xml = (String) request.getSession().getAttribute("XML");
			
			if(xml == null) {
	    		result.put("TROUBLESHOOTING", "Please upload Sametime Contacts in XML before processing.");
	    		throw new Exception();
			}
			
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
			
			
        	if(!contacts.isEmpty()) {
    			result.put("STATUS", "OK");
        		result.put("ANSWER", contacts);
        	}
        	else {
        		result.put("TROUBLESHOOTING", "No contact found in XML.");
        		throw new Exception();
        	}
	        
		}
		catch(Exception e){
			if(! result.containsKey("ANSWER")) {
        		result.put("ANSWER", "An error occured while processing file.");
			}
			result.put("STATUS", "KO");
            result.put("EXCEPTION", e.getClass().getName());
            result.put("MESSAGE", e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("STACKTRACE", sw.toString());
            e.printStackTrace(System.err);
		}			
		
		finally {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(result));
		}

	}
	
	private String getAttrValue(Node node,String attrName) {
	    if ( ! node.hasAttributes() ) return "";
	    NamedNodeMap nmap = node.getAttributes();
	    if ( nmap == null ) return "";
	    Node n = nmap.getNamedItem(attrName);
	    if ( n == null ) return "";
	    return n.getNodeValue();
	}	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

