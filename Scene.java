public class Scene {
    private String name;
    private String caption;
    private int budget;
    private Role[] roles;

    public enum SceneState {
        UNDRAWN,
        HIDDEN,
        ACTIVE,
        COMPLETED,
    }
    
    private SceneState state;
    public Scene(String name, String caption, int budget, Role[] roles) {
        this.state = SceneState.UNDRAWN;
        this.name = name;
        this.caption = caption;
        this.budget = budget;
        this.roles = roles;
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

    public Role[] getRoles() {
        return roles;
    }
//#endregion

    public void putCardOnBoard(){

    }

    public void flipCard() {

    }

    public void removeCardFromBoard() {

    }
}   

