package javachallenge.server;

import javachallenge.client.Client;
import javachallenge.util.Cell;
import javachallenge.util.Direction;
import javachallenge.util.EdgeType;

/**
 * Created by merhdad on 2/12/14.
 */
public class TeamServer extends Client {
    private int teamID;
    private int resources;
    private Cell spawn;
    private Cell destination;

    public TeamServer(int teamID, int resources, Cell spawn, Cell destination) {
        this.teamID = teamID;
        this.resources = resources;
        this.spawn = spawn;
        this.destination = destination;
    }

    @Override
    public void step() {
        for (int i = 0; i < myUnits.size(); i++)
            for (int j = 0; j < 6; j++)
                if (myUnits.get(i).getCell().getEdge(Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == EdgeType.OPEN) {
                    move(myUnits.get(i), Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                    break;
                }
    }
}
