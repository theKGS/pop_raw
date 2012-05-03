package raw.java.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/*
 * This is a tutorial for Swing.
 */


public class SwingExample implements Runnable {
    @Override
    public void run() {
        // Create the window
        JFrame f = new JFrame ("Hello, World!");
        f.setLayout(null);
        
        f.setBounds(200, 200, 500, 500);

        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // add buttons
        JButton aB = new JButton("Press me!");
        aB.setToolTipText("This button is totally awesome. Seriously.");
        JButton bB = new JButton("Press me too!");
        bB.setToolTipText("This button is just as awesome. Trust me!");
        aB.setLocation(0, 0);
        aB.setSize(200, 20);
        bB.setLocation(0, 20);
        bB.setSize(200, 20);
        
        // add JSlider
        JSlider jSl = new JSlider();
        jSl.setPaintTicks(true);
        jSl.setBounds(0, 60, 20, 200);        
        jSl.setSnapToTicks(true);
        jSl.setMaximum(50);
        jSl.setMinimum(0);
        jSl.setMajorTickSpacing(10);
        jSl.setPaintTicks(true);
        jSl.setOrientation(SwingConstants.VERTICAL);
        
        //JList b;
        
        
        f.getContentPane().add(new JLabel("Hello, world!"));
        f.getContentPane().add(aB);
        f.getContentPane().add(bB);
        f.getContentPane().add(jSl);
        
        //f.validate();
        // arrange the components inside the window
        //f.pack();
        //By default, the window is not visible. Make it visible.
        f.setVisible(true);
    }
 
    public static void main(String[] args) {
        SwingExample se = new SwingExample();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(se);
    }
}
