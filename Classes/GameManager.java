public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    private int maxDays = 4;
    public GameManager(Board board, Player[] players, Bank bank) {
        this.bank = bank;
        this.board = board;
        this.players = players;
        this.gui = new GUI(this);
        this.day = 0;
        this.completedScenes = 0;

        if (players.length < 4) {
            maxDays = 3;
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
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
            if (this.currentPlayer.isWorking()) {
                this.currentPlayer.setCanMove(false);
            }
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
                act();
                break;
            case "rehearse":
                rehearse();
                break;
            case "work":
                takeRole(args[1]);
                break;
            case "move":
                move(args[1]);
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
    public void act(){

    }

    // resolve the "rehearse" action a player may take
    public void rehearse(){
        
        
        //int max_rehearsal_chips = difficulty - 1
    }

    // resolve the "take role" action a player may take
    public void takeRole(String role){
        // get room player is in
        String playerRoom = board.getPlayerRoom(currentPlayer.getPlayerNum());
        // role isn't in same room as player, they can't take that role
        if (!board.isRoleInRoom(playerRoom, role)) {
            this.gui.roleNotInRoom();
        }
        // role IS in room, but it isn't available
        else if (!board.isRoleAvailable(role)) {
            this.gui.roleNotAvailable();
        }
        // role IS available, but player does not have high enough of a rank
        else if (this.board.getRoleDifficulty(role) > this.currentPlayer.getRank()) {
            this.gui.rankTooLow();
        }
        else {
            // at this point role is available, tell player and update things
            this.gui.displayTakeRole(this.currentPlayer.getPlayerNum(), role);
            this.currentPlayer.setWorking(true);
            this.board.setPlayerRole(role, this.getCurrentPlayerNum());
        }
               
    }

    // resolve the "move" action a player may take
    public void move(String destinationRoom) {
        // destinationRoom will be null if the user tries to move twice
        if (destinationRoom != null) {
            if (currentPlayer.canMove()) {
                boolean success = board.movePlayer(currentPlayer.getPlayerNum(), destinationRoom);
                if (!success) {
                    gui.invalidMove();
                }
                else {
                    this.currentPlayer.setCanMove(false);
                    gui.displayMove(currentPlayer.getPlayerNum(), destinationRoom);
                    
                }
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
    public void payPlayers(){
        this.bank.payPlayers(players, completedScenes, null, null);
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return getCurrentPlayer().getPlayerNum();
    }

    public String getPlayerLocation(){
        return board.getPlayerRoom(this.getCurrentPlayerNum());
    }

    public String[] getPlayerRoomNeighbors() {
        String room = board.getPlayerRoom(getCurrentPlayerNum());
        return board.getRoomNeighbors(room);
    }

    public void endTurn(){
        // if current player isn't working a role, they can move next turn
        if (!this.currentPlayer.isWorking()) {
            this.currentPlayer.setCanMove(true);
        }
        // set current player to next player
        int currentPlayerNum = currentPlayer.getPlayerNum();
        this.currentPlayer = this.players[currentPlayerNum % this.players.length];
    }

    public void endGame(){
        System.exit(1);
    }

    public boolean canCurrentPlayerMove() {
        return this.currentPlayer.canMove();
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

}