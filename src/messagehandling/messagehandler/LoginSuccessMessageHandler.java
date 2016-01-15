package messagehandling.messagehandler;

import java.util.concurrent.Semaphore;

import client.Client;
import client.Service;
import messagehandling.Message;
import surfaces.SurfaceHandler;

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
