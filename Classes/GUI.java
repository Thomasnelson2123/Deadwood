import java.util.ArrayList;
import java.util.Scanner;

public class GUI {
    private Scanner scanner;
    private GameManager manager;
    //ArrayList<String> roomList = new ArrayList<String>();
    public GUI(GameManager manager) {
        this.manager = manager;
        scanner = new Scanner(System.in);
        
    }

    // lets player type the command they want to execute
    public String getUserInput() {
        System.out.print("\nEnter command \n");
        return scanner.nextLine();
    }

    // takes user input and decides what function to call
    public String[] parseUserInput() {
        boolean invalidInput = true;
        while(invalidInput) {
            invalidInput = false;
            String input = getUserInput();
            switch(input) {
                case "where":
                    return getLocation();
                case "who":
                    return activePlayer();
                case "move":
                    return move();
                case "act":
                    return act();
                case "rehearse":
                    return rehearse();
                case "upgrade":
                    return upgrade();
                case "work":
                    return takeRole();
                case "end":
                    return endTurn();
                case "terminate":
                    return endGame();   
                default:
                    System.out.println("invalid command, try again");
                    invalidInput = true;
                    break;
            }         
        }
        return null; // unreachable code
    }


    // displays to user the active player
    public String[] activePlayer() {
        //Player currPlayer = manager.getCurrentPlayer();
        int playerNum = manager.getCurrentPlayerNum();

        System.out.println("Current Player num: " + playerNum);
        //print player info based on "currPlayer" object? eg rank, credits, etc


        return null;
    }

    // get current location of player
    // BAD CODE FIX LATER
    public String[] getLocation() {
        String location = manager.getPlayerLocation(manager.getCurrentPlayerNum());
        System.out.println("Current player is at: " + location);
        System.out.print("Nearby rooms are: ");
        displayNeighbors();
        printRoomInfo(location);
        return null;
    }

    public String[] act() {
        return new String[] {"act"};
    }

    public String[] rehearse() {
        return new String[] {"rehearse"};
    }

    public String[] move() {
        // current player is allowed to move, prompt them for where they would like to go
        if (manager.canCurrentPlayerMove()) {
            System.out.println("Where would you like to move to?");
            String destination = scanner.nextLine();
            return new String[] {"move", destination}; 
        } 
        // find out if it is because current player is working
        else if (this.manager.isCurrentPlayerWorking()) {
            System.out.println("You cannot leave your role until you wrap!");
            return new String[] {"move", null};
        }else{
            actionAlreadyTaken();
            return new String[] {"move", null};
        }

    }

    public String getDestination() {
        System.out.println("Where would you like to move to?");
        String destination = scanner.nextLine();
        return destination; 
    }

    public String[] takeRole() {
        System.out.println("Which role would you like to take?");
        String targetRole = scanner.nextLine();

        return new String[] {"work", targetRole};
    }

    // ends the game early
    public String[] endGame() {
        return new String[] {"terminate"};
    }

    // ends the players turn
    public String[] endTurn() {
        return new String[] {"end"};
    }

    public String[] upgrade() {
        System.out.println("Which rank would you like to upgrade to?");
        String targetRank = scanner.nextLine();

        System.out.println("Would you like to use [money] or [credits]?");
        String paymentType = scanner.nextLine();
        
        return new String[] {"upgrade", targetRank, paymentType};
    }

    
    public void invalidMove() {
        System.out.println("You cannot move there!");
        System.out.print("Valid options are: ");
        displayNeighbors();
    }

    public void displayMove(int playerNum, String room) {
        System.out.println("Player " + playerNum + " has moved to " + room);
    }

    private void displayNeighbors() {
        String[] neighbors = manager.getPlayerRoomNeighbors();
        for (String neighbor: neighbors) {
            System.out.print(neighbor + ", ");
        }
        System.out.print("\n");
    }

    public void invalidUpgrade(boolean badInput){
        System.out.println("Upgrade failed!");
        if(badInput){
            System.out.println("Payment type not detected. You may only pay with [money] or [credits].");
        }else{
            System.out.println("You do not possess enough resources to upgrade.");
        }
    }

    private void printRoomInfo(String room) {
        String[][] roomRoleInfo = this.manager.getRoomRoleInfo(room);
        String[][] sceneRoleInfo = this.manager.getSceneRoleInfo(room);
        String[] sceneInfo = this.manager.getSceneInfo(room);
        int[] shots = this.manager.getShotCounterInfo(room);

        //scene info
        //name - caption
        if(sceneInfo != null){
            System.out.println("SCENE: "+sceneInfo[0]+" - "+sceneInfo[1]);
            System.out.println("Budget: "+sceneInfo[2]+" - Scene number: "+sceneInfo[3]+" - Flipped: "+sceneInfo[4]);
            System.out.println("");
        }

        //scene role info
        if(sceneRoleInfo != null){
            System.out.println("On card roles: \n");
            for(int i = 0; i < sceneRoleInfo.length; i++){
                //name - caption
                System.out.println("ROLE: "+sceneRoleInfo[i][0] + " - "+sceneRoleInfo[i][1]);
                //difficulty - on card - is occupied
                System.out.println("difficulty: "+sceneRoleInfo[i][2]+" - On card: "+sceneRoleInfo[i][3]+" - Occupied: "+sceneRoleInfo[i][4]);
                System.out.println("");
            }
        }
        
        //normal role info
        if(roomRoleInfo != null){
            System.out.println("Off card roles: \n");
            for(int i = 0; i < roomRoleInfo.length; i++){
                //name - caption
                System.out.println("ROLE: "+roomRoleInfo[i][0] + " - "+roomRoleInfo[i][1]);
                //difficulty - on card - is occupied
                System.out.println("difficulty: "+roomRoleInfo[i][2]+" - On card: "+roomRoleInfo[i][3]+" - Occupied: "+roomRoleInfo[i][4]);
                System.out.println("");
            }
        }

        // display shots left
        if(shots != null) {
            System.out.println(shots[0] + " of " + shots[1] + " shots remaining");
            System.out.println();
        }

    }

    public void roleNotInRoom() {
        System.out.println("The specified role is not in your current room. You can only take on a role if you are near it!");
    }

    public void roleNotAvailable() {
        System.out.println("That role is not available!");
    }


    public void displayTakeRole(int playerNum, String role) {
        System.out.println("Player " + playerNum + " has taken the role of: " + role);
    }

    public void rankTooLow() {
        System.out.println("Your rank is too low to take that role!");
    }

    public void alreadyWorking() {
        System.out.println("Already working a role! Cannot leave until the scene has wrapped,");
    }

    public void actionAlreadyTaken(){
        System.out.println("Too many actions!");
    }

    public void cannotAct() {
        System.out.println("You must be working a role before you can act!");
    }

    public void sceneWrap(){
        System.out.println("That's a wrap! Scene completed.");
    }


}