package raw.java.j_int_java;

import com.ericsson.otp.erlang.OtpErlangPid;
import raw.java.map.MapNode;

public class SendMessage extends MessageSuper{
	private MapNode[] map;
	
	public SendMessage(String type, OtpErlangPid pid, MapNode[] map){
		super(type, pid);
		this.map = map;
	}
	
	public SendMessage(String type,String mail, OtpErlangPid pid, MapNode[] map){
		super(type, mail, pid);
		this.map = map;
	}
	
	public MapNode[] getMap() {
		return this.map;
	}
}

/*
mapNode
{grasslvl int, atom(0/1/2) -> (none/rabit/wolf), Pid)
*/
