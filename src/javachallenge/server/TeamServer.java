package javachallenge.server;

import javachallenge.client.Client;
import javachallenge.units.Unit;
import javachallenge.util.*;

import java.util.HashMap;

/**
 * Created by merhdad on 2/12/14.
 */
public class TeamServer extends Client {

    HashMap<Unit, Boolean> isBlocked = new HashMap<Unit, Boolean>();

    public void init() {
        for (Unit unit : myUnits) {
            if (!isBlocked.containsKey(unit))
                isBlocked.put(unit, false);
        }
    }

    @Override
    public void step() {
        init();
        for (Unit unit : myUnits) {
            if (!unit.isArrived()) {
                boolean isMoved = false;
                for (int j = 0; j < 6; j++) {
                    Cell neighbor = map.getNeighborCell(unit.getCell(), Direction.values()[(Direction.EAST.ordinal() + j) % 6]);

                    if (unit.getCell().getEdge(Direction.values()[(Direction.EAST.ordinal() + j) % 6]).getType() == EdgeType.OPEN &&
                            (neighbor.getType() == CellType.TERRAIN || neighbor.getType() == CellType.DESTINATION ||
                                    neighbor.getType() == CellType.SPAWN || neighbor.getType() == CellType.MINE) &&
                            ((neighbor.getUnit() == null) ||
                                    ((neighbor.getUnit().getTeamId() == unit.getTeamId()) && neighbor.getUnit().getId() < unit.getId()))
                            && !isBlocked.get(neighbor.getUnit())
                            ) {
                        move(unit, Direction.values()[(Direction.EAST.ordinal() + j) % 6]);
                        isMoved = true;
                        break;
                    }
                }
                if (!isMoved) {
                    isBlocked.put(unit, true);
                }
            }
        }
    }
}
