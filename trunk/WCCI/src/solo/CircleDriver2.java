/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2IntMap;
import it.unimi.dsi.fastutil.doubles.Double2IntRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2IntSortedMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import it.unimi.dsi.fastutil.doubles.DoubleRBTreeSet;
import it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.geom.AffineTransform;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

import org.jfree.data.xy.XYSeries;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver2 extends BaseStateDriver<NewCarState,CarControl> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6254760984999310075L;
	List<Vector2D> lhps = null;
	Vector2D oldPoint = null;
	boolean detected = false;
	boolean draw = false;			
	XYSeries series = new XYSeries("Curve");
	final static double PRECISION = 1000000.0d;
	static boolean flying =false;
	static boolean foundRadius = false;
	static double lastAccel = 0.0;
	static double lastAngle = 0.0d;
	static double lastBrkCmd = 0;
	static double lastDv = 0;
	static double lastSteer = 0;
	static double lastPos = 0;
	static int highestPointEdge = 0;
	static double sx = -1;
	static double sy = -1;
	static boolean inTurn = true;
	Vector2D trackDirection = new Vector2D(0,1);
	final static double MAX_UNSTUCK_ANGLE = 15.0d/180.0d*Math.PI;	// [radians] If the angle of the car on the track is smaller, we assume we are not stuck.
	final static double UNSTUCK_TIME_LIMIT = 2.0d;				// [s] We try to get unstuck after this time.
	final static double MAX_UNSTUCK_SPEED = 5.0d;				// [m/s] Below this speed we consider being stuck.
	final static double MIN_UNSTUCK_DIST = 3.0d;				// [m] If we are closer to the middle we assume to be not stuck.
	final static double ABS_SLIP = 2.0f;						// [m/s] range [0..10]
	final static double ABS_RANGE = 5.0f;						// [m/s] range [0..10]
	final static double ABS_MINSPEED = 3.0f;					// [m/s] Below this speed the ABS is disabled (numeric, division by small numbers).
	final static int  MAX_UNSTUCK_COUNT = 100;

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
	ObjectList<Segment> l = null;
	ObjectList<Segment> r = null;
	int stuck =0;

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
	static double time = 0;
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
	double tW = 0;
	double prevTW = 0;	
	int turn;
	boolean isTurning = false;
	Vector2D optimalPoint = null;
	Vector2D mustPassPoint = null;
	Segment seg = null;
	Edge edge = null;
	Edge other = null;
	double edgeRadius;
	double nextRadius;
	double currentSpeedRadius;

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
	ObjectArrayList<TrackSegment> trackData = null;
	ObjectArrayList<TrackSegment> trackE = null;
	List<Segment> track = null;
	ObjectList<Segment> tr = null;
	Segment curSeg = null;
	LinkedList<Segment> temp = null;
	int tIndex = -1;
	int count = -1;
	double rDist = -1;
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
		track = new ObjectArrayList<Segment>();
	}


	public boolean isStuck(CarState cs){
		double speedX = cs.speedX/3.6;
		double angle = cs.angle;
		double curPos = cs.getTrackPos();
		if (Math.abs(angle) > MAX_UNSTUCK_ANGLE &&
				speedX < MAX_UNSTUCK_SPEED &&
				Math.abs(edgeDetector.toMiddle()) >MIN_UNSTUCK_DIST) {
			if (stuck > MAX_UNSTUCK_COUNT && curPos*angle <= 0.0) {
				return true;
			} else {
				stuck++;
				return false;
			}
		} else {
			stuck = 0;
			return false;
		}
	}
	
	public void checkVector(NewCarState cs,ObjectArrayList<TrackSegment> oal){
		double s = Math.PI/18;
		double ang = cs.getAngle();
		for (int i=0;i<19;++i){
			double angle = ang-PI_2+i*s;
			Vector2D v = new Vector2D(0,100).rotated(-angle);
			Vector2D p = null;
			double d = Double.MAX_VALUE;
			boolean ok = false;			
			for (int j=0;j<oal.size();++j){
				TrackSegment ts = oal.get(j);				
				if (ts.type==TrackSegment.STRT){
					double[] r = new double[3];
					if (Geom.getSegSegIntersection(0, 0, v.x, v.y, ts.startX, ts.startY, ts.endX, ts.endY, r) !=null){
						double dd = Geom.distance(0, 0, r[0], r[1]);
						if (d>dd && dd>0){
							d = dd;
							p = new Vector2D(r[0],r[1]);
							ok = true;
						}
					}						
				} else {										
					double[] r = Geom.getSegArcIntersection(0, 0, v.x, v.y, ts.centerx, ts.centery, ts.radius, ts.arc, ts.startX, ts.startY, ts.endX, ts.endY);
					if (r!=null && r.length>0){
						Vector2D p1 = new Vector2D(r[0],r[1]);
						double dd = Geom.distance(0, 0, r[0], r[1]);
						
						if (r.length>2 && dd>Geom.distance(0, 0, r[2], r[3])){
							dd=Geom.distance(0, 0, r[2], r[3]);
							p1 = new Vector2D(r[2],r[3]);	
						}
							
						if (d>dd && dd>0){
							d = dd;
							p = p1;
							ok = true;
						}
					}
				}
			}
			
			if (ok){
				if (d>100) {
					d = 100;
					p = v.normalised().times(d);
				}
			} else p = v.normalised().times(100);
			TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, p.x, p.y);
			oal.add(ts);
		}//end of for
	}
	
	public void testing(NewCarState cs,EdgeDetector ed){
		trackData = new ObjectArrayList<TrackSegment>();
		trackE = new ObjectArrayList<TrackSegment>();
		
		double px = cs.posX;
		double py = cs.posY;
		final int STRT = 3;
		final int LFT = 2;
		final int RGT = 1;
		int tp = (int)cs.type[0];
		double sxL = cs.vertex[0].x-px;
		double syL = cs.vertex[0].y-py;
//		double sxR = cs.vertex[1].x-px;
//		double syR = cs.vertex[1].y-py;
		double exL = cs.vertex[2].x-px;
		double eyL = cs.vertex[2].y-py;
//		double exR = cs.vertex[3].x-px;
//		double eyR = cs.vertex[3].y-py;
		double a = 0;
		if (tp==STRT){			
			double dx = exL-sxL;
			double dy = eyL-syL;		
			a = PI_2-Math.atan2(dy,dx);
		} else {
			double dx = cs.center[0].x-px;
			double dy = cs.center[0].y-py;
			a = (tp==LFT) ? Math.PI-Math.atan2(dy, dx) : -Math.atan2(dy, dx);
		} 
//		boolean ok = false;
		for (int i=0;i<cs.l.length;++i){
			int type = (int)cs.type[i];
			double centerx = cs.center[i].x-px;
			double centery = cs.center[i].y-py;
			Vector2D c = new Vector2D(centerx,centery).rotated(a);			
			double length = cs.l[i];
			double rL = cs.radiusL[i];
			double rR = cs.radiusR[i];
			double sXL = cs.sXL[i]-px;
			double sYL = cs.sYL[i]-py;
			double eXL = cs.eXL[i]-px;
			double eYL = cs.eYL[i]-py;
			double sXR = cs.sXR[i]-px;
			double sYR = cs.sYR[i]-py;
			double eXR = cs.eXR[i]-px;
			double eYR = cs.eYR[i]-py;
			Vector2D p1 = new Vector2D(sXL,sYL).rotated(a);
			Vector2D p2 = new Vector2D(eXL,eYL).rotated(a);
			Vector2D p3 = new Vector2D(sXR,sYR).rotated(a);
			Vector2D p4 = new Vector2D(eXR,eYR).rotated(a);
			double arc = cs.arc[i];
//			if (cs.type.length>3 && (int)(cs.type[0])==RGT) 
//				ok = true;			
			if (type==STRT) {
				type = TrackSegment.STRT;								
			} else if (type==LFT) {
				type = TrackSegment.LFT;
			} else {
				type = TrackSegment.RGT;
				arc = -arc;
			}
				
			TrackSegment ts = new TrackSegment(type,c.x,c.y,length,0,rL,arc,p1.x,p1.y,p2.x,p2.y);
			TrackSegment rts = new TrackSegment(type,c.x,c.y,length,0,rR,arc,p3.x,p3.y,p4.x,p4.y);
			trackData.add(ts);
			trackData.add(rts);
				
		}
		
//		checkVector(cs,oal);
		/*for (int i=0;i<ed.numpoint;++i){
			TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, ed.x.getDouble(i), ed.y.getDouble(i));
			oal.add(ts);
		}//*/
//		if (time>=4.78)
//			System.out.println();
		/*TrackSegment.drawTrack(trackData, "Track "+cs.distFromStart+" "+time);			
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			// TODO: handle exception
		}//*/
//		
		
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
			TrackSegment ts = TrackSegment.createStraightSeg(0, edgeDetector.x.get(i), edgeDetector.y.get(i), edgeDetector.x.get(i), edgeDetector.y.get(i));
			trackE.add(ts);
		}

//		TrackSegment.line(0, 0, carDirection.x*20, carDirection.y*20, series);
		/*if (edge!=null && edge.center!=null){
			Vector2D p1 = edge.get(edge.index+1);
			Vector2D p2 = (edge.nIndex!=-1) ? edge.get(edge.nIndex-1) : edge.getHighestPoint();
			TrackSegment ts1 = TrackSegment.createTurnSeg(edge.center.x, edge.center.y, edge.radius, p1.x, p1.y, p2.x, p2.y);
			trackE.add(ts1);
			if (edge.nIndex!=-1 && edge.nextCenter!=null){
				p1 = (edge.index<edge.size-1) ? edge.get(edge.index+1):edge.getHighestPoint();
				p2 = edge.getHighestPoint();
				ts1 = TrackSegment.createTurnSeg(edge.nextCenter.x, edge.nextCenter.y, edge.nextRadius, p1.x, p1.y, p2.x, p2.y);
				trackE.add(ts1);
			}
			p1 = edge.get(0);
			p2 = edge.get(edge.index);
			if (p1!=null && p2!=null) {
				ts1 = TrackSegment.createStraightSeg(0, p1.x, p1.y, p2.x, p2.y);
				trackE.add(ts1);
			}
			
		}
		
		if (other!=null && other.center!=null ){
			Vector2D p1 = (other.index<other.size-1) ? other.get(other.index+1):other.getHighestPoint();
			Vector2D p2 = (other.nIndex!=-1) ? other.get(other.nIndex-1) : other.getHighestPoint();
			TrackSegment ts1 = TrackSegment.createTurnSeg(other.center.x, other.center.y, other.radius, p1.x, p1.y, p2.x, p2.y);
			trackE.add(ts1);
			if (other.nIndex!=-1 && other.nextCenter!=null){
				p1 = (other.nIndex<other.size) ? other.get(other.nIndex) : other.getHighestPoint();
				p2 = other.getHighestPoint();
				ts1 = TrackSegment.createTurnSeg(other.nextCenter.x, other.nextCenter.y, other.nextRadius, p1.x, p1.y, p2.x, p2.y);
				trackE.add(ts1);				
			}
			p1 = other.get(0);
			p2 = other.get(other.index);
			if (p1!=null && p2!=null) {
				ts1 = TrackSegment.createStraightSeg(0, p1.x, p1.y, p2.x, p2.y);
				trackE.add(ts1);
			}
		}//*/
	}

	public void estimateBestPath(){
		double width = trackWidth * (1-WIDTHDIV);				
		double radiusLarge = (edgeRadius+trackWidth/2)-width-W;
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
		radiusOfTurn = 3.414*(radiusLarge-0.707*radiusSmall);
		Vector2D center = edgeDetector.center;
		if (inTurn){						
			Vector2D t = null;
			if (curSeg!=null && curSeg.type==0){
				t = (turn==TURNRIGHT) ? new Vector2D(SIN_PI_4,-SIN_PI_4) : new Vector2D(-SIN_PI_4,-SIN_PI_4);
				mustPassPoint = center.minus(t.times(radiusSmall));
				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusSmall));		
			} else if (curSeg!=null){
				double d = distRaced - curSeg.dist;
				double arc = Math.PI/4-d/curSeg.radius;
				double sign = (center.x<0) ? 1 : -1;
				t = new Vector2D(sign,0).rotated(-turn*arc).normalised();
				mustPassPoint = center.plus(t.times(radiusSmall));
				centerOfTurn = center.minus(t.times(radiusOfTurn-radiusSmall));
			}
			if (t==null){
				optimalPoint = edgeDetector.highestPoint;
				mustPassPoint = new Vector2D(0,1);
			} else {
				t = t.rotated(-turn*Math.PI*0.25);
				optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));
			}
			return;
		}		
		if (edge==null || center==null) return;		
		Vector2D t = (turn==TURNRIGHT) ? new Vector2D(SIN_PI_4,-SIN_PI_4) : new Vector2D(-SIN_PI_4,-SIN_PI_4);					
		centerOfTurn = center.plus(t.times(radiusOfTurn-radiusSmall));
		mustPassPoint = center.minus(t.times(radiusSmall));
		optimalPoint = centerOfTurn.plus(new Vector2D(0,1).times(radiusOfTurn));
	}

	public void estimatePath(Vector2D hh){
		if (edgeDetector==null || edgeDetector.center==null) return;
		Vector2D center = edgeDetector.center;
		double width = trackWidth * (1-WIDTHDIV);		
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
//		double radiusLarge = (edgeRadius+trackWidth/2)-width-W;
		Vector2D t = null;
//		if (centerOfTurn!=null && hh!=null){
//			t = hh.minus(centerOfTurn).normalized();
//			optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));				
//		}
		

		if (centerOfTurn!=null){
			rr = radiusOfTurn;
			cntr = centerOfTurn;
			if (!isTurning) isTurning = (centerOfTurn.length()<radiusOfTurn);
			
			if (isTurning){
				Vector2D[] p = Geom.ptTangentLine(0, 0, center.x,center.y, radiusSmall);
				Vector2D point = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
				if (mustPassPoint.y<6) mustPassPoint = point;
				double[] r = new double[3];
				if (point!=null && Geom.getCircle2(mustPassPoint, optimalPoint, new Vector2D(0,0), carDirection, r)!=null){
					rr = Math.sqrt(r[2]);
					cntr = new Vector2D(r[0],r[1]);
				}
			}			
			
//			if (!isTurning && centerOfTurn!=null){
//				p = Geom.ptTangentLine(0, 0, centerOfTurn.x,centerOfTurn.y, radiusOfTurn-W);
//				point2Follow = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
//				if (point2Follow==null){
////					centerOfTurn = cntr;
////					radiusOfTurn = rr;
//					p = Geom.ptTangentLine(0, 0, cntr.x,cntr.y, rr);
//					point2Follow = (p==null) ? null : (p.length>1 && p[1].y>p[0].y) ? p[1] : p[0];
//				}
//			} else if (isTurning || inTurn){
////				if (time>=8)
////				System.out.println();
////				if (Geom.getCircle2(point, new Vector2D(edge.center.x,edge.center.y+edgeRadius-W/2), new Vector2D(0,0), carDirection, r)!=null){				
////				rr = Math.sqrt(r[2]);
////				cntr = new Vector2D(r[0],r[1]);
////				}
//				point2Follow = hh;
//			}
		}

		if (time>=20.94 && detected){
			draw=true;
			if (draw) store();
			TrackSegment ts = (isTurning) ? TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, 0, 0, optimalPoint.x, optimalPoint.y) : TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, cntr.x-turn*rr, cntr.y, optimalPoint.x, optimalPoint.y);
			trackData.add(ts);
						
			ts = TrackSegment.createStraightSeg(0,0, 0, carDirection.x*20, carDirection.y*20);
			trackData.add(ts);
//			ts = TrackSegment.createTurnSeg(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, centerOfTurn.x-turn*radiusOfTurn, centerOfTurn.y, optimalPoint.x, optimalPoint.y);
//			trackData.add(ts);			
//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusSmall, center.x-turn*radiusSmall, center.y, center.x, center.y+radiusSmall);
//			trackData.add(ts);
//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusLarge, center.x-turn*radiusLarge, center.y, center.x, center.y+radiusSmall);
//			trackData.add(ts);

			if (draw) display();
			draw=false;
		}//*/
		System.out.println("mustP : "+mustPassPoint+"   Highest  "+edgeDetector.highestPoint+"   optimalP "+optimalPoint);
	}

	public void display(){
		if (!draw) return;
		try{
			
			if (edge!=null && edge.size>edge.index+3){
				Vector2D p1 = edge.get(edge.index+1);
				Vector2D p2 = edge.get(edge.index+2);
				Vector2D p3 = edge.get(edge.index+3);
				double[] r = new double[3];
				Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
				r[2] = Math.sqrt(r[2]);
				CircleFitter cf = new  CircleFitter(r,edge.allPoints.elements(),edge.index+1,edge.size-1);
				cf.fit();
				Vector2D c = cf.getEstimatedCenter();
				double rr = cf.getEstimatedRadius();
				TrackSegment ts = TrackSegment.createTurnSeg(c.x, c.y, rr, c.x-rr, c.y, c.x, c.y-rr);
//				trackE.add(ts);
			}
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			TrackSegment.drawTrack(trackData,"E "+nf.format(time)+" a");
			TrackSegment.drawTrack(trackE,"E "+nf.format(time)+" b");
//			if (cntr!=null)TrackSegment.circle(cntr.x, cntr.y, rr, series);
//			if (centerOfTurn!=null) TrackSegment.circle(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, series);
//			EdgeDetector.drawEdge(series, "E "+nf.format(time)+" c");
			Thread.sleep(300);
		} catch (Exception e){
			e.printStackTrace();
		}
}
	
	public void check(AffineTransform at,ObjectList<Segment> l,Double2ObjectSortedMap<Vector2D> all){
		Double2ObjectSortedMap<Vector2D> map = new Double2ObjectRBTreeMap<Vector2D>();
		int k = 0;
		DoubleSortedSet dss = all.keySet();
		for (k=0;k<l.size();) {
			Segment ll = l.get(k);
			ll.transform(at);
			for (double key:ll.points.keySet()){						
				if (!all.containsKey(key)){
					Vector2D v = ll.points.get(key);
					DoubleBidirectionalIterator iter = dss.iterator(key);
					boolean ok = false;
					boolean found = false;
					Vector2D p = null;
					if (iter.hasPrevious()){
						double prevKey = iter.previousDouble();
						 p = all.get(prevKey);
						if (Math.hypot(p.x-v.x, p.y-v.y)<0.5) {
							ok=true;
							if (ll.type!=Segment.UNKNOWN && ll.isPointBelongToSeg(p)){ 
								ll.points.put(prevKey, p);
								found = true;
							}
						}								
					}
					if (!ok){
						iter.next();							
						if (iter.hasNext()){
							double nextKey = iter.nextDouble();
							p = all.get(nextKey);
							if (Math.hypot(p.x-v.x, p.y-v.y)<0.5){ 
								ok = true;
								if (ll.type!=Segment.UNKNOWN && ll.isPointBelongToSeg(p)){ 
									ll.points.put(nextKey, p);
									found = true;
								}																								
							}
						}
					}

					if (!ok){
						p = edgeDetector.highestPoint;
						if (ll.type!=Segment.UNKNOWN && Math.hypot(p.x-v.x, p.y-v.y)<0.5){ 
							ok = true;
							if (ll.isPointBelongToSeg(p)){ 
								ll.points.put(p.y, p);
								found = true;
							}																								
						}
					}
//					if (!ok || !found) 
//						ll.removePoint(v);
//					else ll.points.remove(v.y);
					if (k>0 || (k==0 && ll.type==0 && ll.points.size()>2)) ll.removePoint(v);
					if (ll.start==null) {
						l.remove(k);
						--k;
						break;
					}
					if (ok) {
						if (found){
							if (ll.type==0){ 
								ll.num++;
								ll.reCalLength();
							} else if (ll.type!=Segment.UNKNOWN && ll.num<Segment.LIM){													
								ll.num = ll.points.size();						
								double oldr = ll.radius;
								ll.reCalculate(tW);
								if (Math.abs(ll.radius-oldr)>0.5){
									Segment.checkRs(l, k, tW);
									Segment.updateRs(l, k, tW);
								}						
							} else {
								ll.num = ll.points.size();
								if (ll.type!=Segment.UNKNOWN){ 
									ll.reCalLength();
								} else {
									Segment.lastCheck(l, tW);
								}
							}
						}	
					}
				}
			}			
			k++;			
		}
		
		for (Segment ll:l) map.putAll(ll.points);
		
		for (Vector2D v : all.values()){		
			if (map.containsKey(v.y)) continue;
		
			int j = -1;
			
			boolean found = false;
			for (Segment ll : l){		
				j++;
				if (j==0 && ll.type==0){
					if (v.y>ll.end.y && Math.abs(v.x-ll.start.x)>TrackSegment.EPSILON) {
						double dmin = 10000;
						double dmax = -10000;
						boolean ok = false;
						for (Vector2D p: ll.points.values()){
							dmin = Math.min(dmin, p.x);
							dmax = Math.max(dmax, p.x);
							if (v.x>=dmin && v.x<=dmax){
								ok = true;
								break;
							}
						}
						if (!ok && (v.x<dmin || v.x>dmax)) continue;
					}					
					ll.addPoint(v);					
					found = true;
					break;
				}
				boolean isIn = ll.isPointInSegment(v);
				boolean isBelong = ll.isPointBelongToSeg(v);
//				if (isIn && ll.num>Segment.LIM && isBelong) {
//					found = true;
//					break;
//				}
				if (ll.type!=Segment.UNKNOWN && isBelong){
					ll.addPoint(v);
					if (ll.type!=0 && ll.num<=Segment.LIM){
						double oldr = ll.radius;
						ll.reCalculate(tW);
						if (Math.abs(oldr-ll.radius)>0.5){
							Segment.checkRs(l,j,tW);
							Segment.updateRs(l,j,tW);
						}
					}
					found = true;
					break;
				}
				if (v.y<=ll.end.y || j==l.size()-1){
					Double2ObjectSortedMap<Vector2D> mp = new Double2ObjectRBTreeMap<Vector2D>();
					int m=0;
					
					for (k=j;k>=0;k--){
						Segment s = l.get(k);
						if (s.type!=Segment.UNKNOWN && s.num>3) break;						
						mp.putAll(s.points);
					}
					if (k>=0 && v.y>l.get(k).end.y) 
						k++;					
									
					for (m=j;m<l.size();++m){
						Segment s = l.get(m);
						if (s.type!=Segment.UNKNOWN && s.num>3) break;
						mp.putAll(s.points);
					}

					if (k==m && isIn){
						m++;
						mp.putAll(ll.points);
					}
					mp.put(v.y,v);
					if (k>=0 && k<m) l.removeElements(k, m);
					List<Segment> ol = Segment.segmentize(mp.values(), tW);
					if (k>=0) {
						l.addAll(k, ol);
						Segment.lastCheck(l, tW);
					}
					found = true;
					break;
				}
				
			}//end of for
			if (!found){
				Segment smt = new Segment();				
				smt.addPoint(v);
				l.add(smt);
				Segment.lastCheck(l, tW);				
			}
		}//end of outer most for
		Segment smt = l.get(l.size()-1);				
		if (smt.type==Segment.UNKNOWN && smt.num>=3){
			List<Segment> ol = Segment.segmentize(smt.points.values(), tW);
			l.remove(l.size()-1);
			l.addAll(ol);
		}
		
}
	
	public void record(CarState cs){		
		if (!recording) return;	
		long ti = System.currentTimeMillis();
		centerOfTurn = null;		
//		long time = System.currentTimeMillis();
		nraced = distRaced - raced;		
//		nraced = Math.round(nraced*PRECISION)/PRECISION;
				

		System.out.println("**************** E"+time+" "+distRaced+" ****************  "+inTurn);//
		System.out.println("Turn : "+edgeDetector.turn);
		Edge left = edgeDetector.left;
		Edge right = edgeDetector.right;
			
		if (left==null && right==null){
			prevEdge = edgeDetector;				
			raced = distRaced;		
			return;
		}
		
		
		Edge pEdge = (prevEdge.turn==TURNLEFT) ? prevEdge.right : (prevEdge.turn==TURNRIGHT) ? prevEdge.left : null;
		if (pEdge!=null) System.out.println(pEdge.radius+"   asa");
				
//		double prevToMiddle = Math.round(-prevTW*prevEdge.curPos*PRECISION)/PRECISION;
		double toMiddle = -tW*edgeDetector.curPos;
		double prevToMiddle = -prevTW*prevEdge.curPos;
		double ax = toMiddle-prevToMiddle;
//		if (!inTurn && prevEdge.center!=null && pEdge.center.y<nraced && nraced>0 && edgeDetector.straightDist<5) {
//			inTurn = true;
//		}
//		
						
		
		if (time>=7.42){			
			System.out.println();
		}
		
		if (inTurn){
			l = (left==null) ? null : Segment.segmentize(left.allPoints, tW);
			r = (right==null) ? null :Segment.segmentize(right.allPoints, tW);
//			if (curSeg!=null){			
//				Segment seg = (curSeg.type!=TrackSegment.STRT) ? curSeg : nextSeg;
//				left.calculateRadiusWhileInTurn(seg, -seg.type*trackWidth*0.5, toMiddle, cs.distRaced);
//				right.calculateRadiusWhileInTurn(seg, seg.type*trackWidth*0.5, toMiddle, cs.distRaced);			
//			}			
		}
				
		
		if (!inTurn && l!=null && r!=null && left!=null && right !=null && l.size()>0 && r.size()>0){
			Segment l0 = l.get(0);
			Segment r0 = r.get(0);
			ObjectList<Segment> ll = null;
			ObjectList<Segment> rrr = null;
			if (l0.type!=0 && r0.type!=0)
				inTurn = true;
			else if ((l0.type==0 && l0.end.y<10) || (r0.type==0 && r0.end.y<10)){
				ll = Segment.segmentize(left.allPoints, tW);
				rrr = Segment.segmentize(right.allPoints, tW);
				Segment ll0 = ll.get(0);
				Segment rr0 = rrr.get(0);
				if (ll0.type!=Segment.UNKNOWN && ll0.type!=0 && rr0.type!=Segment.UNKNOWN && rr0.type!=0)
					inTurn = true;
				else if (ll0.type==Segment.UNKNOWN && rr0.type==Segment.UNKNOWN)
					inTurn = false;
				else if (ll0.type == 0 && ll0.end.y>=0) inTurn = false;
				else if (rr0.type == 0 && rr0.end.y>=0) inTurn = false;	
				else {
					if (ll0.type!=Segment.UNKNOWN && ll0.type!=0){				
						Segment s = Segment.toMiddleSegment(ll0, -1, tW);
						if (s.start.y<0.5) inTurn = true;
					}  
					if (!inTurn && rr0.type!=Segment.UNKNOWN && rr0.type!=0){
						Segment s = Segment.toMiddleSegment(rr0, 1, tW);
						if (s.start.y<0) inTurn = true;
					}
				}
			}
			if (inTurn){							
				l = (ll==null) ? Segment.segmentize(left.allPoints, tW) : ll;
				r = (rrr==null) ? Segment.segmentize(right.allPoints, tW) : rrr;
			}
		}

						
		if (!inTurn  && prevEdge.turn*edgeDetector.turn!=-1 && (nraced<=0 || nraced<prevEdge.straightDist-0.5 || (prevEdge.center!=null && prevEdge.center.y>=nraced))) {
			AffineTransform at = null;		
			Double2ObjectSortedMap<Vector2D> allL = new Double2ObjectRBTreeMap<Vector2D>();
			Double2ObjectSortedMap<Vector2D> allR = new Double2ObjectRBTreeMap<Vector2D>();

			at = edgeDetector.combine(prevEdge, nraced);
			left = edgeDetector.left;
			right = edgeDetector.right;
//			if (at!=null && (sx!=-1 || sy!=-1)) {
//				Vector2D p = new Vector2D();
//				at.transform(new Vector2D(sx,sy), p);
//				sx = p.x;
//				sy = p.y;
//			}			
			edge = (edgeDetector.turn==TURNLEFT) ? edgeDetector.right : (edgeDetector.turn==TURNRIGHT) ? edgeDetector.left : null;
			if (left!=null){
				if (l==null || l.size()==0)
					l = Segment.segmentize(left.allPoints, tW);
				else {
					for (Vector2D v:left.allPoints) allL.put(v.y, v);
					check(at, l, allL);
				}
			}
			
			if (right!=null){
				if (r==null || r.size()==0)
					r = Segment.segmentize(right.allPoints, tW);
				else {
					for (Vector2D v:right.allPoints) allR.put(v.y, v);
					check(at, r, allR);
				}
			}
			inTurn = false;
		}
		
	
		Segment.adjust(l);
		Segment.adjust(r);
		
		if (edgeDetector.turn==MyDriver.UNKNOWN) edgeDetector.turn = prevEdge.turn;		

		turn = edgeDetector.turn;
		highestPointEdge = (turn==TURNRIGHT) ? -1 : 1;
		Vector2D hh = edgeDetector.highestPoint;	

		str = "";
		if (draw) store();



//		if (turn==UNKNOWN) {
//			turn = prevEdge.turn;
//			edgeDetector.center = null;
//			prevEdge = edgeDetector;				
//			raced = distRaced;
//			sx = -1;
//			sy = -1;
//			return;
//		}

		if (highestPointEdge==-1){
			edge = edgeDetector.left;
			other = edgeDetector.right;
		} else {
			edge = edgeDetector.right;
			other = edgeDetector.left;
		}
		
		ObjectList<Segment> lm = new ObjectArrayList<Segment>();
		ObjectList<Segment> rm = new ObjectArrayList<Segment>();
		
		if (l!=null)			
		for (Segment ts : l){
			System.out.println("l  "+ts);
			if (ts.type==Segment.UNKNOWN) continue;
			Segment s = Segment.toMiddleSegment(ts, -1, tW);			
			if (s==null) continue;			
			s.start = ts.start;
			s.end = ts.end;
			s.center = ts.center;
			lm.add(s);
		}		
		System.out.println("-----------");
		if (r!=null)
		for (Segment ts : r){			
			System.out.println("r  "+ts);
			if (ts.type==Segment.UNKNOWN) continue;
			Segment s = Segment.toMiddleSegment(ts, 1, tW);					
			if (s==null) continue;
			s.start = ts.start;
			s.end = ts.end;
			s.center = ts.center;
			rm.add(s);
		}
				
		if (time>=3.74)
			System.out.println();
		
		if (time>=0 && (lm.size()>0 || rm.size()>0)){			
			System.out.println("-----------");
			storeTrack(edgeDetector,cs,lm,rm);
			int i=0;
			int j=0;
			for (Segment ts : tr){
				System.out.println("Track  "+ts);
//				if (ts.type!=0){
//					boolean ok = false;
//					while (!ok && i<lm.size()){
//						Segment ls = lm.get(i);
//						if (ls.dist>ts.dist+ts.length) break;
//						if (ls.type!=ts.type || ls.type==Segment.UNKNOWN || ls.dist+ls.length<ts.dist) {
//							i++;
//							continue;
//						}
//						Segment lo = null;
//						double rr = (ts.type==0) ? Double.MAX_VALUE : ts.radius+ts.type*trackWidth*0.5;
//						for (int k=i;k<l.size();++k){
//							lo = l.get(k);							
//							if (lo.type==ls.type && ls.type==0 && lo.length==ls.length) break;
//							if (lo.type==ls.type && ls.type!=0 && Math.abs(lo.radius-rr)<0.5) break;
//						}
//						if (lo==null || lo.type==Segment.UNKNOWN) break;
//						Vector2D p1 = lo.start;
//						Vector2D p2 = lo.end;
//						if (ts.type==0){
//							ts.addPoint(p1);
//							ts.addPoint(p2);
//							ok = true;
//							break;
//						}
//						Vector2D p = p1.plus(p2).times(0.5);
//						double d_2 = p1.distance(p2)*0.5d;						
//						Vector2D n = p2.minus(p1).orthogonal().normalised();
//						if (n.dot(lo.center.minus(p))<0) n = n.negated();
//						ts.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
//						ok = true;
//					}
//					
//					while (!ok && j<rm.size()){
//						Segment rs = rm.get(j);
//						if (rs.dist>ts.dist+ts.length) break;
//						if (rs.type!=ts.type ||rs.type==Segment.UNKNOWN ||rs.dist+rs.length<ts.dist) {
//							j++;
//							continue;
//						}
//						Segment ro = null;
//						double rr = (ts.type==0) ? Double.MAX_VALUE : ts.radius-ts.type*trackWidth*0.5;
//						for (int k=j;k<r.size();++k){
//							ro = r.get(k);							
//							if (ro.type==rs.type && rs.type==0 && ro.length==rs.length) break;
//							if (ro.type==rs.type && rs.type!=0 && Math.abs(ro.radius-rr)<0.5) break;
//						}
//						if (ro==null || ro.type==Segment.UNKNOWN) break;
//						Vector2D p1 = ro.start;
//						Vector2D p2 = ro.end;
//						if (ts.type==0){
//							ts.addPoint(p1);
//							ts.addPoint(p2);
//							ok = true;
//							break;
//						}
//						Vector2D p = p1.plus(p2).times(0.5);
//						double d_2 = p1.distance(p2)*0.5d;						
//						Vector2D n = p2.minus(p1).orthogonal().normalised();
//						if (n.dot(ro.center.minus(p))<0) n = n.negated();
//						ts.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
//						ok = true;
//					}
//					if (!ok)
//						System.out.println();
//				}
			}
						
		}
		
		if (temp==null)
			temp = new LinkedList<Segment>();
		
		if (tr!=null && tr.size()>0){
			Segment s = tr.get(0);
			temp.add(s);
			if (temp.size()>3) temp.pop();
			double r = s.radius;
			count = 0;
			if (curSeg==null){
				for (Segment tmp:temp)
					if (tmp.type==s.type && tmp.radius==r) count++;
				if (count>=3){
					curSeg = new Segment();
					curSeg.copy(s);
					curSeg.dist = temp.getFirst().dist;
				}
			} else if (r!=curSeg.radius){
				for (Segment tmp:temp)
					if (tmp.type==s.type && tmp.radius==r) count++;
				if (count>=3){
					curSeg.length = temp.getFirst().dist - curSeg.dist;
					if (track==null) 
						track = new ObjectArrayList<Segment>();
					track.add(curSeg);
					curSeg = new Segment();
					curSeg.copy(s);
					curSeg.dist = temp.getFirst().dist;
				}
			}
		}
		
//		if (inTurn && l!=null && r!=null){
//			Segment l0 = l.get(0);
//			Segment r0 = r.get(0);	
//			if (l0.type==0 && r0.type==0) {
//				inTurn = false;
//				l.clear();
//				r.clear();
//			}			
//		}

		

//		if (!inTurn && tr.size()>1){ 			
//			edgeRadius = tr.get(1).radius;
////			Vector2D h = edge.getHighestPoint();
////			Vector2D l = edge.allPoints.get(0);
//			nextRadius = edge.nextRadius-trackWidth/2;
//			if (nextRadius<=0){				
//				nextRadius = (edge==left && prevEdge.left!=null) ? prevEdge.left.nextRadius-trackWidth/2 : (prevEdge.right!=null) ? prevEdge.right.nextRadius-trackWidth/2 : edgeRadius;
//				if (nextRadius<=0) nextRadius = edgeRadius;
//				if (nextRadius>0) edge.nextRadius = nextRadius+trackWidth/2;
//			}
//
//		}



		cntr = null;
		rr=0;
		if (time>=5.3)
			System.out.println();
		guessTurn();		
		if (detected){ 
			if (lhps==null) 
				lhps = new ObjectArrayList<Vector2D>();
			Vector2D point = edgeDetector.highestPoint.minus(edgeDetector.currentPointAhead);
			double d = edgeDetector.highestPoint.distance(edgeDetector.currentPointAhead);
			if (d>0 && d<99 && (lhps.size()==0 || point.distance(lhps.get(lhps.size()-1))>0.5)) lhps.add(point);
			System.out.println("Here  "+edgeDetector.highestPoint.minus(edgeDetector.currentPointAhead));
			if (lhps.size()>6){
				Vector2D[] v = new Vector2D[lhps.size()];
				lhps.toArray(v);
				CircleFitter cf = new CircleFitter(new double[]{0,0,1},v,1,6);
				cf.fit();
				System.out.println("Estimate Radius "+cf.getEstimatedRadius());
			}
			
		}
		
		if (edgeDetector.turn!=MyDriver.UNKNOWN){
			estimateBestPath();			
		}			
		estimatePath(hh);
				
		
		prevEdge = edgeDetector;				
		raced = distRaced;
		System.out.println("Time : "+(System.currentTimeMillis()-ti));
//		System.out.println(System.currentTimeMillis()-time);	
	}

	public final void guessTurn(){
		Vector2D hh = edgeDetector.highestPoint;
		
		Segment last = (tr!=null && tr.size()>0) ? tr.get(tr.size()-1) : null;
		if (last!=null && inTurn){
			if (last.type!=0 && last.center!=null){
				double d = last.center.distance(hh);
				if (time>=10.5 && Math.abs(Math.abs(d-last.radius)-trackWidth/2)>0.1){						
					detected = true;
				}
			}
		}


		if (inTurn && tr!=null && tr.size()>0){
			seg = null;
			for (int i=0;i<tr.size();++i){
				seg = tr.get(i);
				if (seg.type!=0) break;
			}
			edgeDetector.center = seg.center;
			edgeRadius = seg.radius;
		} else if (!inTurn && curSeg!=null && curSeg.type==0){
			detected = false;
			if (lhps!=null) lhps.clear();
			if (tr!=null && tr.size()>0){
				boolean ok = false;
				for (int i=0;i<tr.size();++i){
					Segment s = tr.get(i);
					if (s.type!=0) {						
						edgeDetector.center = s.center;
						edgeRadius = s.radius;
						ok = true;
						break;
					}
				}
				
				if (!ok){
					Segment s = null;
					Segment t = null;
					int which = -2;
					if (l!=null && l.size()>1){
						for (int i=0;i<l.size();++i){
							Segment tmp = l.get(i);
							if (t==null && tmp.type==0)
								t = tmp;
							if (tmp.type==Segment.UNKNOWN){
								s = tmp;
								which = -1;
								break;
							}
						}
					}
					
					if (s==null || s.num<=1){
						Segment t1 = null;
						if (r!=null && r.size()>1){
							for (int i=0;i<r.size();++i){
								Segment tmp = r.get(i);
								if (t1==null && tmp.type==0)
									t1 = tmp;
								if (tmp.type==Segment.UNKNOWN){
									if (tmp.num>1 || turn==-1){
										s = tmp;
										t = t1;
										which = 1;
									}
									break;
								}
							}
						}
					}
					
					if (s==null || t==null) return;
					if (s.num==1) s.addPoint(edgeDetector.highestPoint);
					double[] rr = new double[3];
					Geom.getCircle2(s.start, s.end, t.start, t.end, rr);					
					rr[2] = Math.sqrt(rr[2]);
					edgeDetector.center = new Vector2D(rr[0],rr[1]);
					edgeRadius = Math.round(rr[2]+turn*which*trackWidth*0.5);
				}
				
			}
		}
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

//	public double speedAtRadius(double x){
//	if (x>=500) return Double.MAX_VALUE;
//	if (x>=170)
//	return 85.5247084519036+2.01905340962707*x-0.00646416884272083*x*x+7.29293433335612e-06*x*x*x;
//	if (x>=100)
//	return -139.760581424782+6.04756169365199*x-0.0303044500612258*x*x+5.3961109358751e-05*x*x*x;
//	if (x>=60)
//	return 202.745158728743-4.48739317463141*x+0.0755898471248578*x*x-0.00029146122705404*x*x*x;
//	if (x>=20)
//	return 26.7657503764833+2.55982229567786*x-0.0171415320257696*x*x+0.000108671654261182*x*x*x;		
//	return -0.00728601622351035+7.7247995418915*x+-0.360701254346654*x*x+0.00788103445639814*x*x*x;		
//	}

	public double speedAtRadius(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;

		if (x>150)
			return -115431.216892959+834.957333227655*x-3.71025373015644*x2+0.00999768673957289*x3-1.50103486982362e-05*x4+9.64495639130066e-09*x5+9519812.76010439/x-414947104.101293/x2+6808805657.1795/x3;		
		return 5.2604417632244+2.93221790821321*x-0.0308952361016195*x2+0.00027900914172188*x3-1.30914988151668e-06*x4+2.81513214824391e-09*x5+358.981440260822/x-3125.65260706368/x2+7536.84482849135/x3;		
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
		Vector2D o = new Vector2D(cx,cy);
		Vector2D point = (edgeDetector.turn==TURNRIGHT) ? new Vector2D(cx,cy).orthogonal() : new Vector2D(-cx,-cy).orthogonal();
//		double a = point.angle(carDirection);		
		if (d<targetRadius-0.5){			
			return gotoPoint(state, point);
		} else if (d>targetRadius){			
			Vector2D[] points = Geom.ptTangentLine(0, 0, cx, cy, targetRadius);
			if (points==null) return 0;
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
		if (flying) return 0;
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
			if (isTurning && speed<targetSpeed+10)
				return 0;
			double b2 = 0;
			double meanSpd = 0.0d;			
//			for (int i = 0; i < 4; i++) {
//			meanSpd = wheelSpinVel[i]*wheelRadius[i]/speedX;
//			if (slip<meanSpd) slip = meanSpd;
//			}
			meanSpd = (wheelSpinVel[REAR_LFT] + wheelSpinVel[REAR_RGT])*wheelRadius[REAR_LFT]/speedX;
			meanSpd /= 2.0;



			if (point!=null){
				double dist = point.length()-3;
				if (dist<0) return -1;
				double currentBrakeDist = brakeDistAtSpeed(speed)-brakeDistAtSpeed(targetSpeed);				
				if (dist>1.1*currentBrakeDist)
					return 1;				
//				b2 = currentBrakeDist/dist;
			}

//			slip = 1;
			double brake =(speed*speed-targetSpeed*targetSpeed)/(speed*speed);
//			if (speedX<ABS_MINSPEED) return -brake;
//			slip = 0.0f;
//			for (int i = 0; i < 4; i++) {
//			slip += wheelSpinVel[i] * wheelRadius[i];
//			}
//			slip = speedX- slip/4.0f;
//			if (slip > ABS_SLIP) {
//			brake = brake - Math.min(brake, (slip - ABS_SLIP)/ABS_RANGE);
//			}
//			if (brake<b2) brake = b2;
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
	
	public Vector2D higherPoint(Vector2D p1,Vector2D p2,Vector2D center){
		System.out.println(p1+"  a  "+p2+"    "+center);
		double v1 = Math.abs(p1.minus(center).angle());
		double v2 = Math.abs(p2.minus(center).angle());	
		if (v1>=PI_2){
			v1 = Math.PI-v1;
			v2 = Math.PI-v2;
		}
		return (v1<v2) ? p2 : p1;
	}
	
	public Vector2D getCurrentHighestSeenPoint(Edge left,Edge right){
		
		Vector2D p1 = null;
		if (left.nIndex==-1 || (left.nextCenter!=null && Math.abs(left.radius-left.nextRadius)<1))
			p1 = left.getHighestPoint();
		else p1 = left.get(left.nIndex-1);
		
		Vector2D p2 = null;
		if (right.nIndex==-1 || (right.nextCenter!=null && Math.abs(right.radius-right.nextRadius)<1))
			p2 = right.getHighestPoint();
		else p2 = right.get(right.nIndex-1);
		
		if (p1==null) return p2;
		if (p2==null) return p1;
		if (Math.abs(left.radius-right.radius)-trackWidth<1){
			return (left.center!=null) ? higherPoint(p1,p2,left.center) : (right.center!=null) ? higherPoint(p1,p2,right.center) :null;
		}
		return null;
	}
	
	public Vector2D getNextHighestSeenPoint(Edge left,Edge right){		
		Vector2D p1 = (left.nextCenter==null) ? null : left.getHighestPoint();			
		Vector2D p2 = (right.nextCenter==null) ? null : right.getHighestPoint();
		
		if (p1==null) return p2;
		if (p2==null) return p1;
		if (Math.abs(left.nextRadius-right.nextRadius)-trackWidth<1){
			return higherPoint(p1,p2,left.center);
		}
		return null;
	}
	
	public final static void estimateStartEnd(Segment s,double currentDist,double toMiddle){
		if (s.type==0) {
			s.start.x = toMiddle;
			s.end.x = toMiddle;
			s.start.y = s.dist - currentDist;
			s.end.y = s.dist + s.length - currentDist;
			return;
		}
		s.center.y = 0;
		s.center.x = s.type*s.radius+toMiddle;
		double d = s.dist-currentDist;
		double arc = -s.type*d/s.radius;	
		s.start = s.center.plus(new Vector2D(-s.type*s.radius,0).rotated(arc));
		arc = -s.type*(d+s.length)/s.radius;
		s.end = s.center.plus(new Vector2D(-s.type*s.radius,0).rotated(arc));
	}
	
	public final void analyze(List<Segment> track,int from){
		for (int i=from;i<track.size();++i){
			Segment s = track.get(i);			
			Segment t = (i<track.size()-1) ?track.get(i+1) : null;
			while (t!=null && (s.type==t.type && Math.abs(s.radius-t.radius)<1)){
				if (s.type==0 && s.end!=null && t.end!=null){
					double dx = s.end.x - s.start.x;
					double ddx = t.end.x - t.start.x;
					if (Math.abs(dx)<=TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON) break;
					if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)<=TrackSegment.EPSILON) break;
					if (Math.abs(dx)>TrackSegment.EPSILON && Math.abs(ddx)>TrackSegment.EPSILON){
						double dy = s.end.y - s.start.y;
						double ddy = t.end.y - t.start.y;
						if (Math.abs(Math.atan2(dy, dx)-Math.atan2(ddy, ddx))>0.1) break;
					}
				}
				track.remove(i+1);
				s.dist = Math.min(s.dist, t.dist);
				s.length = Math.max(s.dist+s.length, t.dist+t.length) - s.dist;
				
				double dmax = s.radius;
				int maxn = s.map.get(dmax);
				for (double r:t.map.keySet()){
					int nr = s.map.get(r);
					nr = Math.max(t.map.get(r),nr);
					s.map.put(r,nr);
					if (nr>maxn){
						dmax = r;
						maxn = nr;
					}
				}
				if (dmax!=s.radius){
					if (dmax<0 && !Double.isInfinite(dmax)){
						dmax = -dmax;
						Double2IntMap m = new Double2IntRBTreeMap();
						for (double r:s.map.keySet())
							m.put(-r, s.map.get(r));
						s.map = null;
						s.map = m;
						s.type = -s.type;
					}
					s.radius = dmax;
					s.arc = s.type*s.length/s.radius;
					if (Double.isInfinite(dmax)) s.type=0;
					if (s.type!=0 && s.start!=null){
						double rr = s.radius - s.type*trackWidth/2;
						double d_2 = s.start.distance(s.end);
						Vector2D p = s.start.plus(s.end);
						Vector2D n = s.end.minus(s.start).orthogonal().normalised();
						if (n.dot(s.center.minus(p))<0) n = n.negated();
						s.center = p.plus(n.times(rr*rr-d_2*d_2));
					}
				}				
				s.arc = s.type*s.length/s.radius;
				if (i==track.size()-1) return;
				t = track.get(i+1);								
			}					
		}
	}
	
	public void storeTrack(EdgeDetector ed,CarState cs,List<Segment> ol,List<Segment> or){
		double dist = cs.getDistRaced();				
		
		for (int i=0;i<ol.size();++i){
			Segment s = ol.get(i);
			if (i==0){
				double sign = (s.start.y<0) ? -1 : 1;
				double d = (s.type!=0) ? sign*Segment.distance(new Vector2D(toMiddle,0), s.start, s.center, s.radius) : s.start.y;
				s.dist = dist+d;
			} 
			if (i<ol.size()-1){
				Segment t = ol.get(i+1);
				Segment.estimateDist(s, t);
			}
		}
		analyze(ol,0);
		for (int i=0;i<or.size();++i){
			Segment s = or.get(i);
			if (i==0){
				double sign = (s.start.y<0) ? -1 : 1;
				double d = (s.type!=0) ? sign*Segment.distance(new Vector2D(toMiddle,0), s.start, s.center, s.radius) : s.start.y;
				s.dist = dist+d;
			} 
			if (i<or.size()-1){
				Segment t = or.get(i+1);
				Segment.estimateDist(s, t);
			}
		}
		analyze(or,0);
		DoubleSortedSet ds = new DoubleRBTreeSet();
		tIndex = 0;
		for (Segment s:ol){
			if (s.dist+s.length<=dist) continue;
			ds.add(s.dist);
			ds.add(s.dist+s.length);
		}
		
		for (Segment s:or){
			if (s.dist+s.length<=dist) continue;
			ds.add(s.dist);
			ds.add(s.dist+s.length);
		}
		double[] ar = new double[ds.size()];
		ds.toArray(ar);		
		int k=tIndex;
		int j=0;
		int ri =0;
		
		ObjectList<Segment> l =new ObjectArrayList<Segment>();
		ObjectList<Segment> tE = new ObjectArrayList<Segment>();
		for (int i=0;i<ar.length-1;++i){
			double d = ar[i];
			double e = ar[i+1];
			Segment s = null;
			Segment tmp = null;			
			int which = -2;
			while (j<ol.size()){
				s = ol.get(j);
				if (s.dist<=d && s.dist+s.length>=e){
					tmp = new Segment();
					tmp.copy(s);
					tmp.dist = d;
					tmp.length = e-d;
					tmp.arc = tmp.type*tmp.length/tmp.radius;
					which = -1;																				
				}
				if (s.dist+s.length>=e) break;
				if (s.dist+s.length<=e) j++;
			}//end of while
			
			
			while (ri<or.size()){				
				s = or.get(ri);
				if (s.dist<=d && s.dist+s.length>=e){
					if (tmp==null){
						tmp = new Segment();
						tmp.copy(s);
						tmp.dist = d;
						tmp.length = e-d;
						tmp.arc = tmp.type*tmp.length/tmp.radius;
						which = 1;
					} else {
						double dmax = tmp.radius;
						int maxn = tmp.map.get(tmp.radius);
						if (tmp.type==s.type || s.type==0 || tmp.type==0){
							for (double r:s.map.keySet()){
								int nr = s.map.get(r);
								nr += tmp.map.get(r);
								tmp.map.put(r,nr);
								if (nr>maxn){
									dmax = r;
									maxn = nr;
								}
							}
							if (dmax!=tmp.radius){							
								tmp.radius = dmax;
								tmp.arc = tmp.type*tmp.length/tmp.radius;
								if (Double.isInfinite(dmax)) 
									tmp.type=0;
								else tmp.type = s.type;
								if (tmp.type!=0){									
									double rr = tmp.radius - which*tmp.type*trackWidth/2;
									double d_2 = tmp.start.distance(tmp.end)*0.5;
									Vector2D p = tmp.start.plus(tmp.end).times(0.5);
									Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
									if (n.dot(tmp.center.minus(p))<0) n = n.negated();
									tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
								}
							}
						} else {
							for (double r:s.map.keySet()){
								int nr = s.map.get(r);
								if (Double.isInfinite(r))
									nr += tmp.map.get(r);
								else nr += tmp.map.get(-r);
								tmp.map.put(-r,nr);
								if (nr>maxn){
									dmax = (Double.isInfinite(r)) ? r : -r;
									maxn = nr;
								}
							}
							if (dmax!=tmp.radius){							
								tmp.radius = Math.abs(dmax);					
								if (dmax<0){
									tmp.type = -tmp.type;
									Double2IntSortedMap nmap = new Double2IntRBTreeMap();
									for (double r:tmp.map.keySet())
										nmap.put(-r, tmp.map.get(r));
									tmp.map = null;
									tmp.map = nmap;
								}
								if (tmp.type!=0){
									double rr = tmp.radius - which*tmp.type*trackWidth/2;
									double d_2 = tmp.start.distance(tmp.end)*0.5;
									Vector2D p = tmp.start.plus(tmp.end).times(0.5);
									Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
									if (n.dot(tmp.center.minus(p))<0) n = n.negated();
									tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
								}
								tmp.arc = tmp.type*tmp.length/tmp.radius;
							}
						}//end of if						
				
					}
				}
				if (s.dist+s.length>=e) break;
				if (s.dist+s.length<=e) ri++;
			}//end of while
						
			
			if (tr!=null && tr.size()>0 && tmp!=null){
				while (k<tr.size()){
					s = tr.get(k);
					double dd = Math.max(d,s.dist);
					double ee = Math.min(e,s.dist+s.length);
					double ll = ee-dd;
					if ((s.dist<=d && s.dist+s.length>=e) || (ll>0 && ll/(e-d)>0.8)){
						if (tmp==null){
							tmp = new Segment();
							tmp.copy(s);
							tmp.dist = d;
							tmp.length = e-d;
							tmp.arc = tmp.type*tmp.length/tmp.radius;
						} else {
							double dmax = tmp.radius;
							int maxn = tmp.map.get(tmp.radius);
							if (tmp.type==s.type || s.type==0 || tmp.type==0){
								for (double r:s.map.keySet()){
									int nr = s.map.get(r);
									nr += tmp.map.get(r);
									tmp.map.put(r,nr);
									if (nr>=maxn){
										dmax = r;
										maxn = nr;
									}
								}
								if (dmax!=tmp.radius){							
									tmp.radius = dmax;
									tmp.arc = tmp.type*tmp.length/tmp.radius;
									if (Double.isInfinite(dmax)) 
										tmp.type=0;
									else tmp.type = s.type;
									if (tmp.type!=0){
										double rr = tmp.radius - which*tmp.type*trackWidth/2;
										double d_2 = tmp.start.distance(tmp.end)*0.5;
										Vector2D p = tmp.start.plus(tmp.end).times(0.5);
										Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
										if (n.dot(tmp.center.minus(p))<0) n = n.negated();
										tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
									}
								}
							} else {
								for (double r:s.map.keySet()){
									int nr = s.map.get(r);
									if (Double.isInfinite(r))
										nr += tmp.map.get(r);
									else nr += tmp.map.get(-r);
									tmp.map.put(-r,nr);
									if (nr>=maxn){
										dmax = (Double.isInfinite(r)) ? r : -r;
										maxn = nr;
									}
								}
								if (dmax!=tmp.radius){							
									tmp.radius = Math.abs(dmax);
									tmp.arc = tmp.type*tmp.length/tmp.radius;
									if (dmax<0){
										tmp.type = -tmp.type;
										Double2IntSortedMap nmap = new Double2IntRBTreeMap();
										for (double r:tmp.map.keySet())
											nmap.put(-r, tmp.map.get(r));
										tmp.map = null;
										tmp.map = nmap;
									}
									if (tmp.type!=0){
										double rr = tmp.radius - which*tmp.type*trackWidth/2;
										double d_2 = tmp.start.distance(tmp.end)*0.5;
										Vector2D p = tmp.start.plus(tmp.end).times(0.5);
										Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
										if (n.dot(tmp.center.minus(p))<0) n = n.negated();
										tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
									}
								}
							}//end of if												
						}
						
					}
					if (tmp !=null && i==ar.length-2 && s.dist+s.length>e){
						Segment tmp1 = new Segment();
						tmp1.copy(s);
						tmp1.start = null;
						tmp1.end = null;
						tmp1.center = null;
						tmp1.dist = e;
						tmp1.length = s.dist+s.length-e;
						tmp1.arc = tmp1.type*tmp1.length/tmp1.radius;
						tE.add(tmp1);
					}

					if (s.dist+s.length>=e) break;
					if (s.dist+s.length<=e) k++;
				}//end of while
				if (tmp!=null) l.add(tmp);
			} else {//end of if
				if (tmp!=null) l.add(tmp);
			}
			
		}//end of for
		if (tE!=null && tE.size()>0) l.addAll(tE);		
		if (tr!=null && tr.size()>0) {
			tr.removeElements(tIndex, k);			
			tr.addAll(tIndex,l);						
		} else tr = new ObjectArrayList<Segment>(l);
		analyze(tr, tIndex);
	};

	boolean k = false;
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub
		
		long ti = System.currentTimeMillis();		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();

		point2Follow = null;		
		double brake = 0.0d;		
		double acc = 1;		
		NewCarState cs = state.state;
		curAngle = cs.angle;
		if (isStuck(cs)){
			CarControl cc = new CarControl(1,0,-1,-curAngle/steerLock,0);
			ol.add(cc);
			return ol;
		}
		double curPos = -cs.trackPos;
		double dP = curPos-lastPos;

		carDirection = trackDirection.rotated(-curAngle);
		double speed = cs.getSpeed();	
		currentSpeedRadius = radiusAtSpeed(speed);
		time = cs.getLastLapTime();
		if (time>=0.964){			
			System.out.println();
		}
		edgeDetector = new EdgeDetector(cs);
		prevTW = tW;
		tW =  Math.round(edgeDetector.trackWidth)*0.5d;
		if (tW<0) tW = prevTW;
		
		
		testing(cs,edgeDetector);

		distRaced = cs.distRaced;

		if (edgeDetector.trackWidth<=0 && prevEdge!=null)
			edgeDetector.trackWidth = prevEdge.trackWidth;
		trackWidth = edgeDetector.getTrackWidth();//must keep this line
		workingWidth = trackWidth-2*W;			
		
		double tW = Math.round(edgeDetector.trackWidth)*0.5d;
		toMiddle =Math.round(-tW*edgeDetector.curPos*PRECISION)/PRECISION;
		mass = carmass + cs.getFuel();				
		
		double steer = steerControl(cs);
		
		
//		double s = steerAtSpeed(speed);		

//		maxSpeed = 60;
		acc = speedControl(cs, maxSpeed,point2Follow);
//		if (Math.abs(steer)>s){							
//		acc = 0;
//		}

//		if (acc>=0 && isTurning && dP*turn<0){
//		if (curPos*turn<=WIDTHDIV-0.1)
//		acc = (2/(1+Math.exp(-10)) - 1);			
//		if (curPos*turn<=WIDTHDIV-0.15){
//		acc = 0;
//		}

//		acc = 0;
//		}

		if (acc<=0) {
			brake = -acc;
			acc = 0;
		}
//		if (isTurning) brake=0;
//		if (time>=5)
//		System.out.println();

//		if (isTurning && curPos*turn>=0)
//		steer = gotoPoint(cs, trackDirection);

		int gear = getGear(cs);

//		steer = lastSteer + 50 * (steer - lastSteer) * 0.005;
		lastSteer = steer;
		lastPos = curPos;
		lastAngle = curAngle;		
		if (flying) 
			steer *= 0.2;
//		if (time>=3.7){
//		steer = 0;
//		acc = 0;
//		brake = 0;
//		}		
//		System.out.println(new ObjectArrayList<Vector2D>(cs.vertex));
//		Vector2D p0 = cs.vertex[0];
//		Vector2D p1 = cs.vertex[1];
//		Vector2D p2 = cs.vertex[2];
//		Vector2D p3 = cs.vertex[3];
//		System.out.println(p0.distance(new Vector2D(cs.cx,cs.cy))+" haha");
////		System.out.println(p1.distance(new Vector2D(cs.cx,cs.cy)));
//		System.out.println(p2.distance(new Vector2D(cs.cx,cs.cy))+"   hahaha");
//		System.out.println(p3.distance(new Vector2D(cs.cx,cs.cy)));
		System.out.println(cs.radiusl+"    r    "+cs.radiusr+"    r     "+cs.radius);
		System.out.println(Math.round(new Vector2D(cs.posX,cs.posY).distance(new Vector2D(cs.cx,cs.cy))*1000000.0d)/1000000.0d+"  bbbbb ");		

		CarControl cc = new CarControl(acc,brake,gear,steer,0);
		ol.add(cc);
		System.out.println("Time taken : "+(System.currentTimeMillis()-ti));
		return ol;
	}

	public void startRecordingEdge(){
//		long time = System.currentTimeMillis();
		recording = true;		
		sx = -1;
		sy = -1;
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
//			prevSteer+=(0.3-curPos)*turn/25;
			return prevSteer;
		}
//		double straightDist = (edge!=null && other!=null) ? other.straightDist : 0;
		
		

		if (!isTurning){
			targetRadius = radiusOfTurn;
//			targetRadius = Math.min(Math.min(radiusOfTurn, edgeRadius-trackWidth/2),nextRadius-trackWidth/2);
//			steer = gotoPoint(cs, trackDirection)+(curPos+WIDTHDIV/4*turn)/20;
			steer = (centerOfTurn!=null) ? steerAtRadius(cs, centerOfTurn.x, centerOfTurn.y, radiusOfTurn-W) : gotoPoint(cs, trackDirection);						
		} else {
//			if (nextRadius<=edgeRadius+trackWidth/2)
			targetRadius = Math.min(rr,radiusOfTurn);
//			else targetRadius = nextRadius;//Math.min(rr,edgeRadius+trackWidth/2);
//			if (nextRadius>=rr)
//				targetRadius = Math.max(nextRadius, rr)-trackWidth/4;
//			else 
//			targetRadius = Math.max(nextRadius,63);
//			targetRadius = 63;
			
//			targetRadius = Math.max(nextRadius, rr)-trackWidth/4;
			steer = (cntr!=null) ? steerAtRadius(cs, cntr.x, cntr.y, rr) : gotoPoint(cs, trackDirection);
//			steer = gotoPoint(cs, edgeDetector.highestPoint.plus(edge.getHighestPoint()).times(0.5));
			if (Math.abs(steer)>=s*N)
				steer = (steer<0) ? -s*N : s*N;

		}
		draw = false;	
		if (turn!=UNKNOWN) 			
			maxSpeed = speedAtRadius(targetRadius);			
		else maxSpeed = speed;	
//		maxSpeed =Math.min(150,maxSpeed);
		if (detected) steer = 0;
		System.out.println("Turn radius "+edgeRadius+"  MSpeed "+maxSpeed+" Next Radius "+nextRadius+" TRadius "+rr+"  EdgeRadius "+edgeRadius+" speed "+speed);
		System.out.println(edgeDetector.center+"    "+radiusOfTurn);
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
		if (curPos>1 || curPos<-1){
			recording = false;
			return curAngle/steerLock+deflect;
		}
		if (trackWidth<=0)
			return curAngle/steerLock+deflect;
				
		if (recording){ 			
			record(cs);		
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
			inTurn = false;
			if (l!=null && l.size()>0) l.clear();
			if (r!=null && r.size()>0) r.clear();
			
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
			inTurn = false;
			double sign = (curPos<0) ? -1 : 1;
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
		return state.state.getLastLapTime()>=480;
//		return (targetRadius>=300);
	}

	@Override
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
