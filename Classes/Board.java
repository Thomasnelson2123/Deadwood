import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private HashMap<String, Room> rooms; // name of each room (string) -> Room object
    private String[] playerLocations;
    private Role[] playerRoles;
    private HashMap<String, Scene> scenes;
    private ArrayList<Scene> unusedScenes;
    private Random rand;
    
    // constructor
    public Board(int numPlayers, Room[] roomData, Scene[] sceneData, Random rand) throws Exception {
        this.unusedScenes = new ArrayList<Scene>();
        this.rooms = new HashMap<String, Room>();
        this.scenes = new HashMap<String, Scene>();
        this.playerLocations = new String[numPlayers];
        this.playerRoles = new Role[numPlayers];
        for (Room room : roomData) {
            this.rooms.put(room.getRoomName().toLowerCase(), room);
        }
        for (Scene scene: sceneData) {
            this.scenes.put(scene.getName().toLowerCase(), scene);
            this.unusedScenes.add(scene);
        }
        this.rand = rand;
        resetBoard();

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

    private Scene getScene(String scene) {
        return this.scenes.get(scene.toLowerCase());
    }

    // resets board
    public void resetBoard(){
        // move players to trailer
        for(int i = 0; i < playerLocations.length; i++){
            playerLocations[i] = "Trailer";
        }

        // change scene card in every room
        for (Room r : rooms.values()) {
            if (unusedScenes.size() == 0) {
                System.out.println("No more scene cards left");
            }
            int roll = rand.nextInt(0, unusedScenes.size());
            r.setSceneCard(unusedScenes.get(roll));
            unusedScenes.remove(roll);
            r.resetShotCounters();
        }
    }

    // moves player from one room to an adjacent room
    public boolean movePlayer(int playerNum, String destination) {
    
        boolean isValid = isValidMove(playerLocations[playerNum - 1],destination);
        if(isValid){
            playerLocations[playerNum - 1] = destination;
            Scene s = getRoom(destination).getSceneCard();
            if (s != null) {
                getRoom(destination).getSceneCard().flipCard();
            }
        }

        return isValid;
        
    }

    // returns current player room based on id
    public String getPlayerRoom(int playerNum) {
        return playerLocations[playerNum - 1];
    }

    // returns which rooms are next to the specified room, used to determine if a player move is valid
    public String[] getRoomNeighbors(String room) {
        return getRoom(room).getAdjacentRoomNames();
    }

    //gets the roles inside of a room that are NOT on the scene card
    public String[][] getRoomRoles(String room) {
        Room r = getRoom(room);
        Scene s = r.getSceneCard();
        if (!r.canHaveScene() || s == null) {
            return null;
        }
        
        Role[] roles = r.getRoles();
        return getRoleInfo(roles);

    }

    //gets the roles on a scene card. Does NOT return roles that are off the scene card in the room.
    public String[][] getSceneRoles(String room) {
        Room r = getRoom(room);
        Scene s = r.getSceneCard();
        if (!r.canHaveScene() || s == null) {
            return null;
        }

        return getRoleInfo(s.getRoles());
    }

    // given a room, finds its scene and returns all relevant info on it
    // (name, caption, budget, scene number, and whether or not it's been flipped)
    public String[] getSceneInfo(String room) {
        Room r = getRoom(room);
        Scene s = r.getSceneCard();
        if (!r.canHaveScene() || s == null) {
            return null;
        }
        String[] sceneInfo = new String[5];
        sceneInfo[0] = s.getName();
        sceneInfo[1] = s.getCaption();
        sceneInfo[2] = Integer.toString(s.getBudget());
        sceneInfo[3] = Integer.toString(s.getSceneNumber());
        sceneInfo[4] = Boolean.toString(s.isFacingUp());
        return sceneInfo;


    }

    // given a role, returns all relevant info on it
    // (name, caption, difficulty, whether or not it's on a card, whether or not it's occupied)
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

    // returns the number of shot counters remaining in a given room's scene
    public int[] getShotCounters(String room) {
        Room r = this.getRoom(room);
        if (!r.canHaveScene()) {
            return new int[] {0, 0};
        }
        return new int[] {r.getShotsRemaining(), r.getTotalShots()};
    }

    // returns true if role is in room, returns false otherwise
    public boolean isRoleInRoom(String room, String role) {
        Room r = this.getRoom(room);
        Scene s = r.getSceneCard();
        Role[] roles;
        // check if role is on the scene card
        if (s != null) {
            roles = s.getRoles();
            if (roles == null) {
                return false;
            }
            for (Role sceneRole: s.getRoles()) {
                if (sceneRole.getName().equalsIgnoreCase(role)) {
                    // role is on the card, found it
                    return true;
                }
            }
        }
        roles = r.getRoles();
        if (roles == null) {
            return false;
        }
        for (Role roomRole: r.getRoles()) {
            if (roomRole.getName().equalsIgnoreCase(role)) {
                // role is in the room, found it
                return true;
            }
        }
        // role not in card or room, return false
        return false;

    }

    // checks every role on the board to match it's name to given role,
    // returns whether that role is free or not
    // will also return false if the role is not found
    public boolean isRoleAvailable(String role) {
        Role r = this.getRole(role);
        if (r == null) {
            return false;
        }
        return r.isRoleAvailable();

    }

    // sets a player's current role to null
    public void removePlayerRole(int playerNum){
        this.playerRoles[playerNum - 1] = null;
    }

    // asssign player playerNum to role
    public void setPlayerRole(String role, int playerNum){
        try {
            Role r = this.getRole(role);
            this.playerRoles[playerNum - 1] = r;
            r.setOccupied();
            
        } catch (Exception e) {
            throw e;
        }
    }

    private Role getRole(String role) {
        // search every room for role
        for (Room r: this.rooms.values()) {
            // check off card roles
            if (r.getRoles() != null) {
                for (Role roomRole : r.getRoles()) {
                    if (roomRole.getName().equalsIgnoreCase(role)) {
                        return roomRole;
                    }               
                }
            }
            // check on card roles
            Scene s = r.getSceneCard();
            if (s != null) {
                if (s.getRoles() != null) {
                    for (Role sceneRole: s.getRoles()) {
                        if (sceneRole.getName().equalsIgnoreCase(role)) {
                            return sceneRole;
                        }
                    }
                }
            }
        }

        return null;
    }

    // returns true if role's difficulty is less than or equal to rank
    public int getRoleDifficulty(String role) {
        Role r = this.getRole(role);
        // role doesn't exist, so return huge number - impossible to work that!
        if (r == null) {
            return Integer.MAX_VALUE;
        }
        return r.getDifficulty();
    }

    // given a player number, returns the role info for what the player is working
    public String[] getPlayerRoleInfo(int playerNum) {
        Role currentRole = this.playerRoles[playerNum - 1];
        if (currentRole == null) {
            return null;
        }
        String[] roleInfo = new String[5];
        roleInfo[0] = currentRole.getName();
        roleInfo[1] = currentRole.getCaption();
        roleInfo[2] = Integer.toString(currentRole.getDifficulty());
        roleInfo[3] = Boolean.toString(currentRole.getOnCard());
        roleInfo[4] = Boolean.toString(currentRole.getOccupied());
        return roleInfo;

    }

    // removes one shot counter from the scene card in the given room
    public void removeShotCounter(String currentRoom){
        Room room = this.getRoom(currentRoom);
        room.removeShot();
    }

    // removes the scene card from the current room
    public void removeCard(String room) {
        Room r = getRoom(room);
        r.removeSceneCard();
    }

    // returns whether or not the player is currently in the room "Office"
    // used for determining whether or not the player can take the upgrade action
    public boolean playerInOffice(int playerNum) {
        return this.playerLocations[playerNum - 1].equalsIgnoreCase("office");
    }

    public void resetOffCardRoles(String room){
        Room roomObj = getRoom(room);
        Role[] roles = roomObj.getRoles();
        for(int i = 0; i < roles.length; i++){
            roles[i].setNotOccupied();
            roles[i].setFinishedShooting(false);
        }
    }

    


}