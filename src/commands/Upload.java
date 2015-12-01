package commands;

import java.io.File;

import client.Client;

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

	}

}