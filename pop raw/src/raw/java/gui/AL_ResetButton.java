package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class AL_ResetButton implements ActionListener{	
	private Communicator communicator;
	
	public AL_ResetButton(Communicator messageCommunicator) {
		communicator = messageCommunicator;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		communicator.send(new Message("reset", null));
		System.out.println("RESET");
	}
}