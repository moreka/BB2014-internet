package javachallenge.client.teamcli;

import javachallenge.client.Client;
import javachallenge.units.Unit;
import javachallenge.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

class MeNode {
    int parx;
    int pary;
    int k;

    public MeNode() {
        k = 0;
        parx = -1;
        pary = -1;
    }
}

public class TeamClient extends Client {
    int CYCLE = 0;
    int mine_cnt = 0;
    boolean IsCapture=false;
    boolean bfs_empty=false;
    Cell Capture;
    Cell Must_capture;

    // -------------------------------------------------------------------------
    // VALIDITY
    public boolean valid(Cell point, Cell DEST) {
        if (point.isGround() == false)
            return false;
        if (point.getType() != CellType.TERRAIN)
            if (point != DEST && point.getType() != CellType.MINE)
                return false;
        return true;
    }

    // -------------------------------------------------------------------------
    // MYEMPTY
    public boolean myempty(Cell point) {
        if(bfs_empty==true)
            return true;
        if (point == map.getSpawnCell(1))
            return true;
        if (point.getUnit() != null)
            return false;
        for (Unit myUnit : myUnits)
            if (myUnit.getCell() == point)
                return false;
        return true;
    }

    // -------------------------------------------------------------------------
    // BFS
    public Cell bfs(Cell des, Cell start) {
        Queue<Integer> q = new LinkedList<Integer>();
        MeNode[][] me = new MeNode[map.getSizeX() + 5][map.getSizeY() + 5];
        for (int i = 0; i < map.getSizeX() + 5; i++)
            for (int j = 0; j < map.getSizeY() + 5; j++)
                me[i][j] = new MeNode();

		/* Nearby points */
        for (int i = 0; i < 6; i++) {
            Cell temp = map.getNeighborCell(start, Direction.values()[i]);
            int tempX = temp.getX();
            int tempY = temp.getY();

            if (map.isCellInMap(tempX, tempY)) {
                if (me[tempX][tempY].k == 0
                        && map.getCellAt(tempX, tempY) != map
                        .getSpawnCell(this.teamID) && valid(temp, des)
                        && myempty(temp)) {
                    q.add((tempX << 12) + tempY);
                    me[tempX][tempY].k = 1;
                    me[tempX][tempY].parx = start.getX();
                    me[tempX][tempY].pary = start.getY();
                    // System.out.println("parent of start is " + tempX + " " +
                    // tempY);
                }
            }
        }
        me[start.getX()][start.getY()].k = 2;
		/* manage Queue */
        while (!q.isEmpty()) {
            int Temp = q.poll();

            boolean my_flag = false;
            int X = Temp >> 12;
            int Y = Temp % (1 << 12);
            for (int j = 0; j < 6; j++) {
                Cell temp = map.getNeighborCell(map.getCellAt(X, Y),
                        Direction.values()[j]);
                int tempX = temp.getX();
                int tempY = temp.getY();
                if (map.isCellInMap(tempX, tempY)) {
                    if (me[tempX][tempY].k == 0
                            && map.getCellAt(tempX, tempY) != map
                            .getSpawnCell(this.teamID)
                            && valid(temp, des)) {
                        q.add((tempX << 12) + tempY);
                        me[tempX][tempY].k = 1;
                        me[tempX][tempY].parx = X;
                        me[tempX][tempY].pary = Y;
                    }
                }

                if (map.getCellAt(tempX, tempY) == des) {
                    my_flag = true;
                    break;
                }
            }

            if (my_flag == true)
                break;
            me[X][Y].k = 2;
        }

		/* find a rout to starting point */
        int aminx = des.getX();
        int aminy = des.getY();
        Random rand = new Random();

        while (me[aminx][aminy].parx != start.getX()
                || me[aminx][aminy].pary != start.getY()) {
            if (me[aminx][aminy].k == 0) { // means there is no rout
                int randd = rand.nextInt(6);
                return map.getNeighborCell(start, Direction.values()[randd]);
            }
            int my_x = me[aminx][aminy].parx;
            int my_y = me[aminx][aminy].pary;
            aminx = my_x;
            aminy = my_y;

        }

        // System.out.println("end");
        return map.getCellAt(aminx, aminy);
    }

    // -------------------------------------------------------------------------
    // HARKAT()
    public int harekat(Cell start, Cell des) {
        if (start == des)
            return -1;

		/*
		 * bara 1 halat estesna tu map , in for bayad bashe :
		 */
        for (int j = 0; j < 6; j++)
            if (map.getNeighborCell(start, Direction.values()[j]) == des)
                if ((map.getNeighborCell(start, Direction.values()[j])
                        .getUnit() != null && map
                        .getNeighborCell(start, Direction.values()[j])
                        .getUnit().getTeamId() != this.teamID))
                    return j;

        Cell neigh = bfs(des, start);
        int dir = 3;
        for (int i = 0; i < 6; i++) {
            if (map.getNeighborCell(start, Direction.values()[i]) == neigh) {
                dir = i;
                break;
            }
        }
        return dir;
    }

    // -------------------------------------------------------------------------
    // GOSPAWN()
    public Cell go_spawn(Unit mohre, int ID) {
        Cell DES = map.getSpawnCell(ID);
        if (mohre.getCell() == DES)
            return DES;
        if (DES.getUnit() != null
                && DES.getUnit().getTeamId() == this.getTeamID())
            return map.getDestinationCell(this.getTeamID());
        return DES;
    }

    // -------------------------------------------------------------------------
    // GOMINE()
    public Cell go_mine(Unit mohre) {
        if (mohre.getCell().getType() == CellType.MINE){
            MineCell temp = null;
            for(MineCell mine : map.getMines())
                if(mine.getX() == mohre.getCell().getX() && mine.getY() == mohre.getCell().getY()){
                    temp = mine;
                    break;
                }
            if(temp.getAmount() != 0)
                return mohre.getCell();
            else
                return map.getDestinationCell(this.getTeamID());
        }

        if(mine_cnt == 0)
            for (MineCell mine : map.getMines())
                mine_cnt++;

        if(mine_cnt == 0)
            map.getDestinationCell(this.getTeamID());

        MineCell mine=null;
        int mine_id = mohre.getId() % mine_cnt;
        int temp_cnt = 0;
        for(MineCell madan:map.getMines()){
            if(mine_id == temp_cnt){
                mine = madan;
                break;
            }
            temp_cnt++;
        }
        if(mine.getAmount() != 0){
            boolean flag = true;// there is no unit on this mine
            for (Unit unit : myUnits)
                if (unit.getCell().getX() == mine.getX()
                        && unit.getCell().getY() == mine.getY()) {
                    flag = false;
                    break;
                }
            if (flag)
                return mine;
        }

        return map.getDestinationCell(this.getTeamID());
    }

    // -------------------------------------------------------------------------
    // GOPOS()
    public Cell go_pos(Unit mohre, Cell DES) {
        if (mohre.getCell() == DES)
            return DES;
        Cell TMP = DES;
        if (TMP.getUnit() == null || TMP.getUnit().getTeamId() != this.teamID)
            return DES;
        return map.getDestinationCell(this.teamID);
    }

    // check nemishavad ke edge open ast ya none

    // - - - - - - - - -- - - - - - - -- - - - - - - - - - - - - - - - - - -- -
    // divar
    public ArrayList<Cell> divar(int num) {
        ArrayList<Cell> ans = new ArrayList<Cell>();
        Cell temp = map.getSpawnCell(1);

        for(int i = 0 ; i < 6 ; i++){
            if((map.getNeighborCell(temp, Direction.values()[i]).isGround())){
                ans.add(map.getNeighborCell(temp, Direction.values()[i]));
            }
        }

        return ans;
    }


    public void Castle(int rad, Cell gate) {
        for (int i = 0; i < 6; i++) {
            Cell Wall = map.getNeighborCell(Must_capture,
                    Direction.values()[i]);

            if(Wall.getUnit()!=null && Wall.getUnit().getTeamId()==this.teamID)
                continue;
            else
            if (Wall.isGround())
                for (int j = 0; j < 6; j++) {
                    Cell temp= map.getNeighborCell(Must_capture,Direction.values()[j]);
                    if(temp == Wall)
                        if(Must_capture.getEdge(Direction.values()[j]).getType()==EdgeType.OPEN){
                            makeWall(Must_capture, Direction.values()[j]);
                            return;
                        }
                }
        }
        return;
    }


    public Cell checkgate(){
        if(IsCapture==true)
            return Capture;
        for(int i = 0 ; i < 6 ; i ++){
            if(valid(map.getNeighborCell(Must_capture, Direction.values()[i]) , map.getDestinationCell(this.teamID))){
                if(map.getNeighborCell(Must_capture, Direction.values()[i]).getUnit() != null && map.getNeighborCell(Must_capture, Direction.values()[i]).getUnit().getTeamId()==this.teamID){
                    IsCapture=true;
                    Capture=map.getNeighborCell(Must_capture, Direction.values()[i]);
                    return map.getNeighborCell(Must_capture, Direction.values()[i]);
                }
            }
        }
        return null;
    }

    public int distance(Cell my_cell, Cell des){
        int x=Math.abs(my_cell.getX()-des.getX());
        int y=Math.abs(my_cell.getY()-des.getY());
        return (x+y);
    }


    public void Change_capture(int count){
        int min=1000000;
        int index=-1;
        for(Unit myUnit : myUnits){
            if(myUnit.getId()<count)
                if(distance(myUnit.getCell(), Must_capture)<min){
                    min=distance(myUnit.getCell(), Must_capture);
                    index=myUnit.getId();
                }
        }

        bfs_empty=true;
        Cell des=bfs(Must_capture, myUnits.get(index).getCell());
        bfs_empty=false;
        IsCapture=true;
        Capture=myUnits.get(index).getCell();
        Must_capture=des;
    }
	/**/

    @Override
    public void step() {
        int count = 0;
        int max_delay=200;
        CYCLE++;
        Random rand = new Random();
        int myrand = rand.nextInt(2);

        ArrayList<Cell> gates = new ArrayList<Cell>();
        gates = divar(2);

        if(CYCLE>max_delay && IsCapture==false)
            Change_capture(gates.size());
        if(CYCLE<5)
            Must_capture=map.getSpawnCell(1);

        boolean flag=false;
        for (Unit myUnit : myUnits) {
            if (myUnit.isArrived() == false) {
                int a = 0;
                int PER = 60;
                int MID = myUnit.getId();

                if(checkgate() != null && flag==false){
                    Castle(1, checkgate());
                    flag=true;
                    if(myUnit.getCell()==checkgate())
                        continue;
                }
//				System.out.println("size:"+gates.size()+" X"+gates.get(MID).getX());
                if (MID < gates.size() && IsCapture==false){
                    //if(CYCLE>max_delay-5)
                    bfs_empty=true;
                    a=harekat(myUnit.getCell(), gates.get(MID));
                    bfs_empty=false;
                }
                else
                if(myUnit.getCell()!=checkgate())
                    a=harekat(myUnit.getCell(), go_mine(myUnit));
				/**/
                if(a>-1)
                    move(myUnit, Direction.values()[a]);
            }

            flag=true;
        }
    }

}