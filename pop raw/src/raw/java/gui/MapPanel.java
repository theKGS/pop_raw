package raw.java.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import raw.java.map.Map;
import raw.java.map.MapNode;

public class MapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static int TILEWIDTH = 12;
	static int TILEHEIGHT = 12;
	
	private BufferedImage IconRabbit;
	private BufferedImage IconWolf;
	private Map map;
	
	public MapPanel(Map map) {
		try {
			IconWolf = ImageIO.read(new File("Dragon3Headed.PNG"));
			IconRabbit = ImageIO.read(new File("Zombie.PNG"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.map = map;
	}

	/**
	 * This draws the graphics
	 */
	@Override
	public void paintComponent(Graphics g) {
		//g.drawImage(IconRabbit, 0, 0, null); // see javadoc for more info on
		//g.drawImage(IconRabbit, 32, 32, null); // see javadoc for more info on
		MapNode[][] nodes = map.getMapArray();
		Color c = new Color(0,0,0);
		
		for (int y = 0; y < map.getMapSize()-1; y++){
			for (int x = 0; x < map.getMapSize()-1; x++){
				//g.drawImage(IconRabbit, x*TILEWIDTH,y*TILEHEIGHT , null); // see javadoc for more info on
				
				if (nodes[x][y].getType() == MapNode.NONE){
					c = new Color(0,nodes[x][y].getGrassLevel()*50,0);
					g.setColor(c);
					g.fillRect(x*TILEWIDTH,y*TILEHEIGHT,TILEWIDTH,TILEHEIGHT);
				}
				
				if (nodes[x][y].getType() == MapNode.WOLF){
					c = new Color(110,90,90);
					g.setColor(c);
					g.fillRect(x*TILEWIDTH,y*TILEHEIGHT,TILEWIDTH,TILEHEIGHT);
				}

				if (nodes[x][y].getType() == MapNode.RABBIT){
					c = new Color(220,220,200);
					g.setColor(c);
					g.fillRect(x*TILEWIDTH,y*TILEHEIGHT,TILEWIDTH,TILEHEIGHT);
				}
				
				//g.setColor(c);
				//g.drawRect(x*TILEWIDTH,y*TILEHEIGHT,TILEWIDTH-1,TILEHEIGHT-1);
			}
		}
		
		// the parameters
//		g.drawRect(20, 10, 100, 60);
//		g.drawRect(30, 20, 110, 65);
//		g.drawRect(40, 30, 120, 70);
		// g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
	}
}