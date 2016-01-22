package de.hff.ChatClient.messagehandling.messagehandler;

import java.util.concurrent.Semaphore;

import de.hff.ChatClient.client.Client;
import de.hff.ChatClient.client.Service;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.surfaces.SurfaceHandler;

public class LoginSuccessMessageHandler implements MessageHandler, Service {

	private Semaphore lock;
	private Client client;

	public LoginSuccessMessageHandler(Semaphore lock, Client client) {
		this.lock = lock;
		this.client = client;
	}

	@Override
	public void handleMessage(Message message, SurfaceHandler surfaceHandler) {
		surfaceHandler.handleMessage(message);
		client.setLoggedIn(true);
		lock.release();
	}

}
