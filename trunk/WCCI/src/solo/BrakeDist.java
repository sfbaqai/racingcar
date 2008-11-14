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
public final class BrakeDist extends BaseStateDriver<NewCarState,CarControl> {

	double targetRadius = 	0;
	double maxSpeed = 250;
//	double AllowTime = 60000;
	double dist = 0;
	/**
	 * 
	 */
	final static int MAX_GEARS = 10;
	double x =0 ;
	double y = 0;
	final static int GEAR_OFFSET = 1;
	final static double enginerpmRedLine = 890.118;//rpm;
	final static double[] wheelRadius= new double[]{0.3179,0.3179,0.3276,0.3276};
	final static double[] wheelRadiusInInch= new double[]{8.5,8.5,9,9};
	final static double WIDTHDIV = 1;
	final static int[] gearUp = new int[]{5000,6000,6000,6500,7000,7500};
	final static int[] gearDown = new int[]{0,2500,3000,3000,3250,3500};
	final static double[] gearRatio = new double[]{-9.0,0,11.97,8.01,5.85,4.5,3.33,2.7,0,0};
	
	double[] shift = new double[MAX_GEARS];
	
	public BrakeDist() {
		// TODO Auto-generated constructor stub
		ignoredExisting = true;
		storeNewState = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < MAX_GEARS; i++) {
			j = i + 1;
			if (j < MAX_GEARS) {
				if ((gearRatio[j] != 0) && (gearRatio[i] != 0)) {				
					shift[i] = enginerpmRedLine * .95  * wheelRadius[2] / gearRatio[i];
					/* GfOut("   Gear %d: shift %d km/h\n", i, (int)(shiftThld[idx][i] * 3.6)); */
				} else {
					shift[i] = 10000.0;
				}
			} else {
				shift[i] = 10000.0;
			}				
		}
	}
	
	public int getGear(CarState cs) {
		// TODO Auto-generated method stub
		int gear = cs.gear;
		double speedX = cs.speedX/3.6;

		// if gear is 0 (N) or -1 (R) just return 1 
		if (gear<1)
			return 1;

		if (speedX > shift[gear+ GEAR_OFFSET]) {
			gear++;
		} else if ((gear > 1) && (speedX < (shift[gear-1+GEAR_OFFSET] - 10.0))) {
			gear--;
		}
		if (gear <= 0) {
			gear++;
		}

		return gear;
	}

	
	public double steerAtSpeed(double x){
		if (x>=216)
			return 0.405956540491453-0.00402398400604155*x+1.46983630854209e-05*x*x-1.88619693222940e-08*x*x*x;
		if (x>180)
			return -6.40122354928717+0.100933617693404*x-0.000519289942888996*x*x+8.79221413082921e-07*x*x*x;
		if (x>140)
			return 1.31349334978100-0.0158496396133958*x+6.53860943510914e-05*x*x-8.64166930377492e-08*x*x*x;		
		return 1.00309570103181-0.0115995398441519*x+4.70782598803805e-05*x*x-6.49876327849716e-08*x*x*x;	
	}
	
	public double steerAtRadius(double x){
		if (x>=100)
			return 0.0810250510703418-0.000750740530082075*x+2.89854924506364e-06*x*x-3.95924129765923e-09*x*x*x;
		if (x>=70)
			return -1.36459470940743+0.0582094289481451*x-0.000746679722815475*x*x+3.04328497896615e-06*x*x*x;
		if (x>=50)
			return -0.485794442078747+0.0364424208448416*x-0.000642936741947271*x*x+3.44706692338737e-06*x*x*x;
			
		return 1.02589841472933-0.0615964400025362*x+0.00163249602462468*x*x-1.46149044885214e-05*x*x*x;		
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
	public BrakeDist(String name) {
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
	
	public double brakeDistAtSpeed(double x){
		return -0.30220189119619+0.0403949586208308*x+0.00221104316193883*x*x-3.56417513475425e-06*x*x*x;
//		return -0.596348768823269+0.051461029864198*x+0.00135309496429643*x*x-2.1617586962591e-06*x*x*x;
				
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
	double avgSteer = 0;
	int count =0 ;
	int n = 0;
	boolean ok = true;
	double mr=0;
	double startTime=10;
	boolean braking = false;
	double sdist = 0;
	double edist = 0;
	double bspeed = 0;
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#drive(solo.State)
	 */
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		int meta =0;
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
		NewCarState cs = state.state;
		
		double steer = state.state.getAngleToTrackAxis()/SimpleDriver.steerLock;
		if (!braking){
			if (speed<=maxSpeed-1)
				acc = 1;
			else acc = 2/(1+Math.exp(speed - maxSpeed-1)) - 1;
			if (speed>=maxSpeed-1){
				braking = true;
				sdist = cs.distRaced;
				bspeed = speed;
			}
		} else {
			acc = 0;
			brake = 1;
			if (speed<=0.01){
				braking = false;
				edist = cs.distRaced;
				System.out.println(bspeed+"\t\t"+(edist-sdist));
				maxSpeed+=5;
//				if (edist>4000) meta=1;
				edist = 0;
				bspeed=0;
				sdist = 0;
			}
		}
		gear = getGear(cs);
		CarControl cc = new CarControl(acc,brake,gear,steer,meta);
		ol.add(cc);
		return ol;
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#init()
	 */
	@Override
	public void init() {
		edist = 0;
		sdist = 0;
		braking = false;
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
		return (maxSpeed>=320 || state.state.distRaced>6000);
	}
	
	public boolean shutdownCondition(State<NewCarState, CarControl> state){
		return (stopCondition(state) && maxSpeed>=300);
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
