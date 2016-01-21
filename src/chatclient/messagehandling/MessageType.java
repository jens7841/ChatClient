package chatclient.messagehandling;

public enum MessageType {

	CHAT_MESSAGE(0), LOGIN(1), LOGIN_ERROR(2), LOGIN_SUCCESS(3), ERROR(4), SUCCESS(5), DISCONNECT(6), UPLOAD_REQUEST(
			7), UPLOAD_CONFIRMATION(8), UPLOAD_PACKAGE(9), UPLOAD_REJECT(10), COMMAND(11);

	private int typeNumber;

	private MessageType(int typeNumber) {
		this.typeNumber = typeNumber;
	}

	public int getTypeNumber() {
		return typeNumber;
	}

	public static MessageType getType(int typeNumber) {
		for (MessageType msgType : values()) {
			if (msgType.getTypeNumber() == typeNumber) {
				return msgType;
			}
		}
		return null;
	}

}