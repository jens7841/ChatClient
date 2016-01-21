package chatclient.commandhandling.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import chatclient.commandhandling.Command;
import chatclient.filehandling.FileManager;
import chatclient.filehandling.UploadFile;
import chatclient.messagehandling.Message;
import chatclient.messagehandling.MessageType;
import chatclient.messagehandling.messageoutput.MessageSender;
import chatclient.surfaces.SurfaceHandler;

public class Upload extends Command {

	private SurfaceHandler surfaceHandler;
	private MessageSender messageSender;
	private FileManager fileManager;

	public Upload(String command, String[] aliases, SurfaceHandler surfaceHandler, MessageSender sender,
			FileManager fileManager) {
		super(command, aliases);
		this.surfaceHandler = surfaceHandler;
		this.messageSender = sender;
		this.fileManager = fileManager;
	}

	@Override
	public void execute(String msg) {

		if (msg.isEmpty()) {
			surfaceHandler.handleMessage(new Message("/upload <File>", MessageType.ERROR));
			return;
		}

		String fileName = msg.substring(msg.indexOf(" ") + 1);

		File file = new File(fileName);

		if (!file.exists()) {
			surfaceHandler.handleMessage(new Message("Datei konnte nicht gefunden werden!", MessageType.ERROR));
		} else if (file.isDirectory()) {
			surfaceHandler.handleMessage(new Message("Die Datei darf kein Verzeichnis sein!", MessageType.ERROR));
		} else {

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteArrayOutputStream);

			try {
				dataOut.writeInt(file.getName().length());
				dataOut.write(file.getName().getBytes("UTF-8"));
				dataOut.writeLong(file.length());
			} catch (IOException e) {
				e.printStackTrace();
			}

			fileManager.addFile(new UploadFile(file));

			messageSender.sendMessage(new Message(byteArrayOutputStream.toByteArray(), MessageType.UPLOAD_REQUEST));

		}
	}

}