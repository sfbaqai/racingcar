/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.lang.MutableString;

import java.awt.geom.AffineTransform;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

import org.jfree.data.xy.XYSeries;

import com.graphbuilder.geom.Geom;



/**
 * @author kokichi3000
 *
 */
public class MyDriver extends SimpleDriver {
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
	public double trackWidth =-1;
	double workingWidth = 0;
	double leftX;
	double rightX;
	double middleX;
	double ox=0;
	double raced = 0;	
	boolean recording = false;	
	int turn;

	Vector2D highestPoint = null;
	Vector2D highestPointOnOtherEdge = null;
	FuzzyRuleSet fuzzyRuleSet;
	net.sourceforge.jFuzzyLogic.rule.Variable angle,dist,speedx,speedy,pos,steering;
	double nraced = 0;
	double toMiddle = 0;
	double mu = 0;
	double mass = carmass;	

	/**
	 * 
	 */
	EdgeDetector prevEdge = null;
	EdgeDetector edgeDetector = new EdgeDetector();
	public MyDriver() {
		String fileName = "fcl/steering.fcl";//name of the system
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

	}

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
	@Override
	double getAccel() {		
		// TODO Auto-generated method stub
		// checks if car is out of track
		if (curPos <= 1 && curPos >= -1)
		{
//			double targetSpeed = maxSpeed;

			// accel/brake command is expontially scaled w.r.t. the difference between target speed and current one
//			return 2/(1+Math.exp(speed - maxSpeed)) - 1;

			double gr = (gear<0) ? gearRatio[GEAR_OFFSET] : gearRatio[GEAR_OFFSET+gear];
			double rm = enginerpmRedLine;
			if (maxSpeed==speed) return 0;
			if (maxSpeed>speed+1) return 1;
			return (maxSpeed-speedX)/speedAtRpm(rm, gr);
		} else {			
			if (speed>150)
				return 0;
			if (speed<100)
				return 0.5;
			return 0.5; // when out of track returns a moderate acceleration command
		}

	}

	@Override
	int getGear() {
		// TODO Auto-generated method stub	    
		// if gear is 0 (N) or -1 (R) just return 1 
		if (gear<1)
			return 1;

//		double gr_up = gearRatio[gear + GEAR_OFFSET];
//		double omega = enginerpmRedLine/gr_up/DIFFERENTIAL_RATIO/3.6;
//		double wr = wheelRadius[2];

//		System.out.println(speedAtRpm(gearUp[gear-1], gr_up)+"   chuoi  "+gear);
//		if (omega*wr*SHIFT < speedX) 
//		return gear + 1;
		// check if the RPM value of car is greater than the one suggested 
		// to shift up the gear from the current one     
		if (gear <6 && rpm >= gearUp[gear-1])
			return gear + 1;		
		else
//			check if the RPM value of car is lower than the one suggested 
//			to shift down the gear from the current one
			if (gear > 1 && rpm <= gearDown[gear-1])
				return gear - 1;
//		otherwhise keep current gear
//		double gr_down = gearRatio[gear + GEAR_OFFSET - 1];
//		omega = enginerpmRedLine/gr_down/DIFFERENTIAL_RATIO/3.6;
//		if (gear > 1 && omega*wr*SHIFT > speedX + SHIFT_MARGIN) 
//		return gear - 1;
		return gear;
	}




	public void init(){
		edgeDetector = new EdgeDetector(carState);
//		edgeDetector.init(carState);
		if (edgeDetector.trackWidth<=0 && prevEdge!=null)
			edgeDetector.trackWidth = prevEdge.trackWidth;
		trackWidth = edgeDetector.getTrackWidth();//must keep this line
		workingWidth = trackWidth-2*W;
		curPos *= trackWidth/workingWidth;
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
	public Vector2D convertPoint(Vector2D p,double len,double nraced,int edge,Vector2D hP,int hEdge,EdgeDetector ed){
		if (len>=99) return new Vector2D(hP);
		double scale = edgeDetector.trackWidth/prevEdge.trackWidth;
		double ax = -edgeDetector.toLeftEdge()+prevEdge.toLeftEdge();

		if (edge==0)
			return new Vector2D(p.x*scale+ax,p.y-nraced);

		Vector2D tmp = null;

		if (edge==-1){
			if (ed.left==null) 
				return new Vector2D(p.x*scale+ax,p.y-nraced);
			else {
				if (hP==null || edge!=hEdge) 
					tmp = ed.left.estimatePointOnEdge(len-nraced, null);
				else tmp = ed.left.estimatePointOnEdge(len-nraced, hP);				

//				System.out.println(tmp+"   "+hP);
				if (tmp==null || tmp.distance(hP)<EPSILON) return new Vector2D(hP);
				return tmp;
			}
		}

		if (ed.right==null) 
			return new Vector2D(p.x*scale+ax,p.y-nraced);
		if (hP==null || edge!=hEdge) 
			tmp = ed.right.estimatePointOnEdge(len-nraced, null);
		else tmp = ed.right.estimatePointOnEdge(len-nraced, hP);
//		System.out.println(tmp+"   "+hP);
		if (tmp==null || tmp.distance(hP)<EPSILON) return new Vector2D(hP);
		return tmp;
	}

	public void record(){
		if (!recording) return;
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
//		if ( (distRaced>145 && distRaced<170) ){
//		if (left!=null) {
//		System.out.println(left.radius+"   "+left.center);
//		if (hh!=null && left!=null && left.center!=null) System.out.println(hh.distance(left.center)-left.radius);
//		}
//		if (right!=null && right!=null && right.center!=null) {
//		System.out.println(right.radius+"   "+right.center);
//		if (hh!=null) System.out.println(hh.distance(right.center)-right.radius);
//		}
//		System.out.println(highestPointEdge+" asdsa");			
//		}

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
//		System.out.println(edgeDetector.center+"   "+edgeDetector.radiusL+"   "+edgeDetector.radiusR+"   radius");

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
				v = Geom.ptTangentLine(0, 0, x0, y0, radiusR);
				Vector2D rHighest = (right==null) ? null : right.getHighestPoint();
				highestPointOnOtherEdge = (v==null) ? rHighest :  (v[0].x>v[1].x) ? v[1] : v[0];

				if (rHighest!=null && (highestPointOnOtherEdge.y<=rHighest.y || highestPointOnOtherEdge.x<rHighest.x))	highestPointOnOtherEdge = rHighest;
				double[] rs = Geom.getLineCircleIntersection(0, 0, highestPointOnOtherEdge.x, highestPointOnOtherEdge.y, x0, y0, radiusL);
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
				v = Geom.ptTangentLine(0, 0, x0, y0, radiusL);
				Vector2D lHighest = (left==null) ? null : left.getHighestPoint();
				highestPointOnOtherEdge = (v==null) ? lHighest :  (v[0].x<v[1].x) ? v[1] : v[0];

				if (lHighest!=null && (highestPointOnOtherEdge.y<=lHighest.y || highestPointOnOtherEdge.x>lHighest.x)) highestPointOnOtherEdge = lHighest;
				double[] rs = Geom.getLineCircleIntersection(0, 0, highestPointOnOtherEdge.x, highestPointOnOtherEdge.y, x0, y0, radiusR);
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

		}				


		boolean draw = false;
		if (draw && (distRaced>320 && distRaced<370) ){			
			EdgeDetector.drawEdge(edgeDetector, "E"+distRaced+" "+nraced+"a");
			XYSeries series = new XYSeries("Curve");
			if (highestPoint!=null) series.add(highestPoint.x,highestPoint.y);
			if (highestPointOnOtherEdge!=null) series.add(highestPointOnOtherEdge.x,highestPointOnOtherEdge.y);
			for (int i=0;i<edgeDetector.x.size();++i)
				series.add(edgeDetector.x.get(i),edgeDetector.y.get(i));

			if (edgeDetector.center!=null){
				if (edgeDetector.radiusL>0 && edgeDetector.radiusL<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusL, series);
				if (edgeDetector.radiusR>0 && edgeDetector.radiusR<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusR, series);
			}

			EdgeDetector.drawEdge(series, "E"+distRaced+" "+nraced+"d");
//			if (left!=null) Edge.drawEdge(left, "E"+distRaced+" "+nraced+"b");
//			if (right!=null) Edge.drawEdge(right, "E"+distRaced+" "+nraced+"c");
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			meta=1;
		}

		prevEdge = edgeDetector;				
		raced = distRaced;		
		System.out.println(System.currentTimeMillis()-time);	
	}

	public double radiusAtAngle(double angle){
		return L/Math.sin(angle);
	}

	public double angleWithRadius(double r){
		return Math.asin(L/r);
	}

	public double maxAngleAtSpeed(double speed){		
		double ds = speed*3.6;
		return Math.asin(L/ds*ds/2/G);
	}

	public double maxAllowedSpeedAtRadius(double radius){			
		return Math.sqrt(G*radius*mu*2)*3.6;
	}


	double prevSteer = 0;

	public static double stoppingDistance(double speed){
		double x = speed/3.6;		
		double m = estimateMu(speed);
		return (x*x/2/G*m);
	}

	double desireSpeed = 0;
	public Vector2D chooseWp(){
		if (highestPoint==null && highestPointOnOtherEdge==null)
			return null;

		if (highestPointOnOtherEdge==null)
			return highestPoint;

//		double safeDistance = stoppingDistance(speedX);
		Vector2D hh = edgeDetector.highestPoint;
		if (hh==null) return highestPointOnOtherEdge;
		Vector2D p = hh.plus(highestPointOnOtherEdge).times(0.5);
		double[] r = new double[3];
		boolean isCircle = Geom.getCircle(p.x, p.y, highestPoint.x, highestPoint.y, 0, 0, r);
//		System.out.println(highestPoint+"   "+highestPointOnOtherEdge);
		if (isCircle){
			double radius = Math.sqrt(r[2]);
			desireSpeed = maxAllowedSpeedAtRadius(radius);			
		}
		return p.scale(workingWidth/trackWidth, 1);
//		return null;
	}


	public double distanceToBrake(double speed,double targetspeed){
		return stoppingDistance(speed)-stoppingDistance(targetspeed);
	}
	
	public double gotoPoint(Vector2D point){
		if (point==null) return Double.NaN;		

		double alpha = (point==null) ?0 : PI_2-point.angle();
//		double distance = point.length();
		double h = point.y/2;
		double sign = (alpha<0) ? -1 : 1;


		if (desireSpeed<=speedX-5 ){
//			System.out.println(speed+"   "+desireSpeed);
//			System.out.println(distanceToBrake(speed, desireSpeed)+"   "+h);
			if (distanceToBrake(speedX, desireSpeed) >= h){
				maxSpeed = 0;
				if (curAngle-alpha>-0.001) return -sign;
			}
		} else {
			maxSpeed = desireSpeed;							
		}
		System.out.println(desireSpeed);
		return (curAngle-alpha)/steerLock;

//		if (desireSpeed<speed-2){
//		maxSpeed = 0;
//		if (Math.abs(curAngle-alpha)>0.05) return sign;
//		} else if (desireSpeed>speed+2){
//		maxSpeed = Double.MAX_VALUE;
//		} else maxSpeed = desireSpeed;
//		double pos = sign * curPos;
//		double maxA = maxAngleAtSpeed(speedX)-curAngle;
//		double speedy = speedY * sign;
//		angle.setValue(alpha*sign);
//		dist.setValue(distance);
//		this.pos.setValue(pos);
//		speedx.setValue(speedX);
//		this.speedy.setValue(speedy);

//		angle.getMembershipFunction("Small").setParameter(3,maxA);
//		angle.getMembershipFunction("Small").setParameter(2,0.9*maxA);


//		fuzzyRuleSet.evaluate();

//		for (Object r : fuzzyRuleSet.getRules()) {
//		FuzzyRule rule = (FuzzyRule)r;
//		if (rule.getDegreeOfSupport()>0) System.out.println(rule);
//		}//

//		double steer = sign*steering.getLatestDefuzzifiedValue();
//		if (Double.isNaN(steer)){		
//		return steer;
//		}

////		System.out.println(steer+"   "+alpha+"   "+highestPoint+"   "+curAngle);
//		steer = curAngle - steer;
//		steer = Math.max(steer, -steerLock);
//		steer = Math.min(steer, steerLock);


//		return steer/steerLock;

	}
	public double fuzzySteering(){		
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;			
			return curAngle/steerLock;
		}		
//		maxSpeed=120;

		if (highestPoint==null) return prevSteer;
		Vector2D point = chooseWp();				
		double steer = gotoPoint(point);
		if (Double.isNaN(steer))
			return prevSteer;
		prevSteer = steer/steerLock;
		return steer/steerLock;
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


	boolean brake =false;
	double start = -1;
	double SPEED=90;
	double targetSpeed = 0;
	double getSteer() {
		// TODO Auto-generated method stub
		//		jf.setVisible(true);		
		if (meta==1)
			return 0;

//		System.out.println(speedX+"   "+speedY+"  Slip speed:  "+querySlipSpeed());
//		System.out.println("Accel : "+queryAcceleration(speed));
//		if (speedX>SPEED && !brake) {			
//		start = distRaced;
//		System.out.println(distRaced+" Start Braking");
//		brake = true;
//		maxSpeed = 0;
//		return 1;
//		}

//		if (brake && speedX<=targetSpeed+5){
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

//		if (brake){
//		System.out.println(speedX+"   "+speedY+"  Slip speed:  "+querySlipSpeed());
//		maxSpeed = 0;
//		return 1;
//		}

//		if (speedX>150){
//		maxSpeed = Double.MAX_VALUE;
//		return 0;
//		}

		if (trackWidth<=0)
			return curAngle/steerLock;

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
			maxSpeed = Double.MAX_VALUE;
			return curAngle/steerLock+curPos/15;
		}
		if (!recording) {
			startRecordingEdge();
			return curAngle/steerLock;
		}
		if (turn==UNKNOWN){
			maxSpeed = Double.MAX_VALUE;			
			return (curAngle+Math.random()*0.05)/steerLock;
		}

		return fuzzySteering();

//		Left + Right -
//		return 0;
	}


}
