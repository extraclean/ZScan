import java.util.Comparator;

public class MB implements Comparable<MB>{
	public double x;
	public double y; 
	public double Rc;
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	public double distance;
//	public Transection transection1 = new Transection(0,0);//trilateration兩圓交點
//	public Transection transection2 = new Transection(0,0);//
	  
	public MB(int x, int y, double Rc) { 
		this.x = x; 
		this.y = y; 
		this.Rc = Simulation.Rc(Rc);
	} 
	public void ChangeData(double d, double e){
		this.x = d; 
		this.y = e; 
	}
	public void ChangeTransection1(double d, double e){
		this.x1 = d; 
		this.y1 = e; 
	}
	public void ChangeTransection2(double d, double e){
		this.x2 = d; 
		this.y2 = e; 
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}	 
	public double getX1(){//trilateration
		return x1;
	}
	public double getY1(){//trilateration
		return y1;
	}	 
	public double getX2(){//trilateration
		return x2;
	}
	public double getY2(){//trilateration
		return y2;
	}
	public void changeDistance(double d) {
		this.distance = d;
	}
	public double getDistance() {
		return this.distance;
	}	 
	public int compareTo(MB compareDistance){
		double compare = ((MB)compareDistance).getDistance();
		return (int) (this.distance - compare);//ascending order
		//return (int) (compare - this.distance);//descending order
	}
}
