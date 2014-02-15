package mapmaker;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class mapmaker {
    public static int size = 20;
    public static Color[] colors;
    public static JButton[][] cell;
    public static int x;
    public static int y;

    public static void main(String[] args) {
        colors = new Color[7];
        colors[0] = Color.green;
        colors[1] = Color.blue;
        colors[2] = Color.lightGray;
        colors[3] = Color.YELLOW;
        colors[4] = Color.BLACK;
        colors[5] = Color.magenta;
        colors[6] = Color.red;
        x = Integer.valueOf(JOptionPane.showInputDialog("X:"));
        y = Integer.valueOf(JOptionPane.showInputDialog("Y:"));
        //Page
        JFrame mapMaker = new JFrame("Map Maker");
        mapMaker.setLayout(null);
        mapMaker.setSize(1300, 700);
        mapMaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Map Panel
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, x * size + 20 + 12, y * size + 20);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        //Help Panel
        JPanel helpPanel = new JPanel();
        helpPanel.setBounds(1000, 0, 500, 500);
        helpPanel.setLayout(null);
        helpPanel.setBackground(Color.white);
        mapMaker.add(helpPanel);
        //Help Buttons
        JButton[] helps = new JButton[7];
        JLabel[] helpText = new JLabel[7];
        String[] text = {"TERRAIN","RIVER","MOUNTAIN","MINE","OUT OF MAP","SPAWN","DESTINATION"};
        for(int i = 0; i < 7; i++){
            helps[i] = new JButton(String.valueOf(i));
            helps[i].setBackground(colors[i]);
            helps[i].setBounds(10, i * 60, 50, 50);
            helpText[i] = new JLabel(text[i]);
            helpText[i].setBounds(70, i * 60 + 10, 100, 35);
            helpPanel.add(helpText[i]);
            helpPanel.add(helps[i]);
        }
        //Map
        cell = new JButton[x][y];
        ActionListener a = new cellAction(colors);
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                cell[i][j] = new JButton();
                cell[i][j].addActionListener(a);
                if(j % 2 == 1)
                    cell[i][j].setBounds(i * size + (size / 2) + 2, j * size + 2, size, size);
                else
                    cell[i][j].setBounds(i * size + 2, j * size + 2, size, size);
                if(i == 0 || i == x - 1 || j == 0 || j == y - 1) {
                    cell[i][j].setName("4");
                    cell[i][j].setBackground(colors[4]);
                }
                else{
                    cell[i][j].setName("0");
                    cell[i][j].setBackground(colors[0]);
                }
                cell[i][j].setVisible(true);
                panel.add(cell[i][j]);

            }
        }
        //MenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);
        JMenuItem saveItem = new JMenuItem("save");
        saveItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println(x);
                System.out.println(y);
                for (int i = 0; i < y; i++) {
                    for (int j = 0; j < x; j++) {
                        System.out.format("%s ", cell[j][i].getName());
                    }
                    System.out.format("\n");
                }


            }
        });
        menu.add(saveItem);
        mapMaker.setJMenuBar(menuBar);
        mapMaker.add(panel);
        mapMaker.setVisible(true);

    }



}

class cellAction implements ActionListener{
    private Color[] col;
    public cellAction(Color[] in){
        super();
        col = in;
    }
    public void actionPerformed(ActionEvent e){
        int m = Integer.valueOf(((JButton)e.getSource()).getName());
        m = (m + 1) % 6;
        ((JButton)e.getSource()).setBackground(col[m]);
        ((JButton)e.getSource()).setName(String.valueOf(m));
    }
}
