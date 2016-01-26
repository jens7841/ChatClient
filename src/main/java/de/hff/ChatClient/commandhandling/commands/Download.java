package de.hff.ChatClient.commandhandling.commands;

import de.hff.ChatClient.commandhandling.Command;
import de.hff.ChatClient.filehandling.FileManager;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.messagehandling.messageoutput.MessageSender;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class Download extends Command {

	private MessageSender messageSender;
	private FileManager fileManager;

	public Download(String command, String[] aliases, SurfaceHandler surfaceHandler, MessageSender sender,
			FileManager fileManager) {
		super(command, surfaceHandler, aliases);
		this.messageSender = sender;
		this.fileManager = fileManager;
	}

	@Override
	public void execute(String msg) {

		if (msg.isEmpty()) {
			surfaceHandler.handleMessage(new Message("/download <File-ID>", MessageType.ERROR));
			return;
		}

		boolean correctInput = false;

		int fileId = 0;
		do {
			try {
				fileId = Integer.parseInt(msg);
				correctInput = true;
			} catch (NumberFormatException e) {
			}
		} while (!correctInput);

		byte[] message = java.nio.ByteBuffer.allocate(4).putInt(fileId).array();

		messageSender.sendMessage(new Message(message, MessageType.DOWNLOAD_REQUEST));

	}

}