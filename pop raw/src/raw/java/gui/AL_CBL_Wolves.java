package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A listener class for listening to check box changes.
 * @author andreas
 *
 */
public class AL_CBL_Wolves implements ItemListener {
	private MapPanel mPanel;	
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the check box will be linked to.
	 */
	public AL_CBL_Wolves(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Tells the MapPanel instance to hide/unhide wolves on the display.
	 * @param e an event passed by the ItemSelectable.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {		
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mPanel.setWolvesVisibility(false);
			} else {
				mPanel.setWolvesVisibility(true);
			}
		mPanel.repaint();
	}
}
