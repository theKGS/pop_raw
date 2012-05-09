package raw.java.map;

import java.util.ArrayList;
import java.util.Random;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class FakeMsgSender extends Thread {
	private Communicator mErlCom;

	private ArrayList<int[]> mEntities;

	private Random r;

	private int mapSize;

	public FakeMsgSender(Communicator erlCom, ArrayList<int[]> entities,
			int mapSize) {
		this.mErlCom = erlCom;
		this.mEntities = entities;
		this.mapSize = mapSize;
		r = new Random();
	}

	@Override
	public void run() {
		while (true) {
			for (int[] coord : mEntities) {
				int x = coord[0];
				int y = coord[1];
				int dir = r.nextInt(4);
				switch (dir) {
				case 0:
					if (x > 0) {
						coord[0] = x - 1;
						mErlCom.putReceive(new Message("move", null, new int[] {
								x, y, x - 1, y }));
					}
					break;
				case 1:
					if (y > 0) {
						coord[1] = y - 1;
						mErlCom.putReceive(new Message("move", null, new int[] {
								x, y, x, y - 1 }));
					}
					break;
				case 2:
					if (x < mapSize - 1) {
						coord[0] = x + 1;
						mErlCom.putReceive(new Message("move", null, new int[] {
								x, y, x + 1, y }));
					}
					break;
				case 3:
					if (y < mapSize - 1) {
						coord[1] = y + 1;
						mErlCom.putReceive(new Message("move", null, new int[] {
								x, y, x, y + 1 }));
					}
					break;
				default:
					break;

				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
