//
//public class Zcurve {
//
////////////////////////////////////////////////////////////
////Z Curve START
///////////////////////////////////////////////////////////
//static void ZCdetermine(){
//double ZCdistance = 0;
//int ZCkey = 0;
//ZScanController.currentPosition.x = 0;
//ZScanController.currentPosition.y = 0;
//int MBR = 0;
//ZScanController.initialKey();
//if(X > Y)
//ZCdown((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
//else 
//ZCright((int) Math.ceil(Math.log(Math.max(X, Y))/Math.log(2)));
////System.out.println((int) Math.ceil(Math.log(Y)/Math.log(2)));
//
//}
//
//private int ZCcheckMBR(){//check whether every square is traveled
//ZCkey++;
//if(Math.log(X)/Math.log(2) -  (int)(Math.log(X)/Math.log(2)) == 0 && Math.log(Y)/Math.log(2)- (int)(Math.log(Y)/Math.log(2)) == 0 )
//return 0;
//if(currentPosition.getX() <= (X-1)*4+2 && currentPosition.getY() <= (Y-1)*4+2)
//MBR++;
//if(X%2==0&&Y%2==0){
//if(MBR >= (X/2*5*Y/2))
//return -1;
//}else if(X%2==0&&Y%2==1){
//if(MBR>=((Y-1)/2*X/2*5)+(X/2*3)-1)
//return -1;
//}else if(Y%2==0&&X%2==1){
//if(MBR>=((X-1)/2*Y/2*5)+(Y/2*3)-1)
//return -1;
//}else if(MBR>=((Y-1)/2*(X-1)/2*5)+((X-1)/2*3)+((Y-1)/2*3)+1)
//return -1;
//
////System.out.println(MBR);
//return 0;
//}
//
////print each order of right U.S. on the JPanel
//private int ZCright(int m){
//if(m==1){
//if(ZCcheckMBR() == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//}
//else if(m==2){
//if(ZCdown(m-1) == -1)
//return -1;
//left_down();
//ZCdistance = distance;
//if( ZCdown(m-1) == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if( ZCup(m-1) == -1)
//return -1;
//left_up();
//ZCdistance = distance;
//if(ZCup(m-1) == -1)
//return -1;
//}else{
//if(ZCdown(m-1) == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCright(m-1) == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCright(m-1) == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCup(m-1) == -1)
//return -1;
//}
//return 0;
//}
//
//private int reup(int m){//special case in level 2 left curve
//if(ZCcheckMBR() == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//
//return 0;
//}
//
//private int redown(int m){//special case in level 2 left curve
//if(ZCcheckMBR() == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//
//return 0;
//}
//
//private int releft(int m){//special case in level 2 UP curve
//if(ZCcheckMBR() == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//
//return 0;
//}
//
//private int reright(int m){//special case in level 2 UP curve
//if(ZCcheckMBR() == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_left_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//
//return 0;
//}
//
////print each order of left U.S. on the JPanel
//private int ZCleft(int m){
//if(m==1){
//if(ZCcheckMBR() == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//}
//else if(m==2){
//if(reup(m-1) == -1)
//return -1;
//right_up();
//ZCdistance = distance;
//if(reup(m-1) == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(redown(m-1) == -1)
//return -1;
//right_down();
//ZCdistance = distance;
//if(redown(m-1) == -1)
//return -1;
//}else{
//if(ZCup(m-1) == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCleft(m-1) == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCleft(m-1) == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCdown(m-1) == -1)
//return -1;
//}
//return 0;
//}
//
////print each order of up U.S. on the JPanel
//private int ZCup(int m){
//if(m==1){
//if(ZCcheckMBR() == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_down();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//}
//else if(m==2){
//if( releft(m-1) == -1)
//return -1;
//left_down();
//ZCdistance = distance;
//if(releft(m-1) == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(reright(m-1) == -1)
//return -1;
//right_down();
//ZCdistance = distance;
//if(reright(m-1) == -1)
//return -1;
//}else{
//if(ZCleft(m-1) == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCup(m-1) == -1)
//return -1;
//Ustep();
//ZCdistance = distance;
//if(ZCup(m-1) == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCright(m-1) == -1)
//return -1;
//}
//return 0;
//}
//
////print each order of down U.S. on the JPanel
//private int ZCdown(int m){
//if(m==1){
//if(ZCcheckMBR() == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//half_right_up();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCcheckMBR() == -1)
//return -1;
//}
//else if(m==2){
//if(ZCright(m-1) == -1)
//return -1;
//right_up();
//ZCdistance = distance;
//if(ZCright(m-1) == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCleft(m-1) == -1)
//return -1;
//left_up();
//ZCdistance = distance;
//if(ZCleft(m-1) == -1)
//return -1;
//}else{
//if(ZCright(m-1) == -1)
//return -1;
//Rstep();
//ZCdistance = distance;
//if(ZCdown(m-1) == -1)
//return -1;
//Dstep();
//ZCdistance = distance;
//if(ZCdown(m-1) == -1)
//return -1;
//Lstep();
//ZCdistance = distance;
//if(ZCleft(m-1) == -1)
//return -1;
//}
//return 0;
//}
////////////////////////////////////////////////////////////
////Z Curve END
///////////////////////////////////////////////////////////
//}
