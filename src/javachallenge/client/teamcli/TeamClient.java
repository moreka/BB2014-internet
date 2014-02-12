package javachallenge.client.teamcli;

import javachallenge.client.Client;
import javachallenge.message.*;
import javachallenge.units.Unit;
import javachallenge.units.UnitCE;
import javachallenge.util.*;

import java.util.ArrayList;


/**
 * Created by mohammad on 2/5/14.
 */
public class TeamClient extends Client {
    private int teamID;
    private int resources;
    private Cell spawn;
    private Cell destination;

    public TeamClient(int teamID, int resources, Cell spawn, Cell destination) {
        this.teamID = teamID;
        this.resources = resources;
        this.spawn = spawn;
        this.destination = destination;
    }

    @Override
    public void step() {
        for (int i = 0; i < myUnits.size(); i++) {
            move(myUnits.get(i), Direction.EAST);
        }
        makeWall(spawn, Direction.EAST);
    }
}
