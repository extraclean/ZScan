import java.util.ArrayList;

//Controls all the ZScan curve logic
public class ZScanController extends Thread {
	 ArrayList<ArrayList<ColorOfSquare>> Squares= new ArrayList<ArrayList<ColorOfSquare>>();
	 Tuple currentPosition;
	 long speed = 100;
	 //public static int order ;
	public static Integer X;
	public static Integer Y;
	public static float distance = 0;
	public static float ZCdistance = 0;
	public static float ZSdistance = 0;
	public static Integer MBR = 0;//to calculate when to stop
	public static Integer Hkey = 1;//to calculate when to stop
	public static Integer ZCkey = 0;//to calculate when to stop
	public static Integer count = 0;//to eliminate redundant key
	public static Integer bound = 0;//the number that each two column or two row maximal redundant key
	
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
	 }
	 
	 //run
	 public void run() {//determine which algorithm & function to run
//		 ZCdetermine();
		 ZSdetermine();
//		 Hdetermine();
		// showLength();
//		 showKey();
		// average();
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
		 for(Tuple t : positions){
			 int y = t.getX();
			 int x = t.getY();
			 Squares.get(x).get(y).lightMeUp(0); //0: red
		 }
		 for(Path t : trajectory){
			 int y = t.getX();
			 int x = t.getY();
			 Squares.get(x).get(y).lightMeUp(3); // 3: black
		 }
		 for(Redundant t : Rkey){
			 int y = t.getX();
			 int x = t.getY();
			 Squares.get(x).get(y).lightMeUp(2); // 2: green
		 }
		 pauser();
		 }
	 
		//////////////////////////////////////////////////////////
		//Z SCAN START
		/////////////////////////////////////////////////////////
	 private void ZSdetermine(){//TODO change line or run special case
		 ZSdistance = 0;
		 currentPosition.x = 0;
		 currentPosition.y = 0;
		 distance = 0;
		 // if x == odd
		 /*if(x%2 ==1){
		  *   if(y%2 ==1)
		  *   {
		  *   
		  *   
		  *   
		  *   }
		  * else{
		  * 
		  * 
		  * }
		  * 
		  * 
		  * 
		  * }
		  * 
		  * 
		  * 
		  * */
		 
		 if(X%2==1 && Y%2==1){
			 if(X>Y){
				 bound = (int)(((X-3)/2)*4);
				 right(X, Y);
			 }
			 else{
				 bound = (int)(((Y-3)/2)*4);
				 down(X, Y);
			 }
		 }else  if(X%2==1 ){
			 bound = (int)(((Y-2)/2)*4);
			 down(X, Y);
		 }
		 else if(Y%2==1){
			 bound = (int)(((X-2)/2)*4);
			 right(X, Y);
		 }
		 else if(X>Y){
			 bound = (int)(((X-2)/2)*4);
			 right(X,Y);
		 }
		 else{
			 bound = (int)(((Y-2)/2)*4);
			 down(X, Y);
		 }
	 	}
	 
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
		 
		 distance+=Math.sqrt(2);
	 }
	 
	 private void half_right_up(){
		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
			 lightup();
		 }else{
			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
		 }

			 count++;
		 distance+=Math.sqrt(2)/2;
	 }
	 
	 private void half_left_up(){
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
			 lightup();
		 }else{
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
		 }

			 count++;
		 
		 distance+=Math.sqrt(2)/2;
	 }
	 
	 private void half_left_down() {
		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
		 lightup();
		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
			 lightup();
		 }else{
			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
			 lightup();
		 }

			 count++;
		 
		 distance+=Math.sqrt(2)/2;
		}

		private void half_right_down() {
			currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
			 trajectory.add(new Path(currentPosition.x,currentPosition.y));
			 lightup();
			 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
				 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
				 lightup();
			 }else{
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
			 }

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
			 if(currentPosition.getX() <= (X-1)*2 && currentPosition.getY() <= (Y-1)*2)
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
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(down(m-1) == -1)
						 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(right(m-1) == -1)
				 return -1;
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(right(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
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
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(up(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(left(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(left(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
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
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(left(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(up(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(up(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
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
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(checkMBR() == -1)
					 return -1;
			 }
			 else{
				 if(right(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(down(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 if(down(m-1) == -1)
					 return -1;
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
				 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
				 lightup();
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
			
		private float LMAT(){
			//TODO
			return Math.min(0, 0);
		}
		
//		private float ZCurve(){
//			return (float) (Math.ceil(Math.pow(4, Math.ceil(Math.sqrt(Math.max(X, Y)))) * 5/8) - 1 + 
//					Math.floor(Math.pow(4, Math.ceil(Math.sqrt(Math.max(X, Y)))) * 3 / 8) * Math.sqrt(2));
//		}
		
//		private Integer  Hilbert(){
//			return (Integer) X*Y-1;
//		}
//		
		private Integer Zskey(){
			if(X%2==1 && Y%2 == 1){
				if(X>Y)
					return ((Y-1)*2+1+((Y-1)*3+2)*(X-1)/2) - ((Y-3)/2 * (X-1)/2);//last line + common line - redundant key
				else
					return (X-1)*2+1+((X-1)*3+2)*(Y-1)/2 - ((X-3)/2 * (Y-1)/2);
			}else if(X%2==1 ){	
				return ((X-1)*3+2)*Y/2 - ((X-3)/2 * Y/2);
			}else if(Y%2 == 1){
				return ((Y-1)*3+2)*X/2 - ((Y-3)/2 * X/2);
			}else{
				return ((Y-1)*3+2)*X/2 - ((Y-2)/2 * X/2);
			}
		}
		
		private void redundantKey(){//TODO
			
		}
		
//		private Integer Zckey(){
//			if(X  <= 2 &&  Y <= 2)
//				return 5;
//				else
//					return (int) (Math.pow( Math.pow(2 , (int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2))), 2) / 4 * 5);
//		}
//		
//		private Integer Hkey(){
//			 
//			if(X  <= 2 &&  Y <= 2)
//				return 4;
//				else
//					return (int) (Math.pow( Math.pow(2 ,  (int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2))), 2));
//		}
		
		private void showLength(){
			System.out.print("X: " + X + "  ");
			System.out.println("Y: " + Y);
			System.out.println("--------------------------------------------");
			System.out.println("Length ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(ZSdistance);
			System.out.print("Z curve: ");
			System.out.println(ZCdistance);
			System.out.print("Hilbert: ");
			System.out.println(Hkey-1);
//			System.out.print("Scan: ");
//			System.out.println(X*Y - 1);
//			//TODO LMAT
//			System.out.print("LMAT: ");
//			System.out.println(LMAT());
			System.out.println("--------------------------------------------");
		}
		
		private void showKey(){		
			System.out.println("Key ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(Zskey());
			System.out.print("Z curve: ");
			System.out.println(ZCkey);
			System.out.print("Hilbert: ");
			System.out.println(Hkey);
//			System.out.print("Scan: ");
//			System.out.println(X*Y );
////			//TODO LMAT
//			System.out.print("LMAT: ");
//			System.out.println(LMAT());
			System.out.println("+++++++++++++++++++++++++");
		}
		
		private void average(){
			int i =0;
			float ZCtmp = 0, ZStmp = 0, Htmp = 0;
			float ZCtmpkey = 0, ZStmpkey = 0;
			while(i<1000){
				System.out.println("round  " + i);
				X = (int)(Math.random()*100+1);
				Y = (int)(Math.random()*100+1);
				 ZCdetermine();
				 ZCtmp += ZCdistance;
				 ZCtmpkey += ZCkey;
				 ZSdetermine();
				 ZStmp += ZSdistance;
				 ZStmpkey += Zskey();
				 Hdetermine();
				 Htmp += Hkey-1;
				 i++;
			}
			System.out.println("--------------------------------------------");
			System.out.println("Average Length ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(ZStmp/100);
			System.out.print("Z curve: ");
			System.out.println(ZCtmp/100);
			System.out.print("Hilbert: ");
			System.out.println(Htmp/100);
			System.out.println("--------------------------------------------");
			System.out.println("Average Key ");
			System.out.println("--------------------------------------------");
			System.out.print("Z scan: ");
			System.out.println(ZStmpkey/100);
			System.out.print("Z curve: ");
			System.out.println(ZCtmpkey/100);
			System.out.print("Hilbert: ");
			System.out.println((Htmp+1)/100);
		}
		
//		private void averageKey(){
//			int i =0;
//			float ZCtmpkey = 0, ZStmpkey = 0, Htmpkey = 0;
//			while(i<100){
//				X = (int)(Math.random()*32+1);
//				Y = (int)(Math.random()*32+1);
//				 ZCdetermine();
//				 ZCtmpkey += ZCkey;
//				 ZSdetermine();
//				 ZStmpkey += Zskey();
//				 Hdetermine();
//				 Htmpkey += Hkey;
//				 i++;
//			}
//			System.out.println("--------------------------------------------");
//			System.out.println("Average Key ");
//			System.out.println("--------------------------------------------");
//			System.out.print("Z scan: ");
//			System.out.println(ZStmpkey/100);
//			System.out.print("Z curve: ");
//			System.out.println(ZCtmpkey/100);
//			System.out.print("Hilbert: ");
//			System.out.println(Htmpkey/100);
//		}
}
