package raw.java.j_int_java;

public class Communicator {
	private FIFO outGoing = new FIFO();
	private FIFO inComming = new FIFO();
	private Gui_send sender;
	private Gui_receive receiver;
	
	public Communicator() {
		receiver = new Gui_receive(inComming);
		new Thread(receiver).start();
		sender = new Gui_send(outGoing);
		new Thread(sender).start();

	}
	
	public void send(Message m) {
		outGoing.put(m);
	}
	
	public Message receive() {
		return inComming.get();
	}
}