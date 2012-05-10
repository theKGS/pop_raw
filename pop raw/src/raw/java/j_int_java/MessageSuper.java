package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MessageSuper {
	private final String type;
	private final OtpErlangPid pid;
	
	public MessageSuper(String type, OtpErlangPid pid) {
		this.type = type;
		this.pid = pid;
	}
	
	public OtpErlangPid getPid() {
		return pid;
	}
	
	public String getType() {
		return this.type;
	}
}
