/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.lang.MutableString;

import java.awt.geom.AffineTransform;
import java.util.Arrays;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

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
	double highest =-100;
	double highestL = -100;
	double highestR = -100;
	Vector2D highestPointOnLeftEdge = null;
	Vector2D pointOnLeftEdge = null;
	Vector2D highestPointOnRightEdge = null;
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
		int firstIndexMax = edgeDetector.firstIndexMax;
		int lastIndexMax = edgeDetector.lastIndexMax;
		if (edgeDetector.leftCC==null || edgeDetector.rightCC==null) edgeDetector.constructEdge();
		turn = edgeDetector.estimateTurn();		
		double[] x = edgeDetector.x.elements();
		double[] y = edgeDetector.y.elements();
		highestPoint = (firstIndexMax>=0 && edgeDetector.x.size()>0) ? new Vector2D(x[firstIndexMax],y[firstIndexMax]) : null;
		highestPointOnLeftEdge = (firstIndexMax>=1) ? new Vector2D(x[firstIndexMax-1],y[firstIndexMax-1]) : null;
		highestPointOnRightEdge = (lastIndexMax<edgeDetector.x.size()-1) ? new Vector2D(x[lastIndexMax+1],y[lastIndexMax+1]) : null;
				
		highestL = (edgeDetector.leftCC==null) ? 0 : edgeDetector.leftCC.allLengths[edgeDetector.leftCC.allLengths.length-1];
		highestR = (edgeDetector.rightCC==null) ? 0 : edgeDetector.rightCC.allLengths[edgeDetector.rightCC.allLengths.length-1];
		highest = (turn==TURNRIGHT) ? highestL+highestPoint.distance(highestPointOnLeftEdge) : highestR+highestPoint.distance(highestPointOnRightEdge);
		raced = distRaced;		
		System.out.println(System.currentTimeMillis()-time);
	}

	int count=0;
	boolean followedPath = false;

	public static Vector2D locatePointAtLength(double length,Vector2D[] allPoints,double[] allLengths){
		if (allPoints==null || allLengths==null) return null;
		int index = Arrays.binarySearch(allLengths, length);
		int len = allLengths.length;
		if (len<2) return null;
		
		if (index>0)
			return allPoints[index];
					
		if (index<0) index = -index+1;
		Vector2D t = null;
		Vector2D p = null;
		if (index>=len){
			t = allPoints[len-1].minus(allPoints[len-2]).normalized();
			p = allPoints[len-1];
		} else {
			t = allPoints[index].minus(allPoints[index-1]).normalized();
			p = allPoints[index-1];
		}
		
		return p.plus(t.times(length-allLengths[index-1]));
	}

	public static Vector2D estimatePointOnEdge(double length,CurveCoordinate cc,Vector2D hP){
		if (cc==null ) return null;		
		Vector2D[] allPoints = cc.allPoints;
		double[] allLengths = cc.allLengths;
		int n = allLengths.length-1;
		double d = length-allLengths[n];
		if (d<=0) 
			return locatePointAtLength(length, allPoints, allLengths);					
		
		Vector2D lastPoint = allPoints[n];		
		Vector2D t = (hP!=null) ? hP.minus(lastPoint).normalized() : lastPoint.minus(allPoints[n-1]).normalized();		
		return lastPoint.plus(t.times(d));		
	}


	public void record(){
		if (!recording) return;
		long time = System.currentTimeMillis();
		nraced = distRaced - raced;
		nraced = Math.round(nraced*10000.0d)/10000.0d;
		if (prevEdge.straightDist>=nraced) {
			edgeDetector.combine(prevEdge, nraced);			
		}
		
		if (distRaced>200) meta = 1;
		turn = edgeDetector.estimateTurn();		
		double[] x = edgeDetector.x.elements();
		double[] y = edgeDetector.y.elements();
		int firstIndexMax = edgeDetector.firstIndexMax;
		int lastIndexMax = edgeDetector.lastIndexMax;
		
		if (edgeDetector.leftCC==null) edgeDetector.constructEdge();
		Vector2D hh = (firstIndexMax>=0 && edgeDetector.x.size()>0) ? new Vector2D(x[firstIndexMax],y[firstIndexMax]) : null;
		Vector2D hL = (firstIndexMax>=1) ? new Vector2D(x[firstIndexMax-1],y[firstIndexMax-1]) : null;
		Vector2D hR = (lastIndexMax<edgeDetector.x.size()-1) ? new Vector2D(x[lastIndexMax+1],y[lastIndexMax+1]) : null;
		Vector2D gL = (turn==TURNRIGHT) ? hh : null;
		Vector2D gR = (turn==TURNLEFT) ? hh : null;		
		double lenL = (edgeDetector.leftCC==null) ? 0 : edgeDetector.leftCC.allLengths[edgeDetector.leftCC.allLengths.length-1];
		double lenR = (edgeDetector.rightCC==null) ? 0 : edgeDetector.rightCC.allLengths[edgeDetector.rightCC.allLengths.length-1];
		double len = (turn==TURNRIGHT) ? lenL+hh.distance(hL) : lenR+hh.distance(hR);
		
		
		System.out.println("*********** "+distRaced+"  "+nraced+"**************");		
		
		highestL -= nraced;
		if (highestL>0 && highestL>lenL+1){
			if (nraced<=edgeDetector.straightDist)
				highestPointOnLeftEdge.y -= nraced;
			else highestPointOnLeftEdge = (edgeDetector.leftCC!=null) ? estimatePointOnEdge(highestL, edgeDetector.leftCC, gL) : null;
		} else {
			highestPointOnLeftEdge = hL;
			highestL = lenL;
		}
		
		highestR -= nraced;
		if (highestR>0 && highestR>lenR+1){
			if (nraced<=edgeDetector.straightDist)
				highestPointOnRightEdge.y -= nraced;
			else highestPointOnRightEdge = (edgeDetector.rightCC!=null) ? estimatePointOnEdge(highestR, edgeDetector.rightCC, gR) : null;
		} else {
			highestPointOnRightEdge = hR;
			highestR = lenR;
		}
		
		highest -= nraced;		
		if (highest>0 && highest>len+1){
			if (nraced<=edgeDetector.straightDist)
				highestPoint.y -= nraced;
			else if (turn==TURNRIGHT){
				highestPoint = (edgeDetector.leftCC!=null) ? estimatePointOnEdge(highest, edgeDetector.leftCC, gL) : null;
			} else if (turn==TURNLEFT){
				highestPoint = (edgeDetector.rightCC!=null) ? estimatePointOnEdge(highest, edgeDetector.rightCC, gR) : null;
			}
		} else {
			highestPoint = hh;
			highest = len;
		}
				
				
		if (distRaced>140 && distRaced<190){
			Vector2D[] edges = edgeDetector.getEdges();
			EdgeDetector.drawEdge(edges, "E"+distRaced+" "+nraced+"a");
			ObjectArrayList<Vector2D> nedges = new ObjectArrayList<Vector2D>(edges);
			if (highestPoint!=null) nedges.add(highestPoint);
//			if (highestPointOnLeftEdge!=null) nedges.add(highestPointOnLeftEdge);
			if (highestPointOnRightEdge!=null) nedges.add(highestPointOnRightEdge);
			EdgeDetector.drawEdge(nedges, "E"+distRaced+" "+nraced+"b");
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO: handle exception
			}
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
		System.out.println("Here");
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;			
			return curAngle/steerLock;
		}
//		int firstIndexMax = (turn==TURNLEFT) ? edgeDetector.firstIndexMax-1 : edgeDetector.lastIndexMax+1;		
//		if (firstIndexMax>0 && firstIndexMax<edgeDetector.x.size()){				
//			Vector2D p = new Vector2D(edgeDetector.x.getDouble(firstIndexMax),edgeDetector.y.getDouble(firstIndexMax));
//			double[] r = new double[3];
//			Geom.getCircle(0, 0, highestPoint.x, highestPoint.y, p.x, p.y, r);
//			double radius = Math.sqrt(r[2]);			
//			maxSpeed = 0.8*maxAllowedSpeedAtRadius(Math.abs(radius));
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
////			System.out.println(fuzzyRuleSet);
			
			for (Object r : fuzzyRuleSet.getRules()) {
				FuzzyRule rule = (FuzzyRule)r;
				if (rule.getDegreeOfSupport()>0) System.out.println(rule);
			}
//		if (distRaced>330) meta=1;
		double steer = sign*steering.getLatestDefuzzifiedValue();
//		if (Double.isNaN(steer)){
//			System.out.println(fuzzyRuleSet);
//		}
		System.out.println(steer+"   "+alpha+"   "+highestPoint+"   "+curAngle);
		steer = curAngle - steer;
		steer = Math.max(steer, -steerLock);
		steer = Math.min(steer, steerLock);
		
		return steer/steerLock;
	}
	
	public double steerToPoint(Vector2D point,Vector2D via){
		double a = PI_2-point.angle();
		double beta = Math.atan2(speedY, speedX);
		if (Math.abs(curPos)>=0.95){
			System.out.println(a+"  "+curPos+"  "+beta+"   "+speedX+"   "+speedY);
			if (a*curPos>0){//current turn will make the car further away from the middle, should prevent this
				if (beta*curPos>0){//if the car is sliding towards the curb, should prevent this
					a = 0;
				} else a = 0;
			} else {//current turn is correct
				if (beta*curPos>0){//if the car is sliding towards the curb, should prevent this
					a += -beta;
				}
			}
		}
		 
//		double b = PI_2-via.angle();
		int firstIndexMax = (turn==TURNLEFT) ? edgeDetector.firstIndexMax-1 : edgeDetector.lastIndexMax+1;
		if (firstIndexMax<0 || firstIndexMax>=edgeDetector.x.size()){
			return a;
		}
		Vector2D p = new Vector2D(edgeDetector.x.getQuick(firstIndexMax),edgeDetector.y.getQuick(firstIndexMax));
		double[] r = new double[3];
		Geom.getCircle(0, 0, highestPoint.x, highestPoint.y, p.x, p.y, r);
		

		double alpha = a;
		double sign = (alpha>=0) ? 1 : -1;
//		double radius = radiusAtAngle(alpha);
		double radius = Math.sqrt(r[2]);
		
		maxSpeed = 0.8*maxAllowedSpeedAtRadius(Math.abs(radius));
//		System.out.println(radius+"  "+maxSpeed+"   "+speedX+"   "+speedY);
		System.out.println(a+"  "+highestPoint);
		
		return alpha;
	}
	
	double followPath(){
		turn = edgeDetector.estimateTurn();
		if (turn==STRAIGHT || turn==UNKNOWN) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			return curAngle/steerLock;
		}		
		if (highestPoint==null)
			return curAngle/steerLock;
//		if (highestPoint==null || highestPointOnLeftEdge==null || highestPointOnRightEdge==null)
//			return curAngle/steerLock;
//						
//		if (turn==TURNLEFT){//angle>0 --> right						
//			return (curAngle-steerToPoint(highestPoint,highestPointOnLeftEdge.plus(new Vector2D(W,L))))/steerLock;
//		}//angle>0 --> right
					
//		return (curAngle-steerToPoint(highestPoint,highestPointOnRightEdge.minus(new Vector2D(W,-L))))/steerLock;
		return (curAngle-steerToPoint(highestPoint,highestPoint))/steerLock;
	}




	double getSteer() {
		// TODO Auto-generated method stub
		//		jf.setVisible(true);		
		if (meta==1){
//			edgeDetector.drawEdge(edgeDetector.x.toDoubleArray(), edgeDetector.y.toDoubleArray(),"Edge");
//			prevEdge.drawEdge(prevEdge.x.toDoubleArray(), prevEdge.y.toDoubleArray(),"Prev Edge");
			return 0;
		}
				
		if (trackWidth<=0)
			return curAngle/steerLock;
		
		if (recording) 
			record();
		else turn = edgeDetector.estimateTurn();
		
		if (followedPath){			
			double steer =  fuzzySteering();			
			return steer;
		}
				
//		if (distRaced>870 ) {
//			meta=1;
//			try{
//				Thread.sleep(5000);
//			} catch (Exception e){
//
//			}
//			return 0;
//
//		}
		if (!followedPath && (curPos<= -(trackWidth/workingWidth) || curPos>=(trackWidth/workingWidth))){
			recording = false;
			followedPath = false;
			return curAngle/steerLock+curPos/10;
		}
									

//		System.out.println(max);
//		double distance=edgeDetector.getDistanceToTurn();

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
		
//		int firstIndexMax = (turn==TURNLEFT) ? edgeDetector.firstIndexMax-1 : edgeDetector.lastIndexMax+1;
//		if (firstIndexMax<0 || firstIndexMax>=edgeDetector.x.size()){
//			return curPos/steerLock+curPos/10;
//		}
//		Vector2D p = new Vector2D(edgeDetector.x.getDouble(firstIndexMax),edgeDetector.y.getDouble(firstIndexMax));
//
//		if (!recording){
//			startRecordingEdge();		
//		} else {						 
//			double distToTurn = 60*speedX/120;					
//			if (highest<distToTurn && !followedPath){
//				System.out.println(p+"   "+distToTurn);
//				followedPath = true;
//				return followPath();
//			};						
//		}
//		if (turn==TURNRIGHT){				
//			return adjustToPos(-0.9, p.y-20);
//		} if  (turn==TURNLEFT)
//			return adjustToPos(0.9, p.y-20);

		
//		return 0;


//		Left + Right -
//		return 0;
	}


}