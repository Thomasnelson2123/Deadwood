public class Role {
    private String name;
    private String caption;
    private int difficulty;
    private boolean onCard;
    private boolean isOccupied;
    private boolean finishedShooting;
    private int[] dims; // probably not needed for first GUI
    public Role(String name, String caption, int difficulty, boolean onCard, int[] dims) {
        this.name = name;
        this.caption = caption;
        this.difficulty = difficulty;
        this.onCard = onCard;
        this.isOccupied = false;
        this.dims = dims;
        this.finishedShooting = false;
    }

    public int[] getDims() {
        return dims;
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

    public boolean getOccupied() {
        return isOccupied;
    }
    
    public void setOccupied(){
        this.isOccupied = true;
    }

    public void setNotOccupied() {
        this.isOccupied = false;
    }

    public void setFinishedShooting(boolean finishedShooting) {
        this.finishedShooting = finishedShooting;
    }

    public boolean hasFinishedShooting() {
        return this.finishedShooting;
    }

    public boolean isRoleAvailable() {
        return !this.isOccupied && !this.hasFinishedShooting();
    }

}