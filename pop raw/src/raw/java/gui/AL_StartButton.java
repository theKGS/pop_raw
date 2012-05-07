package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class AL_StartButton implements ActionListener{
	private Communicator communicator;
	
	public AL_StartButton(Communicator messageCommunicator) {
		communicator = messageCommunicator;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		communicator.send(new Message("start", null));
		System.out.println("START");
	}
}