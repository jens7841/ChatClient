package messagehandling.messagehandler;

import client.Service;
import messagehandling.Message;
import surfaces.SurfaceHandler;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, SurfaceHandler surfaceHandler);
}