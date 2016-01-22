package de.hff.ChatClient.surfaces;

import de.hff.ChatClient.client.ConnectionDummy;
import de.hff.ChatClient.client.UserDummy;
import de.hff.ChatClient.messagehandling.Message;

public interface SurfaceHandler {

	public void handleMessage(Message message);

	public Message getMessage();

	public ConnectionDummy getConnection();

	public UserDummy getLogin();

}