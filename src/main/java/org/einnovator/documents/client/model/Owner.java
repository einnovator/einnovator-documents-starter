package org.einnovator.documents.client.model;

public class Owner {

	private String username;

	private String app;
	
	public Owner() {
	}

	public Owner(String username) {
		this.username = username;
	}
	
	public Owner(String username, String app) {
		this.username = username;
		this.app = app;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@Override
	public String toString() {
		return "Owner [" + (username != null ? "username=" + username + ", " : "") + (app != null ? "app=" + app : "")
				+ "]";
	}

	
	
}
