package raw.java.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapPanel extends JPanel {
	static int TILEWIDTH = 16;
	static int TILEHEIGHT = 16;
	
	private BufferedImage IconRabbit;
	private BufferedImage IconWolf;

	public MapPanel() {
		try {
			IconWolf = ImageIO.read(new File("Dragon3Headed.PNG"));
			IconRabbit = ImageIO.read(new File("Zombie.PNG"));
		} catch (IOException ex) {
			System.err.println("File not found");
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(IconRabbit, 0, 0, null); // see javadoc for more info on
		g.drawImage(IconRabbit, 32, 32, null); // see javadoc for more info on
		// the parameters
//		g.drawRect(20, 10, 100, 60);
//		g.drawRect(30, 20, 110, 65);
//		g.drawRect(40, 30, 120, 70);
		// g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
	}
}