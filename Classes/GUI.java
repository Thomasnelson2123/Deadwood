import java.util.Scanner;

public class GUI {
    Controller controller;
    Scanner scanner;
    public GUI(Controller controller) {
        this.controller = controller;
        scanner = new Scanner(System.in);
    }

    // lets player type the command they want to execute
    public String getUserInput() {
        return scanner.nextLine();
    }

    // takes user input and decides what function to call
    public void parseUserInput(String input) {
        
    }

    // displays to user the active player
    public void activePlayer() {

    }

    // get current location of player
    public void getLocation() {

    }

    public void act() {

    }

    public void rehearse() {

    }

    public void move() {

    }

    public void takeRole() {

    }

    // ends the game early
    public void endGame() {

    }

    // ends the players turn
    public void endTurn() {

    }

    public void upgrade() {

    }




}
