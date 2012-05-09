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

	private boolean VisibleWolves;
	private boolean VisibleRabbits;
	private boolean VisibleGrass;

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
		// g.drawImage(IconRabbit, 0, 0, null); // see javadoc for more info on
		// g.drawImage(IconRabbit, 32, 32, null); // see javadoc for more info
		// on
		MapNode[][] nodes = map.getMapArray();
		Color c = new Color(0, 0, 0);

		for (int y = 0; y < map.getMapSize() - 1; y++) {
			for (int x = 0; x < map.getMapSize() - 1; x++) {
				// g.drawImage(IconRabbit, x*TILEWIDTH,y*TILEHEIGHT , null); //
				// see javadoc for more info on

				// Draw grass
				if (nodes[x][y].getType() == MapNode.NONE) {
					if (VisibleGrass) {
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					} else {
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					}
				}

				// Draw wolves
				if (nodes[x][y].getType() == MapNode.WOLF) {
					if (VisibleWolves) {
						c = new Color(255, 0, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					} else if (VisibleGrass) { // draw grass instead of wolves
												// when wolves are
						// hidden
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					} else { // draw nothing if grass and wolves are hidden
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					}
				}

				// Draw rabbits
				if (nodes[x][y].getType() == MapNode.RABBIT) {
					if (VisibleRabbits) {
						c = new Color(0, 0, 255);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					} else if (VisibleGrass) { // draw grass instead of rabbits
												// when rabbits are
						// hidden and grass is visible
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					} else { // draw nothing if grass and rabbits are hidden
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect(x * TILEWIDTH, y * TILEHEIGHT, TILEWIDTH,
								TILEHEIGHT);
					}
				}
			}
		}
	}

	public void setWolvesVisibility(boolean b) {
		VisibleWolves = b;
	}

	public void setRabbitsVisibility(boolean b) {
		VisibleRabbits = b;
	}

	public void setGrassVisibility(boolean b) {
		VisibleGrass = b;
	}
}