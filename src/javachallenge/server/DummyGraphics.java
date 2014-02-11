package javachallenge.server;

import javachallenge.units.Unit;
import javachallenge.units.UnitCE;
import javachallenge.util.Map;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by mohammad on 2/11/14.
 */
public class DummyGraphics extends JFrame {
    public DummyGraphics() throws HeadlessException, IOException {
        super("Java Challenge Tester");
        this.setSize(new Dimension(800, 600));
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Map map = Map.loadMap("test.map");
        Unit test = new UnitCE(), ce = new UnitCE();
        ce.setTeamId(1);
        map.getCellAt(2, 3).setUnit(test);
        map.getCellAt(6, 4).setUnit(ce);
        System.out.println("Loaded!");
        DummyPanel dp = new DummyPanel(map);
        this.add(dp);
    }

    public static void main(String[] args) {
        try {
            new DummyGraphics().setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
