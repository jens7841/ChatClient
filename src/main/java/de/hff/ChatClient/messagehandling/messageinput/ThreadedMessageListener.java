package de.hff.ChatClient.messagehandling.messageinput;

import java.io.IOException;
import java.io.InputStream;

import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.messagehandling.MessageHandlerFactory;
import de.hff.ChatClient.messagehandling.messagehandler.MessageHandler;
import de.hff.ChatClient.surfaces.SurfaceHandler;

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