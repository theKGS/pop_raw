package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A listener class for listening to check box changes.
 * @author andreas
 *
 */
public class AL_CBL_Rabbits implements ItemListener {
	private MapPanel mPanel;	
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the check box will be linked to.
	 */
	public AL_CBL_Rabbits(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Tells the MapPanel instance to hide/unhide rabbits on the display.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {		
		System.out.println("Rabbit button pressed");
		
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mPanel.setRabbitsVisibility(false);
			} else {
				mPanel.setRabbitsVisibility(true);
			}

		mPanel.repaint();
	}
}
