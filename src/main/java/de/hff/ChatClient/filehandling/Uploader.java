package de.hff.ChatClient.filehandling;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hff.ChatClient.client.ServiceRegistry;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.messagehandling.MessageType;
import de.hff.ChatClient.messagehandling.messageoutput.MessageSender;
import de.hff.ChatClient.messagehandling.messageoutput.TimedOutputStream;
import de.hff.ChatClient.stats.SpeedDiagram;
import de.hff.ChatClient.stats.SpeedDiegramPainter;

public class Uploader extends Thread {

	private static final int BUFFER_SIZE = 1024 * 1024;
	private UploadFile file;
	private MessageSender sender;
	private TimedOutputStream timedOutputStream;

	public Uploader(UploadFile file, MessageSender sender, ServiceRegistry registry) {
		this.file = file;
		this.sender = sender;
		this.timedOutputStream = (TimedOutputStream) registry.getService(ServiceRegistry.TIMED_MESSAGE_OUTPUT_STREAM);
	}

	@Override
	public void run() {

		SpeedDiagram speedDiagram = new SpeedDiagram();
		SpeedDiegramPainter speedDiegramPainter = new SpeedDiegramPainter(speedDiagram, timedOutputStream);
		speedDiegramPainter.start();

		FileInputStream in = null;

		int packages = 0;
		try {
			in = new FileInputStream(file.getFile());

			byte[] buffer = new byte[BUFFER_SIZE];
			double expectedPackages = (double) file.getFile().length() / (double) BUFFER_SIZE;

			do {
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				DataOutputStream dataOut = new DataOutputStream(byteOut);
				int readBytes = in.read(buffer);

				dataOut.writeInt(file.getId());
				dataOut.writeInt(readBytes);
				dataOut.write(buffer);

				sender.sendMessage(new Message(byteOut.toByteArray(), MessageType.UPLOAD_PACKAGE));
				packages++;

			} while (expectedPackages > packages);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			speedDiegramPainter.stopPainting();

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
	}
}