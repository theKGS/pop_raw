package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AL_CBL_Rabbits implements ItemListener {
	private MapPanel mPanel;	
	
	public AL_CBL_Rabbits(MapPanel mp) {
		mPanel = mp;
	}

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
