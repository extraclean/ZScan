
public class Sensor {
	public  double x; //真實位置
	public  double y; //
	public MB[] queue = new MB[100];
	public int qCount = 0;	  
	public  double xe = -1; //計算位置
	public  double ye = -1; //
	public int timeout = 0;//以zs來說timeout=5要drop
//	public Sensor estimate = new Sensor(0,0);//儲存定位計算過後的座標
	
	public Sensor(double x, double y) { 
		this.x = x; 
		this.y = y;
		for(int i =0;i<100;i++)
			this.queue[i] = new MB(0,0, Simulation.Rc(ZScanController.Rc));
	} 
	public void ChangeData(double x, double y){
		this.x = x; 
		this.y = y; 
	}
	public void ChangeEstimate(double x, double y){
		this.xe = x; 
		this.ye = y; 
	}
	public void addQ(double d, double e){
		//System.out.println(this.queue[qCount]);
		this.queue[qCount].ChangeData(d, e);
		this.qCount++;
	}
	public void drop(){
		for(int i=0;i<100;i++)
			this.queue[i].ChangeData(0, 0);
		qCount=0;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}	 
	public double getXe(){
		return xe;
	}
	public double getYe(){
		return ye;
	}	  	
}
