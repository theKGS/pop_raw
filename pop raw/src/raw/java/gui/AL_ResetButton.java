package raw.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

/**
 * Listener for the reset button.
 * 
 * @author andreas
 */
public class AL_ResetButton implements ActionListener {
	private MapPanel mPanel;
	private JTextField textFieldSize; // Text field for map size input
	private JTextField textFieldSeed; // Text field for seed input
	private JTextField textFieldMaster; // Text field for input of other
										// parameters

	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * 
	 * @param mp
	 *            the MapPanel the JButton will be linked to.
	 * @param tm
	 */
	public AL_ResetButton(MapPanel mp, JTextField sz, JTextField ss,
			JTextField tm) {
		mPanel = mp;
		textFieldSize = sz;
		textFieldSeed = ss;
		textFieldMaster = tm;
	}

	/**
	 * Called by the JButton button. Resets the simulation with new seed and new
	 * size.
	 * 
	 * @param arg0
	 *            an event generated by the button this listener is listening
	 *            on.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		mPanel.getMap().simulationResetStop();
		/*
		 * Attempts to retrieve a size from the size text field. If that fails
		 * it uses a default size as configured here.
		 */
		String txtStrng;
		
		Integer size;
		txtStrng = textFieldSize.getText();
		if (txtStrng != null) {
			if (txtStrng.length() > 0) {
				try {
					size = Integer.parseInt(textFieldSize.getText());
					mPanel.getMap().setMapSize(size);
					System.out.println("size: " + size);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		} else {
			mPanel.getMap().setMapSize(25);
		}

		/*
		 * Attempts to retrieve a seed from the seed text field. If that fails
		 * it uses a default seed as configured here.
		 */
		Integer seed;
		txtStrng = textFieldSeed.getText();
		if (txtStrng != null) {
			if (txtStrng.length() > 0) {
				try {
					seed = Integer.parseInt(textFieldSeed.getText());
					mPanel.getMap().setSeed(seed);
					System.out.println("seed: " + seed);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		} else {
			mPanel.getMap().setSeed(0);
		}

		/*
		 * Attempts to retrieve all values from textFieldMaster
		 */
		String fldPrms = textFieldMaster.getText();
		String params[] = fldPrms.split(" ");

		/*
		 * if (params.length != 5) {
		 * System.err.println("Incorrect or missing parameters"); } else {
		 */

		for (String h : params) {
			System.out.print(h);
		}

		mPanel.getMap().setWolfReprAge(Integer.parseInt(params[0]));
		mPanel.getMap().setWoldReprSuccessProb(Integer.parseInt(params[1]));
		mPanel.getMap().setRappitReprAge(Integer.parseInt(params[2]));
		mPanel.getMap().setRabbitReprSuccessProb(Integer.parseInt(params[3]));
		mPanel.getMap().setSpeedOfGrassGrowth(Integer.parseInt(params[4]));
		// }

		mPanel.getMap().simulationReset();
		mPanel.resetMapSize();
		mPanel.repaint();
	}
}