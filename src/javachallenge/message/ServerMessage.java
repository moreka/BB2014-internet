package javachallenge.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public class ServerMessage
        implements Serializable {

    private static final long serialVersionUID = 1855437094950563362L;
    private ArrayList<Delta> attackDeltaList,  // goes to updateMap()
            wallDeltaList,                     // goes to updateMap()
            moveDeltaList,                     // goes to updateMap()
            gameDeltaList;                     // goes to Client, including Agent_disappear, resource_changed

    public ServerMessage(ArrayList<Delta> attackDeltaList, ArrayList<Delta> wallDeltaList, ArrayList<Delta> moveDeltaList, ArrayList<Delta> gameDeltaList) {
        this.attackDeltaList = attackDeltaList;
        this.wallDeltaList = wallDeltaList;
        this.moveDeltaList = moveDeltaList;
        this.gameDeltaList = gameDeltaList;
    }

    public ServerMessage() {
        this.attackDeltaList = new ArrayList<Delta>();
        this.wallDeltaList = new ArrayList<Delta>();
        this.moveDeltaList = new ArrayList<Delta>();
        this.gameDeltaList = new ArrayList<Delta>();
    }
}
