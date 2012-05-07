package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;

/*
 * Listener for the reset button
 */

public class AL_ResetButton implements ActionListener{	
	private Map mp;
	
	public AL_ResetButton(Map map) {
		mp = map;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		mp.reset();
		System.out.println("RESET");
	}
}