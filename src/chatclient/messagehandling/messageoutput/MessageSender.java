package chatclient.messagehandling.messageoutput;

import java.io.OutputStream;

import chatclient.messagehandling.Message;

public interface MessageSender {

	public void sendMessage(Message message);

	public void setOutputStream(OutputStream out);

}
