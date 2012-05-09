package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

import com.ericsson.otp.erlang.OtpErlangPid;

public class EatMsgHandler extends MsgHandler implements Runnable {
	private MapNode[][] mapArray;
	/**
	 * Constructor for the Eat message handler
	 * @param msg the message containing pid and coords
	 * @param mErlCom communicator used to send responses
	 * @param mapArray the map representation.
	 */
	public EatMsgHandler(Message msg, Communicator mErlCom, MapNode[][] mapArray){
		super(msg.getPid(), msg.getValues(),mErlCom);
		
		this.mapArray = mapArray;
	}
	
	
	
	@Override
	public void run() {
		
		synchronized(mapArray[coords[0]][coords[1]]){
			MapNode tNode = mapArray[coords[0]][coords[1]];
			tNode.setGrassLevel(tNode.getGrassLevel());
			mErlCom.send((new SendMessage("yes", null, pid)));
		}
	}
	
}