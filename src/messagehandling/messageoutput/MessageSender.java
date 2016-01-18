package messagehandling.messageoutput;

import java.io.OutputStream;

import messagehandling.Message;

public interface MessageSender {

	public void sendMessage(Message message);

	public void setOutputStream(OutputStream out);

}
