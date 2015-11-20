package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import chatshared.Messages;
import messagehandling.Message;
import messagehandling.MessageListener;
import messagehandling.MessageSender;
import messagehandling.MessageType;
import surfaces.Surface;

public class Client extends Thread {

	private Surface surface;
	private Socket socket;
	private boolean login;

	public Client(Surface surface) {
		this.surface = surface;
	}

	public void startClient() {
		this.start();
	}

	private void createSocket() throws UnknownHostException, IOException {
		String ip = surface.getInputWithMessage("IP");

		boolean correctInput = false;
		int port = 0;

		while (!correctInput) {
			try {
				port = Integer.parseInt(surface.getInputWithMessage("Port"));
				correctInput = true;
			} catch (NumberFormatException e) {
			}
		}

		socket = new Socket(ip, port);

	}

	public void performLogin() {

		String username = "";
		while (username.isEmpty())
			username = surface.getInputWithMessage("Username");
		String passwort = "";
		while (passwort.isEmpty())
			passwort = surface.getInputWithMessage("Passwort");

		byte[] message = new byte[username.length() + passwort.length() + 1];

		for (int i = 0; i < username.length(); i++) {
			message[i] = (byte) username.charAt(i);
		}
		message[username.length()] = (byte) Messages.DELIMITER;
		for (int i = 0; i < passwort.length(); i++) {
			message[username.length() + 1 + i] = (byte) passwort.charAt(i);
		}

		new MessageSender(socket).sendMessage(new Message(message, MessageType.LOGIN));

	}

	public Socket getSocket() {
		return socket;
	}

	public Surface getSurface() {
		return surface;
	}

	public void setLoggedIn(boolean b) {
		this.login = b;
	}

	public boolean isLoggedIn() {
		return login;
	}

	public void activateChat() {

		new Thread() {
			@Override
			public void run() {
				while (!socket.isClosed()) {
					String msg = surface.getDefaultChatInput();
					new MessageSender(socket).sendMessage(new Message(msg, MessageType.CHAT_MESSAGE));
				}
			}
		}.start();

	}

	@Override
	public void run() {
		try {
			createSocket();
			new MessageListener(this).start();
			performLogin();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
