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
            for (int j = 0; j < 6; j++)
                if (myUnit.getCell().getEdge(Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == EdgeType.OPEN &&
                        (map.getNeighborCell(myUnit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == CellType.TERRAIN ||
                                map.getNeighborCell(myUnit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == CellType.DESTINATION ||
                                map.getNeighborCell(myUnit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == CellType.SPAWN ||
                                map.getNeighborCell(myUnit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == CellType.MINE)) {
                    move(myUnit, Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                    break;
                }
    }
}
