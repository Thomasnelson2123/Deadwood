public class Controller {
    private GameManager manager;
    private GUI gui;

    public Controller() {
        this.gui = new GUI();
    }

    public void instantiateGameManager(GameManager manager) {
        this.manager = manager;
    }
}
