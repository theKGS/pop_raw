package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/**
 * Listener for the start button which is responsible for unpausing the simulation
 * @author andreas
 */
public class AL_StartButton implements ActionListener{
	private MapPanel mPanel;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the JButton will be linked to.
	 */
	public AL_StartButton(MapPanel mp) {
		mPanel = mp;
	}
	
	/**
	 * Called by the JButton button.
	 * Starts the simulation if it has been paused.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPanel.getMap().simulationStart();
	}
}