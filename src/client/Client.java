package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;

import commands.Upload;
import messagehandling.CommandHandler;
import messagehandling.InputMessageHandler;
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
	private MessageSender messageSender;

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
				String ip = surface.getInputWithMessage("IP");

				int port = 0;

				while (!correctInput) {
					try {
						port = Integer.parseInt(surface.getInputWithMessage("Port"));
						correctInput = true;
					} catch (NumberFormatException e) {
						correctInput = false;
					}
				}

				setSocket(new Socket(ip, port));
				correctInput = true;

			} catch (IOException | IllegalArgumentException e) {
				surface.outputErrorMessage("Fehler beim Verbindungsaufbau!");
				correctInput = false;
			}
		}
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		new MessageListener(socket, surface, new InputMessageHandler(this)).start();
		messageSender = new MessageSender(socket);
		messageSender.start();
	}

	public void performLogin() {

		String username = "";

		while (username.trim().isEmpty())
			username = surface.getInputWithMessage("Username");
		String passwort = "";
		while (passwort.trim().isEmpty())
			passwort = surface.getInputWithMessage("Passwort");

		login(username, passwort);
	}

	public void login(String username, String password) {
		try {

			byte[] usernameArray = username.getBytes("UTF-8");
			byte[] passwordArray = password.getBytes("UTF-8");

			byte[] message = ByteBuffer.allocate(8 + usernameArray.length + password.length())
					.putInt(usernameArray.length).put(usernameArray).putInt(passwordArray.length).put(passwordArray)
					.array();

			if (messageSender == null)
				throw new IllegalStateException("Socket must be set first!");

			messageSender.sendMessage(new Message(message, MessageType.LOGIN));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public void loginSuccess() {
		synchronized (waitForLogin) {
			waitForLogin.notifyAll();
		}
	}

	public void sendMessage(Message message) {
		if (message.getType() == MessageType.CHAT_MESSAGE && message.toString().startsWith("/")) {
			if (commandHandler.handleCommand(this, message.toString().substring(1))) {
				return;
			}
		}
		messageSender.sendMessage(message);
	}

	private void startChat() {
		surface.startChatInput(this);
	}

	public boolean isLoggedIn() {
		return login;
	}

	public Socket getSocket() {
		return socket;
	}

	public Surface getSurface() {
		return surface;
	}

	@Override
	public void run() {
		initializeCommands();

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
