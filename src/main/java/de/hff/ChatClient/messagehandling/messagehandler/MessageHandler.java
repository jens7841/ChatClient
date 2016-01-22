package de.hff.ChatClient.messagehandling.messagehandler;

import de.hff.ChatClient.client.Service;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, SurfaceHandler surfaceHandler);
}