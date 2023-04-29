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

    public void move(Player player) {
        
    }




}
