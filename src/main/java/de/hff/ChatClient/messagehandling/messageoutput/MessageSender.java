package de.hff.ChatClient.messagehandling.messageoutput;

import java.io.OutputStream;

import de.hff.ChatClient.messagehandling.Message;

public interface MessageSender {

	public void sendMessage(Message message);

	public void setOutputStream(OutputStream out);

}