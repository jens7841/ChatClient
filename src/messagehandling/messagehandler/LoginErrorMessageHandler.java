package messagehandling.messagehandler;

import java.util.concurrent.Semaphore;

import client.Service;
import messagehandling.Message;
import surfaces.SurfaceHandler;

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
