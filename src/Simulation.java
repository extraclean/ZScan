import java.util.Random;

public class Simulation {
	public double Prr(){//receive rate
		int f = 20;
		int M = 2;
		return Math.pow((1-Pbe()),(8*f*M));
	}
	
	public double Pbe(){//bit error	
		double Bn = 30000;
		double R = 19200;
		return 1/2*Math.exp(-(SNR()*Bn)/(2*R));
	}
	
	public double SNR(){//signal to noise rate	
		return Prec()-Pn();
	}
	
	public double Pn(){//noise floor
		return -105;	
	}
	
	public double Prec(){//reception power
		double Pt;
		Pt = Math.random()*30-20;//-20<Pt<10
		return Pt-PL();
	}
	
	public double PL(){//power loss (-RSSI) simulated RSSI
		Random r = new Random();
		return 55+10*3.3*Math.log(distance())+r.nextGaussian()*2+0;//標準差2，平均值0
	}
	
	public double R2D(){//RSSI to distance	(estimated distance)
		return Math.pow(10, (PL()-55)/33);
	}
	
	public double distance(){//the distance of truth table
		return 0;
	}
}
