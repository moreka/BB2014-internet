package javachallenge.client;

import javachallenge.message.Action;
import javachallenge.units.Unit;
import javachallenge.util.Map;

import java.util.ArrayList;

/**
 * Created by mohammad on 2/5/14.
 */
public abstract class Client {
    private ArrayList<Unit> units;
    private ArrayList<Action> actionList;
    private Map map;

    public abstract void step();
}
