package surfaces;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import client.Client;
import messagehandling.Message;
import messagehandling.MessageType;

public class CmdSurface extends Surface {

	@Override
	public void outputChatMessaage(String message) {
		System.out.println(message);
	}

	@Override
	public void outputErrorMessage(String message) {
		System.err.println("Error: " + message);
	}

	@Override
	public String getDefaultChatInput() {
		System.out.print("> ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(System.in)));
		String input = "";
		try {
			input = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return input;
	}

	@Override
	public String getInputWithMessage(String message) {

		System.out.print(message + ": ");

		return getDefaultChatInput();
	}

	@Override
	public void outputSuccessMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void startChatInput(Client client) {
		if (!chatHasStarted) {
			this.client = client;
			start();
		}
	}

	@Override
	public void run() {
		chatHasStarted = true;
		while (!client.getSocket().isClosed()) {
			String input = getDefaultChatInput();
			input = input.trim();
			if (client.getSocket().isClosed()) {
				outputErrorMessage("Verbindung zum Server verloren!");
				chatHasStarted = false;
				return;
			}
			if (!input.isEmpty()) {
				try {
					client.sendMessage(new Message(input.getBytes("UTF-8"), MessageType.CHAT_MESSAGE));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
