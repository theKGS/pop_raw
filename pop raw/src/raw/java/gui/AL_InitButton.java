package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the reset button
 */
public class AL_InitButton implements ActionListener{	
	private MapPanel mPanel;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the check box will be linked to.
	 */
	public AL_InitButton(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Called by the init button
	 * Resets simulation with new seed.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//mPanel.getMap().simulationReset();
		mPanel.newMap(80, 99);
		mPanel.repaint();
	}
}