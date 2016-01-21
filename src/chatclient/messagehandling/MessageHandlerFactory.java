package chatclient.messagehandling;

import chatclient.client.ServiceRegistry;
import chatclient.messagehandling.messagehandler.MessageHandler;

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

		default:
			break;

		}
		return null;
	}
}
