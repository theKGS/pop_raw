package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

public class MapMsgHandler extends MsgHandler implements Runnable {


	/**
	 * Constructor for the Map message runnable.
	 * 
	 * @param pid
	 *            the pid of the calling process
	 * @param mErlCom
	 *            the communicator used to send messages.
	 * @param mapArray
	 *            the representation of the map
	 */
	public MapMsgHandler(Message msg, Communicator mErlCom, Map map, MessagePool msgPool) {
		super(msg, mErlCom, map, null, msgPool);
	}

	@Override
	public void run() {
		int x = coords[0];
		int y = coords[1];
		int index = 0;
		MapNode[] squares = new MapNode[9];

		for (int j = -1; j < 2; j++) {
			for (int i = -1; i < 2; i++) {
				if (x + i >= 0 && x + i < map.getMapSize() && y + j >= 0
						&& y + j < map.getMapSize()) {
					squares[index] = map.getMapArray()[x+i][y+j];
				}
				index++;
			}
		}
		mErlCom.send(new SendMessage("map", pid, squares));
		msgPool.AddMapToStack(this);
	}
}
