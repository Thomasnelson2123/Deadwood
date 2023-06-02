public class Scene {
    private String name;
    private String caption;
    private int budget;
    private Role[] roles;
    private int sceneNumber;
    private boolean isFacingUp;
    private String fileName;

    // constructor
    public Scene(String name, int sceneNumber, String caption, int budget, Role[] roles, String fileName) {
        this.isFacingUp = false;
        this.name = name;
        this.caption = caption;
        this.budget = budget;
        this.roles = roles;
        this.sceneNumber = sceneNumber;
        this.fileName = fileName;
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

    public boolean isFacingUp() {
        return isFacingUp;
    }

//#endregion

    // flips the card over
    public void flipCard() {
        //this.isFacingUp = !this.isFacingUp;
        this.isFacingUp = true;
    }

    public String getFileName() {
        return fileName;
    }

}   
