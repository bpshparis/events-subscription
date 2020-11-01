package com.ekaly.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekaly.tools.Tools;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

/**
 * Servlet implementation class GetImportedKeysServlet
 */
@WebServlet("/Subscribe")
public class SubscribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubscribeServlet() {
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

			Subscription subscription = (Subscription) Tools.fromJSON(request.getInputStream(), new TypeReference<Subscription>(){});
			
	        if(subscription != null ) {
	        	
	            String subject = "Inscription " + subscription.getEmailAddress() + " " + subscription.getEventName() + " " + subscription.getEventStartDate();
	            String csvName = subscription.getFirstName() + "." + subscription.getLastName() + "." + 
	            		subscription.getEventName() + "." + subscription.getEventStartDate() + ".csv";
	            csvName = csvName.replaceAll("\\s+", "-");
	            
	            System.out.println("subject=" + subject);
	            System.out.println("csvName=" + csvName);
	            System.out.println("attachement=" + subscription.toCsv(",", true));

	            System.out.println("line.separator=" + System.getProperty("line.separator"));

	            System.out.println(String.join(System.getProperty("line.separator"), subscription.toCsv(",", true)));
	            
	            
	            Email from = new Email("iic_paris@fr.ibm.com");
	            
	            Email to = new Email("iic_paris@fr.ibm.com");
//	            Email to = new Email("baudelaine@gmail.com");
	            Content content = new Content("text/plain", "...");
	            Mail mail = new Mail(from, subject, to, content);

	            Attachments attachments = new Attachments();
	            attachments.setFilename(csvName);
	            attachments.setType("text/csv");
	            attachments.setDisposition("attachment");    
	            
	            byte[] attachmentContentBytes = (String.join(System.getProperty("line.separator"), subscription.toCsv(",", true))).getBytes();
//	            String attachmentContent = Base64.getMimeEncoder().encodeToString(attachmentContentBytes);
	            String attachmentContent = Base64.getEncoder().encodeToString(attachmentContentBytes);
	            attachments.setContent(attachmentContent);
	            mail.addAttachments(attachments);    
	            
	            SendGrid sg = new SendGrid("");
	            Request sgRequest = new Request();
	            sgRequest.setMethod(Method.POST);
	            sgRequest.setEndpoint("mail/send");
	            sgRequest.setBody(mail.build());
	            Response sgResponse = sg.api(sgRequest);
	            result.put("SG_STATUS_CODE", sgResponse.getStatusCode());
	            result.put("SG_HEADER", sgResponse.getHeaders());
	        	
	        }
	        else{
        		throw new Exception("Input parameter is not a valid Subscription object.");
	        }
			result.put("STATUS", "OK");
		}
		catch(Exception e){
			result.put("STATUS", "KO");
            result.put("EXCEPTION", e.getClass().getName());
            result.put("MESSAGE", e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("STACKTRACE", sw.toString());
		}			
		
		finally {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(Tools.toJSON(result));
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

