package de.hff.ChatClient.messagehandling;

import de.hff.ChatClient.client.ServiceRegistry;
import de.hff.ChatClient.messagehandling.messagehandler.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class MessageHandlerFactory {

	private ServiceRegistry registry;

	public MessageHandlerFactory(ServiceRegistry registry) {
		this.registry = registry;
	}

	public MessageHandler getMessageHandler(MessageType type) {

		switch (type) {

		case LOGIN_SUCCESS:
			return (MessageHandler) registry.getService(ServiceRegistry.LOGIN_SUCCESS_MESSAGE_HANDLER);

		case LOGIN_ERROR:
			return (MessageHandler) registry.getService(ServiceRegistry.LOGIN_ERROR_MESSAGE_HANDLER);

		case UPLOAD_CONFIRMATION:
			return (MessageHandler) registry.getService(ServiceRegistry.UPLOAD_CONFIRMATION_HANDLER);

		case DOWNLOAD_REJECT:
			return (MessageHandler) registry.getService(ServiceRegistry.DOWNLOAD_REJECT_MESSAGE_HANDLER);

		case DOWNLOAD_PACKAGE:
			return (MessageHandler) registry.getService(ServiceRegistry.DOWNLOAD_PACKAGE_MESSAGE_HANDLER);
		default:
			break;

		}
		return null;
	}
}
