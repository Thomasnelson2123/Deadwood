public class ShotCounter {

    private int[] dims;
    private int id;
    private boolean hasShot;
    public ShotCounter(int id, int[] dims, boolean hasShot) {
        this.id = id;
        this.dims = dims;
        this.hasShot = hasShot;

    }

    public int[] getDims() {
        return dims;
    }

    public int getId() {
        return id;
    }

    public boolean hasShot() {
        return this.hasShot;
    }

    public void setHasShot(boolean hasShot) {
        this.hasShot = hasShot;
    }
}
