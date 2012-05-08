package raw.java.j_int_java;

public class Communicator {
	public FIFO outGoing = new FIFO();
	public FIFO inComming = new FIFO();
	
	public void send(SendMessage m) {
		//outGoing.put(m);
	}
	
	public Message receive() {
		return inComming.get();
	}
}