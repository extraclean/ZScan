import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;


class Window extends JFrame{
	private static final long serialVersionUID = -2542001418764869760L;
	public static ArrayList<ArrayList<ColorOfSquare>> Grid;
	public static int width = 128;
	public static int height = 128;
	//public static int direction ;
	public static int startX ;
	 public static int startY ;
	public Window(){
		
		// Creates the arraylist that'll contain the threads
		Grid = new ArrayList<ArrayList<ColorOfSquare>>();
		ArrayList<ColorOfSquare> data;
		
		// Fill the panel-space with 2D array list
		//light up with white color
		for(int i=0;i<width;i++){
			data= new ArrayList<ColorOfSquare>();
			for(int j=0;j<height;j++){
				ColorOfSquare c = new ColorOfSquare(1);
				data.add(c);
			}
			Grid.add(data);
		}
		
		// Setting up the layout of the panel
		getContentPane().setLayout(new GridLayout(128,128,0,0));
		
		//adds every square of each pixel to the panel
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				getContentPane().add(Grid.get(i).get(j).square);
			}
		}

		startX = 0;
		startY = 0;
		
		//initial position
		Tuple position = new Tuple(startX,startY);
		//passing this value to the controller
		ZScanController z = new ZScanController(position);
		z.start();
//		HilbertController h = new HilbertController(position);
//		h.start();
	}
}
