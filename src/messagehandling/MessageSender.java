package messagehandling;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import chatshared.Messages;

public class MessageSender extends Thread {

	private Socket receiver;
	private Message message;

	public MessageSender(Socket receiver) {
		this.receiver = receiver;
	}

	public void sendMessage(Message message) {
		this.message = message;
		this.start();
	}

	@Override
	public void run() {
		try {
			OutputStream out = new BufferedOutputStream(receiver.getOutputStream());

			out.write(message.getType().getTypeNumber());
			out.write(message.getMessage());
			out.write(Messages.END_OF_MESSAGE);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
