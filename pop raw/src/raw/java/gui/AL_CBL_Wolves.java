package raw.java.gui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class AL_CBL_Wolves implements ItemListener {
	private MapPanel mPanel;	
	
	public AL_CBL_Wolves(MapPanel mp) {
		mPanel = mp;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {		
		System.out.println("ButtonChange");
		
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mPanel.setWolvesVisibility(false);
			} else {
				mPanel.setWolvesVisibility(true);
			}

		mPanel.repaint();
		//mPanel.invalidate();
		//mPanel.getParent().validate();
	}
}
