package raw.java.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import raw.java.map.Map;

/**
 * A listener for the zoom slider.
 * @author andreas
 */

public class AL_ZoomSlider implements ChangeListener{
	MapPanel mPanel;
	JSlider slider;
	
	/**
	 * Constructor. Is passed a reference to a MapPanel.
	 * @param mp  the MapPanel the JSlider will be linked to.
	 */	
	public AL_ZoomSlider(MapPanel mp, JSlider jsl){
		mPanel = mp;
		slider = jsl;
	}
	
	/**
	 * Called by the JSlider when the value changes.
	 * Updates the MapPanel with the new zoom value.
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		mPanel.setZoom(slider.getValue());
		mPanel.repaint();
	}
}