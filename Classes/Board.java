import java.util.HashMap;

public class Board {
    private HashMap<String, Room> rooms; // name of each room (string) -> Room object
    private String[] playerLocations;
    private Player[] players;
    private HashMap<String, Scene> scenes;

    public Board(int numPlayers, Room[] roomData, Scene[] sceneData) {
        rooms = new HashMap<String, Room>();
        scenes = new HashMap<String, Scene>();
        playerLocations = new String[numPlayers];
        for (Room room : roomData) {
            rooms.put(room.getRoomName(), room);
        }
        for (Scene scene: sceneData) {
            scenes.put(scene.getName(), scene);
        }
    }

    // populates the adjacentRooms[] array for each room
    public void assignRoomAdjacencies() {

    }

    // returns the object of a room based on its name
    public Room nameToObject(String name) {
        //uses hash map to get room object from name
        return rooms.get(name);
    }

    // determines if start/destination room are adjacent
    public boolean isValidMove(String start, String destination) {
        
        boolean isValid = false;
        
        //get list of adjacent rooms
        String[] adjacentRooms = rooms.get(start).getAdjacentRoomNames();

        for(String x : adjacentRooms){
            if(x.equals(destination)){
                isValid = true;
                break;
            }
        }
        
        return isValid;
    }

    // resets board
    public void resetBoard() {

    }

    // moves player from one room to an adjacent room
    public boolean movePlayer(int playerNum, String destination) {
    
        boolean isValid = isValidMove(playerLocations[playerNum],destination);
        if(isValid){
            playerLocations[playerNum] = destination;
        }

        return isValid;
        
    }

    // returns current player room based on id
    public String getPlayerRoom(int playerNum) {
        return playerLocations[playerNum];
    }

}
