package messagehandling;

public class Message {

	private byte[] message;
	private String strMessage;
	private MessageType type;

	public Message(byte[] message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public Message(String message, MessageType type) {
		this(message.getBytes(), type);
		this.strMessage = message;
	}

	public MessageType getType() {
		return type;
	}

	public byte[] getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return String.valueOf(message);
	}
}
