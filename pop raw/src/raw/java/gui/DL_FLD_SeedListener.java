package raw.java.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This is a listener for the Seed text field.
 * @author andreas
 *
 */
public class DL_FLD_SeedListener implements DocumentListener {

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("change update");
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("insert update");
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("remove update");
		
	}

}
