import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public void payPlayersOnCard(int budget, int[] onCardDifficulties, ArrayList<Player> playerList, ArrayList<Integer> playerRoleDifficulties){
        int[] rolls = new int[budget];
        
        //get rolls for payment on card
        for(int i = 0; i < budget; i++){
            rolls[i] = rand.nextInt(0,7);
        }
        //sort it, reverse it
        Arrays.sort(rolls);
        Collections.reverse(Arrays.asList(rolls));

        //role difficulties is an array of int
        //representing the difficulty of each role, in descending order
        //eg if a scene has 3 roles, requiring rank 2, 4, and 5 respectively
        //the array would be [5, 4, 2]
        //
        //this has already been sorted when it was passed in, we dont need to sort it here

        //iterate through rolls and give each roll to the player who deserves it
        int totalOnCardRoles = onCardDifficulties.length;
        for(int i = 0; i< rolls.length; i++){
            //for each player,
            //if roleDifficultys[i % total number of on card roles] == this player.roledifficulty, pay that player rolls[i] money
            for(int j = 0; j < playerRoleDifficulties.size(); j++){
                int currentRoleDifficulty = (int) playerRoleDifficulties.get(j);
                if(onCardDifficulties[i % totalOnCardRoles] == currentRoleDifficulty){
                    //pay player j
                    Player playerToPay = (Player) playerList.get(j);
                    int playerMoney = playerToPay.getMoney();

                    playerToPay.setMoney(playerMoney + rolls[i]);

                    //break out of this loop - there shouldnt be 2 players with the same difficulty role
                    break;
                }
            }
        }
    }

    public void payPlayersOffCard(boolean anyPlayersOnCard, ArrayList<Player> playerList, ArrayList<Integer> playerRoleDifficulties){
        //extras dont get a bonus if nobody was working on the card
        if(anyPlayersOnCard){
            //pay each player as much money as their role's difficulty
            for(int i = 0; i < playerList.size(); i++){
                Player p = playerList.get(i);
                int mony = p.getMoney();
                int playerRoleDifficulty = (int)playerRoleDifficulties.get(i);
                p.setMoney(mony + playerRoleDifficulty);
            }
        }
    }
}
