package raw.java.j_int_java;

import java.io.IOException;

import com.ericsson.otp.erlang.*;

public class Gui_receive {
	private String nodeName = "This is";
	private String mboxName = "Sparta";
	private OtpMbox mbox;
	
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
	
	private void run() {
		while(true) {
			try {
				OtpErlangObject o = mbox.receive();
				if(o instanceof OtpErlangTuple) {
					OtpErlangTuple msg = (OtpErlangTuple) o;
					msg.elementAt(1);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
