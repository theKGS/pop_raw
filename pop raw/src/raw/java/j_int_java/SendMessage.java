package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;

import raw.java.map.MapNode;

public class SendMessage {
	private final String type;
	private MapNode[][] map;
	private OtpErlangPid pid;
	public SendMessage(String type, OtpErlangPid pid, MapNode[][] map){
		this.type = type;
		this.map = map;
		this.pid = pid;
	}
}

/*
mapNode
{grasslvl int, atom(0/1/2) -> (none/rabit/wolf), Pid)
*/
