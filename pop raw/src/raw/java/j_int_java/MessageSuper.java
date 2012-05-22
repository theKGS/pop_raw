package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MessageSuper {
	private final int type;
	private final int mailbox; 
	private final OtpErlangPid pid;
	
	public MessageSuper(int type, OtpErlangPid pid) {
		this.type = type;
		this.mailbox = -1;
		this.pid = pid;
	}
	
	public MessageSuper(int type,int mailbox, OtpErlangPid pid) {
		this.type = type;
		this.mailbox = mailbox;
		this.pid = pid;
	}
	
	public OtpErlangPid getPid() {
		return pid;
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getMailbox() {
		return this.mailbox;
	}
}
