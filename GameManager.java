public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private Controller controller;

    private Player currentPlayer;
    private int day;
    private int completedScenes;
    public GameManager(Board board, Player[] players, Bank bank, Controller controller) {
        this.bank = bank;
        this.board = board;
        this.controller = controller;
        this.players = players;

        this.currentPlayer = null;
        this.day = 0;
        this.completedScenes = 0;
    }

    // the main loop for taking turns between players in the game
    public void mainLoop(){

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
    public void move(Player player) {

    }

    // resolve the "upgrade rank" action a player may take
    public void upgradeRank(Player player){

    }

    // pay players for completing a scene
    public void payPlayers(){

    }
}
