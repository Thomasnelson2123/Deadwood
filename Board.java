import java.util.HashMap;

public class Board {
    private HashMap<String, Room> rooms; // name of each room (string) -> Room object
    private String[] playerLocations;
    private Player[] players;

    public Board(int numPlayers) {
        rooms = new HashMap<String, Room>();
        playerLocations = new String[numPlayers];
    }

    // populates the adjacentRooms[] array for each room
    public void assignRoomAdjacencies() {

    }

    // returns the object of a room based on its name
    public Room nameToObject(String name) {
        return null;
    }

    // determines if start/destination room are adjacent
    public boolean isValidMove(String start, String destination) {
        return true;
    }

    // resets board
    public void resetBoard() {

    }

    // moves player from one room to an adjacent room
    public void movePlayer(int playerNum, Room destination) {

    }

    // returns current player room based on id
    public String getPlayerRoom(int playerNum) {
        return null;
    }

}
