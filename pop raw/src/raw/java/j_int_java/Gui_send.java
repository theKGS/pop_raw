package raw.java.j_int_java;

import java.io.IOException;

import com.ericsson.otp.erlang.*;

public class Gui_send implements Runnable{
	private FIFO queue;
	private String nodeName = "rawsender";
	private OtpMbox mbox;
	
	public Gui_send(FIFO queue) {
		this.queue = queue;
		
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() throws IOException {
		OtpNode node = new OtpNode(nodeName);
		mbox = node.createMbox();
	}

	public void run() {
		while(true) {
			OtpErlangTuple message = enCode(queue.get());
			mbox.send("rawMap", "raws@laptop", message);
		}
	}
	
	private OtpErlangTuple enCode(Message msg) {
		String sType = msg.getType();
		int[] values = msg.getValue();
		int size = values.length;
		OtpErlangObject[] message = new OtpErlangObject[size+1];
		message[0] = new OtpErlangAtom(sType);
		for (int i = 1; i <= size; i++) {
			message[i] = new OtpErlangInt(values[i-1]);
		}
		return new OtpErlangTuple(message);
	}
}
