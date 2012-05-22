package raw.java.gui;

import raw.java.map.MapNode;

/**
 * Interface to make an object listen to update events from a Map.
 * @author andreas
 *
 */
public interface UpdateListener {
	/**
	 * Invoked when an update needs to be done to the MapPanel.
	 */
	void update(int x, int y, MapNode mn);
}
