import java.util.ArrayList;
import java.util.Scanner;

public class GUI {
    Scanner scanner;
    GameManager manager;
    //ArrayList<String> roomList = new ArrayList<String>();
    public GUI(GameManager manager) {
        this.manager = manager;
        scanner = new Scanner(System.in);
        
    }

    // lets player type the command they want to execute
    public String getUserInput() {
        System.out.println("Enter command");
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
    public String[] getLocation() {
        String location = manager.getPlayerLocation(manager.getCurrentPlayerNum());
        System.out.println("Current player is at: " + location);
        return null;
    }

    public String[] act() {
        return new String[] {"act"};
    }

    public String[] rehearse() {
        return new String[] {"rehearse"};
    }

    public String[] move() {
        System.out.println("Where would you like to move to?");
        String destination = scanner.nextLine().toLowerCase();

        return new String[] {"move", destination};   

    }

    public String[] takeRole() {
        System.out.println("Which role would you like to take?");
        String targetRole = scanner.nextLine().toLowerCase();

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
        String targetRank = scanner.nextLine().toLowerCase();

        System.out.println("Would you like to use [money] or [credits]?");
        String paymentType = scanner.nextLine().toLowerCase();
        
        return new String[] {"upgrade", targetRank, paymentType};
    }




}
