package javachallenge.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public class ServerMessage
        implements Serializable {

    private static final long serialVersionUID = 1855437094950563362L;
    private ArrayList<Delta> wallDeltaList,    // goes to updateMap()
            moveDeltaList,                     // goes to updateMap()
            gameDeltaList;                     // goes to Client, including Agent_disappear, resource_changed

    public ServerMessage() {
        this.wallDeltaList = new ArrayList<Delta>();
        this.moveDeltaList = new ArrayList<Delta>();
        this.gameDeltaList = new ArrayList<Delta>();
    }

    public ServerMessage(ArrayList<Delta> wallDeltaList, ArrayList<Delta> moveDeltaList, ArrayList<Delta> gameDeltaList) {
        this.wallDeltaList = wallDeltaList;
        this.moveDeltaList = moveDeltaList;
        this.gameDeltaList = gameDeltaList;
    }

    public ArrayList<Delta> getWallDeltaList() {
        return wallDeltaList;
    }

    public void setWallDeltaList(ArrayList<Delta> wallDeltaList) {
        this.wallDeltaList = wallDeltaList;
    }

    public ArrayList<Delta> getMoveDeltaList() {
        return moveDeltaList;
    }

    public void setMoveDeltaList(ArrayList<Delta> moveDeltaList) {
        this.moveDeltaList = moveDeltaList;
    }

    public ArrayList<Delta> getGameDeltaList() {
        return gameDeltaList;
    }

    public void setGameDeltaList(ArrayList<Delta> gameDeltaList) {
        this.gameDeltaList = gameDeltaList;
    }
}
