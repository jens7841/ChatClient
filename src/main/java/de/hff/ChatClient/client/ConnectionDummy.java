package de.hff.ChatClient.client;

public class ConnectionDummy {

	private int port;
	private String adress;

	public ConnectionDummy(String adress, int port) {
		this.adress = adress;
		this.port = port;
	}

	public String getAdress() {
		return adress;
	}

	public int getPort() {
		return port;
	}

}
