import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class Localization {

	static void collinearity(int i){
		if(ZScanController.sensor[i].getXe() != 0){
			double result0 = (double)(ZScanController.sensor[i].queue[0].getY()-ZScanController.sensor[i].queue[1].getY())/(ZScanController.sensor[i].queue[0].getX()-ZScanController.sensor[i].queue[1].getX());
			double result1 = (double)(ZScanController.sensor[i].queue[2].getY()-ZScanController.sensor[i].queue[1].getY())/(ZScanController.sensor[i].queue[2].getX()-ZScanController.sensor[i].queue[1].getX());
			if(result0 == result1)
				ZScanController.collinear++;
			}
	}
	
	public static void WCL(int i){
		double tmp1 = 0,tmp2 = 0,tmp3=0;
		for(int n=0;n<3;n++){
			tmp1 += Simulation.PL(Math.sqrt(Math.pow(ZScanController.sensor[i].queue[n].getX()-ZScanController.sensor[i].getX(),2)+Math.pow(ZScanController.sensor[i].queue[n].getY()-ZScanController.sensor[i].getY(), 2)))*ZScanController.sensor[i].queue[n].getX();
			tmp2 += Simulation.PL(Math.sqrt(Math.pow(ZScanController.sensor[i].queue[n].getX()-ZScanController.sensor[i].getX(),2)+Math.pow(ZScanController.sensor[i].queue[n].getY()-ZScanController.sensor[i].getY(), 2)))*ZScanController.sensor[i].queue[n].getY();
			tmp3 += Simulation.PL(Math.sqrt(Math.pow(ZScanController.sensor[i].queue[n].getX()-ZScanController.sensor[i].getX(),2)+Math.pow(ZScanController.sensor[i].queue[n].getY()-ZScanController.sensor[i].getY(), 2)));
		}
		ZScanController.sensor[i].ChangeEstimate((double)tmp1/tmp3, (double)tmp2/tmp3);
	}
	
	public static void APT(int i){
		for(int n=0;n<ZScanController.sNumber;n++){
			for(int m=0;m<100;m++){
				if(ZScanController.sensor[n].queue[m].x == -1 || ZScanController.sensor[n].queue[m].y == -1){
					ZScanController.sensor[n].queue[m].changeDistance(ZScanController.Rc*2);
				}else
				ZScanController.sensor[n].queue[m].changeDistance(Math.sqrt(Math.pow(ZScanController.sensor[n].queue[m].x-ZScanController.sensor[n].x,2)+Math.pow(ZScanController.sensor[n].queue[m].y-ZScanController.sensor[n].y,2)));
			}
		}
		Arrays.sort(ZScanController.sensor[i].queue);
		TPT(i);
	}
	
	public static void TPT(int i) {//TPT
		double x1,x2,x3,y1,y2,y3;
		//TODO 三次兩圓交點
		transection(i,ZScanController.sensor[i].queue[0],ZScanController.sensor[i].queue[1]);
		transection(i,ZScanController.sensor[i].queue[1],ZScanController.sensor[i].queue[2]);
		transection(i,ZScanController.sensor[i].queue[2],ZScanController.sensor[i].queue[0]);
		
		//TODO 找三內側點（圓一與圓二的兩交點，以靠近圓三圓心者為內側點）
		if(Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[0].getX1(),ZScanController.sensor[i].queue[2].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[0].getY1(),ZScanController.sensor[i].queue[2].getY()), 2))
				<=Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[0].getX2(),ZScanController.sensor[i].queue[2].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[0].getY2(),ZScanController.sensor[i].queue[2].getY()), 2))){
			x1 = ZScanController.sensor[i].queue[0].getX1();
			y1 = ZScanController.sensor[i].queue[0].getY1();
		}else{
			x1 = ZScanController.sensor[i].queue[0].getX2();
			y1 = ZScanController.sensor[i].queue[0].getY2();
		}
		
		if(Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[1].getX1(),ZScanController.sensor[i].queue[0].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[1].getY1(),ZScanController.sensor[i].queue[0].getY()), 2))
				<=Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[1].getX2(),ZScanController.sensor[i].queue[0].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[1].getY2(),ZScanController.sensor[i].queue[0].getY()), 2))){
			x2 = ZScanController.sensor[i].queue[1].getX1();
			y2 = ZScanController.sensor[i].queue[1].getY1();
		}else{
			x2 = ZScanController.sensor[i].queue[1].getX2();
		    y2 = ZScanController.sensor[i].queue[1].getY2();
		}
		
		if(Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[2].getX1(),ZScanController.sensor[i].queue[1].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[2].getY1(),ZScanController.sensor[i].queue[1].getY()), 2))
				<=Math.sqrt(Math.pow(Arith.sub(ZScanController.sensor[i].queue[2].getX2(),ZScanController.sensor[i].queue[1].getX()), 2)+Math.pow(Arith.sub(ZScanController.sensor[i].queue[2].getY2(),ZScanController.sensor[i].queue[1].getY()), 2))){
			x3 = ZScanController.sensor[i].queue[2].getX1();
			y3 = ZScanController.sensor[i].queue[2].getY1();
		}else{
			x3 = ZScanController.sensor[i].queue[2].getX2();
		    y3 = ZScanController.sensor[i].queue[2].getY2();
		}
		
		//TODO 三內側點之質心極為定位座標
		//ZScanController.sensor[i].ChangeEstimate(Math.round(((double)(x1+x2+x3)/3)*1000)/1000, Math.round(((double)(y1+y2+y3)/3)*1000)/1000);
		ZScanController.sensor[i].ChangeEstimate(Arith.div((double)(Arith.add(x1, Arith.add(x2, x3))),3,4), Arith.div((double)Arith.add(y1, Arith.add(y2, y3)),3,4));
//		System.out.print("true:");
//		System.out.print(ZScanController.sensor[i].getX()+",");
//		System.out.println(ZScanController.sensor[i].getY());
//		System.out.print("estimate:");
//		System.out.print(ZScanController.sensor[i].getXe()+",");
//		System.out.println(ZScanController.sensor[i].getYe());
		
	}

	private static void transection(int i,MB mb, MB mb2){
		double x1,x2,y1,y2;//此為兩圓相交的坐標 
		
	    if(mb.y!=mb2.y){//兩圓圓心Y值不同時 
	    	//m: y=mx+k的x項系數、k: y=mx+k的k項常數、 a、b、c: x=(-b±√(b^2-4ac))/2a的係數 

	        double m=Arith.div((Arith.sub(mb.x,mb2.x)),(Arith.sub(mb2.y,mb.y)));
	        double k=(Arith.sub(Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb)),2),Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb2)),2))+Math.pow(mb2.x,2)-Math.pow(mb.x,2)+Math.pow(mb2.y,2)-Math.pow(mb.y,2))/(2*(mb2.y-mb.y));
	        double a=Arith.add(1,Math.pow(m,2)),b=Arith.mul(2,(Arith.mul(k,m)-mb2.x-Arith.mul(m,mb2.y))),c=Arith.sub(Arith.sub(Arith.add(Arith.add(Math.pow(mb2.x,2),Math.pow(mb2.y,2)),Math.pow(k,2)),Arith.mul(Arith.mul(2,k),mb2.y)),Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb2)),2));

	        if(b*b-4*a*c>=0){//有交點時 
	            x1=((-b)+Math.sqrt(b*b-4*a*c))/(2*a);//x=(-b+√(b^2-4ac))/2a 
	            y1=m*x1+k;//y=mx+k
	            x2=((-b)-Math.sqrt(b*b-4*a*c))/(2*a);//x=(-b-√(b^2-4ac))/2a
	            y2=m*x2+k;//y=mx+k
	            if(b*b-4*a*c>0){//兩交點 
	            	mb.ChangeTransection1(x1, y1);
	            	mb.ChangeTransection2(x2, y2);
	            }
	            else{//一交點 
	            	mb.ChangeTransection1(x1, y1);
	            	mb.ChangeTransection2(x1, y1);
	            }
	        }
//	        else//沒有交點時 
//	            System.out.println("No cross points.\n");//
	    }

	    else if((mb.y==mb2.y)){//兩圓圓心Y值相同時
	    	//x1= 兩交點的x值、 a、b、c= x=(-b±√(b^2-4ac))/2a的係數
	    	x1=-(Math.pow(mb.x,2)-Math.pow(mb2.x,2)-Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb)),2)+Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb2)),2))/(2*mb2.x-2*mb.x);
	    	x2=x1;
	        double a=1,b=-2*mb.y,c=Math.pow(x1,2)+Math.pow(mb.x,2)-2*mb.x*x1+Math.pow(mb.y,2)-Math.pow(Simulation.R2D(Simulation.distance(ZScanController.sensor[i],mb)),2);

	        if(b*b-4*a*c>=0){
	            y1=((-b)+Math.sqrt(b*b-4*a*c))/(2*a);//y=(-b+√(b^2-4ac))/2a
	            y2=((-b)-Math.sqrt(b*b-4*a*c))/(2*a);//y=(-b-√(b^2-4ac))/2a
	            if(b*b-4*a*c>0){//兩交點 
	            	mb.ChangeTransection1(x1, y1);
	            	mb.ChangeTransection2(x2, y2);
	            }
	            else{//一交點 
	            	mb.ChangeTransection1(x1, y1);
	            	mb.ChangeTransection2(x1, y1);
	            }
	        }
//	        else//沒有交點時
//	            System.out.println("No cross points.\n");
	    }
	}
}