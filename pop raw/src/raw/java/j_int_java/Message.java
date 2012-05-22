package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

public class Message extends MessageSuper {
	private final int[] values;
	
	public int[] getValues() {
		return values;
	}

	public Message(int type, OtpErlangPid pid, int[] args){
		super(type, pid);
		this.values = args;
	}
	
	public Message(int type, int mail, OtpErlangPid pid, int[] args){
		super(type, mail, pid);
		this.values = args;
	}
}