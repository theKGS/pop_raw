package raw.java.j_int_java;

public class Message {
	private final MessageType type;
	private final int[] values;
	
	public Message(String type, int[] values) {
		if (type.equals("move")) {
			this.type = MessageType.MOVE;
		} else if (type.equals("grass")) {
			this.type = MessageType.GRASS;
		} else if (type.equals("died")) {
			this.type = MessageType.DIED;
		} else if (type.equals("newR")) {
			this.type = MessageType.NEWR;
		} else if (type.equals("newW")) {
			this.type = MessageType.NEWW;
		} else {
			this.type = MessageType.FAIL;
		}
		this.values = values;
	}
	
	public MessageType getType() {
		return this.type;
	}
	
	public int[] getValue() {
		return this.values;
	}
}