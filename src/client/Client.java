package client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import messagehandling.MessageType;
import messagehandling.messagehandler.LoginErrorMessageHandler;
import messagehandling.messagehandler.LoginSuccessMessageHandler;
import messagehandling.messageinput.MessageListener;
import messagehandling.messageinput.ThreadedMessageListener;
import messagehandling.messageoutput.DefaultMessageSender;
import messagehandling.messageoutput.MessageSender;
import surfaces.SurfaceHandler;
import surfaces.console.ConsoleSurface;
import surfaces.console.ConsoleSurfaceHandler;

public class Client {

	public static void main(String[] args) {

		ConsoleSurface surface = new ConsoleSurface();
		ConsoleSurfaceHandler surfaceHandler = new ConsoleSurfaceHandler(surface);

		Client client = new Client(surfaceHandler, "", 12345);
		client.startClient();

	}

	private SurfaceHandler surfaceHandler;
	private boolean isRunning;
	private boolean isLoggedIn;
	private int port;
	private String adress;
	private Socket socket;
	private MessageListener messageListener;
	private MessageSender messageSender;
	private MessageHandlerFactory messageHandlerFactory;
	private Semaphore waitForLoginMessage;

	public Client(SurfaceHandler surfaceHandler, String ip, int port) {
		this.surfaceHandler = surfaceHandler;
		this.isRunning = false;
		this.adress = ip;
		this.port = port;
		this.waitForLoginMessage = new Semaphore(0);

		initializeServices();
	}

	public Client(ConsoleSurfaceHandler surfaceHandler) {

		this(surfaceHandler, null, 0);
	}

	public synchronized void startClient() {
		if (isRunning) {
			throw new RuntimeException("Client is already running!");
		}
		isRunning = true;

		if (adress == null || port <= 0) {
			ConnectionDummy connection = surfaceHandler.getConnection();
			this.adress = connection.getAdress();
			this.port = connection.getPort();
		}
		performConnect();

		while (socket == null) {
			ConnectionDummy connection = surfaceHandler.getConnection();
			this.adress = connection.getAdress();
			this.port = connection.getPort();

			performConnect();
		}

		while (!isLoggedIn) {

			performLogin();
			try {
				waitForLoginMessage.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		waitForSurfaceMessages();

	}

	private void waitForSurfaceMessages() {
		while (true) {
			messageSender.sendMessage(surfaceHandler.getMessage());
			// TODO Commandhandler
		}
	}

	private void performLogin() {
		UserDummy user = surfaceHandler.getLogin();

		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		DataOutputStream dataout = new DataOutputStream(byteout);
		try {

			dataout.writeInt(user.getUsername().getBytes().length);
			dataout.write(user.getUsername().getBytes());

			dataout.writeInt(user.getPassword().getBytes().length);
			dataout.write(user.getPassword().getBytes());

			byte[] message = byteout.toByteArray();

			messageSender.sendMessage(new Message(message, MessageType.LOGIN));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void performConnect() {
		try {
			this.socket = new Socket(adress, port);
			messageListener = new ThreadedMessageListener(messageHandlerFactory, this.socket.getInputStream(),
					surfaceHandler);

			((ThreadedMessageListener) messageListener).start();

			messageSender = new DefaultMessageSender(this.socket.getOutputStream());
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public Semaphore getLoginLock() {
		return waitForLoginMessage;
	}

	private void initializeServices() {
		ServiceRegistry registry = new ServiceRegistry();
		registry.register(new LoginSuccessMessageHandler(waitForLoginMessage, this),
				ServiceRegistry.LOGIN_SUCCESS_MESSAGE_HANDLER);
		registry.register(new LoginErrorMessageHandler(waitForLoginMessage),
				ServiceRegistry.LOGIN_ERROR_MESSAGE_HANDLER);
		messageHandlerFactory = new MessageHandlerFactory(registry);
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

}