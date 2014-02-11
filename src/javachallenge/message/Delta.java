package javachallenge.message;

import javachallenge.util.*;

import java.io.Serializable;

/**
 * Created by mohammad on 2/6/14.
 */
public class Delta
        implements Serializable {
    private static final long serialVersionUID = -4314305772186897307L;
    private DeltaType type;
    private Point source;
    private Point destination;
    private Point destinationWallie;
    //private Direction direction;
    //private NodeDirection nodeDirection;
    private int mineChange;

    public Delta(DeltaType type, Point source) {
        this(type, source, null, null, null);
    }

    public Delta(DeltaType type, Point mineCell, int mineChange) {
        this(type, mineCell, null, null, mineChange);
    }

    public Delta(DeltaType type, Point source, Point destination) {
        this(type, source, destination, null, null);
    }

    public Delta(DeltaType type, Point source, Point destination, Point destinationWallie) {
        this(type, source, destination, destinationWallie, null);
    }

    public Delta(DeltaType type, Point source, Point destination,/* Direction direction, NodeDirection nodeDirection,*/ Point destinationWallie, Integer mineChange) {
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.destinationWallie = destinationWallie;
        //this.direction = direction;
        //this.nodeDirection = nodeDirection;
        this.mineChange = mineChange;
    }

    public Point getDestination() {
        return this.destination;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }
/*
    public NodeDirection getNodeDirection() {
        return nodeDirection;
    }

    public void setNodeDirection(NodeDirection nodeDirection) {
        this.nodeDirection = nodeDirection;
    }
*/
    public DeltaType getType() {
        return type;
    }

    public void setType(DeltaType type) {
        this.type = type;
    }

    public Point getSource() {
        return source;
    }

    public void setSource(Point source) {
        this.source = source;
    }
/*
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
*/
    public int getMineChange() {
        return mineChange;
    }

    public void setMineChange(int mineChange) {
        this.mineChange = mineChange;
    }

    public Point getDestinationWallie() {
        return destinationWallie;
    }

    public void setDestinationWallie(Point destinationWallie) {
        this.destinationWallie = destinationWallie;
    }
}
