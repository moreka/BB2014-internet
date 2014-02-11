package javachallenge.server;

import javachallenge.units.Unit;
import javachallenge.units.UnitCell;
import javachallenge.message.Action;
import javachallenge.message.ActionType;
import javachallenge.message.Delta;
import javachallenge.message.DeltaType;
import javachallenge.util.*;
import javachallenge.util.Map;

import java.util.*;

/**
 * Created by mohammad on 2/5/14.
 */

public class Game {
    private boolean ended;
    private Map map;
    private ArrayList<Edge> wallsUnderConstruction = new ArrayList<Edge>();
    private ArrayList<Edge> wallsUnderDestruction = new ArrayList<Edge>();
    private ArrayList<Unit>[][] tempOtherMoves;
    private ArrayList<Unit>[][] tempWallieMoves;
    private ArrayList<Delta> attackDeltas = new ArrayList<Delta>();
    private ArrayList<Delta> wallDeltas = new ArrayList<Delta>();
    private ArrayList<Delta> moveDeltas = new ArrayList<Delta>();
    private ArrayList<Delta> otherDeltas = new ArrayList<Delta>();
    private static final int MINE_RATE = 4;
    private static final int COST_WALL = 15;
    private static final int GAME_LENGTH = 700;
    private static final int ATTACKER_SPAWN_RATE = 2;
    private static final int BOMBER_SPAWN_RATE = 3;
    private int[] resources = new int[2];
    //private ArrayList<UnitWallie> busyWallies = new ArrayList<UnitWallie>();
    private int turn;
    private Point[] attackerSpawnLocation = new Point[2];
    private Point[] bomberSpawnLocation = new Point[2];
    private Point[] destinations = new Point[2];

    public boolean isEnded() {
        return ended;
    }

    public Game (Map map) {
        this.map = map;
        tempOtherMoves = new ArrayList[map.getSizeX() + 1][map.getSizeY() + 1];
        tempWallieMoves = new ArrayList[(map.getSizeX() + 1) * 2][map.getSizeY() + 1];
        for (int i = 0; i < map.getSizeX() + 1; i++)
            for (int j = 0; j < map.getSizeY() + 1; j++)
                tempOtherMoves[i][j] = new ArrayList<Unit>();
    }

    public Map getMap() {
        return map;
    }

    public void handleActions(ArrayList<Action> actions) {
        ArrayList<Action> attacks = new ArrayList<Action>();
        ArrayList<Action> constructionDestructionWalls = new ArrayList<Action>();
        ArrayList<Action> moves = new ArrayList<Action>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).getType() == ActionType.ATTACK) {
                attacks.add(actions.get(i));
            } else if (actions.get(i).getType() == ActionType.MAKE_WALL || actions.get(i).getType() == ActionType.DESTROY_WALL) {
                constructionDestructionWalls.add(actions.get(i));
            } else if (actions.get(i).getType() == ActionType.MOVE) {
                moves.add(actions.get(i));
            }
        }
        //handleAttacks(attacks);
        map.updateMap(attackDeltas);

        //handleConstructionDestructionWalls(constructionDestructionWalls);
        map.updateMap(wallDeltas);
        handleMoves(moves);
        map.updateMap(moveDeltas);
        handleOthers();
        map.updateMap(otherDeltas);
    }
/*
    private void handleAttacks(ArrayList<Action> attacks) {
        for (int i = 0; i < attacks.size(); i++) {
            UnitAttacker unit = ((UnitAttacker)map.getCellAtPoint(attacks.get(i).getPosition()).getUnit());
            Cell cell = unit.getCell();
            Cell neighborCell = map.getNeighborCell(cell, attacks.get(i).getDirection());
            Point tempPoint = new Point(cell.getX(), cell.getY());
            Point neighborPoint = new Point(neighborCell.getX(), neighborCell.getY());
            if (neighborCell != null && neighborCell.getUnit() != null) {
                attackDeltas.add(new Delta(DeltaType.AGENT_ATTACK, neighborPoint));
                tempPoint = new Point(neighborCell.getX(), neighborCell.getY());
                attackDeltas.add(new Delta(DeltaType.AGENT_KILL, tempPoint));
            } else if (neighborCell != null) {
                attackDeltas.add(new Delta(DeltaType.AGENT_ATTACK, tempPoint, neighborPoint));
            }
        }
    }
*/
/*
    private void handleConstructionDestructionWalls(ArrayList<Action> constructionDestructionWalls) {
        for (int i = wallsUnderConstruction.size() - 1; i >= 0; i--) {
            Node tempNode1 = wallsUnderConstruction.get(i).getNodes()[0];
            Point tempPoint1 = new Point(tempNode1.getX(), tempNode1.getY());
            Node tempNode2 = wallsUnderConstruction.get(i).getNodes()[1];
            Point tempPoint2 = new Point(tempNode2.getX(), tempNode2.getY());
            wallDeltas.add(new Delta(DeltaType.WALL_DRAW, tempPoint1, tempPoint2));
        }
        for (int i = wallsUnderDestruction.size() - 1; i >= 0; i--) {
            Node tempNode1 = wallsUnderDestruction.get(i).getNodes()[0];
            Point tempPoint1 = new Point(tempNode1.getX(), tempNode1.getY());
            Node tempNode2 = wallsUnderDestruction.get(i).getNodes()[1];
            Point tempPoint2 = new Point(tempNode2.getX(), tempNode2.getY());
            wallDeltas.add(new Delta(DeltaType.WALL_DISAPPEAR, tempPoint1, tempPoint2));
        }
        busyWallies = new ArrayList<UnitWallie>();
        wallsUnderConstruction = new ArrayList<Edge>();
        wallsUnderDestruction = new ArrayList<Edge>();
        Collections.shuffle(constructionDestructionWalls);
        for (int i = 0; i < constructionDestructionWalls.size(); i++) {
            Action walli = constructionDestructionWalls.get(i);
            UnitWallie wallie = ((UnitWallie) map.getNodeAtPoint(walli.getPosition()).getUnitWallie());
            if (resources[wallie.getTeamId()] >= COST_WALL) {
                busyWallies.add(wallie);
                Edge edge = wallie.getNode().getEdge(walli.getNodeDirection()[0]);
                Point tempPoint1 = new Point(edge.getNodes()[0].getX(), edge.getNodes()[0].getY());
                Point tempPoint2 = new Point(edge.getNodes()[1].getX(), edge.getNodes()[1].getY());
                resources[wallie.getTeamId()] -= COST_WALL;
                wallDeltas.add(new Delta(DeltaType.WALL_SEMI_DRAW, tempPoint1, tempPoint2));
                if (walli.getType() == ActionType.MAKE_WALL &&
                        edge.getType() == EdgeType.OPEN &&
                        isTherePathAfterThisEdge(bomberSpawnLocation[0], destinations[0], wallie.getNode().getEdge(walli.getNodeDirection()[0])) &&
                        isTherePathAfterThisEdge(bomberSpawnLocation[1], destinations[1], wallie.getNode().getEdge(walli.getNodeDirection()[0]))) {
                    wallsUnderConstruction.add(edge);
                }
                if (walli.getType() == ActionType.DESTROY_WALL && edge.getType() == EdgeType.WALL) {
                    wallsUnderDestruction.add(edge);
                }
            }
        }
    }
*/
    private void handleMoves(ArrayList<Action> moves) {
        ArrayList<Action> walliesMoves = new ArrayList<Action>();
        ArrayList<Action> otherMoves = new ArrayList<Action>();
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).getType() == ActionType.WALLIE_MOVE) {
                walliesMoves.add(moves.get(i));
            } else {
                otherMoves.add(moves.get(i));
            }
        }
        //handleWalliesMoves(walliesMoves);
        handleOthersMoves(otherMoves);
    }

    private void handleOthersMoves(ArrayList<Action> otherMoves) {
        ArrayDeque<Integer> xOfOverloadedCells = new ArrayDeque<Integer>();
        ArrayDeque<Integer> yOfOverloadedCells = new ArrayDeque<Integer>();
        for (int i = 0; i < otherMoves.size(); i++) {
            UnitCell unit = (UnitCell)map.getCellAtPoint(otherMoves.get(i).getPosition()).getUnit();
            Cell source = unit.getCell();
            Cell destination = map.getNeighborCell(source ,otherMoves.get(i).getDirection());
            if (destination.getType() != CellType.MOUNTAIN && destination.getType() != CellType.RIVER &&
                    destination.getType() != CellType.OUTOFMAP &&
                    source.getEdge(otherMoves.get(i).getDirection()).getType() == EdgeType.OPEN) {
                tempOtherMoves[source.getX()][source.getY()].remove(0);
                tempOtherMoves[destination.getX()][destination.getY()].add(map.getCellAtPoint(otherMoves.get(i).getPosition()).getUnit());
            }
        }
        for (int i = 0; i < tempOtherMoves.length; i++)
            for (int j = 0; j < tempOtherMoves[0].length; j++)
                if (tempOtherMoves[i][j].size() > 1) {
                    xOfOverloadedCells.add(i);
                    yOfOverloadedCells.add(j);
                }
        Random rand = new Random();
        while (!xOfOverloadedCells.isEmpty()) {
            int xTemp = xOfOverloadedCells.pop();
            int yTemp = yOfOverloadedCells.pop();
            int overloadedNumber = tempOtherMoves[xTemp][yTemp].size();
            if (overloadedNumber < 2)
                continue;
            boolean fullFlag = false;
            for (int i = 0; i < overloadedNumber; i++) {
                UnitCell existant = (UnitCell)tempOtherMoves[xTemp][yTemp].get(i);
                if (existant.getCell().getX() == xTemp && existant.getCell().getY() == yTemp)
                    fullFlag = true;
            }
            if (fullFlag == false) {
                int lasting = rand.nextInt(overloadedNumber);
                for (int i = 0; i < overloadedNumber; i++)
                    if (i != lasting) {
                        UnitCell goner = (UnitCell)tempOtherMoves[xTemp][yTemp].get(i);
                        tempOtherMoves[goner.getCell().getX()][goner.getCell().getY()].add(goner);
                        if (tempOtherMoves[goner.getCell().getX()][goner.getCell().getY()].size() > 1) {
                            xOfOverloadedCells.add(goner.getCell().getX());
                            yOfOverloadedCells.add(goner.getCell().getY());
                        }
                        tempOtherMoves[xTemp][yTemp].remove(i);
                    }
            } else {
                for (int i = 0; i < overloadedNumber; i++) {
                    UnitCell goner = (UnitCell)tempOtherMoves[xTemp][yTemp].get(i);
                    tempOtherMoves[goner.getCell().getX()][goner.getCell().getY()].add(goner);
                    if (tempOtherMoves[goner.getCell().getX()][goner.getCell().getY()].size() > 1) {
                        xOfOverloadedCells.add(goner.getCell().getX());
                        yOfOverloadedCells.add(goner.getCell().getY());
                    }
                    tempOtherMoves[xTemp][yTemp].remove(i);
                }
            }
        }
        for (int i = 0; i < tempOtherMoves.length; i++)
            for (int j = 0; j < tempOtherMoves[0].length; j++) {
                UnitCell thisUnit = (UnitCell)tempOtherMoves[i][j].get(0);
                Cell tempCell = thisUnit.getCell();
                Point sourcePoint = new Point (tempCell.getX(), tempCell.getY());
                if (thisUnit.getCell().getX() != i || thisUnit.getCell().getY() != j) {
                    Point destinationPoint = new Point(i, j);
                    moveDeltas.add(new Delta(DeltaType.CELL_MOVE, sourcePoint, destinationPoint));
                } else if (thisUnit.getCell().getX() == i && thisUnit.getCell().getY() == j && thisUnit.getCell().getType() == CellType.MINE) {
                    MineCell mineCell = (MineCell) thisUnit.getCell();
                    if (mineCell.getAmount() > MINE_RATE) {
                        resources[thisUnit.getTeamId()] += MINE_RATE;
                        otherDeltas.add(new Delta(DeltaType.MINE_CHANGE, sourcePoint, MINE_RATE));
                    } else if (mineCell.getAmount() > 0) {
                        resources[thisUnit.getTeamId()] += mineCell.getAmount();
                        otherDeltas.add(new Delta(DeltaType.MINE_CHANGE, sourcePoint, mineCell.getAmount()));
                    }
                }
            }
    }
/*
    private void handleWalliesMoves(ArrayList<Action> walliesMoves) {
        ArrayDeque<Integer> xOfOverloadedNodes = new ArrayDeque<Integer>();
        ArrayDeque<Integer> yOfOverloadedNodes = new ArrayDeque<Integer>();
        for (int i = 0; i < walliesMoves.size(); i++) {
            UnitWallie unit = (UnitWallie)map.getCellAtPoint(walliesMoves.get(i).getPosition()).getUnit();
            if (!busyWallies.contains(unit)) {
                Node source = unit.getNode();
                if (source.getEdge(walliesMoves.get(i).getNodeDirection()[0]).getType() != EdgeType.NONE) {
                    Node destination1 = map.getNeighborNode(source ,walliesMoves.get(i).getNodeDirection()[0]);
                    tempWallieMoves[source.getX()][source.getY()].remove(0);
                    if (destination1.getEdge(walliesMoves.get(i).getNodeDirection()[1]).getType() != EdgeType.NONE) {
                        Node destination2 = map.getNeighborNode(destination1 ,walliesMoves.get(i).getNodeDirection()[1]);
                        tempWallieMoves[destination2.getX()][destination2.getY()].add(unit);
                    } else
                        tempWallieMoves[destination1.getX()][destination1.getY()].add(unit);
                }
            }
        }
        for (int i = 0; i < tempWallieMoves.length; i++)
            for (int j = 0; j < tempWallieMoves[0].length; j++)
                if (tempWallieMoves[i][j].size() > 1) {
                    xOfOverloadedNodes.add(i);
                    yOfOverloadedNodes.add(j);
                }
        Random rand = new Random();
        while (!xOfOverloadedNodes.isEmpty()) {
            int xTemp = xOfOverloadedNodes.pop();
            int yTemp = yOfOverloadedNodes.pop();
            int overloadedNumber = tempWallieMoves[xTemp][yTemp].size();
            if (overloadedNumber < 2)
                continue;
            boolean fullFlag = false;
            int fuller = 0;
            for (int i = 0; i < overloadedNumber; i++) {
                UnitWallie existant = (UnitWallie)tempWallieMoves[xTemp][yTemp].get(i);
                if (existant.getNode().getX() == xTemp && existant.getNode().getY() == yTemp) {
                    fullFlag = true;
                    fuller = i;
                }
            }
            if (fullFlag == false) {
                int lasting = rand.nextInt(overloadedNumber);
                for (int i = 0; i < overloadedNumber; i++)
                    if (i != lasting) {
                        UnitWallie goner = (UnitWallie)tempWallieMoves[xTemp][yTemp].get(i);
                        tempWallieMoves[goner.getNode().getX()][goner.getNode().getY()].add(goner);
                        if (tempWallieMoves[goner.getNode().getX()][goner.getNode().getY()].size() > 1) {
                            xOfOverloadedNodes.add(goner.getNode().getX());
                            yOfOverloadedNodes.add(goner.getNode().getY());
                        }
                        tempWallieMoves[xTemp][yTemp].remove(i);
                    }
            } else {
                for (int i = 0; i < overloadedNumber; i++)
                    if (i != fuller) {
                        UnitWallie goner = (UnitWallie)tempWallieMoves[xTemp][yTemp].get(i);
                        tempWallieMoves[goner.getNode().getX()][goner.getNode().getY()].add(goner);
                        if (tempWallieMoves[goner.getNode().getX()][goner.getNode().getY()].size() > 1) {
                            xOfOverloadedNodes.add(goner.getNode().getX());
                            yOfOverloadedNodes.add(goner.getNode().getY());
                        }
                        tempWallieMoves[xTemp][yTemp].remove(i);
                    }
            }
        }
        for (int i = 0; i < tempWallieMoves.length; i++)
            for (int j = 0; j < tempWallieMoves[0].length; j++) {
                UnitWallie thisUnit = (UnitWallie)tempWallieMoves[i][j].get(0);
                if (thisUnit.getNode().getX() != i || thisUnit.getNode().getY() != j) {
                    Node tempNode = thisUnit.getNode();
                    Point sourcePoint = new Point(tempNode.getX(), tempNode.getY());
                    Point destinationPoint = new Point(i, j);
                    moveDeltas.add(new Delta(DeltaType.WALLIE_MOVE, sourcePoint, destinationPoint));
                }
            }
    }
*/
    private void handleOthers() {

    }

    private boolean isTherePathAfterThisEdge (Point sourceInput, Point destinationInput, Edge barrier) {
        Cell source = map.getCellAt(sourceInput.getX(), sourceInput.getY());
        Cell destination = map. getCellAt(destinationInput.getX(), destinationInput.getY());
        boolean[][] flags = new boolean[map.getSizeX()][map.getSizeY()];
        Cell currentCell;
        Stack<Cell> dfs = new Stack<Cell>();
        dfs.add(source);
        Direction[] dir = (Direction.EAST).getDirections();
        while (!dfs.isEmpty()) {
            currentCell = dfs.pop();
            if (currentCell.equals(destination))
                return true;
            flags[currentCell.getX()][currentCell.getY()] = true;
            for (int i = 0; i < 6; i++) {
                Cell neighborCell = map.getNeighborCell(currentCell, dir[i]);
                Edge neighborEdge = currentCell.getEdge(dir[i]);
                if (neighborCell != null && flags[neighborCell.getX()][neighborCell.getY()] == false &&
                        neighborEdge.getType() == EdgeType.OPEN && !neighborEdge.equals(barrier)) {
                    dfs.add(neighborCell);
                }
            }
        }
        return false;
    }

    public void initTurn (int turn) {
        attackDeltas = new ArrayList<Delta>();
        wallDeltas = new ArrayList<Delta>();
        moveDeltas = new ArrayList<Delta>();
        otherDeltas = new ArrayList<Delta>();
        this.turn = turn;
        if (turn == GAME_LENGTH) {
            ended = true;
        }
    }

    public void endTurn() {
        if (turn % ATTACKER_SPAWN_RATE == 0) {
            otherDeltas.add(new Delta(DeltaType.SPAWN_ATTACKER, attackerSpawnLocation[0]));
            otherDeltas.add(new Delta(DeltaType.SPAWN_ATTACKER, attackerSpawnLocation[1]));
        }
        if (turn % BOMBER_SPAWN_RATE == 0) {
            otherDeltas.add(new Delta(DeltaType.SPAWN_BOMBER, bomberSpawnLocation[0]));
            otherDeltas.add(new Delta(DeltaType.SPAWN_BOMBER, bomberSpawnLocation[1]));
        }
    }

    public ArrayList<Delta> getAttackDeltaList() {
        return attackDeltas;
    }

    public ArrayList<Delta> getWallDeltasList() {
        return wallDeltas;
    }

    public ArrayList<Delta> getMoveDeltasList() {
        return moveDeltas;
    }

    public ArrayList<Delta> getOtherDeltasList() {
        return otherDeltas;
    }
}
