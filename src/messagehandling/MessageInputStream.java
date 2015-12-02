package messagehandling;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

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

		DataInputStream dataIn = new DataInputStream(this);

		MessageType msgType = MessageType.getType(in.read());
		int length = dataIn.readInt();

		byte[] message = new byte[length];

		dataIn.readFully(message);

		return new Message(message, msgType);
	}

}
