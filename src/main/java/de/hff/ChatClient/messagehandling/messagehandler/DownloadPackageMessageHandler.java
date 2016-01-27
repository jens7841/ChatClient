package de.hff.ChatClient.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatClient.filehandling.FileManager;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;

public class DownloadPackageMessageHandler implements MessageHandler {

	private FileManager downloadFileManager;

	public DownloadPackageMessageHandler(FileManager downloadFileManager) {
		this.downloadFileManager = downloadFileManager;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {
		try {

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));

			int id = in.readInt();
			int packageSize = in.readInt();

			byte[] data = new byte[packageSize];

			in.readFully(data);

			downloadFileManager.savePackage(id, data);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
