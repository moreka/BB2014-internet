package javachallenge.server;

import javachallenge.units.UnitAttacker;
import javachallenge.units.UnitBomber;
import javachallenge.units.UnitWallie;
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
    private ArrayList<UnitAttacker> unitAttackers;
    private ArrayList<UnitBomber> unitBombers;
    private ArrayList<UnitWallie> unitWallies;

    public Team(int teamId, int resources) {
        this.teamId = teamId;
        this.resources = resources;
    }
}
