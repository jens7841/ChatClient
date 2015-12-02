package commands;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import client.Client;
import messagehandling.Message;
import messagehandling.MessageType;

public class Upload extends Command {

	public Upload(String command, String... aliases) {
		super(command, aliases);
	}

	@Override
	public void execute(Client client, String input) {

		input = input.substring(input.indexOf(" ") + 1);

		File file = new File(input);

		if (!file.exists()) {
			client.getSurface().outputErrorMessage("Datei konnte nicht gefunden werden!");
			return;
		}
		if (file.isDirectory()) {
			client.getSurface().outputErrorMessage("Die Datei draf kein Verzeichniss sein!");
			return;
		}

		try {

			byte[] fileNameBytes = file.getName().getBytes("UTF-8");
			byte[] message = ByteBuffer.allocate(12 + fileNameBytes.length).putInt(fileNameBytes.length)
					.put(fileNameBytes).putLong(file.length()).array();

			client.getFileManager().addFile(file);

			client.getMessageSender().sendMessage(new Message(message, MessageType.UPLOAD_REQUEST));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}