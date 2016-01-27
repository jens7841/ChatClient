package de.hff.ChatClient.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import de.hff.ChatClient.client.ServiceRegistry;
import de.hff.ChatClient.filehandling.FileManager;
import de.hff.ChatClient.filehandling.TransferFile;
import de.hff.ChatClient.filehandling.Uploader;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.messagehandling.messageoutput.MessageSender;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class UploadConfirmationHandler implements MessageHandler {

	private MessageSender sender;
	private FileManager fileManager;
	private SurfaceHandler surfaceHandler;
	private ServiceRegistry registry;

	public UploadConfirmationHandler(SurfaceHandler surfaceHandler, FileManager fileManager, MessageSender sender,
			ServiceRegistry registry) {
		this.surfaceHandler = surfaceHandler;
		this.fileManager = fileManager;
		this.sender = sender;
		this.registry = registry;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));

		try {
			int fileNameLength = in.readInt();
			byte[] fileNameBytes = new byte[fileNameLength];
			in.readFully(fileNameBytes);

			long fileSize = in.readLong();

			String fileName = new String(fileNameBytes, "UTF-8");
			TransferFile file = fileManager.getFile(fileSize, fileName);
			fileManager.removeFile(fileSize, fileName);

			if (file != null) {
				int id = in.readInt();
				file.setId(id);

				new Uploader(file, sender, registry).start();
				this.surfaceHandler
						.handleMessage(new Message("Upload wurde gestartet! (" + fileName + ")", MessageType.SUCCESS));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
