public class Player {

    private int rank;
    private int money;
    private int credits;
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

    public void addRehearsalChip() {
        this.rehearsalChipCount++;
    }

    public void resetRehearsalChipCount() {
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

    // returns player's number. Every player has unique player number
    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setMoney(int newMoney){
        this.money = newMoney;
    }

    public void setCredits(int newCredits){
        this.credits = newCredits;
    }

    public void setRank(int newRank){
        this.rank = newRank;
    }

}
