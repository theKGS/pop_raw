package raw.java.map;

import java.util.ArrayList;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MsgHandler {
	OtpErlangPid pid;
	int[] coords;
	Communicator mErlCom;
	Map map;
	UpdateListener mUpdtLis;

	public MsgHandler(Message msg, Communicator com, Map map,
			UpdateListener updtLis) {
		this.pid = msg.getPid();
		this.coords = msg.getValues();
		this.mErlCom = com;
		this.map = map;
		this.mUpdtLis = updtLis;
	}

	boolean mate() {
		int x = coords[0];
		int y = coords[1];

		int[] syncCoords = new int[] { x - 1, y - 1,

		x, y - 1,

		x + 1, y - 1,

		x - 1, y,

		x, y,

		x + 1, y,

		x - 1, y + 1,

		x, y + 1,

		x + 1, y + 1 };
		syncAll(syncCoords, 0);
		// if (x > 0 && x < map.getMapSize() && y > 0 && y < map.getMapSize()) {
		// synchronized (map.getMapArray()[x - 1][y - 1]) {
		// synchronized (map.getMapArray()[x][y - 1]) {
		// synchronized (map.getMapArray()[x + 1][y - 1]) {
		// synchronized (map.getMapArray()[x - 1][y]) {
		// synchronized (map.getMapArray()[x][y]) {
		// synchronized (map.getMapArray()[x + 1][y]) {
		// synchronized (map.getMapArray()[x - 1][y + 1]) {
		// synchronized (map.getMapArray()[x][y + 1]) {
		// synchronized (map.getMapArray()[x + 1][y + 1]) {
		// doMate(x, y);
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		return false;
	}

	void syncAll(int[] cVector, int index) {
		if (index > cVector.length - 1) {
			doMate(this.coords[0], this.coords[1]);
		}
		if (index + 1 < cVector.length) {
			int x = cVector[index];
			int y = cVector[index + 1];
			if (x >= 0 && x < map.getMapSize() && y >= 0
					&& y < map.getMapSize()) {
				synchronized (map.getMapArray()[x][y]) {
					syncAll(cVector, index + 2);
				}
			} else {
				syncAll(cVector, index + 2);
			}
		}
	}

	void doMate(int x, int y) {
		int mateType = map.getMapArray()[x][y].getType();
		boolean matePossible = false;
		int[] newSpot = null;
		int minX = x > 0 ? -1 : 0;
		int maxX = x < map.getMapSize()-1 ? 2 : 1;
		
		int minY = y > 0 ? -1 : 0;
		int maxY = y < map.getMapSize()-1 ? 2 : 1;
		for (int i = minX; i < maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (map.getMapArray()[x + i][y + j].getType() != MapNode.NONE) {
					if (map.getMapArray()[x + i][y + j].getType() != mateType) {
						matePossible = false;
					} else {
						matePossible = true;
					}
				} else {
					newSpot = new int[] { x + i, y + j };
				}
			}
		}
		if (matePossible && newSpot != null) {
			int r = (int) (Math.random() * 1000);
			if (r > 998)
				map.getMapArray()[newSpot[0]][newSpot[1]].setType(mateType);
		}

	}

	public int[][] getPossibleMates(int x, int y) {
		// synchronized()
		return null;
	}

	protected boolean compareCoordsArr(int[] coords) {
		return coords[1] * map.getMapSize() + coords[0] + map.getMapSize() < coords[3]
				* map.getMapSize() + coords[2] + map.getMapSize() ? true
				: false;

	}

	protected void sendUpdate() {
		if (mUpdtLis != null) {
			mUpdtLis.update();
		}
	}
}
