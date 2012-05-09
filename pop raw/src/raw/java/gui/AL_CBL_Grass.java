package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AL_CBL_Grass implements ItemListener {
	private MapPanel mPanel;
	
	public AL_CBL_Grass(MapPanel mp) {
		mPanel = mp;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {		
		System.out.println("Grass button pressed");
		
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mPanel.setGrassVisibility(false);
			} else {
				mPanel.setGrassVisibility(true);
			}
		
		mPanel.repaint();
	}
}