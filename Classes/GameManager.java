public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    private int maxDays = 4;
    private boolean hasMoved = false;
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
                upgradeRank();
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

    }

    // resolve the "take role" action a player may take
    public void takeRole(){

    }

    // resolve the "move" action a player may take
    public void move(String destinationRoom) {
        // destinationRoom will be null if the user tries to move twice
        if (destinationRoom != null) {
            if (!this.hasMoved) {
                boolean success = board.movePlayer(currentPlayer.getPlayerNumber(), destinationRoom);
                if (!success) {
                    gui.invalidMove();
                }
                else {
                    this.hasMoved = true;
                    gui.displayMove(currentPlayer.getPlayerNumber(), destinationRoom);
                    
                }
            }
        }
    }

    // resolve the "upgrade rank" action a player may take
    public void upgradeRank(){

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
        this.hasMoved = false;
    }

    public void endGame(){
        System.exit(1);
    }

    public boolean canMove() {
        return !this.hasMoved;
    }

}
