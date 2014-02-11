package javachallenge.client;

import javachallenge.message.Action;
import javachallenge.units.Unit;
import javachallenge.util.Map;

import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public abstract class Client {
    protected ArrayList<Unit> units;
    protected ArrayList<Action> actionList;
    protected Map map;

    public abstract void step();
}
