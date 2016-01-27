package de.hff.ChatClient.messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import de.hff.ChatClient.filehandling.FileManager;
import de.hff.ChatClient.filehandling.TransferFile;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class DownloadConfirmationMessageHandler implements MessageHandler {

	private FileManager downloadFileHandler;

	public DownloadConfirmationMessageHandler(FileManager downloadFileHandler) {
		this.downloadFileHandler = downloadFileHandler;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));
		try {

			int fileNameLenght = in.readInt();

			byte[] fileNameBytes = new byte[fileNameLenght];

			in.readFully(fileNameBytes);

			String fileName = new String(fileNameBytes, "UTF-8");

			long fileSize = in.readLong();

			int fileID = in.readInt();

			File file = new File(fileName);

			if (file.exists()) {
				fileName = "" + fileName;
			}

			TransferFile transferFile = new TransferFile(getCorrectFile(file));
			transferFile.setId(fileID);
			transferFile.setExpectedSize(fileSize);

			downloadFileHandler.addDownloadingFile(transferFile);

			surfaceHandler.handleMessage(new Message(
					"Der Download der Datei " + fileName + " (ID: " + fileID + ") hat begonnen", MessageType.SUCCESS));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static File getCorrectFile(File f) {

		String fileName = f.getName();
		int endIndex = f.getAbsolutePath().length() - fileName.length();
		String path = f.getAbsolutePath().substring(0, endIndex);

		int number = 1;

		while (f.exists()) {
			f = new File(path + number + "-" + fileName);
			number++;
		}

		return f;
	}

}
