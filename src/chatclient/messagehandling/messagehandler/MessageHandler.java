package chatclient.messagehandling.messagehandler;

import chatclient.client.Service;
import chatclient.messagehandling.Message;
import chatclient.surfaces.SurfaceHandler;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, SurfaceHandler surfaceHandler);
}