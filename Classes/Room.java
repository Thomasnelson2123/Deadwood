public class Room {
    private Role[] roles;
    private Scene sceneCard;
    private String roomName;
    private String[] adjacentRoomNames;
    private int[] dims;
    private ShotCounter[] shots;
    private boolean hasScene;
    public Room(Role[] roles, String roomName, String[] adjacentRoomNames, int[] dims, ShotCounter[] shots, boolean hasScene) {
        this.roles = roles;
        this.roomName = roomName;
        this.sceneCard = null;
        this.adjacentRoomNames = adjacentRoomNames;
        this.dims = dims;
        this.shots = shots;
        this.hasScene = hasScene;
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

    // returns int for total number of shot counters, including completed ones
    public int getTotalShots() {
        return this.shots.length;
    }

    // returns int for the number of uncompleted shot counters
    public int getShotsRemaining() {
        int total = 0;
        for (ShotCounter shot: this.shots) {
            if (shot.hasShot()) {
                total++;
            }
        }
        return total;
    }

    // removes one shot counter from the room
    public void removeShot(){
        for(ShotCounter shot: this.shots){
            if(shot.hasShot()){
                shot.setHasShot(false);
                return;
            }
        }

    }

    // resets all shot counters, makes them uncompleted
    public void resetShotCounters() {
        for (ShotCounter shot: this.shots) {
            shot.setHasShot(true);
        }
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

    public void removeSceneCard() {
        this.sceneCard = null;
        
    }

    public boolean canHaveScene() {
        return this.hasScene;
    }

}