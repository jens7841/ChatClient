package de.hff.ChatClient.surfaces.console;

import de.hff.ChatClient.client.ConnectionDummy;
import de.hff.ChatClient.client.UserDummy;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;

public class ConsoleSurfaceHandler implements SurfaceHandler {

	private ConsoleSurface surface;

	public ConsoleSurfaceHandler(ConsoleSurface surface) {
		this.surface = surface;
	}

	@Override
	public Message getMessage() {
		return surface.getMessage();
	}

	@Override
	public void handleMessage(Message message) {

		switch (message.getType()) {
		case CHAT_MESSAGE:
			surface.consoleOutput(message.toString());
			break;
		case ERROR:
			surface.consoleErrorOutput(message.toString());
			break;
		case LOGIN_ERROR:
			surface.consoleErrorOutput(message.toString());
			break;
		case LOGIN_SUCCESS:
			surface.consoleOutput(message.toString());
			surface.start();
			break;
		case SUCCESS:
			surface.consoleOutput(message.toString());
			break;
		case UPLOAD_CONFIRMATION:
			surface.consoleOutput(message.toString());
			break;
		case UPLOAD_REJECT:
			surface.consoleErrorOutput(message.toString());
			break;
		default:
			break;

		}

	}

	@Override
	public ConnectionDummy getConnection() {
		ConnectionDummy connection = null;
		do {
			surface.consoleOutput("Adresse:");
			String ip = surface.getConsoleInput();
			int port = -1;

			surface.consoleOutput("Port: ");
			try {
				port = Integer.parseInt(surface.getConsoleInput());

			} catch (NumberFormatException e) {
			}
			connection = new ConnectionDummy(ip, port);
		} while (connection == null);
		return connection;
	}

	@Override
	public UserDummy getLogin() {
		UserDummy user = null;
		String password = null;
		do {
			surface.consoleOutput("Username:");
			String username = surface.getConsoleInput();

			surface.consoleOutput("Password: ");
			password = surface.getConsoleInput();

			user = new UserDummy(username, password);
		} while (user == null || password == null);

		return user;
	}

}