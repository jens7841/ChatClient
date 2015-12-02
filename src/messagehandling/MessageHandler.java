package messagehandling;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import client.Client;
import surfaces.Surface;
import uploadhandling.FileManager;
import uploadhandling.UploadFile;

public class MessageHandler {

	private Client client;

	public MessageHandler(Client client) {
		this.client = client;
	}

	public void handleMessage(Message message) {

		Surface surface = client.getSurface();

		if (message == null)
			return;

		switch (message.getType()) {
		case CHAT_MESSAGE:
			surface.outputChatMessaage(message.toString());
			break;
		case ERROR_MESSAGE:
			surface.outputErrorMessage(message.toString());
			break;
		case SUCCESS_MESSAGE:
			surface.outputSuccessMessage(message.toString());
			break;
		case LOGIN_ERROR_MESSAGE:
			if (!client.isLoggedIn()) {
				surface.outputErrorMessage(message.toString());
				client.performLogin();
			}
			break;
		case LOGIN_SUCCESS_MESSAGE:
			if (!client.isLoggedIn()) {
				surface.outputSuccessMessage(message.toString());
				client.loginSuccess();
			}
			break;
		case DISCONNECT:
			surface.outputSuccessMessage("Verbindung zum Server verloren: " + message.toString());
			try {
				client.getSocket().close();
			} catch (IOException e) {
			}
			new Client(client.getSurface()).startClient();
			break;
		case UPLOAD_CONFIRMATION:
			uploadConfirmation(message);
			break;
		case UPLOAD_REJECT:

			break;
		default:
			break;
		}

	}

	private void uploadConfirmation(Message message) {

		try {
			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new ByteArrayInputStream(message.getMessage())));

			byte[] fileNameBytes = new byte[in.readInt()];
			in.readFully(fileNameBytes);
			String fileName = new String(fileNameBytes, "UTF-8");

			long fileSize = in.readLong();
			int id = in.readInt();

			FileManager fileManager = client.getFileManager();
			UploadFile file = fileManager.getFileWaintingForConfirmation(fileName, fileSize);

			if (file != null) {
				file.startUpload(client.getMessageSender(), id);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
