package javachallenge.util;

/**
 * Created by merhdad on 2/15/14.
 */
public class MapHelper {
    private int sizeX;
    private int sizeY;
    private CellType[][] cells;
    private Point spawn1;
    private Point destination1;
    private Point spawn2;
    private Point destination2;
    private Point[] mines;

    public MapHelper(int sizeX, int sizeY, CellType[][] cells, Point spawn1, Point destination1, Point spawn2, Point destination2, Point[] mines) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.cells = cells;
        this.spawn1 = spawn1;
        this.destination1 = destination1;
        this.spawn2 = spawn2;
        this.destination2 = destination2;
        this.mines = mines;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public CellType[][] getCells() {
        return cells;
    }

    public Point getSpawn1() {
        return spawn1;
    }

    public Point getDestination1() {
        return destination1;
    }

    public Point getSpawn2() {
        return spawn2;
    }

    public Point getDestination2() {
        return destination2;
    }

    public Point[] getMines() {
        return mines;
    }
}
