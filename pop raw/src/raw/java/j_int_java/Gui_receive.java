package raw.java.j_int_java;

import java.io.IOException;
import com.ericsson.otp.erlang.*;

public class Gui_receive implements Runnable{
	private String nodeName = "athens";
	private String mboxName = "sparta";
	private OtpMbox mbox;
	private FIFO queue;
	private Communicator com;

	public Gui_receive(FIFO send, Communicator com) {
		this.queue = send;
		this.com = com;
		
		start();
	}

	public void start() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean stop() {
		return false;
	}

	private void init() throws IOException {
		OtpNode self = new OtpNode(this.nodeName, "thisissparta");
		mbox = self.createMbox(this.mboxName);
	}

	public void run() {
		try {
			OtpErlangObject tempO = mbox.receive();
			if (tempO instanceof OtpErlangTuple) {
				OtpErlangTuple tupleTemp = (OtpErlangTuple) tempO;
				if (tupleTemp.elementAt(0) instanceof OtpErlangPid) {
					com.pid = (OtpErlangPid) tupleTemp.elementAt(0);
				} else {
					System.out.println("No Erlang pid");
				}
			} else {
				System.out.println("No Erlang tuple");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true) {
			try {
				OtpErlangObject o = mbox.receive();
				if(o instanceof OtpErlangTuple) {
					OtpErlangTuple msg = (OtpErlangTuple) o;
					Message message = deCode(msg);
					queue.put(message);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Message deCode(OtpErlangTuple msg) throws OtpErlangRangeException {
		int sType = -1;
		OtpErlangPid pidTemp = null;
		int size = msg.arity();
		int[] values = new int[size - 2];
		OtpErlangObject type = msg.elementAt(0);
		OtpErlangObject pid = msg.elementAt(1);
		if (type instanceof OtpErlangLong) {
			OtpErlangLong tempType = (OtpErlangLong) type;
			sType = tempType.intValue();
		}
		if (pid instanceof OtpErlangPid) {
			pidTemp = (OtpErlangPid) pid;
		}
		for (int i = 2; i <= size; i++) {
			if (msg.elementAt(i) instanceof OtpErlangLong) {
				values[i-2] = ((OtpErlangLong) msg.elementAt(i)).intValue(); 
			}
		}
		return new Message(sType, pidTemp, values);
	}
}
