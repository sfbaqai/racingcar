/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Random;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

import org.jfree.data.xy.XYSeries;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver2 extends BaseStateDriver<NewCarState,CarControl> {
	boolean draw = false;
	XYSeries series = new XYSeries("Curve");
	static double lastAccel = 1.0;
	static double lastBrkCmd = 0;
	static double lastDv = 0;
	final static int[] gearUp = new int[]{5000,6000,6000,6500,7000,7500};
	final static int[] gearDown = new int[]{0,2500,3000,3000,3250,3500};
	final static double[] gearRatio = new double[]{-9.0,0,11.97,8.01,5.85,4.5,3.33,2.7,0,0};
	final static double SHIFT = 0.95d;
	final static double SHIFT_MARGIN = 4.0*3.6d;
	final static double DIFFERENTIAL_RATIO = 4.5d;
	final static double[] wheelRadius= new double[]{0.3179,0.3179,0.3276,0.3276};
	final static double[] wheelRadiusInInch= new double[]{8.5,8.5,9,9};
	final static double FLYSPEED = 55*3.6;
	final static double MAXALLOWEDPITCH = 0.06;
	final static double SPEED_MARGIN = 5;
	final static int GEAR_OFFSET = 1;
	final static double enginerpmRedLine = 890.118;//rpm;
	double radiusR = 0;
	double radiusL = 0;
	boolean followedPath = false;
	double targetRadius = 	220;
	double maxSpeed = 0;
	double AllowTime = 60;
	double prevSteer = 0;
	double curAngle;
	public final static int TURNLEFT = -1;
	public final static int TURNRIGHT = 1;
	public final static int STRAIGHT = 0;
	public final static int UNKNOWN = 2;
	public final static double steerLock = 0.785398;
	double time = 0;
	Vector2D cntr = null;
	double rr = 0;
	Vector2D point2Follow = null;
	String str = "";
	/**
	 * 
	 */
	double x =0 ;
	double y = 0;
	final static int FRONT_RGT = 0;
	final static int FRONT_LFT = 1;
	final static int REAR_RGT = 2;
	final static int REAR_LFT = 3;
	public final static double L = 4.76;
	public final static double W = 1.96;
	public final static double carmass = 1050;
	public final static double G = 9.81;
	public final static double PI_2 = Math.PI/2;
	public final static double EPSILON = 1.5;
	public final static double SIN_PI_4=0.7071068;
	public final static double MARGIN = 2;
	public final static double SLIP_LIMIT = 3.5;
	public double trackWidth =-1;
	double workingWidth = 0;
	double leftX;
	double rightX;
	double middleX;
	double ox=0;
	double raced = 0;	
	double distRaced = 0;
	boolean recording = false;	
	int turn;
	boolean isTurning = false;
	Vector2D optimalPoint = null;

	static private Random random = new Random(1234);

	Vector2D highestPoint = null;
	Vector2D highestPointOnOtherEdge = null;
	FuzzyRuleSet fuzzyRuleSet,fuzzyRuleSetB;
	net.sourceforge.jFuzzyLogic.rule.Variable angle,dist,speedx,speedy,pos,steering,currentAngle,slipSpeed,turning;
	net.sourceforge.jFuzzyLogic.rule.Variable angleB,distB,speedxB,speedyB,posB,brake,currentAngleB,slipSpeedB,difference,spaceRemain;
	double nraced = 0;
	double toMiddle = 0;
	double mu = 0;
	double mass = carmass;	
	Vector2D centerOfTurn = null;
	double radiusOfTurn=-1;
	final static int MAX_GEARS = 10;
	/**
	 * 
	 */
	EdgeDetector prevEdge = null;
	EdgeDetector edgeDetector = new EdgeDetector();
	double[] shift = new double[MAX_GEARS];

	public CircleDriver2() {
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


	public static double findPoint(double x0,double y0,double r,double x){//assume that y0<0
		double dx = x-x0;
		double dy = Math.sqrt(r*r-dx*dx);
		return (y0-dy>0) ? (y0-dy) :y0+dy ;
	}

	public Vector2D startTurnPoint(){
		if (centerOfTurn==null) return null;
		double y = findPoint(centerOfTurn.x, centerOfTurn.y, radiusOfTurn,0 );
		if (Double.isNaN(y) || y<centerOfTurn.y) y = centerOfTurn.y;
		return new Vector2D(0,y);
	}

	public void record(){	

		if (!recording) return;
		centerOfTurn = null;
		if (time>=72.898)
			System.out.println();
//		long time = System.currentTimeMillis();
		nraced = distRaced - raced;
		nraced = Math.round(nraced*10000.0d)/10000.0d;		
//		double scale = edgeDetector.trackWidth/prevEdge.trackWidth;		
//		System.out.println(prevEdge.turn+"   "+edgeDetector.turn);	
		if (prevEdge.turn*edgeDetector.turn!=-1 && prevEdge.straightDist-1>nraced) {
			edgeDetector.combine(prevEdge, nraced);						
		}		
		series = new XYSeries("Curve");
		for (int i=0;i<edgeDetector.numpoint;++i){				
			series.add(edgeDetector.x.get(i), edgeDetector.y.get(i));
		}

		Vector2D trackDirection = new Vector2D(0,1);
		Vector2D carDirection = trackDirection.rotated(-curAngle);
		TrackSegment.line(0, 0, carDirection.x*5, carDirection.y*5, series);

//		Vector2D transform = new Vector2D(ax,0);

		System.out.println("**************** E"+time+" "+nraced+" ****************");
//		if (distRaced>650) meta = 1;
		turn = edgeDetector.turn;
//		if (turn==MyDriver.UNKNOWN && prevEdge.turn!=STRAIGHT && prevEdge.turn!=UNKNOWN) turn = prevEdge.turn;
		if (turn==UNKNOWN) {
			prevEdge = edgeDetector;				
			raced = distRaced;		
//			System.out.println(System.currentTimeMillis()-time);
			return;
		}

		Edge left = edgeDetector.left;
		Edge right = edgeDetector.right;
		
		Vector2D hh = edgeDetector.highestPoint;
//		int highestPointEdge = 	edgeDetector.guessPointOnEdge(hh);
		int highestPointEdge = (turn==TURNRIGHT) ? -1 : 1;

		if (left==null && right==null){
			highestPoint = hh;
			highestPointOnOtherEdge = null;
			prevEdge = edgeDetector;				
			raced = distRaced;		
//			System.out.println(System.currentTimeMillis()-time);
			return;
		}

		str = "";
		if (hh!=null && hh.length()<=99.95){
			if (hh!=null && turn==TURNRIGHT && left!=null){
				if (Math.abs(hh.x-left.getLowestPoint().x)<=trackWidth || left.isPointOnEdge(hh)){
					left.append(hh);
					str = "Left ";
				} else {
					right.append(hh);
					str = "Right Edge";
					highestPointEdge = 1;
				}
			} else if (hh!=null && turn==TURNLEFT && right!=null){
				if (Math.abs(hh.x-right.getLowestPoint().x)<=trackWidth ||right.isPointOnEdge(hh)){
					right.append(hh);
					str = "Right Edge";
				} else {
					left.append(hh);
					str = "Left ";
					highestPointEdge=-1;
				}
			}			
		}
		if (left!=null) left.calculateRadius();
		if (right!=null) right.calculateRadius();

		/*if (hh!=null && hh.length()>=99.95 && edgeDetector.straightDist<2.5){
			double[] r = new double[3];
			point2Follow = hh;
			if (turn==TURNRIGHT && left!=null){
				double nr = left.radiusNextSeg(r)-edgeDetector.trackWidth/2;				
				targetRadius = nr;				
			} else if (turn==TURNLEFT && right!=null){
				double nr = right.radiusNextSeg(r)-edgeDetector.trackWidth/2;
				targetRadius = nr;				
			}
			return;
		}//*/

		edgeDetector.estimateCurve(highestPointEdge);				
		cntr = null;
		rr=0;
		double[] r = new double[3];
		if (edgeDetector.center==null){
			prevEdge = edgeDetector;				
			raced = distRaced;
			return;
		} else if (edgeDetector.center!=null){
			Vector2D center = edgeDetector.center;
			radiusR = edgeDetector.radiusR;
			radiusL = edgeDetector.radiusL;

			Vector2D t = null;			
			if (turn==TURNRIGHT && radiusR>0){
				radiusR += W/2;
				radiusL -= W;
				t = new Vector2D(SIN_PI_4,-SIN_PI_4);
				radiusOfTurn = 3.414*(radiusL-0.707*radiusR);
				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusR));
			} else if (turn==TURNLEFT && radiusL>0){
				radiusL += W;
				radiusR -= W/2;
				t = new Vector2D(-SIN_PI_4,-SIN_PI_4);
				radiusOfTurn = 3.414*(radiusR-0.707*radiusL);
				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusL));
			}
			if (left!=null) edgeDetector.left.center = edgeDetector.center;
			if (right!=null) edgeDetector.right.center = edgeDetector.center;
			if (left!=null) edgeDetector.left.radius = radiusL;
			if (right!=null) edgeDetector.right.radius = radiusR;
			
			if (centerOfTurn!=null && centerOfTurn.length()>=radiusOfTurn){
//				cntr = centerOfTurn;
//				rr = radiusOfTurn;
				point2Follow = trackDirection;
				maxSpeed = speedAtRadius(radiusOfTurn)-10;
				prevEdge = edgeDetector;				
				raced = distRaced;
				return;
			}

			if (centerOfTurn!=null && hh!=null){
				t = hh.minus(centerOfTurn).normalized();
				optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));				
			}
			Vector2D[] p = null;
			if (turn==TURNLEFT){
				p = Geom.ptTangentLine(0, 0, center.x,center.y, radiusL);
			} else if (turn==TURNRIGHT ){				
				p = Geom.ptTangentLine(0, 0, center.x,center.y, radiusR);
			}

			Vector2D point = (turn==TURNLEFT && p!=null) ? p[1] : (turn==TURNRIGHT && p!=null) ? p[0] : null;
			if (point!=null && Geom.getCircle(0, 0, centerOfTurn.x, centerOfTurn.y+radiusOfTurn-W, point.x, point.y, r)){
				cntr=new Vector2D(r[0],r[1]);				
				rr = Math.sqrt(r[2]);		
			} else
				if (optimalPoint!=null && centerOfTurn!=null && Geom.getCircle(0, 0, centerOfTurn.x, centerOfTurn.y+radiusOfTurn-W, optimalPoint.x, optimalPoint.y, r)){
					cntr=new Vector2D(r[0],r[1]);				
					rr = Math.sqrt(r[2]);
				}

		}				

		draw = false;

		if (time>=72.5 && time<=74){ 
			draw =false;
			r = new double[3];


//			if (left!=null && hh.length()<99.95) left.append(hh);
//			left.removeLastPoint();
//			if (right!=null && hh.length()<99.95) right.append(hh);
//			if (left!=null){
//			int index = left.index;
//			Vector2D p1 = left.get(index+1);
//			Vector2D p2 = left.get(index+2);
//			Vector2D p3 = left.get(index+3);			
//			if (p1!=null && p2!=null && p3!=null) 	{
//			Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
////			if (Math.sqrt(r[2])<600) TrackSegment.circle(r[0], r[1], Math.sqrt(r[2]), series);				
//			}
//			}

//			if (right!=null){
//			int index = right.index;
//			Vector2D p1 = right.get(index+1);
//			Vector2D p2 = right.get(index+2);
//			Vector2D p3 = right.get(index+3);
//			if (p1!=null && p2!=null && p3!=null) 	{
//			Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
//			if (Math.sqrt(r[2])<600) TrackSegment.circle(r[0], r[1], Math.sqrt(r[2]), series);
//			}
//			}


//			if (left!=null) System.out.println("Left   "+left.calculateRadius()+"     "+speedAtRadius(left.radius));
//			if (right!=null) System.out.println("Right   "+right.calculateRadius());
//			if (left.center!=null && right.center !=null){
//			Vector2D p1 = Geom.ptTangentLine(0, 0, right.center.x, right.center.y, right.radius)[0];
//			Vector2D p2 = edgeDetector.highestPoint;
//			r = new double[3];
//			Geom.getCircle(0, 0, p1.x, p1.y, p2.x, p2.y, r);
////			TrackSegment.line(0, 0, p1.x, p1.y, series);
//			if (Math.sqrt(r[2])<600) TrackSegment.circle(r[0], r[1], Math.sqrt(r[2]), series);
//			}
//			for (int i=0;i<left.size;++i){				
//			series.add(left.x.get(i), left.y.get(i));
//			}


//			double[] rad = new double[3];
//			if (left!=null) {
//			double rr = left.radiusNextSeg(rad);
//			if (rr<600) TrackSegment.circle(rad[0], rad[1], rr, series);
//			}

//			if (right!=null) {
//			double rr = right.radiusNextSeg(rad);
//			if (rr<600) TrackSegment.circle(rad[0], rad[1], rr, series);
//			}

//			if (left!=null){
//			int size = left.size;
//			Vector2D p1 = left.get(size-2);
//			Vector2D p2 = left.get(size-3);
//			Vector2D p3 = left.get(size-4);
//			r = new double[3];
//			if (Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r)){
//			Vector2D o = new Vector2D(r[0],r[1]);
//			System.out.println(" Check  "+(o.distance(hh)-Math.sqrt(r[2])));
//			if (draw) draw(o,Math.sqrt(r[2]));
//			}
//			}
//			if (right!=null && draw){
//			int size = right.size;
//			Vector2D p1 = right.get(size-2);
//			Vector2D p2 = right.get(size-3);
//			Vector2D p3 = right.get(size-4);
//			r = new double[3];
//			if (Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r)){
//			Vector2D o = new Vector2D(r[0],r[1]);
//			if (draw) draw(o,Math.sqrt(r[2]));
//			}

//			}
			if (draw) EdgeDetector.drawEdge(series, "E"+time+" "+"a");
			if (draw && cntr!=null) draw(cntr,rr,"Go");
			if (left!=null && left.center!=null) draw(edgeDetector.left.center,edgeDetector.left.radius,"L");
//			System.out.println(left.center+"    "+left.radius);
			if (right!=null && right.center!=null) draw(right.center,right.radius,"R");
//			if (right!=null && right.center!=null) System.out.println(right.center+"    "+right.radius);
			if (hh!=null && right!=null) System.out.println(hh+"   "+hh.distance(right.getHighestPoint()));

//			left.removeLastPoint();
//			right.removeLastPoint();
			if (draw && centerOfTurn!=null) draw(centerOfTurn,radiusOfTurn,"Best");
		}

//		if (cntr!=null) draw(cntr,rr);
//		System.out.println(cntr+"   "+optimalPoint+"    "+startTurnPoint+"   New");
		Vector2D hc = (centerOfTurn==null) ? null : new Vector2D(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
		if (highestPoint==null) highestPoint=hh;		
//		if (hc!=null && startTurnPoint!=null) 
//		System.out.println((hc.distance(startTurnPoint))+"   "+hc.minus(startTurnPoint).angle()+"    Check coi");
//		centerOfTurn = edgeDetector.center;
//		centerOfTurn = null;
//		radiusOfTurn = (radiusL+radiusR)*0.5;
//		cntr = edgeDetector.center;
//		rr = radiusOfTurn;
		prevEdge = edgeDetector;				
		raced = distRaced;


//		System.out.println(System.currentTimeMillis()-time);	
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
		if (x>=500) return Double.MAX_VALUE;
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
	public CircleDriver2(String name) {
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
		double steer = Math.max(-SimpleDriver.steerLock,Math.min(angle-Math.PI/2+a.angle(point),SimpleDriver.steerLock));		
		return (steer)/SimpleDriver.steerLock;
	}


	public double steerAtRadius(CarState state,double cx,double cy,double targetRadius){
		double angle = state.angle;				
//		double speed = state.getSpeed();		
		double d = Math.sqrt(cx*cx+cy*cy);
//		Vector2D p = new Vector2D(cx,cy).orthogonal().normalised().rotated(angle);
//		double closeDist = speed*0.04/3.6;				

		Vector2D trackDirection = new Vector2D(0,1);
		Vector2D carDirection = trackDirection.rotated(-angle);
		Vector2D point = (edgeDetector.turn==TURNRIGHT) ? new Vector2D(cx,cy).orthogonal() : new Vector2D(-cx,-cy).orthogonal();
		double a = point.angle(carDirection);		
		if (d<targetRadius-0.5){
			return Double.NaN;
		} else if (d>targetRadius){			
			Vector2D[] points = Geom.ptTangentLine(0, 0, cx, cy, targetRadius);
			point = (edgeDetector.turn==TURNRIGHT) ? points[0] : points[1];
//			point = points[0];
		}	
//		return  steerToPoint(state, point);				
		return gotoPoint(state, point);
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


	double filterABS(CarState cs,double brake)	{
		// convert speed to m/s
		if (brake<=0) return 0;
		brake = Math.min(1.0, brake);
		double speed = cs.speedX/3.6;
		// when spedd lower than min speed for abs do nothing
		if (speed < MyDriver.absMinSpeed)
			return brake;

		// compute the speed of wheels in m/s
		double slip = Double.MAX_VALUE;	    
		for (int i = 0; i < 4; i++)	{
			slip += cs.wheelSpinVel[i] * MyDriver.wheelRadius[i]/speed;			
		}

		slip = slip/4.0f;
		// when slip too high applu ABS
		if (slip < 0.9) {
			brake *=slip;
		}

		// check brake is not negative, otherwise set it to zero

		return brake;
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



	double speedControl(CarState cs,double targetSpeed){
		int gear = cs.getGear();
		final double Dxb  = 0.05f;
		final double Dxxb = 0.01f;
		double[] wheelSpinVel = cs.getWheelSpinVel();
		double rwd = (wheelSpinVel[REAR_LFT]+wheelSpinVel[REAR_RGT])*wheelSpinVel[REAR_LFT]/2;
//		double tcl_slip = cs.speedX/3.6*rwd;

		double speedX = cs.speedX/3.6;
		double speed = cs.getSpeed();
		double dv = targetSpeed/3.6-speedX;
		double Dvv = dv - lastDv;
		lastDv = dv;
		double slip=0;
		if (speedX > 0) {
			slip = (wheelRadius[3] * wheelSpinVel[3] - speedX) / speedX;
		} 				
		double acc = 0;		


		if (dv>0){			
			if (speed<=targetSpeed-1)
				acc = 1;
			else acc = 2/(1+Math.exp(speed - targetSpeed-1)) - 1;

			if ((slip > 1.0) && (gear > 1)) {
				acc *= 0.5;
			} else {
				acc = lastAccel + 50 * (acc - lastAccel) * 0.01;	
				lastAccel = acc;		    
			}
//			acc = Math.min(acc,Math.abs(dv/6.0d));
		} else {			
			slip = 0;
			double meanSpd = 0.0d;
			for (int i = 0; i < 4; i++) {
				meanSpd += wheelSpinVel[i];
			}
			meanSpd /= 4.0;

			if (meanSpd > 15.0) {
				for (int i = 0; i < 4; i++) {
					if (((meanSpd - wheelSpinVel[i]) / meanSpd) < -0.1) {
						slip = 1.0;
					}
				}
			}

			double brake = Math.min(-dv * Dxb + Dvv * Dxxb, 1.0);
			if (slip > 0.2) {
				double maxslp = Math.exp(-3.47*(slip - 0.2));
				brake = Math.min(brake, maxslp);
			} else {
				brake = (lastBrkCmd + 50 * (brake - lastBrkCmd) * 0.01);	
				lastBrkCmd = brake;

			}
			acc = -Math.min(brake, Math.abs(dv/5.0));			
		}

		return acc;
	}

	boolean k = false;
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		point2Follow = null;		
		double brake = 0.0d;		
		double acc = 1;		
		NewCarState cs = state.state;
		curAngle = cs.angle;
		double speed = cs.getSpeed();		
		time = cs.getLastLapTime();
//		double posX = cs.posX;
//		double posY = cs.posY;
//		double cx = cs.cx;
//		double cy = cs.cy;				
		edgeDetector = new EdgeDetector(cs);
		distRaced = cs.distRaced;

		if (edgeDetector.trackWidth<=0 && prevEdge!=null)
			edgeDetector.trackWidth = prevEdge.trackWidth;
		trackWidth = edgeDetector.getTrackWidth();//must keep this line
		workingWidth = trackWidth-2*W;			
		toMiddle = edgeDetector.toMiddle();				
		mass = carmass + cs.getFuel();				

		if (accel!=0 || oldSpeed!=0) {
			accel = (speed-oldSpeed)/0.02;		
		}		


		double steer = steerControl(cs);
		maxSpeed=Math.min(200,maxSpeed);
		acc = speedControl(cs, maxSpeed);
				
		if (acc<=0) {
			brake = -acc;
			acc = 0;
		}
		if ((turn==TURNLEFT || turn==TURNRIGHT) && (edgeDetector.curPos*turn)<0.8 && acc >0 && speed>30){
			if ((edgeDetector.curPos*turn)>0.9)
				brake = -0.1;
			acc = 0;
		}
		
		int gear = getGear(cs);
		CarControl cc = new CarControl(acc,brake,gear,steer,0);
		ol.add(cc);
		return ol;
	}

	public void startRecordingEdge(){
//		long time = System.currentTimeMillis();
		recording = true;		
		prevEdge = edgeDetector;					

		highestPoint = edgeDetector.highestPoint;		
		highestPointOnOtherEdge = null;			
		raced = distRaced;		
//		System.out.println(System.currentTimeMillis()-time);
	}

	public double gotoPoint(CarState cs,Vector2D point){
		double angle = cs.angle;
		if (point==null) return angle/SimpleDriver.steerLock;				
		double steer = Math.max(-SimpleDriver.steerLock,Math.min(angle-PI_2+point.angle(),SimpleDriver.steerLock));		
		return (steer)/SimpleDriver.steerLock;

	}


	public void draw(Vector2D centerOfTurn,double radiusOfTurn,String s){

		if (draw){			
//			EdgeDetector.drawEdge(edgeDetector, "E"+time+" "+radiusOfTurn+" "+radiusL+""+radiusR+" a");
			XYSeries series = new XYSeries("Curve");
//			if (centerOfTurn!=null) {
//			series.add(centerOfTurn.x,centerOfTurn.y);
//			series.add(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
//			}
//			if (highestPoint!=null) series.add(highestPoint.x,highestPoint.y);			
			for (int i=0;i<edgeDetector.x.size();++i)
				series.add(edgeDetector.x.get(i),edgeDetector.y.get(i));

//			if (edgeDetector.center!=null){
//			if (edgeDetector.left.center!=null && edgeDetector.radiusL>0 && edgeDetector.radiusL<550) TrackSegment.circle(edgeDetector.left.center.x, edgeDetector.left.center.y, edgeDetector.left.radius, series);
//			if (edgeDetector.right.center!=null && edgeDetector.radiusR>0 && edgeDetector.radiusR<550) TrackSegment.circle(edgeDetector.right.center.x, edgeDetector.right.center.y, edgeDetector.right.radius, series);
//			}
			if (centerOfTurn!=null && radiusOfTurn<550) TrackSegment.circle(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, series);
//			if (startTurnPoint!=null) series.add(startTurnPoint.x,startTurnPoint.y);
//			if (optimalPoint!=null) series.add(optimalPoint.x,optimalPoint.y);
//			if (cntr!=null && rr<550) TrackSegment.circle(cntr.x, cntr.y, rr, series);

			EdgeDetector.drawEdge(series, "E"+time+" "+s+" "+str+" a");
//			if (left!=null) Edge.drawEdge(left, "E"+distRaced+" "+nraced+"b");
//			if (right!=null) Edge.drawEdge(right, "E"+distRaced+" "+nraced+"c");
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			meta=1;
		}

	}

	public double fuzzySteering(CarState cs){
		double curPos = -cs.trackPos;
		double speed = cs.getSpeed();
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;	
			isTurning = false;
			return curAngle/steerLock;
		}		
		Vector2D trackDirection = new Vector2D(0,1);

		double steer = Double.NaN;		
		if (edgeDetector.center==null){
			maxSpeed = speed;
			prevSteer = gotoPoint(cs, trackDirection);
			if (turn!=UNKNOWN)
				prevSteer+=(0.2-curPos)*turn/25;
			return prevSteer;
		}
//		if (time>=41)
//			System.out.println();

		if (point2Follow!=null){
			steer = gotoPoint(cs, point2Follow);
			prevSteer = steer;
			return prevSteer;
		}

//		maxSpeed = Double.MAX_VALUE;		


//		double sign = (turn==TURNRIGHT) ? -1 : 1;				
//		Vector2D hh = edgeDetector.highestPoint;
		
		double edgeRadius =0;
//		targetRadius = radiusOfTurn;
		if (centerOfTurn==null || radiusOfTurn<0){
			prevSteer = gotoPoint(cs, trackDirection)+curPos/5;
			return prevSteer;
		}
		Edge edge = null;
		Edge other = null;

		if (edgeDetector.left!=null && turn==TURNRIGHT){		
			edge = edgeDetector.left;
			other = edgeDetector.right;			
			if (other !=null) other.radius = radiusR;
			if (edge!=null) edge.radius = radiusL;
		} else if (edgeDetector.right!=null && turn==TURNLEFT){				
			edge= edgeDetector.right;
			other = edgeDetector.left;
			if (other !=null) other.radius = radiusL;
			if (edge!=null) edge.radius = radiusR;
		}
		if (other!=null) other.center = edgeDetector.center;
		if (edge!=null) edge.center = edgeDetector.center;

		double[] r = new double[3];
		double nextRadius = 0;

		if (edge!=null){ 			
			edgeRadius = edge.radius-trackWidth/2;
			Vector2D h = edge.getHighestPoint();
			Vector2D l = edge.getLowestPoint();
			if (h.x-l.x>trackWidth) 
				nextRadius = edge.radiusNextSeg(r)-trackWidth/2;
			else nextRadius = rr;
		}
		
		
		if (nextRadius>=edgeRadius){//next turn is of nearly the same radius or larger, dont need to slow down
			targetRadius = rr;
		} else {
			targetRadius = nextRadius;
		}
				
		double currentRadiusAtSpeed = radiusAtSpeed(speed);
		
			

		double straightDist = (edge!=null && other!=null) ? other.straightDist : 0;

		if (targetRadius>=400 || (currentRadiusAtSpeed<nextRadius && currentRadiusAtSpeed<edgeRadius)){
//			steer = gotoPoint(cs, trackDirection);
			steer = steerAtRadius(cs, cntr.x, cntr.y, rr);
		} else 	if (currentRadiusAtSpeed>=rr-SPEED_MARGIN || straightDist<=30){//if current speed is nearly match the 
			steer = steerAtRadius(cs, cntr.x, cntr.y, rr);
		} else {
			steer = gotoPoint(cs, trackDirection);
		}
		if (steer*turn>=0) steer = gotoPoint(cs, trackDirection);



//		if (centerOfTurn.length()>=radiusOfTurn){
////		System.out.println(radiusOfTurn+"\t"+time);
//		targetRadius = Math.min(radiusOfTurn,rr);
//		steer = steerAtRadius(cs, centerOfTurn.x, centerOfTurn.y, radiusOfTurn);
//////	steer = gotoPoint(cs, new Vector2D(0,1));			
//////	draw(centerOfTurn,targetRadius);

////		} else if (straightDist>=30){		
////		double tmp = (edge!=null && edge.center!=null) ?edge.center.length()-W/2:0;
////		if (maxRadius<=tmp && edge!=null && edge.center!=null){
////		targetRadius = tmp;
////		steer = steerAtRadius(cs, edge.center.x, edge.center.y, targetRadius);
////		} else steer = gotoPoint(cs, new Vector2D(0,1));
////		} else if (straightDist >15 && straightDist<30){
////		steer = gotoPoint(cs, new Vector2D(0,1));
//		} else if (centerOfTurn!=null && centerOfTurn.length()<radiusOfTurn && straightDist<35){
//		System.out.println(rr+"\t"+time);			
//		if (edge!=null) targetRadius = edge.radiusNextSeg(r)-trackWidth/2;
//		if (other!=null) edgeRadius = other.radius;

////		targetRadius = Math.min(targetRadius,rr);
//		if (maxRadius<=rr || rr<=targetRadius)
//		steer = steerAtRadius(cs, cntr.x, cntr.y, rr);
//		else {
////		targetRadius =Math.min(Math.min(targetRadius,rr),edgeRadius);				
////		if (other!=null && targetRadius>other.radius)
////		targetRadius = other.radius;
//		targetRadius = edgeRadius;
////		if (centerOfTurn!=null) 
////		steer = steerAtRadius(cs, centerOfTurn.x, centerOfTurn.y, maxRadius);
////		else steer = gotoPoint(cs, new Vector2D(0,1));
//		steer = gotoPoint(cs, new Vector2D(0,1));
////		if (curPos*turn<0) 
////		steer += curPos/5;	
//		}


//		if (steer*turn>=0) steer = 0;
////		draw(cntr,targetRadius);
//		} else if (straightDist>80){
//		steer = gotoPoint(cs, new Vector2D(0,1))+(0.5-curPos)*turn/1000;
//		} else {
//		steer = gotoPoint(cs, new Vector2D(0,1));
//		}

		if (turn!=UNKNOWN) 			
				maxSpeed = speedAtRadius(targetRadius);			
		else maxSpeed = speed;

		System.out.println("MSpeed "+maxSpeed+" MRadius "+currentRadiusAtSpeed+" TRadius "+targetRadius+"  EdgeRadius "+edgeRadius+" speed "+speed);
		if (Double.isNaN(steer)){			
			followedPath = false;
			recording = false;			
			steer =  prevSteer;
		}		
		prevSteer = steer;
		return prevSteer;
	}


	public double steerControl(CarState cs){
		double curAngle = cs.angle;
		double curPos = -cs.trackPos;
		if (trackWidth<=0)
			return curAngle/steerLock+curPos/10;

		if (recording){ 			
			record();		
		} else turn = edgeDetector.turn;

		if (followedPath){			
			double steer =  fuzzySteering(cs);			
			return steer;
		}
		if (!followedPath && (curPos<= -(trackWidth/workingWidth) || curPos>=(trackWidth/workingWidth))){
			recording = false;
			followedPath = false;			
			return curAngle/steerLock+curPos/15;
		}


		if (turn==STRAIGHT){
			recording = false;
			isTurning = false;
			maxSpeed = Double.MAX_VALUE;
			return curAngle/steerLock+curPos/15;
		}

		if (!recording) {
			startRecordingEdge();
			turn = UNKNOWN;
			return curAngle/steerLock;
		}
		if (turn==UNKNOWN){
//			if (maxSpeed<150) 
//			maxSpeed = Double.MAX_VALUE;
//			else maxSpeed = cs.speedX-1;
			return (curAngle+Math.abs(random.nextDouble()*0.05))/steerLock;
		}


		return fuzzySteering(cs);
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
		return state.state.getLastLapTime()>=120;
//		return (targetRadius>=300);
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
		System.out.println("Score  : "+distRaced);
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
