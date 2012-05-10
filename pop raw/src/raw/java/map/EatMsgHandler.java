package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

public class EatMsgHandler extends MsgHandler implements Runnable {

	/**
	 * Constructor for the Eat message handler
	 * 
	 * @param msg
	 *            the message containing pid and coords
	 * @param mErlCom
	 *            communicator used to send responses
	 * @param mapArray
	 *            the map representation.
	 */
	public EatMsgHandler(Message msg, Communicator mErlCom, Map map,UpdateListener updtLis) {
		super(msg, mErlCom, map, updtLis);
	}

	@Override
	public void run() {

		synchronized (map.getMapArray()[coords[X1]][coords[Y1]]) {
			MapNode tNode = map.getMapArray()[coords[X1]][coords[Y1]];
			if (tNode.getGrassLevel() > 0) {
				tNode.setGrassLevel(tNode.getGrassLevel() - 1);
			}
			//mErlCom.send((new SendMessage("yes", pid, null)));
		}
	}

}