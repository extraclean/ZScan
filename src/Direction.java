//
//public class Direction {
//	private void sigma(){
//		 Lstep();
//		 half_right_down();
//		 half_left_down();
//		 Rstep();
//	 }
//	 
//	 private void re_sigma(){
//		 Lstep();
//		 half_right_up();
//		 half_left_up();
//		 Rstep();
//	 }
//	 
//	 private void Z(){
//		 Rstep();
//		 half_left_down();
//		 half_left_down();
//		 Rstep();
//	 }
//	 
//	 private void re_Z(){
//		 Rstep();
//		 half_left_up();
//		 half_left_up();
//		 Rstep();
//	 }
//	 
//	 private void half_Z(){
//		 half_left_down();
//		 Rstep();
//	 }
//	 
//	 private void re_half_Z(){
//		 half_left_up();
//		 Rstep();
//	 }
//	 
//	 private void v(){
//		 half_left_down();
//		 half_right_down();
//	 }
//	 
//	 private void re_v(){
//		 half_left_up();
//		 half_right_up();
//	 }
//	 
//	 private void w(){
//		 half_left_down();
//		 half_right_down();
//		 half_left_down();
//		 half_right_down();
//	 }
//	 
//	 private void re_w(){
//		 half_left_up();
//		 half_right_up();
//		 half_left_up();
//		 half_right_up();
//	 }
//	 
//	 //////////////////////
//	 //Z Scan End
//	 /////////////////////
//	 private void right(int X, int Y){
//		 if(Y%2==1 && ZScanController.currentPosition.y ==  (Y-1)*4 && ZScanController.currentPosition.x == (X-2)*4 ) {//X is odd (end point)
//			 half_right_down();
//			 half_right_up();			 
//			 ZSdistance = distance;
//		 }else if(Y%2==1 && ZScanController.currentPosition.y ==  (Y-1)*4) {//X is odd (last line begin)
//			 half_right_down();
//			 half_right_up();
//			 
//			 right(X,Y);
//			 ZSdistance = distance;
//		 }else  if(currentPosition.x == (X-2)*4 && currentPosition.y ==  (Y-2)*4 ){//lower-right is the end
//			Dstep(); 
//			 half_right_up(); 
//			 half_right_up();
//			Dstep();
//			 ZSdistance = distance;
//		 }else  if(currentPosition.x == (X-2)*4 ){//meet the right bound
//			Dstep();
//			 half_right_up();
//			 half_right_up();
//			Dstep();
//			Dstep();
//			count = 0;//change line count reset
//			 
//			 left(X,Y);
//			 ZSdistance = distance;
//		 }
//		 else{//keep going upward
//			Dstep();
//			 half_right_up();
//			 half_right_up();
//			 
//			 right(X,Y);
//			 ZSdistance = distance;
//		 }
//	 }
//
//	private void left(int X, int Y){
//		 if(Y%2==1 && currentPosition.y == (Y-1)*4 && currentPosition.x == 4 ) {//X is odd (end point)
//			 half_left_down();
//			 half_left_up();			
//			 ZSdistance = distance; 
//		 }else if(Y%2==1 && currentPosition.y == (Y-1)*4 ) {//X is odd (last line begin)
//			 half_left_down();
//			 half_left_up();
//			 
//			 left(X,Y);
//			 ZSdistance = distance;
//		 }else  if(currentPosition.y ==  (Y-2)*4 && currentPosition.x == 4){//upper-right is the end
//			Dstep(); 
//			 half_left_up(); 
//			 half_left_up();
//			Dstep();
//			 ZSdistance = distance;
//		 }else  if(currentPosition.x == 4){//meet the upper bound
//			Dstep();
//			 half_left_up();
//			 half_left_up();
//			Dstep();
//			Dstep();
//			count = 0;//change line count reset
//			 
//			 right(X,Y);
//			 ZSdistance = distance;
//		 }
//		 else{//keep going upward
//			Dstep();
//			 half_left_up();
//			 half_left_up();
//			 
//			 left(X,Y);
//			 ZSdistance = distance;
//		 }
//	 }
//	 
//		//print each order of up U.S. on the JPanel
//	 private void up(int X, int Y){
//		 if(X%2==1 && currentPosition.x == (X-1)*4 && currentPosition.y == 4 ) {//X is odd (end point)
//			 half_right_up();
//			 half_left_up();
//			 ZSdistance = distance;
//		 }else if(X%2==1 && currentPosition.x == (X-1)*4 ) {//X is odd (last line begin)
//			 half_right_up();
//			 half_left_up();
//			 
//			 up(X,Y);
//			 ZSdistance = distance;
//		 }else  if(currentPosition.y == 4 && currentPosition.x == (X-2)*4 ){//upper-right is the end
//			Rstep(); 
//			half_left_up();
//			half_left_up();
//			Rstep();
//			 ZSdistance = distance;
//		 }else  if(currentPosition.y == 4){//meet the upper bound
//			Rstep();
//			 half_left_up();
//			 half_left_up();
//			Rstep();
//			Rstep();
//			count = 0;//change line count reset
//			 
//			 down(X,Y);
//			 ZSdistance = distance;
//		 }
//		 else{//keep going upward
//			Rstep();
//			 half_left_up();
//			 half_left_up();
//			 
//			 up(X,Y);
//			 ZSdistance = distance;
//		 }
//	 }
//	 
//	//print each order of down U.S. on the JPanel
//	 private void down(int X, int Y){ 
//		 if(X%2==1 && currentPosition.x == (X-1)*4 && currentPosition.y == (Y-2)*4 ) {//X is odd (end point)
//			half_right_down();
//			half_left_down();
//			 ZSdistance = distance;
//		 }else if(X%2==1 && currentPosition.x == (X-1)*4  ) {//X is odd (last line begin)
//			 half_right_down();
//			 half_left_down();
//			 
//			 down(X,Y);
//			 ZSdistance = distance;
//		 }else  if(currentPosition.y == (Y-2)*4 && currentPosition.x == (X-2)*4 ){//lower-right is the end
//		Rstep();
//		 half_left_down();
//		 half_left_down();
//		Rstep();
//		 ZSdistance = distance;
//	 }else if(currentPosition.y == (Y-2)*4){//meet the lower bound
//		Rstep();
//		 half_left_down();
//		 half_left_down();
//		Rstep();			 
//		Rstep();
//		count = 0;//change line count reset
//
//		 up(X,Y);
//		 ZSdistance = distance;
//		 }
//		 else{//keep going down
//			Rstep();
//			 half_left_down();
//			 half_left_down();
//	
//			 down(X,Y);
//			 ZSdistance = distance;
//		 }
//	 }
//	 private void Rstep(){
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance++;
//	 }
//
//	 private void left_up(){
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance+=Math.sqrt(2);
//	 }
//	 
//	 private void left_down(){
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance+=Math.sqrt(2);
//	 }
//	 
//	 private void Dstep(){
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y+1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance++;
//	 }
//	 
//	 private void Ustep(){
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x,currentPosition.y-1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance++;
//	 }
//	 
//	 private void Lstep(){
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance++;
//	 }
//	 
//	 private void right_up(){
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance+=Math.sqrt(2);
//	 }
//
//	 private void right_down(){
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//		 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//		 lightup();
//		 
//		 distance+=Math.sqrt(2);
//	 }
//	 
//	 private void half_right_up(){
//		 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
////		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
////			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
////			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
////			 lightup();
////		 }else{
//			 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y-1);
//			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//			 lightup();
////		 }
//
//			 count++;
//		 distance+=Math.sqrt(2)/2;
//	 }
//	 
//	 private void half_left_up(){
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
////		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
////			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
////			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
////			 lightup();
////		 }else{
//			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y-1);
//			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//			 lightup();
////		 }
//
//			 count++;
//		 
//		 distance+=Math.sqrt(2)/2;
//	 }
//	 
//	 private void half_left_down() {
//		 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//		 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//		 lightup();
////		 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
////			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
////			 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
////			 lightup();
////		 }else{
//			 currentPosition.ChangeData(currentPosition.x-1,currentPosition.y+1);
//			 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//			 lightup();
////		 }
//
//			 count++;
//		 
//		 distance+=Math.sqrt(2)/2;
//		}
//
//		private void half_right_down() {
//			currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//			 trajectory.add(new Path(currentPosition.x,currentPosition.y));
//			 lightup();
////			 if((count-2)%4 == 0 && (count -2)< bound){//if key need to be eliminate & not at the last line
////				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
////				 Rkey.add(new Redundant(currentPosition.x,currentPosition.y));
////				 lightup();
////			 }else{
//				 currentPosition.ChangeData(currentPosition.x+1,currentPosition.y+1);
//				 positions.add(new Tuple(currentPosition.x,currentPosition.y));
//				 lightup();
////			 }
//
//				 count++;
//			 
//			 distance+=Math.sqrt(2)/2;
//	}
//		//////////////////////////////////////////////////////////
//		//Z SCAN END
//		/////////////////////////////////////////////////////////
//
//}
