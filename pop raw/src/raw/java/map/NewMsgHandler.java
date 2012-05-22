package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class NewMsgHandler extends MsgHandler implements Runnable {
	/**
	 * Handler for the messagetype new
	 * 
	 * @param msg
	 *            the message
	 * @param mErlCom
	 *            communicator to respond through
	 * @param map
	 *            the map that created the message
	 */
	public NewMsgHandler(Message msg, Communicator mErlCom, Map map, MessagePool msgPool) {
		super(msg, mErlCom, map, null, msgPool);
	}

	@Override
	public void run() {
		synchronized (map.getMapArray()[coords[0]][coords[1]]) {
			map.getMapArray()[coords[0]][coords[1]].setPid(pid);
			mErlCom.send(new Message("start", pid, null));
		}
		msgPool.AddNewToStack(this);
	}
}
