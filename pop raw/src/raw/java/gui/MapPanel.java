package raw.java.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MapPanel extends JPanel {
	public MapPanel() {
		setBackground(Color.gray);
		setPreferredSize(new Dimension(600, 600));
	}

	/*
	 * public void paint(Graphics g) { super.paint(g); }
	 */

	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D)g;
    	//drawing messages sent to g2d
    }
}