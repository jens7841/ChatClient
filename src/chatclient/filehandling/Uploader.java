package chatclient.filehandling;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import chatclient.messagehandling.Message;
import chatclient.messagehandling.MessageType;
import chatclient.messagehandling.messageoutput.MessageSender;

public class Uploader extends Thread {

	private static final int BUFFER_SIZE = 1024 * 1024;
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

		int packages = 0;
		try {
			in = new FileInputStream(file.getFile());

			byte[] buffer = new byte[BUFFER_SIZE];
			double expectedPackages = (double) file.getFile().length() / (double) BUFFER_SIZE;
			System.out.println("Packages Datei: " + expectedPackages);

			do {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				DataOutputStream dataOut = new DataOutputStream(byteOut);
				int readBytes = in.read(buffer);

				dataOut.writeInt(file.getId());
				dataOut.writeInt(readBytes);
				dataOut.write(buffer);

				i += readBytes;

				sender.sendMessage(new Message(byteOut.toByteArray(), MessageType.UPLOAD_PACKAGE));
				packages++;
				if (buffer[buffer.length - 1] < 0)
					System.out.println(buffer[buffer.length - 1]);

			} while (expectedPackages > packages);

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
		System.out.println("Gesendete Packages " + packages);
		System.out.println("Datei wurde hochgeladen!");
		// System.out.println(i);
	}
}