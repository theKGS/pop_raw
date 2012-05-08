package raw.java.gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import raw.java.map.Map;

/*
 * Listener for the stop button.
 */

public class AL_StopButton implements ActionListener{
	private Map mp;
	
	public AL_StopButton(Map map) {
		mp = map;
	}

	/**
	 * Called by the stop button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mp.stop();
		System.out.println("STOP");
	}
}
