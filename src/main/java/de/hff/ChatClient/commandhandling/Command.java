package de.hff.ChatClient.commandhandling;

import java.util.Arrays;

import de.hff.ChatClient.surfaces.SurfaceHandler;

public abstract class Command {
	protected String command;
	protected String[] aliases;
	protected SurfaceHandler surfaceHandler;

	public Command(String command, SurfaceHandler handler, String... aliases) {
		this.command = command;
		this.surfaceHandler = handler;
		this.aliases = aliases;
	}

	public boolean contains(String command) {
		if (command.equalsIgnoreCase(this.command)) {
			return true;
		}
		for (String alias : aliases) {
			if (command.equalsIgnoreCase(alias)) {
				return true;
			}
		}
		return false;
	}

	public abstract void execute(String msg);

	@Override
	public String toString() {
		return "Command [command=" + command + ", aliases=" + Arrays.toString(aliases) + "]";
	}

}
