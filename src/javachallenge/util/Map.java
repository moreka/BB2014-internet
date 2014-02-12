package javachallenge.util;

import javachallenge.units.Unit;
import javachallenge.units.UnitCE;
import javachallenge.units.UnitCell;
import javachallenge.message.Delta;
import sun.print.resources.serviceui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohammad on 2/5/14.
 */
public class Map implements Serializable, Cloneable {
    private Cell[][] cells;
    private Node[][] nodes;
    private ArrayList<MineCell> mines;
    private int sizeX;
    private int sizeY;
    private Edge[] walls;
    private int mineRate;

    public Point getSpawnPoint(int teamId) {
        //TODO: implement this asshole
        if (teamId == 0)
            return new Point(1, 1);
        else
            return new Point(1, 6);
    }

    public Point getDestinationPoint(int teamId) {
        //TODO: implement this asshole
        if (teamId == 0)
            return new Point(6, 1);
        else
            return new Point(6, 6);
    }

    public Map(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cells = new Cell[sizeX][sizeY];
    }

    private void init() {
        nodes = new Node[2 * sizeX + 2][];
        this.walls = new Edge[ (3 * (2 * sizeX + 2) * (sizeY + 1)) / 2];

        for (int k = 0; k < 2 * sizeX + 2; k++) {
            nodes[k] = new Node[sizeY + 1];
            for (int l = 0; l < sizeY + 1; l++) {
                nodes[k][l] = new Node(k, l);
            }
        }

        for(int i = 0; i < this.sizeX; i++){
            for(int j = 0; j < this.sizeY; j++){
                if(this.cells[i][j].getType().equals(CellType.OUTOFMAP) || this.cells[i][j].getType().equals(CellType.MOUNTAIN) || this.cells[i][j].getType().equals(CellType.RIVER))
                    continue;
                for (Direction d : Direction.values()){
                    Node[] temp = getNodesFromCellAt(cells[i][j], d);
                    NodeDirection[] dirTemp = getNodDirFromCellDir(d);
                    temp[0].setEdge(temp[1].getEdge(dirTemp[1]),dirTemp[0]); //Same Edge between the Nodes
                    temp[0].getEdge(dirTemp[0]).setType(EdgeType.OPEN);//set the EdgeType = OPEN
                    temp[0].getEdge(dirTemp[0]).setNodes(temp);//set the Nodes in Edge Class
                    Cell[] input = new Cell[2];
                    input[0] = cells[i][j];
                    input[1] = getNeighborCell(cells[i][j], d);
                    temp[0].getEdge(dirTemp[0]).setCells(input);//set the Cells in Edge Class
                    this.cells[i][j].setEdge(temp[0].getEdge(dirTemp[0]), d);
                    if (getNeighborCell(this.cells[i][j],d) != null)
                        getNeighborCell(this.cells[i][j],d).setEdge(temp[0].getEdge(dirTemp[0]),Direction.values()[(d.ordinal() + 3) % 6]);
                }
            }
        }
        int wall_Pointer = 0;
        for(int i = 0; i < 2 * sizeX + 2; i++){
            for(int j = 0; j < sizeY + 1; j++)
                if((i + j) % 2 == 1 && isNodeInMap(i,j)){
                    if(!this.nodes[i][j].getEdge(NodeDirection.NORTH).getType().equals(EdgeType.NONE)){
                        walls[wall_Pointer] = this.nodes[i][j].getEdge(NodeDirection.NORTH);
                        wall_Pointer++;
                    }
                    if(!this.nodes[i][j].getEdge(NodeDirection.SOUTHEAST).getType().equals(EdgeType.NONE)){
                        walls[wall_Pointer] = this.nodes[i][j].getEdge(NodeDirection.SOUTHEAST);
                        wall_Pointer++;
                    }
                    if(!this.nodes[i][j].getEdge(NodeDirection.SOUTHWEST).getType().equals(EdgeType.NONE)){
                        walls[wall_Pointer] = this.nodes[i][j].getEdge(NodeDirection.SOUTHWEST);
                        wall_Pointer++;
                    }
                }
        }
    }

    public static Map loadMap(String mapAddr) throws IOException, IndexOutOfBoundsException {
        BufferedReader br = new BufferedReader(new FileReader(mapAddr));

        Map map = new Map(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));

        for (int i = 0; i < map.getSizeY(); i++) {
            String[] cell_types = br.readLine().split("[ ]");
            for (int j = 0; j < map.getSizeX(); j++) {
                map.cells[j][i] = new Cell(j, i, CellType.values()[Integer.parseInt(cell_types[j])]);
            }
        }

        map.init();
        return map;
    }

    public void addMineCell(int x, int y){
        mines.add((MineCell) this.cells[x][y]);
    }

    public void removeMineCell(MineCell e){
        mines.remove(e);
    }

    public ArrayList<MineCell> getMines(){
        return  mines;
    }

    public boolean isCellInMap(int x, int y){
        if( x >= 0 && x < sizeX && y >= 0 && y < sizeY)
            return true;
        return false;
    }

    public boolean isNodeInMap(int x, int y){
        if(x >= 0 && x <= (2 * this.sizeX + 1) && y >= 0 && y <= sizeY){
            if(y == 0 && x == (2 * this.sizeX + 1))
                return  false;
            if(y % 2 == 0 && y == this.sizeY && x == 0)
                return  false;
            if(y % 2 == 1 && y == this.sizeY && x == (2 * this.sizeX + 1))
                return  false;
            return  true;
        }
        return false;
    }

    public Cell getCellAt(int x, int y){
        if(isCellInMap(x, y))
            return  cells[x][y];
        return  null;
    }

    public  Cell getCellAtPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        if(isCellInMap(x, y))
            return  cells[x][y];
        return  null;
    }

    public  Node getNodeAt(int x, int y){
        if(isNodeInMap(x, y))
            return  nodes[x][y];
        return  null;
    }

    public Node getNodeAtPoint(Point point){
        int x = point.getX();
        int y = point.getY();
        if(isNodeInMap(x, y))
            return this.nodes[x][y];
        return  null;
    }

    public Cell getNeighborCell(Cell c, Direction dir) {
        int x = 0, y = 0;
        switch (dir) {
            case EAST:
                x = c.getX() + 1;
                y = c.getY();
                break;
            case WEST:
                x = c.getX() - 1;
                y = c.getY();
                break;
            default:
                if (c.getY() % 2 == 1) {
                    switch (dir) {
                        case NORTHEAST:
                            x = c.getX() + 1;
                            y = c.getY() - 1;
                            break;
                        case SOUTHEAST:
                            x = c.getX() + 1;
                            y = c.getY() + 1;
                            break;
                        case NORTHWEST:
                            x = c.getX();
                            y = c.getY() - 1;
                            break;
                        case SOUTHWEST:
                            x = c.getX();
                            y = c.getY() + 1;
                            break;
                    }
                } else {
                    switch (dir) {
                        case NORTHWEST:
                            x = c.getX() - 1;
                            y = c.getY() - 1;
                            break;
                        case SOUTHEAST:
                            x = c.getX();
                            y = c.getY() + 1;
                            break;
                        case NORTHEAST:
                            x = c.getX();
                            y = c.getY() - 1;
                            break;
                        case SOUTHWEST:
                            x = c.getX() - 1;
                            y = c.getY() + 1;
                            break;
                    }
                }
        }

        if(isCellInMap(x, y))
            return cells[x][y];

        return null;
    }

    private ArrayList<UnitCE>[][] initUpdate() {
        ArrayList<UnitCE>[][] units = new ArrayList[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (cells[i][j].getUnit() != null) {
                    if (units[i][j] == null)
                        units[i][j] = new ArrayList<UnitCE>(2);
                    units[i][j].add((UnitCE) cells[i][j].getUnit());
                }
            }
        }
        return units;
    }

    public void updateMap(ArrayList<Delta> deltaList) {
        ArrayList<UnitCE>[][] oldUnits = initUpdate();

        for (int i = 0; i < deltaList.size(); i++) {
            Delta temp = deltaList.get(i);
            Node nodeSr = null;
            Node nodeDes = null;
            Cell cellSr = null;
            Cell cellDes = null;
            switch (temp.getType()){
                case WALL_DRAW:
                    nodeSr = this.nodes[temp.getSource().getX()][temp.getSource().getY()];
                    nodeDes = this.nodes[temp.getDestination().getX()][temp.getDestination().getY()];
                    nodeSr.getEdge(getDirectionFromTwoNodes(nodeSr, nodeDes)).setType(EdgeType.WALL);//Edge.EdgeType = WALL
                    break;
                case CELL_MOVE:
                    cellSr = this.cells[temp.getSource().getX()][temp.getSource().getY()];
                    cellDes = this.cells[temp.getDestination().getX()][temp.getDestination().getY()];
                    UnitCE unit = oldUnits[cellSr.getX()][cellSr.getY()].get(0);

                    if (oldUnits[cellSr.getX()][cellSr.getY()].size() > 0)
                        oldUnits[cellSr.getX()][cellSr.getY()].remove(0);
                    if (oldUnits[cellDes.getX()][cellDes.getY()] == null)
                        oldUnits[cellDes.getX()][cellDes.getY()] = new ArrayList<UnitCE>(2);
                    oldUnits[cellDes.getX()][cellDes.getY()].add(unit);
                    break;
                case MINE_DISAPPEAR:
                    cellSr = this.cells[temp.getSource().getX()][temp.getSource().getY()];
                    MineCell mineCell = (MineCell) cellSr;
                    mineCell.setAmount(0);
                    cellSr.setType(CellType.TERRAIN);
                    mines.remove(mineCell);
                    ////
                    break;
                case MINE_CHANGE:
                    cellSr = this.cells[temp.getSource().getX()][temp.getSource().getY()];
                    MineCell mineCell2 = (MineCell) cellSr;
                    mineCell2.setAmount(mineCell2.getAmount() - temp.getMineChange());
                    break;
                case AGENT_DISAPPEAR:
                    cellSr = this.cells[temp.getSource().getX()][temp.getSource().getY()];
                    cellSr.getUnit().setAlive(false);
                    ((UnitCE)cellSr.getUnit()).setArrived(true);
                    UnitCell unitCell2 = (UnitCell) cellSr.getUnit();
                    unitCell2.setCell(null);
                    cellSr.setUnit(null);
                    break;
                case SPAWN:
                    cellSr = this.cells[temp.getSource().getX()][temp.getSource().getY()];
                    UnitCE newUnit = new UnitCE();
                    newUnit.setCell(cellSr);
                    cellSr.setUnit(newUnit);
                    newUnit.setId(temp.getUnitID());
                    newUnit.setTeamId(temp.getTeamID());
                    System.out.println("Spawning a new unit with ID " + newUnit.getId());
                    break;
            }
        }

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (oldUnits[i][j] != null && oldUnits[i][j].size() > 0) {
                    UnitCE unit = oldUnits[i][j].get(0);

                    // if unit disappears
                    if (unit.getCell() == null)
                        continue;

                    if (oldUnits[unit.getCell().getX()][unit.getCell().getY()] == null ||
                            oldUnits[unit.getCell().getX()][unit.getCell().getY()].size() == 0) {
                        unit.getCell().setUnit(null);
                    }
                    unit.setCell(cells[i][j]);
                    cells[i][j].setUnit(unit);
                }
            }
        }
    }

    public Node getNeighborNode(Node n, NodeDirection dir){
        int x = 0;
        int y = 0;

        if((n.getX() + n.getY()) % 2 == 0){
            switch (dir){
                case NORTHWEST:
                    x = n.getX() - 1;
                    y = n.getY();
                    break;
                case NORTHEAST:
                    x = n.getX() + 1;
                    y = n.getY();
                    break;
                case SOUTH:
                    x = n.getX();
                    y = n.getY() + 1;
                    break;
            }
        }
        else{
            switch (dir){
                case NORTH:
                    x = n.getX();
                    y = n.getY() - 1;
                    break;
                case SOUTHEAST:
                    x = n.getX() + 1;
                    y = n.getY();
                    break;
                case SOUTHWEST:
                    x = n.getX() - 1;
                    y = n.getY();
                    break;
            }
        }

        if(x >= 0 && x <= (2 * this.sizeX + 1) && y >= 0 && y <= sizeY){
            if(y == 0 && x == (2 * this.sizeX + 1))
                return  null;
            if(y % 2 == 0 && y == this.sizeY && x == 0)
                return  null;
            if(y % 2 == 1 && y == this.sizeY && x == (2 * this.sizeX + 1))
                return  null;
            return  nodes[x][y];
        }
        return null;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Node[] getNodesFromCellAt(Cell c, Direction dir){
        Node[] res = new Node[2];
        switch (dir){
            case NORTHWEST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2)][c.getY()];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2) + 1][c.getY()];
                break;
            case NORTHEAST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2) + 1][c.getY()];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2) + 2][c.getY()];
                break;
            case EAST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2) + 2][c.getY()];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2) + 2][c.getY() + 1];
                break;
            case SOUTHEAST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2) + 2][c.getY() + 1];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2) + 1][c.getY() + 1];
                break;
            case SOUTHWEST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2) + 1][c.getY() + 1];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2)][c.getY() + 1];
                break;
            case WEST:
                res[0] = nodes[c.getX() * 2 + (c.getY() % 2)][c.getY() + 1];
                res[1] = nodes[c.getX() * 2 + (c.getY() % 2)][c.getY()];
                break;
        }
        return res;
    }

    private NodeDirection[] getNodDirFromCellDir(Direction dir){
        NodeDirection[] res = new NodeDirection[2];

        switch (dir){
            case NORTHWEST:
                res[0] = NodeDirection.NORTHEAST;
                res[1] = NodeDirection.SOUTHWEST;
                break;
            case NORTHEAST:
                res[0] = NodeDirection.SOUTHEAST;
                res[1] = NodeDirection.NORTHWEST;
                break;
            case EAST:
                res[0] = NodeDirection.SOUTH;
                res[1] = NodeDirection.NORTH;
                break;
            case SOUTHEAST:
                res[1] = NodeDirection.NORTHEAST;
                res[0] = NodeDirection.SOUTHWEST;
                break;
            case SOUTHWEST:
                res[1] = NodeDirection.SOUTHEAST;
                res[0] = NodeDirection.NORTHWEST;
                break;
            case  WEST:
                res[1] = NodeDirection.SOUTH;
                res[0] = NodeDirection.NORTH;
                break;
        }
        return res;
    }

    public void print() {
        for (int i = 0; i < this.sizeY; i++) {
            for (int j = 0; j < this.sizeX; j++) {
                System.out.print(cells[j][i].getType() + "\t\t");
            }
            System.out.println();
        }
    }

    public void printUnits() {
        for (int i = 0; i < this.sizeY; i++) {
            for (int j = 0; j < this.sizeX; j++) {
                System.out.print(((cells[j][i].getUnit() == null) ? "N" : cells[j][i].getUnit().getId()) + "\t");
            }
            System.out.println();
        }
    }

    public Edge[] getWalls() {
        return walls;
    }

    public NodeDirection getDirectionFromTwoNodes(Node sr, Node des){
        NodeDirection res = null;
        for(NodeDirection dir : NodeDirection.values()){
            if(getNeighborNode(sr,dir).equals(des)){
                res = dir;
                break;
            }
        }
        return  res;
    }

    public NodeDirection getDirectionFromNodePoint(Node sr, Point des){
        NodeDirection res = null;
        for(NodeDirection dir : NodeDirection.values()){
            if(getNeighborNode(sr,dir).equals(this.nodes[des.getX()][des.getY()])){
                res = dir;
                break;
            }
        }
        return res;
    }

    public Direction getDirectionFromCellPoint(Cell sr, Point des){
        Direction res = null;
        for(Direction dir : Direction.values()){
            if(getNeighborCell(sr, dir).equals(this.cells[des.getX()][des.getY()])){
                res = dir;
                break;
            }
        }
        return  res;
    }

    public static void main(String[] args) {
        // map tester!
        try {
            Map m = Map.loadMap("test.map");
            m.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMineRate() {
        return mineRate;
    }

    public void setMineRate(int mineRate) {
        this.mineRate = mineRate;
    }


}
