package raw.java.map;

import java.util.ArrayList;
import java.util.Random;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class FakeMsgSender extends Thread {
	private Communicator mErlCom;



	private Random r;
	private Map map;
	private int mapSize;

	public FakeMsgSender(Communicator erlCom, Map map) {
		this.mErlCom = erlCom;
		this.mapSize = map.getMapSize();
		this.map = map;
		r = new Random();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int x = 0; x < mapSize; x++) {
				for (int y = 0; y < mapSize; y++) {
					if (map.getMapArray()[x][y].getType() != 0) {
						int dir = r.nextInt(4);
						int hunger = r.nextInt(6);
						int age = r.nextInt(60);
						switch (dir) {
						case 0:
							if (x > 0) {
								mErlCom.putReceive(new Message("move", null,
										new int[] {age, hunger, x, y, x - 1, y }));
							}
							break;
						case 1:
							if (y > 0) {

								mErlCom.putReceive(new Message("move", null,
										new int[] {age, hunger, x, y, x, y - 1 }));
							}
							break;
						case 2:
							if (x < mapSize - 1) {

								mErlCom.putReceive(new Message("move", null,
										new int[] {age, hunger, x, y, x + 1, y }));
							}
							break;
						case 3:
							if (y < mapSize - 1) {
								mErlCom.putReceive(new Message("move", null,
										new int[] {age, hunger, x, y, x, y + 1 }));
							}
							break;
						default:
							break;

						}
					}
					
				}
			}
		}
	}
}
