public class Role {
    private String name;
    private String caption;
    private int difficulty;
    public Role(String name, String caption, int difficulty) {
        this.name = name;
        this.caption = caption;
        this.difficulty = difficulty;
    }

    public String getCaption() {
        return caption;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }
}
