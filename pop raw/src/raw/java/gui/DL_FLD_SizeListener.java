package raw.java.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * This is a listener for the Size text field.
 * @author andreas
 *
 */
public class DL_FLD_SizeListener implements DocumentListener{
	private MapPanel mPanel; 
	
	public DL_FLD_SizeListener(MapPanel mp){
		mPanel = mp;
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}

}
