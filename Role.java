public class Role {
    private String name;
    private String caption;
    private int difficulty;
    private boolean onCard;
    private boolean isOccupied;
    public Role(String name, String caption, int difficulty, boolean onCard) {
        this.name = name;
        this.caption = caption;
        this.difficulty = difficulty;
        this.onCard = onCard;
        this.isOccupied = false;
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

    public boolean getOnCard() {
        return onCard;
    }
}
