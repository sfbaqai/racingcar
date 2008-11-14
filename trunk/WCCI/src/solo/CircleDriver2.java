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
	static boolean flying =false;
	static boolean foundRadius = false;
	static double lastAccel = 0.0;
	static double lastAngle = 0.0d;
	static double lastBrkCmd = 0;
	static double lastDv = 0;
	static double lastSteer = 0;
	static double lastPos = 0;
	static int highestPointEdge = 0;
	Vector2D trackDirection = new Vector2D(0,1);
	final static double WIDTHDIV = 1;
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
	final double TCL_SLIP = 2.0;				/* [m/s] range [0..10] */
	final double TCL_RANGE = 5.0;			/* [m/s] range [0..10] */

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
	Vector2D carDirection = null;
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
	Edge edge = null;
	Edge other = null;
	double edgeRadius;
	double nextRadius;
	double currentSpeedRadius;

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

	public void store(){
		series = new XYSeries("Curve");
		for (int i=0;i<edgeDetector.numpoint;++i){				
			series.add(edgeDetector.x.get(i), edgeDetector.y.get(i));
		}
		
		TrackSegment.line(0, 0, carDirection.x*20, carDirection.y*20, series);
	}

	public void estimateBestPath(){
		Vector2D center = edgeDetector.center;
		if (edge==null || center==null) return;
		double width = trackWidth * (1-WIDTHDIV);		
		double radiusLarge = (edgeRadius+trackWidth/2)-width-W/2;
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
		Vector2D t = (turn==TURNRIGHT) ? new Vector2D(SIN_PI_4,-SIN_PI_4) : new Vector2D(-SIN_PI_4,-SIN_PI_4);			
		radiusOfTurn = 3.414*(radiusLarge-0.707*radiusSmall);
		centerOfTurn = center.plus(t.times(radiusOfTurn-radiusSmall));
	}
	
	public void estimatePath(Vector2D hh){
		if (edge==null || edge.center==null) return;
		
		double width = trackWidth * (1-WIDTHDIV);		
//		double radiusLarge = (edgeRadius+trackWidth/2)-width;
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
		Vector2D t = null;
		if (centerOfTurn!=null && hh!=null){
			t = hh.minus(centerOfTurn).normalized();
			optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));				
		}
		
		Vector2D[] p = Geom.ptTangentLine(0, 0, edge.center.x,edge.center.y, radiusSmall);
		Vector2D point = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
//		if (series!=null ){
//			if (point!=null) series.add(point.x,point.y);
//			if (centerOfTurn!=null) series.add(centerOfTurn.x,centerOfTurn.y);
//		}
		

		
		double[] r = new double[3];
//		if (point2Follow!=null && Geom.getCircle(0, 0, centerOfTurn.x, centerOfTurn.y+radiusOfTurn, point2Follow.x, point2Follow.y, r)){
//			cntr=new Vector2D(r[0],r[1]);				
//			rr = Math.sqrt(r[2]);		
//		} else if (optimalPoint!=null && centerOfTurn!=null && Geom.getCircle(0, 0, centerOfTurn.x, centerOfTurn.y+radiusOfTurn, optimalPoint.x, optimalPoint.y, r)){
//			cntr=new Vector2D(r[0],r[1]);				
//			rr = Math.sqrt(r[2]);
//		}
		
		if (centerOfTurn!=null && point!=null){			
			if (Geom.getCircle2(point, new Vector2D(centerOfTurn.x,centerOfTurn.y+radiusOfTurn), new Vector2D(0,0), carDirection, r)!=null){				
				rr = Math.sqrt(r[2]);
				cntr = new Vector2D(r[0],r[1]);
			}
			if (!isTurning) isTurning = (cntr.y<=0);
			if (!isTurning && centerOfTurn!=null){
				p = Geom.ptTangentLine(0, 0, centerOfTurn.x,centerOfTurn.y, radiusOfTurn-W);
				point2Follow = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
				if (point2Follow==null){
//					centerOfTurn = cntr;
//					radiusOfTurn = rr;
					p = Geom.ptTangentLine(0, 0, cntr.x,cntr.y, rr);
					point2Follow = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
				}
			} else if (isTurning && cntr!=null){				
				point2Follow = point;
			}
		}
		
		if (time>=11.9 && time<=13){
			draw=false;
			if (draw)store();
//		TrackSegment.circle(other.center.x,other.center.y, edge.radius,series);
//		if (point!=null)
//			series.add(point.x,point.y);
//		if (centerOfTurn!=null)
//			series.add(centerOfTurn.x,centerOfTurn.y);		
			display();
			draw=false;
	}

	}
	
	public void display(){
		if (!draw) return;
		try{
			if (series!=null) EdgeDetector.drawEdge(series, "E "+time+" "+lastSteer+" "+lastAngle+"  a");
			Thread.sleep(200);
		} catch (Exception e){
			
		}
		if (cntr!=null) draw(cntr,rr,"Go ");
//		if (centerOfTurn!=null) draw(centerOfTurn,radiusOfTurn,"Best ");
//		Edge.drawEdge(edge, "E "+time+" "+str);
		if (edge!=null && edge.center!=null) 
		draw(edge.center,edge.radius,str);		
//		Edge.drawEdge(other, "E "+time+" "+(str.equals("Left Edge")?"Right Edge":"Left Edge"));
		if (other!=null && other.center!=null) 
			draw(other.center,other.radius,(str.equals("Left Edge")?"Right Edge":"Left Edge"));
			
	}

	public void record(){		
		if (!recording) return;
		if (time>=4.604)
			System.out.println();
		centerOfTurn = null;		
//		long time = System.currentTimeMillis();
		nraced = distRaced - raced;
		nraced = Math.round(nraced*10000.0d)/10000.0d;
		
		Edge left = edgeDetector.left;
		Edge right = edgeDetector.right;
//		left.calculateRadius();
		
		Vector2D hh = edgeDetector.highestPoint;
		highestPointEdge = (turn==TURNRIGHT) ? -1 : 1;

		if (left==null && right==null){
			prevEdge = edgeDetector;				
			raced = distRaced;		
			return;
		}		
		
		if (prevEdge.turn*edgeDetector.turn!=-1 && prevEdge.straightDist-1>nraced) {
			edgeDetector.combine(prevEdge, nraced);
			left = edgeDetector.left;
			right = edgeDetector.right;
		}
		
		str = "";
		if (hh!=null){
			if (hh!=null && turn==TURNRIGHT && left!=null && hh.x>left.getHighestPoint().x && left.center!=null){
				if (Math.abs(hh.x-left.getLowestPoint().x)<=trackWidth || left.isPointOnEdge(hh)){
					left.append(hh);
					str = "Left Edge";					
				} else if (right!=null && right.center!=null && right.center.distance(hh)<=right.radius){
					right.append(hh);
					str = "Right Edge";
					highestPointEdge = 1;					
				} else highestPointEdge = 0;
			} else if (hh!=null && turn==TURNLEFT && right!=null && hh.x<right.getHighestPoint().x && right.center!=null){
				if (Math.abs(hh.x-right.getLowestPoint().x)<=trackWidth || right.isPointOnEdge(hh)){
					right.append(hh);
					str = "Right Edge";					
				} else if (left!=null && left.center!=null && left.center.distance(hh)<=left.radius){
					left.append(hh);
					str = "Left Edge";
					highestPointEdge=-1;					
				} else highestPointEdge = 0;
			}			
		}
		
		if (highestPointEdge==0){
			prevEdge = edgeDetector;				
			raced = distRaced;				
			return;			
		}
		
		if (draw) store();
		
		if (left!=null) edgeDetector.left.calculateRadius();
		if (right!=null) edgeDetector.right.calculateRadius();
		System.out.println("**************** E"+time+" "+nraced+" ****************");//		
		turn = edgeDetector.turn;
		
		if (turn==UNKNOWN) {
			turn = prevEdge.turn;
			edgeDetector.center = null;
			prevEdge = edgeDetector;				
			raced = distRaced;				
			return;
		}

		
		edgeDetector.estimateCurve(highestPointEdge);
		if (highestPointEdge==-1){
			edge = left;
			other = right;
		} else {
			edge = right;
			other = left;
		}
		if (left!=null) {
			edgeDetector.left.center = edgeDetector.center;
			edgeDetector.left.radius = edgeDetector.radiusL;
		}
		if (right!=null){
			edgeDetector.right.center = edgeDetector.center;
			edgeDetector.right.radius = edgeDetector.radiusR;
		}
		double[] r = new double[3];		

		if (edge!=null){ 			
			edgeRadius = edge.radius-trackWidth/2;
			Vector2D h = edge.getHighestPoint();
			Vector2D l = edge.allPoints.get(edge.index);
			if (h.x-l.x>trackWidth) 
				nextRadius = edge.radiusNextSeg(r)-trackWidth/2;
			else nextRadius = edgeRadius;
		}



		cntr = null;
		rr=0;		
		if (edgeDetector.center==null){
			prevEdge = edgeDetector;				
			raced = distRaced;
			return;
		}

		if (edgeDetector.center!=null){
			estimateBestPath();			
			estimatePath(hh);						
		}				
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

	double speedControl(CarState cs,double targetSpeed,Vector2D point){
		int gear = cs.getGear();
		final double Dxb  = 0.05f;
		final double Dxxb = 0.01f;
		double[] wheelSpinVel = cs.getWheelSpinVel();
//		double tcl_slip = cs.speedX/3.6*rwd;

		double coeff = 1/3.6/3.6;
		double speedX = cs.speedX/3.6;
		double speed = cs.getSpeed();
		double dv = targetSpeed/3.6-speedX;
//		double rwd = (wheelSpinVel[REAR_LFT]+wheelSpinVel[REAR_RGT])*wheelSpinVel[REAR_LFT]/2-speedX;
		double Dvv = dv - lastDv;
		lastDv = dv;
		double slip=0;
		if (speedX > 0) {
			slip = (wheelSpinVel[REAR_LFT]+wheelSpinVel[REAR_RGT])*wheelRadius[REAR_LFT]/2-speedX;
		} 				
		double acc = 0;
		flying = (slip>TCL_RANGE);			

		if (dv>=0){			
			if (speed<=targetSpeed-1)
				acc = 1;
			else acc = 2/(1+Math.exp(speed - targetSpeed-1)) - 1;

			if (!flying && slip>TCL_SLIP){				
				acc -= Math.min(acc, (slip-TCL_SLIP)/TCL_RANGE);
				
			}
		} else {			
			double s = steerAtSpeed(speed);
			double st = (point==null) ? cs.angle/SimpleDriver.steerLock : Math.max(-SimpleDriver.steerLock,Math.min(cs.angle-PI_2+point.angle(),SimpleDriver.steerLock));
			if (Math.abs(st)>0.2*s && isTurning)
				return 0;
			double b2 = 0;
			double meanSpd = 0.0d;			
//			for (int i = 0; i < 4; i++) {
//				meanSpd = wheelSpinVel[i]*wheelRadius[i]/speedX;
//				if (slip<meanSpd) slip = meanSpd;
//			}
			meanSpd = (wheelSpinVel[REAR_LFT] + wheelSpinVel[REAR_RGT])*wheelRadius[REAR_LFT]/speedX;
			meanSpd /= 2.0;

			if (point!=null){
				double dist = point.length()-3;
				if (dist<0) return 0;
				double currentBrakeDist = brakeDistAtSpeed(speed)-brakeDistAtSpeed(targetSpeed);				
				if (dist>1.1*currentBrakeDist)
					return 1;				
				b2 = currentBrakeDist/dist;
			}
			 
			slip = 1;
			double brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
			if (brake<b2) brake = b2;
			brake = Math.min(brake, 1)*meanSpd;			
			acc = -Math.min(brake, 1);			
		}

		return acc;
	}
	
	
	public double brakeDistAtSpeed(double x){
		return -0.30220189119619+0.0403949586208308*x+0.00221104316193883*x*x-3.56417513475425e-06*x*x*x;
//		return -0.596348768823269+0.051461029864198*x+0.00135309496429643*x*x-2.1617586962591e-06*x*x*x;			
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

	boolean k = false;
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		
		point2Follow = null;		
		double brake = 0.0d;		
		double acc = 1;		
		NewCarState cs = state.state;
		double curPos = -cs.trackPos;
		double dP = curPos-lastPos;
		curAngle = cs.angle;
		carDirection = trackDirection.rotated(-curAngle);
		double speed = cs.getSpeed();	
		currentSpeedRadius = radiusAtSpeed(speed);
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

		double steer = steerControl(cs);
//		double s = steerAtSpeed(speed);
//				
		acc = speedControl(cs, maxSpeed,point2Follow);
//		if (Math.abs(steer)>s){							
//			acc = 0;
//		}

		if (acc>=0 && isTurning && dP*turn<0){
//			if (curPos*turn<=WIDTHDIV-0.1)
//				acc = (2/(1+Math.exp(-10)) - 1);			
//			if (curPos*turn<=WIDTHDIV-0.15){
//				acc = 0;
//			}
						
			acc = 0;
		}
//		
		if (acc<=0) {
			brake = -acc;
			acc = 0;
		}
//		if (isTurning) brake=0;
//		if (time>=5)
//			System.out.println();
		
//		if (isTurning && curPos*turn>=0)
//			steer = gotoPoint(cs, trackDirection);

		int gear = getGear(cs);
		
//		steer = lastSteer + 50 * (steer - lastSteer) * 0.005;
		lastSteer = steer;
		lastPos = curPos;
		lastAngle = curAngle;		
		if (flying) steer = gotoPoint(cs, trackDirection);
		
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
		point2Follow = point;
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
		final double N = 1.0;
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;	
			isTurning = false;
			return curAngle/steerLock;
		}				

		draw = false;
		if (time>=12.4 && time<=13){
			draw = false;
			if (draw) display();
		}
		double s = steerAtSpeed(speed);
		if (highestPointEdge==0){//cannot decide the highest point belong to which edge
			prevSteer = gotoPoint(cs, trackDirection)+curPos/10;
			if (Math.abs(prevSteer)>=s*N)
				prevSteer = (prevSteer<0) ? -s*N : s*N;
			return prevSteer;
		}
				
		double steer = Double.NaN;		
		if (edgeDetector.center==null){			
			prevSteer = gotoPoint(cs, trackDirection);
			if (Math.abs(prevSteer)>=N*s)
				prevSteer = (prevSteer<0) ? -s*N : s*N;
//			if (turn!=UNKNOWN)
//				prevSteer+=(0.3-curPos)*turn/25;
			return prevSteer;
		}
//		double straightDist = (edge!=null && other!=null) ? other.straightDist : 0;
		
		if (!isTurning){
			targetRadius = Math.max(radiusOfTurn,rr);
//			targetRadius = Math.min(Math.min(radiusOfTurn, edgeRadius-trackWidth/2),nextRadius-trackWidth/2);
//			steer = gotoPoint(cs, trackDirection)+(curPos+WIDTHDIV/4*turn)/20;
//			steer = (centerOfTurn!=null) ? steerAtRadius(cs, centerOfTurn.x, centerOfTurn.y, radiusOfTurn-W) : gotoPoint(cs, trackDirection)+curPos/15;						
		} else {
			if (nextRadius<=edgeRadius+trackWidth/2)
				targetRadius = nextRadius-trackWidth/2;
			else targetRadius = nextRadius;//Math.min(rr,edgeRadius+trackWidth/2);			
//			targetRadius=rr;
//			
//			steer = (cntr!=null) ? steerAtRadius(cs, cntr.x, cntr.y, rr) : gotoPoint(cs, trackDirection)+curPos/15;									
		}
		steer = gotoPoint(cs, point2Follow);
		if (Math.abs(steer)>=s*N)
			steer = (steer<0) ? -s*N : s*N;

		if (turn!=UNKNOWN) 			
			maxSpeed = speedAtRadius(targetRadius);			
		else maxSpeed = speed;

		System.out.println("MSpeed "+maxSpeed+" MRadius "+currentSpeedRadius+" TRadius "+rr+"  EdgeRadius "+edgeRadius+" speed "+speed);
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
		double signPos = (curPos<0) ? -1 :1;
		double deflect = Math.min(steerAtSpeed(cs.getSpeed())*signPos,curPos/10);
		if (curPos>1 || curPos<-1)
			return curAngle/steerLock+deflect;
		if (trackWidth<=0)
			return curAngle/steerLock+deflect;

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
			return curAngle/steerLock+deflect;
		}


		if (turn==STRAIGHT){
			recording = false;
			isTurning = false;
			maxSpeed = Double.MAX_VALUE;
			return curAngle/steerLock+deflect;
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
			isTurning = false;						
			double sign = curPos < 0 ? -1 : 1;
			return (curAngle)/steerLock+sign*0.05;
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
		return state.state.getLastLapTime()>=240;
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
