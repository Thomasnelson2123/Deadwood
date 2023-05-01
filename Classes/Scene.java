public class Scene {
    private String name;
    private String caption;
    private int budget;
    private Role[] roles;
    private int sceneNumber;

    // the state of a scene card
    public enum SceneState {
        UNDRAWN,
        HIDDEN,
        ACTIVE,
        COMPLETED,
    }
    
    private SceneState state;
    public Scene(String name, int sceneNumber, String caption, int budget, Role[] roles) {
        this.state = SceneState.UNDRAWN;
        this.name = name;
        this.caption = caption;
        this.budget = budget;
        this.roles = roles;
        this.sceneNumber = sceneNumber;
    }
//#region Getters
    public int getBudget() {
        return budget;
    }

    public String getCaption() {
        return caption;
    }

    public String getName() {
        return name;
    }

    public int getSceneNumber() {
        return sceneNumber;
    }

    public Role[] getRoles() {
        return roles;
    }
//#endregion

    // puts the card on the board
    public void putCardOnBoard(){

    }

    // flips the card over
    public void flipCard() {

    }

    // remove the card from the board
    public void removeCardFromBoard() {

    }
}   

