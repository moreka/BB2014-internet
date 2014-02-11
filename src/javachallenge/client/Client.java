package javachallenge.client;

import javachallenge.message.Action;
import javachallenge.message.ClientMessage;
import javachallenge.message.ServerMessage;
import javachallenge.units.Unit;
import javachallenge.util.Map;

import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public abstract class Client {
    protected ArrayList<Unit> units;
    protected ArrayList<Action> actionList;
    protected Map map;

    public abstract void step();

    public void update(ServerMessage message) {
        this.map.updateMap(message.getMoveDeltaList());
        this.map.updateMap(message.getGameDeltaList());
        this.map.updateMap(message.getWallDeltaList());
    }

    public ClientMessage end() {
        return new ClientMessage(actionList);
    }
}
