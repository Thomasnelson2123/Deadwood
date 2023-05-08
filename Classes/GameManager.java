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
                act(currentPlayer);
                break;
            case "rehearse":
                rehearse(currentPlayer);
                break;
            case "work":
                takeRole(currentPlayer);
                break;
            case "move":
                move(currentPlayer,args[1]);
                break;
            case "upgrade":
                upgradeRank(currentPlayer);
                break;
            case "end": //end turn
                endTurn();
                break;
            case "terminate":
                endGame();
                break;
        }
    }
    
    // gives one player the option to take actions
    public void playerTakeTurn(Player player){

    }

    // resets players/scenes to next day
    public void nextDay(){

    }

    // resolve the "act" action a player may take
    public void act(Player player){

    }

    // resolve the "rehearse" action a player may take
    public void rehearse(Player player){

    }

    // resolve the "take role" action a player may take
    public void takeRole(Player player){

    }

    // resolve the "move" action a player may take
    public void move(Player player, String destinationRoom) {
        int playerNum = player.getPlayerNumber();
        boolean success = board.movePlayer(playerNum, destinationRoom);
        if (!success) {

        }
        

    }

    // resolve the "upgrade rank" action a player may take
    public void upgradeRank(Player player){

    }

    // pay players for completing a scene
    public void payPlayers(){

    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return getCurrentPlayer().getPlayerNumber();
    }

    public String getPlayerLocation(int playerNum){
        return board.getPlayerRoom(playerNum);
    }

    public void endTurn(){
        int currentPlayerNum = currentPlayer.getPlayerNumber();
        this.currentPlayer = this.players[currentPlayerNum % this.players.length];
    }

    public void endGame(){
        System.exit(1);
    }

}
