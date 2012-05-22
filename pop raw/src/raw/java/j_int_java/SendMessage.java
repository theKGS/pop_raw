package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;
import raw.java.map.MapNode;

/**
 * @author group 8
 */
public class SendMessage extends MessageSuper{
	private MapNode[] map;
	
	/**
	 * Creates a SendMessage with arguments int, OtpErlangPid, MapNode[].
	 * @param type int
	 * @param pid OtpErlangPid
	 * @param map MapNode[]
	 */
	public SendMessage(int type, OtpErlangPid pid, MapNode[] map){
		super(type, pid);
		this.map = map;
	}
	
	/**
	 * Creates a message with arguments int, int, OtpErlangPid, MapNode[].
	 * @param type int
	 * @param mail int
	 * @param pid OtpErlangPid
	 * @param map MapNode[]
	 */
	public SendMessage(int type, int mail, OtpErlangPid pid, MapNode[] map){
		super(type, mail, pid);
		this.map = map;
	}
	
	/**
	 * Returns the MapNode[] part from the message.
	 * @return Returns the MapNode[] from the message.
	 */
	public MapNode[] getMap() {
		return this.map;
	}
}