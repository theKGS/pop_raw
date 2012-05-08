package raw.java.j_int_java;

import java.io.IOException;
import com.ericsson.otp.erlang.*;

public class Gui_receive implements Runnable{
	private String nodeName = "athens";
	private String mboxName = "sparta";
	private OtpMbox mbox;
	private FIFO queue;

	public Gui_receive(FIFO send) {
		this.queue = send;
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
		OtpNode self = new OtpNode(this.nodeName);
		mbox = self.createMbox(this.mboxName);
		self.setCookie("thisissparta");
		System.out.println("name: " + self.alive());
		System.out.println("host: " + self.host());
		System.out.println("cookie: " + self.cookie());
	}

	public void run() {
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
		String sType;
		int size = msg.arity();
		int[] values = new int[size-1];
		OtpErlangObject type = msg.elementAt(0);
		if (type instanceof OtpErlangAtom) {
			OtpErlangAtom tempType = (OtpErlangAtom) type;
			sType = tempType.atomValue();
		} else {
			sType = "fail";
		}
		for (int i = 2; i <= size; i++) {
			if (msg.elementAt(i) instanceof OtpErlangInt) {
				values[i-2] = ((OtpErlangInt) msg.elementAt(i)).intValue(); 
			}
		}
		return new Message(sType, values);
	}
}
