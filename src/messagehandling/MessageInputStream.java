package messagehandling;

import java.io.IOException;
import java.io.InputStream;

import chatshared.Messages;

public class MessageInputStream extends InputStream {

	private InputStream in;

	public MessageInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	public Message readMessage() throws IOException {

		StringBuilder builder = new StringBuilder();
		MessageType msgType = MessageType.getType(in.read());
		int read;
		while ((read = in.read()) != Messages.END_OF_MESSAGE) {
			builder.append((char) read);
		}

		return new Message(builder.toString(), msgType);
	}

}
