package raw.java.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

public class ML_MouseListener implements MouseMotionListener, MouseInputListener{
	private MapPanel mPanel;
	private int scrollX;
	private int scrollY;
	
	public ML_MouseListener(MapPanel mp){
		mPanel = mp;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		System.out.println("Mouse vector X: " + (arg0.getX() - scrollX) + ", Y:" + (arg0.getY() - scrollY));
		mPanel.updateMousePosition((arg0.getX() - scrollX), (arg0.getY() - scrollY));
		scrollX = arg0.getX();
		scrollY = arg0.getY();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		//System.out.println("The mouse was dragged");
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		//System.out.println("Mouse was clicked");		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//System.out.println("Mouse was pressed");
		scrollX = arg0.getX();
		scrollY = arg0.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//System.out.println("Mouse was released");
	}
}