package javachallenge.client.teamcli;

import javachallenge.client.Client;
import javachallenge.units.Unit;
import javachallenge.util.*;

import java.util.Random;


/**
 * Created by mohammad on 2/5/14.
 */
public class TeamClient extends Client {
    int turn = 0;
    @Override
    public void step() {
        // your code here ...
        // this is an example implementation:
        Random rnd = new Random();
        /**
         * Move section
         */
        for (Unit myUnit : myUnits) {
            //move(myUnit, Direction.EAST);
            /*if (myUnit.getCell().getY() == map.getSizeY() - 2)
                move(myUnit, Direction.WEST);
            else
                move(myUnit, Direction.SOUTHWEST);*/ /*Direction.values()[rnd.nextInt(6)]);*/
            //move(myUnits.get(0), Direction.SOUTHEAST);
            if (turn < 6 && turn % 2 == 0)
                move(myUnits.get(0), Direction.SOUTHEAST);
            else if (turn < 6 && turn % 2 == 1)
                move(myUnits.get(0), Direction.SOUTHWEST);
            else if (turn < 17)
                move(myUnits.get(0), Direction.WEST);
            else if (turn % 2 == 1 && turn < 20)
                move(myUnits.get(0), Direction.NORTHEAST);
            else if (turn % 2 == 0 && turn < 20)
                move(myUnits.get(0), Direction.NORTHWEST);
        }
        turn++;
        System.out.println("<------------------------- Resources:" + getResources() + " ------------------------>");
        /**
         * Making walls section
         */

        makeWall(map.getCellAt(6, 6), Direction.EAST);
        makeWall(map.getCellAt(6, 6), Direction.SOUTHEAST);
        makeWall(map.getCellAt(6, 6), Direction.SOUTHWEST);
        makeWall(map.getCellAt(6, 6), Direction.NORTHEAST);
        makeWall(map.getCellAt(6, 6), Direction.NORTHWEST);
        //makeWall(map.getCellAt(rnd.nextInt(map.getSizeX()), rnd.nextInt(map.getSizeY())),
            //Direction.values()[rnd.nextInt(6)]);
        //for(Direction d : Direction.values())
          //  makeWall(map.getCellAt(3,18),d);

    }
}
