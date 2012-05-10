package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MoveMsgHandler extends MsgHandler implements Runnable {

	

	public MoveMsgHandler(Message message, Communicator mErlCom, Map map,
			UpdateListener updtLis) {
		super(message, mErlCom, map, updtLis);
	}
	
	@Override
	public void run() {
		mate();
		if (compareCoordsArr(coords)) {
			synchronized (map.getMapArray()[coords[X1]][coords[Y1]]) {
				synchronized (map.getMapArray()[coords[X2]][coords[Y2]]) {
					checkMove();
					sendUpdate(coords[X1],coords[Y1], map.getMapArray()[coords[X1]][coords[Y1]]);
					sendUpdate(coords[X2],coords[Y2], map.getMapArray()[coords[X2]][coords[Y2]]);
				}
			}
		} else {
			synchronized (map.getMapArray()[coords[X2]][coords[Y2]]) {
				synchronized (map.getMapArray()[coords[X1]][coords[Y1]]) {
					checkMove();
					sendUpdate(coords[X1],coords[Y1], map.getMapArray()[coords[X1]][coords[Y1]]);
					sendUpdate(coords[X2],coords[Y2], map.getMapArray()[coords[X2]][coords[Y2]]);
				}
			}
		}
		

	}

	/**
	 * Method that get the index of both coordinate pairs and returns the
	 * smallest one
	 * 
	 * @param coords
	 *            int[4] with {x1,y1,x,y2} coords
	 * @return coord-pair with the smallest index
	 */


	private void checkMove() {
		MapNode currentNode = map.getMapArray()[coords[X1]][coords[Y1]];
		MapNode targetNode = map.getMapArray()[coords[X2]][coords[Y1]];
		if (targetNode.getType() != MapNode.NONE) {
			//mErlCom.send(new SendMessage("no", null, pid));
		} else {

			targetNode.setPid(currentNode.getPid());
			targetNode.setType(currentNode.getType());
			currentNode.setPid(null);
			currentNode.setType(MapNode.NONE);
			//.send(new SendMessage("yes", null, pid));

		}
		// System.out.println("Move handled");
	}
}
