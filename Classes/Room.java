public class Room {
    private Role[] roles;
    private Scene sceneCard;
    private String roomName;
    private String[] adjacentRoomNames;
    private int[] dims;
    private ShotCounter[] shots;
    public Room(Role[] roles, String roomName, String[] adjacentRoomNames, int[] dims, ShotCounter[] shots) {
        this.roles = roles;
        this.roomName = roomName;
        this.sceneCard = null;
        this.adjacentRoomNames = adjacentRoomNames;
        this.dims = dims;
        this.shots = shots;
    }

    //#region getters
    public String getRoomName() {
        return roomName;
    }

    public Role[] getRoles() {
        return roles;
    }

    public Scene getSceneCard() {
        return sceneCard;
    }

    public String[] getAdjacentRoomNames() {
        return adjacentRoomNames;
    }

     //#endregion

    // for debug
    public void printRoomInfo() {
        System.out.println(roomName);
        for (ShotCounter shot: shots) {
            System.out.println(shot.getId());
        }
        // System.out.print("adjacent: ");
        // for (String str: adjacentRoomNames) {
        //     System.out.print(str + ", ");
        // }
    }

    public void setSceneCard(Scene sceneCard) {
        this.sceneCard = sceneCard;
    }


}
