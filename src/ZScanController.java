import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

//Controls all the ZScan curve logic
public class ZScanController extends Thread {
	 ArrayList<ArrayList<ColorOfSquare>> Squares= new ArrayList<ArrayList<ColorOfSquare>>();
	 Tuple currentPosition;
	 long speed = 0;
	 //public static int order ;
	public static double X;
	public static double Y;
	public static float distance = 0;
	public static float ZCdistance = 0;
	public static float ZSdistance = 0;
	public static float Hdistance = 0;
	public static double MBR = 0;//to calculate when to stop
	public static double Hkey = 1;//to calculate when to stop
	public static double ZCkey = 0;//to calculate when to stop
	public static double count = 0;//to eliminate redundant key
	public static int sNumber = 1000;//sensor number
	public static double bound = 0;//the number that each two column or two row maximal redundant key
	public static Sensor[] sensor = new Sensor[sNumber];//sensor data
	public static int key_count = 0;//計算目前是第幾個key
	public static MB[] key = new MB[1280];//預設32S*32S(32*32有1280個key)
	public static int Rc = 100;//通訊半徑設為10m
	private int flag = 0;//決定要不要使用timeout
	private int APT = 0;//如果要用ＡＰＴ改成1
	
	 ArrayList<Tuple> positions = new ArrayList<Tuple>();//key
	 ArrayList<Path> trajectory = new ArrayList<Path>();//path
	 ArrayList<Redundant> Rkey = new ArrayList<Redundant>(); //Redundant key
	 
	 //Constructor  
	 ZScanController(Tuple positionDepart){
		 
		//Get all the pixels
		Squares=Window.Grid;
		//get the current position
		currentPosition=new Tuple(positionDepart.x,positionDepart.y);
		Tuple headPos = new Tuple(currentPosition.getX(),currentPosition.getY());
		positions.add(headPos);
		for(int i=0; i< key.length;i++)
			key[i] = new MB(0, 0, Rc);
	 }
	 
	 //run
	 public void run() {//determine which algorithm & function to run
		//Readsensor();
//		 ZCdetermine();
		 
//		 Hdetermine();
		// showLength();
				 
		 deploySensor();
		 readSensor();
		 
		 RWP();
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count); 
		 System.out.println("length: "+distance);
		 flush();
		 
		 Scan();
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count); 
		 System.out.println("length: "+distance);
		 flush();
		 
		 doubleScan();
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count); 
		 System.out.println("length: "+distance);
		 flush();
		 
		 flag = 1;
		 ZSdetermine();
		 writeEstimate("ZSestimate.txt");
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count); 
		 System.out.println("length: "+ZSdistance);
//		 System.out.println(key_count); 
//		 System.out.println(ZSdistance);
		 flush();
		 
		 flag = 1;
		 Hdetermine();
		 writeEstimate("Hestimate.txt");
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count);
		 System.out.println("length: "+Hdistance);
//		 System.out.println(key_count);
//		 System.out.println(Hdistance);
		 
		 flush();
		 flag =1;
		 ZCdetermine();
		 writeEstimate("ZCestimate.txt");
		 System.out.println(Simulation.errorRate());
		 System.out.println(Simulation.coverage());
		 System.out.println("key: "+key_count);
		 System.out.println("length: "+ZCdistance);
//		 System.out.println(key_count);
//		 System.out.println(ZCdistance);
		 
		 //average();//跑1000次會花非常多時間，小心用
	 }
	 
	 private void flush(){//清空key
		 key_count = 0;
		 distance = 0;
		 for(int i =0;i<1000;i++){
			 for(int j=0;j<100;j++){
				 sensor[i].queue[j].ChangeData(0,0);
				 sensor[i].qCount = 0;
				 sensor[i].timeout = 0;
				 sensor[i].ChangeEstimate(-1, -1);
			 }
		 }
	 }
	 
	 private void deploySensor(){
		 try {//佈點
			Simulation.deployment(X*Simulation.step(), Y*Simulation.step(), sNumber);//X,Y,sensor number
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 private void readSensor(){
		 
		 for(int i=0; i< sensor.length;i++)
			    sensor[i] = new Sensor(0, 0);
		 try {//讀點
			Simulation.readSensor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 private void writeEstimate(String name){
		 try {
				FileWriter fw = new FileWriter(name);
				 for(int i =0 ; i<sNumber ; i++){
				 			fw.write((double) (sensor[i].getXe())+",");
				 	        fw.write((double) (sensor[i].getYe())+"\r\n");
				 }
				 	        fw.flush();
				 	        fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 private void initialKey(){
		 
		 key[key_count].ChangeData(((currentPosition.x*0.25)+0.5)*Simulation.step(), ((currentPosition.y*0.25)+0.5)*Simulation.step());
		 for(int i =0;i<sensor.length;i++){
			 if(Simulation.Rc(Rc)>=Simulation.distance(sensor[i], key[key_count])){
//			 if(Rc>=Simulation.distance(i, key_count)){
				 sensor[i].addQ(key[key_count].getX(), key[key_count].getY());
				 sensor[i].timeout++;
			 }
			 if(APT==0&&sensor[i].timeout>=6&&sensor[i].getXe()==0&&sensor[i].getYe()==0&&flag==0){
				 sensor[i].drop();//timeout system triggered
			 }
			 if(APT==0&&sensor[i].qCount>=2){
				 Localization.TPT(i);//每個key執行定位
				 //Localization.WCL(i);
			 }
			 if(APT==1){
				 Localization.APT(i);
			 }
		 }
		 key_count++;
	 }
	 
	 //delay for all the steps
	 private void pauser(){
		 try {
				sleep(speed);
		 } catch (InterruptedException e) {
				e.printStackTrace();
		 }
	 }
	 
	 //display the newest position
	 private void lightup(){
//		 for(Tuple t : positions){
//			 int y = t.getX();
//			 int x = t.getY();
//			 Squares.get(x).get(y).lightMeUp(0); //0: red
//		 }
//		 for(Path t : trajectory){
//			 int y = t.getX();
//			 int x = t.getY();
//			 Squares.get(x).get(y).lightMeUp(3); // 3: black
//		 }
//		 for(Redundant t : Rkey){
//			 int y = t.getX();
//			 int x = t.getY();
//			 Squares.get(x).get(y).lightMeUp(2); // 2: green
//		 }
//		 pauser();
	}
	 
	 
//////////////////////////////////////////////////////////
//Circle START
/////////////////////////////////////////////////////////
	 private void Circle(){
		 key[key_count].ChangeData(((currentPosition.x*0.25)+0.5)*Simulation.step(), ((currentPosition.y*0.25)+0.5)*Simulation.step());
		 for(int i =0;i<sensor.length;i++){
			 if(Simulation.Rc(Rc)>=Simulation.distance(sensor[i], key[key_count])){
//		 if(Rc>=Simulation.distance(i, key_count)){
				 sensor[i].addQ(key[key_count].getX(), key[key_count].getY());
				 sensor[i].timeout++;
			 }
			 if(APT==0&&sensor[i].timeout>=6&&sensor[i].getXe()==0&&sensor[i].getYe()==0&&flag==0){
				 sensor[i].drop();//timeout system triggered
			 }
			 if(APT==0&&sensor[i].qCount>=2){
				 Localization.TPT(i);//每個key執行定位
				 //Localization.WCL(i);
			 }
			 if(APT==1){
				 Localization.APT(i);
			 }
	 		}
	 	key_count++;
	 }
//////////////////////////////////////////////////////////
//Circle END
/////////////////////////////////////////////////////////
	 
//////////////////////////////////////////////////////////
//RWP START
/////////////////////////////////////////////////////////
	 private void RWP(){
		 int i = 300;
		 distance = 0;
		 int tmpX,tmpY;
		 while(i!=0){
			tmpX = currentPosition.x;
			tmpY = currentPosition.y;
			currentPosition.ChangeData((int)(Math.random()*(X*4)),(int)(Math.random()*(Y*4)));
		 	positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 	lightup();
		 	distance += Math.sqrt(Math.pow(currentPosition.x-tmpX, 2)+Math.pow(currentPosition.y-tmpY, 2));
		 	initialKey();//key broadcast to sensors
		 	i--;
		 }
	 }
	 
//////////////////////////////////////////////////////////
//RWP END
/////////////////////////////////////////////////////////
	 
	 
//////////////////////////////////////////////////////////
//Scan START
/////////////////////////////////////////////////////////

	 	private void Scan(){
	 		currentPosition.x = 0;
	 		currentPosition.y = 0;
			distance = 0;
			 
	 		initialKey();
	 		
	 		while(currentPosition.x/4<(X-1)){
	 			while(currentPosition.y/4<(Y-1))
	 				Dstep();
	 			if(currentPosition.x/4<(X-1)){
	 				Rstep();
	 				while(currentPosition.y/4>0)
	 					Ustep();
	 			}
	 			if(currentPosition.x/4<(X-1))
	 				Rstep();
	 		}
	 	}
	 
//////////////////////////////////////////////////////////
//Scan END
/////////////////////////////////////////////////////////
	 	
//////////////////////////////////////////////////////////
//Double Scan START
/////////////////////////////////////////////////////////

	 	private void doubleScan(){
	 		currentPosition.x = 0;
	 		currentPosition.y = 0;
	 		distance = 0;

	 		initialKey();

	 		while(currentPosition.x/4<(X-2)){
	 			while(currentPosition.y/4<(Y-1))
	 				Dstep();
	 			if(currentPosition.x/4<(X-1)){
	 				Rstep();
	 				Rstep();
	 				while(currentPosition.y/4>0)
	 					Ustep();
	 			}
	 			if(currentPosition.x/4<(X-2)){
	 				Rstep();
	 				Rstep();
	 			}
	 		}
	 		if(currentPosition.y==0){
	 			while(currentPosition.y/4<(Y-2)){
	 				while(currentPosition.x/4>0)
	 					Lstep();
	 				if(currentPosition.y/4<(Y-1)){
	 					Dstep();
	 					Dstep();
	 					while(currentPosition.x/4<(X-1))
	 						Rstep();
	 				}
	 				if(currentPosition.y/4<(Y-2)){
	 					Dstep();
	 					Dstep();
	 				}
	 			}
	 		}else{
	 			while(currentPosition.y/4>1){
	 				while(currentPosition.x/4>0)
	 					Lstep();
	 				if(currentPosition.y/4>0){
	 					Ustep();
	 					Ustep();
	 					while(currentPosition.x/4<(X-1))
	 						Rstep();
	 				}
	 				if(currentPosition.y/4>0){
	 					Ustep();
	 					Ustep();
	 				}
	 			}
	 		}
	 	}

//////////////////////////////////////////////////////////
//Double Scan END
/////////////////////////////////////////////////////////
	 
		//////////////////////////////////////////////////////////
		//Z SCAN START
		/////////////////////////////////////////////////////////
	 private void ZSdetermine(){
		 ZSdistance = 0;
		 currentPosition.x = 0;
		 currentPosition.y = 0;
		 distance = 0;
		 int i=0,j=0;

		 initialKey();
		 
		 for (i=0; i<X/2; i++) {
			 if ((i+1)>Math.floor(X/2) && ((X+1)/2)%2==1) {
				 for (j=0; j<Y/2; j++) {
					 if ((currentPosition.y/4) == (Y-3)) {
						 w();//special unit
						 j++;
		             }else{
		            	 v();//take ㄑ
		            	 if((currentPosition.y/4) != (Y-1)) Dstep();
		             }
				 }
			 } else if((i+1)>Math.floor(X/2) && ((X+1)/2)%2==0){
				 for (j=0; j<Y/2; j++) {
					 if ((currentPosition.y/4) == 2) {
						 re_w();//special unit
						 j++;
		             }else{
		            	 re_v();//take ㄑ
		            	 if((currentPosition.y/4) != 1) Ustep();
		             }
				 }
			 }else if ((currentPosition.x/4)%4<2) {
				 for (j=0; j<Y/2; j++) {
					 if ((j+1)>Math.floor(Y/2)) {
						 half_left_down();
						 half_Z();//take ㄥ
					 } else if ((currentPosition.x/4)%2==0) {
						 Z();//take Z
					 } else {
						 sigma();//take sigma
					 }
					 if ((j+1)<Math.floor(Y/2)) Dstep();//??
				 }
				 if((currentPosition.x/4)<X-1) Rstep();
			 } else if((currentPosition.x/4)%4>=2) {
				 for (j=0; j<Y/2; j++) {
					 if ((j+1)>Math.floor(Y/2)) {
						 half_left_up();
						 re_half_Z();//take re-ㄥ
					 } else if (currentPosition.x%8==0) {
						 re_Z();//take re-Z
					 } else {
						 re_sigma();//take sigma
					 }
					 if ((j+1)<Math.floor(Y/2)) Ustep();//??
				 }
				 if((currentPosition.x/4)<X-1) Rstep();
			 }
		 }
		 
//		 if(X%2==1 && Y%2==1){
//			 if(X>Y){
//				 bound = (int)(((X-3)/2)*4);
//				 right(X, Y);
//			 }
//			 else{
//				 bound = (int)(((Y-3)/2)*4);
//				 down(X, Y);
//			 }
//		 }else  if(X%2==1 ){
//			 bound = (int)(((Y-2)/2)*4);
//			 down(X, Y);
//		 }
//		 else if(Y%2==1){
//			 bound = (int)(((X-2)/2)*4);
//			 right(X, Y);
//		 }
//		 else if(X>Y){
//			 bound = (int)(((X-2)/2)*4);
//			 right(X,Y);
//		 }
//		 else{
//			 bound = (int)(((Y-2)/2)*4);
//			 down(X, Y);
//		 }
	 	}
	 
	 private void sigma(){
		 Lstep();
		 half_right_down();
		 half_left_down();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void re_sigma(){
		 Lstep();
		 half_right_up();
		 half_left_up();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void Z(){
		 Rstep();
		 half_left_down();
		 half_left_down();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void re_Z(){
		 Rstep();
		 half_left_up();
		 half_left_up();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void half_Z(){
		 half_left_down();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void re_half_Z(){
		 half_left_up();
		 Rstep();
		 ZSdistance = distance;
	 }
	 
	 private void v(){
		 half_left_down();
		 half_right_down();
		 ZSdistance = distance;
	 }
	 
	 private void re_v(){
		 half_left_up();
		 half_right_up();
		 ZSdistance = distance;
	 }
	 
	 private void w(){
		 half_left_down();
		 half_right_down();
		 half_left_down();
		 half_right_down();
		 ZSdistance = distance;
	 }
	 
	 private void re_w(){
		 half_left_up();
		 half_right_up();
		 half_left_up();
		 half_right_up();
		 ZSdistance = distance;
	 }
	 
	 //////////////////////
	 //Z Scan End
	 /////////////////////
	 private void right(int X, int Y){
		 if(Y%2==1 && currentPosition.y ==  (Y-1)*4 && currentPosition.x == (X-2)*4 ) {//X is odd (end point)
			 half_right_down();
			 half_right_up();			 
			 ZSdistance = distance;
		 }else if(Y%2==1 && currentPosition.y ==  (Y-1)*4) {//X is odd (last line begin)
			 half_right_down();
			 half_right_up();
			 
			 right(X,Y);
			 ZSdistance = distance;
		 }else  if(currentPosition.x == (X-2)*4 && currentPosition.y ==  (Y-2)*4 ){//lower-right is the end
			Dstep(); 
			 half_right_up(); 
			 half_right_up();
			Dstep();
			 ZSdistance = distance;
		 }else  if(currentPosition.x == (X-2)*4 ){//meet the right bound
			Dstep();
			 half_right_up();
			 half_right_up();
			Dstep();
			Dstep();
			count = 0;//change line count reset
			 
			 left(X,Y);
			 ZSdistance = distance;
		 }
		 else{//keep going upward
			Dstep();
			 half_right_up();
			 half_right_up();
			 
			 right(X,Y);
			 ZSdistance = distance;
		 }
	 }

	private void left(int X, int Y){
		 if(Y%2==1 && currentPosition.y == (Y-1)*4 && currentPosition.x == 4 ) {//X is odd (end point)
			 half_left_down();
			 half_left_up();			
			 ZSdistance = distance; 
		 }else if(Y%2==1 && currentPosition.y == (Y-1)*4 ) {//X is odd (last line begin)
			 half_left_down();
			 half_left_up();
			 
			 left(X,Y);
			 ZSdistance = distance;
		 }else  if(currentPosition.y ==  (Y-2)*4 && currentPosition.x == 4){//upper-right is the end
			Dstep(); 
			 half_left_up(); 
			 half_left_up();
			Dstep();
			 ZSdistance = distance;
		 }else  if(currentPosition.x == 4){//meet the upper bound
			Dstep();
			 half_left_up();
			 half_left_up();
			Dstep();
			Dstep();
			count = 0;//change line count reset
			 
			 right(X,Y);
			 ZSdistance = distance;
		 }
		 else{//keep going upward
			Dstep();
			 half_left_up();
			 half_left_up();
			 
			 left(X,Y);
			 ZSdistance = distance;
		 }
	 }
	 
		//print each order of up U.S. on the JPanel
	 private void up(int X, int Y){
		 if(X%2==1 && currentPosition.x == (X-1)*4 && currentPosition.y == 4 ) {//X is odd (end point)
			 half_right_up();
			 half_left_up();
			 ZSdistance = distance;
		 }else if(X%2==1 && currentPosition.x == (X-1)*4 ) {//X is odd (last line begin)
			 half_right_up();
			 half_left_up();
			 
			 up(X,Y);
			 ZSdistance = distance;
		 }else  if(currentPosition.y == 4 && currentPosition.x == (X-2)*4 ){//upper-right is the end
			Rstep(); 
			half_left_up();
			half_left_up();
			Rstep();
			 ZSdistance = distance;
		 }else  if(currentPosition.y == 4){//meet the upper bound
			Rstep();
			 half_left_up();
			 half_left_up();
			Rstep();
			Rstep();
			count = 0;//change line count reset
			 
			 down(X,Y);
			 ZSdistance = distance;
		 }
		 else{//keep going upward
			Rstep();
			 half_left_up();
			 half_left_up();
			 
			 up(X,Y);
			 ZSdistance = distance;
		 }
	 }
	 
	//print each order of down U.S. on the JPanel
	 private void down(int X, int Y){ 
		 if(X%2==1 && currentPosition.x == (X-1)*4 && currentPosition.y == (Y-2)*4 ) {//X is odd (end point)
			half_right_down();
			half_left_down();
			 ZSdistance = distance;
		 }else if(X%2==1 && currentPosition.x == (X-1)*4  ) {//X is odd (last line begin)
			 half_right_down();
			 half_left_down();
			 
			 down(X,Y);
			 ZSdistance = distance;
		 }else  if(currentPosition.y == (Y-2)*4 && currentPosition.x == (X-2)*4 ){//lower-right is the end
		Rstep();
		 half_left_down();
		 half_left_down();
		Rstep();
		 ZSdistance = distance;
	 }else if(currentPosition.y == (Y-2)*4){//meet the lower bound
		Rstep();
		 half_left_down();
		 half_left_down();
		Rstep();			 
		Rstep();
		count = 0;//change line count reset

		 up(X,Y);
		 ZSdistance = distance;
		 }
		 else{//keep going down
			Rstep();
			 half_left_down();
			 half_left_down();
	
			 down(X,Y);
			 ZSdistance = distance;
		 }
	 }
	 private void Rstep(){
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance++;
	 }

	 private void left_up(){
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance+=Math.sqrt(2);
	 }
	 
	 private void left_down(){
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance+=Math.sqrt(2);
	 }
	 
	 private void Dstep(){
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance++;
	 }
	 
	 private void Ustep(){
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance++;
	 }
	 
	 private void Lstep(){
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance++;
	 }
	 
	 private void right_up(){
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance+=Math.sqrt(2);
	 }

	 private void right_down(){
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
		 lightup();
		 initialKey();//key broadcast to sensors
		 
		 distance+=Math.sqrt(2);
	 }
	 
	 private void half_right_up(){
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
//		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
//			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
//			 lightup();
//		 }else{
			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
			 initialKey();//key broadcast to sensors
//		 }

			 count++;
		 distance+=Math.sqrt(2)/2;
	 }
	 
	 private void half_left_up(){
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
//		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
//			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
//			 lightup();
//		 }else{
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
			 initialKey();//key broadcast to sensors
//		 }

			 count++;
		 
		 distance+=Math.sqrt(2)/2;
	 }
	 
	 private void half_left_down() {
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
//		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
//			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
//			 lightup();
//		 }else{
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
			 initialKey();//key broadcast to sensors
//		 }

			 count++;
		 
		 distance+=Math.sqrt(2)/2;
		}

		private void half_right_down() {
			currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
			 trajectory.add(new Path(currentPosition.x,currentPosition.y));
			 lightup();
//			 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
//				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//				 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
//				 lightup();
//			 }else{
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 initialKey();
//			 }

				 count++;
			 
			 distance+=Math.sqrt(2)/2;
	}
		//////////////////////////////////////////////////////////
		//Z SCAN END
		/////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////
		//HILBERT START
		/////////////////////////////////////////////////////////
		private void Hdetermine(){
			Hkey = 0;
			currentPosition.x = 0;
			currentPosition.y = 0;
			MBR = 0;
			initialKey();
			 if(X > Y)
				 down((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
			 else 
				 down((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
				 //System.out.println((int) Math.ceil(Math.log(Y)/Math.log(2)));
			 
		 }
		
		private int checkMBR(){
			if(Math.log(X)/Math.log(2) -  (int)(Math.log(X)/Math.log(2)) == 0 && Math.log(Y)/Math.log(2)- (int)(Math.log(Y)/Math.log(2)) == 0 ){
				Hkey = X*Y;
				return 0;
			}
			Hkey++;
			 if(currentPosition.getX() <= (X-1)*4 && currentPosition.getY() <= (Y-1)*4)
				 MBR++;
			 if(MBR >= (X*Y)){
				Hkey--;
				 return -1;
			 }
//			 System.out.println(MBR);
			 return 0;
		 }
		
		 //print each order of right U.S. on the JPanel
		 private int right(int m){
			 if(m==1){
				 if(checkMBR() == -1)
					 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(down(m-1) == -1)
						 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(right(m-1) == -1)
				 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(right(m-1) == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(up(m-1) == -1)
					 return -1;
			 }
			return 0;
		 }
		 
		//print each order of left U.S. on the JPanel
		 private int left(int m){
			 if(m==1){
				 if(checkMBR() == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(up(m-1) == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(left(m-1) == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(left(m-1) == -1)
					 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(down(m-1) == -1)
					 return -1;
			 }
			return 0;
		 }
		 
		//print each order of up U.S. on the JPanel
		 private int up(int m){
			 if(m==1){
				 if(checkMBR() == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(left(m-1) == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(up(m-1) == -1)
					 return -1;
				 Ustep();
				 Hdistance = distance;
				 if(up(m-1) == -1)
					 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(right(m-1) == -1)
					 return -1;
			 }
			return 0;
		 }
		 
		//print each order of down U.S. on the JPanel
		 private int down(int m){
			 if(m==1){
				 if(checkMBR() == -1)
					 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(right(m-1) == -1)
					 return -1;
				 Rstep();
				 Hdistance = distance;
				 if(down(m-1) == -1)
					 return -1;
				 Dstep();
				 Hdistance = distance;
				 if(down(m-1) == -1)
					 return -1;
				 Lstep();
				 Hdistance = distance;
				 if(left(m-1) == -1)
					 return -1;
			 }
			return 0;
		 }
		//////////////////////////////////////////////////////////
		//HILBERT END
		/////////////////////////////////////////////////////////
		
			//////////////////////////////////////////////////////////
			//Z Curve START
			/////////////////////////////////////////////////////////
			private void ZCdetermine(){
				ZCdistance = 0;
				ZCkey = 0;
				currentPosition.x = 0;
				currentPosition.y = 0;
				MBR = 0;
				initialKey();
				 if(X > Y)
					 ZCdown((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
				 else 
					 ZCright((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
					 //System.out.println((int) Math.ceil(Math.log(Y)/Math.log(2)));
				 
			 }
			
			private int ZCcheckMBR(){//check whether every square is traveled
				 ZCkey++;
				 if(Math.log(X)/Math.log(2) -  (int)(Math.log(X)/Math.log(2)) == 0 && Math.log(Y)/Math.log(2)- (int)(Math.log(Y)/Math.log(2)) == 0 )
						return 0;
				 if(currentPosition.getX() <= (X-1)*4+2 && currentPosition.getY() <= (Y-1)*4+2)
					 MBR++;
				 if(X%2==0&&Y%2==0){
					 if(MBR >= (X/2*5*Y/2))
							return -1;
				 }else if(X%2==0&&Y%2==1){
					 if(MBR>=((Y-1)/2*X/2*5)+(X/2*3)-1)
						 return -1;
				 }else if(Y%2==0&&X%2==1){
					 if(MBR>=((X-1)/2*Y/2*5)+(Y/2*3)-1)
						 return -1;
				 }else if(MBR>=((Y-1)/2*(X-1)/2*5)+((X-1)/2*3)+((Y-1)/2*3)+1)
						 return -1;

//				 System.out.println(MBR);
				 return 0;
			 }
			
			 //print each order of right U.S. on the JPanel
			 private int ZCright(int m){
				 if(m==1){
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Rstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_left_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_left_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Rstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
				 }
				 else if(m==2){
					 if(ZCdown(m-1) == -1)
							 return -1;
					 left_down();
					 ZCdistance = distance;
					if( ZCdown(m-1) == -1)
						return -1;
					 Rstep();
					 ZCdistance = distance;
					if( ZCup(m-1) == -1)
						return -1;
					 left_up();
					 ZCdistance = distance;
					 if(ZCup(m-1) == -1)
							return -1;
				 }else{
					 if(ZCdown(m-1) == -1)
							return -1;
					 Dstep();
					 ZCdistance = distance;
					 if(ZCright(m-1) == -1)
							return -1;
					 Rstep();
					 ZCdistance = distance;
					 if(ZCright(m-1) == -1)
							return -1;
					 Ustep();
					 ZCdistance = distance;
					 if(ZCup(m-1) == -1)
							return -1;
				 }
				return 0;
			 }
			 
			 private int reup(int m){//special case in level 2 left curve
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Ustep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_down();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_down();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Ustep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 
				 return 0;
			 }
			 
			 private int redown(int m){//special case in level 2 left curve
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Dstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Dstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 
				 return 0;
			 }
			 
			 private int releft(int m){//special case in level 2 UP curve
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Lstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_right_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_right_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Lstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 
				 return 0;
			 }
			 
			 private int reright(int m){//special case in level 2 UP curve
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Rstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 half_left_up();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 Rstep();
				 ZCdistance = distance;
				 if(ZCcheckMBR() == -1)
					 return -1;
				 
				 return 0;
			 }
			 
			//print each order of left U.S. on the JPanel
			 private int ZCleft(int m){
				 if(m==1){
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_right_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_right_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
				 }
				 else if(m==2){
					 if(reup(m-1) == -1)
							return -1;
					 right_up();
					 ZCdistance = distance;
					 if(reup(m-1) == -1)
							return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(redown(m-1) == -1)
							return -1;
					 right_down();
					 ZCdistance = distance;
					 if(redown(m-1) == -1)
							return -1;
				 }else{
					 if(ZCup(m-1) == -1)
							return -1;
					 Ustep();
					 ZCdistance = distance;
					 if(ZCleft(m-1) == -1)
							return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(ZCleft(m-1) == -1)
							return -1;
					 Dstep();
					 ZCdistance = distance;
					 if(ZCdown(m-1) == -1)
							return -1;
				 }
				return 0;
			 }
			 
			//print each order of up U.S. on the JPanel
			 private int ZCup(int m){
				 if(m==1){
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Ustep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_right_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 half_right_down();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Ustep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
				 }
				 else if(m==2){
					if( releft(m-1) == -1)
						return -1;
					left_down();
					 ZCdistance = distance;
					if(releft(m-1) == -1)
							return -1;
					Ustep();
					 ZCdistance = distance;
					if(reright(m-1) == -1)
						return -1;
					right_down();
					 ZCdistance = distance;
					 if(reright(m-1) == -1)
							return -1;
				 }else{
					 if(ZCleft(m-1) == -1)
							return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(ZCup(m-1) == -1)
							return -1;
					 Ustep();
					 ZCdistance = distance;
					 if(ZCup(m-1) == -1)
							return -1;
					 Rstep();
					 ZCdistance = distance;
					 if(ZCright(m-1) == -1)
							return -1;
				 }
				return 0;
			 }
			 
			//print each order of down U.S. on the JPanel
			 private int ZCdown(int m){
				 if(m==1){
					 if(ZCcheckMBR() == -1)
						 return -1;
					Dstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					half_right_up();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					half_right_up();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
					 Dstep();
					 ZCdistance = distance;
					 if(ZCcheckMBR() == -1)
						 return -1;
				 }
				 else if(m==2){
					 if(ZCright(m-1) == -1)
							return -1;
					 right_up();
					 ZCdistance = distance;
					 if(ZCright(m-1) == -1)
							return -1;
					 Dstep();
					 ZCdistance = distance;
					 if(ZCleft(m-1) == -1)
							return -1;
					 left_up();
					 ZCdistance = distance;
					 if(ZCleft(m-1) == -1)
							return -1;
				 }else{
					 if(ZCright(m-1) == -1)
							return -1;
					 Rstep();
					 ZCdistance = distance;
					 if(ZCdown(m-1) == -1)
							return -1;
					 Dstep();
					 ZCdistance = distance;
					 if(ZCdown(m-1) == -1)
							return -1;
					 Lstep();
					 ZCdistance = distance;
					 if(ZCleft(m-1) == -1)
							return -1;
				 }
				return 0;
			 }
			//////////////////////////////////////////////////////////
			//Z Curve END
			/////////////////////////////////////////////////////////
			
		private void average(){
			int i =0;
			float ZCtmp = 0, ZStmp = 0, Htmp = 0;
			float ZCtmpkey = 0, ZStmpkey = 0,Htmpkey = 0;
			while(i<1000){//原本是一千
				System.out.println("round  " + i);
				X = (int)(Math.random()*32);
				Y = (int)(Math.random()*32);
				 ZCdetermine();
				 ZCtmp += ZCdistance;
				 ZCtmpkey += key_count;
				 flush();
				 ZSdetermine();
				 ZStmp += ZSdistance;
				 ZStmpkey += key_count;
				 flush();
				 Hdetermine();
				 Htmp += Hdistance;
				 Htmpkey += key_count;
				 flush();
				 i++;
			}
			System.out.println("--------------------------------------------");
			System.out.println("Average Length ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(ZStmp/1000);
			System.out.print("Z curve: ");
			System.out.println(ZCtmp/1000);
			System.out.print("Hilbert: ");
			System.out.println(Htmp/1000);
			System.out.println("--------------------------------------------");
			System.out.println("Average Key ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(ZStmpkey/1000);
			System.out.print("Z curve: ");
			System.out.println(ZCtmpkey/1000);
			System.out.print("Hilbert: ");
			System.out.println((Htmpkey)/1000);
		}
}
