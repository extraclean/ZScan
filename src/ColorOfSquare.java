import java.util.ArrayList;
import java.awt.Color;

public class ColorOfSquare {

	//ArrayList that'll contain the colors
	ArrayList<Color> C =new ArrayList<Color>();
	int color;  
	SquarePanel square;
	public ColorOfSquare(int col){
		
		//Lets add the color to the arrayList
		C.add(Color.RED);//0
		C.add(Color.white);   //1
		C.add(Color.GREEN);//2
		C.add(Color.BLACK);//3
		color=col;
		square = new SquarePanel(C.get(color));
	}
	public void lightMeUp(int c){
		square.ChangeColor(C.get(c));
	}
}
