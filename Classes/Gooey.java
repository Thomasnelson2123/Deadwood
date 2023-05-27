import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.ArrayList;

public class Gooey extends JFrame {

    //String PREFIX = "../Images/";
    String PREFIX = "Images/";

    double SCALE_HEIGHT = 0.8;
    double SCALE_WIDTH = 0.8;

    int TRAILER_X = 991;
    int TRAILER_Y = 248;

    int DICE_SIZE = 46;

    int CARD_W = 205;
    int CARD_H = 115;

    int BOARD_LAYER = 0;
    int CARD_LAYER = 1;
    int PLAYER_LAYER = 2;
    int BUTTON_LAYER = 3;
    GameManager manager;


    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
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
    //JComboBox moveChoices;
    //JComboBox roleChoices;

    // JLayered Pane
    JLayeredPane bPane;

    //board icon
    ImageIcon icon;

    JOptionPane optionPane;

    // ArrayList of role buttons
    ArrayList<RoleButton> roleButtons = new ArrayList<RoleButton>();

    // ArrayList of move buttons
    ArrayList<MoveButton> moveButtons = new ArrayList<MoveButton>();

    ArrayList<PlayerDice> playerDices = new ArrayList<PlayerDice>();

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
        this.icon =  new ImageIcon(PREFIX + "board.jpg");
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
        bPane.add(bAct, BUTTON_LAYER);
        bPane.add(bRehearse, BUTTON_LAYER);
        bPane.add(bMove, BUTTON_LAYER);
        bPane.add(bWork, BUTTON_LAYER);
        bPane.add(bUpgrade, BUTTON_LAYER);
        bPane.add(bEnd, BUTTON_LAYER);
        bPane.add(bCancel, BUTTON_LAYER);

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
    public void createRoleButtons() {
        String[][] onCardRoles = this.manager.getOnCardRoleDims();
        String[][] offCardRoles = this.manager.getOffCardRoleDims();

        for (String[] info: onCardRoles) {
            RoleButton rb = new RoleButton(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
            Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            rb.setAvailable(false);
            rb.addMouseListener(new boardMouseListener());
            this.bPane.add(rb, -1);
            roleButtons.add(rb);
        }

        for (String[] info: offCardRoles) {
            RoleButton rb = new RoleButton(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
            Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            rb.setAvailable(false);
            rb.addMouseListener(new boardMouseListener());
            bPane.setLayer(rb, BUTTON_LAYER);
            this.bPane.add(rb, BUTTON_LAYER);
            roleButtons.add(rb);

        }
    }

    public void createMoveButtons() {
        String[][] roomInfo = this.manager.getRoomDims();
        for (String[] info: roomInfo) {
            MoveButton mb = new MoveButton(Integer.parseInt(info[1]), Integer.parseInt(info[2]), 
            Integer.parseInt(info[3]), Integer.parseInt(info[4]), info[0]);
            mb.setAvailable(false);
            mb.addMouseListener(new boardMouseListener());
            bPane.setLayer(mb, BUTTON_LAYER);
            this.bPane.add(mb, BUTTON_LAYER);
            moveButtons.add(mb);
            
        }
    }

    public void setManager(GameManager manager){
        this.manager = manager;
        displayPlayerStats();
        createRoleButtons();
        createMoveButtons();
        initPlayers();

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

    public void initPlayers() {
        String[] playerColors = new String[] {"r", "b", "y", "g", "c", "p", "v", "w"};
        int numPlayers = this.manager.getNumberOfPlayers();
        for (int i = 0; i < numPlayers; i++) {
            String file = PREFIX + playerColors[i] + "1.png";
            // set at trailer as well
            PlayerDice player = new PlayerDice(file, DICE_SIZE, DICE_SIZE);
            this.setBoundsScaled(player, TRAILER_X, TRAILER_Y, DICE_SIZE, DICE_SIZE);
            this.bPane.setLayer(player, PLAYER_LAYER);
            this.playerDices.add(player);
            this.bPane.add(player, PLAYER_LAYER);
        }

    }

    //#region JOptionPane notifications
    public void roleNotInRoom() {
        JOptionPane.showMessageDialog(bPane,"The specified role is not in your current room. You can only take on a role if you are near it!");
    }

    public void noValidRoles() {
        JOptionPane.showMessageDialog(bPane,"There are no valid roles for you in this room! Try going to a room with unoccupied roles that have lower difficulties.");
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

    public void moveOverrideFailed() {
        JOptionPane.showMessageDialog(bPane,"Error: move could not be completed!");
    }

    public void cannotMoveWithRole() {
        JOptionPane.showMessageDialog(bPane,"You cannot leave your role until you wrap!");
    }

    public void actionAlreadyTaken(){
        JOptionPane.showMessageDialog(bPane,"Too many actions!");
    }

    // to call, enableAdjacentRooms(manager.getPlayerRoomNeighbors);
    public void enableAdjacentRooms(String[] adjacentRooms){
        for(int i = 0; i < moveButtons.size(); i++){
            for(int j = 0; j < adjacentRooms.length; j++){
                String buttonName = moveButtons.get(i).getRoomName();
                if(adjacentRooms[j].equals(buttonName)){
                    moveButtons.get(i).setAvailable(true);
                }
            }
        }
    }

    public void disableAllRoomButtons(){
        for(MoveButton button : moveButtons){
            button.setAvailable(false);
        }
    }


    public void enableRolesInRoom(){
        String[] availableRoles = manager.getAllAvailableRoles(manager.getCurrentPlayer());
        if(availableRoles.length == 0){
            // no available roles! cry about it
            noValidRoles();
        }else{
            // enable all valid role buttons
            for(int i = 0; i < roleButtons.size(); i++){
                for(int j = 0; j < availableRoles.length; j++){
                    String buttonName = roleButtons.get(i).getRoleName();
                    if(availableRoles[j].equals(buttonName)){
                        roleButtons.get(i).setAvailable(true);
                    }
                }
            }
        }
    }

    public void disableAllRoleButtons(){
        for(RoleButton button : roleButtons){
            button.setAvailable(false);
        }
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
                boolean canMove = manager.checkCanMove();
                if (canMove) {
                    enableAdjacentRooms(manager.getPlayerRoomNeighbors());
                }
            }
            else if(e.getSource() == bUpgrade){
                //not this simple! need to prompt player for money vs credits and what rank
                //manager.parseAction(new String[]{"upgrade"});
            }else if(e.getSource() == bEnd){
                manager.parseAction(new String[]{"end"} );
            }
            else if (e.getSource() == bWork) {
                boolean canWork = manager.checkCanWork();
                if(canWork){
                    enableRolesInRoom();
                }
            }
            else if (e.getSource() == bCancel) {    
                disableAllRoomButtons();
                disableAllRoleButtons();
            }

            for (MoveButton b: moveButtons) {
                if (e.getSource() == b) {
                    String targetRoomName = b.getRoomName();
                    manager.movePlayerOverride(targetRoomName);
                    disableAllRoomButtons();
                }
            }

            for (RoleButton b: roleButtons) {
                if (e.getSource() == b) {
                    String targetRoleName = b.getRoleName();
                    manager.takeRoleOverride(targetRoleName);
                    disableAllRoleButtons();
                    System.out.println("big penis");
                }
            }

            displayPlayerStats();
            updateAllPlayerdice();

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
            ImageIcon icon = new ImageIcon(PREFIX + "button.png");
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

    class MoveButton extends JButton {

        private String roomName;

        public MoveButton(int x, int y, int w, int h, String roomName) {
            this.roomName = roomName;
            int scaledWidth = (int) (w * SCALE_WIDTH);
            int scaledHeight = (int) (h * SCALE_HEIGHT);   
            this.setBounds((int) (x  * SCALE_WIDTH), (int) (y * SCALE_HEIGHT), scaledWidth, scaledHeight);
            ImageIcon icon = new ImageIcon(PREFIX + "movehere.png"); 
            this.setContentAreaFilled(false);
            this.setIcon(icon);
        }

        public String getRoomName() {
            return this.roomName;
        }

        public boolean getAvailable() {
            return this.isEnabled();
        }

        public void setAvailable(boolean isAvailable) {
            this.setEnabled(isAvailable);
            this.setVisible(isAvailable);
        }

    }

    public void updateAllPlayerdice(){
        
        for(int i = 0; i < playerDices.size(); i++){
            int xDiff = DICE_SIZE + 1;
            int yDiff = DICE_SIZE + 1;
            
            int[] dims = manager.getPlayerDims(i);

            int xOffset = 0;
            int yOffset = 0;

            if(!manager.playerIsWorking(i)){
                xOffset = xDiff * (i % 4);
                yOffset = yDiff * (i / 4) + CARD_H;
            }

            playerDices.get(i).setCoordinates(dims[0], dims[1], xOffset, yOffset);
        }
    }

    public class PlayerDice extends JLabel{

        private int x;
        private int y;
        private int w;
        private int h;

        public PlayerDice(String fileName, int w, int h) {
            ImageIcon icon = new ImageIcon(fileName);
            this.setIcon(icon);
            this.setVisible(true);
            this.w = (int) (w * SCALE_WIDTH);
            this.h = (int) (h * SCALE_HEIGHT);
        }

        // move the player somewhere on the board
        // offsets are for when a player is not on a role
        public void setCoordinates(int x, int y, int xOffset, int yOffset) {

            this.x = (int) ((x + xOffset)  * SCALE_WIDTH);
            this.y = (int) ((y + yOffset) * SCALE_HEIGHT);  
            this.setBounds(this.x,this.y, this.w, this.h);
        }
    }

} 
