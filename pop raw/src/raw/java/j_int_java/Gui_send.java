package raw.java.j_int_java;

import java.io.IOException;

import com.ericsson.otp.erlang.*;

public class Gui_send implements Runnable{
	private FIFO queue;
	private String nodeName = "rawsender";
	private String cookie = "thisissparta";
	private OtpMbox mbox;
	private OtpErlangPid pid;

	public Gui_send(FIFO queue, OtpErlangPid pid) {
		this.queue = queue;
		this.pid = pid;
		
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() throws IOException {
		OtpNode node = new OtpNode(nodeName, cookie);
		mbox = node.createMbox();
	}

	public void run() {
		while(true) {
			OtpErlangTuple message = enCode(queue.get());
			mbox.send(this.pid , message);
		}
	}
	
	private OtpErlangTuple enCode(Message msg) {
		String sType = msg.getType();
		int[] values = msg.getValues();
		int size = values.length;
		System.out.println("value size: " + size);
		OtpErlangObject[] message = new OtpErlangObject[size+2];
		System.out.println("message size" + message.length);
		message[0] = new OtpErlangAtom(sType);
		message[1] = msg.getPid();
		for (int i = 2; i <= size+1; i++) {
			message[i] = new OtpErlangInt(values[i-2]);
		}
		return new OtpErlangTuple(message);
	}
}
