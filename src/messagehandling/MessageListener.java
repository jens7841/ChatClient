package messagehandling;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.SocketException;

import client.Client;

public class MessageListener extends Thread {

	private Client client;

	public MessageListener(Client client) {
		this.client = client;
	}

	@Override
	public void run() {
		MessageInputStream in = null;
		while (!client.getSocket().isClosed()) {
			try {
				in = new MessageInputStream(new BufferedInputStream(client.getSocket().getInputStream()));

				Message msg = in.readMessage();

				new InputMessageHandler(client).handleMessage(msg);

			} catch (SocketException e) {
				if (!e.getMessage().equalsIgnoreCase("socket closed"))
					client.getSurface().outputErrorMessage("Verbindung zum Server verloren!");
				break;
			} catch (IOException e) {
				break;
			}
		}

		try {
			client.getSocket().close();
			if (in != null) {
				in.close();
			}
		} catch (IOException e1) {
		}

	}
}