package messagehandling;

import java.util.ArrayList;
import java.util.List;

import client.Client;
import commands.Command;

public class CommandHandler {

	private List<Command> commands = new ArrayList<>();

	public void addCommand(Command command) {
		commands.add(command);
	}

	public boolean handleCommand(Client client, String input) {
		String commandString = input.split("\\ ")[0];
		for (Command command : commands) {
			if (command.contains(commandString)) {
				command.execute(client, input);
				return true;
			}
		}
		return false;
	}

}