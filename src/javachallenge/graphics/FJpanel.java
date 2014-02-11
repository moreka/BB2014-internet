package javachallenge.graphics;


import javachallenge.message.Delta;
import javachallenge.message.DeltaType;
import javachallenge.server.Game;
import javachallenge.util.Cell;
import javachallenge.util.CellType;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;

public class FJpanel extends JPanel {
    BufferedImage slate;
    TexturePaint slatetp;
    private Image grass, mountain, water, mine, attacker, wallie, black, spawn, destination;
    private Image wall1, wall2, wall3, semiWall1, semiWall2, semiWall3;
    private Image[] cells;
    private Image[] walls;
    private Random random;
    private Hexagon[][] map;
    private Node[][] nodes;
    private int rows;
    private int cols;
    private int counter;

    
    private Game game;
    private BufferedImage buffer;

    public FJpanel(Game game, Hexagon[][] map, Node[][] nodes, int rows, int cols){
        this.map = map;
        this.nodes = nodes;
        this.rows = rows;
        this.cols = cols;
        this.game = game;
        random = new Random();
        loadImage();
    }

    private void loadImage(){
        grass = new ImageIcon("data/grass.png").getImage();
        mountain = new ImageIcon("data/mountain.png").getImage();
        water = new ImageIcon("data/water.png").getImage();
        mine = new ImageIcon("data/mine.png").getImage();
        attacker = new ImageIcon("data/attacker.png").getImage();
        wallie = new ImageIcon("data/wallie.png").getImage();
        black = new ImageIcon("data/black.png").getImage();
        spawn = new ImageIcon("data/spawn.png").getImage();
        destination = new ImageIcon("data/destination.png").getImage();
        //cells = new Image[]{grass, mountain, water};
        wall1 = new ImageIcon("data/wall_1.png").getImage();
        wall2 = new ImageIcon("data/wall_2.png").getImage();
        wall3 = new ImageIcon("data/wall_3.png").getImage();
        semiWall1 = new ImageIcon("data/semi_wall_1.png").getImage();
        semiWall2 = new ImageIcon("data/semi_wall_2.png").getImage();
        semiWall3 = new ImageIcon("data/semi_wall_3.png").getImage();
		/*walls = new Image[]{wall1, wall2, wall3, wall4, wall5, wall6};
		for (int i = 0; i < walls.length; i++) {
			walls[i] = new ImageIcon("C:\\Users\\AlirezaF\\Desktop\\jc_wall"+ String.valueOf(i) +".png").getImage();
		}*/
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        /**
         * this is temporary!!!
         */

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D buffer_g2d = (Graphics2D) buffer.getGraphics();

        /**
         * End of temp
         */

        // ino havaset bashe bayad ba tavajjoh be type game bache ha render koni
        drawMap(buffer_g2d);
        drawDelta(buffer_g2d, getDelta(counter));

        //hex.draw(g2d, 50, 50, 20, 0x008844, true);

        g2d.drawImage(buffer, 0, 0, null);

    }


    public void paintDelta(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        /**
         * this is temporary!!!
         */

        buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) buffer.getGraphics();


        drawDelta(g2d, getDelta(counter));

        ((Graphics2D) g).drawImage(buffer, 0, 0, null);

        counter++;

    }

    public ArrayList<Delta> getDelta(int round) {
        switch (round % 4) {
            case 0:
                return game.getAttackDeltaList();
            case 1:
                return game.getWallDeltasList();
            case 2:
                return game.getMoveDeltasList();
            case 3:
                return game.getOtherDeltasList();
            default:
                return null;
        }
    }

/*	public void update(Delta delta){

	}*/

    private Image getImage(CellType type){
        switch (type){
            case TERRAIN:
                return grass;
            case RIVER:
                return water;
            case SPAWN:
                return spawn;
            case MOUNTAIN:
                return mountain;
            case OUTOFMAP:
                return black;
            case DESTINATION:
                return destination;
            case MINE:
                return mine;
            default:
                return null;
        }
    }

    private void drawMap(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                //Image img = getImage(map[col][row].getType());
            	// man inja bayad ye for ru tamame game ine bezanam ke peyda konam tu har cell chi hast o chi bayad draw she.
                Cell cell = game.getMap().getCellAt(col, row);
                //if
            	Image img = getImage(cell.getType());
                map[col][row].draw(g2d, img, 0, 0, false);
                
            }
        }
    }

    private void drawDelta(Graphics g, ArrayList<Delta> deltas){
        Graphics2D g2d = (Graphics2D) g;
        //
        //
		/*for (int i = 0; i < deltas.length; i++){
			if (i == 0){
			Point[] points = new Point[4];
			points[0] = new Point(nodes[deltas[i].node1.x][deltas[i].node1.y].xpoints[1], nodes[deltas[i].node1.x][deltas[i].node1.y].ypoints[1]);
			points[1] = new Point(nodes[deltas[i].node2.x][deltas[i].node2.y].xpoints[0], nodes[deltas[i].node2.x][deltas[i].node2.y].ypoints[0]);
			points[2] = new Point(nodes[deltas[i].node2.x][deltas[i].node2.y].xpoints[2], nodes[deltas[i].node2.x][deltas[i].node2.y].ypoints[2]);
			points[3] = new Point(nodes[deltas[i].node1.x][deltas[i].node1.y].xpoints[2], nodes[deltas[i].node1.x][deltas[i].node1.y].ypoints[2]);
			Shape shape= new FJgon(points, 8);
			drawImage(g2d, shape, wall);
			}
			else{
				drawImage(g2d, nodes[deltas[i].node1.x][deltas[i].node1.y].draw(), wall);
			}
		}*/
        for (int i = 0; i < deltas.size(); i++){
            Delta delta = deltas.get(i);
            FJgon temp;
            switch (delta.getType()) {
                case MINE_DISAPPEAR:
                    //mineDisappear(g2d, deltas[i].getCell, mineDissapear);
                    //drawImage(g2d, map[delta.getSource().x][delta.getSource().y], mineDissapear);
                    break;

                case CELL_MOVE:
                    drawImage(g2d, map[delta.getDestination().x][delta.getDestination().y], attacker);
                    break;

                case AGENT_KILL:
                    // tu phase badi animation e kill
                    //drawImage(g2d, map[delta.getCell().x][delta.getCell().y], attacker);
                    break;
                case AGENT_ATTACK:
                    // tu phase badi animation e kill
                    //drawImage(g2d, map[delta.getCell().x][delta.getCell().y], attacker);
                    break;

                case AGENT_DISAPPEAR:
                    //
                    break;

                case SPAWN_ATTACKER:
                    drawImage(g2d, map[delta.getSource().x][delta.getSource().y], attacker);
                    break;
                case MINE_CHANGE:
                    // ye addad draw kon
                    drawImage(g2d, map[delta.getSource().x][delta.getSource().y], mine);
                    break;
                case WALL_DISAPPEAR:
                    //
                    break;
                case WALL_DRAW:
                	temp = getFJgonByDelta(delta);
                    drawImage(g2d, temp, walls[temp.getShib() + 1]);
                    break;
                case WALL_SEMI_DRAW:
                	temp = getFJgonByDelta(delta);
                    drawImage(g2d, getFJgonByDelta(delta), walls[temp.getShib() + 4]);
                    break;
                case WALLIE_MOVE:
                    drawImage(g2d, nodes[delta.getDestinationWallie().x][delta.getDestinationWallie().y], wallie);



                default:

                    break;
            }
        }
    }

    private int shib (Point one, Point two){
        if (one.y - two.y == 0)
            return 0;
        float shib = (float)(one.y - two.y) / (float)(one.x - one.y);
        System.out.println(shib);
        if (shib < 0)
            return -1;
        else // (ship > 0)
            return 1;
    }
    
    private FJgon getFJgonByDelta(Delta delta){
        Node source;
        Node dest;
        if (delta.getSource().x + delta.getSource().y < delta.getDestination().x + delta.getDestination().y){
            source = nodes[delta.getSource().x][delta.getSource().y];
            dest = nodes[delta.getDestination().x][delta.getDestination().y];
        }
        else{
            source = nodes[delta.getDestination().x][delta.getDestination().y];
            dest = nodes[delta.getSource().x][delta.getSource().y];
        }
        int shib = shib(source.getCircleCenter(), dest.getCircleCenter());
        Point one, two, three, four;
        if (shib < 0){
            one = new Point(source.xpoints[1], source.ypoints[1]);
            two = new Point(dest.xpoints[0], dest.ypoints[0]);
            three = new Point(dest.xpoints[2], dest.ypoints[2]);
            four =  new Point(dest.xpoints[1], dest.ypoints[1]);
        }

        else if (shib == 0){
            one = new Point(source.xpoints[1], source.ypoints[1]);
            two = new Point(dest.xpoints[1], dest.ypoints[1]);
            three = new Point(dest.xpoints[0], dest.ypoints[0]);
            four =  new Point(dest.xpoints[2], dest.ypoints[2]);
        }

        else{ //(shib > 0)
            one = new Point(source.xpoints[2], source.ypoints[2]);
            two = new Point(dest.xpoints[1], dest.ypoints[1]);
            three = new Point(dest.xpoints[0], dest.ypoints[0]);
            four =  new Point(dest.xpoints[0], dest.ypoints[0]);
        }
        return new FJgon(new Point[]{one, two, three, four}, FJframe.FJHEIGHT, shib);
    }


    private void drawImage(Graphics g, Shape shape, Image img){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g2d.setClip(shape);
        Rectangle r = shape.getBounds();
        g2d.drawImage(img, r.x, r.y, null);
    }

}