package de.hff.ChatClient.client;

public class UserDummy {

	private String password;
	private String username;

	public UserDummy(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}
}
