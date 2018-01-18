package com.main;

import java.util.ArrayList;
import java.util.List;

public class UserOnline {

	private static List<String> userOnline = new ArrayList<>();

	
	public static synchronized void putUser(String user) {
		if(!userOnline.contains(user)) {
			userOnline.add(user);
		}
	}
	
	public static synchronized void removeUser(String user) {
		userOnline.remove(user);
	}
	
	public static Boolean isUserOnline(String user) {
		return userOnline.contains(user);
	}
	
	public static List<String> listUserOnline() {
		return userOnline;
		
	}
	
}
