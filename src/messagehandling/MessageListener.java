package messagehandling;

import java.io.BufferedInputStream;
import java.io.IOException;

import client.Client;

public class MessageListener extends Thread {

	private Client client;

	public MessageListener(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		while (!client.getSocket().isClosed()) {
			MessageInputStream in = null;
			try {
				in = new MessageInputStream(new BufferedInputStream(client.getSocket().getInputStream()));

				Message msg = in.readMessage();

				new InputMessageHandler(client).handleMessage(msg);

			} catch (IOException e) {
				try {
					client.getSocket().close();
					if (in != null) {
						in.close();
					}
				} catch (IOException e1) {
				}
				client.getSurface().outputErrorMessage("Verbindung zum Server verloren!");
			}
		}
	}
}
