package raw.java.j_int_java;

import java.io.IOException;

import com.ericsson.otp.erlang.*;

public class Gui_receive implements Runnable{
	private String nodeName = "This is";
	private String mboxName = "Sparta";
	private OtpMbox mbox;
	private FIFO send;
	
	public Gui_receive(FIFO send) {
		this.send = send;
	}
	
	public boolean start() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		run();
		
		return true;
	}
	
	public boolean stop() {
		return false;
	}
	
	private void init() throws IOException {
		OtpNode self = new OtpNode(this.nodeName);
		mbox = self.createMbox(this.mboxName);
	}
	
	public void run() {
		while(true) {
			try {
				OtpErlangObject o = mbox.receive();
				if(o instanceof OtpErlangTuple) {
					Message message;
					String sType;
					OtpErlangTuple msg = (OtpErlangTuple) o;
					OtpErlangObject type = msg.elementAt(1);
					if (type instanceof OtpErlangAtom) {
						OtpErlangAtom tempType = (OtpErlangAtom) type;
						sType = tempType.atomValue();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
