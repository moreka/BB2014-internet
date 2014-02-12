package javachallenge.client.teamcli;

import javachallenge.client.Client;
import javachallenge.message.*;
import javachallenge.units.Unit;
import javachallenge.units.UnitCE;
import javachallenge.util.*;

import java.util.ArrayList;


/**
 * Created by mohammad on 2/5/14.
 */
public class TeamClient extends Client {
    private int teamID;
    private int resources;
    private Cell spawn;
    private Cell destination;
    protected ArrayList<UnitCE> myUnits = new ArrayList<UnitCE>();

    public TeamClient(int teamID, int resources, Cell spawn, Cell destination) {
        this.teamID = teamID;
        this.resources = resources;
        this.spawn = spawn;
        this.destination = destination;
    }

    public void update(ServerMessage message) {
        super.update(message);
        for (Delta d : message.getGameDeltaList()) {
            if (d.getType() == DeltaType.SPAWN) {
                myUnits.add((UnitCE) map.getCellAt(d.getSource().getX(), d.getSource().getY()).getUnit());
            }
        }
    }

    @Override

    public void step() {
        for (int i = 0; i < myUnits.size(); i++) {
            move(myUnits.get(i), Direction.EAST);
        }
        makeWall(spawn, Direction.EAST);
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
