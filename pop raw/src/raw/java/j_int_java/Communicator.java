package raw.java.j_int_java;

public class Communicator {
	private FIFO outGoing = new FIFO();
	private FIFO inComming = new FIFO();
	
	public void send(Message m) {
		outGoing.put(m);
	}
	
	public Message receive() {
		return inComming.get();
	}
}