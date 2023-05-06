public class Controller {
    private GameManager manager;
    private GUI gui;

    public Controller() {
        this.gui = new GUI(this);
    }

    // simple instantiation function
    public void instantiateGameManager(GameManager manager) {
        this.manager = manager;
    }

    // gui tells controller what player wants to do
    // parse the command and execute correct gamemanager operation
    public void guiToGameManager(String command) {
        
    }

     // displays to user the active player
     public int activePlayer() {
        return manager.getCurrentPlayer().getPlayerNumber();
     }
 
     // get current location of current player
     public String getCurrentPlayerLocation() {
        return manager.getPlayerLocation(activePlayer());
     }

     public String getPlayerLocation(int playerNum) {
        return manager.getPlayerLocation(playerNum);
     }
 
     public void act() {
        
     }
 
     public void rehearse() {
 
     }
 
     public void move() {
        
     }
 
     public void takeRole() {
 
     }
 
     // ends the game early
     public void endGame() {
        
     }
 
     // ends the players turn
     public void endTurn() {
 
     }
 
     public void upgrade() {
 
     }




}
