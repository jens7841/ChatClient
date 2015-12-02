package messagehandling;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class MessageSender extends Thread {

	private Socket receiver;
	private List<Message> messages;
	private Semaphore lock;

	public MessageSender(Socket receiver) {
		this.messages = new ArrayList<>();
		this.lock = new Semaphore(1);
		this.receiver = receiver;
	}

	public void sendMessage(Message message) {
		messages.add(message);
		lock.release();
	}

	@Override
	public void run() {
		try {
			lock.acquire();

			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(receiver.getOutputStream()));

			while (!receiver.isClosed()) {
				while (messages.size() > 0) {
					Message message = messages.get(0);

					messages.remove(0);

					out.write(message.getType().getTypeNumber());
					out.writeInt(message.getMessage().length);
					out.write(message.getMessage());

					out.flush();
				}
				lock.acquire();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
