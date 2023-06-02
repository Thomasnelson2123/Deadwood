import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;

public class Gooey extends JFrame {

    //String PREFIX = "../Images/";
    String PREFIX = "../Images/";

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

    ArrayList<CardLabel> cards = new ArrayList<CardLabel>();

    ArrayList<UpgradeButton> upgradeButtons = new ArrayList<UpgradeButton>();

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

        playerInfo.setBounds(icon.getIconWidth()+10,240,130, 160);
        //playerInfo.
        bPane.add(playerInfo);

        optionPane = new JOptionPane();
    }

    public void displayPlayerStats() {
        int[] pstats = this.manager.getPlayerStats();
        int day = this.manager.getDayNumber();
        String displayText = String.format("<html><h2> Day %d </h2><h2>Current Player: %d</h2>Money: %d <br> Credits: %d <br> Rehearsal Chips: %d </html>", day, pstats[0], pstats[1], pstats[2], pstats[3]);
        this.playerInfo.setText(displayText);


    }

    //#region Create Buttons and Labels 

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
            bPane.setLayer(rb,BUTTON_LAYER);
            this.bPane.add(rb, BUTTON_LAYER);
            roleButtons.add(rb);

            //System.out.println(info[0]);
        }

        for (String[] info: offCardRoles) {
            RoleButton rb = new RoleButton(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]),
            Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            rb.setAvailable(false);
            rb.addMouseListener(new boardMouseListener());
            bPane.setLayer(rb, BUTTON_LAYER);
            this.bPane.add(rb, BUTTON_LAYER);
            roleButtons.add(rb);

            //System.out.println(info[0]);
        }
    }

    public void createMoveButtons() {
        String[][] roomInfo = this.manager.getRoomDims();
        for (String[] info: roomInfo) {
            String roomName = info[0];
            String[][] shotInfo = this.manager.getShotCounterDims(roomName);
            ShotLabel[] shots = null;
            if (shotInfo != null) {
                shots = createShots(shotInfo);
            }   
            MoveButton mb = new MoveButton(Integer.parseInt(info[1]), Integer.parseInt(info[2]), 
            Integer.parseInt(info[3]), Integer.parseInt(info[4]), roomName, shots);
            mb.setAvailable(false);
            mb.addMouseListener(new boardMouseListener());
            bPane.setLayer(mb, BUTTON_LAYER);
            this.bPane.add(mb, BUTTON_LAYER);
            moveButtons.add(mb);
            
        }
    }

    public void createUpgradeButtons(){
        int startX = 98;
        int startY = 542;
        
        int xDiff = 49;
        int yDiff = 22;

        int buttonWidth = 19;
        int buttonHeight = 19;

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 2; j++){
                boolean isUsingMoney = j == 0;
                UpgradeButton ub = new UpgradeButton(startX + (xDiff*j), startY+(yDiff*i), buttonWidth, buttonHeight, isUsingMoney, i + 2);
                ub.setAvailable(false);
                ub.addMouseListener(new boardMouseListener());
                bPane.setLayer(ub, BUTTON_LAYER);
                this.bPane.add(ub, BUTTON_LAYER);
                upgradeButtons.add(ub);
            }
        }
    }

    public void createCards() {
        for (int i = 1; i <= 40; i++) {
            // because the first 9 cards have a leading '0'
            String number = String.format("%02d", i);
            CardLabel card = new CardLabel(number + ".png", 205, 115);
            bPane.setLayer(card, CARD_LAYER);
            this.bPane.add(card);
            this.cards.add(card);
        }
    }

    public ShotLabel[] createShots(String[][] shotInfo) {

        ShotLabel[] shotLabels = new ShotLabel[shotInfo.length];
        int i = 0;
        for (String[] shot : shotInfo) {
            boolean hasShot = Boolean.parseBoolean(shot[0]);
            int x = Integer.parseInt(shot[2]);
            int y = Integer.parseInt(shot[3]);
            int w = Integer.parseInt(shot[4]);
            int h = Integer.parseInt(shot[5]);

            ShotLabel shotLabel = new ShotLabel(hasShot, shot[1], x, y, w, h);
            this.bPane.setLayer(shotLabel, BUTTON_LAYER);
            this.bPane.add(shotLabel);
            shotLabels[i] = shotLabel;
            i++;
        }
        return shotLabels;
    }

    //#endregion

    public void updateShotCounters() {
        for (MoveButton mb: this.moveButtons) {
            String roomName = mb.roomName;
            int[] shotCounterInfo = manager.getShotCounterInfo(roomName);
            int removedShots = shotCounterInfo[1] - shotCounterInfo[0];
            ShotLabel[] shots = mb.getShots();
            if (shots == null) {
                continue;
            }
            for (int i = 0; i < shotCounterInfo[1]; i++) {
                shots[i].setShot(true);
            }
            for (int i = 0; i < removedShots; i++) {
                shots[i].setShot(false);
            }
        }
    }

    // Gooey does not start with the manager, but is rather passed in
    // after the number of players are selected
    // because of this, any call that requires the manager class during initialization of the game
    // goes here
    public void setManager(GameManager manager){
        this.manager = manager;
        displayPlayerStats();
        createRoleButtons();
        createMoveButtons();
        createUpgradeButtons();
        initPlayers();
        updateAllPlayerdice();
        createCards();
        populateSceneCardButtonArray();
        updateAllSceneCards();
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
            String color = playerColors[i];
            // set at trailer as well
            PlayerDice player = new PlayerDice(color, DICE_SIZE, DICE_SIZE);
            this.setBoundsScaled(player, TRAILER_X, TRAILER_Y, DICE_SIZE, DICE_SIZE);
            this.bPane.setLayer(player, PLAYER_LAYER);
            this.playerDices.add(player);
            this.bPane.add(player, PLAYER_LAYER);
        }
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

    public void enableUpgradeButtons(){
        boolean isInOffice = manager.playerIsInOffice();
        if(!isInOffice){
            notInOffice();
        }else{
            int playerRank = manager.getCurrentPlayer().getRank();
            boolean canAffordAny = false;
            for(UpgradeButton b : upgradeButtons){
                int myRank = b.getTargetRank();
                boolean isMoney = b.getIsUsingMoney();

                int dosh = 0;
                if(isMoney){
                    dosh = manager.getCurrentPlayer().getMoney();
                }else{
                    dosh = manager.getCurrentPlayer().getCredits();
                }

                boolean canAfford = manager.canAfford(dosh,isMoney,myRank);
                if(playerRank < myRank && canAfford){
                    b.setAvailable(true);
                    canAffordAny = true;
                }
            }

            if(!canAffordAny){
                cannotAffordAnyUpgrades();
            }
        }
    }

    public void disableUpgradeButtons(){
        for(UpgradeButton b : upgradeButtons){
            b.setAvailable(false);
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
                String buttonName = roleButtons.get(i).getRoleName();
                for(int j = 0; j < availableRoles.length; j++){
                    if(availableRoles[j].equals(buttonName)){
                        roleButtons.get(i).setAvailable(true);
                        //System.out.println(availableRoles[j]);
                        //int[] coords = roleButtons.get(i).getLocalCoords();
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

            //System.out.println(dims[0] +", "+dims[1]+", "+xOffset+", "+yOffset);

            playerDices.get(i).setCoordinates(dims[0], dims[1], xOffset, yOffset);
        }
    }

    public void populateSceneCardButtonArray(){
        // this method is a little expensive, it should ideally only be called once at startup

        for(int i = 0; i < cards.size(); i++){
            // find names of each role in my scene
            CardLabel workingCard = cards.get(i);
            String workingFileName = workingCard.getFileName();

            String[] myRoleNames = manager.getRoleNamesFromRoomFileName(workingFileName);
            
            /*
            System.out.println("----------------------------------------------------");
            for(String s : myRoleNames){
                System.out.println(s);
            }*/

            ArrayList<RoleButton> myRoleButtons = new ArrayList<RoleButton>();

            //search through rolebuttons, if rolebutton r.name is one of my role button names, add it to myRoleButtons
            for(RoleButton r : roleButtons){
                String roleName = r.getRoleName();
                for(int j = 0; j < myRoleNames.length; j++){
                    if(myRoleNames[j].equals(roleName)){
                        myRoleButtons.add(r);
                        //System.out.println(roleName);
                        break;
                    }
                }
            }

            workingCard.setButtons(myRoleButtons.toArray(new RoleButton[myRoleNames.length]));
        }
    }

    public void updateAllSceneCards(){

        /*
         * each scene in allCurrentScenes is like this:
         * (this list only contains scenes currently out on the board)
         * 
         * allCurrentScenes[i][0] = filename
         * allCurrentScenes[i][1] = isFlipped
         * allCurrentScenes[i][2] = xCoordinate
         * allCurrentScenes[i][3] = yCoordinate
         * 
         */

        String[][] allCurrentScenes = manager.getAllCurrentScenesInfo();
        
        for(int i = 0; i < cards.size(); i++){
            CardLabel workingCard = cards.get(i);
            String workingFileName = workingCard.getFileName();

            // check to see if this card is currently on the board
            boolean isContained = false;
            int myIndex = -1;

            for(int j = 0; j < allCurrentScenes.length; j++){
                if(workingFileName.equalsIgnoreCase(allCurrentScenes[j][0])){
                    isContained = true;
                    myIndex = j;
                    break;
                }
            }


            // if it is, set its position to the correct spot and whatnot
            if(isContained){
                //set card to either front or back
                workingCard.setCardIcon(Boolean.valueOf(allCurrentScenes[myIndex][1]));
                //move to correct spot
                int cardX = Integer.valueOf(allCurrentScenes[myIndex][2]);
                int cardY = Integer.valueOf(allCurrentScenes[myIndex][3]);
                workingCard.placeCard(cardX, cardY);
                //also move my buttons to correct spot
                for(RoleButton b : workingCard.getButtons()){
                    //set position takes local coords into account, we dont need to do it here
                    b.setPositon(cardX,cardY);
                }
            }else{
                workingCard.setVisible(false);
            }
            
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

    // the greatest method of all time
    public void actionAlreadyTaken(){
        JOptionPane.showMessageDialog(bPane,"Too many actions!");
    }

    public void nextDay() {
        JOptionPane.showMessageDialog(bPane,"All but one scene has wrapped. It is a new day!");
    }

    public void actNotification(boolean actSuccess){
        if(actSuccess){
            JOptionPane.showMessageDialog(bPane,"Act success!");
        }else{
            JOptionPane.showMessageDialog(bPane,"Act failed.");
        }
    }

    public void sceneWrap(){
        JOptionPane.showMessageDialog(bPane,"That's a wrap! Scene completed.");
    }

    public void cannotAct() {
        JOptionPane.showMessageDialog(bPane,"You can't act right now!");
    }

    public void addRehearsalChip(int numChips){
        JOptionPane.showMessageDialog(bPane,"<html>You have gained a rehearsal chip!<br>Your chips: "+numChips+"</html>");
    }

    public void tooManyChips(){
        JOptionPane.showMessageDialog(bPane,"You already have the max amount of rehearsal chips!");
    }

    public void chipsButNoRole(){
        JOptionPane.showMessageDialog(bPane,"You cannot rehearse if you don't have a role!");
    }

    public void notInOffice() {
        JOptionPane.showMessageDialog(bPane,"You must be in the office to upgrade!");
    }

    public void cannotAffordAnyUpgrades() {
        JOptionPane.showMessageDialog(bPane,"No buttons have been enabled because you either cant afford them or are too high rank!");
    }

    public void upgradeSuccess(){
        JOptionPane.showMessageDialog(bPane,"Upgrade successful!");
    }

    public void invalidUpgrade(boolean badInput){
        // these should basically never run, they are here for debugging
        if(badInput){
            JOptionPane.showMessageDialog(bPane,"<html>Upgrade failed!<br>Invalid input</html>");
        }else{
            JOptionPane.showMessageDialog(bPane,"<html>Upgrade failed!<br>You do not possess enough resources to upgrade.</html>");
        }
    }

    //#endregion

    public void displayWinners(ArrayList<Player> winners){
        String printme = "";
        if(winners.size() == 1){
            int winnerNum = winners.get(0).getPlayerNum();
            printme += "The winner is: Player "+winnerNum+"! Congratulations!";
        }
        else{
            printme += "There has been a tie! The winners are: ";
            for(int i = 0; i < winners.size(); i++){
                int winnerNum = winners.get(i).getPlayerNum();
                printme += "Player "+winnerNum+", ";
            }
            printme += "Congratulations!";
        }

        JOptionPane.showMessageDialog(bPane,printme);
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
                updateShotCounters();
            }
            else if (e.getSource()== bRehearse){
                //System.out.println("Rehearse is Selected\n");
                manager.parseAction(new String[]{"rehearse"} );
            }
            else if (e.getSource()== bMove){
                boolean canMove = manager.checkCanMove();
                if (canMove) {
                    disableAllRoleButtons();
                    disableUpgradeButtons();
                    enableAdjacentRooms(manager.getPlayerRoomNeighbors());
                }
            }
            else if(e.getSource() == bUpgrade){
                enableUpgradeButtons();
            }else if(e.getSource() == bEnd){
                disableAllRoomButtons();
                disableAllRoleButtons();
                disableUpgradeButtons();
                manager.parseAction(new String[]{"end"} );
            }
            else if (e.getSource() == bWork) {
                boolean canWork = manager.checkCanWork();
                if(canWork){
                    disableAllRoomButtons();
                    enableRolesInRoom();
                }
            }
            else if (e.getSource() == bCancel) {    
                disableAllRoomButtons();
                disableAllRoleButtons();
                disableUpgradeButtons();
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
                }
            }

            for(UpgradeButton b: upgradeButtons){
                if (e.getSource() == b) {
                    String targetRank = Integer.toString(b.getTargetRank());
                    String isMoney = "";
                    if(b.getIsUsingMoney()){
                        isMoney = "money";
                    }else{
                        isMoney = "credits";
                    }
                    manager.upgradeRank(targetRank,isMoney,manager.getCurrentPlayer());
                    playerDices.get(manager.getCurrentPlayerNum() - 1).updateIcon(b.getTargetRank());
                    disableUpgradeButtons();
                }
            }

            displayPlayerStats();
            updateAllPlayerdice();
            updateAllSceneCards();

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
    //#region Button and Label classes
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

        public int[] getLocalCoords(){
            int[] coords = new int[2];
            coords[0] = this.localX;
            coords[1] = this.localY;
            return coords;
        }

    }

    class MoveButton extends JButton {

        private String roomName;
        private ShotLabel[] shots;

        public MoveButton(int x, int y, int w, int h, String roomName, ShotLabel[] shots) {
            this.roomName = roomName;
            int scaledWidth = (int) (w * SCALE_WIDTH);
            int scaledHeight = (int) (h * SCALE_HEIGHT);   
            this.setBounds((int) (x  * SCALE_WIDTH), (int) (y * SCALE_HEIGHT), scaledWidth, scaledHeight);
            ImageIcon icon = new ImageIcon(PREFIX + "movehere.png"); 
            this.setContentAreaFilled(false);
            this.setIcon(icon);
            this.shots = shots;
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

        public ShotLabel[] getShots() {
            return shots;
        }

    }

    // class used for the upgrade buttons
    class UpgradeButton extends JButton{
        
        private boolean isUsingMoney;
        private int targetRank;
        //private int cost; //resolved in upgrade thing

        public UpgradeButton(int x, int y, int w, int h, boolean isUsingMoney, int targetRank) {
            this.isUsingMoney = isUsingMoney;
            this.targetRank = targetRank;
            int scaledWidth = (int) (w * SCALE_WIDTH);
            int scaledHeight = (int) (h * SCALE_HEIGHT);   
            this.setBounds((int) (x  * SCALE_WIDTH), (int) (y * SCALE_HEIGHT), scaledWidth, scaledHeight);
            ImageIcon icon = new ImageIcon(PREFIX + "button.png"); 
            this.setContentAreaFilled(false);
            this.setIcon(icon);
        }

        public boolean getIsUsingMoney(){
            return this.isUsingMoney;
        }

        public int getTargetRank() {
            return this.targetRank;
        }

        public boolean getAvailable() {
            return this.isEnabled();
        }

        public void setAvailable(boolean isAvailable) {
            this.setEnabled(isAvailable);
            this.setVisible(isAvailable);
        }
    }

    // class used to represent the player dice
    public class PlayerDice extends JLabel{

        private int x;
        private int y;
        private int w;
        private int h;
        private String color;

        public PlayerDice(String color, int w, int h) {
            this.color = color;
            String file = PREFIX + color + "1.png";
            ImageIcon icon = new ImageIcon(file);
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

        // used when upgrading
        public void updateIcon(int num) {
            if (num > 6 || num < 1) {
                JOptionPane.showMessageDialog(bPane,"Curious, the application attemped to set a player rank to something that shouldn't be possible. Quite curious indeed");
                return;
            }
            String fileName = PREFIX + color + num + ".png";
            ImageIcon icon = new ImageIcon(fileName);
            this.setIcon(icon);
            this.setVisible(true);
        }
    }

    public class CardLabel extends JLabel{

        private int x;
        private int y;
        private int w;
        private int h;
        private String fileName;
        private ImageIcon cardFront;
        private ImageIcon cardBack;
        private RoleButton[] buttons;

        private CardLabel(String fileName, int w, int h) {
            this.w = w;
            this.h = h;
            this.fileName = fileName;
            this.cardFront = new ImageIcon(PREFIX + fileName);
            this.cardBack = new ImageIcon(PREFIX + "Cardback-small.jpg");
            this.setIcon(cardBack);
            setBoundsScaled(this, 0, 0, w, h);
            this.setVisible(false);
            this.buttons = null; //populated elsewhere
        }

        public void placeCard(int x, int y) {

            this.x = (int) (x  * SCALE_WIDTH);
            this.y = (int) (y  * SCALE_HEIGHT);  
            setBoundsScaled(this, x, y, w, h);
            this.setVisible(true);
        }

        public void setCardIcon(boolean isFlipped) {
            //this.setIcon(cardFront);
            if(isFlipped){
                this.setIcon(cardFront);
            }else{
                this.setIcon(cardBack);
            }
        }

        public String getFileName(){
            return this.fileName;
        }

        public void removeCard() {
            this.x = 0;
            this.y = 0;
            this.setVisible(false);
        }

        public RoleButton[] getButtons(){
            return this.buttons;
        }

        public void setButtons(RoleButton[] buttons){
            this.buttons = buttons;
        }


    }

    // this class holds information about ShotCounters for the GUI
    public class ShotLabel extends JLabel {
        private int x;
        private int y;
        private boolean hasShot;
        private String roomName;
        public ShotLabel(boolean hasShot, String roomName, int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.hasShot = hasShot;
            this.setVisible(hasShot);
            ImageIcon icon = new ImageIcon(PREFIX + "shot.png");
            this.setIcon(icon);
            setBoundsScaled(this, x, y, w, h);
            this.roomName = roomName;
        }

        public void setShot(boolean hasShot) {
            this.hasShot = hasShot;
            this.setVisible(this.hasShot);
        }

        public String getRoomName() {
            return roomName;
        }
    }
    //#endregion
}