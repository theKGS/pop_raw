package raw.java.gui;

import javax.swing.*;
import raw.java.j_int_java.Communicator;
import raw.java.map.Map;
import raw.java.map.MapNode;

/**
 * 
 * @author andreas
 *
 */
public class Main implements Runnable, UpdateListener{    
	static Map map;
	private MapPanel mapDisplayPanel;
    private JTextField textFieldSize;
    private JTextField textFieldSeed;
	
	/**
	 * The Swing thread
	 */
	@Override public void run() {
        // Create the window
        JFrame controlFrame = new JFrame ("Rabbits & Wolves");
        JFrame mapFrame = new JFrame ("Map");
        
        controlFrame.setLayout(null);
        //mapFrame.setLayout(null);
        
        controlFrame.setBounds(100, 100, 208, 320);
        mapFrame.setBounds(300, 100, 600, 600);

        // Sets the behavior for when the window is closed
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        /*
         * Start, stop, reset -buttons
         */
        JButton rawButtonStart = new JButton("Start");
        rawButtonStart.setToolTipText("Starts simulation");
        JButton rawButtonStop = new JButton("Stop");
        rawButtonStop.setToolTipText("Stops simulation");
        JButton rawButtonReset = new JButton("Reset");
        rawButtonReset.setToolTipText("Resets simulation");

        rawButtonStart.setLocation(0, 0);
        rawButtonStart.setSize(200, 20);
        rawButtonStop.setLocation(0, 20);
        rawButtonStop.setSize(200, 20);
        rawButtonReset.setLocation(0, 40);
        rawButtonReset.setSize(200, 20);
        
        JButton tButton = new JButton("Temporary");
        tButton.setLocation(10, 10);
        tButton.setSize(40, 40);
        
        /*
         * Updates-per-minute slider
         */
        JSlider jSl = new JSlider();
        jSl.setLabelTable(jSl.createStandardLabels(30));
        jSl.setPaintLabels(true);
        jSl.setPaintTicks(true);
        jSl.setBounds(0, 70, 200, 70);        
        jSl.setMaximum(120);
        jSl.setMinimum(0);
        jSl.setMajorTickSpacing(10);
        jSl.setPaintTicks(true);
        
        JSlider zoomSlider = new JSlider();
        zoomSlider.setBounds(0, 200, 200, 70);
        zoomSlider.setMaximum(64);
        zoomSlider.setMinimum(1);  
        zoomSlider.setValue(7);
        
        mapDisplayPanel = new MapPanel(map);
        mapDisplayPanel.setBounds(0,0,400,400);
        
        mapFrame.add(mapDisplayPanel);
       // mapFrame.pack();
        
        /*
         * Text fields
         */
        JTextField textFieldSize = new JTextField();
        JTextField textFieldSeed = new JTextField();
        textFieldSize.setBounds(16, 250, 64, 24);
        textFieldSeed.setBounds(116, 250, 64, 24);
        
        controlFrame.getContentPane().add(textFieldSize);
        controlFrame.getContentPane().add(textFieldSeed);
        
        /*
         * Show/hide map elements 
         */
        JCheckBox cBoxWolves = new JCheckBox();
        cBoxWolves.setBounds(0, 140, 200, 20);
        cBoxWolves.setText("Hide wolves");
        cBoxWolves.setToolTipText("Hide wolves in the display");
        JCheckBox cBoxRabbits = new JCheckBox();
        cBoxRabbits.setBounds(0, 160, 200, 20);
        cBoxRabbits.setText("Hide rabbits");
        cBoxRabbits.setToolTipText("Hide rabbits in the display");
        JCheckBox cBoxGrass = new JCheckBox();
        cBoxGrass.setBounds(0, 180, 200, 20);
        cBoxGrass.setText("Hide grass");
        cBoxGrass.setToolTipText("Hide grass in the display");
        
        /*
         * Add all components to controlFrame and mapFrame
         */
        controlFrame.getContentPane().add(new JLabel("Rabbits & Wolves"));
        controlFrame.getContentPane().add(rawButtonStart);
        controlFrame.getContentPane().add(rawButtonStop);
        controlFrame.getContentPane().add(rawButtonReset);
        controlFrame.getContentPane().add(jSl);
        controlFrame.getContentPane().add(zoomSlider);
        
        controlFrame.getContentPane().add(cBoxWolves);
        controlFrame.getContentPane().add(cBoxRabbits);
        controlFrame.getContentPane().add(cBoxGrass);

        /*
         * Add ActionListeners to all GUI elements.
         */
        rawButtonStart.addActionListener(new AL_StartButton(map));
        rawButtonStop.addActionListener(new AL_StopButton(map));
        rawButtonReset.addActionListener(new AL_InitButton(map));
        jSl.addChangeListener(new AL_TimeSlider(map, jSl));
        zoomSlider.addChangeListener(new AL_ZoomSlider(mapDisplayPanel, zoomSlider));
        cBoxWolves.addItemListener(new AL_CBL_Wolves(mapDisplayPanel));
        cBoxRabbits.addItemListener(new AL_CBL_Rabbits(mapDisplayPanel));
        cBoxGrass.addItemListener(new AL_CBL_Grass(mapDisplayPanel));
        
        /*
         * Set frame specifics. Visibility, resizeability.        
         */
        controlFrame.setResizable(false);
        controlFrame.setVisible(true);
        mapFrame.setVisible(true);
       
        mapDisplayPanel.setWolvesVisibility(true);
        mapDisplayPanel.setRabbitsVisibility(true);
        mapDisplayPanel.setGrassVisibility(true);
    }

	/**
	 * Main method
	 * @param args
	 */
    public static void main(String[] args) {
    	Main se = new Main();    	
    	map = new Map(80, 32, se);
    	map.start();
        SwingUtilities.invokeLater(se);
    }
    
    public void update(int x, int y, MapNode mn){
    	mapDisplayPanel.addNode(x, y, mn);
    	mapDisplayPanel.repaint();
    }
    
    public int getSizeFromTextField(){
    	Integer size = 32;
    	size = Integer.getInteger(textFieldSeed.getText());
    	return size;
    }
    
    public int getSeedFromTextField(){
    	Integer seed = 0;
    	seed = Integer.getInteger(textFieldSeed.getText());
    	return seed;
    }
}
