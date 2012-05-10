package raw.java.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the stop button.
 */

public class AL_StopButton implements ActionListener{
	private MapPanel mPanel;
	
	public AL_StopButton(MapPanel mp) {
		mPanel = mp;
	}

	/**
	 * Called by the stop button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPanel.getMap().simulationStop();
		System.out.println("STOP");
	}
}
