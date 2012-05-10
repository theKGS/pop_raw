package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.SendMessage;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MapMsgHandler implements Runnable {
	OtpErlangPid pid;
	Communicator mErlCom;
	MapNode[][] mapArray;
	/**
	 * Constructor for the Map message runnable.
	 * @param pid the pid of the calling process
	 * @param mErlCom the communicator used to send messages.
	 * @param mapArray the representation of the map
	 */
	public MapMsgHandler(OtpErlangPid pid, Communicator mErlCom, MapNode[][] mapArray) {
		
		this.pid = pid;
		this.mErlCom = mErlCom;
		this.mapArray = mapArray;
	}

	@Override
	public void run() {
		//mErlCom.send(new SendMessage("map", mapArray, pid));
	}
}
