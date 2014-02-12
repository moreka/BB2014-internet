package javachallenge.client;

import javachallenge.message.*;
import javachallenge.units.Unit;
import javachallenge.units.UnitCE;
import javachallenge.util.*;

import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public abstract class Client {
    protected ArrayList<Action> actionList;
    protected Map map;
    protected ArrayList<UnitCE> myUnits = new ArrayList<UnitCE>();

    public void init() {
        actionList = new ArrayList<Action>();
    }

    public abstract void step();

    public void update(ServerMessage message) {
        this.map.updateMap(message.getMoveDeltaList());
        this.map.updateMap(message.getGameDeltaList());
        this.map.updateMap(message.getWallDeltaList());
        for (Delta d : message.getGameDeltaList()) {
            if (d.getType() == DeltaType.SPAWN) {
                myUnits.add((UnitCE) map.getCellAt(d.getSource().getX(), d.getSource().getY()).getUnit());
            }
        }
    }

    public ClientMessage end() {
        return new ClientMessage(actionList);
    }
    public void move(UnitCE unit, Direction direction) {
        if (!unit.isArrived())
            actionList.add(new Action(ActionType.MOVE, new Point(unit.getCell().getX(), unit.getCell().getY()), direction));
    }

    public void makeWall(Cell cell, Direction direction) {
        Node[] nodes = map.getNodesFromCellAt(cell, direction);
        actionList.add(new Action(ActionType.MAKE_WALL, new Point(nodes[0].getX(), nodes[0].getY()), map.getDirectionFromTwoNodes(nodes[0], nodes[1])));
    }
}
