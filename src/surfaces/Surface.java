package surfaces;

import client.Client;

public abstract class Surface extends Thread {

	protected boolean chatHasStarted;
	protected Client client;

	public abstract void startChatInput(Client client);

	public abstract void outputChatMessaage(String message);

	public abstract void outputErrorMessage(String message);

	public abstract String getDefaultChatInput();

	public abstract String getInputWithMessage(String message);

	public abstract void outputSuccessMessage(String string);
}
