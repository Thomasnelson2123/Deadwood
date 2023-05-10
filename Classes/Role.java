public class Role {
    private String name;
    private String caption;
    private int difficulty;
    private boolean onCard;
    private boolean isOccupied;
    private int[] dims; // probably not needed for first GUI
    private Player player;
    public Role(String name, String caption, int difficulty, boolean onCard, int[] dims) {
        this.name = name;
        this.caption = caption;
        this.difficulty = difficulty;
        this.onCard = onCard;
        this.isOccupied = false;
        this.dims = dims;
        this.player = null;
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

    public Player getPlayer(){
        return player;
    }
    
    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public void setPlayer(Player setPlayer){
        this.player = setPlayer;
    }
}
