package raw.java.gui;

import raw.java.map.MapNode;

public interface UpdateListener {
	void update(int x, int y, MapNode mn);
}
