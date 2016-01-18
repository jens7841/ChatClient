package filehandling;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageType;
import messagehandling.messageoutput.MessageSender;

public class Uploader extends Thread {

	private static final int BUFFER_SIZE = 8192;
	private UploadFile file;
	private MessageSender sender;

	public Uploader(UploadFile file, MessageSender sender) {
		this.file = file;
		this.sender = sender;
	}

	private static int i = 0;

	@Override
	public void run() {
		FileInputStream in = null;

		try {
			in = new FileInputStream(file.getFile());

			byte[] buffer = new byte[BUFFER_SIZE];

			do {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				DataOutputStream dataOut = new DataOutputStream(byteOut);
				int readBytes = in.read(buffer);

				dataOut.writeInt(file.getId());
				dataOut.writeInt(readBytes);
				dataOut.write(buffer);

				i += readBytes;

				sender.sendMessage(new Message(byteOut.toByteArray(), MessageType.UPLOAD_PACKAGE));

				System.out.println(buffer[buffer.length - 1]);

			} while (buffer[buffer.length - 1] != -1);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(i);
	}
}