package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

import raw.java.map.MapNode;

public class SendMessage {
	private final String type;
	private MapNode[][] map;
	private OtpErlangPid pid;
	public SendMessage(String type, MapNode[][] map, OtpErlangPid pid){
		this.type = type;
		this.map = map;
		this.pid = pid;
	}
}
