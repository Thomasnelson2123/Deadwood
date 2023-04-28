public class Player {

    private int rank;
    private int money;
    private int credits;
    private Room currentLocation;
    private int playerNumber;
    private int rehearsalChipCount;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.rank = 1;
        this.money = 0;
        this.credits = 0;
        this.rehearsalChipCount = 0;
        //this.currentLocation = Trailer
    }

    public void addChip() {
        this.rehearsalChipCount++;
    }

    public void resetChipCount() {
        this.rehearsalChipCount = 0;
    }

    public int getRehearsalChipCount() {
        return rehearsalChipCount;
    }

    public int getMoney() {
        return money;
    }

    public int getCredits() {
        return credits;
    }

    public int getRank() {
        return rank;
    }

    public Room getCurrentLocation() {
        return currentLocation;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

}
