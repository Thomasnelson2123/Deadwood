public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    private int maxDays = 4;
    private boolean canMove = true;
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
                takeRole();
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
    public void takeRole(){

    }

    // resolve the "move" action a player may take
    public void move(String destinationRoom) {
        // destinationRoom will be null if the user tries to move twice
        if (destinationRoom != null) {
            if (this.canMove) {
                boolean success = board.movePlayer(currentPlayer.getPlayerNumber(), destinationRoom);
                if (!success) {
                    gui.invalidMove();
                }
                else {
                    this.canMove = false;
                    gui.displayMove(currentPlayer.getPlayerNumber(), destinationRoom);
                    
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
        return getCurrentPlayer().getPlayerNumber();
    }

    public String getPlayerLocation(){
        return board.getPlayerRoom(this.getCurrentPlayerNum());
    }

    public String[] getPlayerRoomNeighbors() {
        String room = board.getPlayerRoom(getCurrentPlayerNum());
        return board.getRoomNeighbors(room);
    }

    public void endTurn(){
        int currentPlayerNum = currentPlayer.getPlayerNumber();
        this.currentPlayer = this.players[currentPlayerNum % this.players.length];
        this.canMove = true;
    }

    public void endGame(){
        System.exit(1);
    }

    public boolean canMove() {
        return this.canMove;
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

}
