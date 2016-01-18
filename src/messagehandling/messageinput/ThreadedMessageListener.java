package messagehandling.messageinput;

import java.io.IOException;
import java.io.InputStream;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import messagehandling.messagehandler.MessageHandler;
import surfaces.SurfaceHandler;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private MessageInputStream in;
	private MessageHandlerFactory messageHandlerFactory;
	private SurfaceHandler surfaceHandler;

	public ThreadedMessageListener(MessageHandlerFactory messageHandlerFactory, InputStream in,
			SurfaceHandler surfaceHandler) {
		this.in = new MessageInputStream(in);
		this.messageHandlerFactory = messageHandlerFactory;
		this.surfaceHandler = surfaceHandler;
	}

	@Override
	public void run() {

		try {
			while (true) {
				Message message = in.readMessage();
				MessageHandler messageHandler = messageHandlerFactory.getMessageHandler(message.getType());
				if (messageHandler == null) {
					surfaceHandler.handleMessage(message);
				} else {
					messageHandler.handleMessage(message, surfaceHandler);
				}

			}
		} catch (IOException e) {
			System.out.println("DISCONNECT B�M!");
			e.printStackTrace();
		}

	}

}