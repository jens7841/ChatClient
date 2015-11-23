package surfaces;

import client.Client;

public abstract class Surface {

	public abstract void startChatInput(Client client);

	public abstract void outputChatMessaage(String message);

	public abstract void outputErrorMessage(String message);

	public abstract String getDefaultChatInput();

	public abstract String getInputWithMessage(String message);

	public abstract void outputSuccessMessage(String string);
}
