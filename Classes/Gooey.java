/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.imageio.ImageIO;
import java.awt.event.*;

public class Gooey extends JFrame {

  double SCALE_HEIGHT = 0.8;
  double SCALE_WIDTH = 0.8;
  GameManager manager;

  // JLabels
  JLabel boardlabel;
  JLabel cardlabel;
  JLabel playerlabel;
  JLabel mLabel;
  JLabel playerInfo;
  
  //JButtons
  JButton bAct;
  JButton bRehearse;
  JButton bMove;
  JButton bWork;
  JButton bUpgrade;
  JButton bEnd;

  //JComboBox
  JComboBox moveChoices;
  JComboBox roleChoices;
  
  // JLayered Pane
  JLayeredPane bPane;

  //board icon
  ImageIcon icon;
  
  // Constructor
  
  public Gooey() {
    
        // Set the title of the JFrame
       super("Deadwood");
       // Set the exit option for the JFrame
       setDefaultCloseOperation(EXIT_ON_CLOSE);
      
       // Create the JLayeredPane to hold the display, cards, dice and buttons
       bPane = getLayeredPane();
       Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
       bPane.setMaximumSize(screenSize);
    
       // Create the deadwood board
       boardlabel = new JLabel();
       //boardlabel.setSize(bounds);
       this.icon =  new ImageIcon("../Images/board.jpg");
       // set the scale width and height such that everything is scaled to the screen size
       SCALE_HEIGHT*= screenSize.getHeight() / icon.getIconHeight();
       SCALE_WIDTH*= screenSize.getWidth() / icon.getIconWidth();
       Image resizedImage = icon.getImage().getScaledInstance((int)(icon.getIconWidth() * SCALE_WIDTH),(int)(icon.getIconHeight() * SCALE_HEIGHT), Image.SCALE_SMOOTH);
       icon = new ImageIcon(resizedImage);
       boardlabel.setIcon(icon); 
       boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
      
       // Add the board to the lowest layer
       bPane.add(boardlabel, new Integer(0));
      
       // Set the size of the GUI
       setSize(icon.getIconWidth()+200,icon.getIconHeight());
       
       // Add a scene card to this room
       cardlabel = new JLabel();
       ImageIcon cIcon =  new ImageIcon("01.png");
    //    cardlabel.setIcon(cIcon); 
    //    cardlabel.setBounds(20 / WIDTH,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
    //    cardlabel.setOpaque(true);
      
       // Add the card to the lower layer
       bPane.add(cardlabel, new Integer(1));
       
      

    
       // Add a dice to represent a player. 
       // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
       playerlabel = new JLabel();
       ImageIcon pIcon = new ImageIcon("../Images/r2.png");
       playerlabel.setIcon(pIcon);
       this.setBoundsScaled(playerlabel, 114, 227, 46, 46); 
       playerlabel.setVisible(true);
       bPane.add(playerlabel,new Integer(3));
      
       // Create the Menu for action buttons
       mLabel = new JLabel("MENU");
       mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
       bPane.add(mLabel,new Integer(2));

       // Create Action buttons
       bAct = new JButton("ACT");
       bAct.setBackground(Color.white);
       bAct.setBounds(icon.getIconWidth()+10, 30,100, 20);
       bAct.addMouseListener(new boardMouseListener());
       
       bRehearse = new JButton("REHEARSE");
       bRehearse.setBackground(Color.white);
       bRehearse.setBounds(icon.getIconWidth()+10,60,100, 20);
       bRehearse.addMouseListener(new boardMouseListener());
       
       bMove = new JButton("MOVE");
       bMove.setBackground(Color.white);
       bMove.setBounds(icon.getIconWidth()+10,90,100, 20);
       bMove.addMouseListener(new boardMouseListener());

       bWork = new JButton("WORK");
       bWork.setBackground(Color.white);
       bWork.setBounds(icon.getIconWidth()+10, 120,100, 20);
       bWork.addMouseListener(new boardMouseListener());

       bUpgrade = new JButton("UPGRADE");
       bUpgrade.setBackground(Color.white);
       bUpgrade.setBounds(icon.getIconWidth()+10, 150,100, 20);
       bUpgrade.addMouseListener(new boardMouseListener());

       bEnd = new JButton("END TURN");
       bEnd.setBackground(Color.white);
       bEnd.setBounds(icon.getIconWidth()+10, 180,100, 20);
       bEnd.addMouseListener(new boardMouseListener());

       moveChoices = new JComboBox<>();
       moveChoices.setBackground(Color.white);
       moveChoices.setBounds(icon.getIconWidth()+10, 210,100, 20);

       updateMoveSelection();

       // Place the action buttons in the top layer
       bPane.add(bAct, new Integer(2));
       bPane.add(bRehearse, new Integer(2));
       bPane.add(bMove, new Integer(2));
       bPane.add(bWork, new Integer(2));
       bPane.add(bUpgrade, new Integer(2));
       bPane.add(bEnd, new Integer(2));
       bPane.add(moveChoices, 2);

       playerInfo = new JLabel("test");
       
       playerInfo.setBounds(icon.getIconWidth()+10,240,130, 120);
       //playerInfo.
       bPane.add(playerInfo);
  }

    public void displayPlayerStats() {
        int[] pstats = this.manager.getPlayerStats();
        String displayText = String.format("<html><h2>Current Player: %d</h2>Money: %d <br> Credits: %d <br> Rehearsal Chips: %d </html>", pstats[0], pstats[1], pstats[2], pstats[3]);
        this.playerInfo.setText(displayText);


    }

   public void updateMoveSelection() {
      String[] test = {"boo", "b"};
      moveChoices = new JComboBox<>(test);
      moveChoices.setSelectedIndex(-1);
      bEnd.setBackground(Color.white);
      moveChoices.setBounds(this.icon.getIconWidth()+10, 210,100, 20);
      moveChoices.addMouseListener(new boardMouseListener());
      moveChoices.addActionListener(e -> {
         String selectedChoice = (String) moveChoices.getSelectedItem();
         System.out.println("Selected choice: " + selectedChoice);
      });
   }

    public void setManager(GameManager manager){
        this.manager = manager;
        displayPlayerStats();
    }

    public void setBoundsScaled(JLabel label, int x, int y, int width, int height) {   
        int scaledWidth = (int) (width * SCALE_WIDTH);
        int scaledHeight = (int) (height * SCALE_HEIGHT);   
        ImageIcon icon = (ImageIcon) label.getIcon(); 
        Image resizedImage = icon.getImage().getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        icon = new ImageIcon(resizedImage);
        label.setIcon(icon);
        label.setBounds((int) (x * SCALE_WIDTH), (int) (y * SCALE_HEIGHT), scaledWidth, scaledHeight);
    }


  
  // This class implements Mouse Events
  
  class boardMouseListener implements MouseListener{
  
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         
        //String[] args = new String[];

         if (e.getSource()== bAct){
            //playerlabel.setVisible(true);
            //System.out.println("Acting is Selected\n");
            manager.parseAction(new String[]{"act"} );
         }
         else if (e.getSource()== bRehearse){
            //System.out.println("Rehearse is Selected\n");
            manager.parseAction(new String[]{"rehearse"} );
         }
         else if (e.getSource()== bMove){
            //System.out.println("Move is Selected\n");
         }
         else if(e.getSource() == bUpgrade){
            //not this simple! need to prompt player for money vs credits and what rank
            //manager.parseAction(new String[]{"upgrade"});
         }else if(e.getSource() == bEnd){
            manager.parseAction(new String[]{"end"} );
         }
         displayPlayerStats();

      }
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
   }

} 
