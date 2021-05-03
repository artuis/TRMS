package com.revature.beans;

public class AuthResponse {
	
	private String userId;
    private String token;
    private String role;

    public AuthResponse(){

    }
    
    public AuthResponse(String token, String userId, String role) {
        this.userId = userId;
        this.token = token;
        this.role = role;
    }
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
