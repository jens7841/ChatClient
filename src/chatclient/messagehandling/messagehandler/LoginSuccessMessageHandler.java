package chatclient.messagehandling.messagehandler;

import java.util.concurrent.Semaphore;

import chatclient.client.Client;
import chatclient.client.Service;
import chatclient.messagehandling.Message;
import chatclient.surfaces.SurfaceHandler;

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
