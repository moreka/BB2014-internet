package javachallenge.graphics;

import javachallenge.message.Delta;
import javachallenge.server.Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FJframe extends JFrame{
	public static final int ROWS = 6;
	public static final int COLS = 6;
	public static final int RADIUS = 30;
	public static final int PADDING = 15;
    public static final int FJHEIGHT = 8;
	private Hexagon[][] map;
	private Node[][] nodes;
	private FJpanel panel;
    private Game game;
	
	
	public FJframe(Game game){
		super();
		
		// setting basic properties
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Challenge 1392");
		// full screen
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.game = game;

        map = JCmap.makeMap(new Point(50,  20), ROWS, COLS, RADIUS, PADDING);
        nodes = JCmap.makeNodes(ROWS, 2 * COLS + 1, map);

		initUI();
	}
	
	
	
	private void initUI(){
		FJpanel panel = new FJpanel(game, map, nodes, ROWS, COLS);
		getContentPane().add(panel);
		//setLayout(null);
//		JButton button = new JButton();
//		button.setText("sdgasdgasg");
		//panel.add(button);
		
		
		this.add(panel);
		this.panel = panel;
	}
	
	public FJpanel getPanel(){
		return panel;
	}
}