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
		// TODO Auto-generated method stub
		Document d = e.getDocument();
		try {
			String t = d.getText(0, d.getLength());
			System.out.println(t);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		Document d = e.getDocument();
		try {
			String t = d.getText(0, d.getLength());
			System.out.println(t);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
	}

}
