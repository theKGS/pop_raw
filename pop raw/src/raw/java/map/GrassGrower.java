package raw.java.map;

import raw.java.gui.UpdateListener;

/**
 * 
 * @author Johan Runnable that makes the grass grow
 */
public class GrassGrower extends Thread {
	Map map;
	long growthRate;
	public boolean running = false;
	private UpdateListener mUpdtLis;

	/**
	 * Cunstructor for the GrassGrower
	 * 
	 * @param map
	 *            The map object that created the grass grower.
	 * @param growthRate
	 *            The growth rate of the grass 1000 means grass will grow every
	 *            1000ms
	 * @param mUpdtLis
	 *            update listener from map, grass grower will signal the uodate
	 *            listener when grass has grown.
	 */
	public GrassGrower(Map map, long growthRate, UpdateListener mUpdtLis) {
		this.map = map;
		this.growthRate = growthRate;
		this.mUpdtLis = mUpdtLis;
	}

	@Override
	public void run() {
	    running = true;
		while (running) {
		    if(Map.paused){
		        continue;
		    }
			for (int i = 0; i < map.getMapSize(); i++) {
				for (int j = 0; j < map.getMapSize(); j++) {
					synchronized (map.getMapArray()[i][j]) {
						MapNode mn = map.getMapArray()[i][j];
						if (mn.getGrassLevel() < 5
								&& mn.getType() == MapNode.NONE) {
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
