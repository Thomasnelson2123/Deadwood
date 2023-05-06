public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    public GameManager(Board board, Player[] players, Bank bank) {
        this.bank = bank;
        this.board = board;
        this.players = players;
        this.gui = new GUI(this);

        this.currentPlayer = null;
        this.day = 0;
        this.completedScenes = 0;
    }

    // the main loop for taking turns between players in the game
    public void mainLoop(){
        while(true) {
            // HEY CARTER READ THIS

            // loop goes as such
            // args = gui.parseUserInput()
            // parseAction(args)
            
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
        return currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return getCurrentPlayer().getPlayerNumber();
    }

    public String getPlayerLocation(int playerNum){
        return board.getPlayerRoom(playerNum);
    }

    public void endTurn(){

    }

    public void endGame(){
        System.exit(1);
    }

}
