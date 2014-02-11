package javachallenge.units;

import javachallenge.util.Cell;
import javachallenge.util.Direction;

/**
 * Created by mohammad on 2/5/14.
 */
public abstract class Unit {
    private int teamId;
    private int unitId;
    private int id;
    private Direction move;
    private boolean alive = true;
    private boolean stay = true;

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
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
}
