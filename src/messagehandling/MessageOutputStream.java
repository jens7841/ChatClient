package messagehandling;

import java.io.IOException;
import java.io.OutputStream;

import chatshared.Messages;

public class MessageOutputStream extends OutputStream {

	private OutputStream out;

	public MessageOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	public void writeMessage(Message message) throws IOException {
		out.write(message.getType().getTypeNumber());
		out.write(message.getMessage());
		out.write(Messages.END_OF_MESSAGE);
	}

}
