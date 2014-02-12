package javachallenge.graphics;

import javachallenge.server.Game;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JFrame;

public class FJframe extends JFrame{
	private int rows;
	private int cols;
	public static final int RADIUS = 24;
	public static final int PADDING = 4;
    public static final int FJHEIGHT = 4;
	private Hexagon[][] map;
	private FJNode[][] nodes;
	private FJpanel panel;
    private Game game;
    private ArrayList<Hexagon[]> outOfMaps;
	
	
	public FJframe(Game game, int rows, int cols){
		super();

        this.rows = rows;
        this.cols = cols;

		// setting basic properties
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Java Challenge 1392");
		// full screen
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.game = game;

        map = JCmap.makeMap(new Point(50,  20), rows, cols, RADIUS, PADDING);
        nodes = JCmap.makeNodes(rows, 2 * (cols + 1), map);
        /*for (int i = 0; i<outOfMaps.size(); i++){
            outOfMaps.set(i, JCmap.makeOutofMapHexagon(new Point(50,  20), rows, cols, RADIUS, PADDING, i));
        }*/

		initUI();
        setVisible(true);
	}
	
	
	
	private void initUI(){
        this.setBackground(Color.BLACK);
		FJpanel panel = new FJpanel(game, map, nodes, rows, cols);
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