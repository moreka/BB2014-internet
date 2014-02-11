package javachallenge.server;

import javachallenge.units.UnitCE;
import javachallenge.util.Cell;

import java.util.ArrayList;

/**
 * Created by peyman on 2/11/14.
 */
public class Team {
    private int teamId;
    private int resources;
    private Cell spawn;
    private Cell destination;
    private ArrayList<UnitCE> unitCEs;

    public Team(int teamId, int resources) {
        this.teamId = teamId;
        this.resources = resources;
    }
}
