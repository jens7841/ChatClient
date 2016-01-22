package de.hff.ChatClient.messagehandling.messageoutput;

import java.io.IOException;
import java.io.OutputStream;

import de.hff.ChatClient.messagehandling.Message;

public class DefaultMessageSender implements MessageSender {

	private OutputStream out;

	public DefaultMessageSender(OutputStream out) {
		this.out = out;
	}

	public DefaultMessageSender() {
	}

	@Override
	public void sendMessage(Message message) {
		try {
			MessageOutputstream output = new MessageOutputstream(out);
			output.writeMessage(message);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace(); // TODO Throw?
		}

	}

	@Override
	public void setOutputStream(OutputStream out) {
		this.out = out;
	}

}