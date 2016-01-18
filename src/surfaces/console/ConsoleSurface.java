package surfaces.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import messagehandling.Message;
import messagehandling.MessageType;
import surfaces.Surface;

public class ConsoleSurface extends Thread implements Surface {

	private BufferedReader bufferedReader;
	private BlockingQueue<Message> messages;

	public ConsoleSurface() {
		this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		this.messages = new LinkedBlockingQueue<>();
	}

	public void consoleOutput(String message) {
		System.out.println(message);
	}

	public void consoleErrorOutput(String message) {
		System.err.println(message);
	}

	public String getConsoleInput() {
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {

		while (true) {
			String input = getConsoleInput().trim();

			try {
				if (input.startsWith("/")) {

					Message message = new Message(input.substring(1).getBytes("UTF-8"), MessageType.COMMAND);
					messages.put(message);

				} else if (!input.isEmpty()) {

					Message message = new Message(input.getBytes("UTF-8"), MessageType.CHAT_MESSAGE);
					messages.put(message);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}

	}

	public Message getMessage() {
		try {
			return messages.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

}