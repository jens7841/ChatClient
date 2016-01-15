package surfaces;

import client.ConnectionDummy;
import client.UserDummy;
import messagehandling.Message;

public interface SurfaceHandler {

	public void handleMessage(Message message);

	public Message getMessage();

	public ConnectionDummy getConnection();

	public UserDummy getLogin();

}