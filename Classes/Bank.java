import java.util.Random;
import java.util.Arrays;
//import java.util.Comparator;
import java.util.HashMap;

public class Bank {
    
    private Random rand;

    public Bank(Random rand){
        this.rand = rand;
    }

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
    public boolean upgradePlayer(Player player, int targetRank, boolean usingMoney){

        boolean isSuccess = true;

        //get cost
        int cost = 0;
        try{
            if(usingMoney){
                cost = getUpgradeCostMoney(targetRank);
            }else{
                cost = getUpgradeCostCredits(targetRank);
            }
        }catch(Exception ex){
            
        }
        
        //determine whether or not player is able to upgrade based on cost,
        //then apply upgrade and return
        if(usingMoney){
            int currentMoney = player.getMoney();
            if(currentMoney < cost){
                //failure
                isSuccess = false;
            }else{
                //success
                player.setMoney(currentMoney-cost);
                player.setRank(targetRank);
            }
        }else{
            int currentCredits = player.getCredits();
            if(currentCredits < cost){
                //failure
                isSuccess = false;
            }else{
                //success
                player.setCredits(currentCredits-cost);
                player.setRank(targetRank);
            }
        }

        return isSuccess;
    }

    public void actingReward(boolean success, boolean isOnCard, Player currentPlayer){
        int currentCredits = currentPlayer.getCredits();
        int currentMoney = currentPlayer.getMoney();
        
        if(isOnCard){
            if(success){
                currentPlayer.setCredits(currentCredits+2);
            }
            else{
                //you get nothing! you lose! good day sir!
            }
        }
        else{
            if(success){
                currentPlayer.setCredits(currentCredits+1);
                currentPlayer.setMoney(currentMoney+1);
            }
            else {
                currentPlayer.setMoney(currentMoney+1);
            }
        }
    }

    // pays players for completing a scene based on their roles + scene budget
    public void payPlayers(HashMap<Integer, Player> onCardRoles, HashMap<Integer, Player> offCardRoles, int budget){
        int[] rolls = new int[budget];
        
        //get rolls for paying on card
        for(int i = 0; i < budget; i++){
            rolls[i] = rand.nextInt(0,7);
        }
        //sort it
        Arrays.sort(rolls);

        //int j = 0;
        //iterate through it backwards, paying players respectively
        for(int i = rolls.length - 1; i >= 0; i--){
            
        }
    }
}
