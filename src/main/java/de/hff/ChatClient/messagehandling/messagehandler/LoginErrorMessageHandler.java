package de.hff.ChatClient.messagehandling.messagehandler;

import java.util.concurrent.Semaphore;

import de.hff.ChatClient.client.Service;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;

public class LoginErrorMessageHandler implements MessageHandler, Service {

	private Semaphore lock;

	public LoginErrorMessageHandler(Semaphore lock) {
		this.lock = lock;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {
		surfaceHandler.handleMessage(message);
		lock.release();
	}

}
