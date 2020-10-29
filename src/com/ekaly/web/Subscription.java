package com.ekaly.web;

import java.util.ArrayList;
import java.util.List;

public class Subscription {
	
	//Company name,First name,Last name,E-mail address,Event name,Event start date,Business phone,Email choice, Phone choice
	
	
	String companyName = "";
	String firstName = "";
	String lastName = "";
	String emailAddress = "";
	String eventName = "";
	String eventStartDate = "";
	String businessPhone = "";
	boolean emailChoice = false;
	boolean phoneChoice = false;
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventStartDate() {
		return eventStartDate;
	}
	public void setEventStartDate(String eventStartDate) {
		this.eventStartDate = eventStartDate;
	}
	public String getBusinessPhone() {
		return businessPhone;
	}
	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}
	public boolean isEmailChoice() {
		return emailChoice;
	}
	public void setEmailChoice(boolean emailChoice) {
		this.emailChoice = emailChoice;
	}
	public boolean isPhoneChoice() {
		return phoneChoice;
	}
	public void setPhoneChoice(boolean phoneChoice) {
		this.phoneChoice = phoneChoice;
	}

	public String toCsv(String sep) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(this.getCompanyName() + sep + this.getFirstName() + sep + this.getLastName() + sep +
				this.getEmailAddress() + sep + this.getEventName() + " - " + this.getEventStartDate() + sep +
				this.getBusinessPhone() + sep + this.isEmailChoice() + sep + this.isPhoneChoice());
		
		return sb.toString();
	}
	
	public List<String> toCsv(String sep, boolean withHeader){
		
		List<String> result = new ArrayList<String>();
		
		String header = "Company name,First name,Last name,E-mail address,Event name - Event start date,Business phone,Email choice, Phone choice";
		header = header.replaceAll(",", sep);
		result.add(header);
		result.add(this.toCsv(sep));
		
		return result;
	}
	
}
