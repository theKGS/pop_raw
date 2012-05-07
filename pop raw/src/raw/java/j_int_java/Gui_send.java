package raw.java.j_int_java;

import com.ericsson.otp.erlang.*;

public class Gui_send implements Runnable{
	private FIFO queue;
	
	public Gui_send(FIFO queue) {
		this.queue = queue;
	}

	public void run() {
		while(true) {
			OtpErlangTuple message = enCode(queue.get());
		// TODO fetch message from FIFO, encode it and send it.
		}
	}
	
	private OtpErlangTuple enCode(Message msg) {
		String sType = msg.getType();
		int[] values = msg.getValue();
		int size = values.length;
		OtpErlangObject[] message = new OtpErlangObject[size+1];
		message[0] = new OtpErlangAtom(sType);
		for (int i = 0; i <= size; i++) {
			message[i+1] = new OtpErlangInt(values[i]);
		}
		return new OtpErlangTuple(message);
	}
}
