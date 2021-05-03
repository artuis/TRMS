package com.revature.factories;

import java.util.Map;

import com.revature.beans.Employee;

public interface TokenFactory {
	String issueToken(Employee e);
	boolean authorize(String token, Employee e);
	Map<String, String> decode(String token);
	boolean authorizeAdmin(String cookie, Employee updater);
}
