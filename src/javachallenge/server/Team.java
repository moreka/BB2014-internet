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
    private int arrivedUnitsNum = 0;

    public Team(int teamId, int resources) {
        this.teamId = teamId;
        this.resources = resources;
        unitCEs = new ArrayList<UnitCE>();
    }

    public int getTeamId() {
        return teamId;
    }

    public UnitCE addUnitCE(){
        UnitCE newUnit = new UnitCE();
        newUnit.setAlive(true);
        newUnit.setTeamId(this.teamId);
        this.unitCEs.add(newUnit);
        return newUnit;
    }

    public int getResources() {
        return resources;
    }

    public Cell getSpawn() {
        return spawn;
    }

    public void setSpawn(Cell spawn) {
        this.spawn = spawn;
    }

    public Cell getDestination() {
        return destination;
    }

    public void setDestination(Cell destination) {
        this.destination = destination;
    }

    public ArrayList<UnitCE> getUnitCEs() {
        return unitCEs;
    }

    public void setUnitCEs(ArrayList<UnitCE> unitCEs) {
        this.unitCEs = unitCEs;
    }

    public void decreaseResources (int input) {
        resources -= input;
    }

    public int getArrivedUnitsNum() {
        return arrivedUnitsNum;
    }

    public void increaseArrivedNumber() {
        arrivedUnitsNum++;
    }
}
