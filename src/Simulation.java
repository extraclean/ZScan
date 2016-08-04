import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Simulation {//Prr
	
//	public static double Prr(int d){//receive rate
//		int f = 20;
//		int M = 2;
//		int i =0;
//		double tmp1= 1,tmp2=1;
//		//System.out.println(d);
//		//System.out.println(R2D(d));
//		//System.out.println(Math.pow((1-Pbe(d)),(8*f*M)));
////		for(i=0;i<8*f*M;i++){
////			tmp1 = Math.floor( Pbe(d) * 100000 );
////			tmp1 = tmp1 / 100000 ;
////			tmp2 *= (1-tmp1);
////			tmp2 = Math.floor( tmp2 * 100000 );
////			tmp2 = tmp2 / 100000 ;
////		}
//		//tmp = Math.floor( tmp * 100000000 );
//		//tmp = tmp / 100000000 ;
//		//System.out.println(tmp2);
//		//System.out.println(Math.pow((1-Pbe(d)),(8*f*M)));
//		System.out.println(Pbe(d));
//		return Math.pow((1-Pbe(d)),(8*f*M));
//	}
//	
//	public static double Pbe(int d){//bit error	
//		double Bn = 30;
//		double R = 19.2;
//		double tmp = 1;
////		System.out.println(SNR(d));
////		System.out.println(-(SNR(d)*Bn)/(2*R)/2);
//		tmp = Math.floor(Math.exp(-(SNR(d)/2)*(Bn/R)));
////		tmp = tmp/10000;
//		return tmp/2;
//	}
//	
//	public static double SNR(int d){//signal to noise rate	
//		double tmp;
//		//System.out.println(Prec(d));
//		tmp = Math.floor((Prec(d)-Pn())*10000);
//		tmp = tmp/10000;
//		return tmp;
//	}
//	
//	public static double Pn(){//noise floor
//		return -105;	
//	}
//	
//	public static double Prec(int d){//reception power
//		double Pt;
//		Pt = Math.random()*30-20;//-20<Pt<10
//		//System.out.println(PL(d));
//		return Pt-PL(d);
//	}
	
	public static double Rc(double rc){//高斯分佈Rc
		Random n = new Random();
//		return rc;
		double tmp;
		if((tmp = n.nextGaussian()*(0.5*rc)) > 0)
			tmp = tmp * (-1);
		return (rc+tmp+0);//標準差50%Rc,mean=0
	}
	
	public static double PL(double d){//power loss (-RSSI) simulated RSSI
		Random r = new Random();
		return 55+10*3.3*(Math.log(d)/Math.log(10))+r.nextGaussian()*0.02*d+0;//標準差0.05d，平均值0
	}

	public static double R2D(double d){//RSSI to distance	(estimated distance)
//		System.out.println(Math.pow(10, ((double)(PL(d)-55)/33)));
//		System.out.println(d);
		return Math.pow(10, ((double)(PL(d)-55)/33));
		//return d;
	}
	
	public static double distance(Sensor n, MB k) {
		return Math.sqrt(Math.pow((k.getX()-n.getX()), 2)+Math.pow(k.getY()-n.getY(), 2));
	}	
	
	public static double step(){//the distance of truth table
		//System.out.println(d);
		
//		double s = 2*ZScanController.Rc;//out of range
		
//		double s = 1.5*ZScanController.Rc;
		
//		double s = 1*ZScanController.Rc;
//		double s = (double)(2)/3*ZScanController.Rc;
//		double s = 0.6324555*ZScanController.Rc;//sqrt(2/5) ~= 0.632455, Rc = 10(m)
		double s = ZScanController.RD*ZScanController.Rc;//sqrt(2/5) ~= 0.632455, Rc = 10(m)
//		double s = 0.5*ZScanController.Rc;
		
//		double s = (double)(2)/5*ZScanController.Rc;
//		double s = (double)(1)/3*ZScanController.Rc;
		
		return s;
	}
	
	public static void deployment(double X,double Y,int N) throws IOException{
		 FileWriter fw = new FileWriter("sensors.txt");
		 for(int i =0 ; i<N ; i++){
		 			fw.write((int) (Math.random()*X)+",");
		 	        fw.write((int) (Math.random()*Y)+"\r\n");
		 }
		 	        fw.flush();
		 	        fw.close();
	}
	
	public static void readSensor() throws IOException{
		String read;
		int i=0;
		FileReader fr = new FileReader("sensors.txt");
		BufferedReader br = new BufferedReader(fr);
		while ((read=br.readLine())!=null) {
			String[] tmp = read.split(",");
			ZScanController.sensor[i].ChangeData(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
			i++;
		}
		fr.close();
	}
	
	public static double errorRate(){//定位誤差
		double result=0;
		double tmp = 0;
		for(int i=0;i<ZScanController.sNumber;i++){
			//if(!Double.isNaN(Math.sqrt(Math.pow(ZScanController.sensor[i].getXe()-ZScanController.sensor[i].getX(), 2)+Math.pow(ZScanController.sensor[i].getYe()-ZScanController.sensor[i].getY(), 2)))){
				if(ZScanController.sensor[i].getXe() != -1){
				tmp =  (double)Math.sqrt(Math.pow(ZScanController.sensor[i].getXe()-ZScanController.sensor[i].getX(), 2)+Math.pow(ZScanController.sensor[i].getYe()-ZScanController.sensor[i].getY(), 2))/ZScanController.Rc;
				//if(!Double.isNaN(tmp))
					result+=tmp;
				//System.out.println(result);
			}
		}
		//System.out.print("localization error rate: ");
		return (result/ZScanController.sNumber);//in unit of Rc
	}
	
	public static double coverage(){//定位覆蓋率
		double result = 0;
		int count = 0;
		for(int i=0;i<ZScanController.sNumber;i++){
			if(ZScanController.sensor[i].getXe()!=-1&&ZScanController.sensor[i].getYe()!=-1){
				count++;
			}else{
//				System.out.print(ZScanController.sensor[i].getXe()+",");
//				System.out.println(ZScanController.sensor[i].getYe());
			}
		}
		//System.out.print("non-localized: ");
		//System.out.println((ZScanController.sNumber-count));
		//System.out.print("coverage rate: ");
		//System.out.println(ZScanController.sNumber);
		result = (double)count/ZScanController.sNumber;
		
		return result;
	}
}
