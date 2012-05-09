package raw.java.map;

import raw.java.j_int_java.Communicator;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MsgHandler {
	OtpErlangPid pid;
	int[] coords;
	Communicator mErlCom;

	public MsgHandler(OtpErlangPid pid, int[] coords, Communicator com) {
		this.pid = pid;
		this.coords = coords;
		this.mErlCom = com;
	}
	
	boolean mate(){
		
		
		
		
		
		return false;
	}
}
