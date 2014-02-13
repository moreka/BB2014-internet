package javachallenge.server;

import javachallenge.client.Client;
import javachallenge.units.Unit;
import javachallenge.util.*;

/**
 * Created by merhdad on 2/12/14.
 */
public class TeamServer extends Client {
    @Override
    public void step() {
        for (Unit unit: myUnits) {
            if (!unit.isArrived()) {
                for (int j = 0; j < 6; j++) {
                    Cell neighbor = map.getNeighborCell(unit.getCell(),Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                    if (unit.getCell().getEdge(Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == EdgeType.OPEN &&
                            (neighbor.getType() == CellType.TERRAIN || neighbor.getType() == CellType.DESTINATION ||
                                    neighbor.getType() == CellType.SPAWN || neighbor.getType() == CellType.MINE) &&
                            neighbor.getUnit() == null) {
                        move(unit, Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                        break;
                    }
                }
            }
        }
    }
}
