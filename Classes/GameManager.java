import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


public class GameManager {
    
    private Board board;
    private Player[] players;
    private Bank bank;
    private GUI gui;
    private Gooey gui2;

    private Player currentPlayer;
    private int day;
    private int scenesLeft;
    private int maxDays = 4;
    private Random rand;

    // constructor
    public GameManager(Board board, Player[] players, Bank bank, Random rand) {
        this.bank = bank;
        this.board = board;
        this.players = players;
        this.gui = new GUI();
        this.gui2 = new Gooey();
        this.day = 1;
        this.scenesLeft = 10;
        this.rand = rand;

        if (players.length < 4) {
            maxDays = 3;
        }

        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1);
            players[i].setAvailableActions(new Action[] {Action.Work, Action.Move, Action.Upgrade});
            if (players.length == 5) {
                players[i].setCredits(2);           
            }
            if (players.length == 6) {
                players[i].setCredits(4);
            }
            if (players.length > 6) {
                players[i].setRank(2);
            }   

        }
        currentPlayer = players[0];



    }


    //takes user input from the gui and determines which functions should be run
    //also supplies each function with the relevant data it needs to function
    public void parseAction(String[] args){
        
        switch(args[0]){
            case "act":
                act(this.currentPlayer, getPlayerLocation(this.currentPlayer.getPlayerNum()));
                break;
            case "rehearse":
                rehearse(this.currentPlayer);
                break;
            case "work":
                work(this.currentPlayer);
                break;
            case "move":
                //move(args[1], this.currentPlayer);
                move(this.currentPlayer);
                break;
            case "upgrade":
                if (this.board.playerInOffice(this.currentPlayer.getPlayerNum())) {
                    String[] upgradeInfo = this.gui.promptForUpgrade();
                    upgradeRank(upgradeInfo[0], upgradeInfo[1], this.currentPlayer);
                }
                else {
                    this.gui.notInOffice();
                }
                break;
            case "end": //end turn
                endTurn();
                break;
            case "terminate":
                endGame();
                break;
            case "who":
                displayPlayerStats(this.currentPlayer);
                break;
            case "where":
                String location = this.getPlayerLocation(this.currentPlayer.getPlayerNum());
                String[][] roomRoleInfo = getRoomRoleInfo(location);
                String[][] sceneRoleInfo = getSceneRoleInfo(location);
                String[] neighbors = this.board.getRoomNeighbors(location);
                String[] sceneInfo = this.board.getSceneInfo(location);
                int[] shots = this.board.getShotCounters(location);

                this.gui.displayLocation(location, neighbors, roomRoleInfo, sceneRoleInfo, sceneInfo, shots);
                break;
        }
    }
    
    // resets players/scenes to next day
    public void nextDay(){
        this.board.resetBoard();
        this.scenesLeft = 10;
        this.gui.nextDay();
        this.day++;
    }

    public int getNumberOfPlayers() {
        return this.players.length;
    }

    //returns player info to gui for display with "who" command
    public void displayPlayerStats(Player player) {  
        this.gui.displayCurrentPlayerInfo(player.getPlayerNum(), player.getRank(), player.getMoney(), player.getCredits(), player.getRehearsalChipCount());
    }

    public int[] getPlayerStats() {
        Player player = this.getCurrentPlayer(); 
        return new int[] {(player.getPlayerNum()), player.getMoney(), player.getCredits(), player.getRehearsalChipCount()};
    }

    // resolve the "act" action a player may take
    public void act(Player player, String roomName){
        // check if they can even act rn
        //if player.actionstaken = none && has role
        if (isActionAvailable(Action.Act, player) &&  this.board.getShotCounters(roomName)[0]> 0) {
            player.setAvailableActions(Action.None);
            int diceRoll = this.rand.nextInt(1, 7) + player.getRehearsalChipCount();
            String[] sceneInfo = this.board.getSceneInfo(getPlayerLocation(player.getPlayerNum()));
            int budget = Integer.parseInt(sceneInfo[2]);
            String[] playerRoleInfo = this.board.getPlayerRoleInfo(player.getPlayerNum());
            
            //give player credits/money
            boolean isOnCard = Boolean.parseBoolean(playerRoleInfo[3]);
            boolean actSuccess = diceRoll >= budget;
            // reward player based on whether they succeeded and whether they were on the card
            bank.actingReward(actSuccess, isOnCard, player);

            //notify gui about whether or not acting was a success
            gui2.actNotification(actSuccess);

            //remove shot counter
            if(actSuccess){
                board.removeShotCounter(roomName);
            }

            //if shot counter = 0, reward all players working on scene
            if(board.getShotCounters(roomName)[0] == 0){
                //pays players, removes their roles
                this.sceneWrap(roomName);
                gui2.sceneWrap();

                // remove scene card
                this.board.removeCard(roomName);

                // make it so you cannot take a role in this room
                // this is handled in takerole - you cant take a role in a room
                // if that room's shot counters = 0
                

            }

        }
        else {
            this.gui2.cannotAct();
        }
        
    }

    // resolve the "rehearse" action a player may take
    public void rehearse(Player player){
        
        //check if the player is able to rehearse
        if (this.isActionAvailable(Action.Rehearse, player)) {

            //do not let player rehearse if they dont have a role
            String[] playerRoleInfo = this.board.getPlayerRoleInfo(player.getPlayerNum());
            if(playerRoleInfo != null){
                //do not let player rehearse if they have the max number of rehearsal chips
                String[] sceneInfo = this.board.getSceneInfo(getPlayerLocation(player.getPlayerNum()));
                int budget = Integer.parseInt(sceneInfo[2]);
                int playerRehearsalChips = player.getRehearsalChipCount();

                //its -1 because the player will, at minimum, roll a 1
                //meaning if say budget = 6 and the player has 5 rehearsal chips,
                //they shouldnt be able to get any more because
                //roll of 1 + 5 chips = 6
                //gaurunteed success
                if(playerRehearsalChips < budget-1){
                    //success
                    player.addRehearsalChip();
                    gui2.addRehearsalChip(player.getRehearsalChipCount());
                    player.setAvailableActions(new Action[] {Action.None});
                }else{
                    gui2.tooManyChips();
                }
            }

        }
        else if (player.isWorking()){
            gui2.actionAlreadyTaken();
        }  
        else {
            gui2.chipsButNoRole();
        }
    }

    public void work(Player player) {
        // get room player is in
        String playerRoom = board.getPlayerRoom(player.getPlayerNum());
        // player is already working a role, cannot take another
        if (player.isWorking()) {
            this.gui2.alreadyWorking();
        }
        //if shot counters = 0, this scene has already been completed
        else if(board.getShotCounters(playerRoom)[0] == 0){
            if (playerRoom.equalsIgnoreCase("Trailer") || playerRoom.equalsIgnoreCase("Office")) {
                this.gui2.scenelessRoom();
            }
            else {
                this.gui2.noShotCounters();
            }
        }
        else {
            String response = this.gui2.takeRole();
            takeRole(response, player);
        }
    }


    // resolve the "take role" action a player may take
    public void takeRole(String role, Player player){
        
        String playerRoom = board.getPlayerRoom(player.getPlayerNum());
        // role isn't in same room as player, they can't take that role
        if (!board.isRoleInRoom(playerRoom, role)) {
            this.gui2.roleNotInRoom();
        }
        // role IS in room, but it isn't available
        else if (!board.isRoleAvailable(role)) {
            this.gui.roleNotAvailable();
        }
        // role IS available, but player does not have high enough of a rank
        else if (this.board.getRoleDifficulty(role) > player.getRank()) {
            this.gui.rankTooLow();
        }
        else{
            // at this point role is available, tell player and update things
            this.gui.displayTakeRole(player.getPlayerNum(), role);
            //this.currentPlayer.setWorking(true);
            player.setAvailableActions(new Action[] {Action.None});
            this.board.setPlayerRole(role, player.getPlayerNum());
            player.setWorking(true);
        }
               
    }

    // resolve the "move" action a player may take
    public void move(Player player) {
        if(!canCurrentPlayerMove()){
            if(isCurrentPlayerWorking()){
                gui.cannotMoveWithRole();
            }else{
                gui.actionAlreadyTaken();
            }
        }else {
            //get room to move to
            String destinationRoom = gui.getDestinationRoom();

            //figure out if thats valid
            if (destinationRoom != null) {
                boolean success = board.movePlayer(player.getPlayerNum(), destinationRoom);
                if (!success) {
                    gui.invalidMove(this.getPlayerRoomNeighbors());
                }
                else {
                    //this.currentPlayer.setCanMove(false);
                    player.setAvailableActions(new Action[] {Action.Work, Action.Upgrade});
                    gui.displayMove(player.getPlayerNum(), destinationRoom);
                    
                }
            }
        }
    }

    public boolean checkCanMove() {
        if(!canCurrentPlayerMove()){
            if(isCurrentPlayerWorking()){
                gui2.cannotMoveWithRole();
            }else{
                gui2.actionAlreadyTaken();
            }
            return false;
        }
        return true;
    }

    public boolean checkCanWork(){
        String playerRoom = board.getPlayerRoom(getCurrentPlayerNum());
        
        if(!canCurrentPlayerWork()){
            gui2.actionAlreadyTaken();
        }else if(isCurrentPlayerWorking()){
            gui2.alreadyWorking();
        }else if(board.getShotCounters(playerRoom)[0] == 0){
            if (playerRoom.equalsIgnoreCase("Trailer") || playerRoom.equalsIgnoreCase("Office")) {
                this.gui2.scenelessRoom();
            }
            else {
                this.gui2.noShotCounters();
            }
        }else{
            return true;
        }

        return false;
    }

    // moves the player without checking whether or not the move is valid at all
    public void movePlayerOverride(String destination){
        boolean success = board.movePlayer(currentPlayer.getPlayerNum(), destination);
        if(!success){
            gui2.moveOverrideFailed();
        }else{
            currentPlayer.setAvailableActions(new Action[] {Action.Work, Action.Upgrade});
        }
        
    }

    public void takeRoleOverride(String roleName){
        Player player = getCurrentPlayer();
        player.setAvailableActions(new Action[] {Action.None});
        this.board.setPlayerRole(roleName, player.getPlayerNum());
        player.setWorking(true);
    }

    // resolve the "upgrade rank" action a player may take
    public void upgradeRank(String targetRank, String paymentType, Player player){
        
        int targetRankInt;
        try {
            targetRankInt = Integer.parseInt(targetRank.trim());
        } catch (Exception e) {
            //System.out.println("cant parse int");
            gui.invalidUpgrade(true);
            return;
        }
        if (targetRankInt > 6 || targetRankInt < player.getRank()) {
            gui.invalidUpgrade(false);
            return;
        }
        
        boolean isUsingMoney = true;

        boolean success = false;
        boolean badInput = false;

        if(paymentType.equalsIgnoreCase("money")){
            isUsingMoney = true;
            success = this.bank.upgradePlayer(player,targetRankInt,isUsingMoney);
        }else if(paymentType.equalsIgnoreCase( "credits")){
            isUsingMoney = false;
            success = this.bank.upgradePlayer(player,targetRankInt,isUsingMoney);
        }else{
            //System.out.println("it isnt moni or credit");
            badInput = true;
        }

        if(!success){
            gui.invalidUpgrade(badInput);
        }else{
            gui.upgradeSuccess();
        }
    }

    // resolve all actions when a scene ends. This includes:
    // pay players on card for completing a scene
    // pay players off card for completing the scene
    // reset roles of players working in this room
    // reset rehearsal chips of players working in this room
    public void sceneWrap(String room){
        // can get role for every player
        // can then check if role is in the room being completed
        // pass into bank only the players in the room, and neccessary role info

        String[][] sceneRoleInfo = this.board.getSceneRoles(room);
        int budget = Integer.parseInt(this.board.getSceneInfo(room)[2]);

        //sorted array of difficulties of each role in scene, in descending order
        //eg if a scene has 3 roles, which require rank 1, 3, and 5 to take respectively
        //this array would contain [5, 3, 1]
        int[] onCardDifficulties = new int[sceneRoleInfo.length];
        for(int i = 0; i < onCardDifficulties.length; i++){
            onCardDifficulties[i] = Integer.parseInt(sceneRoleInfo[i][2]);
        }
        Arrays.sort(onCardDifficulties);
        Collections.reverse(Arrays.asList(onCardDifficulties));

        //these dont need to be sorted, but they need to be tied in order to each other
        //eg if we have player A has a role of 5 difficulty, and player B has a role of 3 difficulty, we want this:
        //[player A, player B] and [5, 3]
        //[player B, player A] and [3, 5]
        //
        //so that way if we do playerlist[0] and playerroledifficulties[0] they are for the same player

        board.resetOffCardRoles(room);

        ArrayList<Player> playerOnCardList = new ArrayList<Player>();
        ArrayList<Integer> playerRoleDifficulties = new ArrayList<Integer>();

        sceneWrapHelper(playerRoleDifficulties, playerOnCardList, sceneRoleInfo);
        this.bank.payPlayersOnCard(budget,onCardDifficulties,playerOnCardList,playerRoleDifficulties);

        boolean isPlayersOnCard = true;
        if(playerOnCardList.size() == 0){
            isPlayersOnCard = false;
        }

        ArrayList<Player> playerOffCardList = new ArrayList<Player>();
        ArrayList<Integer> offCardDifficulties = new ArrayList<Integer>();

        String[][] roomRoleInfo = this.board.getRoomRoles(room);
        sceneWrapHelper(offCardDifficulties, playerOffCardList, roomRoleInfo);

        // decrement the count of scenes left on the board
        this.scenesLeft--;
        this.bank.payPlayersOffCard(isPlayersOnCard, playerOffCardList, offCardDifficulties);

        //remove all player's roles and rehearsal chips who were working on this scene
        removePlayerRolesAndChips(playerOnCardList, playerOffCardList);

        // set all off card role's occupied vars to false
        //Role[] roles = board.getRoom(room).getRoles();
    }

    // gets a list of players and the difficulties of the roles they are working on
    // can be used for roles off or on the card
    public void sceneWrapHelper(ArrayList<Integer> roleDifficulties, ArrayList<Player> playersList, String[][] roleInfoArray) {

        // iterates through all the info for each role
        for (String[] roleInfo: roleInfoArray) {
            Player p = null;
            // iterate through every player
            for (Player player: this.players) {
                // gets role info for player; ignores if null
                String[] playerRoleInfo = this.board.getPlayerRoleInfo(player.getPlayerNum());
                if (playerRoleInfo == null) {
                    continue;
                }
                String playerRoleName = playerRoleInfo[0];
                // player working role on card
                if (playerRoleName.equals(roleInfo[0])) {
                    p = player;
                    break;
                }
            }

            if(p != null){
                playersList.add(p);
                int roleDifficulty = Integer.parseInt(roleInfo[2]);
                roleDifficulties.add(roleDifficulty);
            }
        }
    }



    // for all players given, removes roles (sets to null) and resets rehearsal chips (sets to 0)
    public void removePlayerRolesAndChips (ArrayList<Player> playersOnCard, ArrayList<Player> playersOffCard){
        //iterate through players on card and set their roles to null
        for(int i = 0; i < playersOnCard.size(); i++){
            Player player = playersOnCard.get(i);
            int currentPlayerNum = player.getPlayerNum();
            board.removePlayerRole(currentPlayerNum);
            player.setWorking(false);
            player.resetRehearsalChipCount();
        }

        //iterate through players off card and set their roles to null
        for(int i = 0; i < playersOffCard.size(); i++){
            Player player = playersOffCard.get(i);
            int currentPlayerNum = player.getPlayerNum();
            board.removePlayerRole(currentPlayerNum);
            player.setWorking(false);
            player.resetRehearsalChipCount();
        }
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getCurrentPlayerNum(){
        return getCurrentPlayer().getPlayerNum();
    }

    public String getPlayerLocation(int playerNum){
        return board.getPlayerRoom(playerNum);
    }

    public String[] getPlayerRoomNeighbors() {
        String room = board.getPlayerRoom(getCurrentPlayerNum());
        return board.getRoomNeighbors(room);
    }

    // ends the current player's turn
    // resets current player's abilities and sets the next player in line to be currentPlayer
    public void endTurn(){
        // reset players abilities
        // set current player to next player
        Action[] actions;
        if (!this.currentPlayer.isWorking()) {
            actions = new Action[] {Action.Work,Action.Upgrade, Action.Move};
        }
        else {
            actions = new Action[] {Action.Act,Action.Rehearse};
        }
        this.currentPlayer.setAvailableActions(actions);
        int currentPlayerNum = currentPlayer.getPlayerNum();
        this.currentPlayer = this.players[currentPlayerNum % this.players.length];
    }

    // end current game session, close game
    public void endGame(){
        System.exit(1);
    }

    public boolean canCurrentPlayerMove() {
        return this.isActionAvailable(Action.Move, this.currentPlayer);
    }

    public boolean canCurrentPlayerWork() {
        return this.isActionAvailable(Action.Work, this.currentPlayer);
    }

    public boolean isCurrentPlayerWorking() {
        return this.currentPlayer.isWorking();
    }

    public String[][] getRoomRoleInfo(String room) {
        return this.board.getRoomRoles(room);
    }

    public String[][] getSceneRoleInfo(String room) {
        return this.board.getSceneRoles(room);
    }

    public String[] getSceneInfo(String room) {
        return this.board.getSceneInfo(room);
    }

    public int[] getShotCounterInfo(String room) {
        return this.board.getShotCounters(room);
    }

    // returns a boolean to see if the player provided is able to perform the given action
    public boolean isActionAvailable(Action action, Player player) {
        Action[] actions = player.getAvailableActions();
        for (Action a: actions) {
            if (a.equals(action)) {
                return true;
            }
        }
        return false;
    }

    //called at the end of a game to calculate + pass to gui who the winner(s) are.
    public void determineWinner(){
        
        //we have an arraylist of winners that will usually just be one winner
        //but in the event of a tie, it will contain all players that tie.
        ArrayList<Player> winners = new ArrayList<Player>();
        int highScore = 0;

        //populate winners array
        for(int i = 0; i < players.length; i++){
            int currentScore = 0;
            currentScore += this.players[i].getCredits();
            currentScore += this.players[i].getMoney();
            currentScore += this.players[i].getRank() * 5;

            if(currentScore > highScore){
                //new high score! you are the current only winner
                winners.clear();
                winners.add(this.players[i]);
                highScore = currentScore;
            }
            else if(currentScore == highScore){
                //you tied with someone else! add to winners list
                winners.add(this.players[i]);
                //dont need to set highscore = currentscore bc thats already true
            }
        }

        //print to gui
        gui.displayWinners(winners);
    }

    // given a player, returns all available roles in their current room
    public String[] getAllAvailableRoles(Player player){
        return board.availableRoles(player);
    }

    public String[][] getOnCardRoleDims() {
        return this.board.getOnCardRoleDims();
    }

    public String[][] getOffCardRoleDims() {
        return this.board.getOffCardRoleDims();
    }

    public String[][] getRoomDims() {
        return this.board.getAllRoomDims();
    }

    // return specified player's dims
    public int[] getPlayerDims(int playerNum){
        Player p = players[playerNum];
        if(p.isWorking()){
            // return dims of their role
            return this.board.getPlayerRoleDims(playerNum);
        }else{
            // return dims of room, plus offset
            // offset is handled in board method
            return this.board.getPlayerRoomDims(playerNum);
        }
    }

    public boolean playerIsWorking(int playerNum){
        Player p = players[playerNum];
        return p.isWorking();
    }

}