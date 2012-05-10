package raw.java.j_int_java;

import com.ericsson.otp.erlang.*;

public class Communicator {
	private FIFO outGoing = new FIFO();
	private FIFO inComming = new FIFO();
	private Gui_send sender;
	private Gui_receive receiver;
	public OtpErlangPid pid = null;
	
	public Communicator() {
		receiver = new Gui_receive(inComming, this);
		new Thread(receiver).start();
		System.out.println("Gui_receive started");
		boolean hold = true;
//		while (hold) {
//			if (this.pid == null) {
//				
//			} else {
//				hold = false;
//			}
//		}
//		System.out.println(this.pid.toString());
		
		sender = new Gui_send(outGoing, pid);
		new Thread(sender).start();

	}
	
	public void send(Message m) {
		outGoing.put(m);
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
	
	public OtpErlangPid getPid() {
		return this.pid;
	}
}