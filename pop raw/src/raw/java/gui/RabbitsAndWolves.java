package raw.java.gui;

import java.io.IOException;

import javax.swing.*;

import raw.java.map.Map;
import raw.java.map.MapNode;

/*
 * wolf 
 *   reproduction age, reproduction success probability
 * rabbit
 *   -||-
 * grass
 *   grass growth speed
 *    
 */



/**
 * A application that runs a primitive simulation of rabbits & wolves wherein the 
 * rabbits will eat grass and propagate, the wolves will eat the rabbits and the 
 * grass will grow independently.
 * @author andreas
 *
 */
public class RabbitsAndWolves implements Runnable, UpdateListener{    
	static Map map;
	private static RabbitsAndWolves se;
	private static MapPanel mapDisplayPanel;
    private JTextField textFieldSize; // Text field for map size input
    private JTextField textFieldSeed; // Text field for seed input
    private JTextField textFieldMaster; //
    
	/**
	 * The Swing thread
	 */
	@Override
	public void run() {
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
        
        JSlider zoomSlider = new JSlider();
        zoomSlider.setBounds(0, 130, 200, 50);
        zoomSlider.setMaximum(64);
        zoomSlider.setMinimum(1);  
        zoomSlider.setValue(7);
        
        mapDisplayPanel.setBounds(0,0,400,400);
        mapFrame.add(mapDisplayPanel);
        
        /*
         * Text fields
         */
        textFieldSize = new JTextField();
        textFieldSeed = new JTextField();
        textFieldMaster = new JTextField();
        textFieldSize.setBounds(16, 170, 64, 24);
        textFieldSeed.setBounds(116, 170, 64, 24);
        textFieldMaster.setBounds(16, 200, 168, 24);
        textFieldSize.setToolTipText("Size of map on reset");
        textFieldSeed.setToolTipText("Seed for randomizer on reset");
        textFieldMaster.setToolTipText("'WRA WRSP RRA RRSP GGS', updated on reset");
        textFieldMaster.setText("4 0 10 300 5000");
        
        controlFrame.getContentPane().add(textFieldSize);
        controlFrame.getContentPane().add(textFieldSeed);
        controlFrame.getContentPane().add(textFieldMaster);
        /*
         * Show/hide map elements 
         */
        JCheckBox cBoxWolves = new JCheckBox();
        cBoxWolves.setBounds(0, 70, 200, 20);
        cBoxWolves.setText("Hide wolves");
        cBoxWolves.setToolTipText("Hide wolves in the display");
        JCheckBox cBoxRabbits = new JCheckBox();
        cBoxRabbits.setBounds(0, 90, 200, 20);
        cBoxRabbits.setText("Hide rabbits");
        cBoxRabbits.setToolTipText("Hide rabbits in the display");
        JCheckBox cBoxGrass = new JCheckBox();
        cBoxGrass.setBounds(0, 110, 200, 20);
        cBoxGrass.setText("Hide grass");
        cBoxGrass.setToolTipText("Hide grass in the display");
        
        /*
         * Add all components to controlFrame and mapFrame
         */
        controlFrame.getContentPane().add(new JLabel("Rabbits & Wolves"));
        controlFrame.getContentPane().add(rawButtonStart);
        controlFrame.getContentPane().add(rawButtonStop);
        controlFrame.getContentPane().add(rawButtonReset);
        controlFrame.getContentPane().add(zoomSlider);
        
        controlFrame.getContentPane().add(cBoxWolves);
        controlFrame.getContentPane().add(cBoxRabbits);
        controlFrame.getContentPane().add(cBoxGrass);

        /*
         * Add ActionListeners to all GUI elements.
         */
        rawButtonStart.addActionListener(new AL_StartButton(mapDisplayPanel));
        rawButtonStop.addActionListener(new AL_StopButton(mapDisplayPanel));
        rawButtonReset.addActionListener(new AL_ResetButton(mapDisplayPanel, textFieldSize, textFieldSeed, textFieldMaster));
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
	 * Main method. Launches the application. Arguments are ignored.
	 * @param args
	 */
    public static void main(String[] args) {
    	se = new RabbitsAndWolves();    	
    	
        mapDisplayPanel = new MapPanel();
        mapDisplayPanel.setUpdateListener(se);
    	mapDisplayPanel.newMap(25,0);
    	mapDisplayPanel.getMap().start();
        SwingUtilities.invokeLater(se);
    }
    
    /**
     * Updates the internal map with a new MapNode at position x y
     * @param x  position of MapNode
     * @param y  position of MapNode
     * @param mn  a new MapNode to place in the internal map
     */
    public void update(int x, int y, MapNode mn){
    	mapDisplayPanel.addNode(x, y, mn);
    	mapDisplayPanel.repaint();
    }
}