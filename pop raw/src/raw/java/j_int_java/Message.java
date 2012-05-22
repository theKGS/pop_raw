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
	
	public Message(int type, String mail, OtpErlangPid pid, int[] args){
		super(type, mail, pid);
		this.values = args;
	}
	
//	public Message(String type, int value1) {
//		this.type = type;
//		this.values = new int[1];
//		this.values[0] = value1;
//	}
//	public Message(String type, int value1, int value2) {
//		this.type = type;
//		this.values = new int[2];
//		this.values[0] = value1;
//		this.values[1] = value2;
//	}
//	public Message(String type, int[] values) {
//		this.type = type;
//		this.values = values;
//	}
}