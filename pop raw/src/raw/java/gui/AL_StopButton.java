package raw.java.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the stop button.
 */

public class AL_StopButton implements ActionListener{
	private MapPanel mPanel;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the JButton will be linked to.
	 */
	public AL_StopButton(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Called by the JButton button.
	 * Stops the simulation if it has been paused.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPanel.getMap().simulationStop();
	}
}
