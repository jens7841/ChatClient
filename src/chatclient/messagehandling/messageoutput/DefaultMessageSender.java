package chatclient.messagehandling.messageoutput;

import java.io.IOException;
import java.io.OutputStream;

import chatclient.messagehandling.Message;

public class DefaultMessageSender implements MessageSender {

	private TimedOutputStream out;

	public DefaultMessageSender(OutputStream out) {
		this.out = new TimedOutputStream(out);
	}

	public DefaultMessageSender() {
	}

	@Override
	public void sendMessage(Message message) {
		try {
			MessageOutputstream output = new MessageOutputstream(out);
			System.out.println("Sende: " + message.getType());
			output.writeMessage(message);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace(); // TODO Throw?
		}

	}

	@Override
	public void setOutputStream(OutputStream out) {
		this.out = new TimedOutputStream(out);
	}

}