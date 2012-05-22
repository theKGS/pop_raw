package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MsgHandler {
	public static final int AGE = 0;
	public static final int HUNGER = 1;
	public static final int X1 = 2;
	public static final int Y1 = 3;
	public static final int X2 = 4;
	public static final int Y2 = 5;

	protected OtpErlangPid pid;
	protected int[] coords;
	protected Communicator mErlCom;
	protected Map map;
	protected UpdateListener mUpdtLis;
	protected MessagePool msgPool;

	/**
	 * Super constructor called by all message handlers.
	 * 
	 * @param msg
	 *            the message received from communicator
	 * @param com
	 *            the communicator
	 * @param map
	 *            map that created object
	 * @param updtLis
	 *            update lister
	 */
	public MsgHandler(Message msg, Communicator com, Map map,
			UpdateListener updtLis, MessagePool msgPool) {
		this.pid = msg.getPid();
		this.coords = msg.getValues();
		this.msgPool = msgPool;
		this.mErlCom = com;
		this.map = map;
		this.mUpdtLis = updtLis;
	}

	/**
	 * Tries to mate with the current rabbit or wolf.
	 * 
	 */
	protected boolean mate() {
		int x = coords[X1];
		int y = coords[Y1];

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
		return false;
	}

	/**
	 * Synchronizes all squares in cVector, from smallest array index to the
	 * largest
	 * 
	 * @param cVector
	 *            the int array containing all coordinates that shall be
	 *            synchronized (x1,y1,x2,y2,...,xn,yn)
	 * @param index
	 *            due to recursion this index is used to get the correct
	 *            cordinate pair
	 */
	protected void syncAll(int[] cVector, int index) {
		if (index > cVector.length - 1) {
			doMate(this.coords[X1], this.coords[Y1]);
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

	/**
	 * Executes the mating
	 * 
	 * @param x
	 *            x position to mate
	 * @param y
	 *            y position to mate
	 */
	protected void doMate(int x, int y) {
		int mateType = map.getMapArray()[x][y].getType();
		boolean matePossible = false;
		int[] newSpot = null;
		int minX = x > 0 ? -1 : 0;
		int maxX = x < map.getMapSize() - 1 ? 2 : 1;

		int minY = y > 0 ? -1 : 0;
		int maxY = y < map.getMapSize() - 1 ? 2 : 1;
		if (coords[AGE] > 15 && coords[HUNGER] < 3) {
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
				if (r > 300) {
					map.getMapArray()[newSpot[0]][newSpot[1]].setType(mateType);
					if (mateType == MapNode.RABBIT) {
						mErlCom.send(new Message("newRabbit", null, newSpot));
					} else {
						mErlCom.send(new Message("newWolf", null, newSpot));
					}
				}

			}
		}

	}

	/**
	 * Compares two coord pairs
	 * 
	 * @param coords
	 *            array of coord pairs (x1,y1,x2,y2)
	 * @return true if (x1,y1) < (x2,y2) false otherwise
	 */
	protected boolean compareCoordsArr(int[] coords) {
		return coords[Y1] * map.getMapSize() + coords[X1] + map.getMapSize() < coords[Y2]
				* map.getMapSize() + coords[X2] + map.getMapSize() ? true
				: false;

	}

	/**
	 * Sends update to the upådate listener
	 * 
	 * @param x
	 *            x coord of the node
	 * @param y
	 *            y coord of the node
	 * @param updtNode
	 *            instance of the updated node
	 */
	protected void sendUpdate(int x, int y, MapNode updtNode) {
		if (mUpdtLis != null) {
			mUpdtLis.update(x, y, updtNode);
		}
	}

	public OtpErlangPid getPid() {
		return pid;
	}

	public void setPid(OtpErlangPid pid) {
		this.pid = pid;
	}

	public int[] getCoords() {
		return coords;
	}

	public void setCoords(int[] coords) {
		this.coords = coords;
	}

	public Communicator getmErlCom() {
		return mErlCom;
	}

	public void setmErlCom(Communicator mErlCom) {
		this.mErlCom = mErlCom;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public UpdateListener getmUpdtLis() {
		return mUpdtLis;
	}

	public void setmUpdtLis(UpdateListener mUpdtLis) {
		this.mUpdtLis = mUpdtLis;
	}
	
}
