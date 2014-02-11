package javachallenge.graphics;

import java.awt.Point;

public class JCmap {
			
/*	public JCmap(int rows, int cols, Point origin){
	}*/
	
	
	public static FJNode[][] makeNodes(int rows, int cols, Hexagon[][] map) {
		// TODO Auto-generated method stub
		FJNode[][] nodes = new FJNode[cols][rows];
		Hexagon first, second, third;
		FJNode newNode;
		
		// version 1.23
		// preventing index out of bound
		for (int col = 2; col < cols - 2; col++){
			for(int row = 1; row < rows; row++){
				if ((col + row) % 2 == 1){
					first = map[col/2 - 1][row - 1];
					second = map[col/2][row - 1];
					third = map[(col - 1)/2][row];
					newNode = new FJNode(first.getPoint(2), second.getPoint(4), third.getPoint(0));
				}
				else{
					first = map[(col - 1)/2][row - 1];
					second = map[col/2][row];
					third = map[col/2 - 1][row];
					newNode = new FJNode(first.getPoint(3), second.getPoint(5), third.getPoint(1));
				}
				//nodes[col][row] = new FJNode(one, two, three)
				nodes[col][row] = newNode;
			}
		}
		return nodes;
	}


	public void makeEdges(int rows, int cols) {
		
	}


	public static Hexagon[][] makeMap(Point origin, int rows, int cols, int radius, int padding){//Graphics g, Point origin, int rows, int cols, int radius, int padding){
		Hexagon[][] map = new Hexagon[cols][rows];
		double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        //int half = size / 2;

        for (int row = 0; row < rows; row++) {
            //int cols = size; //- java.lang.Math.abs(row - half);
        	//map[row] = new Hexagon[cols];
            for (int col = 0; col < cols; col++) {
            	int x;
            	int y;
            	if (row % 2 == 0)
                	//x = (int) (origin.x + xOff * (col * 2 + 1 - size));
            		x = (int) Math.round(origin.x + xOff * (col * 2));
                else
                	//x = (int) (origin.x + xOff * (col * 2 + 2 - size));
                	x = (int) Math.round(origin.x + xOff * (col * 2 + 1));
            	
                //int y = (int) (origin.y + yOff * (row - half) * 3);
            	y = (int) Math.round(origin.y + yOff * row * 3);
            	
            	map[col][row] = new Hexagon(new Point(x, y), radius);
            	
                //version 1.21
//                drawHex(g, x, y, radius);
                //version 1.22
                //drawHex(g, x, y, radius);
            }
        }
        return map;
	}
	
/*	public static void main(String[] args) {
		JCmap fj = new JCmap(6, 6, new Point(100, 20));
		System.out.println("sdgf");
	}*/
}

