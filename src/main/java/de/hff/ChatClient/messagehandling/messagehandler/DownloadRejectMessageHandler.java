package de.hff.ChatClient.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class DownloadRejectMessageHandler implements MessageHandler {

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {

		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
			int id = in.readInt();

			surfaceHandler.handleMessage(
					new Message("Download (ID:" + id + " konnte nicht gestartet werden!", MessageType.ERROR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
