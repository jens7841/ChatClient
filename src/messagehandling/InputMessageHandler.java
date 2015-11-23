package messagehandling;

import client.Client;
import surfaces.Surface;

public class InputMessageHandler extends Thread {

	private Client client;
	private Message message;

	public InputMessageHandler(Client client) {
		this.client = client;
	}

	public void handleMessage(Message message) {
		this.message = message;
		this.start();
	}

	@Override
	public void run() {

		Surface surface = client.getSurface();

		switch (message.getType()) {
		case CHAT_MESSAGE:
			surface.outputChatMessaage(message.toString());
			break;
		case ERROR_MESSAGE:
			surface.outputErrorMessage(message.toString());
			break;
		case SUCCESS_MESSAGE:
			surface.outputSuccessMessage(message.toString());
			break;
		case LOGIN_ERROR_MESSAGE:
			if (!client.isLoggedIn()) {
				surface.outputErrorMessage(message.toString());
				client.performLogin();
			}
			break;
		case LOGIN_SUCCESS_MESSAGE:
			if (!client.isLoggedIn()) {
				surface.outputSuccessMessage(message.toString());
				client.login();
			}
			break;
		case LOGIN:
		default:
			break;
		}
	}

}
