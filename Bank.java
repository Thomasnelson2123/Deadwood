import java.io.ObjectInputStream.GetField;

public class Bank {
    
    public int getUpgradeCostMoney(int targetRank) throws Exception{
        if (targetRank > 6 || targetRank < 2) {
            throw new Exception("invalid rank");
        }
        int[] costs = new int[] {4, 10, 18, 28, 40};
        return costs[targetRank - 1];
    }

    public int getUpgradeCostCredits(int targetRank) throws Exception{
        if (targetRank > 6 || targetRank < 2) {
             throw new Exception("invalid rank");
        }
        return (targetRank - 1)*5;
    }

    public void upgradePlayer(Player player, boolean usingMoney){
        return;
    }
}
