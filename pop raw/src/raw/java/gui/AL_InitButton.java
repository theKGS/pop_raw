package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the reset button
 */

public class AL_InitButton implements ActionListener{	
	private Map mp;
	
	public AL_InitButton(Map map) {
		mp = map;
	}

	/**
	 * Called by the init button
	 * Resets simulation with new seed.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mp.simulationReset();
		System.out.println("RESET");
	}
}