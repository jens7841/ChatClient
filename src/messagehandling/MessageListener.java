package messagehandling;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import surfaces.Surface;

public class MessageListener extends Thread {

	private Socket socket;
	private Surface surface;
	private InputMessageHandler inputMessageHandler;

	public MessageListener(Socket socket, Surface surface, InputMessageHandler inputMessageHandler) {
		this.socket = socket;
		this.surface = surface;
		this.inputMessageHandler = inputMessageHandler;
	}

	@Override
	public void run() {
		MessageInputStream in = null;
		while (!socket.isClosed()) {
			try {
				in = new MessageInputStream(new BufferedInputStream(socket.getInputStream()));

				Message msg = in.readMessage();

				inputMessageHandler.handleMessage(msg);

			} catch (SocketException e) {
				if (!e.getMessage().equalsIgnoreCase("socket closed"))
					surface.outputErrorMessage("Verbindung zum Server verloren!");
				break;
			} catch (IOException e) {
				break;
			}
		}

		try {
			socket.close();
			if (in != null) {
				in.close();
			}
		} catch (IOException e1) {
		}

	}
}