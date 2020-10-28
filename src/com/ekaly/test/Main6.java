package com.ekaly.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.ekaly.tools.Tools;
import com.ekaly.web.Subscription;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class Main6 {
  public static void main(String[] args) throws IOException {
	  
	Path path = Paths.get("/home/fr054721/eagparis-events/WebContent/res/subscription.json");
	
	Subscription subscription = (Subscription) Tools.fromJSON(path.toFile(), new TypeReference<Subscription>(){});
	
	System.out.println(Tools.toJSON(subscription));

//  format du titre:  Inscription <email du gars> <titre event> <date event>
//  nom du csv:  <prenom>.<Nom>.<titre>.<date>.csv
	
    String subject = "Inscription " + subscription.getEmailAddress() + " " + subscription.getEventName() + " " + subscription.getEventStartDate();
    String csvName = subscription.getFirstName() + "." + subscription.getLastName() + "." + 
    		subscription.getEventName() + "." + subscription.getEventStartDate() + ".csv";
    csvName = csvName.replaceAll("\\s+", "-");
    
    System.out.println("subject=" + subject);
    System.out.println("csvName=" + csvName);
    System.out.println("attachement=" + subscription.toCsv(",", true));

    System.getProperty("line.separator");

    System.out.println(String.join(System.getProperty("line.separator"), subscription.toCsv(",", true)));
    
    Email from = new Email("iic_paris@fr.ibm.com");
    
    
    Email to = new Email("baudelaine@gmail.com");
//    Email to = new Email("iic_paris@fr.ibm.com");
    Content content = new Content("text/plain", "...");
    Mail mail = new Mail(from, subject, to, content);

    Attachments attachments = new Attachments();
    attachments.setFilename(csvName);
    attachments.setType("text/csv");
    attachments.setDisposition("attachment");    
    
    byte[] attachmentContentBytes = (String.join(System.getProperty("line.separator"), subscription.toCsv(",", true))).getBytes();
    String attachmentContent = Base64.getMimeEncoder().encodeToString(attachmentContentBytes);
    attachments.setContent(attachmentContent);
    mail.addAttachments(attachments);    
    
    SendGrid sg = new SendGrid("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");
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
  }
}