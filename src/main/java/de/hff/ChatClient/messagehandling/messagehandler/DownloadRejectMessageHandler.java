package de.hff.ChatClient.messagehandling.messagehandler;

import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class DownloadRejectMessageHandler implements MessageHandler {

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {
		surfaceHandler.handleMessage(new Message("Download konnte nicht gestartet werden!", MessageType.ERROR));
	}

}
