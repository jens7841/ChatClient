package messagehandling.messagehandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import filehandling.FileManager;
import filehandling.UploadFile;
import filehandling.Uploader;
import messagehandling.Message;
import messagehandling.MessageType;
import messagehandling.messageoutput.MessageSender;
import surfaces.SurfaceHandler;

public class UploadConfirmationHandler implements MessageHandler {

	private MessageSender sender;
	private FileManager fileManager;
	private SurfaceHandler surfaceHandler;

	public UploadConfirmationHandler(SurfaceHandler surfaceHandler, FileManager fileManager, MessageSender sender) {
		this.surfaceHandler = surfaceHandler;
		this.fileManager = fileManager;
		this.sender = sender;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));

		try {
			int fileNameLength = in.readInt();
			byte[] fileNameBytes = new byte[fileNameLength];
			in.readFully(fileNameBytes);

			long fileSize = in.readLong();

			String fileName = new String(fileNameBytes, "UTF-8");
			UploadFile file = fileManager.getFile(fileSize, fileName);

			if (file != null) {
				int id = in.readInt();
				file.setId(id);

				new Uploader(file, sender).start();
				this.surfaceHandler
						.handleMessage(new Message("Upload wurde gestartet! (" + fileName + ")", MessageType.SUCCESS));

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
