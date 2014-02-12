package javachallenge.server;

import javachallenge.client.Client;
import javachallenge.units.Unit;
import javachallenge.util.Cell;
import javachallenge.util.CellType;
import javachallenge.util.Direction;
import javachallenge.util.EdgeType;

/**
 * Created by merhdad on 2/12/14.
 */
public class TeamServer extends Client {
    @Override
    public void step() {
        for (Unit myUnit : myUnits)
            if (!myUnit.isArrived()) {
                for (int j = 0; j < 6; j++) {
                    Cell neighbor = map.getNeighborCell(myUnit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                    if (myUnit.getCell().getEdge(Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == EdgeType.OPEN &&
                            (neighbor.getType() == CellType.TERRAIN || neighbor.getType() == CellType.DESTINATION ||
                                    neighbor.getType() == CellType.SPAWN || neighbor.getType() == CellType.MINE)) {
                        move(myUnit, Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                        break;
                    }
                }
            }
    }
}
