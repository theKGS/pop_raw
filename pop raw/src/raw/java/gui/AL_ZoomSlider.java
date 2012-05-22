package raw.java.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import raw.java.map.Map;

/*
 * A listener for the speed slider.
 */

public class AL_ZoomSlider implements ChangeListener{
	MapPanel mPanel;
	JSlider slider;
	
	public AL_ZoomSlider(MapPanel mp, JSlider jsl){
		mPanel = mp;
		slider = jsl;
	}
	
	/**
	 * Called by the JSlider when the value changes
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		//mp.setSimulationSpeed(slider.getValue());
		mPanel.setZoom(slider.getValue());
		mPanel.repaint();
	}
}