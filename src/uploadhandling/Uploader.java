package uploadhandling;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import messagehandling.Message;
import messagehandling.MessageSender;
import messagehandling.MessageType;

public class Uploader extends Thread {

	private UploadFile uploadFile;
	private MessageSender sender;

	public Uploader(UploadFile uploadFile, MessageSender sender) {
		this.sender = sender;
		this.uploadFile = uploadFile;
	}

	private static int counter;

	@Override
	public void run() {
		InputStream in = null;
		try {
			int bufferSize = 256;
			int dataLength = 0;

			in = new FileInputStream(uploadFile.getFile());
			do {

				byte[] fileBytes = new byte[bufferSize];
				dataLength = in.read(fileBytes);

				ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
				DataOutputStream output = new DataOutputStream(byteOutput);

				output.writeInt(uploadFile.getId()); // file-id
				output.writeInt(dataLength); //
				output.write(fileBytes, 0, dataLength); //
				output.close();

				byte[] byteArray = byteOutput.toByteArray();

				// ByteBuffer buffer = ByteBuffer.allocate(8 + dataLength);
				// buffer.putInt(uploadFile.getId()); // file-id
				// buffer.putInt(dataLength); // length
				// buffer.put(fileBytes, 0, dataLength); // bytes
				//
				// byte[] message = buffer.array();
				System.out.println("count: " + (++counter));
				sender.sendMessage(new Message(byteArray, MessageType.UPLOAD_PACKAGE));

				System.out.println(bufferSize + "  " + dataLength);
			} while (dataLength == bufferSize);
			System.out.println("end" + in.read());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

}
