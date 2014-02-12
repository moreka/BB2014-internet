package javachallenge.units;

import javachallenge.util.Cell;
import javachallenge.util.Direction;

public class Unit {
    private int id;
    private int teamId;
    private Direction move;
    private boolean alive = true;
    private boolean stay = true;
    private boolean arrived = false;
    private Cell cell;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Direction getMove() {
        return move;
    }

    public void setMove(Direction move) {
        this.move = move;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void stay (boolean input) {
        stay = input;
        //Add to action list.
    }

    public boolean isStay() {
        return stay;
    }

    public void setStay(boolean stay) {
        this.stay = stay;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }
}
