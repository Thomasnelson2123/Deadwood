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

    


}
