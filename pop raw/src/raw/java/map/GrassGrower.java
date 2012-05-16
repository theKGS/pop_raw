package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class GrassGrower  implements Runnable {
	Map map;
	long growthRate;
	public boolean running = true;
	private UpdateListener mUpdtLis;
	public GrassGrower(Map map, long growthRate, UpdateListener mUpdtLis) {
		this.map = map;
		this.growthRate = growthRate;
		this.mUpdtLis = mUpdtLis;
	}

	@Override
	public void run() {
		while(running){
			for(int i = 0; i  < map.getMapSize(); i++){
				for(int j = 0; j < map.getMapSize(); j++){
					synchronized (map.getMapArray()[i][j]) {
						MapNode mn = map.getMapArray()[i][j];
						if(mn.getGrassLevel() < 5 && mn.getType() == MapNode.NONE){
							mn.setGrassLevel(mn.getGrassLevel() + 1);
							mUpdtLis.update(i, j, mn);
						}
					}
				}
			}
			try {
				Thread.sleep(growthRate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
