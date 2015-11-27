package messagehandling;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import chatshared.Messages;

public class MessageSender extends Thread {

	private Socket receiver;
	private Message message;
	private InputStream inStream;

	public MessageSender(Socket receiver) {
		this.receiver = receiver;
	}

	public void sendMessage(Message message, InputStream inStream) {
		this.inStream = inStream;
		this.message = message;
		this.start();
	}

	public void sendMessage(Message message) {
		this.message = message;
		this.inStream = null;
		this.start();
	}

	@Override
	public void run() {
		try {
			synchronized (receiver.getOutputStream()) {
				OutputStream out = new BufferedOutputStream(receiver.getOutputStream());

				out.write(message.getType().getTypeNumber());
				out.write(message.getMessage());

				if (inStream != null) {
					inStream = new BufferedInputStream(inStream);
					int read;
					while ((read = inStream.read()) != -1) {
						out.write(read);
					}
				}
				out.write(Messages.END_OF_MESSAGE);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
