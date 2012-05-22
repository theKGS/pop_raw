package raw.java.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import raw.java.map.Map;

/*
 * A listener for the speed slider.
 */

public class AL_TimeSlider implements ChangeListener{
	private MapPanel mPanel;
	private JSlider slider;
	
	public AL_TimeSlider(MapPanel mp, JSlider jsl){
		mPanel = mp;
		slider = jsl;
	}
	
	/**
	 * Called by the JSlider when the value changes
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		mPanel.getMap().setSimulationSpeed(slider.getValue());
	}
}