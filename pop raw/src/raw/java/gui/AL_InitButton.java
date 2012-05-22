package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/**
 * Listener for the reset button.
 * @author andreas
 */
public class AL_InitButton implements ActionListener{	
	private MapPanel mPanel;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the JButton will be linked to.
	 */
	public AL_InitButton(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Called by the JButton button.
	 * Resets the simulation with new seed and new size.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//mPanel.getMap().simulationReset();
		mPanel.newMap(80, 99);
		mPanel.repaint();
	}
}