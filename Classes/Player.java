enum Action {
    Work,
    Act,
    Rehearse,
    Move,
    Upgrade,
    None
}

public class Player {

    private int rank;
    private int money;
    private int credits;
    private int playerNumber;
    private int rehearsalChipCount;
    //private boolean canMove;
    private boolean isWorking;
    private Action[] availableActions;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.rank = 4;
        this.money = 0;
        this.credits = 0;
        this.rehearsalChipCount = 0;
        //this.canMove = true;
        this.isWorking = false;
        this.availableActions = null;
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
    public int getPlayerNum() {
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

    public Action[] getAvailableActions() {
        return availableActions;
    }

    // public void setAvailableActions(Action[] availableActions) {
    //     this.availableActions = availableActions;
    // }

    public void setAvailableActions(Action... actions) {
        this.availableActions = actions;
    }

    
    //public boolean canMove() {
    //    return this.canMove;
    //}

    public boolean isWorking() {
        return this.isWorking;
    }

    //public void setCanMove(boolean canMove) {
    //    this.canMove = canMove;
    //}

    public void setWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }
    

    // public Role getCurrentRole() {
    //     return currentRole;
    // }

    // public void setCurrentRole(Role role) throws Exception {
    //     if (currentRole!= null) {
    //         throw new Exception("Current player is already working a different role");
    //     }
    //     this.currentRole = role;
    // }

    // public void exitRole() {
    //     this.currentRole = null;
    // }

}