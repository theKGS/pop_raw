package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

import com.ericsson.otp.erlang.OtpErlangPid;

public class EatMsgHandler implements Runnable {
	OtpErlangPid pid;
	int[] coords;
	private Communicator mErlCom;
	private MapNode[][] mapArray;
	public EatMsgHandler(Message msg, Communicator mErlCom, MapNode[][] mapArray){
		this.pid = msg.getPid();
		this.coords = msg.getValues();
		this.mErlCom = mErlCom;
		this.mapArray = mapArray;
	}
	@Override
	public void run() {
		
		synchronized(mapArray[coords[0]][coords[1]]){
			MapNode tNode = mapArray[coords[0]][coords[1]];
			tNode.setGrassLevel(tNode.getGrassLevel());
			mErlCom.send(new SendMessage("yes", null, pid));
		}
	}
	
}