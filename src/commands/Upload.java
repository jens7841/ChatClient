package commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import chatshared.Messages;
import client.Client;
import messagehandling.Message;
import messagehandling.MessageSender;
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

			String message2 = file.getName() + (char) ((byte) Messages.DELIMITER) + String.valueOf(file.length())
					+ (char) ((byte) Messages.DELIMITER);

			Message message = new Message(message2, MessageType.FILE);

			new MessageSender(client.getSocket()).sendMessage(message, new FileInputStream(file));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("gesendet?");
	}

}