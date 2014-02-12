package javachallenge.server;

import javachallenge.util.CellType;
import javachallenge.util.Direction;
import javachallenge.util.EdgeType;
import javachallenge.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by mohammad on 2/11/14.
 */
public class DummyPanel extends JPanel {
    private final Map map;
    private Image bufferImage;
    private Image sand, ocean, zombie, ce;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public DummyPanel(Map map) {
        this.map = map;
        this.setSize(WIDTH, HEIGHT);
        this.bufferImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        try {
            this.sand = ImageIO.read(new File("dummy/desert.png"));
            this.ocean = ImageIO.read(new File("dummy/ocean.png"));
            this.zombie = ImageIO.read(new File("dummy/zombie.png"));
            this.ce = ImageIO.read(new File("dummy/ce.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawImage();
        g.drawImage(bufferImage, 0, 0, null);
    }

    private void drawImage() {
        Graphics2D g = (Graphics2D) bufferImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);
        for (int i = 0; i < map.getSizeX(); i++) {
            for (int j = 0; j < map.getSizeY(); j++) {
                int x, y = j * 54;
                if (j % 2 == 0)
                    x = i * 72;
                else
                    x = i * 72 + 36;
                switch (map.getCellAt(i, j).getType()) {
                    case TERRAIN:
                        g.drawImage(sand, x, y, null);
                        break;
                    case RIVER:
                        g.drawImage(ocean, x, y, null);
                        break;
                    case OUTOFMAP:
                        g.drawImage(ocean, x, y, null);
                }

                if (map.getCellAt(i, j).getUnit() != null)
                    switch (map.getCellAt(i, j).getUnit().getTeamId()) {
                        case 0:
                            g.drawImage(zombie, x, y, null);
                            break;
                        case 1:
                            g.drawImage(ce, x, y, null);
                            break;
                    }
            }
        }

        int[] xEdge = { 36, 72, 72, 36, 0, 0 };
        int[] yEdge = { 0, 18, 54, 72, 54, 18};

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(6));

        for (int i = 0; i < map.getSizeX(); i++) {
            for (int j = 0; j < map.getSizeY(); j++) {
                int x, y = j * 54;
                if (j % 2 == 0)
                    x = i * 72;
                else
                    x = i * 72 + 36;
                for (Direction dir : Direction.values()) {
                    if (map.getCellAt(i, j).getEdge(dir) != null &&
                            map.getCellAt(i, j).getEdge(dir).getType() == EdgeType.WALL) {
                        g.drawLine(x + xEdge[dir.ordinal()], y + yEdge[dir.ordinal()],
                                x + xEdge[(dir.ordinal() + 1) % 6], y + yEdge[(dir.ordinal() + 1) % 6]);
                    }
                }
            }
        }
    }
}
