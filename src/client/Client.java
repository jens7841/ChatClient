package client;

import java.io.IOException;
import java.net.Socket;

import chatshared.Messages;
import commands.Upload;
import messagehandling.CommandHandler;
import messagehandling.Message;
import messagehandling.MessageListener;
import messagehandling.MessageSender;
import messagehandling.MessageType;
import surfaces.Surface;

public class Client extends Thread {

	private Object waitForLogin = new Object();
	private Surface surface;
	private CommandHandler commandHandler = new CommandHandler();
	private Socket socket;
	private boolean login;

	public Client(Surface surface) {
		this.surface = surface;
	}

	public void startClient() {
		this.start();
	}

	private void initializeCommands() {
		commandHandler.addCommand(new Upload("upload"));
	}

	public void createSocket() {
		boolean correctInput = false;
		while (!correctInput) {
			try {
				String ip = "";// surface.getInputWithMessage("IP");

				int port = 0;

				while (!correctInput) {
					try {
						port = 12345;// Integer.parseInt(surface.getInputWithMessage("Port"));
						correctInput = true;
					} catch (NumberFormatException e) {
						correctInput = false;
					}
				}

				socket = new Socket(ip, port);
				correctInput = true;
			} catch (IOException | IllegalArgumentException e) {
				surface.outputErrorMessage("Fehler beim Verbindungsaufbau!");
				correctInput = false;
			}
		}
	}

	private static int i = 1;

	public void performLogin() {

		String username = "jens" + i;
		i++;
		while (username.trim().isEmpty())
			username = surface.getInputWithMessage("Username");
		String passwort = "jens";
		while (passwort.trim().isEmpty())
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

	public synchronized Socket getSocket() {
		return socket;
	}

	public Surface getSurface() {
		return surface;
	}

	public void login() {
		synchronized (waitForLogin) {
			waitForLogin.notifyAll();
		}
	}

	public boolean isLoggedIn() {
		return login;
	}

	public void sendMessage(Message message) {
		if (message.getType() == MessageType.CHAT_MESSAGE && message.toString().startsWith("/")) {
			if (commandHandler.handleCommand(this, message.toString().substring(1))) {
				return;
			}
		}
		new MessageSender(socket).sendMessage(message);
	}

	private void startChat() {
		surface.startChatInput(this);
	}

	@Override
	public void run() {
		initializeCommands();
		createSocket();
		new MessageListener(this).start();
		performLogin();
		synchronized (waitForLogin) {
			try {
				waitForLogin.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startChat();
		}
	}

}
