import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.management.relation.RoleNotFoundException;

public class GameManager {

    private final Action[] ALL_ACTIONS = new Action[] 
    {Action.Work, Action.Act, Action.Rehearse, Action.Upgrade, Action.Move};
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    private int maxDays = 4;
    private Random rand;
    public GameManager(Board board, Player[] players, Bank bank, Random rand) {
        this.bank = bank;
        this.board = board;
        this.players = players;
        this.gui = new GUI(this);
        this.day = 0;
        this.completedScenes = 0;
        this.rand = rand;

        if (players.length < 4) {
            maxDays = 3;
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            players[i].setAvailableActions(ALL_ACTIONS);
            if (players.length == 5) {
                players[i].setCredits(2);           
            }
            if (players.length == 6) {
                players[i].setCredits(4);
            }
            if (players.length > 6) {
                players[i].setRank(2);
            }   

        }
        currentPlayer = players[0];



    }

    // the main loop for taking turns between players in the game
    public void mainLoop(){
        while(true) {
            // if the current player is working, they shouldn't be able to move
            /*
            if (this.currentPlayer.isWorking()) {
                this.currentPlayer.setCanMove(false);
            }*/
            String[] args = gui.parseUserInput();
            // if null: user entered gui only command, manager needs not do anything
            if (args != null) {
                parseAction(args);
            }

        }
    }

    public void parseAction(String[] args){
        //boolean actionSuccess = something;
        
        switch(args[0]){
            case "act":
                act(this.currentPlayer);
                break;
            case "rehearse":
                rehearse();
                break;
            case "work":
                takeRole(args[1], this.currentPlayer);
                break;
            case "move":
                move(args[1], this.currentPlayer);
                break;
            case "upgrade":
                upgradeRank(args[1], args[2]);
                break;
            case "end": //end turn
                endTurn();
                break;
            case "terminate":
                endGame();
                break;
        }
    }
    
    // resets players/scenes to next day
    public void nextDay(){

    }

    // resolve the "act" action a player may take
    public void act(Player player){
        // check if they can even act rn
        //if player.actionstaken = none && has role
        if (isActionAvailable(Action.Act, player)) {
            player.setAvailableActions(Action.None);
            int diceRoll = this.rand.nextInt(1, 7) + player.getRehearsalChipCount();
            String[] sceneInfo = this.board.getSceneInfo(getPlayerLocation(player.getPlayerNum()));
            int budget = Integer.parseInt(sceneInfo[2]);
            String[] playerRoleInfo = this.board.getPlayerRoleInfo(player.getPlayerNum());
            
            //give player credits/money
            boolean isOnCard = Boolean.parseBoolean(playerRoleInfo[3]);
            boolean actSuccess = diceRoll >= budget;
            String roomName = getPlayerLocation(player.getPlayerNum());
            // reward player based on whether they succeeded and whether they were on the card
            bank.actingReward(actSuccess, isOnCard, player);

            //remove shot counter
            if(actSuccess){
                board.removeShotCounter(roomName);
            }

            //if shot counter = 0, reward all players working on scene
            if(board.getShotCounters(roomName)[0] == 0){
                this.sceneWrap(roomName);
            }

        }
        else {
            this.gui.cannotAct();
        }
        
    }

    // resolve the "rehearse" action a player may take
    public void rehearse(){
        
        
        //int max_rehearsal_chips = difficulty - 1
    }

    // resolve the "take role" action a player may take
    public void takeRole(String role, Player player){
        // get room player is in
        String playerRoom = board.getPlayerRoom(player.getPlayerNum());
        // player is already working a role, cannot take another
        if (player.isWorking()) {
            this.gui.alreadyWorking();
        }
        // role isn't in same room as player, they can't take that role
        else if (!board.isRoleInRoom(playerRoom, role)) {
            this.gui.roleNotInRoom();
        }
        // role IS in room, but it isn't available
        else if (!board.isRoleAvailable(role)) {
            this.gui.roleNotAvailable();
        }
        // role IS available, but player does not have high enough of a rank
        else if (this.board.getRoleDifficulty(role) > player.getRank()) {
            this.gui.rankTooLow();
        }
        else{
            // at this point role is available, tell player and update things
            this.gui.displayTakeRole(player.getPlayerNum(), role);
            //this.currentPlayer.setWorking(true);
            player.setAvailableActions(new Action[] {Action.None});
            this.board.setPlayerRole(role, player.getPlayerNum());
            player.setWorking(true);
        }
               
    }

    // resolve the "move" action a player may take
    public void move(String destinationRoom, Player player) {
        // destinationRoom will be null if the user tries to move twice
        if (destinationRoom != null) {
            if (this.isActionAvailable(Action.Move, player)) {
                boolean success = board.movePlayer(player.getPlayerNum(), destinationRoom);
                if (!success) {
                    gui.invalidMove();
                }
                else {
                    //this.currentPlayer.setCanMove(false);
                    player.setAvailableActions(new Action[] {Action.Work, Action.Upgrade});
                    gui.displayMove(player.getPlayerNum(), destinationRoom);
                    
                }
            }else{

            }
        }
    }

    // resolve the "upgrade rank" action a player may take
    public void upgradeRank(String targetRank, String paymentType){
        

        int targetRankInt = Integer.parseInt(targetRank);
        boolean isUsingMoney = true;

        boolean success = false;
        boolean badInput = false;

        if(paymentType.toLowerCase(null) == "money"){
            isUsingMoney = true;
            success = this.bank.upgradePlayer(currentPlayer,targetRankInt,isUsingMoney);
        }else if(paymentType.toLowerCase(null) == "credits"){
            isUsingMoney = false;
            success = this.bank.upgradePlayer(currentPlayer,targetRankInt,isUsingMoney);
        }else{
            badInput = true;
        }

        if(!success){
            gui.invalidUpgrade(badInput);
        }
        
    }

    // pay players for completing a scene
    public void sceneWrap(String room){
        // can get role for every player
        // can then check if role is in the room being completed
        // pass into bank only the players in the room, and neccessary role info

        String[][] sceneRoleInfo = this.board.getSceneRoles(room);

        HashMap<Integer, Player> onCardRoles = new HashMap<>();
        HashMap<Integer, Player> offCardRoles = new HashMap<>();
        int budget = Integer.parseInt(this.board.getSceneInfo(room)[2]);
        for (String[] roleInfo: sceneRoleInfo) {
            Player p = null;
            for (Player player: this.players) {
                String playerRoleName = this.board.getPlayerRoleInfo(player.getPlayerNum())[0];
                // player working role on card
                if (playerRoleName.equals(roleInfo[0])) {
                    p = player;
                    break;
                }
            }

            int roleDifficulty = Integer.parseInt(roleInfo[2]);
            onCardRoles.put(roleDifficulty, p);
        }
        String[][] roomRoleInfo = this.board.getRoomRoles(room);
        for (String[] roleInfo: roomRoleInfo) {
            Player p = null;
            for (Player player: this.players) {
                String playerRoleName = this.board.getPlayerRoleInfo(player.getPlayerNum())[0];
                // player working role on card
                if (playerRoleName.equals(roleInfo[0])) {
                    p = player;
                    break;
                }
            }

            int roleDifficulty = Integer.parseInt(roleInfo[2]);
            offCardRoles.put(roleDifficulty, p);
        }
        this.bank.payPlayers(onCardRoles, offCardRoles, budget);
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return getCurrentPlayer().getPlayerNum();
    }

    public String getPlayerLocation(int playerNum){
        return board.getPlayerRoom(playerNum);
    }

    public String[] getPlayerRoomNeighbors() {
        String room = board.getPlayerRoom(getCurrentPlayerNum());
        return board.getRoomNeighbors(room);
    }

    public void endTurn(){
        // reset players abilities
        // set current player to next player
        Action[] actions;
        if (!this.currentPlayer.isWorking()) {
            actions = new Action[] {Action.Work,Action.Upgrade, Action.Move};
        }
        else {
            actions = new Action[] {Action.Act,Action.Rehearse};
        }
        this.currentPlayer.setAvailableActions(actions);
        int currentPlayerNum = currentPlayer.getPlayerNum();
        this.currentPlayer = this.players[currentPlayerNum % this.players.length];
    }

    public void endGame(){
        System.exit(1);
    }

    public boolean canCurrentPlayerMove() {
        return this.isActionAvailable(Action.Move, this.currentPlayer);
    }

    public boolean isCurrentPlayerWorking() {
        return this.currentPlayer.isWorking();
    }

    public String[][] getRoomRoleInfo(String room) {
        return this.board.getRoomRoles(room);
    }

    public String[][] getSceneRoleInfo(String room) {
        return this.board.getSceneRoles(room);
    }

    public String[] getSceneInfo(String room) {
        return this.board.getSceneInfo(room);
    }

    public int[] getShotCounterInfo(String room) {
        return this.board.getShotCounters(room);
    }

    public boolean isActionAvailable(Action action, Player player) {
        Action[] actions = player.getAvailableActions();
        for (Action a: actions) {
            if (a.equals(action)) {
                return true;
            }
        }
        return false;
    }

}