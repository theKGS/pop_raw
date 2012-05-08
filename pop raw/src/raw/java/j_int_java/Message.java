package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

public class Message {
	private final String type;
	private final int[] values;
	private final OtpErlangPid pid;
	
	public Message(String type, OtpErlangPid pid, int[] args){
		this.type = type;
		this.values = args;
		this.pid = pid;
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
	
	public String getType() {
		return this.type;
	}
	
	public int[] getValue() {
		return this.values;
	}
}