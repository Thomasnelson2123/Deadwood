public class Scene {
    private String name;
    private String caption;
    private int budget;
    private Role[] roles;
    private int sceneNumber;
    private boolean isFlipped;
    public Scene(String name, int sceneNumber, String caption, int budget, Role[] roles) {
        this.isFlipped = false;
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

    // flips the card over
    public void flipCard() {
        this.isFlipped = !this.isFlipped;
    }

}   

