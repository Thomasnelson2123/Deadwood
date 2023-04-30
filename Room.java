public class Room {
    private int maxShots;
    private int currentShots;
    private Role[] roles;
    private Scene sceneCard;
    private String roomName;
    private String[] adjacentRoomNames;
    public Room(int maxShots, Role[] roles, String roomName) {
        this.maxShots = maxShots;
        this.roles = roles;
        this.roomName = roomName;
        this.currentShots = this.maxShots;
        this.sceneCard = null;
    }

    //#region getters
    public String getRoomName() {
        return roomName;
    }

    public Role[] getRoles() {
        return roles;
    }

    public int getCurrentShots() {
        return currentShots;
    }

    public Scene getSceneCard() {
        return sceneCard;
    }
    //#endregion

    public void setSceneCard(Scene sceneCard) {
        this.sceneCard = sceneCard;
    }

    // each room keeps track of the rooms adjacent to it
    public void setAdjacentRoomNames(String[] adjacentRoomNames) {
        this.adjacentRoomNames = adjacentRoomNames;
    }
}
