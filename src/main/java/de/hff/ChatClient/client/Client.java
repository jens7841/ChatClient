package de.hff.ChatClient.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import de.hff.ChatClient.commandhandling.CommandHandler;
import de.hff.ChatClient.commandhandling.commands.Download;
import de.hff.ChatClient.commandhandling.commands.Upload;
import de.hff.ChatClient.filehandling.FileManager;
import de.hff.ChatClient.messagehandling.Message;
import de.hff.ChatClient.messagehandling.MessageHandlerFactory;
import de.hff.ChatClient.messagehandling.messagehandler.DownloadConfirmationMessageHandler;
import de.hff.ChatClient.messagehandling.messagehandler.DownloadPackageMessageHandler;
import de.hff.ChatClient.messagehandling.messagehandler.DownloadRejectMessageHandler;
import de.hff.ChatClient.messagehandling.messagehandler.LoginErrorMessageHandler;
import de.hff.ChatClient.messagehandling.messagehandler.LoginSuccessMessageHandler;
import de.hff.ChatClient.messagehandling.messagehandler.UploadConfirmationHandler;
import de.hff.ChatClient.messagehandling.messageinput.MessageListener;
import de.hff.ChatClient.messagehandling.messageinput.ThreadedMessageListener;
import de.hff.ChatClient.messagehandling.messageoutput.DefaultMessageSender;
import de.hff.ChatClient.messagehandling.messageoutput.MessageSender;
import de.hff.ChatClient.messagehandling.messageoutput.TimedOutputStream;
import de.hff.ChatClient.surfaces.SurfaceHandler;
import de.hff.ChatClient.surfaces.console.ConsoleSurface;
import de.hff.ChatClient.surfaces.console.ConsoleSurfaceHandler;
import de.hff.ChatShared.messagehandling.MessageType;

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
	private CommandHandler commandHandler;
	private FileManager uploadFileManager;
	private FileManager downloadFileManager;
	private ServiceRegistry registry;

	public Client(SurfaceHandler surfaceHandler, String ip, int port) {
		this.surfaceHandler = surfaceHandler;
		this.isRunning = false;
		this.adress = ip;
		this.port = port;
		this.waitForLoginMessage = new Semaphore(0);
		this.commandHandler = new CommandHandler();
		this.uploadFileManager = new FileManager();
		this.downloadFileManager = new FileManager();
		this.messageSender = new DefaultMessageSender();
		registry = new ServiceRegistry();

		initializeServices();
	}

	private void initializeCommands() {
		commandHandler.addCommand(
				new Upload("upload", new String[] { "up" }, surfaceHandler, messageSender, uploadFileManager));
		commandHandler.addCommand(new Download("download", new String[] { "get", "down" }, surfaceHandler,
				messageSender, downloadFileManager));
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

		initializeCommands();

		waitForSurfaceMessages();

	}

	private void waitForSurfaceMessages() {
		while (true) {

			Message message = surfaceHandler.getMessage();

			if (message.getType().equals(MessageType.COMMAND)) {

				String input = message.toString().trim();

				int indexOfSpace = input.indexOf(" ");

				if ((indexOfSpace != -1 && commandHandler.commandKnown(input.substring(0, indexOfSpace)))
						|| (indexOfSpace == -1 && commandHandler.commandKnown(input))) {
					commandHandler.handleCommand(input);
				} else {
					messageSender.sendMessage(message);
				}

			} else {
				messageSender.sendMessage(message);
			}
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

			TimedOutputStream timedOutputStream = new TimedOutputStream(this.socket.getOutputStream());
			registry.register(timedOutputStream, ServiceRegistry.TIMED_MESSAGE_OUTPUT_STREAM);
			messageSender.setOutputStream(timedOutputStream);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		} catch (IllegalArgumentException e) {
		}
	}

	public Semaphore getLoginLock() {
		return waitForLoginMessage;
	}

	private void initializeServices() {
		registry.register(new LoginSuccessMessageHandler(waitForLoginMessage, this),
				ServiceRegistry.LOGIN_SUCCESS_MESSAGE_HANDLER);
		registry.register(new LoginErrorMessageHandler(waitForLoginMessage),
				ServiceRegistry.LOGIN_ERROR_MESSAGE_HANDLER);
		registry.register(new UploadConfirmationHandler(surfaceHandler, uploadFileManager, messageSender, registry),
				ServiceRegistry.UPLOAD_CONFIRMATION_HANDLER);
		registry.register(new DownloadRejectMessageHandler(), ServiceRegistry.DOWNLOAD_REJECT_MESSAGE_HANDLER);
		registry.register(new DownloadPackageMessageHandler(downloadFileManager),
				ServiceRegistry.DOWNLOAD_PACKAGE_MESSAGE_HANDLER);
		registry.register(new DownloadConfirmationMessageHandler(downloadFileManager),
				ServiceRegistry.DOWNLOAD_CONFIRMATION_MESSAGE_HANDLER);

		messageHandlerFactory = new MessageHandlerFactory(registry);
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

}