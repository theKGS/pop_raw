package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A listener class for listening to check box changes.
 * @author andreas
 *
 */
public class AL_CBL_Grass implements ItemListener {
	private MapPanel mPanel;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the check box will be linked to.
	 */
	public AL_CBL_Grass(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Tells the MapPanel instance to hide/unhide grass on the display.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {		
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mPanel.setGrassVisibility(false);
			} else {
				mPanel.setGrassVisibility(true);
			}
		
		mPanel.repaint();
	}
}