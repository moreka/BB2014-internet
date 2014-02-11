package javachallenge.units;

import javachallenge.util.Direction;

/**
 * Created by merhdad on 2/6/14.
 */
public class UnitAttacker extends UnitCell {
    private Direction attack;
    private int bombersKilled = 0;

    public void attack (Direction input) {
        attack = input;
        //Add to action list.
    }

    public Direction getAttack() {
        return attack;
    }

    public void increaseBombersKilled () {
        bombersKilled++;
    }

    public int getBombersKilled() {
        return bombersKilled;

    }
}
