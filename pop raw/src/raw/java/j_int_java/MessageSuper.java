package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

/**
 * @author group 8
 */
public class MessageSuper {
	private final int type;
	private final int mailbox; 
	private final OtpErlangPid pid;
	
	/**
	 * Creates a MessageSuper with arguments int, OtpErlangPid.
	 * @param type int
	 * @param pid OtpErlangPid
	 */
	public MessageSuper(int type, OtpErlangPid pid) {
		this.type = type;
		this.mailbox = -1;
		this.pid = pid;
	}
	
	/**
	 * Creates a MessageSuper with arguments int, int, OtpErlangPid.
	 * @param type int
	 * @param mailbox int
	 * @param pid OtpErlangPid
	 */
	public MessageSuper(int type,int mailbox, OtpErlangPid pid) {
		this.type = type;
		this.mailbox = mailbox;
		this.pid = pid;
	}
	
	/**
	 * Returns the PID part from the message.
	 * @return OtpErlangPid
	 */
	public OtpErlangPid getPid() {
		return pid;
	}
	
	/**
	 * Returns the type part from the message.
	 * @return int
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Returns the mailbox part from the message.
	 * @return int
	 */
	public int getMailbox() {
		return this.mailbox;
	}
}
