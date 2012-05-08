package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the start button
 */

public class AL_StartButton implements ActionListener{
	private Map mp;
	
	public AL_StartButton(Map map) {
		mp = map;
	}
	
	/**
	 * Called by the start button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mp.simulationStart();
		System.out.println("START");
	}
}