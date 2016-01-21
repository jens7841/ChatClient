package chatclient.surfaces;

import chatclient.client.ConnectionDummy;
import chatclient.client.UserDummy;
import chatclient.messagehandling.Message;

public interface SurfaceHandler {

	public void handleMessage(Message message);

	public Message getMessage();

	public ConnectionDummy getConnection();

	public UserDummy getLogin();

}