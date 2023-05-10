import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private HashMap<String, Room> rooms; // name of each room (string) -> Room object
    private String[] playerLocations;
    private Player[] players;
    private HashMap<String, Scene> scenes;
    private ArrayList<Scene> unusedScenes;
    private Random rand;
    

    public Board(int numPlayers, Room[] roomData, Scene[] sceneData, Random rand) throws Exception {
        this.unusedScenes = new ArrayList<Scene>();
        rooms = new HashMap<String, Room>();
        scenes = new HashMap<String, Scene>();
        playerLocations = new String[numPlayers];
        for (Room room : roomData) {
            rooms.put(room.getRoomName().toLowerCase(), room);
        }
        for (Scene scene: sceneData) {
            scenes.put(scene.getName().toLowerCase(), scene);
            this.unusedScenes.add(scene);
        }
        this.rand = rand;
        resetBoard();

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
        String[] adjacentRooms = getRoom(start).getAdjacentRoomNames();

        for(String x : adjacentRooms){
            if(x.equalsIgnoreCase(destination)){
                isValid = true;
                break;
            }
        }
        
        return isValid;
    }

    private Room getRoom(String room) {
        return this.rooms.get(room.toLowerCase());
    }

    // resets board
    public void resetBoard() throws Exception {
        // move players to trailer
        for(int i = 0; i < playerLocations.length; i++){
            playerLocations[i] = "Trailer";
        }

        // change scene card in every room
        for (Room r : rooms.values()) {
            if (unusedScenes.size() == 0) {
                throw new Exception("No more scene cards left");
            }
            int roll = rand.nextInt(0, unusedScenes.size());
            r.setSceneCard(unusedScenes.get(roll));
            unusedScenes.remove(roll);
        }
    }

    // moves player from one room to an adjacent room
    public boolean movePlayer(int playerNum, String destination) {
    
        boolean isValid = isValidMove(playerLocations[playerNum - 1],destination);
        if(isValid){
            playerLocations[playerNum - 1] = destination;
            getRoom(destination).getSceneCard().flipCard();
        }

        return isValid;
        
    }

    // returns current player room based on id
    public String getPlayerRoom(int playerNum) {
        return playerLocations[playerNum - 1];
    }

    public String[] getRoomNeighbors(String room) {
        return getRoom(room).getAdjacentRoomNames();
    }

    public String[][] getRoomRoles(String room) {
        Room r = getRoom(room);
        if (!r.hasScene()) {
            return null;
        }
        Role[] roles = r.getRoles();
        return getRoleInfo(roles);

    }

    public String[][] getSceneRoles(String room) {
        Room r = getRoom(room);
        if (!r.hasScene()) {
            return null;
        }
        Scene s = r.getSceneCard();
        return getRoleInfo(s.getRoles());
    }

    public String[] getSceneInfo(String room) {
        Room r = getRoom(room);
        if (!r.hasScene()) {
            return null;
        }
        Scene s = r.getSceneCard();
        String[] sceneInfo = new String[5];
        sceneInfo[0] = s.getName();
        sceneInfo[1] = s.getCaption();
        sceneInfo[2] = Integer.toString(s.getBudget());
        sceneInfo[3] = Integer.toString(s.getSceneNumber());
        sceneInfo[4] = Boolean.toString(s.isFlipped());
        return sceneInfo;


    }

    private String[][] getRoleInfo(Role[] roles) {
        int numRoles = roles.length;
        String[][] info = new String[numRoles][5];
        for (int i = 0; i < numRoles; i++) {
            Role role = roles[i];
            info[i][0] = role.getName();
            info[i][1] = role.getCaption();
            info[i][2] = Integer.toString(role.getDifficulty());
            info[i][3] = Boolean.toString(role.getOnCard());
            info[i][4] = Boolean.toString(role.getOccupied());
        }
        return info;
    }

}