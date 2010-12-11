/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.lang.MutableString;

import java.awt.geom.AffineTransform;
import java.util.Random;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;



/**
 * @author kokichi3000
 *
 */
public final class MyDriver extends SimpleDriver {
	public final static int TURNLEFT = -1;
	public final static int TURNRIGHT = 1;
	public final static int STRAIGHT = 0;
	public final static int UNKNOWN = 2;
	public final static double epsilon=0.02;
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

	/**
	 * 
	 */
	EdgeDetector prevEdge = null;
	EdgeDetector edgeDetector = new EdgeDetector();
	public MyDriver() {
		loadSteer();
		loadBrake();
	}
	
	public void loadSteer(){
		String fileName = "fcl/steer.fcl";//name of the system
		FIS fis = FIS.load(fileName, false);//load the system
		if( fis == null )
		{ // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}//load the system from here
		fuzzyRuleSet = fis.getFuzzyRuleSet();//create a fuzzy rule set from the system		

		angle = fuzzyRuleSet.getVariable(new MutableString("angle"));
		dist = fuzzyRuleSet.getVariable(new MutableString("dist"));
		speedx = fuzzyRuleSet.getVariable(new MutableString("speedx"));
		speedy = fuzzyRuleSet.getVariable(new MutableString("speedy"));
		pos = fuzzyRuleSet.getVariable(new MutableString("pos"));
		steering = fuzzyRuleSet.getVariable(new MutableString("steer"));
		currentAngle = fuzzyRuleSet.getVariable(new MutableString("curAngle"));
		slipSpeed = fuzzyRuleSet.getVariable(new MutableString("slipSpeed")); 	
		turning = fuzzyRuleSet.getVariable(new MutableString("turning"));
	}
	
	public void loadBrake(){
		String fileName = "fcl/speed.fcl";//name of the system
		FIS fis = FIS.load(fileName, false);//load the system
		if( fis == null )
		{ // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}//load the system from here
		fuzzyRuleSetB = fis.getFuzzyRuleSet();//create a fuzzy rule set from the system		

		angleB = fuzzyRuleSetB.getVariable(new MutableString("angle"));
		distB = fuzzyRuleSetB.getVariable(new MutableString("dist"));
		speedxB = fuzzyRuleSetB.getVariable(new MutableString("speedx"));
		speedyB = fuzzyRuleSetB.getVariable(new MutableString("speedy"));
		posB = fuzzyRuleSetB.getVariable(new MutableString("pos"));
		brake = fuzzyRuleSetB.getVariable(new MutableString("brake"));
		currentAngleB = fuzzyRuleSetB.getVariable(new MutableString("curAngle"));
		slipSpeedB = fuzzyRuleSetB.getVariable(new MutableString("slipSpeed")); 
		difference = fuzzyRuleSetB.getVariable(new MutableString("difference"));
		spaceRemain= fuzzyRuleSetB.getVariable(new MutableString("spaceRemain"));
	}


	@Override
	double filterABS(double brake)	{
		// convert speed to m/s
		brake = Math.min(1.0, brake);
		double speed = this.speed/3.6;
		// when spedd lower than min speed for abs do nothing
		if (speed < absMinSpeed)
			return brake;

		// compute the speed of wheels in m/s
		double slip = Double.MAX_VALUE;	    
		for (int i = 0; i < 4; i++)	{
			slip += wheelSpinVel[i] * wheelRadius[i];			
		}

		slip = speed - slip/4.0f;
		// when slip too high applu ABS
		if (slip > absSlip) {
			brake = brake - (slip - absSlip)/absRange;
		}

		// check brake is not negative, otherwise set it to zero
		if (brake<0)
			return 0;
		else
			return brake;
	}
	int maxGear = 0;
	double targetRadius = 0;
	@Override
	double getAccel() {		
		// TODO Auto-generated method stub
		// checks if car is out of track
		if (curPos <= 1 && curPos >= -1)
		{
//			double targetSpeed = maxSpeed;

			// accel/brake command is expontially scaled w.r.t. the difference between target speed and current one
//			return 2/(1+Math.exp(speed - maxSpeed)) - 1;

			if (speed<=maxSpeed-1)
				return 1;
			else return 2/(1+Math.exp(speed - maxSpeed-1)) - 1;
		} else {			
			if (speedX>150)
				return 0;
			if (speedX<100)
				return 0.5;
			return 0.5; // when out of track returns a moderate acceleration command
		}

	}

	double accel = 0;
	double oldSpeed = 0;
	@Override
	int getGear() {
		// TODO Auto-generated method stub	    
		// if gear is 0 (N) or -1 (R) just return 1 
//		if (gear<1)
//			return 1;

//		double gr_up = gearRatio[gear + GEAR_OFFSET];
//		double omega = enginerpmRedLine/gr_up/DIFFERENTIAL_RATIO/3.6;
//		double wr = wheelRadius[2];

//		System.out.println(speedAtRpm(gearUp[gear-1], gr_up)+"   chuoi  "+gear);
//		if (omega*wr*SHIFT < speedX) 
//		return gear + 1;
		// check if the RPM value of car is greater than the one suggested 
		// to shift up the gear from the current one     
//		if (gear <6 && rpm >= gearUp[gear-1])
//			return gear + 1;		
//		else
//			check if the RPM value of car is lower than the one suggested 
//			to shift down the gear from the current one
		
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
				
		if (accel>0 && accel<0.5 && gear<maxGear && speed<maxSpeed) gear++;
		if (gear<=0) gear=1;
		oldSpeed = speed;
			if (gear > 1 && rpm <= gearDown[gear-1])
				return gear - 1;
//		otherwhise keep current gear
//		double gr_down = gearRatio[gear + GEAR_OFFSET - 1];
//		omega = enginerpmRedLine/gr_down/DIFFERENTIAL_RATIO/3.6;
//		if (gear > 1 && omega*wr*SHIFT > speedX + SHIFT_MARGIN) 
//		return gear - 1;
		return gear;
	}




	@Override
	public void init(){		
		edgeDetector.init(carState,trackWidth);
//		edgeDetector.init(carState);
		if (edgeDetector.trackWidth<=0 && prevEdge!=null)
			edgeDetector.trackWidth = prevEdge.trackWidth;
		trackWidth = edgeDetector.getTrackWidth();//must keep this line
		workingWidth = trackWidth-2*W;
		
//		curPos /= trackWidth/workingWidth;
		toMiddle = edgeDetector.toMiddle();		
		mu = estimateMu(speed);
		mass = carmass + fuel;		
	}

	//try to get to new pos within distance meters
	public double adjustToPos(double newPos,double distance){
		System.out.println("Adjusting");
		if (newPos<-1 || newPos>1 || Math.abs(curPos-newPos)<epsilon || distance<=0){
			return curAngle/steerLock;
		}



//		if (distance<2*L) return curAngle/steerLock;

		double diffx = (newPos-curPos)*workingWidth*0.5;		
		double angle = curAngle-Math.atan2(diffx,distance );
		angle = Math.min(angle, steerLock);
		angle = Math.max(angle, -steerLock);
//		System.out.println(curPos+"     "+newPos+"     "+distance);
		return angle/steerLock;
	}

	public static Double2ObjectSortedMap<Vector2D> toMap(double[] x,double[] y){
		if (x==null || y == null || x.length==0 || y.length == 0 || x.length!=y.length)
			return null;
		double len = 0;
		int n = x.length;
		Double2ObjectSortedMap<Vector2D> lMap = new Double2ObjectRBTreeMap<Vector2D>();
		Vector2D prev = new Vector2D(0,0);
		for (int i=0;i<n;++i){
			Vector2D p = new Vector2D(x[i],y[i]);
			lMap.put(len, p);
			len += p.distance(prev);
			prev = p;
		}
		return lMap;	
	}

	//this method return a matrix tranform from a point in prev co_ord to ed cord
	//where ed is dist away from prev
	public static AffineTransform transform(EdgeDetector prev,double dist,EdgeDetector ed){
		if (prev==null || ed==null) return null;
		AffineTransform at = new AffineTransform();
		double scale = ed.trackWidth / prev.trackWidth;
		at.scale(scale, 1);
		if (dist<=prev.straightDist){
			double ax = -ed.toLeftEdge()+prev.toLeftEdge();
			double ay = -dist;
			at.translate(ax, ay);
			return at;
		}
		return null;
	}	

	double[] xx;
	double[] yy;
	double miny=-1;


	public void startRecordingEdge(){
		long time = System.currentTimeMillis();
		recording = true;		
		prevEdge = edgeDetector;					

		highestPoint = edgeDetector.highestPoint;		
		highestPointOnOtherEdge = null;			
		raced = distRaced;		
		System.out.println(System.currentTimeMillis()-time);
	}

	int count=0;
	boolean followedPath = false;

	//p is assume to be a point on prev, convert now to ed
	//edge is -1 = left edge, 1: right edge, 0 : UNKNOWN
	//len is the len to point on the edge(specified by edge) on prev
	//hP is a point on ed ....
//	public Vector2D convertPoint(Vector2D p,double len,double nraced,int edge,Vector2D hP,int hEdge,EdgeDetector ed){
//		if (len>=99) return new Vector2D(hP);
//		double scale = edgeDetector.trackWidth/prevEdge.trackWidth;
//		double ax = -edgeDetector.toLeftEdge()+prevEdge.toLeftEdge();
//
//		if (edge==0)
//			return new Vector2D(p.x*scale+ax,p.y-nraced);
//
//		Vector2D tmp = null;
//
//		if (edge==-1){
//			if (ed.left==null) 
//				return new Vector2D(p.x*scale+ax,p.y-nraced);
//			else {
//				if (hP==null || edge!=hEdge) 
//					tmp = ed.left.estimatePointOnEdge(len-nraced, null);
//				else tmp = ed.left.estimatePointOnEdge(len-nraced, hP);				
//
////				System.out.println(tmp+"   "+hP);
//				if (tmp==null || tmp.distance(hP)<EPSILON) return new Vector2D(hP);
//				return tmp;
//			}
//		}
//
//		if (ed.right==null) 
//			return new Vector2D(p.x*scale+ax,p.y-nraced);
//		if (hP==null || edge!=hEdge) 
//			tmp = ed.right.estimatePointOnEdge(len-nraced, null);
//		else tmp = ed.right.estimatePointOnEdge(len-nraced, hP);
////		System.out.println(tmp+"   "+hP);
//		if (tmp==null || tmp.distance(hP)<EPSILON) return new Vector2D(hP);
//		return tmp;
//	}

	public void record(){
		/*if (!recording) return;
		centerOfTurn = null;		
		long time = System.currentTimeMillis();
		nraced = distRaced - raced;
		nraced = Math.round(nraced*10000.0d)/10000.0d;
//		double scale = edgeDetector.trackWidth/prevEdge.trackWidth;		
//		System.out.println(prevEdge.turn+"   "+edgeDetector.turn);
		if (prevEdge.turn*edgeDetector.turn!=-1 && prevEdge.straightDist-1>nraced) {
			edgeDetector.combine(prevEdge, nraced);						
		}		

//		Vector2D transform = new Vector2D(ax,0);

		System.out.println("**************** E"+distRaced+" "+nraced+" ****************");
//		if (distRaced>650) meta = 1;
		turn = edgeDetector.turn;
//		if (turn==MyDriver.UNKNOWN && prevEdge.turn!=STRAIGHT && prevEdge.turn!=UNKNOWN) turn = prevEdge.turn;
		if (turn==UNKNOWN) {
			prevEdge = edgeDetector;				
			raced = distRaced;		
			System.out.println(System.currentTimeMillis()-time);
			return;
		}

		Edge left = edgeDetector.left;
		Edge right = edgeDetector.right;
		Vector2D hh = edgeDetector.highestPoint;
		int highestPointEdge = 	edgeDetector.guessPointOnEdge(hh);

		if (left==null && right==null){
			highestPoint = hh;
			highestPointOnOtherEdge = null;
			prevEdge = edgeDetector;				
			raced = distRaced;		
			System.out.println(System.currentTimeMillis()-time);
			return;
		}


		if (hh!=null && highestPointEdge==-1 && left!=null){
			edgeDetector.left.append(hh);					
		} else if (hh!=null && highestPointEdge==1 && right!=null){
			edgeDetector.right.append(hh);			
		} 
		edgeDetector.estimateCurve(highestPointEdge);

		if (edgeDetector.center==null){
			highestPoint = hh;
			if (turn==TURNRIGHT && edgeDetector.right!=null)
				highestPointOnOtherEdge = edgeDetector.right.getHighestPoint();
			else if (turn==TURNLEFT && edgeDetector.left!=null)
				highestPointOnOtherEdge = edgeDetector.left.getHighestPoint();
		} else {
			Vector2D center = edgeDetector.center;
			double radiusR = edgeDetector.radiusR;
			double radiusL = edgeDetector.radiusL;


			double x0 = center.x;
			double y0 = center.y;
			Vector2D[] v = null;
			if (turn==TURNRIGHT && radiusR>0){//look for furthest point the right edge				
				v = Geom.ptTangentLine(0, 0, x0, y0, radiusR+W);
				Vector2D rHighest = (right==null) ? null : right.getHighestPoint();
				highestPointOnOtherEdge = (v==null) ? rHighest :  (v[0].x>v[1].x) ? v[1] : v[0];

				if (rHighest!=null && (highestPointOnOtherEdge.y<=rHighest.y || highestPointOnOtherEdge.x<rHighest.x))	highestPointOnOtherEdge = rHighest;				
				double[] rs = (highestPointOnOtherEdge==null ) ? null : Geom.getLineCircleIntersection(0, 0, highestPointOnOtherEdge.x, highestPointOnOtherEdge.y, x0, y0, radiusL-W);
				if (rs==null){ 
					highestPoint = hh;
				} else if (rs.length<4){
					highestPoint = new Vector2D(rs[0],rs[1]);
					if (hh!=null && highestPoint.y<=hh.y) highestPoint = hh;
				} else {
					Vector2D p1 = new Vector2D(rs[0],rs[1]);
					Vector2D p2 = new Vector2D(rs[2],rs[3]);
					highestPoint = (p1.y<p2.y) ? p2 : p1;
					if (hh!=null && highestPoint.y<=hh.y) highestPoint = hh;
				}				
			} else if (turn==TURNLEFT && radiusL>0){//look for furthest point the right edge
				v = Geom.ptTangentLine(0, 0, x0, y0, radiusL+W);
				Vector2D lHighest = (left==null) ? null : left.getHighestPoint();
				highestPointOnOtherEdge = (v==null) ? lHighest :  (v[0].x<v[1].x) ? v[1] : v[0];
				if (lHighest!=null && (highestPointOnOtherEdge.y<=lHighest.y || highestPointOnOtherEdge.x>lHighest.x)) highestPointOnOtherEdge = lHighest;							
				double[] rs = (highestPointOnOtherEdge==null ) ? null : Geom.getLineCircleIntersection(0, 0, highestPointOnOtherEdge.x, highestPointOnOtherEdge.y, x0, y0, radiusR-W);
				if (rs==null){ 
					highestPoint = hh;
				} else if (rs.length<4){
					highestPoint = new Vector2D(rs[0],rs[1]);
					if (hh!=null && highestPoint.y<=hh.y) highestPoint = hh;
				} else {
					Vector2D p1 = new Vector2D(rs[0],rs[1]);
					Vector2D p2 = new Vector2D(rs[2],rs[3]);
					highestPoint = (p1.y<p2.y) ? p2 : p1;
					if (hh!=null && highestPoint.y<=hh.y) highestPoint = hh;
				}				
			}
						
			Vector2D t = null;			
			if (turn==TURNRIGHT && radiusR>0){
				radiusR += W;
				radiusL -= W;
				t = new Vector2D(SIN_PI_4,-SIN_PI_4);
				radiusOfTurn = 3.414*(radiusL-0.707*radiusR);
				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusR));
			} else if (turn==TURNLEFT && radiusL>0){
				radiusL += W;
				radiusR -= W;
				t = new Vector2D(-SIN_PI_4,-SIN_PI_4);
				radiusOfTurn = 3.414*(radiusR-0.707*radiusL);
				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusL));
			}
						
			targetRadius = radiusOfTurn;
		}
		//Now looking for optimal point
		Vector2D cntr = null;
		if (centerOfTurn!=null && hh!=null){
			Vector2D t = hh.minus(centerOfTurn).normalized();
			optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));
			
		}
		Vector2D startTurnPoint = startTurnPoint();
		double rr=0;
		if (startTurnPoint!=null && optimalPoint!=null){
			double[] r = new double[3];
			if (Geom.getCircle(0, 0, startTurnPoint.x, startTurnPoint.y, optimalPoint.x, optimalPoint.y, r)){
				cntr=new Vector2D(r[0],r[1]);				
				rr = Math.sqrt(r[2]);
			}
		}
		System.out.println(cntr+"   "+optimalPoint+"    "+startTurnPoint+"   New");
		Vector2D hc = (centerOfTurn==null) ? null : new Vector2D(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
		if (highestPoint==null) highestPoint=hh;
		boolean draw = false;
		if (hc!=null && startTurnPoint!=null) 
			System.out.println((hc.distance(startTurnPoint))+"   "+hc.minus(startTurnPoint).angle()+"    Check coi");
		
		if (draw && (distRaced>-40 && distRaced<80) ){			
			EdgeDetector.drawEdge(edgeDetector, "E"+distRaced+" "+radiusOfTurn+"a");
			XYSeries series = new XYSeries("Curve");
			if (centerOfTurn!=null) {
				series.add(centerOfTurn.x,centerOfTurn.y);
				series.add(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
			}
			if (highestPoint!=null) series.add(highestPoint.x,highestPoint.y);
			if (hc!=null) series.add(hc.x,hc.y);
			for (int i=0;i<edgeDetector.x.size();++i)
				series.add(edgeDetector.x.get(i),edgeDetector.y.get(i));

			if (edgeDetector.center!=null){
				if (edgeDetector.radiusL>0 && edgeDetector.radiusL<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusL, series);
				if (edgeDetector.radiusR>0 && edgeDetector.radiusR<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusR, series);
			}
			if (centerOfTurn!=null && radiusOfTurn<550) TrackSegment.circle(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, series);
			if (startTurnPoint!=null) series.add(startTurnPoint.x,startTurnPoint.y);
			if (optimalPoint!=null) series.add(optimalPoint.x,optimalPoint.y);
			if (cntr!=null && rr<550) TrackSegment.circle(cntr.x, cntr.y, rr, series);

			EdgeDetector.drawEdge(series, "E"+distRaced+" "+rr+"b");
//			if (left!=null) Edge.drawEdge(left, "E"+distRaced+" "+nraced+"b");
//			if (right!=null) Edge.drawEdge(right, "E"+distRaced+" "+nraced+"c");
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			meta=1;
		}



		//		if (distRaced>450) meta=1;

		prevEdge = edgeDetector;				
		raced = distRaced;		
		System.out.println(System.currentTimeMillis()-time);	
		//*/
	}

	public double radiusAtAngle(double angle){
		return L/Math.sin(angle);
	}

	public double angleWithRadius(double r){
		return Math.asin(L/r);
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

	public double maxAngleAtSpeed(double speed){		
		double ds = speed/3.6*0.02;
		double r = radiusAtSpeed(speed);
		return ds/r;
	}

	public double maxAllowedSpeedAtRadius(double x){			
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


	double prevSteer = 0;

	public static double stoppingDistance(double speed){
		double x = speed/3.6;		
		double m = estimateMu(speed);
		return (x*x/2/G*m);
	}

	double desireSpeed = 0;


	public double distanceToBrake(double speed,double targetspeed){
		return stoppingDistance(speed)-stoppingDistance(targetspeed)+2;
	}
	
	
	public double gotoPoint(Vector2D point){
		if (point==null) return Double.NaN;
		double slip = (wheelSpinVel[2]*wheelRadius[2]-wheelSpinVel[0]*wheelRadius[0]);		
		double alpha = (point==null) ? curAngle : curAngle-PI_2+point.angle();
		double distance = point.length();
		distance = Math.min(200, Math.max(distance, 0));
		if (prevEdge.turn!=edgeDetector.turn){
			isTurning = false;
			return 0;
		}
		
		double sign = (alpha<0) ? -1 : 1;		
//		Vector2D t = centerOfTurn.minus(point).orthogonal().normalized();
//		if (t.y<0) t.y=-t.y;	
		double pos = sign * curPos;
		Vector2D startTurnPoint = startTurnPoint();		
		double maxB = Math.abs(curAngle-PI_2+new Vector2D(trackWidth,point.y).angle());
		double maxA = Math.max(Math.abs(curAngle-PI_2+point.minus(startTurnPoint).angle()),maxB);	
		double far = (startTurnPoint.y<=5) ? point.length()+L : point.distance(startTurnPoint)+L;
		System.out.println("Cond : "+startTurnPoint+"   "+maxA+"   "+alpha);
		if ((centerOfTurn.y<=5) && !isTurning)
			isTurning = true;
		if (isTurning) 
			turning.setValue(1);
		else turning.setValue(0);
		
		double sX = Math.min(Math.max(speedX, 0),360); 		 
		double speedy = speedY * sign;
		double sY = Math.min(Math.max(speedy, -360),360);
		double brakeDistance = distanceToBrake(speedX, desireSpeed);
		angle.setValue(alpha*sign);
		dist.setValue(distance);
		this.pos.setValue(pos);
		speedx.setValue(sX);
		this.speedy.setValue(sY);
		this.currentAngle.setValue(sign*curAngle);
		this.slipSpeed.setValue(slip);
		
//		MembershipFunction slipMargin = slipSpeed.getMembershipFunction("Dangerous"); 
//		slipMargin.setParameter(8,SLIP_LIMIT);
		
		
		MembershipFunction smallAngle = angle.getMembershipFunction("Small"); 
		smallAngle.setParameter(3,maxA);
		smallAngle.setParameter(2,0.9*maxA);
		
		MembershipFunction farDist = dist.getMembershipFunction("Far"); 
		farDist.setParameter(3,far+2*brakeDistance);
		farDist.setParameter(2,far+brakeDistance*1.5);
		farDist.setParameter(1,far+1);
		farDist.setParameter(0,far);
		
		MembershipFunction veryFarDist = dist.getMembershipFunction("VeryFar"); 
//		veryFarDist.setParameter(3,far*2);
//		veryFarDist.setParameter(2,1.5*far);
		veryFarDist.setParameter(1,far+2*brakeDistance);
		veryFarDist.setParameter(0,far+1.5*brakeDistance);


				
		fuzzyRuleSet.evaluate();
//		System.out.println(fuzzyRuleSet);
//		if (slip>SLIP_LIMIT) System.out.println(fuzzyRuleSet);
//
//		for (Object r : fuzzyRuleSet.getRules()) {
//			FuzzyRule rule = (FuzzyRule)r;
//			if (rule.getDegreeOfSupport()>0) System.out.println(rule);
//			System.out.println(dist);
//			System.out.println(angle);
//			System.out.println(pos);
//		}//
//
		double steer = sign*steering.getLatestDefuzzifiedValue();
		if (Double.isNaN(steer)){
//			System.out.println(fuzzyRuleSet);
			fuzzyRuleSet.reset();
			return steer;
		}

		System.out.println(steer+"   Steer  "+alpha);
//		steer = curAngle - steer;
		steer = Math.min(Math.max(steer, -steerLock),steerLock);
		
		
		return steer/steerLock;

	}
	
	public double brakeControl(Vector2D point){
		if (point==null) return Double.NaN;
		System.out.println("Here");
		double slip = (wheelSpinVel[2]*wheelRadius[2]-wheelSpinVel[0]*wheelRadius[0]);		
		double alpha = (point==null) ? curAngle : curAngle-PI_2+point.angle();
		double distance = point.length();
		distance = Math.min(200, Math.max(distance, 0));		
		
		double sign = (alpha<0) ? -1 : 1;		
		Vector2D t = centerOfTurn.minus(point).orthogonal().normalized();
		if (t.y<0) t.y=-t.y;	
		double pos = sign * curPos;
		Vector2D startTurnPoint = startTurnPoint();		
		double maxB = Math.abs(curAngle-PI_2+new Vector2D(trackWidth,highestPoint.y).angle());
		double maxA = Math.max(Math.abs(curAngle-PI_2+edgeDetector.highestPoint.minus(startTurnPoint).angle()),maxB);
		double far = (startTurnPoint.y<=5) ? edgeDetector.highestPoint.length()+L : edgeDetector.highestPoint.distance(startTurnPoint)+L;
		double sX = Math.min(Math.max(speedX, 0),360); 		 
		double speedy = speedY * sign;
		double sY = Math.min(Math.max(speedy, -360),360);
		double brakeDistance = distanceToBrake(speedX, desireSpeed);
		double diff = speedX - desireSpeed;
		angleB.setValue(alpha*sign);		
		this.posB.setValue(pos);
		speedxB.setValue(sX);
		this.speedyB.setValue(sY);
		this.currentAngleB.setValue(sign*curAngle);
		this.slipSpeedB.setValue(slip);
		this.difference.setValue(diff);
		
		if (startTurnPoint.y>5)//not turn yet
			distance = startTurnPoint.length();			
		else distance = point.length();
		distB.setValue(distance);
		double spaceRemained = distance - brakeDistance;
		this.spaceRemain.setValue(spaceRemained);
		
		MembershipFunction smallAngle = angleB.getMembershipFunction("Small"); 
		smallAngle.setParameter(3,maxA);
		smallAngle.setParameter(2,0.9*maxA);
		
		MembershipFunction farDist = distB.getMembershipFunction("Far"); 
		farDist.setParameter(3,far+2*brakeDistance);
		farDist.setParameter(2,far+brakeDistance*1.5);
		farDist.setParameter(1,far+1);
		farDist.setParameter(0,far);
		
		MembershipFunction veryFarDist = distB.getMembershipFunction("VeryFar"); 
//		veryFarDist.setParameter(3,far*2);
//		veryFarDist.setParameter(2,1.5*far);
		veryFarDist.setParameter(1,far+2*brakeDistance);
		veryFarDist.setParameter(0,far+1.5*brakeDistance);


				
		fuzzyRuleSetB.evaluate();
//		System.out.println(fuzzyRuleSet);
//		if (slip>SLIP_LIMIT) System.out.println(fuzzyRuleSet);
//
//		for (Object r : fuzzyRuleSetB.getRules()) {
//			FuzzyRule rule = (FuzzyRule)r;
//			if (rule.getDegreeOfSupport()>0) System.out.println(rule);
//			System.out.println(dist);
//			System.out.println(angle);
//			System.out.println(pos);
//		}//
//
//		System.out.println(fuzzyRuleSetB);
		double speed = brake.getLatestDefuzzifiedValue();
		if (Double.isNaN(speed)){
			
			fuzzyRuleSetB.reset();
			return speed;
		}

		System.out.println(speed+"   Brake  ");
//		steer = curAngle - steer;		
		
		
		return speed;

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
		
	public double fuzzySteering(){		
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;	
			isTurning = false;
			return curAngle/steerLock;
		}		
//		maxSpeed = Double.MAX_VALUE;

		if (centerOfTurn==null || radiusOfTurn<0) return prevSteer;
		
//		double sign = (turn==TURNRIGHT) ? -1 : 1;
//		Vector2D t = new Vector2D(radiusOfTurn*sign,0);
//		Vector2D point = null;		
		
		
		Vector2D hh = edgeDetector.highestPoint;
		double steer = Double.NaN;
//		double y = findPoint(centerOfTurn.x, centerOfTurn.y, radiusOfTurn,0 );
//		if (Double.isNaN(y) || y<centerOfTurn.y) y = centerOfTurn.y;
//		System.out.println("Center  "+centerOfTurn+"   "+y);
//		System.out.println(y);
//		System.out.println("Radius  "+radiusOfTurn+"   "+centerOfTurn.length());
//		
		desireSpeed = maxAllowedSpeedAtRadius(radiusOfTurn);
//		double brakeDist = stoppingDistance(speed);
//		double slip = (wheelSpinVel[2]*wheelRadius[2]-wheelSpinVel[0]*wheelRadius[0]);
//		System.out.println("Slip   "+slip);
//		System.out.println(brakeDist+"  Brake dist");
		
//		if (y>25){
//			double d = (turn==TURNLEFT) ? curPos-0.96 : curPos+0.96;
//		System.out.println("Chuoi "+curPos+"  "+d);
//			if (Math.abs(d)<0.05) d=0;
//			if (sign*curPos<0 || sign*curPos<-0.1)
//				steer = curAngle/steerLock+d/10;
//			prevSteer = steer;
//			return prevSteer;
//						
//		} else if (y>brakeDist/3){//Not turn yet, prepare the speed		
////			double a = PI_2 - Math.abs(highestPoint.minus(new Vector2D(0,y)).angle());
////			double r = radiusAtAngle(a);
////			System.out.println(a+"   "+r);			
//			System.out.println(desireSpeed+" speed  "+speed+"   "+maxSpeed);
//			if (speed<desireSpeed-5 || (desireSpeed<speed && distanceToBrake(speed, desireSpeed)<y-4) ){
//				maxSpeed = Double.MAX_VALUE;
//			} else if (speed>desireSpeed+40){
//				if (slip>SLIP_LIMIT) 
//					maxSpeed=speed;
//				else maxSpeed = 0;				
////				return (slip>0 && slip<SLIP_LIMIT) ? sign : 0;
//				prevSteer = 0;
//				return 0;
//			} else if (maxSpeed < desireSpeed-10){
//				maxSpeed = Double.MAX_VALUE;
//				
//			} else maxSpeed = desireSpeed;
//			steer = curAngle;
//		} else if (y<=brakeDist/3){//turning
//			maxSpeed = desireSpeed;
//						
//			Vector2D center = edgeDetector.center;
//			Vector2D t = highestPointOnOtherEdge.minus(center).normalized();
//			Vector2D p = center.plus(t.times((edgeDetector.radiusL+edgeDetector.radiusR)*0.5));
//			
//			if (desireSpeed<160 && speed>120){
//				steer = gotoPoint(highestPoint);
//				System.out.println("Steer nhieu  "+desireSpeed+"   "+highestPoint+"   "+edgeDetector.firstIndexMax);
////			} else if (desireSpeed>185){
////				steer = curAngle;
////				System.out.println("Steer rat it "+desireSpeed+"   "+highestPoint+"   "+edgeDetector.firstIndexMax);
////			} 
//			} else {
//				steer = gotoPoint(p);
//				System.out.println("Steer  it "+desireSpeed+"   "+highestPoint+"   "+edgeDetector.firstIndexMax);
//			}
//		}
		
		
		steer = gotoPoint(hh);
		maxSpeed = desireSpeed;
//		maxSpeed =120;
//		if (speedX<desireSpeed)
//			maxSpeed = Double.MAX_VALUE;
//		else maxSpeed = brakeControl(hh);	
//		maxSpeed = desireSpeed+20;
	
//		double rLim = centerOfTurn.length();
//		if (radiusOfTurn<rLim){
//			maxSpeed = speed;
//			if ((wheelSpinVel[2]*wheelRadius[2]-wheelSpinVel[0]*wheelRadius[0])>5)
//				maxSpeed = 0;
//		}
		
//		if (slip>SLIP_LIMIT || slip<0)
//			if (speed<150)
//				maxSpeed=speed;
//			else maxSpeed = speed*0.9;
				
		if (steer*prevSteer<0){
			if (Math.abs(prevSteer)>PI_2/5)
				prevSteer /= 2;
			else prevSteer=0;
			return prevSteer;
		}			
		
		if (Double.isNaN(steer)){			
			followedPath = false;
			recording = false;			
			steer =  prevSteer;
		}
		System.out.println("Speed  "+" "+speedX+"  "+speedY+"   "+maxSpeed+"   "+desireSpeed);
		prevSteer = steer/steerLock;
		return steer/steerLock;
	}
	
	public double angleToPoint(Vector2D point){
		return curAngle-PI_2+point.angle();
	}

	public static double estimateMu(double speed){		
		double e=0.00025;
		if (speed<=20)
			return 0.577-(Math.random()+1)/2*(speed-20)*0.01;
		else if (speed<=40) return 0.502-(Math.random()+1)*(speed-40)*e;
		else if (speed<=60) return 0.473-(Math.random()+1)*(speed-60)*e;
		else if (speed<=80) return 0.452-(Math.random()+1)*(speed-80)*e;
		else if (speed<=95) return 0.43-(Math.random()+1)*(speed-95)*e;
		else if (speed<=100) return 0.427-(Math.random()+1)*(speed-100)*e;
		else if (speed<=120) return 0.406-(Math.random()+1)*(speed-120)*e;
		else if (speed<=140) return 0.385-(Math.random()+1)*(speed-140)*e;
		else if (speed<=160) return 0.366-(Math.random()+1)*(speed-160)*e;
		else if (speed<=180) return 0.346-(Math.random()+1)*(speed-180)*e;
		else if (speed<=190) return 0.336-(Math.random()+1)*(speed-190)*e;		
		return 0.336-(Math.random()+1)/2*(speed-190)*e;
	}



	double start = -1;
	double SPEED=120;
	double targetSpeed = 100;
	boolean go = false;
	double startPos =0;
	double beta = steerLock;
	@Override
	double getSteer() {
		// TODO Auto-generated method stub
		//		jf.setVisible(true);		
		if (meta==1)
			return 0;
		
//		maxSpeed = 100;
//		if (Math.abs(maxSpeed-speedX)<0.5 && !go){
//			start=distRaced;
//			startPos = curPos; 		
//			System.out.println(curAngle+"   "+beta);
//			go = true;
//			return 0;
//		}
//		
//		if (Math.abs(curAngle)>=Math.PI/2-0.1 && go){
//			double d = distRaced-start;
//			double lateral = (startPos-curPos)*18/2;
//			System.out.println(d+"   "+lateral+"   "+beta+"   "+Math.atan2(lateral, d));
//			go =false;
//			meta = 1;
//		}
//
//		if (go) return beta/steerLock;
//		if (true) return curAngle/steerLock-(1-curPos)/10;
//		if (speedX>SPEED){
//			brake = true;
//			start = distRaced;
//			maxSpeed = 0;
//			return 1;
//		}
//
////		System.out.println(speedX+"   "+speedY+"  Slip speed:  "+querySlipSpeed());
////		System.out.println("Accel : "+queryAcceleration(speed));
//				
//		if (brake && speedX<=targetSpeed){
//		double raced = distRaced-start;
//		double x = SPEED/3.6;
////		double mu = raced / (x*x/2/G);		
//		double m = estimateMu(SPEED);
//		double m0 = estimateMu(targetSpeed);
//		double x0 = speed/3.6;
//		System.out.println(speedX+"    "+speedY+"    "+curAngle+"  "+raced+"   "+((x*x/2/G*m)-(x0*x0/2/G*m0))+"   "+(x*x/2/G*m)+"   "+(x0*x0/2/G*m0));
//		System.out.println(distRaced+"  End Braking");
//		maxSpeed = targetSpeed;
//		brake = false;
//		return 1;
//		}		
//
//		if (brake){		
//		maxSpeed = 0;
//		return 1;
//		}

//		if (speedX>150){
//		maxSpeed = Double.MAX_VALUE;
//		return 0;
//		}
		
		if (trackWidth<=0)
			return curAngle/steerLock+curPos/10;

		if (recording) 
			record();
		else turn = edgeDetector.turn;

		if (followedPath){			
			double steer =  fuzzySteering();			
			return steer;
		}

//		if (distRaced>870 ) {
//		meta=1;
//		try{
//		Thread.sleep(5000);
//		} catch (Exception e){

//		}
//		return 0;

//		}
		if (!followedPath && (curPos<= -(trackWidth/workingWidth) || curPos>=(trackWidth/workingWidth))){
			recording = false;
			followedPath = false;			
			return curAngle/steerLock+curPos/10;
		}


		if (turn==STRAIGHT){
			recording = false;
			isTurning = false;
			maxSpeed = Double.MAX_VALUE;
			return curAngle/steerLock+curPos/15;
		}
		if (!recording) {
			startRecordingEdge();
			return curAngle/steerLock;
		}
		if (turn==UNKNOWN){
			maxSpeed = Double.MAX_VALUE;			
			return (curAngle+random.nextDouble()*0.05)/steerLock;
		}

		return fuzzySteering();

//		Left + Right -
//		return 0;
	}

	@Override
	public int drive(int[] sensors, int len) {
		// TODO Auto-generated method stub
		return 0;
	}


}
