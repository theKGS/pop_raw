package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the start button
 */

public class AL_StartButton implements ActionListener{
	private MapPanel mPanel;
	
	public AL_StartButton(MapPanel mp) {
		mPanel = mp;
	}
	
	/**
	 * Called by the start button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPanel.getMap().simulationStart();
	}
}