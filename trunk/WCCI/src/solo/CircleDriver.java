/**
 * 
 */
package solo;

import com.graphbuilder.geom.Geom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver extends BaseStateDriver<NewCarState,CarControl> {

	double targetRadius = 	100;
	double maxSpeed = 0;
	double AllowTime = 60;
	/**
	 * 
	 */
	double x =0 ;
	double y = 0;
	public CircleDriver() {
		// TODO Auto-generated constructor stub
		ignoredExisting = true;
		storeNewState = false;
	}
	
	
	public double radiusAtSpeed(double speedkmh){
		if (speedkmh<=205)
			return 0.0820619978719424+0.00541730524763449*speedkmh+0.00464210551818003*speedkmh*speedkmh-1.20686439387299e-05*speedkmh*speedkmh*speedkmh; 
		if (speedkmh<274)
			return -1192.18464416069+16.6397488904336*speedkmh-0.0740555508240852*speedkmh*speedkmh+0.000114411488519385*speedkmh*speedkmh*speedkmh;
		if (speedkmh<285)
			return 125181.064069584-1325.79717496569*speedkmh+4.67785276024091*speedkmh*speedkmh-0.00549045392467163*speedkmh*speedkmh*speedkmh;			
			
		return -83508.7895089672+887.064242540164*speedkmh-3.14306813772447*speedkmh*speedkmh+0.00372277142586774*speedkmh*speedkmh*speedkmh;
	}
	
	public double speedAtRadius(double x){
		if (x>=170)
			return 85.5247084519036+2.01905340962707*x-0.00646416884272083*x*x+7.29293433335612e-06*x*x*x;
		if (x>=100)
			return -139.760581424782+6.04756169365199*x-0.0303044500612258*x*x+5.3961109358751e-05*x*x*x;
		if (x>=60)
			return 202.745158728743-4.48739317463141*x+0.0755898471248578*x*x-0.00029146122705404*x*x*x;
		if (x>=20)
			return 26.7657503764833+2.55982229567786*x-0.0171415320257696*x*x+0.000108671654261182*x*x*x;		
		return -0.00728601622351035+7.7247995418915*x+-0.360701254346654*x*x+0.00788103445639814*x*x*x;		
	}

	/**
	 * @param name
	 */
	public CircleDriver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	//point is relative to car
	public double steerToPoint(NewCarState state,Vector2D point){
		double angle = state.angle;
		if (point==null) return angle/SimpleDriver.steerLock;
		double posX = state.posX;
		double posY = state.posY;
		double cx = state.cx;
		double cy = state.cy;
		Vector2D a = new Vector2D(cx-posX,cy-posY);
		double steer = angle-Math.PI/2+a.angle(point);		
		return (steer)/SimpleDriver.steerLock;
	}
	
	
	public double steerAtRadius(NewCarState state,double targetRadius){
		double angle = state.angle;		
		double posX = state.posX;
		double posY = state.posY;
		double speed = state.getSpeed();
		double cx = state.cx;
		double cy = state.cy;
		double d = Geom.distance(posX, posY, cx, cy);
		Vector2D p = new Vector2D(cx-posX,cy-posY).orthogonal().normalised().rotated(angle);
		double closeDist = speed*0.04/3.6;
		double[] r = null;
		double rd = 0;
		
		Vector2D dir = new Vector2D(cx-posX,cy-posY);
		Vector2D v = dir.orthogonal().normalised().rotated(-angle);
		Vector2D o = new Vector2D(posX,posY);
		Vector2D point = null;
		if (d<targetRadius-5){					
			r = Geom.getLineCircleIntersection(posX, posY, posX+v.x, posY+v.y, cx, cy, targetRadius);
			if (r!=null) point = new Vector2D(r[0],r[1]);
			else point = new Vector2D(posX+v.x, posY+v.y);			
		} else if (d>targetRadius){			
			Vector2D[] points = Geom.ptTangentLine(posX, posY, cx, cy, targetRadius);
			point = points[0];						
		} else {			
			point = o.plus(new Vector2D(dir.orthogonal()));		
		}
		
		if (r!=null) rd = Geom.distance(r[0], r[1], posX, posY);		
		return  steerToPoint(state, point.minus(o));		
	}


	double oldSpeed = 0;
	double accel = 0;
	double avgSpeed = 0;
	double avgRadius = 0;
	int count =0 ;
	int n = 0;
	boolean ok = true;
	double mr=0;
	double startTime=10;
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#drive(solo.State)
	 */
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		double speed = state.state.getSpeed();
		double time = state.state.getLastLapTime();
		double posX = state.state.posX;
		double posY = state.state.posY;
		double cx = state.state.cx;
		double cy = state.state.cy;
		double d = Geom.distance(posX, posY, cx, cy);		
		int gear = state.state.getGear();
		double brake = 0.0d;		
		double acc = 1;
		
		double steer = steerAtRadius(state.state,targetRadius);
		
		if (accel!=0 || oldSpeed!=0) {
			accel = (speed-oldSpeed)/0.02;		
		}		
		int maxGear = 1;
		if (targetRadius>235)
			maxGear = 6;
		else if (targetRadius>85) 
			maxGear = 5;
		else if (targetRadius>69)
			maxGear = 4;
		else if (targetRadius>40)
			maxGear = 3;
		else if (targetRadius>10)
			maxGear = 2;		
		
		if (maxSpeed==0) maxSpeed = Math.max(speedAtRadius(targetRadius),maxSpeed);
		if (maxSpeed<=30) maxSpeed =30;
		if (accel>0 && accel<0.5 && gear<maxGear && speed<maxSpeed) gear++;
		if (gear==0) gear=1;
		oldSpeed = state.state.getSpeed();		
				
		if (time>=10+startTime){
			avgSpeed += speed;
			avgRadius += d;			
			n++;
			if (n>0){
				double r = avgRadius/n;
				if (time>=10+startTime && mr<d) mr = d;
			}
			if (n>100) {
				
				double sp = avgSpeed/n;
				double r = avgRadius/n;											
//				System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+gear);
				if (r<=targetRadius+0.75){
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+count);
					if (maxSpeed>=speed+0.5)
						count++;
					else if (maxSpeed<speed){
						maxSpeed=speed+0.5;						
//						count =0;						
					}
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+sp+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+ok);
										
					if (ok) maxSpeed++;		
					ok = true;
//					System.out.println(maxSpeed+"\t"+speed+"\t"+count);
				} else {
					if (!ok) 
						maxSpeed--; 
					else maxSpeed-=0.5;										
					if (ok) count++;					
					ok = false;
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+sp+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+ok);
				}					
				if (count>10){
					maxSpeed = sp-0.5;
					System.out.println(radiusAtSpeed(speedAtRadius(r))+"\t\t"+speedAtRadius(r)+"\t\t"+sp+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+(mr-r));
					targetRadius+=2.5;
					count = 0;
					ok = true;
					mr = 0;
					maxSpeed = Math.max(speedAtRadius(targetRadius),maxSpeed);					
					startTime = time;
				}
								
				n = 0;
				avgSpeed = 0;
				avgRadius = 0;
//				System.out.println(n+"   "+maxSpeed+"   "+sp+"   "+acc);			
			}			
		}		
		
//		else if (time>=0 && time % AllowTime<0.015){			
//			if (n>0) {
//				double sp = avgSpeed/n;
//				double r = avgRadius/n;
//				System.out.println(maxSpeed+"\t"+radiusAtSpeed(sp)+"\t"+sp+"\t"+r+"\t"+targetRadius);				
//			}
//			n = 0;
//			avgSpeed = 0;
//			avgRadius = 0;
//			targetRadius+=5;
//		}	
		if (speed<=maxSpeed-1)
			acc = 1;
		else acc = 2/(1+Math.exp(speed - maxSpeed-1)) - 1;
//		System.out.println(acc+"   "+maxSpeed+"    "+speed);

//		System.out.println(n+"   "+maxSpeed+"   "+acc);
		CarControl cc = new CarControl(acc,brake,gear,steer,0);
		ol.add(cc);
		return ol;
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub		
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#stopCondition(solo.State)
	 */
	@Override
	public boolean stopCondition(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub
//		double posX = state.state.posX;
//		double posY = state.state.posY;
//		double cx = state.state.cx;
//		double cy = state.state.cy;
//		return Geom.distance(posX, posY, cx, cy)<2;
//		return state.state.getLastLapTime()>=AllowTime*2000+1;
		return (targetRadius>=100);
	}
	
	public boolean shutdownCondition(State<NewCarState, CarControl> state){
		return (stopCondition(state));
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
		return new CarControl(0,0,0,0,1);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#shutdown()
	 */
	@Override
	public CarControl shutdown() {
		// TODO Auto-generated method stub
		return new CarControl(0,0,0,0,2);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void storeSingleAction(NewCarState input, CarControl action,
			NewCarState output) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	
	
	

}
