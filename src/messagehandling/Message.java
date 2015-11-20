package messagehandling;

public class Message {

	private byte[] message;
	private MessageType type;

	public Message(byte[] message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public Message(String message, MessageType type) {
		this(message.getBytes(), type);
	}

	public MessageType getType() {
		return type;
	}

	public byte[] getMessage() {
		return message;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (byte b : message) {
			builder.append((char) b);
		}

		return builder.toString();
	}
}
