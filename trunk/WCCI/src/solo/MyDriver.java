/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.lang.MutableString;

import java.awt.geom.AffineTransform;

import net.sourceforge.jFuzzyLogic.FIS;
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
	public final static double mass = 1050;
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

	double filterABS(double brake)
	{
		// convert speed to m/s
		double speed = speedX / 3.6;
		// when spedd lower than min speed for abs do nothing
		if (speed < absMinSpeed)
			return brake;

		// compute the speed of wheels in m/s
		double slip = 0.0d;	    
		for (int i = 0; i < 4; i++)
		{
			slip += wheelSpinVel[i] * wheelRadius[i];
		}
		// slip is the difference between actual speed of car and average speed of wheels
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
//			// reading of sensor at +10 degree w.r.t. car axis
//			double rxSensor=tracks[10];
//			// reading of sensor parallel to car axis
//			double cSensor=cs.getTrack(9);
//			// reading of sensor at -10 degree w.r.t. car axis
//			double sxSensor=cs.getTrack(8);
//			if (Math.abs(speedY)>23) return 0;
			double targetSpeed = maxSpeed;

//			// track is straight and enough far from a turn so goes to max speed
//			if (cSensor>maxSpeedDist || (cSensor>=rxSensor && cSensor >= sxSensor))
//			targetSpeed = maxSpeed;
//			else
//			{
//			// approaching a turn on right
//			if(rxSensor>sxSensor)
//			{
//			// computing approximately the "angle" of turn
//			double h = cSensor*sin10;
//			double b = rxSensor - cSensor*cos10;
//			double sinAngle = b*b/(h*h+b*b);
//			// estimate the target speed depending on turn and on how close it is
//			targetSpeed = maxSpeed*(cSensor*sinAngle/maxSpeedDist);
//			}
//			// approaching a turn on left
//			else
//			{
//			// computing approximately the "angle" of turn
//			double h = cSensor*sin10;
//			double b = sxSensor - cSensor*cos10;
//			double sinAngle = b*b/(h*h+b*b);
//			// estimate the target speed depending on turn and on how close it is
//			targetSpeed = maxSpeed*(cSensor*sinAngle/maxSpeedDist);
//			}

//			}

			if (Math.abs(curPos)>0.9) return 0; 
			// accel/brake command is expontially scaled w.r.t. the difference between target speed and current one
			return 2/(1+Math.exp(speedX - targetSpeed)) - 1;
		} else {
			double speed = Math.sqrt(speedX*speedX+speedY*speedY);
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
		// check if the RPM value of car is greater than the one suggested 
		// to shift up the gear from the current one     
		if (gear <6 && rpm >= gearUp[gear-1])
			return gear + 1;
		else
			// check if the RPM value of car is lower than the one suggested 
			// to shift down the gear from the current one
			if (gear > 1 && rpm <= gearDown[gear-1])
				return gear - 1;
			else // otherwhise keep current gear
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
		if (distRaced>650) meta = 1;
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
		edgeDetector.estimateCurve();
		
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
				
				if (rHighest!=null && (highestPointOnOtherEdge.y<=rHighest.y || highestPointOnOtherEdge.x<rHighest.x-1))	highestPointOnOtherEdge = rHighest;
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
				
				if (lHighest!=null && (highestPointOnOtherEdge.y<=lHighest.y || highestPointOnOtherEdge.x>lHighest.x+1)) highestPointOnOtherEdge = lHighest;
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


		if ( (distRaced>62 && distRaced<650) ){			
			EdgeDetector.drawEdge(edgeDetector, "E"+distRaced+" "+nraced+"a");
//			ObjectArrayList<Vector2D> edges = ObjectArrayList.wrap(edgeDetector.getEdges(),edgeDetector.numpoint);
//			if (highestPoint!=null) edges.add(highestPoint);
//			if (highestPointOnLeftEdge!=null) edges.add(highestPointOnLeftEdge);
//			if (highestPointOnRightEdge!=null) edges.add(highestPointOnRightEdge);
//			EdgeDetector.drawEdge(edges, "E"+distRaced+" "+nraced+"b");
			XYSeries series = new XYSeries("Curve");
			if (highestPoint!=null) series.add(highestPoint.x,highestPoint.y);
			if (highestPointOnOtherEdge!=null) series.add(highestPointOnOtherEdge.x,highestPointOnOtherEdge.y);
			for (int i=0;i<edgeDetector.x.size();++i)
				series.add(edgeDetector.x.get(i),edgeDetector.y.get(i));
//			
//			if (edgeDetector.center!=null){
//				if (edgeDetector.radiusL>0 && edgeDetector.radiusL<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusL, series);
//				if (edgeDetector.radiusR>0 && edgeDetector.radiusR<550) TrackSegment.circle(edgeDetector.center.x, edgeDetector.center.y, edgeDetector.radiusR, series);
//			}
			 
			EdgeDetector.drawEdge(series, "E"+distRaced+" "+nraced+"b");
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
		return Math.sqrt(G*radius*2)*3.6;
	}


	public double fuzzySteering(){
		
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;			
			return curAngle/steerLock;
		}
//		int firstIndexMax = (turn==TURNLEFT) ? edgeDetector.firstIndexMax-1 : edgeDetector.lastIndexMax+1;		
//		if (firstIndexMax>0 && firstIndexMax<edgeDetector.x.size()){				
//		Vector2D p = new Vector2D(edgeDetector.x.getDouble(firstIndexMax),edgeDetector.y.getDouble(firstIndexMax));
//		double[] r = new double[3];
//		Geom.getCircle(0, 0, highestPoint.x, highestPoint.y, p.x, p.y, r);
//		double radius = Math.sqrt(r[2]);			
//		maxSpeed = 0.8*maxAllowedSpeedAtRadius(Math.abs(radius));
//		}
		maxSpeed=120;

		double alpha = PI_2-highestPoint.angle();
		double maxA = maxAngleAtSpeed(speedX)-curAngle;

		double distance = highestPoint.length();
		if (distance>75)
			maxSpeed = Double.MAX_VALUE;
		double sign = (alpha<0) ? -1 : 1;
		double pos = sign * curPos;
		double speedy = speedY * sign;
		angle.setValue(alpha*sign);
		dist.setValue(distance);
		this.pos.setValue(pos);
		speedx.setValue(speedX);
		this.speedy.setValue(speedy);

		angle.getMembershipFunction("Small").setParameter(3,maxA);
		angle.getMembershipFunction("Small").setParameter(2,0.9*maxA);


		fuzzyRuleSet.evaluate();
//		if (distRaced>120 && distRaced<150)
////		System.out.println(fuzzyRuleSet);

//		for (Object r : fuzzyRuleSet.getRules()) {
//		FuzzyRule rule = (FuzzyRule)r;
//		if (rule.getDegreeOfSupport()>0) System.out.println(rule);
//		}
//		if (distRaced>330) meta=1;
		double steer = sign*steering.getLatestDefuzzifiedValue();
//		if (Double.isNaN(steer)){
//		System.out.println(fuzzyRuleSet);
//		}
//		System.out.println(steer+"   "+alpha+"   "+highestPoint+"   "+curAngle);
		steer = curAngle - steer;
		steer = Math.max(steer, -steerLock);
		steer = Math.min(steer, steerLock);

		return steer/steerLock;
	}



	double getSteer() {
		// TODO Auto-generated method stub
		//		jf.setVisible(true);		
		if (meta==1)
			return 0;


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
