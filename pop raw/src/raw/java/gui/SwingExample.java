package raw.java.gui;

import javax.swing.*;

import raw.java.j_int_java.Communicator;
import raw.java.map.Map;
import java.awt.*;

/**
 * 
 * @author andreas
 *
 */
public class SwingExample implements Runnable {
	static Communicator messageCommunicator;    
   	static gfxInterfaceMap absMap;
   	static Map map;
   	
	/**
	 * 
	 */
	@Override
    public void run() {
        // Create the window
        JFrame controlFrame = new JFrame ("Rabbits & Wolves");
        JFrame mapFrame = new JFrame ("Map");
        
        controlFrame.setLayout(null);
        mapFrame.setLayout(null);
        
        controlFrame.setBounds(100, 100, 220, 220);
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
        
        mapDisplay mD = new mapDisplay();
        mapFrame.add(mD);
        
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
        
        controlFrame.getContentPane().add(cBoxWolves);
        controlFrame.getContentPane().add(cBoxRabbits);
        controlFrame.getContentPane().add(cBoxGrass);

        /*
         * Add ActionListeners to all GUI elements.
         */
        rawButtonStart.addActionListener(new AL_StartButton(map));
        rawButtonStop.addActionListener(new AL_StopButton(map));
        rawButtonReset.addActionListener(new AL_ResetButton(map));
        jSl.addChangeListener(new AL_TimeSlider(jSl));
        
        // This is just a test button
        mapFrame.getContentPane().add(tButton);
        
        controlFrame.setVisible(true);
        mapFrame.setVisible(true);
    }

	/**
	 * 
	 * @param args
	 */
	
    public static void main(String[] args) {
    	SwingExample se = new SwingExample();
        //Graphics2D gfx2D = new Graphics2D();
    	
    	absMap = new gfxInterfaceMap(32, 32);
        messageCommunicator = new Communicator();

        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(se);
    }
}