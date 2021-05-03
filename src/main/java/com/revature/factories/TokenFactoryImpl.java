package com.revature.factories;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import com.revature.beans.Employee;
import com.revature.beans.Role;

import io.javalin.http.ForbiddenResponse;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenFactoryImpl implements TokenFactory {
	
	private Key key;
	private static TokenFactory tokenFactory;
	
	private TokenFactoryImpl() {
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}
	
	public static TokenFactory getTokenFactory() {
		if (tokenFactory == null) {
			tokenFactory = new TokenFactoryImpl();
		}
		return tokenFactory;
	}

	@Override
	public String issueToken(Employee e) {
		String token = Jwts.builder().setSubject(e.getEmployeeId()).signWith(key).claim("role", e.getRole().toString()).compact();
		return token;
	}
	
	@Override
	public boolean authorize(String token, Employee e) {
		Map<String, String> decoded = decode(token);
		if (!decoded.get("employeeId").equals(e.getEmployeeId()) || !Role.valueOf(decoded.get("role")).equals(e.getRole())) {
       		throw new ForbiddenResponse();
       	}
		return true;
	}

	@Override
	public Map<String, String> decode(String token) {
		if (token == null) {
			throw new UnauthorizedResponse();
		}
		try {
	        String eId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	       	String role = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role", String.class);
	       	Map<String, String> tokenVal = new HashMap<>();
	       	tokenVal.put("employeeId", eId);
	       	tokenVal.put("role", role);
	       	return tokenVal;
	    } catch (Exception ex) {
	        throw new ForbiddenResponse();
	    }
	}

	@Override
	public boolean authorizeAdmin(String token, Employee e) {
		Map<String, String> decoded = decode(token);
		if (!decoded.get("employeeId").equals(e.getEmployeeId()) || !Role.ADMIN.equals(e.getRole())) {
       		throw new ForbiddenResponse();
       	}
		return true;
	}

	
	
}
