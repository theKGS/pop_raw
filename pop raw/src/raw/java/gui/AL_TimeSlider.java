package raw.java.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AL_TimeSlider implements ChangeListener{
	JSlider slider;
	
	public AL_TimeSlider(JSlider jsl){
		slider = jsl;
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		System.out.println(slider.getValue()+" ");
	}
}