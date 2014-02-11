package javachallenge.units;

import javachallenge.util.Cell;
import javachallenge.util.Direction;

/**
 * Created by merhdad on 2/7/14.
 */
public class UnitCell extends Unit {
    private Direction move;
    private Cell cell;

    public Direction getMove() {
        return move;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
