import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {//area 一定要 （偶,奇）
//Rc: 10(m), ZScan Rc 宣告
//Rc模擬高斯誤差10%
		
		ZScanController.X=Integer.valueOf(args[0]);
		ZScanController.Y=Integer.valueOf(args[1]);
//		Window.direction=Integer.valueOf(args[1]);
		 
		//Create the window 
		Window f1= new Window();
		
		//Set up the window
		f1.setTitle("ZScan_Curve_v1.0");
		f1.setSize(800,800);
		f1.setVisible(false);
	f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             

	}
}
