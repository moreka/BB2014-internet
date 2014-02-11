package javachallenge.message;

import javachallenge.units.Unit;
import javachallenge.util.Direction;
import javachallenge.util.NodeDirection;
import javachallenge.util.Point;

/**
 * Created by mohammad on 2/6/14.
 */
public class Action {
    private ActionType type;
    private Direction direction = null;
    private NodeDirection nodeDirection = null;
    private Point position;

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public NodeDirection getNodeDirection() {
        return nodeDirection;
    }

    public void setNodeDirection(NodeDirection nodeDirection) {
        this.nodeDirection = nodeDirection;
    }
}
