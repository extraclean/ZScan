
public class Transection {
	public double x;
	public double y; 
	public Transection transection1 = new Transection(0,0);//trilateration兩圓交點
	public Transection transection2 = new Transection(0,0);//
	  
	public Transection(int x, int y) { 
		this.x = x; 
		this.y = y; 
	} 
	public void ChangeData(double d, double e){
		this.x = d; 
		this.y = e; 
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}	 
}
