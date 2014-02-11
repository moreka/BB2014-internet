package javachallenge.units;

import javachallenge.util.Direction;
import javachallenge.util.Node;

/**
 * Created by merhdad on 2/6/14.
 */
public class UnitWallie extends Unit {
    private Direction move1;
    private Direction move2;
    private Direction wall;
    private Node node;
    private boolean busy = false;

    public void move1 (Direction input) {
        move1 = input;
        //Add to action list.
    }

    public Direction getMove1() {
        return move1;
    }

    public void move2 (Direction input) {
        move2 = input;
        //Add to action list.
    }

    public Direction getMove2() {
        return move2;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void wall (Direction input) {
        wall = input;
        //Add to action list.
    }

    public Direction getWall() {
        return wall;
    }

    public boolean isBusy() {
        return busy;
    }
}
