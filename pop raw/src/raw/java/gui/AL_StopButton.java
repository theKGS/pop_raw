package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;

public class AL_StopButton implements ActionListener{
	private Map mp;
	
	public AL_StopButton(Map map) {
		mp = map;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		mp.stop();
		System.out.println("STOP");
	}
}
