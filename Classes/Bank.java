
public class Bank {
    
    // returns the cost of an upgrade in cash based on the rank being purchased
    public int getUpgradeCostMoney(int targetRank) throws Exception{
        if (targetRank > 6 || targetRank < 2) {
            throw new Exception("invalid rank");
        }
        int[] costs = new int[] {4, 10, 18, 28, 40};
        return costs[targetRank - 1];
    }

    // returns the cost of an upgrade in credits based on the rank being purchased
    public int getUpgradeCostCredits(int targetRank) throws Exception{
        if (targetRank > 6 || targetRank < 2) {
             throw new Exception("invalid rank");
        }
        return (targetRank - 1)*5;
    }

    // upgrades current player rank and subtracts cost
    public void upgradePlayer(Player player, boolean usingMoney){
        return;
    }

    // pays players for completing a scene based on their roles + scene budget
    public void payPlayers(Player[] players, int budget, int[] roles, boolean[] rolesOnCard){

    }
}
