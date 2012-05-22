package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

/**
 * @author group 8
 */
public class Message extends MessageSuper {
	private final int[] values;
	
	/**
	 * Creates a Message with arguments int, OtpErlangPid, int[].
	 * @param type int
	 * @param pid OtpErlangPid
	 * @param args int[]
	 */
	public Message(int type, OtpErlangPid pid, int[] args){
		super(type, pid);
		this.values = args;
	}
	
	/**
	 * Constructor for Message, takes arguments int, int, OtpErlangPid, int[].
	 * @param type int
	 * @param mail int
	 * @param pid OtpErlangPid
	 * @param args int[]
	 */
	public Message(int type, int mail, OtpErlangPid pid, int[] args){
		super(type, mail, pid);
		this.values = args;
	}
	
	/**
	 * Returns the int[] part from the message.
	 * @return int[]
	 */
	public int[] getValues() {
		return values;
	}
}