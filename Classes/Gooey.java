import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.ArrayList;

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
    JButton bCancel;

    //JComboBox
    JComboBox moveChoices;
    JComboBox roleChoices;

    // JLayered Pane
    JLayeredPane bPane;

    //board icon
    ImageIcon icon;

    JOptionPane optionPane;

    // ArrayList of role buttons
    ArrayList<RoleButton> roleButtons = new ArrayList<RoleButton>();

    //maybe hold a string that has whatever button the user selected? idk
    public String roleChoice = "";

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
        boardlabel.setVisible(true);

        // Add the board to the lowest layer
        bPane.setLayer(boardlabel, 0);
        bPane.add(boardlabel, JLayeredPane.DEFAULT_LAYER);

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

        bCancel = new JButton("CANCEL");
        bCancel.setBackground(Color.white);
        bCancel.setBounds(icon.getIconWidth()+10, 210,100, 20);
        bCancel.addMouseListener(new boardMouseListener());


        // Place the action buttons in the top layer
        bPane.add(bAct, 2);
        bPane.add(bRehearse, 2);
        bPane.add(bMove, 2);
        bPane.add(bWork, 2);
        bPane.add(bUpgrade, 2);
        bPane.add(bEnd, 2);
        bPane.add(bCancel, 2);

        playerInfo = new JLabel("test");

        playerInfo.setBounds(icon.getIconWidth()+10,240,130, 120);
        //playerInfo.
        bPane.add(playerInfo);

        optionPane = new JOptionPane();
    }

    public void displayPlayerStats() {
        int[] pstats = this.manager.getPlayerStats();
        String displayText = String.format("<html><h2>Current Player: %d</h2>Money: %d <br> Credits: %d <br> Rehearsal Chips: %d </html>", pstats[0], pstats[1], pstats[2], pstats[3]);
        this.playerInfo.setText(displayText);


    }

    // this method creates a button for each role in the game. They are default set unavailable
    // they are added to an arraylist of roleButtons
    public void configureRoleButtons() {
        String[][] onCardRoles = this.manager.getOnCardRoleDims();
        String[][] offCardRoles = this.manager.getOffCardRoleDims();

        for (String[] info: onCardRoles) {
            RoleButton rb = new RoleButton(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
            Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            rb.setAvailable(false);
            this.bPane.add(rb, -1);
            roleButtons.add(rb);
        }

        for (String[] info: offCardRoles) {
            RoleButton rb = new RoleButton(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
            Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            rb.setAvailable(false);
            bPane.setLayer(rb, 2);
            this.bPane.add(rb, 2);
            roleButtons.add(rb);

        }
    }

    public void setManager(GameManager manager){
        this.manager = manager;
        displayPlayerStats();
        configureRoleButtons();
    }

    // unfinished, can basically disregard this
    public String takeRole() {
        return this.roleChoice;
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

    //#region JOptionPane notifications
    public void roleNotInRoom() {
        JOptionPane.showMessageDialog(bPane,"The specified role is not in your current room. You can only take on a role if you are near it!");
    }

    public void scenelessRoom() {
        JOptionPane.showMessageDialog(bPane,"This room has no scenes for you to act. What are you doing?? \n Does thou not know? Does thou not see? There are no scenes!");

    }

    public void noShotCounters(){
        JOptionPane.showMessageDialog(bPane, "You cannot join this scene as it has already been completed!");
    }

    public void alreadyWorking() {
        JOptionPane.showMessageDialog(bPane,"Already working a role! Cannot leave until the scene has wrapped,");
    }

    //#endregion

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
            else if (e.getSource() == bWork) {
                manager.parseAction(new String[] {"work"});
            }
            else if (e.getSource() == bCancel) {
                roleChoice = "cancel";
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

    class RoleButton extends JButton{

        private String roleName;
        private int localX;
        private int localY;
        private int w;
        private int h;

        public RoleButton(String roleName, int x, int y, int w, int h) {
            this.roleName = roleName;
            this.w = w;
            this.h = h;
            this.localX = x;
            this.localY = y;
            setPositon(0, 0);
            ImageIcon icon = new ImageIcon("../Images/button.png");
            this.setContentAreaFilled(false);
            this.setIcon(icon);
            //this.setOpaque(false);
        }

        public void setPositon(int cardX, int cardY) {   
            int scaledWidth = (int) (this.w * SCALE_WIDTH);
            int scaledHeight = (int) (this.h * SCALE_HEIGHT);   
            this.setBounds((int) ((this.localX + cardX) * SCALE_WIDTH), (int) ((this.localY+ cardY) * SCALE_HEIGHT), scaledWidth, scaledHeight);
        }

        public void setAvailable(boolean isAvailable) {
            this.setEnabled(isAvailable);
            this.setVisible(isAvailable);
        }

        public boolean getAvailable() {
            return this.isEnabled();
        }

        public String getRoleName() {
            return this.roleName;
        }

    }

} 
