public class Player {

    private int rank;
    private int money;
    private int credits;
    private int playerNumber;
    private int rehearsalChipCount;
    private Role currentRole;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.rank = 1;
        this.money = 0;
        this.credits = 0;
        this.rehearsalChipCount = 0;
        this.currentRole = null;
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

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role role) throws Exception {
        if (currentRole!= null) {
            throw new Exception("Current player is already working a different role");
        }
        this.currentRole = role;
    }

    public void exitRole() {
        this.currentRole = null;
    }

}