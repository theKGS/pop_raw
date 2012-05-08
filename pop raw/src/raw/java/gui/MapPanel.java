package raw.java.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapPanel extends JPanel {
	private BufferedImage IconZombie;
	private BufferedImage IconDragon;

    public MapPanel() {
       try {                
          IconZombie = ImageIO.read(new File("Dragon3Headed.PNG"));
       } catch (IOException ex) {
            System.err.println("File not found");
       }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(IconZombie, 0, 0, null); // see javadoc for more info on the parameters
    }
}