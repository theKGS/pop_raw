package raw.java.gui;

/**
 * JFramePaint2.java
 * Copyright (c) 2002 by Dr. Herong Yang, http://www.herongyang.com/
 */

import java.awt.*;
import javax.swing.*;

public class JFramePaint2 {
   public static void main(String[] a) {
      JFrame f = new JFrame();
      f.setTitle("Drawing Graphics in a Frame"
         +" by Adding a Component");
      f.setBounds(100,50,500,300);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(new MyComponent());
      f.setVisible(true);
   }
   static class MyComponent extends JComponent {
      public void paint(Graphics g) {
         g.drawRect(20,10,100,60);
         g.drawRect(30,20,110,65);
         g.drawRect(40,30,120,70);
         //g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
      }
   }
}