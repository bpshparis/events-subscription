package com.ekaly.tools;

import java.util.ArrayList;
import java.util.List;

public class Group {

	String name = "";
	List<User> users = new ArrayList<User>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
