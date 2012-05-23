package raw.java.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import javax.swing.JPanel;

import raw.java.map.Map;
import raw.java.map.MapNode;

/**
 * MapPanel object used to display the map.
 * @author andreas
 *
 */
public class MapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	static int TILEWIDTH = 16; // width of a tile
	static int TILEHEIGHT = 16; // height of a tile
	private int SIZE = 16;

	private int scrollCoordinateX;
	private int scrollCoordinateY;

	private boolean VisibleWolves;
	private boolean VisibleRabbits;
	private boolean VisibleGrass;

	// private BufferedImage IconRabbit;
	// private BufferedImage IconWolf;
	private Map map;

	private MapNode[][] nodes;
	private UpdateListener mainRef;
	private int defaultSize = 25;

	/**
	 * Constructor.
	 */
	public MapPanel() {
		/*
		 * try { //IconWolf = ImageIO.read(new File("Dragon3Headed.PNG"));
		 * //IconRabbit = ImageIO.read(new File("Zombie.PNG")); } catch
		 * (IOException ex) { ex.printStackTrace(); }
		 */

		// creates a mouse listener
		ML_MouseListener ml = new ML_MouseListener(this);
		this.addMouseMotionListener(ml);
		this.addMouseListener(ml);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Draws the component.
	 * @param g  the Graphics object to use for drawing.
	 */
	@Override
	public void paintComponent(Graphics g) {
		// g.drawImage(IconRabbit, 0, 0, null); // see javadoc for more info on
		// g.drawImage(IconRabbit, 32, 32, null); // see javadoc for more info
		Color c = new Color(15, 0, 15);
		// g.drawImage(IconRabbit, 0, 0, 48, 48, new Color (255, 0, 255), null);

		g.setColor(c);
		g.fillRect(0, 0, 700, 700);

		c = new Color(255, 255, 0);

		float divW = this.getParent().getWidth() / 2;

		g.setColor(c);

		for (int y = 0; y < map.getMapSize(); y++) {
			for (int x = 0; x < map.getMapSize(); x++) {
				// Draw grass
				if (nodes[x][y].getType() == MapNode.NONE) {
					if (VisibleGrass) {
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					} else {
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					}
				}
				// Draw wolves
				if (nodes[x][y].getType() == MapNode.WOLF) {
					if (VisibleWolves) {
						c = new Color(255, 0, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					} else if (VisibleGrass) {
						// draw grass instead of
						// wolves when wolves are hidden
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					} else { // draw nothing if grass and wolves are hidden
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					}
				}
				// Draw rabbits
				if (nodes[x][y].getType() == MapNode.RABBIT) {
					if (VisibleRabbits) {
						c = new Color(0, 0, 255);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					} else if (VisibleGrass) {
						// draw grass instead of rabbits
						// when rabbits are hidden and grass is visible
						c = new Color(0, nodes[x][y].getGrassLevel() * 40, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					} else { // draw nothing if grass and rabbits are hidden
						c = new Color(0, 0, 0);
						g.setColor(c);
						g.fillRect((int) (x * SIZE + divW + scrollCoordinateX),
								(int) (y * SIZE + divW + scrollCoordinateY),
								SIZE, SIZE);
					}
				}

			}
		}
	}

	/**
	 * Sets visibility of wolves in the simulation on or off.
	 * @param b  value to set the visibility to.
	 */
	public void setWolvesVisibility(boolean b) {
		VisibleWolves = b;
	}

	/**
	 * Sets visibility of rabbits in the simulation on or off.
	 * @param b  value to set the visibility to.
	 */
	public void setRabbitsVisibility(boolean b) {
		VisibleRabbits = b;
	}

	/**
	 * Sets visibility of grass in the simulation on or off.
	 * @param b  value to set the visibility to.
	 */
	public void setGrassVisibility(boolean b) {
		VisibleGrass = b;
	}

	/**
	 * Sets zoom value.
	 * @param b  value to set the zoom to.
	 */
	public void setZoom(int size) {
		SIZE = size;
	}

	/**
	 * Adds a MapNode to the display with the given coordinates.
	 * @param x  position at which to add the MapNode.
	 * @param y  position at which to add the MapNode.
	 * @param mn  the node to add to the MapPanel display
	 */
	public void addNode(int x, int y, MapNode mn) {
		nodes[x][y] = mn;
	}

	/**
	 * Retrieves a reference to the map.
	 * @return  the map reference held.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * Creates a new map to replace the old using the given parameters.
	 * @param size  size of the new map.
	 * @param seed  seed for the random generator.
	 */
	public void newMap(int size, int seed) {
		map = new Map(size, seed, mainRef);
		nodes = map.getMapArray();
	}

	/**
	 * Sets an UpdateListener. It is not used directly by the MapPanel, but passed on to the Map.
	 * @param mref  the UpdateListener.
	 */
	public void setUpdateListener(UpdateListener mref) {
		mainRef = mref;
	}

	/**
	 * Moves the display inside the MapPanel. 
	 * @param x  amount of steps to move horizontally.
	 * @param y  amount of steps to move vertically.
	 */
	public void updateMousePosition(int x, int y) {
		this.scrollCoordinateX += x;
		this.scrollCoordinateY += y;
		this.repaint();
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}
}