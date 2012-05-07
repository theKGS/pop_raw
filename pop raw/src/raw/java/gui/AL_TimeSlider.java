package raw.java.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import raw.java.map.Map;

/*
 * A listener for the speed slider.
 */

public class AL_TimeSlider implements ChangeListener{
	Map mp;
	JSlider slider;
	
	public AL_TimeSlider(Map map, JSlider jsl){
		mp = map;
		slider = jsl;
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		//Map.setSpeed(slider.getValue());
		System.out.println(slider.getValue()+" ");
	}
}