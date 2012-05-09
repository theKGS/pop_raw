package raw.java.j_int_java;

import com.ericsson.otp.erlang.*;

public class Communicator {
	private FIFO outGoing = new FIFO();
	private FIFO inComming = new FIFO();
	private Gui_send sender;
	private Gui_receive receiver;
	private OtpErlangPid pid = null;
	
	public Communicator() {
		receiver = new Gui_receive(inComming, pid);
		new Thread(receiver).start();
		boolean hold = true;
//		while (hold) {
//			if (this.pid == null) {
//				try {
//					wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			} else {
//				hold = false;
//			}
//		}
		sender = new Gui_send(outGoing, pid);
		new Thread(sender).start();

	}
	
	public void send(SendMessage m) {
//		outGoing.put(m);
	}
	
	public Message getSend() {
		return outGoing.get();
	}
	
	public Message receive() {
		return inComming.get();
	}
	
	public void putReceive(Message m) {
		inComming.put(m);
	}
}