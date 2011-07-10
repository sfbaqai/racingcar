/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;

import javax.imageio.ImageIO;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver2{
	/**
	 * 
	 */
	public static final double BREAK_TIME =  3900.12;
	public static boolean debug = false;
	//		661.28;

	//	private static final double ABS_SLIP = 2.0f;	+				-	// [m/s] range [0..10]
	private static final double ABS_RANGE = 5.0f;						// [m/s] range [0..10]
	private static double acc = 0;
	private static double lgap = 0;
	private static double slip = 0;
	private static double lastSlip = 0;
	private static double balance = 0;
	private static double lastBalance = 0;
	private static boolean isOffBalance = false;
	private static double distToEstCircle = 0;
	private static double lastDistToEstCircle = 0;
	private static boolean possibleChangeDirection = false;
	private static boolean guessTurn = true;
	private static boolean flyingHazard = false;
	private static boolean landed = false;
	private static double startLanding = 0;
	private static double startFlying = 0;
	private static boolean canGoVeryFast = false;
	private static boolean canGoModerate = false;
	

	public static final double[][][] allCntrxL = new double[19][19][19];

	public static final double[][][] allCntrxR = new double[19][19][19];

	public static final double[][][] allCntryL = new double[19][19][19];
	public static final double[][][] allCntryR = new double[19][19][19];
	public static final double[][][] allRadiusLeft = new double[19][19][19];
	public static final double[][][] allRadiusRight = new double[19][19][19];

	public static final int[][][] allTpL = new int[19][19][19];
	public static final int[][][] allTpR = new int[19][19][19];
	private static int ARRAY_LENGTH = 256;

	public static final double[][] bestFitRadL = new double[19][19];
	public static final double[][] bestFitRadR = new double[19][19];
	public static final int[][] bestIntFitRadL = new int[19][19];


	public static final int[][] bestIntFitRadR = new int[19][19];
	public static final int[][] bestTpL = new int[19][19];

	public static final int[][] bestTpR = new int[19][19];	

	private static double brake = 0;	
	public static boolean canGoAtCurrentSpeed = false;
	private final static Vector2D carDirection = new Vector2D();
	private static final double carmass = 1150;//1050
	public static Vector2D cntr = null;
	public static final double CONSTANT_SPEED_ACC = (2/(1+Math.exp(-1)) - 1);
	public static final double INCREASE_ONE = 2/(1+Math.exp(-3)) - 1;
	public static boolean counterDrift = false;
	public static double curAngle;
	private static double curPos = 0;
	public static double CURRENT_MAX_BRAKE = 0.15;
	static Segment curSeg = null;
	public static boolean DANGER = false;
	public static double DANGER_POS = 0.8;	
	static boolean draw = false;
	public static boolean drift = false;	
	public static double DRIFT_BREAK = 2;
	private static final double E = TrackSegment.EPSILON*0.1;
	public static final EdgeDetector edgeDetector = new EdgeDetector();
	static double edgeRadius;
	private static final double enginerpmRedLine = 958.395;//890.118;//rpm;
	private static final double TURNANGLE = 0.173*1.5;
	private static final double EPSILON = 1.5;
	public static double FAR_DISTANCE = 50;
	public static double FAST_MARGIN = 30;
	private static boolean flying =false;	
	static final int FRONT_LFT = 1;
	static final int FRONT_RGT = 0;
	private static final double G = 9.81;	
	private static final double gap = TrackSegment.EPSILON*0.11;
	private int reserveCount = 0;
	private int reserveMCount = 0;
	private static boolean isSafeToAccel = false;
	public static int gear = 0;
	private static int hazard = 0;
	//	private static final double[] wheelRadiusInInch= new double[]{9,9,9,9};	
	//	private static final double FLYSPEED = 55*3.6;
	//	private static final double MAXALLOWEDPITCH = 0.06;
	//	private static final double SPEED_MARGIN = 5;
	private static final int GEAR_OFFSET = 1;
	//	private static final int[] gearUp = new int[]{5000,6000,6000,6500,7000,7500};
	//	private static final int[] gearDown = new int[]{0,2500,3000,3000,3250,3500};
	//	private static final double[] gearRatio = new double[]{-9.0,0,11.97,8.01,5.85,4.5,3.33,2.7,0,0};
	private static final double[] gearRatio = new double[]{-9.0,0,13.5,8.55,6.3,4.95,4.05,3.465,0,0};
	public static double HIGH_SPEEDY = 30;
	static Vector2D speedV = new Vector2D();
	//	private static int highestPointEdge = 0;
	//	private static double sx = -1;
	//	private static double sy = -1;
	public static boolean inTurn = true;
	static boolean isTurning = false;
	private static final ObjectArrayList<Segment> l = ObjectArrayList.wrap(new Segment[50], 0);
	private static final double L = 4.52;//4.76
	public static final Segment l0 = new Segment();
	public static double lastAcc = 0;
	//	private static boolean foundRadius = false;
	private static double lastAccel = 0.0;
	private static double relativeTargetAngle =0;
	private static double lastAngle = 0.0d;

	private static double lastBrkCmd = 0;

	private static double lastDv = 0;
	public static int lastGear = 0;
	private static double lastPos = 0;	

	public static double lastRPM = 0;
	public static double lastSpeedX = 0;
	public static double lastSpeedY = 0;	

	private static double lastSteer = 0;
	public static double lastTargetAngle = 0;		
	public static double lastTargetSpeed = 0;
	public static final Vector2D leftMost = new Vector2D();
	//	private static Double2ObjectSortedMap<Vector2D> allL = new Double2ObjectRBTreeMap<Vector2D>();
	//	private static Double2ObjectSortedMap<Vector2D> allR = new Double2ObjectRBTreeMap<Vector2D>();
	private static final ObjectList<Segment> lm = ObjectArrayList.wrap(new Segment[100], 0);
	public static Storage lMap = new Storage();
	private static final int[] lnum = new int[150];
	public static double LOW_SPEEDY = 5;		
	//	private static Segment[] segAr = new Segment[150];
	//	private static Segment[] segArr = new Segment[150];
	//	private static Segment[] nsegAr = new Segment[150];
	//	private static Segment[] nsegArr = new Segment[150];
	private static final ObjectArrayList<Segment> lTmp = ObjectArrayList.wrap(new Segment[100], 0);
	public static double MAX_ANGLE = 0.25;
	static final int MAX_GEARS = 10;	
	private static final double MAX_UNSTUCK_ANGLE = 0.25;	// [radians] If the angle of the car on the track is smaller, we assume we are not stuck.
	//	private static final double ABS_MINSPEED = 3.0f;					// [m/s] Below this speed the ABS is disabled (numeric, division by small numbers).
	private static final int  MAX_UNSTUCK_COUNT = 20;
	//	private static final double UNSTUCK_TIME_LIMIT = 2.0d;				// [s] We try to get unstuck after this time.
	private static final double MAX_UNSTUCK_SPEED = 5.0d;				// [m/s] Below this speed we consider being stuck.
	//	private static double oldFirstRad = 0;
	private static double maxAngle = 0;	
	public static final int[][] maxAppearL = new int[19][19];		
	public static final int[][] maxAppearR = new int[19][19];
	public static boolean maxTurn = false;
	public static boolean max = false;
	private static final double MIN_UNSTUCK_DIST = 3.0d;				// [m] If we are closer to the middle we assume to be not stuck.	
	public static double MODERATE_BRAKE = 0.2;
	public static double MODERATE_SPEEDY = 15;
	public static Vector2D mustPassPoint = new Vector2D();
	public static double NEAR_DISTANCE = 10;			
	public static double nraced = 0;
	//	private static final double[] cntrx = new double[150];
	//	private static final double[] cntry = new double[150];
	private static final int[] nums = new int[150];
	public static final int[] occupied = new int[ARRAY_LENGTH];
	public static double OK_POS = 0.5;
	//	private static final ObjectArrayList<Segment> tr = ObjectArrayList.wrap(trArr, 0);
	private static final CarControl[] ol = new CarControl[ARRAY_LENGTH];
	private static final int[] out={0,0};

	private static double mLastX =0;
	private static double mLastY = 0;
	private static final double PI_2 = Math.PI/2;
	static Vector2D point2Follow = null;
	private static final double PRECISION = 1000000.0d;
	/**
	 * 
	 */
	public static final EdgeDetector prevEdge = new EdgeDetector();
	private static final ObjectArrayList<Segment> r = ObjectArrayList.wrap(new Segment[50], 0);
	public static final Segment r0 = new Segment();
	//	private static final double[] realRads = new double[150];
	private static final double[] rads = new double[150];
	static final int REAR_LFT = 3;
	static final int REAR_RGT = 2;
	public static final Vector2D rightMost = new Vector2D();
	private static final ObjectList<Segment> rm = ObjectArrayList.wrap(new Segment[100], 0);

	public static Storage rMap = new Storage();
	private static final int[] rnum = new int[150];
	public static double rpm = 0;
	public static double rr = 0;
	public static double SAFE_DISTANCE = 40;
	public static final double SAFE_EDGE_MARGIN = 7;

	public static double SAFE_POS = -0.2;

	private static final StringBuffer sb = new StringBuffer(300);
	private static final int[] selections = new int[150];
	private static final long serialVersionUID = 6254760984999310075L;
	private static XYSeries series = new XYSeries("Curve");
	private static final double[] shift = new double[MAX_GEARS];
	private static final double SIN_PI_4=0.7071068;	
	public static double SLIGHT_BRAKE = 0.075;
	//	private static double realRad = 0;	
	private static final double SMALL_MARGIN = 0.002;
	public static double speed = 0;
	public static double speedX = 0;
	public static double speedY = 0;
	public static double steer = 0;
	private static final double steerLock = 0.366519;//0.785398;	
	private static final int STRAIGHT = 0;
	public static double STRAIGHT_ANGLE = 0.1;
	public static double TARGET_ANGLE_THRESHOLD = -0.05;
	//	private final static double TCL_SLIP = 2.0;				/* [m/s] range [0..10] */
	private final static double TCL_RANGE = 5.0;			/* [m/s] range [0..10] */
	private static final ObjectArrayList<Segment> tE = ObjectArrayList.wrap(new Segment[100], 0);
	private static Segment[] temp = new Segment[5];
	private static int tempSz = 0;
	public static long ti;
	public static double time = 0;
	private static final double[] tmp = new double[6];
	private static final double[] tmpBuf = new double[6];
	private static final int[] tmpMap = new int[Segment.MAX_RADIUS];
	public static double toInnerEdge = 0;
	public static double toMiddle = 0;
	public static double toOutterEdge = 0;
	private static ObjectArrayList<Segment> track = null;
	private static final TrackSegment[] trackDataArr = new TrackSegment[100];
	private static final TrackSegment[] trackEArr = new TrackSegment[2000];
	private static final TrackSegment[] trackMidArr = new TrackSegment[100];
	public static final ObjectArrayList<TrackSegment> trackData = ObjectArrayList.wrap(trackDataArr, 0);
	//	private static double[] ar = new double[1000];
	private final static Vector2D trackDirection = new Vector2D(0,1);
	private static final ObjectArrayList<TrackSegment> trackE = ObjectArrayList.wrap(trackEArr, 0);

	private static final ObjectArrayList<TrackSegment> trackMid = ObjectArrayList.wrap(trackMidArr, 0);

	//	private static final double MARGIN = 2;
	//	private static final double SLIP_LIMIT = 3.5;
	private static double trackWidth =-1;	
	public static final int[][] trAppearRadius = new int[ARRAY_LENGTH][Segment.MAX_RADIUS];
	public static final Segment[] trArr = new Segment[ARRAY_LENGTH];	
	public static final int[] trIndx = new int[ARRAY_LENGTH];
	public static final int[][] trMap = new int[ARRAY_LENGTH][Segment.MAX_RADIUS];		
	public static int trSz = 0;
	public static int turn = 0;
	private static int turnLeft = 0;
	private static int turnRight = 0;
	public static final int TURNLEFT = -1;
	public static final int TURNRIGHT = 1;
	private static final int[] turns = new int[150];
	static double tW = 0;
	private static final int UNKNOWN = 2;
	public static double VERY_HIGH_SPEEDY = 38;

	private static final double W = 1.94;//1.96
	private static final double GAP = W*0.5;//1.96
	//	private static final double SHIFT = 0.95d;
	//	private static final double SHIFT_MARGIN = 4.0*3.6d;
	//	private static final double DIFFERENTIAL_RATIO = 4.5d;
	//	private static final double[] wheelRadius= new double[]{0.3179,0.3179,0.3276,0.3276};
	//	private static final double[] wheelRadiusInInch= new double[]{8.5,8.5,9,9};
	private static final double[] wheelRadius= new double[]{0.3276,0.3276,0.3276,0.3276};
	private static final double WIDTHDIV = 1;
	private static boolean canGoToLastSeg = false;
	
	private static double relativePosMovement = 0;	
	private static double relativeAngleMovement = 0;
	private static double relativeAngle = 0;
	
	private static double lastRelativePosMovement = 0;
	private static double lastRelativeTargetAngle = 0;
	private static double lastRelativeAngleMovement = 0;
	private static double lastRelativeAngle = 0;
	static {
		for (int i = trackDataArr.length-1;i>=0;--i) 
			trackDataArr[i] = new TrackSegment();
		for (int i = trackMidArr.length-1;i>=0;--i) 
			trackMidArr[i] = new TrackSegment();		
	}
	static{
		for (int i=trArr.length-1;i>=0;--i) {			 
			Segment t= new Segment();
			Segment os = new Segment();
			Segment leftSeg = new Segment();
			Segment rightSeg = new Segment();
			trArr[i] = t;
			t.opp = os;
			os.opp = t;
			t.leftSeg = leftSeg;
			t.rightSeg = rightSeg;
			leftSeg.opp = rightSeg;
			rightSeg.opp = leftSeg;
			leftSeg.points = edgeDetector.left;
			rightSeg.points = edgeDetector.right;
			int[] map = trMap[i];
			int[] appearedRads = trAppearRadius[i];
			t.map = map;;
			t.leftSeg.map = map;
			t.rightSeg.map = map;
			t.appearedRads = appearedRads;
			t.leftSeg.appearedRads = appearedRads;
			t.rightSeg.appearedRads = appearedRads;
			t.appearedRads = trAppearRadius[i];
			t.radCount = 0;			
		}
	}
	private static final int binarySearchFromTo(Vector2D[] list, Vector2D key, int from, int to) {
		Vector2D midVal = list[from];
		if (from>=to) return -from-1;
		double d = midVal.y-key.y;
		if (d<0) d = -d;
		int cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1; 
		if (cmp==0){
			if (from<to){
				midVal = list[from+1];
				double de = midVal.y-key.y;
				if (de<0) de = -de;
				if (de<d) return from+1;
			}
			return from;
		}
		else if (cmp>0) return -from-1;
		midVal = list[to];
		d = midVal.y-key.y;
		if (d<0) d = -d;
		cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1;
		if (cmp==0) {
			if (from<to){
				midVal = list[to-1];
				double de = midVal.y-key.y;
				if (de<0) de = -de;
				if (de<d) return to-1;
			}
			return to;
		}
		else if (cmp<0) return -(to+2);
		while (from <= to) {
			int mid =(from + to)/2;
			midVal = list[mid];
			d = midVal.y-key.y;
			if (d<0) d = -d;
			cmp = (d<=SMALL_MARGIN) ? 0 : (midVal.y>key.y) ? 1 : -1;

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else {
				if (midVal.y>key.y){
					if (mid>from){
						midVal = list[mid-1];
						double de = midVal.y-key.y;
						if (de<0) de = -de;
						if (de<d) return mid-1;
					}					
				} else if (midVal.y<key.y){
					midVal = list[mid+1];
					double de = midVal.y-key.y;
					if (de<0) de = -de;
					if (de<d) return mid+1;
				}
				return mid; // key found
			}
		}
		return -(from + 1);  // key not found.
	}
	// 
	private final static int canGoDirect(Vector2D carDirection,double speedRadius,int turn,double rx,double ry,double cnx,double cny,double dSpeed,int fromSeg,int toSeg,double[] tmp){
		if (!canGotoPoint(carDirection, speedRadius, rx, ry)) return -1;				
		//Now check if we can go direct				
		double nx = carDirection.y;
		double ny = -carDirection.x;
		double temp = speedRadius*turn;
		Segment first = (trSz>0) ?  trArr[trIndx[0]] : null;
		if (turn!=0 && (first!=null && first.type==0 || speedRadius>40)){					
			double cx0 = -nx*temp;
			double cy0 = -ny*temp;			
			double rad = speedRadius;
			//			double distToEdge = Math.abs(toMiddle-turn*tW);

			int szz = Geom.getCircleSpcial(cx0, cy0, rad,rx, ry, cnx, cny, tmpBuf);
			int indx = 0;
			while (indx<szz){
				double tmpx = tmpBuf[indx++];
				double tmpy = tmpBuf[indx++];
				double dx = tmpx-rx;
				double dy = tmpy-ry;
				double k = Math.sqrt(dx*dx+dy*dy);
				dx = cx0 - tmpx;
				dy = cy0 - tmpy;
				double d = k/Math.sqrt(dx*dx+dy*dy);
				double startX = tmpx + dx*d;
				double startY = tmpy + dy*d;				
				//Now tmpx,tmpy is the centre of the circle thru the best point on the last edge
				//k is radius of the circle.
				//Now check if the circle int
				//				TrackSegment ts = TrackSegment.createTurnSeg(cx0, cy0, rad, 0,0,startX, startY);
				//				trackData.add(ts);
				//				
				//				ts = TrackSegment.createTurnSeg(tmpx, tmpy, k, startX, startY,rx,ry);
				//				trackData.add(ts);
				int wEdge = (startY<0 || Math.abs(startX)>40) ? -1 : isCut(cx0, cy0, rad,0,0,startX,startY, fromSeg, toSeg, tmp,GAP);
				if (wEdge==0){							
					if ((k>=dSpeed || k>=speedRadius) && (wEdge= isCut(tmpx, tmpy, k,startX,startY,rx,ry,fromSeg,toSeg, tmp,GAP))==0){						
//						TrackSegment t = TrackSegment.createTurnSeg(tmpx, tmpy, k, startX, startY, rx, ry);
//						trackData.add(t);
						if (startY<=0 && Math.sqrt(tmpx*tmpx+tmpy*tmpy)>k){							
							Geom.ptTangentLine(0, 0, tmpx, tmpy, k, tmp);
							startX = (turn==TURNRIGHT) ? tmp[0] : tmp[2];
							startY = (turn==TURNRIGHT) ? tmp[1] : tmp[3];						
						}	
						double rd = k;						
						if (startY<0){
							startY = ry*0.5;
							double dby = tmpy-startY;
							double dbx = Math.sqrt(k*k - dby*dby);							
							startX = (tmpx>0) ? tmpx-dbx : tmpx+dbx;
						}
						//						if (k>dSpeed && dSpeed>0){
						//							dx = startX - rx;
						//							dy = startY - ry;-
						//							d = Math.sqrt(dx*dx+dy*dy);
						//							if (speedRadius>rd && speedRadius<90){
						//								if (d>=20 && rad<60) 
						//									d *=3;
						//								else if (d>15) d*=2;
						//							}							
						//						}
						tmp[0] =  tmpx;
						tmp[1] = tmpy;
						tmp[2] = Math.max(dSpeed,rd);
						tmp[3] = startX;
						tmp[4] = startY;
//						TrackSegment tt = TrackSegment.createTurnSeg(tmpx, tmpy, k, startX, startY, rx, ry);
//						trackData.add(tt);
//						TrackSegment ttt = TrackSegment.createTurnSeg(cx0, cy0, rad, 0,0,startX, startY);
//						trackData.add(ttt);
						return 0;
					}
				}				
			}
		};

		Geom.getLineLineIntersection(0, 0, carDirection.x, carDirection.y, rx, ry, rx+cny, ry+cnx, tmp);
		double ox = tmp[0];
		double oy = tmp[1];
		//		if (oy>ry) return -1;
		double angle = carDirection.angle(rx,ry);
		double dx = carDirection.x;
		double dy = carDirection.y;
		int tp = angle<0 ? 1 :(angle==0) ? 0 : -1;
		if (tp*turn<0) return -1;

		double dax = ox-rx;
		double day = oy-ry;
		double da = dax*dax+day*day;

		double ddx = dx-ox;
		double ddy = dy-oy;
		double dd = ddx*ddx+ddy*ddy;
		double ratio = Math.sqrt(da/dd);
		double tx = ox + ddx*ratio;
		double ty = oy + ddy*ratio;
		double px = (rx+tx)*0.5;
		double py = (ry+ty)*0.5;
		nx = oy-ry;
		ny = rx-ox;			
		if (Geom.getLineLineIntersection(rx, ry, rx+nx, ry+ny, ox, oy, px, py, tmp)==Geom.PARALLEL) return -1;		
		double cx = tmp[0];
		double cy = tmp[1];
		double r = Math.sqrt(Geom.ptLineDistSq(0, 0, dx,dy,cx, cy,tmp));
		double pointx = tmp[0];
		double pointy = tmp[1];
		if (pointy<=0) {
			if (Math.sqrt(cx*cx+cy*cy)>r){				
				Geom.ptTangentLine(0, 0, cx, cy, r, tmp);
				pointx = (tmp[1]>tmp[3]) ? tmp[0] : tmp[2];
				pointy = (tmp[1]>tmp[3]) ? tmp[1] : tmp[3];				
			} 
		}
		int wEdge = pointy<=0 || speedRadius-r>Math.sqrt(pointx*pointx+pointy*pointy) ? -1 : isCut(cx, cy, r,0,0,pointx,pointy, fromSeg, toSeg, tmp,GAP);
		if (wEdge==0){			
			//			double d = Math.sqrt(pointx*pointx+pointy*pointy);
			//			if (speedRadius>r && speedRadius<90){
			//				if (d>=20 && r<60) 
			//					d *=3;
			//				else if (d>15) d*=2;
			//			}
			tmp[0] = cx;
			tmp[1] = cy;
			tmp[2] = Math.max(dSpeed,r);
			tmp[3] = pointx;
			tmp[4] = pointy;
			return 0;
		}
		return -1;
	}
	public final static boolean canGotoPoint(Vector2D carDirection,double speedRadius,double px,double py){
		double nx = carDirection.y;
		double ny = -carDirection.x;		
		double ox = nx*speedRadius;
		double oy = ny*speedRadius;
		double dx = ox - px;
		double dy = oy - py;
		if (Math.sqrt(dx*dx+dy*dy)>=speedRadius) return true;
		ox = -nx*speedRadius;
		oy = -ny*speedRadius;
		dx = ox - px;
		dy = oy - py;
		if (Math.sqrt(dx*dx+dy*dy)>=speedRadius) return true;
		return false;
	}
	public static final void createSegment(Segment l,Segment r,Segment s){
		if (s==null) return;
		for (int i = s.radCount-1;i>=0;--i){
			int rr = s.appearedRads[i];
			s.map[rr] = 0;
			s.appearedRads[i] = 0;
		}
		double rad = (l.radius+r.radius)*0.5d;
		if (rad>=Segment.MAX_RADIUS) {
			rad = Segment.MAX_RADIUS-1;
			l.type = 0;
			r.type = 0;
		}

		if (l.type==0 && Math.abs(l.start.x-l.end.x)<=TrackSegment.EPSILON*0.1){
			if (r.end==null || l.end.y>r.end.y){
				Segment.toMiddleSegment(l, s, -1, tW);
				Vector2D[] rP = r.points;
				Segment.reSynchronize(l, r, 0, edgeDetector.rSize, 1, tW+tW);
				r.points = rP;
			} else {
				Segment.toMiddleSegment(r, s, 1, tW);
				Vector2D[] lP = l.points;				
				Segment.reSynchronize(r, l, 0, edgeDetector.lSize, -1, tW+tW);				
				l.points = lP;

			}
			s.appearedRads[0] = Segment.MAX_RADIUS-1;
			s.map[Segment.MAX_RADIUS-1] = 1;
			s.radCount = 1;
			s.rightSeg.radCount = 1;
			s.leftSeg.radCount = 1;
			Segment.copy(s.leftSeg,l);						
			Segment.copy(s.rightSeg,r);						
			return;
		}			

		s.radius = rad;
		s.type = l.type;
		double lx,ly,tx,ty,dx,dy,d,t,sx = toMiddle,sy = 0;
		if (l.start!=null && l.center!=null){
			lx = l.start.x;
			ly = l.start.y;		
			tx = l.center.x;
			ty = l.center.y;
			dx = lx-tx;
			dy = ly-ty;
			d = Math.sqrt(dx*dx+dy*dy);
			t = rad/d;
			sx = tx+dx*t;
			sy = ty+dy*t;
		} 
		if (s.start==null || s.start.y>sy){
			if (s.start==null) 
				s.start = new Vector2D(sx,sy);
			else s.start.copy(sx,sy);
		}

		if (r.start!=null && r.center!=null){
			lx = r.start.x;
			ly = r.start.y;
			tx = r.center.x;
			ty = r.center.y;
			dx = lx-tx;
			dy = ly-ty;
			d = Math.sqrt(dx*dx+dy*dy);
			t = rad/d;
			sx = tx+dx*t;
			sy = ty+dy*t;
		}
		if (s.start==null || s.start.y>sy){
			if (s.start==null) 
				s.start = new Vector2D(sx,sy);
			else s.start.copy(sx,sy);
		}

		if (l.end!=null && l.center!=null){
			lx = l.end.x;
			ly = l.end.y;
			tx = l.center.x;
			ty = l.center.y;
			dx = lx-tx;
			dy = ly-ty;
			d = Math.sqrt(dx*dx+dy*dy);
			t = rad/d;
			sx = tx+dx*t;
			sy = ty+dy*t;
		}
		if (s.end==null || s.end.y<sy){
			if (s.end==null) 
				s.end = new Vector2D(sx,sy);
			else s.end.copy(sx,sy);
		}

		if (r.end!=null && r.center!=null){
			lx = r.end.x;
			ly = r.end.y;
			tx = r.center.x;
			ty = r.center.y;
			dx = lx-tx;
			dy = ly-ty;
			d = Math.sqrt(dx*dx+dy*dy);
			t = rad/d;
			sx = tx+dx*t;
			sy = ty+dy*t;
		}
		if (s.end==null || s.end.y<sy){
			if (s.end==null) 
				s.end = new Vector2D(sx,sy);
			else s.end.copy(sx,sy);
		}
		if (l.num>r.num){
			if (l.center!=null) {
				if (s.center!=null)
					s.center.copy(l.center);
				else s.center = new Vector2D(l.center);
			}
		} else {
			if (r.center!=null) {
				if (s.center!=null)
					s.center.copy(r.center);
				else s.center = new Vector2D(r.center);
			}
		}
		int rr = (int)Math.round(rad);
		if (rr>=Segment.MAX_RADIUS-1) rr = Segment.MAX_RADIUS-1; 
		s.appearedRads[0] = rr;
		if (rr>=0 && s.map!=null) s.map[rr] = 1;
		s.radCount = 1;
		s.rightSeg.radCount = 1;
		s.leftSeg.radCount = 1;
		Segment.copy(s.leftSeg,l);						
		Segment.copy(s.rightSeg,r);
	}
	public static void getFirstSegment(Vector2D[] v,int to,Vector2D[] opoints,int otherTo,int tp,double toMiddle,double r,Segment lft,Segment rgt){		
		double ttx = (tp==-1) ? r-toMiddle : r+toMiddle;
		double ty = 0;
		Vector2D last = null;
		Vector2D first = null;	
		lft.points = v;
		rgt.points = opoints;		
		if (tp==0){
			int j = 0;
			first = null;
			int lIndx = -1;
			for (j = 0;j<to;++j){
				last = v[j];
				if (last.y<0) continue;
				if (first==null){
					first = new Vector2D(last);
					lIndx = j;
					continue;
				}								
				double e = last.x - first.x;
				if (e<0) e = -e;
				if (e>=E) break;
			}
			last = (lIndx>=0 && j>0) ? new Vector2D(v[j-1]) : null;
			if (last!=null && last.y<0) last = null;
			int k = 0;
			Vector2D oLast = null;
			int rIndx = -1;
			Vector2D oFirst = null;
			for (k = 0;k<otherTo;++k){
				oLast = opoints[k];		
				if (oLast.y<0) continue;
				if (oFirst==null){
					oFirst = new Vector2D(oLast);
					rIndx = k;
					continue;
				}				
				double e = oLast.x - oFirst.x;
				if (e<0) e = -e;
				if (e>=E) break;				
			}
			oLast = (rIndx>=0 && k>0) ? new Vector2D(opoints[k-1]) : null;
			if (oLast!=null && oLast.y<0) oLast = null;			
			double tw = tW+tW;
			if (oLast!=null && oLast!=oFirst && last!=null && last.y>oLast.y && last==first){
				if (Math.abs(oFirst.x-last.x-tw)>E) {
					last = null;
					first = null;
					j = lIndx;
				} 
			} else if (last!=null && last!=first && oLast!=null && oLast.y>last.y && oLast==oFirst){
				if (Math.abs(first.x-oLast.x+tw)>E) {
					oLast = null;
					oFirst = null;
					k = rIndx;
				} 
			}
			if (oLast!=null && (last==null || oLast.y>last.y)){
				if (last == null ) 
					last = new Vector2D(oFirst.x-tw,oLast.y);
				else {
					if (oLast.y>=v[j].y) return;
					last = new Vector2D(first.x,oLast.y);
				}
			} else if (last!=null && (oLast==null || last.y>oLast.y)){
				if (oLast == null ) 
					oLast = new Vector2D(first.x+tw,last.y);
				else {
					if (last.y>=opoints[k].y) return;
					oLast = new Vector2D(oFirst.x,last.y);
				}
			}

			if (oFirst!=null && (first==null || oFirst.y<first.y)){
				if (first == null ) 
					first = new Vector2D(oFirst.x-tw,oFirst.y);
				else first = new Vector2D(first.x,oFirst.y);
			} else if (first!=null && (oFirst==null || first.y<oFirst.y)){
				if (oFirst == null ) 
					oFirst = new Vector2D(first.x+tw,first.y);
				else oFirst = new Vector2D(oFirst.x,first.y);
			}

			if (lft!=null && last!=null && first!=null && last.y>first.y){				
				lft.type = 0;
				lft.start = first;
				lft.end = last;
				lft.startIndex = (lIndx>=0) ? lIndx : to;
				lft.endIndex = (lIndx>=0) ? j-1 : to-1;
				lft.num = (lIndx>=0) ? j - lft.startIndex : 0;
				lft.radius = 0;
				lft.points = v;
			}

			if (rgt!=null && oLast!=null && oFirst!=null && oLast.y>oFirst.y){
				rgt.type = 0;
				rgt.start = oFirst;
				rgt.end = oLast;
				rgt.startIndex = (rIndx>=0) ? rIndx : otherTo;
				rgt.endIndex = (rIndx>=0) ? k-1 : otherTo-1;
				rgt.num = (rIndx>=0) ? k - rgt.startIndex : 0;
				rgt.radius = 0;
				rgt.points = opoints;
			}
			return;
		}
		double tx = tp*ttx;
		int j = 0;
		double de = -tp*tW;			

		first = null;
		int lIndx = -1;
		int rIndx = -1;
		Vector2D cn = new Vector2D(tx,0);
		for (j = 0;j<to;++j){
			last = v[j];
			if (last.y<0) continue;
			if (lIndx<0) {
				lIndx = j;
				first = last;
				ty = last.y;				
				double dx = tx - last.x;				
				double e = Math.sqrt(dx*dx+ty*ty)-r+de;
				if (e<0) e = -e;
				if (e>=gap) break;
				//				if (last.y>0.1){
				//					cn.x = tx;
				//					cn.y = 0;
				//					cn = Segment.circle(new Vector2D(toMiddle-tW,0), last, cn, r-de);
				//					if (cn.y!=0) break;
				//				}
				continue;
			}
			ty = last.y;				
			double dx = tx - last.x;				
			double e = Math.sqrt(dx*dx+ty*ty)-r+de;
			if (e<0) e = -e;
			if (e>=gap) break;			
			Segment.circle(leftMost, last, tx,0, r-de,cn);
			if (cn.y!=0){				
				if (last.y-1>=first.y){
					Segment.circle(first, last, tx,0, r-de,cn);
					if (cn.y!=0) break;
				} else if (last.y>=1)break;
				//				if (cn.y!=0) break;
			} else if (last.y-1>=first.y){
				Segment.circle(first, last, tx,0, r-de,cn);
				if (cn.y!=0) break;
			}
		}
		last = (lIndx>=0 && j>0) ? v[j-1] : null;
		if (last!=null && last.y<0) last = null;
		if (last!=null && last==first) first = leftMost;
		int k = 0;
		Vector2D oLast = null;

		Vector2D oFirst = null;
		for (k = 0;k<otherTo;++k){
			oLast = opoints[k];		
			if (oLast.y<0) continue;
			if (rIndx<0) {
				rIndx = k;
				oFirst = oLast;
				ty = oLast.y;				
				double dx = tx - oLast.x;				
				double e = Math.sqrt(dx*dx+ty*ty)-r-de;
				if (e<0) e = -e;
				if (e>=gap) break;
				//				if (oLast.y>0.1){
				//					cn.x = tx;
				//					cn.y = 0;
				//					cn = Segment.circle(new Vector2D(toMiddle+tW,0), oLast, cn, r+de);
				//					if (cn.y!=0) break;
				//				}
				continue;
			}
			ty = oLast.y;				
			double dx = tx - oLast.x;				
			double e = Math.sqrt(dx*dx+ty*ty)-r-de;
			if (e<0) e = -e;
			if (e>=gap) break;			
			Segment.circle(rightMost, oLast, tx,0, r+de,cn);
			if (cn.y!=0) {		
				if (oLast.y-1>=oFirst.y){
					Segment.circle(oFirst, oLast, tx,0, r+de,cn);
					if (cn.y!=0) break;
				} else if (oLast.y>=1)break;
			} if (oLast.y-1>=oFirst.y){
				Segment.circle(oFirst, oLast, tx,0, r+de,cn);
				if (cn.y!=0) break;
			}
		}
		oLast = (rIndx>=0 && k>0) ? opoints[k-1] : null;		
		if (oLast!=null && oLast.y<0) oLast = null;
		if (oLast!=null && oLast==oFirst) oFirst = rightMost;

		double mx = 0;
		double my = 0;
		double nx = 0;
		double ny = 0;
		if (last!=null){
			double dx = last.x - tx;
			double dy = last.y;
			double d = Math.sqrt(dx*dx+dy*dy);
			double t = (r+de)/d;
			mx = tx + dx*t;
			my = dy*t;
		}

		while (oLast==null && last!=null && rIndx>=0 && rIndx<otherTo  && my>opoints[rIndx].y && j>=lIndx){
			j--;
			last = (j>=1) ? v[j-1] : null;
			if (last==null || j<lIndx) {
				last = null;
				break;
			}
		}

		if (oLast!=null){
			double dx = oLast.x - tx;
			double dy = oLast.y;
			double d = Math.sqrt(dx*dx+dy*dy);
			double t = (r-de)/d;
			nx = tx + dx*t;
			ny = dy*t;
		}

		while (last==null && oLast!=null && lIndx<to && lIndx>=0 && ny>v[lIndx].y && k>=rIndx) {
			k--;
			oLast = (k>=1) ? opoints[k-1] : null;
			if (oLast==null || k<rIndx) {
				oLast = null;
				break;
			}
		}


		if (last==null && oLast!=null){
			last = new Vector2D(nx,ny);
			double dx = oFirst.x - tx;
			double dy = oFirst.y;
			double d = Math.sqrt(dx*dx+dy*dy);
			double t = (r-de)/d;
			nx = tx + dx*t;
			ny = dy*t;
			if (first==null || first.y>ny) {
				dx = oFirst.x - tx;
				dy = oFirst.y;
				d = Math.sqrt(dx*dx+dy*dy);
				t = (r-de)/d;
				nx = tx + dx*t;
				ny = dy*t;				
				first = new Vector2D(nx,ny);
			}
		} else if (oLast==null && last!=null ){
			oLast = new Vector2D(mx,my);
			double dx = first.x - tx;
			double dy = first.y;
			double d = Math.sqrt(dx*dx+dy*dy);
			double t = (r+de)/d;
			mx = tx + dx*t;
			my = dy*t;
			if (oFirst==null || oFirst.y>my) {
				dx = first.x - tx;
				dy = first.y;
				d = Math.sqrt(dx*dx+dy*dy);
				t = (r+de)/d;
				nx = tx + dx*t;
				ny = dy*t;
				oFirst = new Vector2D(nx,ny);
			}
		} else if (last!=null && oLast!=null){
			if (my>oLast.y){
				boolean ok = true;
				while (ok){
					int kk = k;
					for (;kk<otherTo;++kk){
						Vector2D p = opoints[kk];
						if (p.y>my) break;
					}
					if (kk>k){
						j--;
						last = (j>=1) ? v[j-1] : null;
						if (last==null || j<lIndx) {
							last = null;
							break;
						}
						if (last!=null){
							double dx = last.x - tx;
							double dy = last.y;
							double d = Math.sqrt(dx*dx+dy*dy);
							double t = (r+de)/d;
							mx = tx + dx*t;
							my = dy*t;
							if (my<=oLast.y) ok = false; 
						}						
					} else ok = false;
				}
				if (last!=null&& (oLast==null || my>oLast.y)) oLast = new Vector2D(mx,my);
			} 

			if (last==null || ny>last.y){
				boolean ok = true;
				while (ok){
					int jj = j;
					if (last!=null)
						for (;jj<to;++jj){
							Vector2D p = v[jj];
							if (p.y>ny) break;
						}
					if (jj>j){
						k--;
						oLast = (k>=1) ? opoints[k-1] : null;
						if (oLast==null || k<rIndx) {
							oLast = null;
							break;
						}
						if (oLast!=null){
							double dx = oLast.x - tx;
							double dy = oLast.y;
							double d = Math.sqrt(dx*dx+dy*dy);
							double t = (r-de)/d;
							nx = tx + dx*t;
							ny = dy*t;
							if (ny<=last.y) ok = false;
						}
					} else ok = false;
				}
				if (oLast!=null && (last==null || ny>last.y)) last = new Vector2D(nx,ny);
			}

			if (oFirst!=null){
				double dx = oFirst.x - tx;
				double dy = oFirst.y;
				double d = Math.sqrt(dx*dx+dy*dy);
				double t = (r-de)/d;
				nx = tx + dx*t;
				ny = dy*t;
				if (first==null || ny<first.y){ 
					first = new Vector2D(nx,ny);
				} else {
					dx = first.x - tx;
					dy = first.y;
					d = Math.sqrt(dx*dx+dy*dy);
					t = (r+de)/d;
					nx = tx + dx*t;
					ny = dy*t;
					oFirst = new Vector2D(nx,ny);
				}
			} else if (first!=null){
				double dx = first.x - tx;
				double dy = first.y;
				double d = Math.sqrt(dx*dx+dy*dy);
				double t = (r+de)/d;
				nx = tx + dx*t;
				ny = dy*t;
				oFirst = new Vector2D(nx,ny);
			}
		}

		if (lft!=null && last!=null && last.y>first.y){
			lft.type = tp;
			lft.start = (last==null) ? null : new Vector2D(first);
			lft.end = (last==null) ? null : new Vector2D(last);
			lft.startIndex = (lIndx>=0) ? lIndx : to;
			lft.endIndex = (lIndx>=0) ? j-1 : to-1;
			lft.num = (lIndx>=0) ? j - lft.startIndex : 0;
			lft.radius = r-de;
			lft.points = v;
			if (lft.center==null) 
				lft.center = new Vector2D(tx,0);
			else {
				lft.center.x = tx;
				lft.center.y = 0;
			}
		}

		if (rgt!=null && oLast!=null && oLast.y>oFirst.y){
			rgt.type = tp;
			rgt.start = (oLast==null) ? null : new Vector2D(oFirst);
			rgt.end = (oLast==null) ? null : new Vector2D(oLast);
			rgt.startIndex = (rIndx>=0) ? rIndx : otherTo;
			rgt.endIndex = (rIndx>=0) ? k-1 : otherTo-1;
			rgt.num = (rIndx>=0) ? k - rgt.startIndex : 0;
			rgt.radius = r+de;
			rgt.points = opoints;
			if (rgt.center==null){
				rgt.center = new Vector2D(tx,0);
			} else {
				rgt.center.x = tx;
				rgt.center.y = 0;
			}
		}	
	}
	/**
	 * @param name
	 */
	//	private CircleDriver2(String name) {
	//		super(name);
	//		// TODO Auto-generated constructor stub
	//	}

	//point is relative to car

	/* Temporary comment
	private static final double steerToPoint(NewCarState state,Vector2D point){
		double angle = state.angle;
		if (point==null) return angle/EdgeDetector.steerLock;
		double posX = state.posX;
		double posY = state.posY;
		double cx = state.cx;
		double cy = state.cy;
		Vector2D a = new Vector2D(cx-posX,cy-posY);
		double steer = Math.max(-EdgeDetector.steerLock,Math.min(angle-Math.PI/2+a.angle(point),EdgeDetector.steerLock));		
		return (steer)/EdgeDetector.steerLock;
	}


	private static final double steerAtRadius(CarState state,double cx,double cy,double targetRadius){
		//		double angle = state.angle;				
		//		double speed = state.getSpeed();		
		double d = Math.sqrt(cx*cx+cy*cy);
		//		Vector2D p = new Vector2D(cx,cy).orthogonal().normalised().rotated(angle);
		//		double closeDist = speed*0.04/3.6;				

		//		Vector2D trackDirection = new Vector2D(0,1);		

		double px = (edgeDetector.turn==TURNRIGHT) ? -cy : cy;
		double py = (edgeDetector.turn==TURNRIGHT) ? cx : -cx;
		//		double a = point.angle(carDirection);		
		if (d<targetRadius){	
			double rr = Math.sqrt(targetRadius*targetRadius-d*d)/d;			
			px = px*rr;
			py = py*rr;
			return gotoPoint(state, px,py);
		} else if (d>targetRadius){			
			Geom.ptTangentLine(0, 0, cx, cy, targetRadius,tmp);									
			px = (edgeDetector.turn==TURNRIGHT) ? tmp[0] : tmp[2];
			py = (edgeDetector.turn==TURNRIGHT) ? tmp[1] : tmp[3];
			//			point = points[0];
		}	
		//		return  steerToPoint(state, point);				
		return gotoPoint(state, px,py);
	}//*/

	private static final int getGear(CarState cs) {
		// TODO Auto-generated method stub
		int gear = cs.gear;
		double speedX = cs.speedX/3.6;

		// if gear is 0 (N) or -1 (R) just return 1 
		if (gear<1)
			return 1;

		if (speedX > shift[gear+ GEAR_OFFSET]) {
			while (speedX > shift[gear+ GEAR_OFFSET]) gear++;
		} else if ((gear > 1) && (speedX < (shift[gear-1+GEAR_OFFSET] - 10.0))) {
			while ((gear > 1) && (speedX < (shift[gear-1+GEAR_OFFSET] - 10.0))) gear--;
		}

		while (gear <= 0) {
			gear++;
		}

		return gear;
	}	
	private static final int getGear(double speedX) {
		// TODO Auto-generated method stub				
		// if gear is 0 (N) or -1 (R) just return 1 	
		speedX /= 3.6d;
		for (int i=6;i>=0;--i)
			if (speedX > shift[i+ GEAR_OFFSET]) 
				return i+1;


		return 1;
	}
	private static final double gotoPoint(CarState cs,double px,double py){
				double angle = cs.angle;
//		double angle = PI_2-Math.atan2(speedV.y,speedV.x);
		if (point2Follow==null) 
			point2Follow = new Vector2D(px,py);
		else {
			point2Follow.x = px;
			point2Follow.y = py;
		}
		if (py==0 && px==0) return angle/EdgeDetector.steerLock;
		double pangle = Math.atan2(py, px);
		double steer = Math.max(-EdgeDetector.steerLock,Math.min(angle-PI_2+pangle,EdgeDetector.steerLock));		
		return steer/EdgeDetector.steerLock;

	}
	private static final double gotoPoint(CarState cs,Vector2D point){
				double angle = cs.angle;
//		double angle = PI_2-Math.atan2(speedV.y,speedV.x);
		point2Follow = point;
		if (point==null) return angle/EdgeDetector.steerLock;
		double px = point.x;
		double py = point.y;
		double pangle = Math.atan2(py, px);
		double steer = Math.max(-EdgeDetector.steerLock,Math.min(angle-PI_2+pangle,EdgeDetector.steerLock));
		//		if (steer*lastSteer<0 && Math.abs(steer)<0.2) steer *= 0.5;
		return (steer)/EdgeDetector.steerLock;

	}

	/*static final double guessFirst(Vector2D[] v,int to,Vector2D[] opoints,int otherTo,Segment edge,int wh,double toMiddle,double tW,double nraced){		
		Vector2D cn = new Vector2D();		
		final double MARGIN = 0.005;
		int[] map = new int[Segment.MAX_RADIUS];
		double[] possible = new double[30];
		int[] posTp = new int[30];
		int count = 0;

		double[] cnL = new double[to];  
		for (int i = to-1;i>=2;--i){
			Vector2D last = v[i];
			double ox = (leftMost.x+last.x)*0.5d;
			double oy = (leftMost.y+last.y)*0.5d;
			double nx = leftMost.y - oy  ;
			double ny = ox - leftMost.x;
			Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp);
			double dx = tmp[0]-leftMost.x;
			double dy = tmp[1];
			double dd1 = Math.sqrt(dx*dx+dy*dy);
			dd1 = Math.round(dd1*100.0d)/100.0d;
			cnL[i] = dd1;
		}
		if (time>=BREAK_TIME)
			System.out.println();

		for (int i = 0;i<to;++i){
			Vector2D first = v[i];
			for (int k = to-1;k>i+1;--k){
				Vector2D last = v[k];
				double r = bestIntFitRadL[i][k]+tW;				
				double rr = bestFitRadL[i][k];				
				int tp = bestTpL[i][k];
				if (tp==0) continue;
				double de = wh*tp*tW;
				double ox = (first.x+last.x)*0.5d;
				double oy = (first.y+last.y)*0.5d;
				double nx = first.y - oy  ;
				double ny = ox - first.x;
				Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp);
				double dx = tmp[0]-first.x;
				double dy = tmp[1];
				double dd = Math.sqrt(dx*dx+dy*dy);
				dd = Math.round(dd*100.0d)/100.0d;
				//				double tx = (tp==-1) ? toMiddle-r-de : r+de+toMiddle;
				//				double ttx = (tp==-1) ? toMiddle-rr-de : rr+de+toMiddle;

				double dd1 = cnL[k];

				if (Math.abs(dd1-dd)>0.5){
					//					if (edge!=null && tp == edge.type && Math.abs(r+de-edge.radius)<0.5) return tp*edge.radius;
					//					if (Math.abs(dd-r)>0.5 && Math.abs(dd1-r)>0.5) continue;
					//					if (edge!=null && tp==edge.type && Math.abs(r-edge.radius)<0.5) return edge.type*edge.radius;
					double pr = 0;										
					if (Math.abs(dd-r)<=0.5){						
						pr = (Math.abs(dd-r)<=Math.abs(dd-rr)) ? r : rr;						
					} else {						
						pr = (Math.abs(dd1-r)<=Math.abs(dd1-rr)) ? r : rr;						
					}
					if (pr!=0){
						double prx = (tp==-1) ? toMiddle-pr-de : pr+de+toMiddle;
						cn.x = prx;
						cn.y = 0;
						if (Segment.check(v, i, k+1, cn, pr)<0) continue;
					}

					for (int j=i+1;j<k;++j){
						double sy = allCntryL[i][j][k];
						double rad = allRadiusLeft[i][j][k];
						double tmpr = Math.round(rad+de);
						if (Math.abs(sy)<=MARGIN) {
							if (edge!=null && edge.type==tp && tmpr==edge.radius) return tmpr * tp;
							if (map[(int)tmpr]==0) {
								posTp[count] =tp;
								possible[count++] = (Math.abs(rad+de-tmpr)<=0.2) ? tmpr : rad+de;
							}					
							map[(int)tmpr]++;				
						}
					}
					//					if (edge!=null && tp == edge.type && possibleRad==edge.radius) return tp*edge.radius; 
					continue;
				}

				double pr = Math.round(dd1-tW)+tW;
				double dis = Math.abs(dd1-r);				
				if (Math.abs(rr-dd1)<=0.49 && Math.abs(rr-dd)<=0.49 && Math.abs(r-pr)<=0.49){
					pr = (dis<=0.2 || Math.abs(rr-r)<=0.2 || dis<=Math.abs(dd1-rr)) ? r : dd1;
					double prx = (tp==-1) ? toMiddle-pr-de : pr+de+toMiddle;
					cn.x = prx;
					cn.y = 0;
					if (Segment.check(v, i, k+1, cn, pr)<0) continue;
					cn = Segment.circle(first, last, cn, pr);
					System.out.println(cn);
					if (cn.y>=0.01 || cn.y<=-0.01) continue;
					double minsy = 1;					
					double tmpr = r+de;															
					for (int j=i+1;j<k;++j){
						double sy = allCntryL[i][j][k];
						double rad = allRadiusLeft[i][j][k];
						if (Math.abs(rad-r)<=0.49 && Math.abs(sy)<minsy) {
							if (edge!=null && edge.type==tp && tmpr==edge.radius) return tmpr * tp;
							minsy = Math.abs(sy);
							if (map[(int)tmpr]==0) {
								posTp[count] =tp;
								possible[count++] = tmpr;
							}						
							map[(int)tmpr]++;
						}
					}
					if (minsy>=0.01) continue;
					if (minsy>MARGIN || minsy<-MARGIN) System.out.println("----------------------------------------"+minsy+"    "+(pr+de));											


					continue;
				}									
				//				if (edge!=null && tp == edge.type && pr+de==edge.radius) return tp*edge.radius;
				//				pr = (dis<=0.2 || Math.abs(rr-r)<=0.2 || dis<=Math.abs(dd1-rr) || maxAppearL[i][k]>1) ? r : dd1;				
				//				possibleRad = tp*tmpr;
				double br = Math.round(dd1-tW)+tW;
				for (int j=i+1;j<k;++j){
					double sy = allCntryL[i][j][k];
					double rad = allRadiusLeft[i][j][k];
					double tmpr = Math.round(rad+de);
					if (Math.abs(sy)<=MARGIN  && (Math.abs(rad-br)<=0.49 || Math.abs(rad-r)<=0.49)) {
						if (map[(int)tmpr]==0) {
							posTp[count] =tp;
							possible[count++] = (Math.abs(rad+de-tmpr)<=0.2) ? tmpr : rad+de;						
						}
						map[(int)tmpr]++;
					}
				}								
			}
			if (first.y>0) break;
		}

		double[] cnR = new double[otherTo];  
		for (int i = otherTo-1;i>=2;--i){
			Vector2D last = opoints[i];
			double ox = (rightMost.x+last.x)*0.5d;
			double oy = (rightMost.y+last.y)*0.5d;
			double nx = rightMost.y - oy  ;
			double ny = ox - rightMost.x;
			Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp);
			double dx = tmp[0]-rightMost.x;
			double dy = tmp[1];
			double dd1 = Math.sqrt(dx*dx+dy*dy);
			dd1 = Math.round(dd1*100.0d)/100.0d;
			cnR[i] = dd1;
		}

		for (int i = 0;i<otherTo;++i){
			Vector2D first = opoints[i];
			for (int k = otherTo-1;k>i+1;--k){
				Vector2D last = opoints[k];				
				double r = bestIntFitRadR[i][k]+tW;				
				double rr = bestFitRadR[i][k];
				int tp = bestTpR[i][k];
				if (tp==0) continue;						
				double de = wh*tp*tW;
				double ox = (first.x+last.x)*0.5d;
				double oy = (first.y+last.y)*0.5d;
				double nx = first.y - oy  ;
				double ny = ox - first.x;
				Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp);
				double dx = tmp[0]-first.x;
				double dy = tmp[1];
				double dd = Math.sqrt(dx*dx+dy*dy);
				dd = Math.round(dd*100.0d)/100.0d;
				//				double tx = (tp==-1) ? toMiddle-r-de : r+de+toMiddle;
				//				double ttx = (tp==-1) ? toMiddle-rr-de : rr+de+toMiddle;

				double dd1 = cnR[k];

				if (Math.abs(dd1-dd)>0.5){
					//					if (edge!=null && tp == edge.type && Math.abs(r-de-edge.radius)<0.5) return tp*edge.radius;
					//					if (Math.abs(dd-r)>0.5 && Math.abs(dd1-r)>0.5) continue;
					//					if (edge!=null && tp==edge.type && Math.abs(r-edge.radius)<0.5) return edge.type*edge.radius;
					double pr = 0;					
					if (Math.abs(dd-r)<=0.5){						
						pr = (Math.abs(dd-r)<=Math.abs(dd-rr)) ? r : rr;						
					} else {						
						pr = (Math.abs(dd1-r)<=Math.abs(dd1-rr)) ? r : rr;						
					}
					if (pr!=0){
						double prx = (tp==-1) ? toMiddle-pr+de : pr-de+toMiddle;
						cn.x = prx;
						cn.y = 0;
						if (Segment.check(opoints, i, k+1, cn, pr)<0) continue;
					}
					for (int j=i+1;j<k;++j){
						double sy = allCntryR[i][j][k];
						double rad = allRadiusRight[i][j][k];
						double tmpr = Math.round(rad-de);
						if (Math.abs(sy)<=MARGIN) {
							if (edge!=null && edge.type==tp && edge.radius==tmpr) return tmpr * tp;
							if (map[(int)tmpr]==0) {
								posTp[count] =tp;
								possible[count++] = (Math.abs(rad+de-tmpr)<=0.2) ? tmpr : rad-de;
							}					
							map[(int)tmpr]++;				
						}
					}
					continue;
				}

				double pr = Math.round(dd1-tW)+tW;								
				double dis = Math.abs(dd1-r);
				if (Math.abs(rr-dd1)<=0.49 && Math.abs(rr-dd)<=0.49 && Math.abs(r-pr)<=0.49){					
					pr = (dis<=0.2 || Math.abs(rr-r)<=0.2 || dis<=Math.abs(dd1-rr) || maxAppearR[i][k]>1) ? r : dd1;
					double prx = (tp==-1) ? toMiddle-pr+de : pr-de+toMiddle;
					cn.x = prx;
					cn.y = 0;
					if (Segment.check(opoints, i, k+1, cn, pr)<0) continue;
					cn = Segment.circle(first, last, cn, pr);
					if (cn.y>=0.01 || cn.y<=-0.01) continue;
					double minsy = 1;					
					double tmpr = r-de;															
					for (int j=i+1;j<k;++j){
						double sy = allCntryR[i][j][k];
						double rad = allRadiusRight[i][j][k];
						if (Math.abs(rad-r)<=0.49 && Math.abs(sy)<minsy) {
							if (edge!=null && edge.type==tp && tmpr==edge.radius) return tmpr * tp;
							minsy = Math.abs(sy);
							if (map[(int)tmpr]==0) {
								posTp[count] =tp;
								possible[count++] = tmpr;
							}						
							map[(int)tmpr]++;
						}
					}														
					continue;
				}									
				//				if (edge!=null && tp == edge.type && pr-de==edge.radius) return tp*edge.radius;
				//				pr = (dis<=0.2 || Math.abs(rr-r)<=0.2 || dis<=Math.abs(dd1-rr) || maxAppearR[i][k]>1) ? r : dd1;
				double br = Math.round(dd1-tW)+tW;					
				for (int j=i+1;j<k;++j){
					double sy = allCntryR[i][j][k];
					double rad = allRadiusRight[i][j][k];
					double tmpr = Math.round(rad-de);		
					if (Math.abs(sy)<=MARGIN  && (Math.abs(rad-tW-br)<=0.49 || Math.abs(rad-r)<=0.49)) {
						if (edge!=null && edge.type==tp && edge.radius==tmpr) return tmpr * tp;
						if (map[(int)tmpr]==0) {
							posTp[count] =tp;
							possible[count++] = (Math.abs(rad-de-tmpr)<=0.2) ? tmpr : rad-de;
						}						
						map[(int)tmpr]++;				
					}
				}
			}			
			if (first.y>0) break;
		}

		int max = 0;
		if (edge!=null) map[(int)Math.round(edge.radius)]++;
		int j = 0;
		for (int i = count-1;i>=0;--i){
			double rad = possible[i];
			int rr = (int)rad;
			if (max<map[rr]){
				j = i;
				max = map[rr];
			} else if (max==map[rr] && edge!=null && edge.type==posTp[i] && rr==Math.round(edge.radius)){
				j = i;
				max = map[rr];
			}
		}

		if (max>1){
			return posTp[j]*possible[j];
		}
		return -1;
	}//*/

	private final void guessTurn(){
		//		Vector2D[] left = edgeDetector.left;
		//		Vector2D[] right = edgeDetector.right;
		//		int sL = edgeDetector.lSize;
		//		int sR = edgeDetector.rSize;	

		//		Vector2D hh = edgeDetector.highestPoint;
		int sL = edgeDetector.lSize;
		int sR = edgeDetector.rSize;
		Vector2D[] left = edgeDetector.left;
		Vector2D[] right = edgeDetector.right;

		Segment last = (trSz>0) ? trArr[ trIndx[trSz-1] ] : null;


		/*if (last!=null && inTurn){
			if (last.type!=0 && last.center!=null && hh !=null){
				double d = last.center.distance(hh);
				if (Math.abs(Math.abs(d-last.radius)-trackWidth/2)>4*TrackSegment.EPSILON){						
					detected = true;
				} else detected = false; 
			} else if (hh !=null && last.type==0){
				double d = Math.sqrt(Geom.ptLineDistSq(last.start.x, last.start.y, last.end.x, last.end.y, hh.x, hh.y, null));
				if (Math.abs(d-trackWidth/2)>4*TrackSegment.EPSILON){						
					detected = true;
				} else detected = false;
			}

		}//*/


		if (inTurn){
			Segment seg = null;
			if (trSz>0)
				for (int i=trSz-1;i>=0;--i){
					seg = trArr[ trIndx[i] ];
					if (seg.type!=0) break;
				}
			if (seg !=null && seg.type!=0){
				edgeDetector.center = seg.center;
				edgeRadius = seg.radius;
				turn = seg.type;
				edgeDetector.turn = turn;				
				//				if (time<17.35){
				//					if (r.size()>1){
				//						Segment lst = r.get(r.size()-1);
				//						Segment prev = r.get(r.size()-2);
				//						if (lst.type==Segment.UNKNOWN && lst.num>=1){
				//							Vector2D center = prev.center;
				//							double rad = prev.radius;
				//							double[] r = new double[3];
				//							Vector2D p = (lst.num>=2) ? lst.points[lst.num-1] : edgeDetector.highestPoint;
				//							Geom.getCircle4(lst.points[0], p , center, rad, r);
				//							System.out.println("Heeeeeeeeeeeeeee    "+new Vector2D(r[0],r[1]).distance(center)+"    "+(Math.round(r[2]-tW)+tW));					
				//						}
				//					}
				//				} else if (l.size()>1){
				//					Segment lst = l.get(l.size()-1);
				//					Segment prev = l.get(l.size()-2);
				//					if (lst.type==Segment.UNKNOWN && lst.num>=1){
				//						Vector2D center = prev.center;
				//						double rad = prev.radius;
				//						double[] r = new double[3];
				//						Vector2D p = (lst.num>=2) ? lst.points[lst.num-1] : edgeDetector.highestPoint;
				//						Geom.getCircle5(lst.points[0], p , center, rad, r);
				//						System.out.println("Heeeeeeeeeeeeeee    "+new Vector2D(r[0],r[1]).distance(center)+"    "+(Math.round(r[2]-tW)+tW));					
				//					}
				//				}

			} 
			//			else inTurn = false;
		} else if (!inTurn && curSeg!=null && curSeg.type==0){
			//			detected = false;
			//			if (lhps!=null) lhps.clear();
			if (trSz>0){
				boolean ok = false;
				for (int i=trSz-1;i>=0;--i){
					Segment s = trArr[ trIndx[i] ];
					if (s.type!=0) {						
						edgeDetector.center = s.center;
						edgeRadius = s.radius;
						turn = s.type;
						edgeDetector.turn = turn;
						ok = true;
						break;
					}
				}

				if (!ok){					
					Segment t = trArr[ trIndx[0] ].leftSeg;
					int sNum = sL-t.endIndex-1;					

					if (sNum<=1){
						Segment t1 = trArr[ trIndx[0]].rightSeg;
						sNum = sR-t1.endIndex-1;						
					}

					if (sNum<=1) return;
					////					if (s.num>=1 && s.points!=null && edgeDetector.whichE==0 && edgeDetector.highestPoint.length()<EdgeDetector.MAX_DISTANCE && ((which==-1 && ls>0 && s==lArr[ls-1]) || (which==1 && rs>0 && s==rArr[rs-1]))){ 
					////						s.addPoint(edgeDetector.highestPoint);						
					////						if (s.num>=2 && t.type==0){
					////							double[] rr = new double[3];
					////							Geom.getCircle2(s.start, s.end, t.start, t.end, rr);					
					////							rr[2] = Math.sqrt(rr[2]);
					////							if (rr[2]<1000){						
					////								edgeDetector.center = new Vector2D(rr[0],rr[1]);
					////								turn =  (rr[0]<0) ? -1 : 1; 
					////								edgeDetector.turn = turn;	
					////								edgeRadius = Math.round(rr[2]+turn*which*trackWidth*0.5);								
					////								s.type = turn;
					////								Vector2D p = s.start.plus(s.end).times(0.5);
					////								Vector2D n = new Vector2D(rr[0]-p.x,rr[1]-p.y).normalised();
					////								s.radius = edgeRadius-turn*which*trackWidth*0.5;
					////								double d = s.radius*s.radius;
					////								if (s.map==null) s.map = new int[Segment.MAX_RADIUS]; 
					////								int rad = Segment.double2int(edgeRadius); 
					////								s.map[rad] = 1;
					////								s.minR = rad;
					////								s.maxR = rad;
					////								p = p.plus(n.times(Math.sqrt(d-s.start.distanceSq(p))));
					////								s.center = p;
					////								//						s.seg = TrackSegment.createTurnSeg(s.center.x, s.center.y, s.radius, s.start.x, s.start.y, s.end.x, s.end.y);						
					////								Segment tmp = Segment.toMiddleSegment(s, which, tW);
					//////								if (which==-1) 
					//////									Segment.estimateDist(lm.get(lm.size()-1), tmp);
					//////								else if (which==1) Segment.estimateDist(rm.get(rm.size()-1), tmp);
					////								trArr[ts++] = tmp;
					////								tr.size(ts);
					////							}
					////						}
					//					} else {
					//						inTurn = false;
					//						edgeRadius = Double.MAX_VALUE;
					//					}
				}

			}
		}

		if (trSz>0 && inTurn){
			Segment lSeg = last.leftSeg;
			Segment rSeg = last.rightSeg;
			int lastTurnL = turnLeft;
			int lastTurnR = turnRight;
			turnLeft = 0;
			turnRight = 0;
			int numL = sL - lSeg.endIndex-1; 
			int numR = sR - rSeg.endIndex-1;
			if (numL<0 || numR<0){
				inTurn = true;				
				edgeDetector.lSize = edgeDetector.nLsz;
				edgeDetector.rSize = edgeDetector.nRsz;
				sL = edgeDetector.lSize;
				sR = edgeDetector.rSize;
				if (sL>0) {
					for (int i = sL-1;i>=0;--i)			
						left[i].copy(EdgeDetector.nleft[i]);					
				}
				if (sR>0) {
					for (int i = sR-1;i>=0;--i)
						right[i].copy(EdgeDetector.nright[i]);					
				}

				for (int i = trSz -1 ;i>=0;--i)
					occupied[ trIndx[i] ] = 0;
				trSz = 0;
				lMap.setRowLen(sL);
				rMap.setRowLen(sR);
				combine(null, left, sL, right,sR);
				sL = edgeDetector.lSize;
				sR = edgeDetector.rSize;	
				last = (trSz>0) ? trArr[ trIndx[trSz-1] ] : null;
				if (trSz>0){
					lSeg = last.leftSeg;
					rSeg = last.rightSeg;
					lastTurnL = turnLeft;
					lastTurnR = turnRight;
					turnLeft = 0;
					turnRight = 0;
					numL = sL - lSeg.endIndex-1; 
					numR = sR - rSeg.endIndex-1;
				}
				//				first = (trSz==0) ? null : trArr[ trIndx[0] ];
			}
			int oldTrSz = trSz;
			int oldTurnL = turnLeft;
			int oldTurnR = turnRight;

			//			int oldSl = sL;
			//			int oldSr = sR;
			Vector2D nL = null;
			Vector2D nR = null;			
			boolean isLastConfirm = (lastTurnL==lastTurnR && lastTurnL!=0);
			boolean reCheck = false;


			edgeDetector.lSize = sL;
			edgeDetector.rSize = sR;

			while (true){
				if (numL==1){
					Vector2D v = left[lSeg.endIndex+1];
					turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(lSeg.end.x-v.x)<E) ? 0 : (lSeg.type==0) ? turn : (v.x>lSeg.end.x) ? 1 : -1;

				} else if (numL>1){
					Vector2D p1 = left[lSeg.endIndex+1];
					Vector2D p2 = left[sL-1];
					if (p2.y-1>p1.y){
						turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>p1.x) ? 1 : -1;
					} else turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>lSeg.end.x) ? 1 : -1;				
				}


				if (numR==1){
					Vector2D v = right[rSeg.endIndex+1];
					turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(rSeg.end.x-v.x)<E) ? 0 : (rSeg.type==0) ? turn : (v.x>rSeg.end.x) ? 1 : -1;
				} else if (numR>1){
					Vector2D p1 = right[rSeg.endIndex+1];
					Vector2D p2 = right[sR-1];
					if (p2.y-1>p1.y){
						turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>p1.x) ? 1 : -1;
					} else turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>rSeg.end.x) ? 1 : -1;
				}				
				if (numL==0 && numR==0 || turnLeft==0 && turnRight==0) break;
				oldTurnL = turnLeft;
				oldTurnR = turnRight;

				if (last!=null && !last.unsafe && (turnLeft==turnRight && turnLeft!=0 || turnLeft==0 || turnRight==0)){
					if (last.type*turnLeft<0 || last.type*turnRight<0){
						lSeg.type = Segment.UNKNOWN;
						rSeg.type = Segment.UNKNOWN;
						last.type = Segment.UNKNOWN;
						trSz--;
						occupied[ trIndx[trSz] ] = 0;
						last = (trSz>0) ? trArr[ trIndx[trSz-1] ] : null;
						if (last!=null){
							lSeg = last.leftSeg;
							rSeg = last.rightSeg;
						}						
						numL = sL - lSeg.endIndex-1; 
						numR = sR - rSeg.endIndex-1;
						oldTrSz = trSz;
						continue;
					}
				}
				break;
			}

			reCheck = isLastConfirm && (turnLeft!=0 && turnLeft!=lastTurnL || turnRight!=0 && turnRight!=lastTurnR);
			if (!reCheck && (numL>0 || numR>0)){
				int ix = lSeg.endIndex+1;
				for (int i = 0;i<numL;++i){
					Vector2D v = left[ix++];
					if (v.distance(rSeg.end)<=trackWidth-EdgeDetector.EPS){
						reCheck = true;
						break;
					} 

					if (numL>1 && (v.x-lSeg.end.x)*turnLeft<0){
						reCheck = true;
						break;
					}
				}

				if (!reCheck){
					ix = rSeg.endIndex+1;
					for (int i = 0;i<numR;++i){
						Vector2D v = right[ix++];
						if (v.distance(lSeg.end)<=trackWidth-EdgeDetector.EPS){
							reCheck = true;
							break;
						}
						if (numR>1 && (v.x-rSeg.end.x)*turnRight<0){
							reCheck = true;
							break;
						}
					}
				}
			}

			int indxL = 0;
			int indxR = 0;
			if (reCheck || (turnLeft!=0 && turnRight!=0 && turnLeft!=turnRight) || (turnLeft!=0 && turnLeft*edgeDetector.whichE>0) || (turnRight!=0 && turnRight*edgeDetector.whichE>0 && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.y<60)){				

				if (numL+numR>0){
					Vector2D highest = null;
					double h = 0;
					Vector2D[] allPoints = new Vector2D[Math.abs(numL)+Math.abs(numR)+1];
					int num = 0;				
					for (int i = 0;i<numL;++i){
						Vector2D v = left[i+lSeg.endIndex+1];
						allPoints[num++] = v;
						if (h<v.y){
							h = v.y;
							highest = v;
						}
					}

					for (int i = 0;i<numR;++i){
						Vector2D v = right[i+rSeg.endIndex+1];
						allPoints[num++] = v;
						if (h<v.y){
							h = v.y;
							highest = v;
						}
					}

					if (trSz>0){
						Segment lSg = trArr[trIndx[trSz-1]].leftSeg;
						if (highest==null || lSg.end.y>highest.y) highest = lSg.end;
						lSg = trArr[trIndx[trSz-1]].rightSeg;
						if (highest==null || lSg.end.y>highest.y) highest = lSg.end;
					}

					int n = 0;
					int oldNumL = numL;
					int oldNumR = numR;
					numL = 0;
					numR = 0;
					for (int i=num-1;i>=0;--i){
						Vector2D v = allPoints[i];
						if (v.y<=0) continue;
						double angle = Vector2D.angle(highest.x, highest.y, v.x, v.y);
						double dv = v.distance(highest); 
						if (dv>trackWidth){
							if (angle<0){
								int j = binarySearchFromTo(left, v, 0, sL-1);
								numL++;
								if (j<0){
									sL = EdgeDetector.join(v, left, sL, trArr, trIndx, trSz, -1);
									sR = (sR<=1) ? 0 :remove(right, sR, v);
								}
							} else {
								int j = binarySearchFromTo(right, v, 0, sR-1);
								numR++;
								if (j<0){
									sR = EdgeDetector.join(v, right, sR, trArr, trIndx, trSz, 1);
									sL = (sL<=1) ? 0 : remove(left, sL, v);
								}
							}
							allPoints[i] = null;
							n++;
						}						
					}

					if (n<num){
						Vector2D p1 = new Vector2D();
						Vector2D p2 = new Vector2D();
						for (int i=num-1;i>=0;--i){
							Vector2D v = allPoints[i];
							if (v==null) continue;
							EdgeDetector.findNearestPoint(v, left, sL, p1);
							EdgeDetector.findNearestPoint(v, right, sR, p2);
							double dv1 = v.distance(p1);
							double dv2 = v.distance(p2);
							if (dv1<trackWidth && dv2<trackWidth){
								if (dv1<dv2){
									int j = binarySearchFromTo(left, v, 0, sL-1);
									numL++;
									if (j<0){
										sL = EdgeDetector.join(v, left, sL, trArr, trIndx, trSz, -1);
										sR = (sR<=1) ? 0 :remove(right, sR, v);
									}
								} else {
									int j = binarySearchFromTo(right, v, 0, sR-1);
									numR++;
									if (j<0){
										sR = EdgeDetector.join(v, right, sR, trArr, trIndx, trSz, 1);
										sL = (sL<=1) ? 0 : remove(left, sL, v);
									}
								}
								allPoints[i] = null;
								n++;
							} else if (dv1<=trackWidth-EdgeDetector.EPS){
								int j = binarySearchFromTo(left, v, 0, sL-1);
								numL++;
								if (j<0){									
									sL = EdgeDetector.join(v, left, sL, trArr, trIndx, trSz, -1);
									sR = (sR<=1) ? 0 : remove(right, sR, v);
								}
								allPoints[i] = null;
								n++;
							} else if (dv2<=trackWidth-EdgeDetector.EPS){
								int j = binarySearchFromTo(right, v, 0, sR-1);
								numR++;
								if (j<0){									
									sR = EdgeDetector.join(v, right, sR, trArr, trIndx, trSz, 1);
									sL = (sL<=1) ? 0 :remove(left, sL, v);
								}
								allPoints[i] = null;
								n++;
							}
						}//end of for

						turnLeft = 0;
						turnRight = 0;
						Segment[] oalArr = Segment.oalArr;
						if (numL==1){
							Vector2D v = left[lSeg.endIndex+1];
							turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(lSeg.end.x-v.x)<E) ? 0 : (lSeg.endIndex>=0 && lSeg.num>0 && v.x>right[lSeg.endIndex].x || v.x>lSeg.end.x) ? 1 : -1;

						} else if (numL>1){
							p1 = left[lSeg.endIndex+1];
							p2 = left[lSeg.endIndex+numL];
							int liSz = Segment.bestGuess(left, lSeg.endIndex+1, lSeg.endIndex+numL+1, tW, lSeg, null, oalArr, 0);
							if (liSz>0){
								turnLeft = oalArr[liSz-1].type;
							} else {
								if (p2.y-1>p1.y){
									turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>p1.x) ? 1 : -1;
								} else turnLeft = (lSeg.type==0 && isFirstSeg(lSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>lSeg.end.x) ? 1 : -1;								
							}
						}


						if (numR==1){
							Vector2D v = right[rSeg.endIndex+1];
							turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(rSeg.end.x-v.x)<E) ? 0 : (rSeg.endIndex>=0 && rSeg.num>0 && v.x>right[rSeg.endIndex].x || v.x>rSeg.end.x) ? 1 : -1;
						} else if (numR>1){
							int liSz = Segment.bestGuess(right, rSeg.endIndex+1, rSeg.endIndex+numR+1, -tW, rSeg, null, oalArr, 0);
							if (liSz>0){
								turnRight = oalArr[liSz-1].type;
							} else {
								p1 = right[rSeg.endIndex+1];
								p2 = right[rSeg.endIndex+numR];
								if (p2.y-1>p1.y){
									turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>p1.x) ? 1 : -1;
								} else turnRight = (rSeg.type==0 && isFirstSeg(rSeg) && Math.abs(p2.x-p1.x)<E) ? 0 : (p2.x>rSeg.end.x) ? 1 : -1;
							}
						}


						if (n<num && turnLeft==turnRight && turnLeft!=0){							
							for (int i=num-1;i>=0;--i){
								Vector2D v = allPoints[i];
								if (v==null) continue;
								if (turnLeft==-1){
									int j = binarySearchFromTo(right, v, 0, sR-1);
									numR++;
									if (j<0){
										sR = EdgeDetector.join(v, right, sR, trArr, trIndx, trSz, 1);
										sL = remove(left, sL, v);
									}
								} else {
									int j = binarySearchFromTo(left, v, 0, sL-1);
									numL++;
									if (j<0){
										sL = EdgeDetector.join(v, left, sL, trArr, trIndx, trSz, -1);
										sR = remove(right, sR, v);
									}
								}
								allPoints[i] = null;
								n++;
								if (n==num) break;
							}//end of for							
						}						
					}
					edgeDetector.lSize = sL;
					edgeDetector.rSize = sR;
					if (turnLeft==turnRight && turnLeft!=0 && trSz>0){														
						trSz = Segment.reUpdate(trArr, trSz, tW, trSz-1);																									
						for (int i =oldTrSz;i<trSz;++i){
							Segment t = trArr[trIndx[i]];
							Segment.toMiddleSegment(t.leftSeg, t,-1, tW);
							t.radCount = t.leftSeg.radCount;
						}		
						if (lSeg.type==Segment.UNKNOWN){
							occupied[ trIndx[oldTrSz-1] ] = 0;
							if (trSz-oldTrSz>0) System.arraycopy(trIndx, oldTrSz, trIndx, oldTrSz-1, trSz-oldTrSz);				
							trSz--;
							last.type = Segment.UNKNOWN;				
						}
						last.radCount = lSeg.radCount;


						for (int i = trSz-1;i>=0;--i){
							last = trArr [ trIndx[ i] ];
							if (last.type!=0 && last.type!=Segment.UNKNOWN) break;
						}
						lSeg = last.leftSeg;
						rSeg = last.rightSeg;
						numL = sL - lSeg.endIndex-1; 
						numR = sR - rSeg.endIndex-1;												

						if (trSz<=oldTrSz){
							turn = turnLeft;							
						} else {
							turn = lSeg.type==0 ? lSeg.end.x>lSeg.start.x ? 1 : -1 : lSeg.type;
						}
						edgeDetector.center = (last.type==0 || last.type==Segment.UNKNOWN) ? null : last.center;
						edgeDetector.turn = turn;
						return;
					} else {
						if (oldNumL==numL){
							nL = null;
							int idx = rSeg.endIndex+1;	
							for (int i = oldNumR-1;i>=0;--i){
								Vector2D v = right[idx+i];
								boolean confirm = (v.distance(rSeg.end)<=trackWidth-EdgeDetector.EPS || idx+i>0 && v.distance(right[idx+i-1])<=trackWidth-EdgeDetector.EPS);
								if (!confirm){
									nR = new Vector2D(v);
									indxR = idx+i;
									break;
								}
							}
						} else if (oldNumR==numR){
							nR = null;
							int idx = lSeg.endIndex+1;	
							for (int i = oldNumL-1;i>=0;--i){
								Vector2D v = left[idx+i];
								boolean confirm = (v.distance(lSeg.end)<=trackWidth-EdgeDetector.EPS || idx+i>0 && v.distance(left[idx+i-1])<=trackWidth-EdgeDetector.EPS);
								if (!confirm){
									nL = new Vector2D(v);
									indxL = idx+i;
									break;
								}
							}
						} else {
							int idx = rSeg.endIndex+1;	
							for (int i = oldNumR-1;i>=0;--i){
								Vector2D v = right[idx+i];
								boolean confirm = (v.distance(rSeg.end)<=trackWidth-EdgeDetector.EPS || idx+i>0 && v.distance(right[idx+i-1])<=trackWidth-EdgeDetector.EPS);
								if (!confirm){
									nR = new Vector2D(v);
									indxR = idx+i;
									break;
								}
							}

							idx = lSeg.endIndex+1;	
							for (int i = oldNumL-1;i>=0;--i){
								Vector2D v = left[idx+i];
								boolean confirm = (v.distance(lSeg.end)<=trackWidth-EdgeDetector.EPS || idx+i>0 && v.distance(left[idx+i-1])<=trackWidth-EdgeDetector.EPS);
								if (!confirm){
									nL = new Vector2D(v);
									indxL = idx+i;
									break;
								}
							}
						}
						numL = oldNumL;
						numR = oldNumR;
					}
				}

//				System.out.println("Check immediately");

				if (nL==null && nR==null){					
					if (turnLeft==turnRight && turnLeft!=0) 
						turn = turnLeft;
					else if (turnLeft==0 && turnRight!=0) 
						turn = turnRight;
					else if (turnRight==0 && turnLeft!=0)
						turn = turnLeft;
					edgeDetector.turn = turn;
					//					System.out.println("Check Immediately");
					return;
				} else if (nL!=null && (nR==null && indxL==sL-1 || nR!=null && nL.y>=nR.y && nL.x<nR.x)){
					if (sL-indxL-1>0) {
						for (int i = indxL;i<sL-1;++i){
							Vector2D v = left[i];
							v.copy(left[i+1]);
						}
					}
					sL--;					
					sR = EdgeDetector.join(nL, right, sR, trArr, trIndx, trSz, 1);		
					boolean isHighestPoint = nL!=null && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.distance(nL)<1;
					if (isHighestPoint) edgeDetector.whichE = 1;					
					numL--;
					numR++;
					int indx = binarySearchFromTo(right, nL, 0, sR-1);
					int j = trSz-1;
					boolean ok = (indx>0 && nL.distance(right[indx-1])<=trackWidth-EdgeDetector.EPS ) || (indx>=0 && indx<sR-1 && nL.distance(right[indx+1])<=trackWidth-EdgeDetector.EPS);
					for (;j>=0;--j){
						Segment l = trArr[ trIndx[j] ].rightSeg;
						if (l.start.y-SMALL_MARGIN<=nL.y && l.end.y+SMALL_MARGIN>=nL.y) {
							if (!ok && (l.start.distance(nL)<=trackWidth-EdgeDetector.EPS || l.end.distance(nL)<=trackWidth-EdgeDetector.EPS || 
									(nL.x-l.start.x)*(nL.x-l.end.x)<=0 || l.isPointBelongToSeg(nL)) ) {
								ok = true;
							}
							break;
						}
						if (l.end.y+SMALL_MARGIN<nL.y) {
							ok = true;
							j++;
							break;
						}
					}
					edgeDetector.lSize = sL;
					edgeDetector.rSize = sR;
					if (ok) {
						trSz = Segment.reUpdate(trArr, trSz, tW, j);
						if (trSz>oldTrSz){
							Segment lst = trArr[ trIndx[trSz-1] ];
							Segment bfLst =  trArr[ trIndx[trSz-2] ];
							if (bfLst.type!=0 && lst.start.y>50 && lst.type!=0 && lst.radius<bfLst.radius){
								turn = lst.type;
								lst.type = Segment.UNKNOWN;
								occupied[ trIndx[trSz-1] ] = 0;
								trSz--;
							}
						}
					}

					if (!reCheck && (!ok || !CircleDriver2.inTurn && oldTrSz==trSz)){
						if (indx>=0 && sR-indx-1>0) {
							for (int i = indx;i<sR-1;++i){
								Vector2D v = right[i];
								v.copy(right[i+1]);
							}							
						}
						sR--;
						for (int i = j;i<trSz;++i){			
							if (i<0) continue;
							Segment l = trArr[ trIndx[i] ].rightSeg;
							if (l.startIndex>=indx) l.startIndex--;
							if (l.endIndex>=indx) l.endIndex--;
						}
						sL = EdgeDetector.join(nL, left, sL, trArr, trIndx, trSz, -1);
						if (isHighestPoint) edgeDetector.whichE = -1;
					} else if (trSz>0){
						last = trArr[ trIndx[trSz-1] ];
						lSeg = last.leftSeg;
						rSeg = last.rightSeg;
						if (oldTrSz==trSz && numL>0 && numR>0 && oldTurnL==oldTurnR){
							turn = oldTurnL;
							turnLeft = oldTurnL;
							turnRight = oldTurnL;
						} else {
							turn = (last.type==0) ? last.end.x>last.start.x ? 1 : -1 : last.type;
							turnLeft = turn;
							turnRight = turn;
						}
					}
				} else if (nR!=null && (nL==null && indxR==sR-1 || nL!=null && nL.y<nR.y && nL.x<nR.x)){
					if (sR-indxR-1>0) {
						for (int i = indxR;i<sR-1;++i){
							Vector2D v = right[i];
							v.copy(right[i+1]);
						}
					}
					sR--;					
					sL = EdgeDetector.join(nR, left, sL, trArr, trIndx, trSz, -1);	
					boolean isHighestPoint = nR!=null && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.distance(nR)<1;
					if (isHighestPoint) edgeDetector.whichE = -1;									
					numR--;
					numL++;
					int indx = binarySearchFromTo(left, nR, 0, sL-1);
					int j = trSz-1;
					boolean ok = (indx>0 && nR.distance(left[indx-1])<=trackWidth-EdgeDetector.EPS ) || (indx>=0 && indx<sL-1 && nR.distance(left[indx+1])<=trackWidth-EdgeDetector.EPS);
					for (;j>=0;--j){
						Segment l = trArr[ trIndx[j] ].leftSeg;
						if (l.start.y-SMALL_MARGIN<=nR.y && l.end.y+SMALL_MARGIN>=nR.y) {
							if (!ok && (l.start.distance(nR)<=trackWidth-EdgeDetector.EPS || l.end.distance(nR)<=trackWidth-EdgeDetector.EPS || 
									(nR.x-l.start.x)*(nR.x-l.end.x)<=0 || l.isPointBelongToSeg(nR)) ){
								ok = true;
							}
							break;
						}
						if (l.end.y+SMALL_MARGIN<nR.y) {
							ok = true;
							j++;
							break;
						}
					}
					edgeDetector.lSize = sL;
					edgeDetector.rSize = sR;
					if (ok) 
						trSz = Segment.reUpdate(trArr, trSz, tW, j);

					if (!reCheck && (!ok || !CircleDriver2.inTurn && oldTrSz==trSz || trSz>oldTrSz)){
						if (indx>=0 && sL-indx-1>0) {
							for (int i = indx;i<sL-1;++i){
								Vector2D v = left[i];
								v.copy(left[i+1]);
							}							
						}
						sL--;
						for (int i = j;i<trSz;++i){
							if (i<0) continue;
							Segment l = trArr[ trIndx[i] ].leftSeg;
							if (l.startIndex>=indx) l.startIndex--;
							if (l.endIndex>=indx) l.endIndex--;
						}
						sR = EdgeDetector.join(nR, right, sR, trArr, trIndx, trSz, 1);
						if (isHighestPoint) edgeDetector.whichE = 1;
					} else if (trSz>0){
						last = trArr[ trIndx[trSz-1] ];
						lSeg = last.leftSeg;
						rSeg = last.rightSeg;
						turn = (last.type==0) ? last.end.x>last.start.x ? 1 : -1 : last.type;
						turnLeft = turn;
						turnRight = turn;
					}
				}										
			} else {//Manually check if num==3
				turn = (turnLeft!=0) ? turnLeft : (turnRight!=0) ? turnRight : edgeDetector.turn;
				if (numL>0 && numR>0 && numL+numR==3){
					Vector2D highest = null;
					double h = 0;
					int wE = 0;

					for (int i = 0;i<numL;++i){
						Vector2D v = left[i+lSeg.endIndex+1];						
						if (h<v.y){
							h = v.y;
							highest = v;
							wE = -1;
						}
					}

					for (int i = 0;i<numR;++i){
						Vector2D v = right[i+rSeg.endIndex+1];						
						if (h<v.y){
							h = v.y;
							highest = v;
							wE = 1;
						}
					}

					switch (wE) {
					case -1:
						sL--;
						sR = EdgeDetector.join(highest, right, sR, trArr, trIndx, trSz, 1);
						break;

					case 1:
						sR--;
						sL = EdgeDetector.join(highest, left, sL, trArr, trIndx, trSz, -1);
						break;
					default:
						break;
					}

					edgeDetector.lSize = sL;
					edgeDetector.rSize = sR;					
					trSz = Segment.reUpdate(trArr, trSz, tW, trSz);
					boolean good = false;
					if (trSz>oldTrSz){
						last = trArr[ trIndx[trSz-1] ];
						lSeg = last.leftSeg;
						rSeg = last.rightSeg;
						Segment bfLst =  (trSz>=2) ? trArr[ trIndx[trSz-2] ] : null;
						good = true;
						if (bfLst!=null && bfLst.type!=0 && last.start.y>50 && last.type!=0 && last.radius<bfLst.radius){
							good = false;
							turn = last.type;
							last.type = Segment.UNKNOWN;
							lSeg.type = Segment.UNKNOWN;
							rSeg.type = Segment.UNKNOWN;
							occupied[ trIndx[trSz-1] ] = 0;
							trSz--;
							last = trArr[ trIndx[trSz-1] ];
							lSeg = last.leftSeg;
							rSeg = last.rightSeg;
						} else {
							turn = (last.type==0) ? last.end.x>last.start.x ? 1 : -1 : last.type;
							turnLeft = turn;
							turnRight = turn;
						}

					}

					if (!good){
						switch (wE) {
						case -1:							
							sL = EdgeDetector.join(highest, left, sL, trArr, trIndx, trSz, -1);
							sR = remove(right, sR, highest);
							break;

						case 1:
							sR = EdgeDetector.join(highest, right, sR, trArr, trIndx, trSz, 1);
							sL = remove(left, sL, highest);							
							break;
						default:
							break;
						}

					}													
				} 
			}

			edgeDetector.lSize = sL;
			edgeDetector.rSize = sR;
			if (oldTrSz<trSz){
				for (int i =oldTrSz;i<trSz;++i){
					Segment t = trArr[trIndx[i]];
					Segment.toMiddleSegment(t.leftSeg, t,-1, tW);
					t.radCount = t.leftSeg.radCount;
				}		
				if (lSeg.type==Segment.UNKNOWN){
					occupied[ trIndx[oldTrSz-1] ] = 0;
					if (trSz-oldTrSz>0) System.arraycopy(trIndx, oldTrSz, trIndx, oldTrSz-1, trSz-oldTrSz);				
					trSz--;
					last.type = Segment.UNKNOWN;				
				}
				last.radCount = lSeg.radCount;
				for (int i = trSz-1;i>=0;--i){
					last = trArr [ trIndx[ i] ];
					if (last.type!=0 && last.type!=Segment.UNKNOWN) break;
				}
				edgeDetector.center = (last.type==0 || last.type==Segment.UNKNOWN) ? null : last.center;
				edgeDetector.turn = turn;
			}


			return;
		}
	}
	private final static int isCut(double ox,double oy,double radius,double startX,double startY,double endX,double endY,int fromSeg, int toSeg,double[] tmp,double W){
		Segment prev = null;		
		int whichEdge = 0;		
		int i = fromSeg;
		//		int sL = edgeDetector.lSize;
		double px = 0;
		double py = 0;
		//		int sR = edgeDetector.rSize;
		//		double cutX = 0;
		//		double cutY = 0;
		boolean isCut = false;		
		Segment lst = (trSz>0)? trArr[ trIndx[trSz-1]] : null;
		tmp[2] = trSz-1;
		if (lst!=null && oy>=lst.end.y){
			return -edgeDetector.turn;
		}
		//		Segment first = (trSz>0)? trArr[ trIndx[0]] : null;
		for (;i<toSeg;++i){
			Segment t = trArr[trIndx[i]];
			if (t==null || t.type==Segment.UNKNOWN) continue;						
			prev =  (i>0) ? trArr[ trIndx[i-1] ] : null;
			Segment lSeg = t.leftSeg;
			Segment rSeg = t.rightSeg;
			px = 0;
			py = 0;			
			int numInterSections = 0;
			if (prev!=null && prev.upper==null){
				Segment pLSeg = prev.leftSeg;
				Segment pRSeg = prev.rightSeg;

				double sx = pLSeg.end.x;
				double sy = pLSeg.end.y;
				double ex = lSeg.start.x;
				double ey = lSeg.start.y;
				if (isIntersect(pLSeg, lSeg, tmp)){
					sx = tmp[0];
					sy = tmp[1];
					ex = tmp[2];
					ey = tmp[3];
				}
				double ntx = ey-sy;
				double nty = ex-sx;
				double n = W/Math.sqrt(ntx*ntx+nty*nty);
				sx += ntx*n;
				sy += nty*n;
				ex += ntx*n;
				ey += nty*n;
				numInterSections = Geom.getSegArcIntersection(sx, sy, ex, ey, ox, oy, radius,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
				if (numInterSections>0){										
					whichEdge = -1;																					
				}

				sx = pRSeg.end.x;
				sy = pRSeg.end.y;
				ex = rSeg.start.x;
				ey = rSeg.start.y;
				if (isIntersect(pRSeg, rSeg, tmp)){
					sx = tmp[0];
					sy = tmp[1];
					ex = tmp[2];
					ey = tmp[3];
				}
				sx -= ntx*n;
				sy -= nty*n;
				ex -= ntx*n;
				ey -= nty*n;

				numInterSections = Geom.getSegArcIntersection(sx, sy, ex, ey, ox, oy, radius,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
				if (numInterSections>0){															
					whichEdge = 1;																					
				}
				isCut = whichEdge!=0;
				if (isCut)  break;							
			}//Finish check intersect bewtween prev and cur


			double sX = lSeg.lower==null ? lSeg.start.x : lSeg.lower.x;
			double sY = lSeg.lower==null ? lSeg.start.y : lSeg.lower.y;
			double eX = lSeg.upper==null ? lSeg.end.x : lSeg.upper.x;
			double eY = lSeg.upper==null ? lSeg.end.y : lSeg.upper.y;						

			if (sY>oy+radius && rSeg.start.y>oy+radius) break;
			double ntx = 0;
			double nty = 0;
			double n = 0;
			if (t.type==0){
				ntx = eY-sY;
				nty = eX-sX;
				n = W/Math.sqrt(ntx*ntx+nty*nty);
				sX += ntx*n;
				sY += nty*n;
				eX += ntx*n;
				eY += nty*n;				
				numInterSections = Geom.getSegArcIntersection(sX, sY, eX, eY, ox, oy, radius,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			} else if (lSeg.type!=0 && lSeg.center!=null){
				n = -W*lSeg.type;

				numInterSections = Geom.getArcArcIntersection(lSeg.center.x, lSeg.center.y, lSeg.radius+n,sX, sY, eX, eY, ox, oy, radius,startX,startY,endX,endY, tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			}

			if (numInterSections>0){
				whichEdge = -1;				
				if (numInterSections==2){
					px = tmp[0];
					py = tmp[1];
				} else if (tmp[1]<0){
					px = tmp[2];
					py = tmp[3];
				}
			}			

			sX = rSeg.lower==null ? rSeg.start.x : rSeg.lower.x;
			sY = rSeg.lower==null ? rSeg.start.y : rSeg.lower.y;
			eX = rSeg.upper==null ? rSeg.end.x : rSeg.upper.x;
			eY = rSeg.upper==null ? rSeg.end.y : rSeg.upper.y;	
			if (t.type==0){			
				sX -= ntx*n;
				sY -= nty*n;
				eX -= ntx*n;
				eY -= nty*n;
				numInterSections = Geom.getSegArcIntersection(sX, sY, eX, eY, ox, oy, radius,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;				
			} else {
				numInterSections = Geom.getArcArcIntersection(rSeg.center.x, rSeg.center.y, rSeg.radius-n,sX, sY, eX, eY, ox, oy, radius,startX,startY,endX,endY, tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			}
			if (numInterSections>0){								
				whichEdge = 1;		
				if (numInterSections==2){
					px = tmp[0];
					py = tmp[1];
				} else if (tmp[1]<0){
					px = tmp[2];
					py = tmp[3];
				}
			}

			isCut = whichEdge!=0;
			if (isCut && i==trSz-1 && whichEdge==turn){
				if (py>endY || i>0) {
					isCut = false;
					whichEdge = 0;
				}
			}
			if (isCut) break;		
			//Finish check cur

			/*if (i==trSz-1){//Final					
				if (lSeg.endIndex<sL-1){
					Vector2D hL = lSeg.points[sL-1];
					numInterSections = Geom.getLineSegCircleIntersection(lSeg.end.x, lSeg.end.y, hL.x, hL.y, ox, oy, radius,tmp);
					if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
						numInterSections = 0;
					if (numInterSections>0){					
						py = tmp[1];
						if (py>=oy){
							px = tmp[0];
							py = tmp[1];
							whichEdge = -1;
						} else py = 0; 

						double newy=0;
						if (numInterSections>2 && (newy=tmp[3])>=oy && (py==0 || newy<py) ){											
							px = tmp[2];
							py = newy;
							whichEdge = -1;												
						}					
					}
				}


				if (rSeg.endIndex<sR-1){
					Vector2D hR = rSeg.points[sR-1];
					numInterSections = Geom.getLineSegCircleIntersection(rSeg.end.x, rSeg.end.y, hR.x, hR.y, ox, oy, radius,tmp);
					if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
						numInterSections = 0;
					if (numInterSections>0){												
						if (tmp[0]>=oy && (py==0 || py>=tmp[0])){
							px = tmp[0];
							py = tmp[1];
							whichEdge = 1;
						} else py = 0; 

						double newy=0;
						if (numInterSections>2 && (newy=tmp[3])>=oy && (py==0 || newy<py) ){											
							px = tmp[2];
							py = newy;
							whichEdge = 1;												
						}					
					}
					isCut = (whichEdge!=0);
					if (isCut) break;
				}
			}	//*/	
		}
		if (isCut){
			tmp[0] = px;
			tmp[1] = py;
			tmp[2] = i;
		}
		return whichEdge;
	}
	private final static int isCutLine(double startX,double startY,double endX,double endY,int fromSeg, int toSeg,double[] tmp,double W){
		Segment prev = null;		
		int whichEdge = 0;
		if (fromSeg<0) return -1;
		int i = fromSeg;
		//		int sL = edgeDetector.lSize;
		double px = 0;
		double py = 0;
		//		int sR = edgeDetector.rSize;
		boolean isCut = false;		
		//		Segment lst = (trSz>0)? trArr[ trIndx[trSz-1]] : null;		
		for (;i<toSeg;++i){
			Segment t = trArr[trIndx[i]];
			if (t==null || t.type==Segment.UNKNOWN) continue;						
			prev =  (i>0) ? trArr[ trIndx[i-1] ] : null;
			Segment lSeg = t.leftSeg;
			Segment rSeg = t.rightSeg;
			px = 0;
			py = 0;			
			int numInterSections = 0;
			if (prev!=null && prev.upper==null){
				Segment pLSeg = prev.leftSeg;
				Segment pRSeg = prev.rightSeg;

				double sx = pLSeg.end.x;
				double sy = pLSeg.end.y;
				double ex = lSeg.start.x;
				double ey = lSeg.start.y;
				if (isIntersect(pLSeg, lSeg, tmp)){
					sx = tmp[0];
					sy = tmp[1];
					ex = tmp[2];
					ey = tmp[3];
				}
				double ntx = ey-sy;
				double nty = ex-sx;
				double n = GAP/Math.sqrt(ntx*ntx+nty*nty);
				sx += ntx*n;
				sy += nty*n;
				ex += ntx*n;
				ey += nty*n;
				numInterSections = Geom.getSegSegIntersect(sx, sy, ex, ey,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
				if (numInterSections>0){										
					whichEdge = -1;																					
				}

				sx = pRSeg.end.x;
				sy = pRSeg.end.y;
				ex = rSeg.start.x;
				ey = rSeg.start.y;
				if (isIntersect(pRSeg, rSeg, tmp)){
					sx = tmp[0];
					sy = tmp[1];
					ex = tmp[2];
					ey = tmp[3];
				}
				sx -= ntx*n;
				sy -= nty*n;
				ex -= ntx*n;
				ey -= nty*n;

				numInterSections = Geom.getSegSegIntersect(sx, sy, ex, ey,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
				if (numInterSections>0){															
					whichEdge = 1;																					
				}
				isCut = whichEdge!=0;
				if (isCut)  break;							
			}//Finish check intersect bewtween prev and cur


			double sX = lSeg.lower==null ? lSeg.start.x : lSeg.lower.x;
			double sY = lSeg.lower==null ? lSeg.start.y : lSeg.lower.y;
			double eX = lSeg.upper==null ? lSeg.end.x : lSeg.upper.x;
			double eY = lSeg.upper==null ? lSeg.end.y : lSeg.upper.y;						

			if (sY>endY && rSeg.start.y>endY) break;
			double ntx = 0;
			double nty = 0;
			double n = 0;
			if (t.type==0){
				ntx = eY-sY;
				nty = eX-sX;
				n = GAP/Math.sqrt(ntx*ntx+nty*nty);
				sX += ntx*n;
				sY += nty*n;
				eX += ntx*n;
				eY += nty*n;
				numInterSections = Geom.getSegSegIntersect(sX, sY, eX, eY, startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			} else if (lSeg.center!=null ){
				n = -W*lSeg.type;
				numInterSections = Geom.getSegArcIntersection(startX,startY,endX,endY,lSeg.center.x, lSeg.center.y, lSeg.radius+n,sX, sY, eX, eY, tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			}

			if (numInterSections>0){
				whichEdge = -1;																	
			}

			sX = rSeg.lower==null ? rSeg.start.x : rSeg.lower.x;
			sY = rSeg.lower==null ? rSeg.start.y : rSeg.lower.y;
			eX = rSeg.upper==null ? rSeg.end.x : rSeg.upper.x;
			eY = rSeg.upper==null ? rSeg.end.y : rSeg.upper.y;	
			if (t.type==0){			
				sX -= ntx*n;
				sY -= nty*n;
				eX -= ntx*n;
				eY -= nty*n;
				numInterSections = Geom.getSegSegIntersect(sX, sY, eX, eY,startX,startY,endX,endY,tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;				
			} else if (rSeg.center!=null){
				numInterSections = Geom.getSegArcIntersection( startX,startY,endX,endY,rSeg.center.x, rSeg.center.y, rSeg.radius-n,sX, sY, eX, eY, tmp);
				if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
					numInterSections = 0;
			}
			if (numInterSections>0){								
				whichEdge = 1;							
			}

			isCut = whichEdge!=0;
			if (isCut) break;		
			//Finish check cur

			/*if (i==trSz-1){//Final					
				if (lSeg.endIndex<sL-1){
					Vector2D hL = lSeg.points[sL-1];
					numInterSections = Geom.getLineSegCircleIntersection(lSeg.end.x, lSeg.end.y, hL.x, hL.y, ox, oy, radius,tmp);
					if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
						numInterSections = 0;
					if (numInterSections>0){					
						py = tmp[1];
						if (py>=oy){
							px = tmp[0];
							py = tmp[1];
							whichEdge = -1;
						} else py = 0; 

						double newy=0;
						if (numInterSections>2 && (newy=tmp[3])>=oy && (py==0 || newy<py) ){											
							px = tmp[2];
							py = newy;
							whichEdge = -1;												
						}					
					}
				}


				if (rSeg.endIndex<sR-1){
					Vector2D hR = rSeg.points[sR-1];
					numInterSections = Geom.getLineSegCircleIntersection(rSeg.end.x, rSeg.end.y, hR.x, hR.y, ox, oy, radius,tmp);
					if (tmp[1]<0 || (numInterSections>2 && tmp[3]<0))
						numInterSections = 0;
					if (numInterSections>0){												
						if (tmp[0]>=oy && (py==0 || py>=tmp[0])){
							px = tmp[0];
							py = tmp[1];
							whichEdge = 1;
						} else py = 0; 

						double newy=0;
						if (numInterSections>2 && (newy=tmp[3])>=oy && (py==0 || newy<py) ){											
							px = tmp[2];
							py = newy;
							whichEdge = 1;												
						}					
					}
					isCut = (whichEdge!=0);
					if (isCut) break;
				}
			}	//*/	
		}
		if (isCut){
			tmp[0] = px;
			tmp[1] = py;
			tmp[2] = i;
		}
		return whichEdge;
	}
	public static boolean isFirstSeg(Segment seg){
		return (seg.type==0 && Math.abs(seg.start.x-seg.end.x)<E) || (seg.type!=0 && seg.type!=Segment.UNKNOWN && seg.center!=null && seg.center.y==0);
	}
	private final static boolean isIntersect(Segment s,Segment t,double[] tmp){

		if (s.type==0){
			if (t.type==0) {
				tmp[0] = s.end.x;
				tmp[1] = s.end.y;
				tmp[2] = t.start.x;
				tmp[3] = t.start.y;
				return true;
			}
			Vector2D cntr = t.center;			
			if (cntr==null) return false;
			double dist = Geom.ptLineDistSq(s.start.x,s.start.y,s.end.x,s.end.y,cntr.x,cntr.y,tmpBuf);
			dist = Math.sqrt(dist);
			double ix = tmpBuf[0];
			double iy = tmpBuf[1];
			if (dist>t.radius || Math.abs(dist-t.radius)<=1){
				tmp[0] = s.end.x;
				tmp[1] = s.end.y;
				tmp[2] = ix;
				tmp[3] = iy;
				return true;
			}			
		} else if (t.type==0){
			if (s.type==0) {
				tmp[0] = s.end.x;
				tmp[1] = s.end.y;
				tmp[2] = t.start.x;
				tmp[3] = t.start.y;
				return true;
			}
			Vector2D cntr = s.center;			
			double dist = Geom.ptLineDistSq(t.start.x,t.start.y,t.end.x,t.end.y,cntr.x,cntr.y,tmpBuf);
			dist = Math.sqrt(dist);
			double ix = tmpBuf[0];
			double iy = tmpBuf[1];
			if (dist>s.radius || Math.abs(dist-s.radius)<=1){
				tmp[0] = ix;
				tmp[1] = iy;
				tmp[2] = t.start.x;
				tmp[3] = t.start.y;
				return true;
			}
		} 
		return false;
	}
	//	private final static int canGoDirect(Vector2D carDirection,double speedRadius,int turn,double rx,double ry,double cnx,double cny,double dSpeed,double[] tmp){
	//		return canGoDirect(carDirection, speedRadius, turn, rx, ry, cnx, cny,dSpeed, 0, trSz, tmp);
	//	}
	//	//Return center of best radius in 0,1
	// radius of cirlce in 2
	// start turning point in 3,4
	private final static boolean optimalPath(double speedRadius,Segment t,Segment lastSeg,int turn,double[] tmp){
		double bestCx = 0;
		double bestCy = 0;
		double bestRad = 0;
		double startX = 0;
		double startY = 0;
		double rx = 0;
		double ry = 0;
		int wEdge = -2;
		if (t.type==0 && lastSeg.type!=0){
			Segment seg = (turn==1) ? lastSeg.rightSeg : lastSeg.leftSeg;
			Segment tSeg = (turn==1) ? t.leftSeg : t.rightSeg;
			Vector2D cnter = lastSeg.center;
			double cnx = cnter.x;
			double cny = cnter.y;
			double ttx = 0;
			double tty = 0;
			if (lastSeg.radius<80){
				ttx = (turn==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
				tty = (turn==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;
				rx = cnx-ttx*(seg.radius+W);
				ry = cny -tty*(seg.radius+W);
				double ddy = lastSeg.end.y - cny;
				double ddx = lastSeg.end.x - cnx;
				double dd = (lastSeg.radius-tW+0.5*W)/lastSeg.radius;
				double rry = cny+ ddy*dd;
				if (ry<rry){
					ry = rry;
					rx = cnx+ddx*dd;
				}

				if (ry<seg.end.y){
					rx = seg.end.x;
					ry = seg.end.y;
					ttx = rx -cnx;
					tty = ry -cny;
					double ratio = (seg.radius+W)/seg.radius;
					rx = cnx+ttx*ratio;
					ry = cny+tty*ratio;
				}
			} else {
				rx = lastSeg.end.x;
				ry = lastSeg.end.y;				
			}			
			ttx = rx -cnx;
			tty = ry -cny;

			double ntx = -tty;
			double nty = ttx;
			double dx = tSeg.end.x-tSeg.start.x;
			double dy = tSeg.end.y-tSeg.start.y;
			double d = Math.sqrt(dx*dx+dy*dy);
			double ndx = dy*turn/d*W;
			double ndy = -dx*turn/d*W;
			Geom.getCircle3(rx, ry, rx+ntx, ry+nty, tSeg.end.x+ndx, tSeg.end.y+ndy, tSeg.start.x+ndx, tSeg.start.y+ndy, tmp);
			bestCx = tmp[0];
			bestCy = tmp[1];
			bestRad = Math.sqrt(tmp[2]);
			Geom.ptLineDistSq(tSeg.end.x+ndx, tSeg.end.y+ndy, tSeg.start.x+ndx, tSeg.start.y+ndy, bestCx, bestCy, tmp);
			startX = tmp[0];
			startY = tmp[1];	
			//			startY = ry*0.1;
			//			double dby = bestCy-startY;
			//			double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
			//			startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
		} else if (t.type!=0 && lastSeg.type==0){						
			//			Segment seg = (turn==1) ? lastSeg.leftSeg : lastSeg.rightSeg;
			Segment tSeg = (turn==1) ? t.leftSeg : t.rightSeg;			
			Vector2D cnter = tSeg.center;
			double cnx = cnter.x;
			//			double cny = cnter.y;
			rx = lastSeg.end.x;
			ry = lastSeg.end.y;

			//Want to make sure rx,ry is outside current circle
			double d = Math.sqrt(rx*rx+ry*ry);
			double dx = rx-lastSeg.start.x;
			double dy = ry - lastSeg.start.y;
			if (d<speedRadius){
				int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
				if (sz>0){
					double nrx = tmp[0];
					double nry = tmp[1];
					if (sz>2 && nry<tmp[3]){
						nrx = tmp[2];
						nry = tmp[3];
					}
					dx = nrx - rx;
					dy = nry - ry;
					d = Math.sqrt(dx*dx+dy*dy);
					double ratio = (d+1)/d;
					rx += dx*ratio;
					ry += dy*ratio;
				} 
			}

			double ntx = dy;
			double nty = dx;
			double n = turn*GAP/Math.sqrt(ntx*ntx+nty*nty);
			ntx*=n;
			nty*=n;
			//			double ndx = toMiddle+turn*(W-tW);			
			double tx = tSeg.start.x - tSeg.opp.start.x;
			double ty = tSeg.start.y - tSeg.opp.start.y;
			double sx = tSeg.start.x+ntx;
			double sy = tSeg.start.y+nty;
			if (sy<=0){
				sx = cnx-turn*(tSeg.radius-0.5*W);
				sy = 0;
				ty = 0;
				tx = 1;
			}
			Geom.getLineLineIntersection(rx, ry, rx+dx, ry+dy, sx, sy, sx-ty, sy+tx, tmp);			
			double oy = tmp[1];
			if (oy>0){												
				Geom.getCircle3(rx, ry, rx+dx, ry+dy, sx, sy, sx-ty, sy+tx, tmp);
				bestCx = tmp[0];
				bestCy = tmp[1];
				bestRad = Math.sqrt(tmp[2]);
				//				startY = ry*0.1;
				//				double dby = bestCy-startY;
				//				double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
				//				startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
				Geom.getLineLineIntersection(bestCx, bestCy, bestCx-bestRad, bestCy,sx, sy, sx-ty, sx+tx, tmp);
				startX = tmp[0];
				startY = tmp[1];				
				//			double dx = lastSeg.end.x-lastSeg.start.x;
				//			double dy = lastSeg.end.y-lastSeg.start.y;			
				//				Geom.getCircle3(startX, startY,startX ,startY+1, lastSeg.start.x, lastSeg.start.y, lastSeg.end.x, lastSeg.end.y, tmp);
				//				bestCx = tmp[0];
				//				bestCy = tmp[1];
				//				bestRad = Math.sqrt(tmp[2]);
			}
			if (oy<=0 || bestRad<=speedRadius || isCut(bestCx, bestCy, bestRad, startX, startY, rx, ry, 0, trSz, tmp,GAP)!=0){
				Segment seg = (turn==1) ? lastSeg.leftSeg : lastSeg.rightSeg;
				rx = seg.end.x;
				ry = seg.end.y;

				//Want to make sure rx,ry is outside current circle
				d = Math.sqrt(rx*rx+ry*ry);
				dx = rx-seg.start.x;
				dy = ry - seg.start.y;
				ntx = dy;
				nty = dx;
				n = turn*GAP/Math.sqrt(ntx*ntx+nty*nty);
				ntx*=n;
				nty*=n;
				rx+=ntx;
				ry+=nty;
				if (d<speedRadius){
					int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
					if (sz>0){
						double nrx = tmp[0];
						double nry = tmp[1];
						if (sz>2 && nry<tmp[3]){
							nrx = tmp[2];
							nry = tmp[3];
						}
						dx = nrx - rx;
						dy = nry - ry;
						d = Math.sqrt(dx*dx+dy*dy);
						double ratio = (d+1)/d;
						rx += dx*ratio;
						ry += dy*ratio;
					} 
				}
				Geom.getCircle3(rx, ry, rx+dx, ry+dy, sx, sy, sx-ty, sy+tx, tmp);
				bestCx = tmp[0];
				bestCy = tmp[1];
				bestRad = Math.sqrt(tmp[2]);
				Geom.getLineLineIntersection(bestCx, bestCy, bestCx-bestRad, bestCy,sx, sy, sx-ty, sx+tx, tmp);
				startX = tmp[0];
				startY = tmp[1];
				//				startY = ry*0.1;
				//				double dby = bestCy-startY;
				//				double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
				//				startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
			}
		} else {		
			double dx = 0;
			double dy = 0;
			if (lastSeg.type==t.type && t.type!=0 && lastSeg.radius<80){
				double ttx = (turn==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
				double tty = (turn==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;		
				Vector2D cnt = lastSeg.center;				
				double cnx = cnt.x;
				double cny = cnt.y;
				double wdth = (lastSeg.radius+W);
				rx =  cnx-ttx*wdth;
				ry = cny-tty*wdth;
				double ddy = lastSeg.end.y - cny;
				double ddx = lastSeg.end.x - cnx;
				double dd = (lastSeg.radius-tW+0.5*W)/lastSeg.radius;
				double rry = cny+ ddy*dd;
				if (ry<rry){
					ry = rry;
					rx = cnx+ddx*dd;
				}
				dx = ry - cny;
				dy = cnx - rx;
			} else {
				rx = lastSeg.end.x;
				ry = lastSeg.end.y;
				dx = rx-lastSeg.start.x;
				dy = ry - lastSeg.start.y;
			}

			double d = Math.sqrt(rx*rx+ry*ry);			
			if (d<speedRadius){
				int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
				if (sz>0){
					double nrx = tmp[0];
					double nry = tmp[1];
					if (sz>2 && nry<tmp[3]){
						nrx = tmp[2];
						nry = tmp[3];
					}
					dx = nrx - rx;
					dy = nry - ry;
					d = Math.sqrt(dx*dx+dy*dy);
					double ratio = (d+1)/d;
					rx += dx*ratio;
					ry += dy*ratio;
				} 
			}

			int tp = (lastSeg.type==0) ? (rx>=0) ? 1 : -1 : lastSeg.type; 
			double ndx = toMiddle+tp*(GAP-tW);	
			Geom.getLineLineIntersection(rx, ry, rx+dx, ry+dy, ndx, 0, ndx, 1, tmp);			
			double oy = tmp[1];
			if (oy>0){												
				Geom.getCircle3(rx, ry, rx+dx, ry+dy, ndx, 0, ndx, 1, tmp);			
				bestCx = tmp[0];
				bestCy = tmp[1];
				bestRad = Math.sqrt(tmp[2]);
				Geom.getLineLineIntersection(bestCx, bestCy, bestCx-bestRad, bestCy, ndx, 0, ndx, 1, tmp);
				startX = tmp[0];
				startY = tmp[1];
				//				startY = ry*0.3;
				//				double dby = bestCy-startY;
				//				double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
				//				startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
			}			
			if (oy<=0 || bestRad<=speedRadius || (wEdge = isCut(bestCx, bestCy, bestRad, startX, startY, rx, ry, 0, trSz, tmp,GAP))!=0){
				Segment seg = (turn==1) ? lastSeg.leftSeg : lastSeg.rightSeg;
				rx = seg.end.x;
				ry = seg.end.y;
				dx = rx-seg.start.x;
				dy = ry - seg.start.y;
				d = Math.sqrt(rx*rx+ry*ry);			
				if (d<speedRadius){
					int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
					if (sz>0){
						double nrx = tmp[0];
						double nry = tmp[1];
						if (sz>2 && nry<tmp[3]){
							nrx = tmp[2];
							nry = tmp[3];
						}
						dx = nrx - rx;
						dy = nry - ry;
						d = Math.sqrt(dx*dx+dy*dy);
						double ratio = (d+1)/d;
						rx += dx*ratio;
						ry += dy*ratio;
					} 
				}
				double ntx = dy;
				double nty = dx;
				double n = turn*GAP/Math.sqrt(ntx*ntx+nty*nty);
				ntx*=n;
				nty*=n;
				rx+=ntx;
				ry+=nty;
				Geom.getCircle3(rx, ry, rx+dx, ry+dy, ndx, 0, ndx, 1, tmp);			
				bestCx = tmp[0];
				bestCy = tmp[1];
				bestRad = Math.sqrt(tmp[2]);
				//				startY = ry*0.3;
				//				double dby = bestCy-startY;
				//				double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
				//				startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
				Geom.getLineLineIntersection(bestCx, bestCy, bestCx-bestRad, bestCy, ndx, 0, ndx, 1, tmp);
				startX = tmp[0];
				startY = tmp[1];
			}
		}


		if (wEdge==-2) wEdge = isCut(bestCx, bestCy, bestRad, startX, startY, rx, ry, 0, trSz, tmp,GAP);
		if (wEdge!=0){
			//			TrackSegment tt = TrackSegment.createTurnSeg(bestCx, bestCy, bestRad,startX, startY, rx, ry);
			//			trackData.add(tt);		
//			System.out.println(wEdge);
			return false;
		}
		if (startY<=0){
			double d = Math.sqrt(bestCx*bestCx+bestCy*bestCy);
			if (d>bestRad){
				Geom.ptTangentLine(0, 0, bestCx, bestCy, bestRad, tmp);
				startX = (turn==TURNRIGHT && tmp[1]>0) ? tmp[0] : tmp[2];
				startY = (turn==TURNRIGHT && tmp[1]>0) ? tmp[1] : tmp[3];
			} else if (lastSeg.type==0 || lastSeg.radius>=bestRad){
				if (lastSeg.type==0){
					startX = lastSeg.end.x;
					startY = lastSeg.end.y;
				} else {
					bestRad = lastSeg.radius;
					startX = lastSeg.end.x;
					startY = lastSeg.end.y;
				}
			}
		}
		tmp[0] = bestCx;
		tmp[1] = bestCy;
		tmp[2] = bestRad;
		tmp[3] = startX;
		tmp[4] = startY;

//		TrackSegment tt = TrackSegment.createTurnSeg(bestCx, bestCy, bestRad,startX, startY, rx, ry);
//		trackData.add(tt);		
		return true;
	}
	public static final double radiusAtSpeed(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		if (x>=240)
			return -62928534.9760697+785858.308875644*x-4413.4525439081*x2+13.2113085704168*x3-0.0205852939864750*x4+1.31893299944692e-05*x5+213081050935.215/x2;			

		return 3691.25064715183-45.2440875835028*x+0.341931648472089*x2-0.00149210322507684*x3+3.5279050744347e-06*x4-3.50744762562337e-09*x5-178615.31239954/x+4694532.43709498/x2-51414212.7178153/x3;
	}
	public final static int remove(Vector2D[] v,int sz,Vector2D p){
		int indx = binarySearchFromTo(v, p, 0, sz-1);
		if (indx<0) return sz;
		if (indx>=0 && sz-indx-1>0) {
			for (int i = indx;i<sz-1;++i){
				Vector2D vv = v[i];
				vv.copy(v[i+1]);
			}							
		}
		sz--;	
		return sz;
	}
	private static final double speedAtRadius(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;

		if (x>150)
			return -115431.216892959+834.957333227655*x-3.71025373015644*x2+0.00999768673957289*x3-1.50103486982362e-05*x4+9.64495639130066e-09*x5+9519812.76010439/x-414947104.101293/x2+6808805657.1795/x3;		
		return 5.2604417632244+2.93221790821321*x-0.0308952361016195*x2+0.00027900914172188*x3-1.30914988151668e-06*x4+2.81513214824391e-09*x5+358.981440260822/x-3125.65260706368/x2+7536.84482849135/x3;		
	}

	private static final boolean checkFast(double ox,double oy,double speedRadius,Segment lst,Vector2D p){
		Vector2D highestPoint = edgeDetector.highestPoint;
		if (highestPoint==null) return false;
		//		Vector2D currentPoint = edgeDetector.currentPointAhead;		
		//		if (edgeDetector.whichEdgeAhead==edgeDetector.whichE && !highestPoint.equals(currentPoint)){			
		//			rr = Math.sqrt(Geom.ptLineDistSq(speedV.x, speedV.y, highestPoint.x, highestPoint.y, ox, oy, tmpBuf))-GAP;										
		//			if (rr<=speedRadius)
		//				return false;			
		//		}					

		double px = p.x;
		double py =p.y;
		double nnx = highestPoint.x;
		double nny = highestPoint.y;
		if (nnx==px && nny==py){
			nnx = lst.end.x;
			nny = lst.end.y;			
		}
		if (nnx==px && nny==py){
			if (lst.type==0 || lst.center==null) return false;
			nnx = px+py - lst.center.y;
			nny = py-px + lst.center.x;					
		}
		if (nny>py)
			nny -= W;
		else py -= W;
		double cnx = nnx - px;
		double cny = nny - py;
		double c = Math.sqrt(cnx*cnx+cny*cny);
		cnx/=c;
		cny/=c;
		double cx = cny*turn*0.5*W;
		double cy = cnx*turn*0.5*W;
		nnx+=cx;
		nny+=cy;
		px+=cx;
		py+=cy;

		if (Geom.getCircle3(0,0,speedV.x,speedV.y,px,py,nnx,nny,tmpBuf)){
			double rr = Math.sqrt(tmpBuf[2])-W;
//			if (time>=BREAK_TIME){
////				TrackSegment tss = TrackSegment.createTurnSeg(tmpBuf[0], tmpBuf[1], rr, 0, 0, tmpBuf[0], tmpBuf[1]+rr);
//				trackE.add(tss);
//				draw = true;
//				display();
//			}
			if (rr>speedRadius){
				return true;
			}

		}

		return false;
	}
	
	public static double smoothSteering(){
		if (relativeAngleMovement>0.02 || lastRelativeAngleMovement>0.02 && relativeAngleMovement>0.01 
				|| relativeAngleMovement<-0.02 || lastRelativeAngleMovement<-0.02 && relativeAngleMovement<-0.01 || Math.abs(relativeAngleMovement-lastRelativeAngleMovement)>0.01)
			return stableSteer(steer);
		//Now smooth steer				
		double stdelta = steer - lastSteer;
		double maxSpeed = Math.max(200.0, 300.0 - speedX*2) * (Math.PI/180.0);
		
		if ((Math.abs(stdelta) / 0.02) > maxSpeed)
		    steer = Math.signum(stdelta) * maxSpeed * 0.02 + lastSteer;//car->_steerCmd;

//		if (Math.abs(steer-lastSteer)>0.1)
//			steer = lastSteer+Math.signum(steer-lastSteer)*0.1;
		return steer;
	}
	
	public static final boolean dangerSlip(){
		return (relativePosMovement<-0.02 || relativePosMovement<-0.01);
	}
	
	private static final boolean mustSlowDown(double first_speed,double last_speed){
		double absSpeedY = Math.abs(speedY);
		double absLastSpeedY = Math.abs(lastSpeedY);
		if (relativeAngleMovement>0.01 && relativeAngle>-0.1 && maxTurn && relativePosMovement>-0.02 && (toOutterEdge>=GAP && relativePosMovement>=-0.01 || toOutterEdge>=W)) return false;
		if (speedX<last_speed && maxTurn && (relativePosMovement>-0.01 || distToEstCircle>-tW && relativePosMovement>-0.02) && relativeAngleMovement>-0.01) return false;
		return (dangerSlip() || absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<-0.01 && relativePosMovement<-0.001) && relativeAngle<0 && (!isSafeToAccel || speedX<first_speed) || (speedX>first_speed && relativePosMovement<-0.02 && absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY);
	}
	
	private double BRAKE_HARD(double a,double b,double m,double lastSpeed,double absSpeedY,double targetSpeed){
		return relativePosMovement<-0.001 && relativeAngleMovement<-0.01 && steer*turn<0 ? brake*0.5 
					: steer*turn>=0 
						? (isSafeToAccel || absSpeedY <MODERATE_SPEEDY && distToEstCircle>0) ? 1 : brake 
						: relativePosMovement<-0.001 || distToEstCircle<0 ? relativeAngleMovement<-0.001 || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle 
				? (relativeAngleMovement>-0.001 || b<TURNANGLE && relativePosMovement>0) 
						? distToEstCircle>-W && speedX>targetSpeed+35 ? 1 : brake 
						: (steer*turn>=0 || distToEstCircle>-W && relativePosMovement>-0.001) ? brake : brake*0.5 
				: (relativeAngleMovement>0.01 && distToEstCircle>0) ? 1 : brake 
					: (absSpeedY>=MODERATE_SPEEDY && m<=20 && distToEstCircle<0 || distToEstCircle<0) ? brake : 1;

//		return relativeAngleMovement<-0.01 ? brake*0.5 : steer*turn>=0 && relativeAngleMovement>-0.001 ? 1 : relativePosMovement<-0.001 || distToEstCircle<0 
//				? relativeAngleMovement<-0.001 || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle 
//						? (m>20 && relativePosMovement>-0.001 || isSafeToAccel) && relativeAngleMovement>-0.01 && distToEstCircle>-GAP 
//								? (b<TURNANGLE*1.5 && speedX>targetSpeed+30) ? 1 : brake 
//								: lastSpeed<80 && a<TURNANGLE && distToEstCircle>-W*1.5 ? 1 : brake 
//						: (relativeAngleMovement>0.01) ? 1 : brake*0.5 
//				: (absSpeedY>=MODERATE_SPEEDY && m<=20 && distToEstCircle<0 || distToEstCircle<0 || relativeAngleMovement<-0.001) 
//					? brake*0.5 
//					: 1;
	}

	/**
	 * @param cs
	 * @param targetSpeed
	 * @param point
	 * @return
	 */
	private final double speedControl(CarState cs,double targetSpeed,Vector2D point){
		//		int gear = cs.getGear();
		//		final double Dxb  = 0.05f;
		//		final double Dxxb = 0.01f;
		if (targetSpeed==0) return 0;
		

		double mx = mustPassPoint.x;
		double my = mustPassPoint.y;
		//		double d = Math.sqrt(mx*mx+my*my);
		//		double angle = Vector2D.angle(carDirection.x, carDirection.y, mx, my);
		//		if (angle>Math.PI) angle-=Math.PI*2;
		//		if (angle<-Math.PI) angle+=Math.PI*2;
		double[] wheelSpinVel = cs.getWheelSpinVel();
		//		double tcl_slip = cs.speedX/3.6*rwd;

		//		double coeff = 1/3.6/3.6;
		double speedX = cs.speedX/3.6;
		double speed = cs.getSpeed();
		double relativeSpeedY = (turn==0 || turn==2) ? -Math.abs(speedY): turn*speedY;
		double absSpeedY = Math.abs(speedY);
		double absLastSpeedY = Math.abs(lastSpeedY);
		if (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.length()>95){
			targetSpeed = Math.max(targetSpeed,speed+10);
			brake = 0;
		}

		//All relative variables: >=0: correct direction;  <0 wrong direction		
		double relativeHeading = (turn==0 || turn==2) ? carDirection.x : carDirection.x*turn;				
		double relativeSteer = (turn==0 || turn==2) ? -steer : -steer*turn;				
		double relativeCurPos = (turn==0 || turn==2) ? curPos : curPos*turn;	
//		double angle = (mLastX==0 && mLastY==0) ? Vector2D.angle(speedV.x, speedV.y, mx, my) 
//				: Vector2D.angle(speedV.x, speedV.y, mLastX,mLastY);
		double angle = (turn==0 || turn==2) ? relativeTargetAngle : relativeTargetAngle*turn;
		double absAngle = Math.abs(angle);
//		if (angle>Math.PI) angle-=Math.PI*2;
//		if (angle<-Math.PI) angle+=Math.PI*2;
//		double relativeTargetAngle = (turn==0 || turn==2) ? angle : angle*turn;
//		angle 
		double relativeTargetAngleMovement = (turn==0 || turn==2) ? (angle-lastTargetAngle) : (angle-lastTargetAngle)*turn;


		//		if (relativeAngle>0 && relativeAngleMovement>0 && relativePosMovement>0 && relativeTargetAngle>0 && absSpeedY<HIGH_SPEEDY)// turning toward the right point
		//			targetSpeed += d;
		if (debug){
			System.out.println("xxxxxxxxxxxxxxxxx");
			System.out.println("RelativeTargetAngle : "+relativeTargetAngle);
			System.out.println("RelativePosMovement :  "+relativePosMovement);
			System.out.println("RelativeAngle : "+relativeAngle);
			System.out.println("RelativeAngleMovement :  "+relativeAngleMovement);
			System.out.println("CanGoToLastSeg:  "+canGoToLastSeg);
			System.out.println("CanGoAtCurrentSpeed:  "+canGoAtCurrentSpeed);
			System.out.println("lgap:  "+lgap);
			if (edgeDetector.highestPoint!=null) 
				System.out.println("Highest point: "+edgeDetector.highestPoint+"   length = "+edgeDetector.highestPoint.length());
			
			if (edgeDetector.currentPointAhead!=null) 
				System.out.println("Ahead point: "+edgeDetector.currentPointAhead+"   length = "+edgeDetector.currentPointAhead.length());
		}
		

		speedX = cs.speedX;
		double s = steerAtSpeed(speedX);
		//		double rwd = (wheelSpinVel[REAR_LFT]+wheelSpinVel[REAR_RGT])*wheelSpinVel[REAR_LFT]/2-speedX;
		//		double Dvv = dv - lastDv;		

		acc = 0;
		brake = 0;
		double F_LFT = wheelSpinVel[FRONT_LFT];
		double F_RGT = wheelSpinVel[FRONT_RGT];
		//		double rad = 1000;
		int lowestSegIndx = 0;
		//		boolean safe = false;
		Segment lowestSeg = null;
		//		double speedRadius = radiusAtSpeed(speedX);
		boolean tooFast = false;
		boolean possibleTooFast = false;
		double lowestSpeed = Double.MAX_VALUE;
		double highestRad = 0;
		//		Segment nextSlowSeg = null;
		Segment aheadSeg = null;
		Vector2D ahead = (edgeDetector==null) ? null : edgeDetector.currentPointAhead;
		for (int i = trSz-1;i>=0;--i){
			Segment t = trArr[ trIndx[i]];
			if (aheadSeg==null){
				aheadSeg = (t.type==TURNRIGHT) ? t.rightSeg : t.leftSeg;
				if ((ahead.y-aheadSeg.start.y)*(ahead.y-aheadSeg.end.y)>0)
					aheadSeg = null;
			}
			double segSpeed = (t.type==0) ? 1000 : speedAtRadius(t.radius);
			double startY = Math.max(t.start.y,0);
			if (!possibleTooFast){ 
				possibleTooFast = startY<40 && speedX-segSpeed-startY>FAST_MARGIN+10;
				//				if (possibleTooFast) nextSlowSeg = t;
			}
			if (lowestSpeed>segSpeed){
				//				rad = t.radius;
				lowestSeg = t;
				lowestSegIndx = i;
				lowestSpeed = segSpeed;
			}			

			if (t.type==0 && lowestSeg==null || t.radius>highestRad){
				highestRad = (t.type==0) ? 1001 : t.radius;
				//				highestSeg = t;				
			}
			//			if (t.type!=0 && t.radius>speedRadius) safe = true; 
		}
		double highestSpeed = speedAtRadius(highestRad);
		Segment last = (trSz<=0) ? null : trArr[trIndx[trSz-1]];
		Segment lastS = (last==null) ? null : (last.type==1) ? last.rightSeg : last.leftSeg;
		double m = Math.sqrt(mLastX*mLastX+mLastY*mLastY);
		double mLen = (trSz>1) ? (!inTurn && last!=null && last.type!=0 && last.type!=Segment.UNKNOWN) ? m : (canGoToLastSeg || mLastY>0) ? m : last.start.y*0.5 : (canGoToLastSeg || mLastY>0) ? m : mustPassPoint.y;
		double lastSpeed = (last==null || last.type==0) ? 1000 : speedAtRadius(last.radius);		 
		double hl = edgeDetector==null || edgeDetector.highestPoint==null ? 0 : edgeDetector.highestPoint.length();		
		double al = (ahead==null) ? 0 : ahead.length();
		double aheadSpeed = (aheadSeg==null) ? lastSpeed : speedAtRadius(aheadSeg.radius);
//		if (edgeDetector.highestPoint!=null && absSpeedY<MODERATE_SPEEDY) targetSpeed = Math.max(targetSpeed, lastSpeed+(edgeDetector.highestPoint.length()-m)*0.5);
	
		if (EdgeDetector.isNoisy){
			if (speedX>highestSpeed &&  highestPoint!=null && highestPoint.y<40 && (relativePosMovement<0.001 || relativeAngleMovement<-0.005 || edgeDetector.currentPointAhead!=null && edgeDetector.currentPointAhead.y<30)){
				targetSpeed = (highestPoint.y<35 || edgeDetector.currentPointAhead!=null && edgeDetector.currentPointAhead.y<30) ? Math.min(speedX-1, highestSpeed) : Math.min(speedX-1, targetSpeed);
				if (targetSpeed<speedX-tW && ahead!=null && ahead.y>tW*4) targetSpeed+=tW;
				if (relativePosMovement>0.001 && distToEstCircle>-GAP  && ahead!=null && ahead.y>tW*4) targetSpeed += m*2;
			}
				
			if (highestPoint!=null && highestPoint.y<30){
				double tmpSteer = gotoPoint(cs, highestPoint);
				if (tmpSteer*steer<0) steer = tmpSteer;
			}
			
			if (highestPoint!=null && highestPoint.y<75 && (speedX>235 || speedX>220 && highestPoint.y<65 || speedX>200 && highestPoint.y<60 || speedX>160 && highestPoint.y<50 || speedX>150 && highestPoint.y<40 || speedX>120 && highestPoint.y<30 || highestPoint.y<40 && ahead.y<35
					|| ahead!= null && (speedX>220 && ahead.y<60 || speedX>200 && ahead.y<55 || speedX>170 && ahead.y<45 || speedX>150 && ahead.y<35 || speedX>120 && ahead.y<25))){
				if (targetSpeed>highestSpeed)
					targetSpeed = highestSpeed;
				else targetSpeed = Math.min(speedX-1,lowestSpeed);
				if (targetSpeed<speedX-tW && ahead!=null && ahead.y>tW*4) targetSpeed+=tW;
				if (relativePosMovement>0.001 && distToEstCircle>-GAP && ahead!=null && ahead.y>tW*3) targetSpeed += m*2;
				if (targetSpeed>200) targetSpeed = 200;
				if (ahead!=null && ahead.x*highestPoint.x<0){
					if (turn*ahead.x>0){
						turn = -turn;
						relativeAngleMovement = -relativeAngleMovement;
						relativeAngle = -relativeAngle;
						relativePosMovement = -relativePosMovement;
					}
					if (targetSpeed<200) targetSpeed = Math.max(speedX-15,targetSpeed);
				}
			} else if (ahead!=null && highestPoint!=null && ahead.x*highestPoint.x<0){
				if (turn*ahead.x>0){
					turn = -turn;
					relativeAngleMovement = -relativeAngleMovement;
					relativeAngle = -relativeAngle;
					relativePosMovement = -relativePosMovement;
				}
				if (targetSpeed<200) targetSpeed = Math.max(speedX-15,targetSpeed);
			}
			
			if (targetSpeed>=speedX && targetSpeed>highestSpeed && distToEstCircle<-W || ahead!=null && (ahead.y<35 || ahead.y<40 && speedX>=150)  && relativeAngleMovement<-0.001){
				targetSpeed = Math.min(speedX-1,highestSpeed);
				if (targetSpeed<speedX-tW) targetSpeed+=tW;
			}
			
//			if (absSpeedY>MODERATE_SPEEDY && toOutterEdge<W*1.5 && targetSpeed<speedX) targetSpeed = speedX-1; 
			
			if (relativePosMovement<-0.001 && relativeAngleMovement<0.01 && (speedX>targetSpeed || relativeAngleMovement<0.001 || toOutterEdge<tW))
				steer = -turn;
			
			if (trSz>0 && lastS.end.y<m){
				targetSpeed = Math.min(targetSpeed,lastSpeed);
				if (highestPoint!=null && highestPoint.y>=30) steer = gotoPoint(cs, highestPoint);
			}
			
			if (targetSpeed<speedX && targetSpeed+m*2>speedX && highestPoint!=null && highestPoint.y>70 && ahead!=null  && ahead.y>=30)
				targetSpeed = speedX+2;
			else if (speedX<=150 && highestPoint!=null && highestPoint.y>50 && ahead!=null  && ahead.y>=30)
				targetSpeed = speedX+2;
			else if (speedX<=100 && highestPoint!=null && highestPoint.y>30 && ahead!=null  && ahead.y>=25)
				targetSpeed = speedX+2;
			else if (speedX<=90 && highestPoint!=null && highestPoint.y>30 && ahead!=null  && ahead.y>=20)
				targetSpeed = speedX+2;
		}
		
		if (debug) System.out.println("Max speed  "+targetSpeed+"   Current Speed "+speed);

//		if (relativeCurPos>1 && Math.abs(turn)<=1){			
//			targetSpeed = lowestSpeed+FAST_MARGIN;
//		}

//		if (trSz==1 && targetSpeed>lowestSpeed+FAST_MARGIN+mLen)
//			targetSpeed = lowestSpeed+FAST_MARGIN+mLen;

		double dv = (targetSpeed-speedX)/3.6;		

		//		if (absSpeedY>20) safe = false;		
		Segment first = trArr[trIndx[0]]; 

		double first_speed = (first==null || first.type==0) ? Double.MAX_VALUE : speedAtRadius(first.radius);

		int tp = first.type;
		//		if (curPos*tp<-0.80 && speedY*tp>5) {
		//			acc = 0;
		//			brake = 0;
		//			lastAcc = 0;
		//			lastTargetAngle = angle;
		//			return 0;
		//		}
		double startY = (last==null) ? 0 : (!inTurn && last.type!=0 && trSz==2 && last.lower!=null) ? Math.max(last.center.y,0) : Math.max(last.start.y,0);
				

		tooFast = trSz==1 && speedX-lastSpeed>FAST_MARGIN || startY<40 && speedX-lastSpeed-startY>FAST_MARGIN+20;
		if (tooFast){
			//			nextSlowSeg = last;
		} else if (trSz>1 && speedX-lastSpeed-startY>FAST_MARGIN)
			tooFast = possibleTooFast;	


		//		double dAngle = (lastTargetAngle-angle)*turn;

		//		double nx = turn*carDirection.y;	
		//		double ny = turn*-carDirection.x;			
		//		double ox = nx*speedRadius;
		//		double oy = ny*speedRadius;
		//		double dx = ox - mx;
		//		double dy = oy - my;
		//		double tmpRad = Math.sqrt(dx*dx+dy*dy);
		//		boolean canGoAtCurrentSpeed = (tmpRad>=speedRadius-2);
		//		if (!canGoAtCurrentSpeed) targetSpeed = speedAtRadius(tmpRad);
		//		if (targetSpeed<speed && speed<=targetSpeed+3 && slip*3.6>-0.6){
		//			lastTargetSpeed = targetSpeed;
		//			lastAcc = 0;
		//			return 0;
		//		}
		
		if (debug){
			System.out.println("First_speed :  "+first_speed);
			System.out.println("Last_speed :  "+lastSpeed);
			System.out.println("IsOffBalance : "+isOffBalance);
			System.out.println("IsSafeToAccel =  "+isSafeToAccel);
			System.out.println("xxxxxxxxxxxxxxxxx");
		}
		double meanSpd = 0.0d;		
		double abs_min = 1.0;
		if (slip>ABS_RANGE){
			for (int i = 0; i < 4; i++) {
				meanSpd = wheelSpinVel[i]*wheelRadius[i]/speedX;
				if (abs_min>meanSpd) abs_min = meanSpd;
			}
			//		meanSpd = (wheelSpinVel[REAR_LFT] + wheelSpinVel[REAR_RGT])*wheelRadius[REAR_LFT]/speedX;
			meanSpd = abs_min;			
		} else meanSpd = 1;
		if (meanSpd<0) meanSpd = 1;
		//		meanSpd /= 2.0;				
		flying = (slip>TCL_RANGE);
		double slipBF = wheelSpinVel[REAR_LFT] * wheelRadius[REAR_LFT]+ wheelSpinVel[REAR_RGT] * wheelRadius[REAR_RGT]-wheelSpinVel[FRONT_LFT] * wheelRadius[FRONT_LFT]-wheelSpinVel[FRONT_RGT] * wheelRadius[FRONT_RGT];
		slipBF*=0.5;
		//		flying = slipBF<0;

		//		if (flying) return 0;
		if (dv>=0){
			//			if (targetSpeed>lastSpeed+startY+FAST_MARGIN) targetSpeed = lastSpeed+startY+FAST_MARGIN;
			if ((toOutterEdge<=W || toOutterEdge<=W*2 && trSz==1 && relativeAngle<0 || trSz==1 && absSpeedY>=MODERATE_SPEEDY) && relativePosMovement<0 && relativeAngle<=0 && relativeAngleMovement<-0.001 && !maxTurn && !canGoToLastSeg){				
				//if (targetSpeed>first_speed) targetSpeed = first_speed;
				double tmp = Math.max(first_speed,speed);
				if (targetSpeed>tmp) targetSpeed = tmp;
				dv = (targetSpeed-speedX)/3.6;				
			}
		}
		lastDv = dv;
		if (dv>=0){						
			if (speedX<=targetSpeed-1)
				acc = 1;
			else acc = 2/(1+Math.exp(speedX - targetSpeed-1)) - 1;

			if (acc<0){
				brake = -acc;
				acc = 0;				
			}
			//			if (!flying && slip>TCL_SLIP){				
			//				acc -= Math.min(acc, (slip-TCL_SLIP)/TCL_RANGE);
			//
			//			}
		} else if (speed>targetSpeed+tW || absSpeedY>HIGH_SPEEDY){					
			//if (targetSpeed>speed) targetSpeed = speed;
			//			double s = steerAtSpeed(speed);
			//			double st = (point==null) ? cs.angle/SimpleDriver.steerLock : Math.max(-SimpleDriver.steerLock,Math.min(cs.angle-PI_2+point.angle(),SimpleDriver.steerLock));
			//			if (isTurning && speed<=targetSpeed+5 && speed>targetSpeed)
			//				return 0;
			//			double b2 = 0;									


			if (point!=null){
				//				double dist = point.length()-3;
				//				if (dist<0) return -1;
				//				double currentBrakeDist = brakeDistAtSpeed(speed)-brakeDistAtSpeed(targetSpeed);				
				//				if (dist>1.1*currentBrakeDist)
				//					return 1;				
				//				b2 = currentBrakeDist/dist;
			}

			//			slip = 1;
			//			if (speed>=targetSpeed+10) return -1; 
			brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
			//			if (speedX<ABS_MINSPEED) return -brake;
			//			slip = 0.0f;
			//			for (int i = 0; i < 4; i++) {
			//				slip += wheelSpinVel[i] * wheelRadius[i];
			//			}
			//			slip = speedX- slip/4.0f;
			//			if (slip > ABS_SLIP) {
			//				brake = brake - Math.min(brake, (slip - ABS_SLIP)/ABS_RANGE);
			//			}
			//						if (brake<b2) brake = b2;
			//			brake = Math.min(brake, 1)*meanSpd;			
			//			acc = -Math.min(brake, 1);			
		}						
		double b = 0,c=0;		
		double a = (mLastY==0) ? 0 : turn*Vector2D.angle(carDirection.x,carDirection.y,mLastX, mLastY);
		if (trSz>0 && lastS!=null && lastS.type!=0 && lastS.center.length()>lastS.radius){
			Geom.ptTangentLine(0, 0, lastS.center.x, lastS.center.y, lastS.radius, tmp);
			double dx = (lastS.type==TURNRIGHT) ? tmp[0] : tmp[2];
			double dy = (lastS.type==TURNRIGHT) ? tmp[1] : tmp[3];
			a = turn*Vector2D.angle(carDirection.x, carDirection.y, dx,dy);
			b = turn*Vector2D.angle(0, 1, dx,dy);
		} else b = (mLastY==0) ? 0 : turn*Vector2D.angle(0,1,mLastX, mLastY);
		
		c = Math.max(a,b);
		boolean possiblyGoFast = false;
		if (distToEstCircle>-tW){
			for (int i = lowestSegIndx+1;i<trSz;++i){
				Segment t = trArr[trIndx[i]];
				if (t.start.y>20 || t.end.y<20) break;				
				if (t.type==0 || speedAtRadius(t.radius)>=speedX+15){
					possiblyGoFast = (t.start.y<=10 || toOutterEdge>Math.max(tW,SAFE_EDGE_MARGIN) || toOutterEdge>=tW && relativeAngle>=-0.01 && relativeAngleMovement>0.001 && absSpeedY<HIGH_SPEEDY ) ? true : false;
					break;
				}
			}
		}
		double bal = Math.atan2(speedY,speedX);
		boolean dangerSlip = dangerSlip();
		boolean mustSlowDown = mustSlowDown(first_speed,lastSpeed);
		
		
		
		if (absSpeedY>VERY_HIGH_SPEEDY && relativePosMovement<-0.001){
			double gap = Math.max(toOutterEdge,tW)*2;
			if (speedX>targetSpeed+FAST_MARGIN){
				acc = 0;				
				steer = relativeAngleMovement>0.02 || relativeAngle>0.3 && relativeAngleMovement>0.001 ? turn 
						: (relativeAngleMovement>0.01) 
							? relativeAngle>0.3 ? turn :(relativeAngle>0 && relativePosMovement>-0.001) ? bal : 0 
							: (relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement) ? (relativeAngle>-0.05) ? bal*0.5 : steer 
					: relativeAngleMovement<0.001 
						? (speedX>120) ? -turn : steer 
						: (relativeAngle>-0.05) ? 0 : steer;
				
				brake = (relativeAngleMovement>0.01 && relativeAngleMovement<lastRelativeAngleMovement) ? 1 
						: (relativeAngleMovement>0 || steer*turn>=0) ? (speedX*speedX-targetSpeed*targetSpeed)*0.5/(speedX*speedX) 
						: 0;
			} else if (speed>targetSpeed+gap){
				acc = 0;						
				steer = relativeAngleMovement>0.02 || relativeAngle>0.3 && relativeAngleMovement>0.001 ? turn 
						: (relativeAngleMovement>0.01) 
							? relativeAngle>0.3 ? turn :(relativeAngle>0 && relativePosMovement>-0.001) ? bal : 0 
							: (relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement) ? (relativeAngle>-0.05) ? bal*0.5 : steer 
					: relativeAngleMovement<0.001 
						? (speedX>120) ? -turn : steer 
						: (relativeAngle>-0.05) ? 0 : steer;
				brake = (relativeAngleMovement>0.01 && relativeAngleMovement<lastRelativeAngleMovement) ? 1 
						: (relativeAngleMovement>0 || steer*turn>=0) ? (speedX*speedX-targetSpeed*targetSpeed)*0.5/(speedX*speedX) 
						: 0;
			}
			else {
//				acc = 0;
				acc = (speed<targetSpeed+gap || relativeAngleMovement<-0.001 || relativeAngleMovement>-0.001 && relativeAngle>0.3 || relativeAngle>0.25 && relativeAngleMovement>0.01) ? CONSTANT_SPEED_ACC*0.25 : 1;
				brake = 0;
//				if (speedX>targetSpeed) gear = 1;
				steer = relativeAngleMovement>0.02 || relativeAngle>0.3 && relativeAngleMovement>0.001 ? turn 
							: (relativeAngleMovement>0.01) 
								? relativeAngle>0.3 ? turn :(relativeAngle>0 && relativePosMovement>-0.001) ? bal : 0 
								: (relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement) ? (relativeAngle>-0.05) ? bal*0.5 : steer 
						: relativeAngleMovement<0.001 
							? (speedX>120) ? -turn : steer 
							: (relativeAngle>0) ? 0 : steer;
//				steer = turn;
			}
			
//			if ((relativeAngle>0.3 && relativeAngleMovement>0 || relativeAngle>0.35 || relativeAngle>0.25 && relativeAngleMovement>0.01) && relativePosMovement<-0.001)
//				steer = turn;
//			else
				if (relativePosMovement<-0.001){
					steer = (relativeAngleMovement>0.02  || relativeAngle>0.3 && relativeAngleMovement>0.001 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement) 
							? turn
							: relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement 
								? relativeAngle>0.3 ? turn  : (relativeAngle>0) ? bal : 0 
								: relativePosMovement<-0.01 && relativeAngleMovement>lastRelativeAngleMovement && relativeAngleMovement>0.001 || relativeAngleMovement>0.01 
									? (relativeAngle>0) ? bal : (relativeAngle>0) ? bal*0.5 : steer 
									: (relativeAngleMovement<0.001) ? (speedX>120) ? -turn : steer  : steer;
				}
			
			if (relativePosMovement<-0.02 && relativeAngleMovement<-0.001 && a>0 && (relativePosMovement<-0.025 || relativePosMovement<lastRelativePosMovement) ) acc = 0;
			
			if (relativeAngleMovement<0 && brake==0 && speedX>targetSpeed || steer*turn>0 && speed>targetSpeed+10){
				acc = 0;
				brake = (speed>targetSpeed+10) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : 0;
				
			}
			
//			if (Math.abs(a)>0.5 && relativeAngle<0 && relativePosMovement<-0.01 && brake==0 && speedX>highestSpeed){
//				brake = 0.1;
//				acc = 0;
//			}
			
			if (relativeAngleMovement>0 && (relativeAngle>0 || relativeAngleMovement>0.01 && steer*turn<=0) && speed<aheadSpeed+Math.min(gap,10)){
				acc = CONSTANT_SPEED_ACC;
				brake = 0;
			}
			if (relativeAngle>0.3 && relativeAngleMovement<0.001 && speed<aheadSpeed+Math.min(gap,10)){
				acc = INCREASE_ONE;
				brake =0;
			}
			if (canGoVeryFast) {
//				brake = (a<0 || b<0) ? brake : 0;
				brake = 0;
//				acc = CONSTANT_SPEED_ACC;
			}//*/
			
			if (brake==0 && acc==0) acc = CONSTANT_SPEED_ACC*0.25;
//			steer =0;
			return steer;
		}
				
				

		if (!inTurn && turn==0 && absSpeedY<MODERATE_SPEEDY && Math.abs(curAngle)<0.1) return acc;

		if (absSpeedY>=50){		
			double gap = Math.max(toOutterEdge,tW)*2;
			if (relativeAngle>0 && speedX<targetSpeed+20 && relativePosMovement<-0.001 && toOutterEdge<tW) 
				acc = 1;
			else if (absSpeedY>absLastSpeedY && relativePosMovement<-0.001) 
				acc = CONSTANT_SPEED_ACC*0.25;
			else acc = (speedX<targetSpeed+gap) ? 1 : 0;
			if (acc>0) 
				brake = 0;
			else if (speedX>targetSpeed+20) brake = 1;
			if (a>0){
				steer = relativeAngle>0.3 && relativeAngleMovement>-0.001 || relativeAngle>0.25 && relativeAngleMovement>0.01 || relativeAngleMovement>0.02 ? turn : (relativeAngleMovement>0.01) ? bal : (relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement) ? bal*0.5 
						: relativeAngle<0.3 && (relativeAngle<0.001 || relativeAngleMovement<-0.001)							
							? -turn : 0;
			} else {
				steer = (relativeAngleMovement>-0.01) ? turn : 0;
				if (relativeAngleMovement>0.001) acc = CONSTANT_SPEED_ACC*0.25;
			}
			
			if ((relativeAngle>0.3 && relativeAngleMovement>0 || relativeAngle>0.35 || relativeAngle>0.25 && relativeAngleMovement>0.01) && relativePosMovement<-0.001)
				steer = turn;
			else
			if (relativePosMovement<-0.001){
				steer = (relativeAngle>0.3 && relativeAngleMovement>-0.001 || relativeAngle>0.25 && relativeAngleMovement>0.01 || relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement) 
						? turn
						: relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement 
							? turn 
							: relativeAngleMovement>lastRelativeAngleMovement && relativeAngleMovement>0.001 || relativeAngleMovement>0.001  
								? bal 
								: (relativeAngle<0.3 && relativeAngleMovement<0.001) ? (speedX>120) ? -turn : steer  : steer;
			}
			
			if (relativePosMovement<-0.02 && relativeAngleMovement<-0.001 && a>0 && (relativePosMovement<-0.025 || relativePosMovement<lastRelativePosMovement) ) acc = 0;
			
			
			if (relativeAngleMovement<0 && brake==0 && speedX>targetSpeed+gap){
				acc = 0;
				brake = (speedX*speedX-targetSpeed*targetSpeed)/(speedX*speedX);
			}
			
			if (relativeAngleMovement>0 && a>0 && (relativeAngle>0 || relativeAngleMovement>0.01) && speedX<targetSpeed+gap)
				acc = 1;
			return acc;
		}

		
		
		if ((startFlying>0 || flyingHazard || slip>20 ) && speedX<lowestSpeed && (relativeAngle>0 || maxTurn && distToEstCircle>0) && relativeTargetAngle>0) 
			steer = (relativeAngle>0) 
				? (relativeAngle>0.1 && absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement>0.001) 
						? bal*0.5 
						: (relativePosMovement<-0.001 || relativeAngleMovement<-0.001) ? stableSteer(steer) : stableSteer(0) 
				: steer*0.5;
		
		if (flyingHazard || speedX>50 && speedX<lastSpeedX && slip>10 && slip>0 && lastSlip>0 && balance>0){
			int signPos = (curPos<0) ? -1 :1;
			int tpe = (turn==0 || turn==2) ? -signPos : turn;
			
			if ((flyingHazard && !landed || startFlying>0 && relativeAngle>0 && relativeAngleMovement>0.001 && steer*tpe<0 && speedX<lowestSpeed) && absSpeedY<MODERATE_SPEEDY && relativeTargetAngle>0)
				steer = (relativePosMovement<-0.001 && relativeAngleMovement<-0.001 && relativeAngle>0) ? steer*0.5 : 0;
			else if (speedX>lowestSpeed && toOutterEdge>SAFE_EDGE_MARGIN && absSpeedY>10 && relativePosMovement<-0.001 && !flyingHazard)
				steer = (relativeAngle>0.2 && relativeAngleMovement>0) ? tpe 
						: relativeAngle>0.15 && relativeAngleMovement>0 ? bal : (relativeAngleMovement>0.01 || relativeAngle>0.15) ? 0 : -tpe;
			else if (dangerSlip && !flyingHazard)
				steer = (relativeAngle<0) 
							? relativeAngleMovement<-0.01 ? -tpe : relativeAngleMovement>0.01 ? 0 : steer
							: relativeAngleMovement>0.01 && steer*tpe<0 ? stableSteer(0) : steer;
			else if (a>0){
				steer = (relativeAngle>0.2 && relativeAngleMovement>0.01 && absSpeedY>HIGH_SPEEDY) ? stableSteer(bal*0.5) : (relativeAngleMovement>0.01 && relativeAngle<0 && steer*tpe<0|| relativeAngleMovement<-0.01 && relativeAngle>0 && steer*tpe>0) ? 0 : 
					(flyingHazard) 
						? (landed && steer*tpe<0) 
								? relativeAngleMovement<-0.01 ? (relativeAngle>0.1) ? 0 : -tpe
								: (slip>10 || relativeSpeedY<LOW_SPEEDY) ? minAbs(steer*0.5,-tpe*0.3) 
									: (relativePosMovement<0.01 && relativeAngleMovement<-0.001 && relativeAngle>0.1) 
										? (absSpeedY>HIGH_SPEEDY) ? bal : 0 
										: steer*0.75
								: (relativeAngle<-0.3 && relativeAngleMovement<0.01 || relativeAngle<-0.25 && relativeAngleMovement<-0.001 
										|| relativeAngleMovement<-0.01 && relativeAngleMovement<lastRelativeAngleMovement || b<0 && relativeAngleMovement<-0.01 && relativeAngle<-0.05) ? -tpe 
										: (relativeAngleMovement<-0.01) ? 0 : steer 
						: (relativeAngle<-0.01 || relativeTargetAngle<0 || relativePosMovement<-0.001 && relativeAngleMovement<-0.01) 
							? (relativeAngleMovement<-0.015 || relativeAngleMovement<-0.01 && relativeAngleMovement<lastRelativeAngleMovement) 
									? -tpe 
									: (relativeAngleMovement>-0.001) 
										? steer*tpe>0 && (relativeAngle<-0.25 || b<0 && relativeAngle<-0.15) 
												? (relativeAngle<-0.3 && relativeAngleMovement<0.01) 
													? bal 
													: relativeAngle<-0.25 && relativeAngleMovement<0.01 ? bal*0.5 : 0 
												: steer 
										: (relativeAngleMovement<-0.01) 
											? (steer*tpe<0) ? steer : 0 
											: steer*tpe>0 && (relativeAngle<-0.25 || b<0 && relativeAngle<-0.15) ? bal : steer*0.5 
							: (absSpeedY>absLastSpeedY+4 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY+2) ? curAngle/steerLock 			
					: (absSpeedY>absLastSpeedY) ? steer : 0;
			
				if (absSpeedY>=MODERATE_SPEEDY && relativeAngle>0 && relativeAngleMovement>0.001 && relativeTargetAngle>0 && (flyingHazard || speedX<lowestSpeed)){
					steer = (steer*bal<=0) ?  0 : maxAbs(bal, steer);
				} else if (absSpeedY>=MODERATE_SPEEDY && relativeAngle<0 && relativeAngleMovement<-0.001 && (flyingHazard || speedX<lowestSpeed))
					steer = !landed && (absSpeedY>=HIGH_SPEEDY || dangerSlip) ? -tpe : steer;
				else if ((dangerSlip || relativePosMovement<0 && relativeAngleMovement<-0.01) && !landed) 
					steer = -tpe;
//				else if (relativePosMovement>0.001 && relativeAngle>0.01 && relativeAngleMovement<-0.001 && steer*turn>=0)
//					steer = relativeAngleMovement<-0.01 && a>-0.05 && speedX>lowestSpeed? -turn : (a<0) ? steer : 0;
			} else if (a<0){//if (a<0)
				if (relativeAngle>0.3 || relativeAngle>0.25 && relativeAngleMovement>0.01)
					steer = (relativeAngleMovement>-0.01) 
							? (turn==0 || turn==2) 
								? Math.signum(bal) 
								: (relativeAngleMovement>lastRelativeAngleMovement || relativeAngleMovement>0.01) ? turn : steer*0.5 
							: bal;
				else
				steer = (relativeAngleMovement>0.01) ? (turn==0 || turn==2) ? Math.signum(bal) : turn : (relativeAngleMovement<-0.01) ? 0 : -tpe*relativeTargetAngle*0.5/steerLock;			
			}
			
//			if ((startFlying>0 || flying+Hazard || slip>20 ) && speedX<lowestSpeed && (relativeAngle>0 || maxTurn && distToEstCircle>0) && relativeTargetAngle>0) steer = 0;
					
//			steer = 0;
//			if (relativePosMovement<0 || absSpeedY>MODERATE_SPEEDY) smoothSteering();
			acc = (!mustSlowDown && slip>15 && Math.abs(a)<0.5 && absSpeedY<HIGH_SPEEDY) ? 1 : (!maxTurn && relativeAngle<0 || Math.abs(a)>0.5) ? CONSTANT_SPEED_ACC*0.25 : !flyingHazard && startFlying==0 && mustSlowDown || speedX>targetSpeed+10 || relativePosMovement<-0.02 && absSpeedY>MODERATE_SPEEDY || speed>targetSpeed-tW && absSpeedY>45 ? 0.25*CONSTANT_SPEED_ACC: 1;
//			if ((absSpeedY<absLastSpeedY || absSpeedY<HIGH_SPEEDY) && speedX>Math.max(targetSpeed,lastSpeed))
//				acc = 0;
			if (acc>CONSTANT_SPEED_ACC && speedX<targetSpeed+10 && relativeAngleMovement<-0.01 && relativePosMovement<0.001) 
				acc = CONSTANT_SPEED_ACC*0.25;
			else if (acc>CONSTANT_SPEED_ACC && speedX<targetSpeed+10 && relativeAngleMovement>0 && steer*turn>0)
				acc = CONSTANT_SPEED_ACC*0.25;
			
			if (relativeAngleMovement<-0.01 && relativeAngleMovement<lastRelativeAngleMovement && lastSteer==-turn && steer==lastSteer || relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement && lastSteer==turn && steer==lastSteer
					|| relativeAngle<-0.25 && relativeAngleMovement<-0.005 || relativeAngle>0.25 && relativeAngleMovement>0.005 || relativeAngle<-0.3 && relativeAngleMovement<0.001 || relativeAngle>0.3 && relativeAngleMovement>-0.001)
				acc = CONSTANT_SPEED_ACC*0.25;
			brake = 0;
			return acc;
		}

		//		if (speed<5){
		//			acc = 1;
		//			steer = 0;
		//			return acc;
		//		}

		//		if (tooFast && lowestSegIndx>0){
		//			mx+=turn*W;
		//		}

		//		if (time>=BREAK_TIME){
		//			TrackSegment ts = TrackSegment.createTurnSeg(ox, oy, speedRadius, 0, 0, mx, my);
		//			trackData.add(ts);
		//			System.out.println(mx+"  fasfa  "+my);
		//		}
		

		if (hazard>0) hazard--;				
		
		if (relativeAngleMovement>0.03 && relativeAngle>0 && absSpeedY>MODERATE_SPEEDY && relativeTargetAngle<0.5*TURNANGLE|| (relativeSpeedY>=0 && absSpeedY>absLastSpeedY+2 && (speed<targetSpeed && lastAcc>0 && lastSteer*steer>=0) && relativeAngleMovement>0 && absSpeedY>=MODERATE_SPEEDY) && (lastSteer*turn<0 && Math.abs(lastSteer)<0.5 || lastSteer*turn>0 && lastSteer==Math.signum(speedY) && relativePosMovement>0 && speed<targetSpeed) || !isSafeToAccel && absSpeedY>=HIGH_SPEEDY && absSpeedY>absLastSpeedY+1.5 && (!inTurn || lastSteer*turn>0) && relativeAngleMovement>0){
			if (speed<targetSpeed){
//				acc = 1;
//				brake = 0;
//				steer = relativeSpeedY>=0 && absSpeedY>absLastSpeedY+2 && Math.abs(absSpeedY-absLastSpeedY)<4 && relativeTargetAngle>0 && relativeAngleMovement>0 && absSpeedY<=VERY_HIGH_SPEEDY ? (relativePosMovement<-0.001) ? steer: 0 :((relativePosMovement<=0 && absSpeedY>=MODERATE_SPEEDY  && relativeAngleMovement>0 ||  Math.abs(absSpeedY-absLastSpeedY)>4 && relativeAngle>0)) ? Math.signum(speedY) : 0;
				steer = relativeAngleMovement>0.01 
						? relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement 
								? turn
								: relativeAngleMovement>lastRelativeAngleMovement ? bal : 0
						: relativeAngleMovement>0.001 ?
								relativeAngle>0.3 
									? (steer*turn>0) ? steer : turn 
									: relativeAngle>0.25 
										? (steer*turn>0) ? minAbs(steer,bal) : bal 
										: relativeAngle<0 && a>0 ? -turn : steer
						: -turn;
			} else {
//				acc = (relativeAngle>0) ? CONSTANT_SPEED_ACC : 0;
				//				if (absSpeedY>=MODERATE_SPEEDY) acc = 1;
//				brake = 0;
//				steer = relativeSpeedY>=0 && absSpeedY>absLastSpeedY+2 && Math.abs(absSpeedY-absLastSpeedY)<4 && relativeTargetAngle>0 && relativeAngleMovement>0 && absSpeedY<=VERY_HIGH_SPEEDY ? (relativePosMovement<-0.001) ? steer: 0 : (relativePosMovement<=0 && (absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement>0 || Math.abs(absSpeedY-absLastSpeedY)>4 && relativeAngle>0)) ? Math.signum(speedY) : 0;
				steer = relativeAngleMovement>0.01 
							? relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement 
									? turn
									: relativeAngleMovement>lastRelativeAngleMovement ? bal : 0
							: relativeAngleMovement>0.001 ?
									relativeAngle>0.3 
										? (steer*turn>0) ? steer : turn 
										: relativeAngle>0.25 
											? (steer*turn>0) ? minAbs(steer,bal) : bal 
											: relativeAngle<0 && a>0 ? -turn : steer
							: -turn;
									
			}
			hazard = 2;
			return acc;
		}
		


		
		if (hl>EdgeDetector.MAX_DISTANCE && absSpeedY<MODERATE_SPEEDY && relativePosMovement>-0.01){
			acc = (relativePosMovement<-0.02 && (!isSafeToAccel || lastSteer*turn<0)) ? CONSTANT_SPEED_ACC*0.25 : 1;
			double steering = gotoPoint(cs, edgeDetector.highestPoint)*0.5;
			double deflect = curPos*0.2;
//			double signPos = (curPos<0) ? -1 :1;
//			deflect*=signPos;
//			double steering = curAngle/steerLock;
			brake = 0;
			double lsteer = (!inTurn) ? curAngle/steerLock + deflect*0.5 : steering+deflect*0.5;
			if (relativeAngle>0.3 || relativeAngle>0.25 && relativeAngleMovement>0.01){
				steer = (relativeAngleMovement>0.01) ? (turn==0 || turn==2) ? Math.signum(bal) : turn : (steering*bal>0) ? maxAbs(steering, bal) : bal;				
			} else if (lsteer*steer>0){ 
				steer =  minAbs(steer,lsteer);
				if (distToEstCircle<-GAP && speedX>lastSpeed-15 && al<80) 
					steer = maxAbs(-turn*relativeTargetAngle*0.5/steerLock,steer);
//				steer = (speedX<lowestSpeed && distToEstCircle>-W) ? minAbs(steer,lsteer) :  minAbs(steer,lsteer);
//				steer = (speedX<lastSpeed || distToEstCircle<0) ?  minAbs(steer,lsteer) : (steering+lastSteer)*0.5;
			} else steer = (steering+lastSteer)*0.5;
			smoothSteering();
//			else steer = steer*0.5;
			
//			steer = steering - toMiddle*0.001;
			return steer;
		} else if (maxTurn && hl>EdgeDetector.MAX_DISTANCE && speed>lowestSpeed && absSpeedY<MODERATE_SPEEDY && turn !=0 && turn!=2 && turn!=-2 && (last==null || last.type!=0)){
			acc = (relativePosMovement<-0.02 && (!isSafeToAccel || lastSteer*turn<0)) ? CONSTANT_SPEED_ACC*0.25 : 1;
			double steering = gotoPoint(cs, edgeDetector.highestPoint);
			if (relativeAngle>0.3 || relativeAngle>0.25 && relativeAngleMovement>0.01)
				steer = (relativeAngleMovement>-0.01) ? (turn==0 || turn==2) ? Math.signum(bal) : turn : (steering*bal>0) ? maxAbs(steering, bal) : bal;
			else
			if (absSpeedY<10 && maxTurn && relativePosMovement>-0.01)
				steer = (steering+lastSteer)*0.5;
			else if (steer*steering<=0 || relativePosMovement>0 && relativeAngle>-0.01) 
				steer = (steering+lastSteer)*0.5;
			else if (Math.abs(steer)>Math.abs(steering)){
				if (relativeAngle>0 || relativeAngleMovement<-0.001 && relativeTargetAngle>=0)
					steer = (relativeTargetAngle>0.3 || relativeAngleMovement<-0.01) ? (steer*lastSteer>0) ? maxAbs(steer, lastSteer) : steer :  (steering+lastSteer)*0.5;					
			} else steer = (steering+lastSteer)*0.5;
			
			acc = (a<0 || speedX>lastSpeed+al) ? acc : 1;
			smoothSteering();
			brake = 0;
			return steer;
		} else if (maxTurn && inTurn && hl>80 && speed>lastSpeed && absSpeedY<MODERATE_SPEEDY && (last==null || last.type!=0) &&edgeDetector.highestPoint.y-Math.max(last.leftSeg.end.y,last.rightSeg.end.y)>10){
//			steer = gotoPoint(cs, edgeDetector.highestPoint)*0.5;
			double steering = gotoPoint(cs, edgeDetector.highestPoint);
			if (relativeAngle>0.3 || relativeAngle>0.25 && relativeAngleMovement>0.01)
				steer = (relativeAngleMovement>0.01) ? (turn==0 || turn==2) ? Math.signum(bal) : turn : (steering*bal>0) ? maxAbs(steering, bal) : bal;
			else
			if (absSpeedY<10 && maxTurn && relativePosMovement>-0.01)
				steer = (steering+lastSteer)*0.5;
			else if (steer*steering<0 && turn*steering>0 || relativePosMovement>0 && relativeAngle>-0.01) 
				steer = (steering+lastSteer)*0.5;
			else  if (Math.abs(steer)>Math.abs(steering)){
				if (relativeAngle>0 || relativeAngleMovement<-0.001 && relativeTargetAngle>=0)
					steer = (relativeTargetAngle>0.3 || relativeAngleMovement<-0.01) ? (steer*lastSteer>0) ? maxAbs(steer, lastSteer) : steer :  (steering+lastSteer)*0.5;					
			} else steer = (steering+lastSteer)*0.5;
			acc = (relativePosMovement<-0.02 && (!isSafeToAccel || lastSteer*turn<0)) ? CONSTANT_SPEED_ACC*0.25 : 1;
			smoothSteering();
			brake = 0;
			return steer;
		} 
		else if (maxTurn && inTurn && (last==null || last.type!=0) && hl>=80 && (canGoVeryFast || hl>al && (edgeDetector.highestPoint.x*ahead.x<0 || steer*(edgeDetector.highestPoint.x-ahead.x)>0)  || absSpeedY<MODERATE_SPEEDY && ahead.y==edgeDetector.highestPoint.y && EdgeDetector.isNoisy && ahead.length()>=85)){
			double steering = gotoPoint(cs, edgeDetector.highestPoint)*0.5;
			if (steer*steering<0 || canGoVeryFast){
				if (relativeAngle>0.3 || relativeAngle>0.15 && relativeAngleMovement>0.01)
					steer = (relativeAngleMovement>0.01) ? (turn==0 || turn==2) ? Math.signum(bal) : turn : (steering*bal>0) ? maxAbs(steering, bal) : bal;
				else {
					steer = (speedX<lowestSpeed || distToEstCircle>GAP) ? (steer+lastSteer)*0.5 
							: (distToEstCircle<-GAP)
								? (speedX<lastSpeed-15) ? steering 
										: (turn*steering>0) ? steering : maxAbs(-turn*relativeTargetAngle*0.5/steerLock,steering)
//								? (a>0 && steer*turn<0 && (relativePosMovement<-0.001 || distToEstCircle<-W) && relativeAngleMovement<-0.001 && speedX>lowestSpeed) ? (steer*0.5+steering)*0.5 : steering 
								: (steering+lastSteer)*0.5;
//					smoothSteering();
				}
				smoothSteering();
				if (canGoVeryFast || al>30) {
					acc = (a<0 || speedX>lastSpeed+al) ? acc : 1;
					brake = 0;
				}//*/
				return steer;
			}
		}
		//		double relativeAngleMovementToCurrentSeg = (lastAngle-curAngle)*tp;
		double speedRadius = radiusAtSpeed(speed);
		
		if (acc>0 && absSpeedY>MODERATE_SPEEDY && relativePosMovement<0) {
//			if (relativeAngleMovement<0 || absSpeedY>absLastSpeedY) acc *= CONSTANT_SPEED_ACC;
//			acc *= INCREASE_ONE;
		}
		
		
		//		if (trSz==1 && first.end.y<15 || last!=null && last.end.y<15){
		//			steer = gotoPoint(cs, edgeDetector.highestPoint);
		//			return acc;
		//		}

		//		if (tp==turn && speed<lowestSpeed+FAST_MARGIN+mLen && relativeAngle>0.1 && relativeTargetAngle>0 && (relativePosMovement>0 || relativeAngleMovement>0)){
		//			acc = 1;
		//			steer = -turn*relativeTargetAngle/steerLock;
		//			brake = 0;
		//			return acc;
		//		}
		
	/*	if (relativeSpeedY<0 && absSpeedY<absLastSpeedY) {
			if (relativeAngleMovement<0 && relativeTargetAngle>=0) 
				steer = (steer*turn<0) ? steer * 2 : steer*0.5;
			else if (relativeTargetAngle>=0) 
				steer *= 0.5;
//			else if (relativeTargetAngle<0 && relativeAngleMovement<0)
//				steer = Math.signum(speedY);
		}//*/
		
		if (!max) max = canGoAtCurrentSpeed;

//		if (inTurn && relativePosMovement>=0 && first_speed>=highestSpeed && speed>highestSpeed && speed<lastSpeed+FAST_MARGIN+mLen && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.y<=Math.max(last.leftSeg.end.y,last.rightSeg.end.y)){
//			brake = 0;
//			acc = 0;
//			if (relativeTargetAngle>=0) steer = -turn;
//			return steer;
//		}

		if (last!=null && last.type==turn &&  last.type!=0 && (brake>0 || acc>=0 && !canGoToLastSeg) || acc>0 && inTurn && trSz==1 && last!=null && last.type==0){
			if (trSz==1) last = first;
			Segment lst = (turn==TURNRIGHT) ? last.leftSeg : last.rightSeg;
			if (lst.num>0 || edgeDetector.whichE*turn<0){
				Vector2D p = (edgeDetector.whichE*turn<0) ? edgeDetector.highestPoint : lst.points[lst.endIndex];				
				double nx = turn*speedV.y;	
				double ny = turn*-speedV.x;			
				double ox = nx*speedRadius;
				double oy = ny*speedRadius;
				double mmx = p.x;
				double mmy = p.y;
				double dx = ox - mmx;
				double dy = oy - mmy;
				double rad = speedRadius;				
				double dist = Math.sqrt(dx*dx+dy*dy);
//				if (inTurn && (brake>0 || acc<1) && max && dist>rad && speed<targetSpeed+Math.max(mLen,FAST_MARGIN)){
//					Vector2D p2 = null;
//					if (turn==TURNRIGHT && lst.endIndex+1<=edgeDetector.lSize){
//						p2 = (lst.endIndex<edgeDetector.lSize-2) ? lst.points[lst.endIndex+1] : lst.points[lst.endIndex];
//					} else if (turn==TURNLEFT && lst.endIndex+1<=edgeDetector.rSize){
//						p2 = (lst.endIndex<edgeDetector.rSize-2) ? lst.points[lst.endIndex+1] : lst.points[lst.endIndex];
//					}
//					if (p2==p & edgeDetector.highestPoint!=null && p2!=edgeDetector.highestPoint) p2 = edgeDetector.highestPoint;
//					if (p2!=null && p2!=p && p2.y!=p.y){
//						if (Geom.getCircle3(0,0,speedV.x,speedV.y,p.x,p.y,p2.x,p2.y,tmpBuf)){
//							double rr = Math.sqrt(tmpBuf[2])-W;
//							double cx = tmpBuf[0];
//							double cy = tmpBuf[1];
//							if (time>=BREAK_TIME){
//								TrackSegment tss = TrackSegment.createTurnSeg(tmpBuf[0], tmpBuf[1], rr, 0, 0, tmpBuf[0], tmpBuf[1]+rr);
//								trackE.add(tss);
//								draw = true;
//								display();
//							}			
//							if (rr>speedRadius-GAP){
//								Geom.ptLineDistSq(p.x, p.y, p2.x, p2.y, cx, cy, tmpBuf);
//								cx = tmpBuf[0];
//								cy = tmpBuf[1];
//								if ( (cy>Math.min(p.y,p2.y) || cy>lst.end.y)){
//									if (rr>speedRadius){								
//										if (absSpeedY<MODERATE_SPEEDY && relativePosMovement>0 && relativeTargetAngle>0) acc = 1;
//										//										acc = 1;
//										brake = 0;
//										//										if (!canGoAtCurrentSpeed && !canGoToLastSeg && !isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
//										//										return acc;
//									} else {
//										brake = 0;
//										//										if (!canGoAtCurrentSpeed && !canGoToLastSeg && !isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
//										//										return acc;
//									}
//								}
//							}
//						}
//					}
//
//					if (lowestSpeed+p.length()-1.5*W>speed && absSpeedY<MODERATE_SPEEDY && checkFast(ox, oy, speedRadius, lst, p)){
//						acc *= INCREASE_ONE;
//						brake = 0;
//					}
//				}
				if (brake>0 && (dist>rad || inTurn && max && trSz==1)){
					if (lastSpeed+p.length()*1.5-1.5*W>speed && absSpeedY<MODERATE_SPEEDY || inTurn && max && trSz==1){
						//						if (p!=edgeDetector.highestPoint || inTurn && max && trSz==1) {						
						brake *= 0.5;
						//							if (!canGoAtCurrentSpeed && !canGoToLastSeg && !isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
						//						}

						if (checkFast(ox, oy, speedRadius, lst, p)){
							acc *= INCREASE_ONE;
							brake = 0;
						}


						//						return acc;
					} 
//					else if (lastSpeed+(p.length()-W)*2<speed && p.length()<35){	
//						acc = 0;
//						if (steer<HIGH_SPEEDY) {
//							if (!canGoToLastSeg && !canGoAtCurrentSpeed) {
//								//								if (!isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
//								brake = (steer*turn<0) ? Math.min(brake,0.1) : 1;
//							} else {
////								steer = 0;
//								brake = (steer*turn<0) ? brake*1.5 : 1;
//								steer = 0;
//								//								brake =1;
//							}													
//						} else {
//							if (canGoToLastSeg || canGoAtCurrentSpeed){
////								steer = 0;
//								brake = (steer*turn<0) ? brake*1.5 : 1;
//								steer = 0;
//								//								brake = 1;
//							} else {
//								acc = CONSTANT_SPEED_ACC;
//								//								steer = turn;
//							}
//						}
//						return acc;
//					}
				} else if (acc>0 && (dist<=rad || lastSpeed+p.length()*2-1.5*W<speed )){

					if (lastSpeed+p.length()*2-1.5*W<speed){
						targetSpeed = lastSpeed+p.length()-1.5*W;
						acc = 0;
						if (targetSpeed<speed){							
							brake = Math.min((speed*speed-targetSpeed*targetSpeed)/(speed*speed),0.1);
							//							if (!canGoAtCurrentSpeed && !canGoToLastSeg && !isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
						}
					} else if (!max){
						targetSpeed = speedAtRadius(Math.sqrt(dx*dx+dy*dy)-W);
						if (targetSpeed<speed){
							acc = 0;
							brake = Math.min((speed*speed-targetSpeed*targetSpeed)/(speed*speed),0.1);
							//							if (!canGoAtCurrentSpeed && !canGoToLastSeg && !isSafeToAccel && (relativeTargetAngle>0 && relativeAngle>=0)) steer = -turn;
						}
					}				
					//					return acc;
				} 
				//				else if (acc>0 && edgeDetector.whichEdgeAhead==-turn && (edgeDetector.pointAheadIndx<edgeDetector.lSize-1 && turn==TURNRIGHT || turn==TURNLEFT && edgeDetector.pointAheadIndx<edgeDetector.rSize-1)){
				//					Vector2D p1 = edgeDetector.currentPointAhead;
				//					Vector2D p2 = (turn==TURNRIGHT) ? edgeDetector.left[edgeDetector.lSize-1] : edgeDetector.right[edgeDetector.rSize-1];
				//					if (Geom.getCircle3(0,0,speedV.x,speedV.y,p1.x,p1.y,p2.x,p2.y,tmpBuf)){
				//						double rr = Math.sqrt(tmpBuf[2])-GAP;
				//						targetSpeed = speedAtRadius(rr);
				//						if (speed>=targetSpeed){							 
				//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
				////							if (!canGoAtCurrentSpeed && !canGoToLastSeg && (relativeTargetAngle>0 || relativeAngle<0)) steer = -turn;
				//							acc = 0;
				////							return acc;
				//						}
				//					}
				//				}
			}
		}
		
		if (inTurn && trSz==1 && last.type==0 || (inTurn || lowestSeg!=null && lowestSeg.type!=Segment.UNKNOWN && lowestSeg.type!=0) && (!inTurn || canGoAtCurrentSpeed || toOutterEdge>=SAFE_EDGE_MARGIN || toOutterEdge>=0) && trSz>=2 && last.type==0){
			brake = 0;
			//			if (absSpeedY<MODERATE_SPEEDY && toOutterEdge>W*3 && maxTurn && relativeAngle>-0.1){
			//				acc = 1;				
			//				return acc;
			//			}
			//			if ((toOutterEdge> SAFE_EDGE_MARGIN || relativeAngle>-0.1) && maxTurn){
			//				if (toOutterEdge>W) {
			//					double lsteer = gotoPoint(cs, edgeDetector.highestPoint);
			//					if (lsteer*steer<0 || Math.abs(lsteer)>Math.abs(steer)) steer = lsteer;
			//				}
			////				if (steer*turn<0 && toOutterEdge>SAFE_EDGE_MARGIN && maxTurn) steer = -turn;
			//				if (maxTurn || (relativeAngle>=0 || relativeAngle>=-0.1 && toOutterEdge>W*1.75) && absSpeedY<MODERATE_SPEEDY) 
			//					acc = 1;
			//				else {
			////					acc *= 0.6;
			//					if (absSpeedY>VERY_HIGH_SPEEDY){
			//						acc = 1;
			//						steer = 0;
			//					} else acc = 0;
			//				}
			//				brake = 0;
			////				if (absSpeedY>=MODERATE_SPEEDY )
			////					acc = relativeAngle<0 ? 0 : CONSTANT_SPEED_ACC;
			////					acc = CONSTANT_SPEED_ACC;
			//				return acc;
			//			}
			Segment t = (turn==1) ? last.leftSeg : last.rightSeg;
			if (t.num<2) {
				for (int i = trSz-2;i>=0;--i) {
					Segment tt = trArr[ trIndx[i] ];
					if (tt.type!=0) break;
					tt = (turn==1) ? tt.leftSeg : tt.rightSeg;
					if (tt.num>t.num) t = tt;
				}
			}
			double nx = turn*carDirection.y;	
			double ny = -turn*carDirection.x;			
			double ox = nx*speedRadius;
			double oy = ny*speedRadius;
			double dist = 0;
			boolean certain = false;
			int tpe = (t.end.x-t.start.x>0) ? 1 : -1;					
			
			if (t.num>1 && t.points!=null){
				Vector2D p1 = t.points[t.startIndex];
				Vector2D p2 = t.points[t.endIndex];
				dist = Math.sqrt(Geom.ptLineDistSq(p1.x, p1.y, p2.x, p2.y, ox, oy, tmp));
				certain = true;				
			} else dist = Math.sqrt(Geom.ptLineDistSq(t.start.x, t.start.y, t.end.x, t.end.y, ox, oy, tmp));
			if ((relativeSpeedY<0 || canGoAtCurrentSpeed) && tpe==turn){
				steer *=0.5;
//			} else if (relativeSpeedY>0 && relativeTargetAngle>0 && absSpeedY>LOW_SPEEDY && absSpeedY<absLastSpeedY && relativeAngleMovement<0){
//				steer = -turn;
			} else if ((relativeTargetAngle<0 || absSpeedY>absLastSpeedY-1.5) && (relativeAngle>0 || last.start.y<=5 && toOutterEdge>=W || !maxTurn)){				
				Vector2D v = (edgeDetector.highestPoint!=null && last!=null && last.end!=null && edgeDetector.whichE!=turn) ? edgeDetector.highestPoint : (last==null) ? null : last.end;
				if (v!=null && (speedX<lowestSpeed || possiblyGoFast) && absSpeedY<MODERATE_SPEEDY && (distToEstCircle>-tW || toOutterEdge>tW)){
					steer = gotoPoint(cs, v);					
				} else if (dangerSlip && relativeAngle<-0.1 && relativeAngleMovement<0.01 || relativeAngle<0 && relativeAngleMovement<-0.01)
					steer = -turn;
				else if (v!=null && absSpeedY<MODERATE_SPEEDY && (relativeAngleMovement>-0.001 || relativePosMovement>-0.001 || relativeAngle>0 || speedX<lowestSpeed)){ 
					steer = gotoPoint(cs, v);					
				} else if (v!=null && relativeAngleMovement>-0.001 && relativeAngle>0 && absSpeedY<=MODERATE_SPEEDY){ 
					steer = gotoPoint(cs, v);					
				} else if (relativeTargetAngle>0)
					steer = (relativeAngleMovement<-0.001) ? -turn : gotoPoint(cs, v);				
			} else if (edgeDetector.highestPoint!=null && (edgeDetector.highestPoint.length()>85 || dist-W*1.5>speedRadius && toOutterEdge>tW) && absSpeedY<MODERATE_SPEEDY && relativeAngleMovement>-0.01){ 
				steer = gotoPoint(cs, edgeDetector.highestPoint);
//				if (relativeAngleMovement<-0.001 && relativeAngle<-0.001 && relativeAngleMovement<lastRelativeAngleMovement)
//					steer = maxAbs(steer, lastSteer)+turn*0.06;
			} else if (!certain && relativeTargetAngle>0) 
				steer = -turn;			
			
			if (relativeAngleMovement>0.005 && relativeAngleMovement>lastRelativeAngleMovement || relativeAngleMovement>0.01) steer*= 0.5;
			
			if (!maxTurn && (relativeAngle<0 && tpe==turn || tpe!=turn)){
				steer = -tpe;
//				acc = (relativePosMovement>0 || absSpeedY<absLastSpeedY) ? CONSTANT_SPEED_ACC*0.25 : CONSTANT_SPEED_ACC*0.25;
			} 
			
			if (relativePosMovement<-0.001){
				Vector2D v = (edgeDetector.highestPoint!=null && last!=null && last.end!=null && edgeDetector.whichE!=turn) ? edgeDetector.highestPoint : (last==null) ? null : last.end;
				steer = gotoPoint(cs, v);
				steer = simpleSteer(steer,absSpeedY,absLastSpeedY);
			}//*/
			
			if (dist-W>speedRadius) {
				if (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.length()>=EdgeDetector.MAX_DISTANCE || certain || dist-W*1.5>speedRadius || absSpeedY<MODERATE_SPEEDY){
					acc = dist-W*1.5>speedRadius ? 1 : (absSpeedY>HIGH_SPEEDY && absLastSpeedY>absSpeedY) ? 0
							: (absSpeedY>10 && toOutterEdge<W) ? 0 
							: (absSpeedY>=MODERATE_SPEEDY && absLastSpeedY>absSpeedY) 
								? INCREASE_ONE  : 1;
					if (relativePosMovement<-0.02 && (relativeAngle<-0.1 && toOutterEdge<=GAP*1.5 || absSpeedY>HIGH_SPEEDY))
						acc = Math.min(acc,INCREASE_ONE);
					else if (dangerSlip && relativeAngle<-0.01 && relativeAngleMovement<-0.001 && (toOutterEdge<GAP*1.5 || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<-0.01)) 
						acc=Math.min(acc,INCREASE_ONE);
//					else if (acc>CONSTANT_SPEED_ACC && relativePosMovement<-0.015 && relativePosMovement<lastRelativePosMovement && (relativeAngleMovement<-0.01 || relativeAngleMovement<-0.001 && relativeAngleMovement<lastRelativeAngleMovement) )
//						acc = (relativePosMovement<-0.025) ? CONSTANT_SPEED_ACC*0.25 : acc*INCREASE_ONE;

				} else if (relativeAngleMovement>-0.001 || relativePosMovement>-0.001 || dist-W>speedRadius){
					acc = INCREASE_ONE;
//					if (relativePosMovement<-0.001 && a>TURNANGLE*0.37 && relativeTargetAngle>=0 && !canGoAtCurrentSpeed && !canGoToLastSeg && steer*turn<0)
//						steer = -turn;
				} else {
					acc = CONSTANT_SPEED_ACC;
//					if (relativePosMovement<-0.001 && a>TURNANGLE*0.37 && relativeTargetAngle>=0 && !canGoAtCurrentSpeed && !canGoToLastSeg && steer*turn<0)
//						steer = -turn;
				}
				
				t = (turn==1) ? last.leftSeg : last.rightSeg;
				int rm = (turn==1) ? CircleDriver2.edgeDetector.lSize : CircleDriver2.edgeDetector.rSize;
				rm -= t.endIndex+1;
				Vector2D p = (rm<=0) ? null : t.points[t.endIndex+1];
				if (rm>0 && turn==tpe && curPos*turn>-0.2 && absSpeedY<10 && speedX<lowestSpeed && relativePosMovement>-0.01 && acc==1 && t.end.y>30 && p!=null && tpe*(p.x-t.end.x)>0){
					steer = gotoPoint(cs, t.end)*0.5;
				}
				
			} else if (dist-W<speedRadius){
				if (dist-GAP*0.9>speedRadius && absSpeedY<MODERATE_SPEEDY && (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.length()>=EdgeDetector.MAX_DISTANCE || certain || dist-W*1.5>speedRadius || absSpeedY<MODERATE_SPEEDY)) {
//					acc = INCREASE_ONE;
					acc = relativeAngleMovement<-0.01 && relativePosMovement<-0.01 && relativeAngle<-0.1 && absSpeedY>MODERATE_SPEEDY ? 0 : (absSpeedY>HIGH_SPEEDY && absLastSpeedY>absSpeedY) ? 0
							: (absSpeedY>10 && toOutterEdge<W) ? 0 
							: (absSpeedY>=MODERATE_SPEEDY && absLastSpeedY>absSpeedY && relativeAngle<-0.001 && relativeAngleMovement<-0.001) 
								? CONSTANT_SPEED_ACC  : 1;					
				} else acc = (absSpeedY<MODERATE_SPEEDY) ? dist>speedRadius ? 1 : CONSTANT_SPEED_ACC*0.25 : 0;
//				else acc = (absSpeedY<MODERATE_SPEEDY && dist-GAP*0.5>speedRadius) ? CONSTANT_SPEED_ACC : 0;	
				if (absSpeedY<MODERATE_SPEEDY && relativeTargetAngle>=0 && relativePosMovement<0 && t!=null && t.start.y>2 && a>0) steer = -turn;
				if (dangerSlip && (relativeAngle<-0.1 && toOutterEdge<=GAP*1.5 || absSpeedY>HIGH_SPEEDY))
					acc = Math.min(acc,CONSTANT_SPEED_ACC);
				else if (dangerSlip && relativeAngle<-0.01 && relativeAngleMovement<-0.001 && (toOutterEdge<GAP*1.5 || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<-0.01)) 
					acc=Math.min(acc,INCREASE_ONE);
				
				if (relativeTargetAngle>0.3 && relativeAngleMovement<-0.001) acc = CONSTANT_SPEED_ACC*0.25;
//				else if (acc>CONSTANT_SPEED_ACC && relativePosMovement<-0.015 && relativePosMovement<lastRelativePosMovement && (relativeAngleMovement<-0.01 || relativeAngleMovement<-0.001 && relativeAngleMovement<lastRelativeAngleMovement) )
//					acc = (relativePosMovement<-0.025) ? CONSTANT_SPEED_ACC*0.25 : acc*INCREASE_ONE;
			} else if (max && absSpeedY<MODERATE_SPEEDY && toInnerEdge<-W){ 
				acc = INCREASE_ONE;
				
			} else {
				acc = 0;
//				if (absSpeedY<MODERATE_SPEEDY && relativeTargetAngle>=0 && relativePosMovement<0 && t!=null && t.start.y>2) steer = -turn;
			}
			
		/*	if (dangerSlip && (absSpeedY>=MODERATE_SPEEDY || relativeAngleMovement<-0.01) && relativeAngleMovement<-0.001 && relativeAngle<-0.001 && a>0) {
				steer = -turn;
				hazard = 2;
//				smoothSteering();
//				acc = 0;				
			}//*/ 
			
//			if (inTurn && speedX>lowestSpeed-tW && a>TURNANGLE*0.37 && relativePosMovement<-0.001 && relativeTargetAngle>=0 && relativeAngleMovement<0 && steer*turn<0)
//				steer = -turn;
//			if (relativeSpeedY>0 &&  absSpeedY<absLastSpeedY) acc = 1;
			//			steer = -turn;		
//			if (hazard==0) smoothSteering();
//			if (relativePosMovement<-0.001 && relativeAngle>0) hazard = 2;
			if (steer*lastSteer>=0 && relativeSpeedY>0 && absSpeedY<=MODERATE_SPEEDY) {
				boolean noAdjustSteering = (distToEstCircle>0 && relativePosMovement>-0.001 && Math.abs(steer)<Math.abs(lastSteer) && relativeAngleMovement>-0.01);
				if (!noAdjustSteering) smoothSteering();
			}
			if (!canGoAtCurrentSpeed && !canGoToLastSeg && relativePosMovement<-0.001 && speedX>lowestSpeed){
				if (speedX>first_speed && relativeAngle<-0.001 && isOffBalance) {
//					acc = (!maxTurn) ? 0 : (absSpeedY<10 && relativeAngle>0 && speedX<lastSpeed) ? acc : Math.min(acc,lastAcc)*INCREASE_ONE;
				}
			}
			if (turn!=0 && turn!=2 && acc>CONSTANT_SPEED_ACC && Math.abs(a)>0.8 && speedX>lowestSpeed-tW*2){
				acc = 0;
			}
			if (acc==0) acc = CONSTANT_SPEED_ACC*0.25;
			return acc;
		}

		if (inTurn && trSz>2 && last.type!=0 && acc>0 && (absSpeedY<=MODERATE_SPEEDY && relativeAngle>-0.1|| relativeAngle>0) && maxTurn){
			boolean ok = false;
			for (int i = 0;i<trSz;++i){
				if (trArr[ trIndx[i]].type==0) {
					ok = true;
					break;
				}
			}
			if (ok && last.start.y>18){
				return acc;
			}
		}		
		boolean canGoFast = absSpeedY<=10 && relativeAngleMovement>=-0.001 && relativePosMovement>-0.001 && relativeAngle>-0.001 && (speed<lastSpeed && maxTurn || speed<lastSpeed+10 && relativeAngle>-0.001) && (toOutterEdge>=W*1.75 || toOutterEdge>=W && maxTurn);
		//		if (toOutterEdge<GAP || toInnerEdge>-GAP) steer = (steer*first.type>=0) ? steer*1.5 : -curPos*first.type*0.2+curAngle/steerLock;
		Vector2D highest = edgeDetector.highestPoint;
		//		if (acc>0 && speed>200 && speed<lowestSpeed && absSpeedY<MODERATE_SPEEDY && cs.track[9]<=80 && speed-highest.length()*2>=100){
		//			acc = 0;
		//			if (highest.y<=60) {
		//				targetSpeed = highest.length()*2+100;
		//				brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
		//			}
		//			return acc;		
		//		} else 
		boolean seenNewSeg = !isNoNewSeg(last);
		double alpha = (!inTurn || first==null || first.type==0 || first.type==Segment.UNKNOWN) ? 0 : Math.abs(first.end.angle(first.type,0));
				
		
		if (acc>0 && speed<lowestSpeed && relativeAngle>0 && absSpeedY<MODERATE_SPEEDY && relativeTargetAngle>=0 && toOutterEdge>W*2 && relativePosMovement>-0.01 && relativeAngleMovement>0.001){
			//			gear = lastGear;
			if (steer*lastSteer>=0 && inTurn) smoothSteering();
			return acc;
		}
//		if (!inTurn && acc>0 && lowestSpeed<180 && (speed>=250 && speed>=lowestSpeed+FAST_MARGIN && lowestSeg.center.y<SAFE_DISTANCE))
//			acc =0;
//		else 
		if (acc>0 && (!isSafeToAccel || !maxTurn || relativePosMovement<-0.001 || !canGoFast)){
			Segment seg = null;
			for (int i = 0;i<trSz;++i){
				seg = trArr[ trIndx[i]];
				if (seg.end.y>25) break;
			}
			if (seg!=null && seg.end.y<25) seg=null;
			if (!maxTurn && toOutterEdge>GAP)
				acc = (distToEstCircle<-tW && relativePosMovement<-0.01 || relativePosMovement<-0.03) ? 0 : acc;
			else if (last!=null && last.type*turn<0 || trSz==1 && tp*turn<0){				
				//				if (speed>250 && speed>=lowestSpeed+FAST_MARGIN && my>=FAST_MARGIN){
				//					acc = 0;
				//					steer = (Math.abs(relativeTargetAngle)>0.1) ? steer*0.1 : 0;
				//				} else if (speedX>=lowestSpeed-1 && last.start.y>15 && relativePosMovement<-0.001)
				//					acc = (absSpeedY>MODERATE_SPEEDY) ? 0 :  Math.min(acc*0.3,CONSTANT_SPEED_ACC);
				if (toOutterEdge<=GAP && (relativePosMovement<-0.001 ||  relativeAngle<-0.001)) 
					acc = 0;
			} else if (!canGoFast && targetSpeed>=speedX){
				//				if (tp==turn && speed>240 && lowestSegIndx>0 && speed>=lowestSpeed+FAST_MARGIN && mLen<=FAST_MARGIN && !canGoAtCurrentSpeed && !canGoToLastSeg){
				//					acc = 0;
				//					if (relativeSteer>0) 					
				//						steer = (!canGoAtCurrentSpeed || lowestSeg.start.y<18) ? -turn : (relativeAngle>0) ? 0 : (curAngle*turn>0) ? 0 : curAngle/steerLock;
				//				} else
				if (distToEstCircle>-tW && tp==turn && relativePosMovement>-0.01 && speedX<lastSpeed){
					steer = (relativePosMovement>-0.001) ? (steer*turn<0) ? steer*0.5 : steer 
							: (relativeAngleMovement<-0.01) ? -turn : minAbs(steer, -turn*a*0.5/steerLock);
				} else if (possiblyGoFast && tp==turn && relativeAngleMovement>-0.01){
					acc = (relativePosMovement<-0.02) ? acc*INCREASE_ONE : acc;
//					acc = 1;
					if (trSz>1 && (relativePosMovement>-0.001 || toOutterEdge>=tW )&& absSpeedY<HIGH_SPEEDY){ 
						steer = (relativePosMovement>-0.001) ? (steer*turn<0) ? steer*0.5 : steer 
								: (relativeAngleMovement<-0.01) ? -turn : minAbs(steer, -turn*a*0.5/steerLock);
						hazard = 2;
					}
				} else if (tp==turn && trSz==1 && (absSpeedY>=MODERATE_SPEEDY && Math.abs(relativeTargetAngle)>TURNANGLE || relativeAngle<-0.1 && toOutterEdge<SAFE_EDGE_MARGIN)){
					if (relativeTargetAngle>0 && relativeAngle>0 && relativeAngleMovement>0 && relativePosMovement>-0.001)
						acc = 1;
					else if (relativeTargetAngle>0){
						if (relativeAngle<0 || !maxTurn) 
							acc = (speed>lowestSpeed+FAST_MARGIN) ? 0 : mustSlowDown ? CONSTANT_SPEED_ACC*0.25 : CONSTANT_SPEED_ACC;
//						else 
//							acc = CONSTANT_SPEED_ACC*0.5;
					}
				} else if (tp==turn && trSz==1 && !maxTurn && (relativeAngle<-0.01 || (relativeAngle<0 || toOutterEdge<SAFE_EDGE_MARGIN) && (absSpeedY>=MODERATE_SPEEDY || relativeCurPos>=1 || absSpeedY>absLastSpeedY)) && (relativePosMovement<-0.001 || relativeAngleMovement<-0.001 || relativeTargetAngle<-0.001 || absSpeedY>absLastSpeedY) && toOutterEdge<SAFE_EDGE_MARGIN){
					acc = (toOutterEdge<=GAP && relativePosMovement>0) ? acc : (relativeAngle<0 || absSpeedY>=MODERATE_SPEEDY) ? 0 : CONSTANT_SPEED_ACC*0.5;
					if (speed>lowestSpeed && absSpeedY<=MODERATE_SPEEDY && relativeAngleMovement<0 && relativeTargetAngle>=0) steer = -turn;
				} else if (tp==turn && relativePosMovement<-0.001 && relativeTargetAngle>-0.001 && (toOutterEdge<=W || (absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<-0.001 || toOutterEdge<W*1.5 && relativeAngle<=0) &&  (seg!=null && seg.type!=0 && tp==seg.type && speed>speedAtRadius(Math.max(first.radius,seg.radius)) || lowestSegIndx>0 && first_speed-lowestSpeed<15 || absSpeedY>absLastSpeedY && relativeAngleMovement<0 || !maxTurn && relativeAngle<=0 && relativeAngleMovement<-0.001) || toOutterEdge<W && relativeAngleMovement<-0.001 || (toOutterEdge<W*1.5 && speed>first_speed+10 && relativeAngleMovement<-0.001 &&  relativeAngle<=0 ))){
					if (absSpeedY>MODERATE_SPEEDY && toOutterEdge>=W*2.5 && relativeAngle>=0 && relativeAngleMovement<-0.001 ){
						acc *= (relativeAngleMovement<-0.01 && speedX>lastSpeed+m*2 || distToEstCircle<-tW) ? 0 : (speedX>lastSpeed+m*2) ? CONSTANT_SPEED_ACC : 1;
//						acc = (speedX>first_speed && alpha>=PI_2*0.5 || dangerSlip) ? 0 : acc*CONSTANT_SPEED_ACC;
						//						steer *= 0.5;
					} else {
						if (absSpeedY>=HIGH_SPEEDY) {
							steer *= 0.5;
							if (relativeAngle<0) acc = (speedX>lowestSpeed-tW) ? 0 : CONSTANT_SPEED_ACC*0.5;
						} else acc = mustSlowDown ? 0 :(relativeAngle>0 && relativeAngleMovement>0) ? acc : (speedX>lowestSpeed-tW) ? 0 : CONSTANT_SPEED_ACC;						
					}

				} else if (turn!=0 && tp==turn && relativePosMovement<-0.001 && relativeAngleMovement<-0.001 && speedX>lowestSpeed && relativeTargetAngle>0.01 && !canGoToLastSeg && !canGoAtCurrentSpeed){
					acc = mustSlowDown ? 0 :(!maxTurn || (dangerSlip || absSpeedY>10) && relativeAngle<0) 
							? (toOutterEdge>SAFE_EDGE_MARGIN) ? 
									 (absSpeedY>=HIGH_SPEEDY) ? acc*CONSTANT_SPEED_ACC : acc 
									:0 
							: (maxTurn && (absSpeedY<MODERATE_SPEEDY && relativeAngle>0 && toOutterEdge>=W*0.75 
								|| speedX<lastSpeed+m && (distToEstCircle>-tW || toOutterEdge>tW || relativeAngle>-0.001) 
								|| (edgeDetector.highestPoint==null || edgeDetector.highestPoint.length()-m<50) && relativePosMovement>-0.01 && relativeAngle>-0.15 
								|| speedX<lastSpeed && relativeAngleMovement>-0.01 && toOutterEdge>W && relativeAngle>-0.1)) ? acc : Math.min(acc,lastAcc)*INCREASE_ONE;
					if (acc>CONSTANT_SPEED_ACC && relativeAngleMovement<-0.01 && relativePosMovement<-0.001 && steer*turn<0 && absSpeedY>MODERATE_SPEEDY) acc = CONSTANT_SPEED_ACC;
				} else if (turn!=0 && tp==turn && relativePosMovement<-0.001 && (relativeAngle<-0.1 || relativeAngle<-0.001 && a>TURNANGLE/1.5 || absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<0.001 && a>TURNANGLE || relativeAngle<0  && relativeAngleMovement<0 && !canGoAtCurrentSpeed && !canGoToLastSeg && (toOutterEdge<SAFE_EDGE_MARGIN || a>TURNANGLE/1.5) && (absSpeedY>MODERATE_SPEEDY || speedX>lowestSpeed-tW))){
					if (!canGoToLastSeg){
						if (relativeAngleMovement<-0.001 || !maxTurn || relativeAngle<-0.01){
							if (!(speedX<lowestSpeed-tW && absSpeedY<10 && toInnerEdge>=-W*2.5))								
								acc = 
									(speedX>lowestSpeed) 
//									? (maxTurn) 
//											? (absSpeedY<10) ? acc : absSpeedY<=MODERATE_SPEEDY ? acc*CONSTANT_SPEED_ACC : (absSpeedY<HIGH_SPEEDY) ? acc*CONSTANT_SPEED_ACC : 0 
//											: 0
									? (maxTurn) 
											? mustSlowDown && !isSafeToAccel && speedX>highestSpeed ? 0 : (isSafeToAccel && speedX<first_speed && !dangerSlip && (distToEstCircle>=-W || relativeAngleMovement>0.01 && distToEstCircle>-W*1.5) 
													|| distToEstCircle>-tW && relativeAngleMovement>-0.01 && toOutterEdge>Math.min(toOutterEdge,tW) 
													|| relativeAngle>0 || relativeAngle>-0.005 && relativeAngleMovement>0 || relativeAngleMovement>0.001 && speedX<=100) 
													? acc 
													: acc*CONSTANT_SPEED_ACC*0.25 
											: 0
										: (relativeAngle<-0.001 && (relativeAngleMovement<-0.001 || dangerSlip && distToEstCircle<-tW || dangerSlip && slip>15) && speedX>lowestSpeed && !isSafeToAccel) 
											? 0 
											: (slip>10 && relativeAngle<0.01 && relativePosMovement<-0.01) ? acc*CONSTANT_SPEED_ACC	: acc;
//							if (acc>lastAcc){
//								acc = lastAcc;
//							} else acc = Math.min(Math.max(lastAcc,acc)*0.9,acc);
						} 
						else if (mustSlowDown)
							acc = 0;
//						else acc = Math.min(acc,lastAcc)*INCREASE_ONE;
					} 
//					if (!canGoAtCurrentSpeed && !canGoToLastSeg){
//						if (trSz>1 && relativeAngle>-0.1 && speed<lastSpeed && (speed<lowestSpeed || lowestSeg.end.y<=10) && (maxTurn || relativeAngle>=0 || relativePosMovement>=0 || toOutterEdge>W*2))
//							acc = 1;
//						else if (maxTurn && absSpeedY<MODERATE_SPEEDY && toOutterEdge>W*2){ 
//							if (speed>lastSpeed && lowestSeg.end.y>10) acc = CONSTANT_SPEED_ACC;
//						} else acc = 0;
//					} else acc = 0; 
				} else if (turn!=0 && tp==turn && maxTurn && (absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY &&  relativePosMovement<-0.001 &&  relativeAngle<-0.01 && relativeAngleMovement<-0.001 && toOutterEdge<=SAFE_EDGE_MARGIN)){
					if (relativeAngle>=0 && (relativeAngleMovement>-0.001 || relativePosMovement>-0.001) && toOutterEdge>=W*2.5){ 
						//						steer = 0;
						acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC*0.5;
					}
					if (acc>0 && absSpeedY>=MODERATE_SPEEDY) acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC*0.5;
				} else if (inTurn && !maxTurn && trSz>1 && lowestSegIndx==trSz-1 && speedX>first_speed && (relativeAngleMovement<-0.001 || relativePosMovement<-0.001) && toOutterEdge<SAFE_EDGE_MARGIN){
					acc = 0;
					//				else if (inTurn && !maxTurn && trSz>1 && first_speed>lowestSpeed && speed>lowestSpeed+10 && first_speed-lowestSpeed<FAST_MARGIN){ 
					//					acc = 0;
				} else if (tp==turn && absSpeedY>=MODERATE_SPEEDY && relativeAngle<0 && (absSpeedY>absLastSpeedY || relativePosMovement<-0.001 || relativeAngleMovement<-0.001) && speed>=lowestSpeed+tW ) {
					if (!canGoToLastSeg) 
						acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC;
					else steer = (-turn*relativeTargetAngle/steerLock)*0.5;
				} else if (tp==turn && relativePosMovement<-0.001 && relativeAngle<-0.1 && relativeAngleMovement<-0.001 && speed>=lowestSpeed-tW && speed>=lastSpeed-tW)
					acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC;
				else if (tp==turn && !canGoToLastSeg && speed>targetSpeed+FAST_MARGIN ){
					acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC;
//				} else if (tp*turn==-1 && (relativeTargetAngle<0 || toInnerEdge>-W)){
//					acc = 0;
				} else if (inTurn && trSz>1 && !canGoToLastSeg && !max &&  speed>lowestSpeed && (speed>highestSpeed || speed>highestSpeed-10 && absSpeedY>MODERATE_SPEEDY) && last!=null && last.end.y>30)
					acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC;
				else if (inTurn && tp==turn && trSz>1 && !canGoAtCurrentSpeed && !canGoToLastSeg && last==lowestSeg && toOutterEdge<W*2.5 && (relativeAngle<0 || absSpeedY>10) && relativePosMovement<0){
					acc = (speed>lowestSpeed) ? 0 : CONSTANT_SPEED_ACC;
				//} 
				/*else if (!canGoAtCurrentSpeed && !canGoToLastSeg && relativePosMovement<-0.001 && speedX>lowestSpeed){
					if (speedX<first_speed && (relativeAngle<-0.001 && relativeAngleMovement<-0.001 || isSafeToAccel && (relativeSpeedY<0 || lastSteer*turn>=0) )) {
						targetSpeed = Math.max(targetSpeed, faster(lowestSpeed, m));
					} else if (speedX<lastSpeed) {
						if (mustSlowDown || absSpeedY>MODERATE_SPEEDY && speedX>first_speed && alpha>PI_2 && relativeAngle<-0.001) {
							targetSpeed = speedX-1;
						} else if ((relativeAngle>-0.001 && relativeAngleMovement>0 || maxTurn && toOutterEdge>=W && (absSpeedY<10 || relativeAngle>0.001 && absSpeedY<20) ) )
							targetSpeed = (dangerSlip && absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<-0.001 && distToEstCircle<-tW) ? speedX : Math.min(targetSpeed, lastSpeed+m);
						else targetSpeed = distToEstCircle<-W && lastDistToEstCircle>distToEstCircle ? speedX-1 : speedX;
					} else if (speedX>lastSpeed && (distToEstCircle<-W && lastDistToEstCircle>distToEstCircle || mustSlowDown || relativeAngle<0 && relativeAngleMovement<-0.01 && !isSafeToAccel))
						targetSpeed = slip<10 && lastSteer*turn<0 && (relativeAngleMovement<-0.01 || relativeAngle<-0.001) ? Math.max(speedX-1,lastSpeed+m) : targetSpeed;
					if (speedX>targetSpeed) {
						acc = 0;
					} else acc = (speedX<targetSpeed-1.5) ? 1 : Math.min(acc,2/(1+Math.exp(speedX - targetSpeed-1)) - 1);//*/
				} else if (!isSafeToAccel && relativePosMovement>0 && relativeTargetAngle>0 && (speedX>lastSpeed+FAST_MARGIN+tW+m*0.5 || speedX>lastSpeed+FAST_MARGIN+tW && absSpeedY>MODERATE_SPEEDY)) {
					acc = absSpeedY>MODERATE_SPEEDY && (relativeAngleMovement<-0.001 || relativeAngleMovement<lastRelativeAngleMovement) ? 0 : acc;
					targetSpeed = Math.min(targetSpeed, faster(lastSpeed, m));
				} else if (relativePosMovement<-0.01 && relativeAngle<-0.1 && relativeAngleMovement<0.001 && !isSafeToAccel && lastSteer*turn<0 && speedX>lowestSpeed)
					acc = 0;
				else if (m<15 && relativePosMovement>-0.001 && relativeAngleMovement<-0.001 && speedX>highestSpeed+FAST_MARGIN+m && distToEstCircle<0 && distToEstCircle<lastDistToEstCircle)
					acc = 0;
				else if (!isSafeToAccel && m<20 && relativePosMovement>-0.001 && (edgeDetector.highestPoint==null || edgeDetector.highestPoint.length()-m<50) && 
						(speedX>highestSpeed+Math.max(FAST_MARGIN,m) || speedX>highestSpeed+Math.min(FAST_MARGIN,m*2) && (distToEstCircle<-GAP || distToEstCircle<0 && distToEstCircle<lastDistToEstCircle || distToEstCircle<GAP*0.5 && relativeAngleMovement<-0.01)) 
						&& (distToEstCircle<-GAP || distToEstCircle<0 && (absSpeedY>=MODERATE_SPEEDY ||  distToEstCircle<lastDistToEstCircle) || distToEstCircle<GAP*0.5 && relativeAngleMovement<-0.01))
					acc = 0;
				else if (!isSafeToAccel && m<20 && distToEstCircle<-GAP && (edgeDetector.highestPoint==null || edgeDetector.highestPoint.length()-m<50) && (relativeAngleMovement<-0.001 && distToEstCircle<-W || relativeAngleMovement<-0.01) && relativePosMovement<0.001 && speedX>highestSpeed+m)
					acc = 0;
				else if (trSz==1 && steer*turn<0 && speedX>lowestSpeed+tW && relativeAngleMovement<-0.01 && relativeAngleMovement<lastRelativeAngleMovement && relativePosMovement<-0.001)
					acc = 0;
				
//				else if (!isSafeToAccel && steer*turn<0 && relativePosMovement<-0.001 && relativeAngleMovement<-0.01 && speedX>highestSpeed+m*2)
//					acc = 0;
			
//				else if (tp==turn && relativePosMovement<-0.001 && absSpeedY>MODERATE_SPEEDY && speedX>lowestSpeed && (trSz==1 || speedX>first_speed) && alpha>=PI_2*0.5) {
//					System.out.println(alpha+"   "+PI_2*0.5);
//					acc = 0; 
//				}
//					else if (relativePosMovement<-0.001 && (lastAcc>0 || lastBrkCmd==0)){
//					if (Math.abs(acc-lastAcc)>0.1)
//						acc = lastAcc+Math.signum(acc-lastAcc)*0.1;
//				}
			}

			//			if (acc==0 && absSpeedY<=MODERATE_SPEEDY){
			//				if (relativeAngle>=0 && maxTurn) 
			//					acc = tmpAcc;
			//				else if (relativeAngle>=-0.01 && relativeAngleMovement>=0 && toOutterEdge>GAP) 
			//					acc = tmpAcc;
			//				else if (relativeAngle>-0.1 && toOutterEdge>W+W)
			//					acc = tmpAcc;
			//			}
		}

		
		if (acc>=CONSTANT_SPEED_ACC && steer*turn<0 && m<15 && speedX>highestSpeed+tW  && !canGoVeryFast && relativePosMovement<-0.001 && (relativeAngle<0.05 && relativeAngleMovement<-0.01 || relativeAngle<0 && relativeAngleMovement<-0.001)){
			acc = 0;
		} else if (acc>0 && absSpeedY>HIGH_SPEEDY && relativePosMovement<-0.02 && relativeAngleMovement<-0.001 && a>0 && (relativePosMovement<-0.025 || relativePosMovement<lastRelativePosMovement) ) 
			acc = 0;
		if (turn!=0 && turn!=2 && acc>CONSTANT_SPEED_ACC && Math.abs(a)>0.5 && speedX>lowestSpeed-2*tW  
				|| a<0 && toInnerEdge>-GAP && relativePosMovement>0.001 || (a<-0.1 || a<0 && b<TURNANGLE*0.5) && relativePosMovement>0.01 && (relativeAngleMovement>-0.001 || a<-0.2)
				|| (a<0 || b<TURNANGLE*0.5) && relativeAngleMovement>0.01 && relativePosMovement>0.01){
			acc = (a<0 && relativeAngle<0 && relativeAngleMovement<0) ? acc : 0;
		}
		
		if (acc>CONSTANT_SPEED_ACC && a>0 && distToEstCircle<-W && relativePosMovement<-0.001 && absSpeedY>=MODERATE_SPEEDY && aheadSeg!=null && aheadSeg.type!=0 && aheadSpeed+al<speedX){
			acc = 0;
		}
		if (acc>CONSTANT_SPEED_ACC && absSpeedY>MODERATE_SPEEDY && speedX>targetSpeed-10 && speedX>lowestSpeed && relativeAngleMovement<-0.01 && (relativePosMovement<0.001 || distToEstCircle<0)) 
			acc = CONSTANT_SPEED_ACC*0.25;
		
		if (acc>CONSTANT_SPEED_ACC && speedX>highestSpeed && relativePosMovement<-0.001 && distToEstCircle<-W*1.5 && toOutterEdge<(tW+SAFE_EDGE_MARGIN)*0.5 && (highestPoint==null || highestPoint.y<50))
			acc = CONSTANT_SPEED_ACC*0.25;
		if (acc>=CONSTANT_SPEED_ACC &&  slip>15)
			acc *= CONSTANT_SPEED_ACC;
		
//		if (acc>CONSTANT_SPEED_ACC*0.25 && speedX>170 && relativePosMovement<-0.001 && (edgeDetector!=null && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.length()<40 || ahead!=null && ahead.y<35))
//			acc = 0;
		

		//if (acc>0 && speed>150 && speed>lowestSpeed+80) acc = 0;				

		double sign = (speedY<0) ? 1 : -1;

		//Now rules for (relativeTargetAngle<0)************************************************************************************************//		
		if (steer*turn>0 && absSpeedY<=HIGH_SPEEDY && (canGoAtCurrentSpeed || canGoToLastSeg) && mLen<35 && speed>lastSpeed+Math.max(FAST_MARGIN,mLen) && relativeTargetAngle<0){
			//			steer = (relativePosMovement>0) ? relativeTargetAngle<-0.05 ? steer : 0 : -turn;			
		} else if (relativeTargetAngle<-0.01 && steer*turn>0){
			if (lastSteer*steer<0) hazard = 1;
			if (mustPassPoint.y>FAST_MARGIN){
				//				steer *= (speed>200) ? 0.1 : (speed>150) ? 0.5 : 1;
			} else if (absSpeedY<=MODERATE_SPEEDY && last!=null && last.end.y<20){
				steer *= (speed>200) ? 0.01 : (speed>150) ? 0.1 : (speed>100) ? 0.3 : 1;
			}

		}

		/*if (flying){
			if (speedX>targetSpeed) 
				brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
			else brake = 0;

			if (tp==turn || tp*turn==0){
				if (relativeAngleMovement>=-0.001 || relativePosMovement>=-0.001){
					if (acc>0 && (relativeCurPos<=-0.4 || absSpeedY>=MODERATE_SPEEDY || relativeHeading<=-0.001)) acc = 0;					
					brake = 0;
					if (relativeHeading>=-0.001){
						if (absSpeedY>=VERY_HIGH_SPEEDY) 
							steer = -sign;
						else if (absSpeedY>=HIGH_SPEEDY) 
							steer = (relativeSteer>0) ? -steer : steer;	
						else steer = 0;					
					}
				}
			};

			//			acc = 1;
			//			gear = 1;
			if (acc==0 || brake>0)gear =  Math.min(getGear(speed),lastGear);
			if (steer*lastSteer>=0) smoothSteering();
			return acc;
		}//*/


		/*if (speedX<250 && canGoAtCurrentSpeed && speedX>targetSpeed && lowestSpeed+FAST_MARGIN>=speed && curPos*tp>0.2 && lastSteer*tp<=0 && carDirection.x*tp>=0){
			//			acc = (absSpeedY<15) ? 2*Math.E/(1+Math.E) - 1 : 0;
			acc = 0;
			if (relativeAngleMovement>=-0.001 || relativePosMovement>=-0.001){
				if (acc>0 && (relativeCurPos<=-0.4 || absSpeedY>=MODERATE_SPEEDY || relativeHeading<=-0.001)) acc = 0;					
//				brake = 0;
				if (relativeHeading>=-0.001){
					if (absSpeedY>=VERY_HIGH_SPEEDY) 
						steer = -sign;
					else if (absSpeedY>=HIGH_SPEEDY) 
						steer = (relativeSteer>0) ? turn*s : steer;	
					else steer = (absSpeedY>=MODERATE_SPEEDY) ? steer*0.2 : steer;
				}
			}			
			if (Math.abs(steer)>s) brake = 0;
			if (acc==0 || brake>0)gear =  Math.min(getGear(speed),lastGear);
			//			if (absSpeedY<=MODERATE_SPEEDY && relativeTargetAngle>0){
			//				steer = -turn;
			//			}
			//			if (lastSpeedX<=speedX && lastBrkCmd>0) brake = 1;
			lastAcc = acc;						
			lastTargetAngle = angle;			
			return acc;
		}//*/
		//		double safeSpeed = (lowestSegIndx==trSz-1 && lowestSeg!=null) ? lowestSpeed+Math.max(lowestSeg.start.y,0)*2 : targetSpeed;		
		/*if (speedX<250 && turn!=Segment.UNKNOWN && turn <2 && speedX<targetSpeed && targetSpeed<safeSpeed+50 && (last!=null && trSz>0 && (last.type==0 || first.type!=0 && last.radius>first.radius)) &&(absSpeedY<MODERATE_SPEEDY || absSpeedY<HIGH_SPEEDY && (tp==turn || tp==0) && toOutterEdge<=SAFE_EDGE_MARGIN && relativeAngle<=-0.001)){
			//			if (toOutterEdge<=GAP || !canGoAtCurrentSpeed) acc = 0;
			lastTargetAngle = angle;
			if (absSpeedY>lastSpeedY && absSpeedY>MODERATE_SPEEDY && relativePosMovement<0)
				steer = -turn;
			//			if (absSpeedY<HIGH_SPEEDY && (!canGoAtCurrentSpeed || speedX>=first_speed-tW && toOutterEdge<SAFE_EDGE_MARGIN && relativeAngle<-0.001)) 
			//				steer = absSpeedY<=MODERATE_SPEEDY && (!canGoAtCurrentSpeed || first.type==turn) ? -turn : steer;			
			return acc;
		}//*/

		if (acc==0){
			if (drift || tooFast || speed<targetSpeed+10 && toOutterEdge<tW+GAP || speed>targetSpeed|| trSz==1 && speed>lastSpeed+FAST_MARGIN || (trSz>1 && speed>lastSpeed+FAST_MARGIN+last.start.y) || toOutterEdge<=GAP && relativePosMovement<=-0.001 || absSpeedY>MODERATE_SPEEDY || toOutterEdge<SAFE_EDGE_MARGIN && speedX>lowestSpeed && relativeSteer<-0.001 && (relativePosMovement<-0.001 || relativeAngle<0 ) ) {			
				/*if ((speedX<=lowestSpeed || speedX<=targetSpeed && speedX<=safeSpeed)&& absSpeedY<LOW_SPEEDY && (curAngle*tp>=-0.05 && curPos*tp>-0.1)){
					if ((relativeTargetAngle>=-0.001 || relativeAngle>=-0.001) && (tooFast && nextSlowSeg.start.y>FAST_MARGIN || (last!=null && last.start.y>FAST_MARGIN))) 
						if (relativeSteer>=0 && toOutterEdge>=GAP) steer = 0;
					lastTargetAngle = angle;
					lastTargetSpeed = targetSpeed;		
					if (acc==0 || brake>0)gear =  Math.min(getGear(speed),lastGear);
					if (speed<targetSpeed+10 &&  toOutterEdge<tW+GAP && !canGoAtCurrentSpeed && !canGoToLastSeg && !maxTurn){
						gear = lastGear;
						if((relativeAngle<0 || absSpeedY>MODERATE_SPEEDY)){
	//						acc = (absSpeedY>HIGH_SPEEDY) ? 1 : CONSTANT_SPEED_ACC;						
							brake = (absSpeedY>HIGH_SPEEDY || relativeAngle>0 ) ? 0 : Math.max(brake,0.1);												
						}
						steer = -turn;
						acc = (absSpeedY>HIGH_SPEEDY || absSpeedY>MODERATE_SPEEDY && absSpeedY<absLastSpeedY-2.5 && relativeAngleMovement<0 && relativeTargetAngle>0 && relativeAngle>0) ? 1 : (relativeAngle<0 || absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY || toOutterEdge<GAP) ? 0 :  Math.max(acc, CONSTANT_SPEED_ACC);							
//						acc = (absSpeedY>HIGH_SPEEDY) ? 1 : (relativeAngle<0 || absSpeedY>=MODERATE_SPEEDY || toOutterEdge<W) ? 0 : CONSTANT_SPEED_ACC;
//						acc = (absSpeedY>HIGH_SPEEDY) ? 1 : (relativeAngle<0 || absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY) ? 0 : acc;
						if (acc>0) {
							brake = 0;
//							if (toOutterEdge<W) acc = CONSTANT_SPEED_ACC;
						}						
					}

					return acc;
				}//*/

				/*				if (speedX<lowestSpeed+10) {							
					//				if (gear>1) gear--;								
					if (tp==turn || tp*turn==0){
						if (relativeAngleMovement<-0.001 || relativePosMovement<-0.001){ 
							//						if (toOutterEdge<=GAP && acc>0 && relativeAngle<-0.001) acc = 0;
							//						if (acc>0 && (relativeCurPos<=-0.4 || absSpeedY>=HIGH_SPEEDY)) 
							//							acc = 0;
							//						if (brake>0) brake = Math.min(brake,0.05);
							//						if (acc>0) if (gear>1) gear--;
							//						if (relativeCurPos<0.2 && speed>lowestSpeed) steer = -turn;						
						}
						if (relativeHeading>=0.01){
							if (absSpeedY>=HIGH_SPEEDY){
								steer = 0;
								if (speed<lowestSpeed || relativeAngle>=0) acc = 1;
								//							if (toOutterEdge<=GAP && acc>0 && relativeAngle<-0.001) acc = 0;
								if (absSpeedY>absLastSpeedY && steer*sign<0) brake = 0;
								if (brake==0) acc = CONSTANT_SPEED_ACC;
							} else if (absSpeedY>=MODERATE_SPEEDY){ 
								if (absSpeedY>absLastSpeedY || relativeSpeedY<0){								
									steer = (absSpeedY>absLastSpeedY && (lastSteer*sign<0 || relativeAngleMovement>=0 && relativePosMovement>=0)) ? turn : steer;
									//								if (absSpeedY>=HIGH_SPEEDY || toOutterEdge<=GAP && acc>0 && relativeAngle<-0.001) acc = 0;
									acc = CONSTANT_SPEED_ACC;
								} else steer = 0;
								if (absSpeedY>absLastSpeedY && steer*sign<0) brake = 0;								
							} 							
						}
						if (acc==0 || brake>0)gear =  Math.min(getGear(speed),lastGear);
					}				
					lastAcc = acc;				
					lastTargetAngle = angle;				
					lastTargetSpeed = targetSpeed;							
					return lastAcc;
				}//*/					

				int lowestGear = getGear(Math.min(lowestSpeed+FAST_MARGIN,targetSpeed));
				//			if (gear>1 && gear>lowestGear) gear--;
				gear = getGear(speed);
				if (lowestGear<gear){
					//gear = Math.max(gear,lowestGear);
					//					gear = (int) ((tooFast) ? (relativeHeading<0) ? gear : Math.min(lowestGear+1,gear) : 
					//						(targetSpeed<speed || acc==0 || speed<lowestSpeed+FAST_MARGIN && (absSpeedY>MODERATE_SPEEDY || absSpeedY>absLastSpeedY)) ?  Math.max(gear,lastGear) : gear);
					//					if (gear>lastGear && (brake>0 || acc==0)) gear = lastGear;
					//					gear = getGear(speed+20) -1;
					gear = Math.max(getGear(speed+8),lowestGear);
				}
				//			gear = lowestGear;

				//			gear = getGear(speed);
				/*if (!tooFast && absSpeedY<MODERATE_SPEEDY && relativeSteer<0 && speedX<=first_speed && lowestSeg!=null && lowestSeg.start.y>NEAR_DISTANCE){
					if (acc<0) {
						brake = -acc;
						acc = 0;
					}
					return acc;
				} //*/

				
				//				double TURNDIST = (speed>targetSpeed+FAST_MARGIN) ? 25 : (speed>targetSpeed+20) ? 20 :15;
				boolean mustTurn = last!=null && last.type!=0 && last.radius<30 ? a>=TURNANGLE*2 : a>=TURNANGLE || !canGoToLastSeg && a>=TURNANGLE*0.5;
				double FAST_GAP = Math.max(mLen,FAST_MARGIN);
				double MODERATE_GAP = Math.max(mLen*0.5,15);

				if (absSpeedY>VERY_HIGH_SPEEDY && relativePosMovement<0){
					if (absSpeedY>absLastSpeedY){
						acc = CONSTANT_SPEED_ACC;
						if (steer*turn>0 || absSpeedY>=50) 
							steer = 0;
						else steer = -turn;
						brake = 0;
					} else {
						acc = (relativeAngleMovement>0.001) ? 1 : CONSTANT_SPEED_ACC;
						steer = -turn;
						brake = 0;
					}
					return steer;
				} else if (absSpeedY<HIGH_SPEEDY && relativeTargetAngle>=-0.001 && relativeTargetAngle<0.1){
					if (debug) System.out.println("------------------------------BRAKE HARD OR DRIFT ----------------------------");											
					//					boolean brakeHard =  speed>=targetSpeed && (relativeCurPos>0.5 || relativeTargetAngle<0.05 || (Math.abs(carDirection.x)<0.05 && relativeCurPos>0.5 && tooFast)) ? true : false;
					//					if (mLen<5){ 
					//						brake = (speed>=targetSpeed+15) ? 1 : 0;
					//					}

					if (relativeSpeedY<0 && (absSpeedY>=MODERATE_SPEEDY || absSpeedY>absLastSpeedY) )
						steer *= 0.5;
					//					if (brakeHard && last!=null) targetSpeed = Math.min(lastSpeed+last.start.y+tW,targetSpeed);
					//				if (relativeTargetAngle>=0 && tooFast) steer = -turn;
					if (steer>1) 
						steer = 1;
					if (steer<-1) steer = -1;
					counterDrift = true;
					if (absSpeedY>VERY_HIGH_SPEEDY){
						//						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if (brake<0){
							brake = 0;
							acc = 0;
						} else acc = 0;						
						lastTargetSpeed = targetSpeed;
						lastTargetAngle = angle;
//						if (F_LFT<=0 && F_RGT<=0) {
//							brake = 0;
//							acc = 0;
//						}
						//						if (brake>0 && brake<1 && lastSpeedX<=speedX && lastBrkCmd>0) {
						////							brake = 1;
						//							steer = -turn;
						//						}
						return acc;

					}

					brake = (speed>targetSpeed) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : 0;
					double TURNDIST = (speed>targetSpeed+FAST_MARGIN) ? 5 : 5;
					if (speed>=lowestSpeed+FAST_MARGIN){						
						acc = 0;
						if ((absSpeedY>HIGH_SPEEDY || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<0 && relativePosMovement<0 && relativeTargetAngle>0) && speed>targetSpeed+FAST_GAP || mLastY>0 && speed>=targetSpeed+FAST_GAP && !mustTurn || speed>=targetSpeed+FAST_GAP && relativeSpeedY<0 && !canGoToLastSeg){
							brake =  speedX>targetSpeed+25 
											? BRAKE_HARD(a, b, m, lastSpeed, absSpeedY, targetSpeed)
											: (absSpeedY>=MODERATE_SPEEDY || distToEstCircle<GAP) ? brake : 1;
//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
							if (isSafeToAccel && toInnerEdge<-W*1.5 || (absSpeedY<=MODERATE_SPEEDY || relativePosMovement>0) && canGoAtCurrentSpeed && canGoToLastSeg || absSpeedY<absLastSpeedY && relativeAngleMovement<0) 
								brake = (isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY || distToEstCircle<GAP) ? brake : 1;
							else if (!isSafeToAccel && relativeAngleMovement>0.001 && relativeTargetAngle>0 && a<TURNANGLE*1.5){
								brake = 1;
//								steer = -turn;
							}
						} else if (speed>targetSpeed+MODERATE_GAP){ 
//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
							if (speedX>targetSpeed+25){
//								brake = relativePosMovement<-0.001 && relativeAngleMovement<-0.01 ? brake*0.5 : steer*turn>=0 ? 1 : relativePosMovement<-0.001 || distToEstCircle<0 ? relativeAngleMovement<-0.001 || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle ? brake*0.5 : (relativeAngleMovement>0.01) ? 1 : brake : (absSpeedY>=MODERATE_SPEEDY && m<=20 && distToEstCircle<0 || distToEstCircle<0) ? brake : 1;
								brake = BRAKE_HARD(a, b, m, lastSpeed, absSpeedY, targetSpeed);
//								if ((targetSpeed<110 || distToEstCircle>0 || distToEstCircle>-GAP && relativeAngleMovement>0.001) && relativePosMovement>0 && absSpeedY<MODERATE_SPEEDY && distToEstCircle>-GAP && relativeAngleMovement<-0.01)
//									brake = 1;
							} else if (isSafeToAccel && mLen>15 && toInnerEdge<-W*1.5){
								brake = (distToEstCircle>0 && relativeAngleMovement>0.01 && absSpeedY<MODERATE_SPEEDY) ? 1 : (isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY || relativeAngleMovement<-0.01) ? brake : 1;								
							} else if (isSafeToAccel && mLen<15|| steer*turn>0 && toInnerEdge<-W*1.5) 
								brake *= 1;
							else if (!canGoToLastSeg && relativePosMovement>=0 && !canGoAtCurrentSpeed) {
								brake = (relativeAngleMovement<0) ? brake : (distToEstCircle>0 || distToEstCircle>GAP*0.5 && distToEstCircle>lastDistToEstCircle) && (absSpeedY<=MODERATE_SPEEDY || canGoToLastSeg) ? 1 : brake;
							} else if (relativePosMovement<0) 
								brake = (canGoToLastSeg || canGoAtCurrentSpeed || absSpeedY<absLastSpeedY-1.5 && relativeAngleMovement<0) ? brake: brake;
							else brake = (steer*turn>0 && relativeTargetAngle>-0.001) ? 1 :(isSafeToAccel && relativeAngleMovement>0 && steer*turn<=0) ? 1: brake;
						} else if (speed>targetSpeed+Math.max(10,mLen*0.5)){
//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
							if (isSafeToAccel && mLen>15 && toInnerEdge<-W*1.5){
								brake = (relativeAngleMovement<-0.001) ? brake*0.5 : brake;
							} else if (speed>=targetSpeed+15 && ((isSafeToAccel || steer*turn>0) && toInnerEdge<-W*1.5)) 
								brake = (relativePosMovement>0 && relativeAngleMovement>0.001) ? 1 : (isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY) ? brake : 1;
							else brake = (relativePosMovement>0) ? lastSpeed>90 && (highest==null || highest.y>30) && (seenNewSeg && highest!=null && highest.y>50 || speedX<targetSpeed+15 && targetSpeed>120) ? 0 : brake : steer*turn<0 ? 0 : 0.5*(speed*speed-targetSpeed*targetSpeed)/(speed*speed);
//							if (!isSafeToAccel && absSpeedY>=MODERATE_SPEEDY) brake = Math.max(brake,0.1);							
						} else if (speed>=targetSpeed+15 && !mustTurn){
//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
							if (isSafeToAccel && mLen>15 && toInnerEdge<-W*1.5){
								brake = (relativeAngleMovement<0 || absSpeedY>MODERATE_SPEEDY) ? brake : 1;
							} else if (isSafeToAccel && mLen<15 || steer*turn>0 && toInnerEdge<-W*1.5) brake = 1;
						} else if (speed>targetSpeed+10){
							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
							if ((highest==null || highest.y>30) && (distToEstCircle>0 || distToEstCircle>-GAP && distToEstCircle>lastDistToEstCircle)) brake = 0;
						} else if (relativeAngleMovement>-0.001 && absSpeedY<HIGH_SPEEDY) brake = 0;
						
						if (relativeTargetAngle>0 && relativeSpeedY>=0 && mustTurn && !canGoToLastSeg && !canGoAtCurrentSpeed && absSpeedY<MODERATE_SPEEDY){
//							if (speed>targetSpeed+MODERATE_GAP && absSpeedY>LOW_SPEEDY) steer = -turn;
//							if (relativeAngleMovement<0) brake *= 0.5;
						}
						//						if (relativeAngle<0) brake *= 0.5;

						//						if (mLen<TURNDIST)

					} else if (canGoAtCurrentSpeed && canGoToLastSeg){
						//						steer = -turn;
						if (speed<targetSpeed+10) 
							brake = 0;								
						else if (absSpeedY>MODERATE_SPEEDY) 
							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						else brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						//						if (speed>targetSpeed+Math.max(FAST_MARGIN,mLen)) 
						//							brake = 1;
						//						else if (speed>targetSpeed+Math.max(10,mLen) && mLen>FAST_MARGIN) 
						//							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);

					} else {
						//						if (absSpeedY<MODERATE_SPEEDY && relativeTargetAngle>0 && mustTurn ) steer = -turn;
						if (speed<targetSpeed+10) 
							brake = 0;								
						else if (absSpeedY>MODERATE_SPEEDY) 
							brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						else brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
					}
					//					if (brake==1 && relativeTargetAngle>=0 && !canGoToLastSeg && !isSafeToAccel){
					//						steer = -turn;
					//					}



					//				if (speedX>=lowestSpeed && relativeAngle<-0.001 && relativeTargetAngle>1.5*maxAngle && absSpeedY<HIGH_SPEEDY) 
					//					steer = - turn;
					//				else if (absSpeedY>=HIGH_SPEEDY && relativeSteer>=0) steer = 0;					
					//							
					//	
					//				if (relativeSpeedY>-LOW_SPEEDY && absSpeedY>=MODERATE_SPEEDY && (relativePosMovement>-0.001 || relativeAngle>=-0.001)) 
					//					steer = (absSpeedY<HIGH_SPEEDY) ? (relativeAngle<0 || relativePosMovement<0) ? - turn : 0 : (relativeSteer>0) ? -steer : steer;
					//				else if (relativeSpeedY<-LOW_SPEEDY  && relativeSteer>0 && canGoAtCurrentSpeed) 
					//					steer = -turn*Math.min(s,Math.abs(steer));

					//					if (brake==1) steer = -turn;
//					if (F_LFT<=0 || F_RGT<=0) brake = 0;					
				} else if (relativeTargetAngle>0){					
					if (debug) System.out.println("------------------------------TURN ----------------------------");

					//					if (absSpeedY>=VERY_HIGH_SPEEDY){
					//						acc = 1;
					//						steer *= 0.5;
					//						return acc;
					//					}

					/*if (relativeAngleMovement<0 && relativeTargetAngle>0 && absSpeedY<absLastSpeedY-5){
						if (absSpeedY>=MODERATE_SPEEDY)
							steer = (speedY>0) ? -1 : 1;
						else steer = 0;
						if (speed<targetSpeed+5){
							if (toOutterEdge>W) 
								acc = 1;
							else acc = CONSTANT_SPEED_ACC; 
						} else acc = CONSTANT_SPEED_ACC;
						return acc;
					}//*/ 

					double dist = Math.sqrt(mLastX*mLastX+mLastY*mLastY)*0.5;				

//					if (relativeSpeedY<0 && mustTurn && (absSpeedY>=MODERATE_SPEEDY || absSpeedY>absLastSpeedY))
//						steer *= 0.5;


					//					if (!mustTurn && canGoAtCurrentSpeed && canGoToLastSeg && relativeSpeedY>=0 && relativeAngle>=0 && relativeTargetAngle>0 && relativeAngleMovement>=0) steer = 0;					
//					brake = ((absSpeedY>HIGH_SPEEDY || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<0 && relativePosMovement<0 && relativeTargetAngle>0) && speed>targetSpeed+FAST_GAP || mLastY>0 && speed>=targetSpeed+FAST_GAP && !mustTurn || speed>=targetSpeed+FAST_GAP && relativeSpeedY<0 && !canGoToLastSeg) ? relativeSpeedY<0 ? brake : relativeAngle<0 ? Math.min(brake,0.1) : 1 :
//						(mLastY>0 &&speed>=targetSpeed+Math.max(mLen*1.5,20) || toOutterEdge<SAFE_EDGE_MARGIN && relativePosMovement<0) ? brake :
//						(mLastY>0 && speed>=targetSpeed+Math.max(mLen*0.5,10) || toOutterEdge<SAFE_EDGE_MARGIN && relativePosMovement<0) ? Math.min(brake,0.1) : 0;
					
					if ((absSpeedY>HIGH_SPEEDY || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<0 && relativePosMovement<0 && relativeTargetAngle>0 || relativePosMovement<-0.001) && speed>targetSpeed+FAST_GAP || mLastY>0 && speed>=targetSpeed+FAST_GAP || speed>=targetSpeed+FAST_GAP && relativeSpeedY<0 && !canGoToLastSeg){ 						
//						if (!isSafeToAccel && relativeAngle<-0.001 && relativeTargetAngle>=0) {
//							steer = (dist<15 || a>TURNANGLE*2) ? -turn : (dist>15) ? 0 : steer;
//							if (steer==0) 
//								brake = 1;
//							else brake *= 0.5;
//						} else if (isSafeToAccel || speed>targetSpeed+FAST_GAP && relativePosMovement>0 && relativeAngle>0 && relativeTargetAngle>0 && (mLastY==0 && a<TURNANGLE || mLastY>25 || a<TURNANGLE*2)){
//							brake = 1;
//							steer = -turn;
//						} else 
						
						brake = (speed>targetSpeed) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : 0;
//						if (speedX>targetSpeed+30)
//							brake = 1;
//						else 
						if (speedX>targetSpeed+25){
							brake = BRAKE_HARD(a, b, m, lastSpeed, absSpeedY, targetSpeed);
//							if ((targetSpeed<110 || distToEstCircle>0 || distToEstCircle>-GAP && relativeAngleMovement>0.001) && relativePosMovement>0 && absSpeedY<MODERATE_SPEEDY && distToEstCircle>-GAP && relativeAngleMovement<-0.01)
//								brake = 1;
						} else brake = (relativeAngleMovement>0.001 && relativeTargetAngle>0 && (canGoToLastSeg || m>20)) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if ((isSafeToAccel || steer*turn>0) && toInnerEdge<-W*1.5 || (absSpeedY<=MODERATE_SPEEDY || relativePosMovement>0) && canGoAtCurrentSpeed && canGoToLastSeg || absSpeedY<absLastSpeedY && relativeAngleMovement<0 && !mustTurn) 
							brake = (isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY || relativeAngleMovement<-0.01 || distToEstCircle<0 && relativeAngleMovement<-0.001) ? brake : 1;
						else if (!isSafeToAccel && relativeAngleMovement>0.001 && relativeTargetAngle>0 && a<TURNANGLE/1.5 && m>20 && absSpeedY<=MODERATE_SPEEDY && (distToEstCircle>0 || distToEstCircle>-GAP && distToEstCircle>lastDistToEstCircle)){
							brake = 1;
//							steer = -turn;
						}
					} else if (speed>targetSpeed+MODERATE_GAP){ 
						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if (speedX>targetSpeed+25){
//							brake = relativePosMovement<-0.001 && relativeAngleMovement<-0.01 ? brake*0.5 : steer*turn>=0 ? 1 : relativePosMovement<-0.001 || distToEstCircle<0 ? relativeAngleMovement<-0.001 || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle ? brake*0.5 : (relativeAngleMovement>0.01) ? 1 : brake : (absSpeedY>=MODERATE_SPEEDY && m<=20 && distToEstCircle<0 || distToEstCircle<0) ? brake : 1;
							brake = BRAKE_HARD(a, b, m, lastSpeed, absSpeedY, targetSpeed);
//							if ((targetSpeed<110 || distToEstCircle>0 || distToEstCircle>-GAP && relativeAngleMovement>0.001) && relativePosMovement>0 && absSpeedY<MODERATE_SPEEDY && distToEstCircle>-GAP && relativeAngleMovement<-0.01)
//								brake = 1;
						
						} else if (isSafeToAccel && steer*turn<0 && relativePosMovement>0 && (distToEstCircle<-W*1.5 || distToEstCircle>-GAP && distToEstCircle>lastDistToEstCircle || distToEstCircle>0 && a>0))
							brake = (distToEstCircle<-W || a>TURNANGLE*0.75 && b>TURNANGLE && distToEstCircle<lastDistToEstCircle && distToEstCircle<-GAP || absSpeedY>=MODERATE_SPEEDY) ? brake 
									: (m<25 && speed<targetSpeed+20  || relativeAngleMovement<-0.01 || distToEstCircle<0 && relativeAngleMovement<-0.001) ? brake*0.5 : 1;
						else if ((isSafeToAccel || steer*turn>0) && toInnerEdge<-W*1.5){ 
							brake = absSpeedY<MODERATE_SPEEDY && (distToEstCircle>-GAP*0.5 && relativeAngleMovement>0.01 || distToEstCircle>0) ? 1 : (a>TURNANGLE*0.5 && distToEstCircle<lastDistToEstCircle && isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY || distToEstCircle<-GAP) ? brake : 1;
//							steer = (steer*turn<0) ? -turn : (steer==0) ? 0 : turn;
						} else if (!canGoToLastSeg && relativePosMovement>=0 && !canGoAtCurrentSpeed) {
							brake = (relativeAngleMovement<0) ? brake*0.5 : brake;							
						} else if (relativePosMovement<0) 
							brake = (absSpeedY>=HIGH_SPEEDY || speedX<highestSpeed+Math.max(m,FAST_MARGIN) && (absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY || canGoToLastSeg || canGoAtCurrentSpeed || absSpeedY<absLastSpeedY-1.5 && relativeAngleMovement<0 || relativeAngleMovement<-0.01)) 
							? isSafeToAccel || relativePosMovement<-0.02 && relativeAngleMovement<-0.001 || absSpeedY>HIGH_SPEEDY && (relativeAngleMovement>0.01 || relativeAngleMovement>0 && relativeAngleMovement>lastRelativeAngleMovement) ? brake : 0 
							: brake;
						else brake = (steer*turn>0 && relativeTargetAngle>-0.001) ? brake :(relativeAngleMovement>0) ? brake : brake*0.5;
					} else if (speed>targetSpeed+Math.max(10,mLen*0.5)){ 
						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if (speed>=targetSpeed+15 && ((isSafeToAccel || steer*turn>0) && toInnerEdge<-W*1.5)) 
							brake = (isSafeToAccel && steer*turn<0 || absSpeedY>=MODERATE_SPEEDY || distToEstCircle<-W) ? brake : 1;
						else brake = (relativePosMovement>0) 
								? (distToEstCircle<-W*1.2 && !isSafeToAccel) 
										? brake 
										: relativeAngleMovement>-0.01 && (seenNewSeg && (absSpeedY<MODERATE_SPEEDY || absSpeedY<absLastSpeedY) || speedX<targetSpeed+15 && distToEstCircle>lastDistToEstCircle && absSpeedY<MODERATE_SPEEDY) 
											? (seenNewSeg && highest!=null && highest.y<50 && distToEstCircle<-GAP*0.5 && distToEstCircle<lastDistToEstCircle && relativePosMovement>-0.001) ? brake : 0 
											: brake 
								: steer*turn<0 && targetSpeed>100 && absSpeedY<HIGH_SPEEDY || (relativeAngleMovement<-0.01 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY ||  absSpeedY>20)
									? speedX<highestSpeed+Math.max(m,FAST_MARGIN) && distToEstCircle>-tW ? 0 : (speed*speed-targetSpeed*targetSpeed)/(speed*speed)
									: (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
//						if (relativePosMovement<0) brake*=0.5;
//						if (!isSafeToAccel && absSpeedY>=MODERATE_SPEEDY) brake = Math.max(brake,0.15);							
					} else if (speed>=targetSpeed+15){
						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if ((isSafeToAccel || steer*turn>0) && toInnerEdge<-W*1.5 && !mustTurn) 
							brake = (isSafeToAccel && steer*turn<0) 
								? (relativePosMovement>-0.001) 
										? 1 
										: (relativeAngleMovement<-0.01 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY ||  absSpeedY>20) ? 0 : brake 
							: 1;
						else if (relativePosMovement<0 && steer*turn<0 && !isSafeToAccel && absSpeedY<HIGH_SPEEDY) brake = 0;
					} else if (isSafeToAccel || speed>targetSpeed+10){ 
						brake = speed>=targetSpeed+10 && relativePosMovement<-0.001 && (relativeAngleMovement<-0.01 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY ||  absSpeedY>20) 
								? distToEstCircle>-tW || absSpeedY>20 ? 0 : brake
								: speed>=targetSpeed+10 && (absSpeedY>MODERATE_SPEEDY || isSafeToAccel && relativeAngle<0 || isSafeToAccel && distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle || relativePosMovement>-0.001 && (distToEstCircle>-(W*1.5) || relativeAngle<0)) 
								? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : 0;							
					} else if (trSz>1 && targetSpeed>highestSpeed+m*2 && relativePosMovement<-0.001){
//						targetSpeed = (toOutterEdge<Math.max(tW, SAFE_EDGE_MARGIN)) ? highestSpeed : Math.min(highestSpeed+Math.min(m*2,FAST_MARGIN),targetSpeed);
						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if (brake<0.1) brake = 0;
					} else if (relativeAngleMovement<-0.001 && relativePosMovement>-0.001 && distToEstCircle<0) {
						brake = Math.max((speed*speed-targetSpeed*targetSpeed)/(speed*speed),0.1);						
					} else if (relativeAngleMovement>-0.01 && absSpeedY<HIGH_SPEEDY) brake = 0;


					//					if (brake<1 && relativePosMovement<0 && relativeTargetAngle>0) brake = 1;

					//					if (!isSafeToAccel && relativeAngle>=0 && relativeTargetAngle>=0 && speed>lastSpeed+40 && curPoint.length()>W*10) {
					//						if (speed-lastSpeed<(curPoint.length()-W*10)*3)
					//							brake = 1;
					//					}

					if (speedX<lowestSpeed+tW &&  (toOutterEdge<SAFE_EDGE_MARGIN && !maxTurn || absSpeedY>=MODERATE_SPEEDY) && !canGoAtCurrentSpeed && !canGoToLastSeg  && (absSpeedY>MODERATE_SPEEDY || relativeAngle<-0.1)){
						gear = lastGear;
//						if((relativeAngle<0 || absSpeedY>MODERATE_SPEEDY)){
//							//						acc = (absSpeedY>HIGH_SPEEDY) ? 1 : CONSTANT_SPEED_ACC;						
//							brake = (absSpeedY>HIGH_SPEEDY || relativeAngle>0 ) ? 0.05 : Math.max(brake,0.1);												
//						}
						if (relativeAngleMovement<0.01) steer = -turn;
//						acc = (absSpeedY>HIGH_SPEEDY || absSpeedY>MODERATE_SPEEDY && absSpeedY<absLastSpeedY && relativeAngleMovement<0 && relativeTargetAngle>0 && relativeAngle>0) ? CONSTANT_SPEED_ACC : (relativeAngle<0 || absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY || toOutterEdge<GAP) ? 0 :  Math.max(acc, CONSTANT_SPEED_ACC);							
//						if (acc>0) {
//							brake = 0;
//						} else if (brake==0) brake = 0.05;
						brake = 0.0;
						if (relativeAngleMovement>0 && max){
							brake = 0;
//							acc = (relativeAngle>0 && absSpeedY<MODERATE_SPEEDY) ? 1 : CONSTANT_SPEED_ACC*0.5;
						}
						/*if (absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY && relativeSpeedY>=0){
							acc = 1;
							brake = 0;
							steer = 0;
						}//*/
						if (acc==0 && brake==0) acc = CONSTANT_SPEED_ACC*0.25;
						return acc;
					}


					if (speed-lowestSpeed>FAST_MARGIN){						
						if (!canGoToLastSeg){
							if (mustTurn && speedX-targetSpeed<Math.min(dist,tW) && absSpeedY<MODERATE_SPEEDY){
								brake = 0;
								//								brake = Math.min(brake,0.2);
								//								if (brake>0.5) brake *=0.5;
								//								if (brake<0.1) brake = 0;
							}


							//							if (steer*turn<0) 
							//								steer = (absSpeedY<=MODERATE_SPEEDY && relativeTargetAngle>0 && (relativeAngle<0 || relativePosMovement<0 || speed>targetSpeed+FAST_MARGIN) && (trSz>1 && mLen<TURNDIST)) ? -turn : (relativeSpeedY<0) ? steer : (absSpeedY<=HIGH_SPEEDY) ? steer : steer;
							//							if (brake==1 && relativeTarregetAngle>0 && !isSafeToAccel) 
							//								steer = -turn;
							if (!canGoToLastSeg && mustTurn && relativeTargetAngle>0){
								if (!canGoAtCurrentSpeed && relativeTargetAngle>=0) {
									if (!isOffBalance && relativeSpeedY>=0 && (inTurn || a>=TURNANGLE*1.5 || mLen<20) && !isSafeToAccel && toInnerEdge<-GAP && (mustTurn || mLen<25) ) 
										steer = (relativePosMovement<-0.001 || relativePosMovement<0.01 && relativeAngleMovement<-0.001) ? -turn : steer;
//									if (relativePosMovement<0 || absSpeedY>MODERATE_SPEEDY) brake = Math.min(brake, 0.1);
//									if (relativePosMovement>-0.001 && mLastY>15 && !isSafeToAccel && speed>targetSpeed+20 && a<TURNANGLE && relativeAngleMovement>0.001 && (distToEstCircle>-GAP || distToEstCircle>-W && distToEstCircle>lastDistToEstCircle)){
//										brake = 1;
//										steer = -turn;
//									} else if (relativeSpeedY>=0 && inTurn && !isSafeToAccel && relativePosMovement>=0 && (relativeAngle<0 || relativeAngleMovement<0))										
//										brake *= 1;
								}
								//								brake = Math.max(brake, 0.1);
								//								if (brake>=0.4) brake *=0.5;
								//								if ((relativePosMovement>=0 || relativeAngle<0)){
								//									if (relativeTargetAngle>0.2) 
								//										brake = Math.min(brake,0.1);
								//									else if (relativeAngleMovement>0 && speed>targetSpeed+Math.max(15,mLen)) {
								//										brake =  Math.min(brake,0.4);										
								//									}
								//								}
//								if (mLen<25 && (relativeAngleMovement<0 || relativePosMovement<0 || relativeAngle<-0.001) && steer*turn<=0 && !isSafeToAccel) {
//									steer = (!isOffBalance && relativeSpeedY>0) ? -turn : steer;
////									if (brake==0 && !canGoToLastSeg && mLastY>0 &&  a>TURNANGLE && relativeAngleMovement<-0.001) brake = 0.1;
//								}
							}
						} else {
							//							if (brake!=1 && relativeSpeedY>0 && relativeTargetAngle>0 && absSpeedY<=MODERATE_SPEEDY) steer = -turn;
							if (mustTurn && speed-targetSpeed<Math.min(dist,tW) && relativePosMovement>=0)
								brake = (absSpeedY<MODERATE_SPEEDY) ? 0 : brake;
							else if (absSpeedY<absLastSpeedY-1.5 && relativeAngleMovement<0)
								brake *= 1;
							else if (relativePosMovement>=0 && steer*turn<0 && (relativeAngle<0 || relativeAngleMovement<0)) brake *= 0.5;

//							if ((F_LFT<=0 || F_RGT<=0)) {
//								brake = 0;
//								steer = -turn;
//							}
						}

//						if (absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY && relativeAngle>0){
//							if (!mustTurn) brake = 1;
//						}

						if (brake>1) brake = 1;
						//						if (brake==1 && relativeTargetAngle>=0 && mustTurn){
						//							steer = -turn;
						//						}

//						if (F_LFT<=0 || F_RGT<=0) brake = 0;
						
						/*if (absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY && relativeSpeedY>=0){
							acc = 1;
							brake = 0;
							steer = 0;
						}//*/

						
					} else {

						//					brake = Math.min(brake,0.1);
						//					brake *= 0.5;
						if (mustTurn && speedX-targetSpeed<Math.min(dist,tW) && absSpeedY<MODERATE_SPEEDY)
							brake = 0;
						if (relativePosMovement>=-0.0001 ||  relativeAngleMovement>=-0.0001 ||  relativeTargetAngleMovement>=-0.0001){ //				
							if (relativeSpeedY>-LOW_SPEEDY && absSpeedY<MODERATE_SPEEDY && relativePosMovement<-0.001 && relativeTargetAngle>0 && (!canGoToLastSeg && !canGoAtCurrentSpeed || maxTurn)){ 
//								steer = (steer<0) ? -1 : (steer>0) ? 1 : 0;
								steer *= 1.5;
							} else if (relativeSpeedY>-LOW_SPEEDY && absSpeedY>=MODERATE_SPEEDY && (relativePosMovement>-0.001 || relativeAngle>=-0.001)){ 
								//							steer = (absSpeedY<HIGH_SPEEDY) ? -turn*Math.min(s,Math.abs(steer)) : (relativeSteer>0) ? turn*Math.min(s,Math.abs(steer)) : steer;
								if (relativeAngleMovement<0) 
									steer *= 1.5;
								else if (relativeAngle>=0.1) {
									if (absSpeedY<MODERATE_SPEEDY && absSpeedY>absLastSpeedY) steer *= 0.5;
//									if (brake==0) acc = 1;
								}
								//							if (relativeAngle>0) acc = CONSTANT_SPEED_ACC;
							} else if (relativeSpeedY<-LOW_SPEEDY && canGoAtCurrentSpeed) 
								steer *= 0.3;  
						}
						
						/*if (absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY && relativeSpeedY>=0){
							acc = 1;
							brake = 0;
							steer = 0;
						}//*/
	
	
						//					if (absSpeedY<=HIGH_SPEEDY && speed<targetSpeed+10 && (relativePosMovement>=-0.0001 || relativeSteer>s || relativeAngleMovement>=-0.001 || relativeAngle<=-0.001 || absSpeedY>absLastSpeedY)) brake = 0;				
						if (brake>0) acc = 0;
	//					if (speed<lowestSpeed+tW && brake>0 && relativeSteer>s) brake = 0;
						if (!tooFast && speed<lowestSpeed+tW && (relativePosMovement>=-0.001 || relativeAngleMovement>=-0.001)){ 					
							brake = 0;					
						}
						//					if (brake==1 && relativeTargetAngle>=0 && !isSafeToAccel){
						//						steer = -turn;
						//					}
					}
				} else {
					if (debug) System.out.println("------------------------------DURING DRIFT ----------------------------");

					if (!isSafeToAccel && mustTurn && relativeSpeedY<0 && absSpeedY>=MODERATE_SPEEDY && toInnerEdge<-W*2)
						steer = -turn;
					else if (!isSafeToAccel && inTurn && toInnerEdge<-W && mLastY>0 && mLen<5 && relativeAngle<0.1 && (relativeAngleMovement<0 || relativePosMovement<0 || relativeAngleMovement>-0.01 && speed>=targetSpeed))
						steer = 0;

					//IF pANGLE TO TARGET EXCEEDS THRESHOLD AND STILL TURNING, RECOVER
//					double TURNDIST = (speed>targetSpeed+FAST_MARGIN) ? 25 : (speed>targetSpeed+20) ? 20 :15;
//					brake = ((absSpeedY>HIGH_SPEEDY || isSafeToAccel) && speed>targetSpeed+FAST_MARGIN || mLastY>0 && speed>=targetSpeed+Math.min(mLen,15) && mLen>TURNDIST) ?  1 : 
//						(mLastY>0 && speed>=targetSpeed+Math.min(mLen,5)) ? brake : 0;
					brake = (speed>targetSpeed) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed) : 0;
					if ((absSpeedY>HIGH_SPEEDY || absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement<0 && relativePosMovement<0 && relativeTargetAngle>0) && speed>targetSpeed+FAST_GAP || mLastY>0 && speed>=targetSpeed+FAST_GAP && !mustTurn || speed>=targetSpeed+FAST_GAP && relativeSpeedY<0 && !canGoToLastSeg){ 
						if (isSafeToAccel && toInnerEdge<-W*1.5 || (absSpeedY<=MODERATE_SPEEDY || relativePosMovement>0) && canGoAtCurrentSpeed && canGoToLastSeg) 
							brake = (a<0 || a>0 && distToEstCircle>-GAP && (relativeAngleMovement>0.01 || distToEstCircle>0 && relativeAngleMovement>-0.01 ||  m>15 && distToEstCircle>GAP*0.5 && speedX>targetSpeed+FAST_MARGIN)) 
								? 1
								: brake;						
					} else if (speed>targetSpeed+MODERATE_GAP){ 
						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
						if (mustTurn && relativePosMovement>=0 && !canGoToLastSeg) {
							brake = (speed<targetSpeed+15) ? 0 : brake;
						} else if (relativePosMovement<0 && !canGoToLastSeg) 
							brake *= (relativePosMovement<-0.01 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY) ? 0 : 0.5;
						else if (canGoAtCurrentSpeed && relativePosMovement>0 && (!isSafeToAccel && relativeTargetAngle>0 || isSafeToAccel && toInnerEdge<-W*1.5 || steer==0 && absSpeedY<absSpeedY && absSpeedY<MODERATE_SPEEDY)){
							brake = (absSpeedY<MODERATE_SPEEDY || speed>targetSpeed+25) ? 1 : brake;
//							steer = 0;
						}
					} else if (speed>targetSpeed+Math.max(10,mLen*0.5)){ 
						brake = speed>=targetSpeed+10 && relativePosMovement<-0.001 || targetSpeed<=100 || absSpeedY>=MODERATE_SPEEDY && speed>targetSpeed+5 && relativePosMovement<-0.001 ? (speed*speed-targetSpeed*targetSpeed)*0.5/(speed*speed) : 0;
//						if (absSpeedY>MODERATE_SPEEDY) 
//							brake *= (m<20) ? 1.5 : 0.75;
//						if (!isSafeToAccel && speedX>=targetSpeed+15) brake = Math.min(brake,0.1);	
					} else if (speed>=targetSpeed+15){
						if (speed>=targetSpeed+15 && isSafeToAccel && toInnerEdge<-W*1.5)
							brake = 1;
						else brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
					} else if (speed<targetSpeed+10) brake = 0;
				
					
//					if (mLen<TURNDIST && brake>0 && !isSafeToAccel) {
//						brake = Math.min(brake,0.4);
//					}
					if (isSafeToAccel && toInnerEdge<-W*2 && speed>=targetSpeed+FAST_GAP && (steer*turn>0 || relativeAngleMovement>-0.005)) brake = 1;
//					if (relativeAngle>0 && toInnerEdge>-W && relativePosMovement>=0) brake = 0;					
//					steer = (relativeAngleMovement>=-0.0001 && (absSpeedY>=VERY_HIGH_SPEEDY || absSpeedY>=HIGH_SPEEDY && absAngle>0.5)) ? -sign : (relativeCurPos>0.4 || absSpeedY>=VERY_HIGH_SPEEDY && steer*sign<0) ? steer :  steer;
					/*if (absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY && relativeSpeedY>=0){
						acc = 1;
						brake = 0;
//						steer = 0;
					}//*/
					/*if (relativeSpeedY>=0 && !isSafeToAccel){
						if (relativeSpeedY<=MODERATE_SPEEDY){
							if (relativeAngleMovement<0 && absSpeedY<absLastSpeedY)
								steer = -turn;							
						}
					}//*/
					//					if (speed>targetSpeed+Math.max(mLen,20) && mLen>10) 
					//						brake = 1;
					//					else if (speed>targetSpeed+5 && mLen>5) 
					//						brake = (speed*speed-targetSpeed*targetSpeed)/(speed*speed);
					//					else brake = 0;

				}
			}		
			//		} else if (relativeSteer<-0.001 && absSpeedY>absLastSpeedY && absSpeedY>MODERATE_SPEEDY && lastSteer*sign<0) {
			//			steer = -sign;			
		} else if (brake>0 || acc==0){			
			gear =  Math.min(getGear(speed),lastGear);
			if ((tooFast || speed>lowestSpeed) && !canGoAtCurrentSpeed && toInnerEdge<-W && Math.abs(steer)<s) steer = (steer<0) ? -1 : 1;		
		} 
//		if (relativeAngle>0.1 && speed<lowestSpeed && absSpeedY>=MODERATE_SPEEDY){
//			acc = INCREASE_ONE;
//			steer = 0;
//		}
		
		/*if (brake>0 && distToEstCircle<-W && lastDistToEstCircle<0) {
			if ( steer*turn<0) {
					if (!isSafeToAccel) {
//						if (absSpeedY>MODERATE_SPEEDY && speedX>targetSpeed+15 && relativePosMovement>0)
//							brake = 1;
						brake = (lastDistToEstCircle>distToEstCircle && relativePosMovement>-0.001) ? 
								(distToEstCircle<-W*1.5 && (relativeAngle<0 || relativeAngleMovement<0)) ? brake*0.3 : brake
						: (relativePosMovement<-0.001 || relativeAngle<-0.001) ?  brake*0.3 : brake;
					} else {
						if (distToEstCircle<-W*1.5 && (relativeAngle<0 || relativeAngleMovement<0)) brake = brake*0.3; 
					}
			} else {
				brake = (lastDistToEstCircle>distToEstCircle) ? brake : Math.max(brake,lastBrkCmd)*1.5;
			}
		} else if (distToEstCircle>=0 && absSpeedY<MODERATE_SPEEDY && speedX>targetSpeed+15 && relativePosMovement>0)
			brake = 1;
//		else if (brake==0 && speedX>targetSpeed && distToEstCircle<0 && lastDistToEstCircle>distToEstCircle && relativePosMovement<0 && absSpeedY>20) {
//			brake = 0.1;
//			acc = 0;
//		}
		//*/		
		boolean drift = false;
		if (inTurn && trSz>1 && speedX>highestSpeed && relativePosMovement>=-0.001 &&canGoAtCurrentSpeed && first!=null && last!=null && first.type==last.type && speedX>=lastSpeed+FAST_MARGIN){
			Segment t = (first.type==TURNRIGHT) ? first.rightSeg : first.leftSeg;
			double dd = Geom.ptTangentDist(0, 0, t.center.x, t.center.y, t.radius);
			drift = dd<15;
		}
			
		
//		if (time>=60 && speedX>targetSpeed+35)
//			gear = 1;		
			if (relativeAngle<0.3 && (m<15 && relativePosMovement>=-0.001 &&  speedX>=lastSpeed+FAST_MARGIN || drift)){
//				if (time>=BREAK_TIME){
//					double d = highest.distance(first.center);
//					if (debug) System.out.print);
//				}
				steer = distToEstCircle>0 && (relativeTargetAngle<-0.001 && relativePosMovement>0.005 ||  relativePosMovement>0.01) 
					? (relativeAngleMovement>0) 
							? (a>0.05)  
									? steer*turn<0 ? steer*0.5 : 0 
									: minAbs(steer,bal) 
							: (relativeAngleMovement<-0.01 && a>0.05 || distToEstCircle<GAP*0.5 && distToEstCircle<lastDistToEstCircle) 
								? (relativePosMovement>0.01) ? steer*turn<0 || a<0 ? steer : -turn : -turn 
									: steer*turn<0 ? (relativeAngleMovement<-0.001) ? steer 
											: bal*0.5 
											: (relativeAngleMovement<-0.001) 
												? a<0 ? steer : minAbs(bal,steer)*0.5 
												: a<0 ? maxAbs(bal,steer) :  minAbs(bal,steer)  
					: distToEstCircle<0 || relativePosMovement<0.01 && (relativeAngleMovement<0.001 || absSpeedY>MODERATE_SPEEDY) 
						? (relativeAngleMovement<-0.01 || distToEstCircle<0 || relativePosMovement<0.001
								|| relativeAngleMovement<-0.001 && relativePosMovement<lastRelativePosMovement && relativePosMovement<0.005) 
									? -turn 
									: (relativeAngleMovement<-0.001) ? steer :steer*0.5 
						: (relativeAngleMovement<-0.01 || relativePosMovement<0.001
								|| relativePosMovement<lastRelativePosMovement && relativePosMovement<0.005) 
								? -turn 
								: (relativeAngleMovement<-0.01 && steer*turn>0) ? 0 : steer;
				
				//if (relativeAngleMovement<0.001 && steer==-turn) acc = 0;
			} else if (relativePosMovement>0 && relativeTargetAngle>0 && relativeAngle>0 && relativeAngleMovement>0 && distToEstCircle>-GAP && lastDistToEstCircle<distToEstCircle) {
				steer = !possibleChangeDirection && canGoToLastSeg && absSpeedY<10 && m>10 || relativeAngleMovement>0.02 
						|| relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement && distToEstCircle>-W? 0 : steer;
				if (steer!=0 && hazard==0 && (m>10 || toInnerEdge<-W || relativeAngleMovement<0.001 || distToEstCircle<0)) smoothSteering();				
				
				
				if (steer*turn<0 && (relativeAngleMovement>0.01 && distToEstCircle>-GAP || relativeAngleMovement>0.001 && speedX<lastSpeed+m || relativeAngleMovement>0.001 && distToEstCircle>0)) 
					steer = ((brake>0 || b>TURNANGLE && m<20) && distToEstCircle<0) ? steer : 0;				
			} else if (steer*lastSteer>=0 && hazard==0){
				boolean noNeedSmoothSteering = false;
				noNeedSmoothSteering = (speedX<lastSpeed && distToEstCircle>-tW && relativePosMovement>-0.01 && relativeAngleMovement>-0.01  && Math.abs(lastSteer)>Math.abs(steer))
						|| (relativePosMovement>-0.001 && distToEstCircle>0 && relativeAngleMovement<0.01 && relativeAngleMovement>-0.01 && Math.abs(lastSteer)>Math.abs(steer));				
				if (!noNeedSmoothSteering) smoothSteering();
			} else if (steer*lastSteer<0 && Math.abs(lastSteer)>0.001) steer =0;
			
			if (relativeAngle<0.3 && speedX>lowestSpeed && steer*turn<0 && a>0 && relativePosMovement>0 && relativePosMovement<0.005 && absSpeedY>MODERATE_SPEEDY && distToEstCircle<lastDistToEstCircle && relativeAngleMovement<lastRelativeAngleMovement){
				steer = (relativeAngleMovement<-0.001) ? -turn : steer;
//			} else if (steer*turn<=0 && relativePosMovement>=0 && distToEstCircle<0 && brake>0 && relativeSpeedY>0 && (distToEstCircle<-GAP || distToEstCircle<lastDistToEstCircle || relativeAngleMovement<-0.001)){
//				steer = -turn;
			} else if (relativeAngle<0.3 && relativePosMovement<0.001 && speedX>lastSpeed+20 && absSpeedY>MODERATE_SPEEDY && distToEstCircle<GAP*0.5 && distToEstCircle<lastDistToEstCircle && (relativeAngleMovement<lastRelativeAngleMovement || relativeAngleMovement<0.001)){
				steer = -turn;
				if (acc>CONSTANT_SPEED_ACC && relativeAngleMovement<-0.001) acc = CONSTANT_SPEED_ACC*0.25;
			} else if (m<5 && steer*turn>0 && relativeAngleMovement<-0.001 && relativeTargetAngle<0 && a>0.05)
				steer = 0;
			
			
			if ((isSafeToAccel || m>20) && (brake==1 || speedX>targetSpeed+40) && steer*turn<=0 && distToEstCircle<lastDistToEstCircle && relativeAngleMovement<0.01 && (relativeAngleMovement<0.001 || relativeAngleMovement<lastRelativeAngleMovement)){
				if (a>0 && !canGoVeryFast) 
					steer=-turn;
				
				if (relativeAngleMovement<0.001 && absSpeedY>10)
					brake = (distToEstCircle<0 && relativeAngleMovement>-0.01 && speedX<targetSpeed+25 && absSpeedY<HIGH_SPEEDY) ? (speed*speed-targetSpeed*targetSpeed)/(speed*speed)*0.5 : brake;
			} 
//			else if (relativePosMovement>-0.001 && relativeAngleMovement>0.01 && steer*turn<0 && (distToEstCircle>0 || drift))
//				steer = 0;
			else if (isSafeToAccel && !canGoVeryFast && distToEstCircle>0 && distToEstCircle<W*1.5 && b>TURNANGLE*0.75 && (speedX>targetSpeed+25 && m>30 || speedX>targetSpeed+5 && m>40 || speedX>targetSpeed && m>45 || brake==1 && m>15) && (relativeAngleMovement<0.01 || distToEstCircle<lastDistToEstCircle)){
				steer = -turn;			
			} else if (isSafeToAccel && distToEstCircle>0 && b>TURNANGLE*0.5 && speedX>targetSpeed+25 && m<30 && steer*turn>0 && relativeAngleMovement<0.01)
				steer = (relativeAngleMovement<-0.01) 
							? (distToEstCircle<W*1.5) ? -turn : 0
							: (lastRelativeAngleMovement>0.01 || lastRelativeAngleMovement-relativeAngleMovement>0.01 || brake==1 && distToEstCircle<lastDistToEstCircle) ? 0 : steer;
			
//			if (isSafeToAccel && distToEstCircle<W && distToEstCircle<lastDistToEstCircle && (speedX>targetSpeed+25 && m>30 || speedX>targetSpeed+5 && m>40 || speedX>targetSpeed && m>45) && steer*turn>=0 && relativeSpeedY<LOW_SPEEDY){
//				brake = 1;
//			} else
//			if (isSafeToAccel && speedX>targetSpeed+40 && distToEstCircle>W && distToEstCircle>lastDistToEstCircle){
//				brake = 1;
////				steer = (relativeAngleMovement>0.03) ? 1 : (relativeAngleMovement>0.02) ? bal : 0;
//				steer = -turn;
//			}
			else if (isSafeToAccel && distToEstCircle<W && !canGoVeryFast && (speedX>targetSpeed+25 && m>30 || speedX>targetSpeed+5 && m>40 || speedX>targetSpeed && m>45) && steer*turn<=0 && relativeSpeedY<LOW_SPEEDY){
				steer = (relativeAngleMovement<0.01 && lastSpeed>80) ? -turn : steer;
				if (distToEstCircle<0 && steer*turn<0){
					brake *= (speedX>targetSpeed+30 && relativeAngleMovement<-0.001) ? 0.15 : 0.5;
				} else if (speedX<targetSpeed+25){
					brake *= 0.5;
				} else brake = (speedX>targetSpeed+30) ? Math.max(0.25,brake) : Math.max(0.25,brake);
			} else if ((isSafeToAccel || m>15 && distToEstCircle<lastDistToEstCircle) && distToEstCircle>0 && distToEstCircle<W*1.5 && b>TURNANGLE*0.5 && (speedX>targetSpeed+25 || speedX>targetSpeed+20 && m<25) && absSpeedY>HIGH_SPEEDY){
				 steer = !canGoVeryFast && (relativeAngleMovement<-0.001 || relativeAngleMovement<0.005 && relativeAngleMovement<lastRelativeAngleMovement) ? -turn : 0;
				 if (relativeAngleMovement>-0.005 || relativeAngleMovement>-0.01 && lastRelativeAngleMovement<relativeAngleMovement) 
					 brake = 1;
				 else brake *=0.5;
			 } else if (isSafeToAccel && distToEstCircle>0 && (speedX>targetSpeed+25 && m>30 || speedX>targetSpeed+5 && m>40 || speedX>targetSpeed && m>45) && steer*turn>0 && relativeSpeedY>0){			
					brake = (speedX>targetSpeed+30 || steer*turn>0) ? 1 : Math.max(0.25,brake);
  			 } 
					
		
			
//			if (trSz>0 && turn!=UNKNOWN &&  speedX<lowestSpeed && steer*turn<0 && last.type!=turn) steer = 0;
//			if ((isSafeToAccel || m>15) && (speedX>targetSpeed+25 && m>30 || speedX>targetSpeed+5 && m>40 || speedX>targetSpeed && m>45) && steer*turn<=0){
//				steer = (relativeAngleMovement<0.01) ? -turn : forceSteer();
////				if (relativeAngleMovement<0.01 && distToEstCircle<0 || relativeAn) brake *= 0.25;
////				steer = -turn;
//				if (relativeAngleMovement<-0.01) brake = 1;
//			}
			
			if ((inTurn || absSpeedY<absLastSpeedY-1.5) && steer*turn<=0 && relativeAngleMovement<0 && speedY*lastSpeedY>0){
				if ((relativeSpeedY>0 && (absSpeedY<absLastSpeedY-1.5 || absSpeedY<absLastSpeedY-1.2 && relativePosMovement<0) || relativeSpeedY<0 && absSpeedY>absLastSpeedY+1.5) && relativeTargetAngle>=0) {
					steer = (absSpeedY>=10 && relativeTargetAngle>0 && (relativeSpeedY>0 || absSpeedY>HIGH_SPEEDY)) ? -turn : steer*1.5;
					if (Math.abs(steer)>=turn) hazard = 2;
//					hazard = 2;
//					steer = -turn;
					if (!canGoToLastSeg && !canGoAtCurrentSpeed && speedX<targetSpeed+15 && speedX>lowestSpeed && absSpeedY>MODERATE_SPEEDY && (relativeAngle<0 || relativePosMovement<0)) {
						acc = CONSTANT_SPEED_ACC*0.25;
//						acc = 0;
						brake = 0;
//						brake *= 0.5;
					}
				} 
			} else if (steer*turn>0 && (inTurn || absSpeedY>absLastSpeedY+1.5) && relativeAngleMovement>0 && speedY*lastSpeedY>0 && (relativeSpeedY<0 && absSpeedY<absLastSpeedY-1.5 || relativeSpeedY>0 && absSpeedY>absLastSpeedY+1.5) && absSpeedY>=MODERATE_SPEEDY && relativeTargetAngle<=0){
				steer = ( relativeTargetAngle<0 && relativePosMovement>0 &&(relativeSpeedY<0 || absSpeedY>HIGH_SPEEDY) ) ? steer*2 : steer;
				if (Math.abs(steer)>=turn) hazard = 2;
//				hazard = 2;
				if (absSpeedY>absLastSpeedY+1.5 || absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY){
					if (speedX<targetSpeed+15){
						acc *= CONSTANT_SPEED_ACC*0.25;
						brake = 0;
					}
				} else if (!canGoToLastSeg && !canGoAtCurrentSpeed && (relativeAngle<0 || absSpeedY>10 && relativePosMovement<0)) {
					acc *= CONSTANT_SPEED_ACC;
					brake = 0;
				}
			}  else if (inTurn && steer*turn>0 && relativeSpeedY<0 && relativeAngle<=0 && relativeAngleMovement<0 && absSpeedY>absLastSpeedY+1.5 && absSpeedY>HIGH_SPEEDY && relativeTargetAngle<0){
				hazard = 2;
				steer = 0;
			} else if (inTurn && steer*turn<0 && relativeSpeedY>0 && relativeAngle>=0 && relativeAngleMovement>0 && absSpeedY>absLastSpeedY+1.5 && absSpeedY>HIGH_SPEEDY && relativeTargetAngle>=0){
				hazard = 2;
				steer = 0;
			} else if (turn==2 && Math.abs(absSpeedY-absLastSpeedY)>1.5) {
				hazard = 2;
				steer = (relativeAngle*relativeAngleMovement<0) ? 0 : curAngle/steerLock;
			}
						
			if (canGoVeryFast && speedX-lastSpeed<Math.min(100, al*1.5)) {				
				if (a>0 && b>0 && (al>50 || distToEstCircle>-W && absSpeedY<MODERATE_SPEEDY || distToEstCircle>-GAP && absSpeedY<HIGH_SPEEDY || (distToEstCircle>-W || relativeAngleMovement>0.01) && b>TURNANGLE*0.5 && relativePosMovement>-0.001 && absSpeedY<HIGH_SPEEDY)) {
					acc = (distToEstCircle<0 && speedX>lastSpeed+al && relativePosMovement<-0.001) ? acc : (relativeAngleMovement>-0.001 || absSpeedY<MODERATE_SPEEDY && distToEstCircle>0) ? 1 : acc;
					brake = 0;
				} else if (a>0 && b>0 && absSpeedY<HIGH_SPEEDY && relativePosMovement>-0.001 && relativeAngleMovement>0.001) brake = 0;
				if (acc<=CONSTANT_SPEED_ACC*0.25 && brake==0 && relativeAngleMovement>0.001 && speedX<lowestSpeed) acc = CONSTANT_SPEED_ACC;
			} 
			/*else if (canGoModerate){
				if (a>0 && b>0){
					if (brake>0 && absSpeedY<HIGH_SPEEDY) 
						brake = 0;
					else acc = 0;
				}
			}//*/
			
//			if (relativePosMovement<-0.01 && relativeAngle>0 && absSpeedY>HIGH_SPEEDY && relativeAngleMovement>-0.01)
// 				 steer = (relativeAngleMovement<0.001) ? 0 : turn;
			
//			if (speedX<120 && steer*turn<0 && relativeTargetAngle>=0 && relativeAngleMovement>-0.01)
//				steer = minAbs(steer, -turn*relativeTargetAngle*0.5/steerLock);
			
//			if (relativeAngleMovement>0 && steer*turn<0 && relativePosMovement>0 && relativeAngle>0 && absSpeedY>=MODERATE_SPEEDY && absSpeedY>absLastSpeedY && acc ==0 && brake==0)
//				acc = CONSTANT_SPEED_ACC;
			
			if (acc==0 && brake==0  && steer*turn<0)
				acc = (relativePosMovement>0 || absSpeedY<absLastSpeedY) ? CONSTANT_SPEED_ACC*0.25 : CONSTANT_SPEED_ACC*0.25;
			else if (acc==0 && brake==0 && relativePosMovement<0 && steer*turn<0 && absSpeedY>10 && absSpeedY<absLastSpeedY && relativeAngleMovement<0)
				acc = CONSTANT_SPEED_ACC*0.25;
			
			if ((relativeAngle>0.3 && relativeAngleMovement>0 || relativeAngle>0.35 || relativeAngle>0.25 && relativeAngleMovement>0.01) && relativePosMovement<-0.001 || relativeAngle>0.5)
				steer = turn;
			else if (relativePosMovement<-0.01 && absSpeedY>HIGH_SPEEDY && absSpeedY>absLastSpeedY && relativeAngleMovement>0.001 && steer*turn<=0){
				steer = (relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement) 
						? turn
						: relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement 
							? bal 
							: steer;
			}
			
			
			if (steer*turn<0 && relativeSpeedY<0 && relativePosMovement>-0.001 && a>0){
				steer = distToEstCircle>-GAP && (relativeAngleMovement>0.01 || relativeAngleMovement>0.005) ? 0 : steer;
			}
			
			double signPos = (curPos<0) ? -1 :1;
			if (relativeAngleMovement>0.01 && (turn==0 || turn==Segment.UNKNOWN) && (steer*signPos<0 || steer==0 && relativeAngle>=0)){
				if (relativeAngle>0) 
					steer = -signPos;
				else steer = (relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>lastRelativeAngleMovement) ? bal : 0; 
			}

			
			if (inTurn && relativeAngleMovement<-0.001 && relativePosMovement<-0.001 && distToEstCircle<-W && distToEstCircle<lastDistToEstCircle && speedX>lowestSpeed) {
//				acc = 0;
//				brake = (speed*speed-lowestSpeed*lowestSpeed)/(speed*speed);
			}
//			if (absSpeedY<absLastSpeedY && relativePosMovement<-0.001 && relativeAngleMovement<-0.001 && absSpeedY<LOW_SPEEDY) {
//				steer = 0;
//			}
			
//			if (acc==0 && brake ==0 && relativeAngleMovement>0.001 && absSpeedY>absLastSpeedY && absSpeedY>=MODERATE_SPEEDY)
//				acc = CONSTANT_SPEED_ACC;
			
			/*if ( relativeAngleMovement<-0.001 && (!canGoToLastSeg && mLastY>0 ||  a>TURNANGLE && absSpeedY>=MODERATE_SPEEDY) && (relativePosMovement<0 || relativeAngle<-0.001) && steer*turn<=0 && !isSafeToAccel && absSpeedY<VERY_HIGH_SPEEDY) {
				steer = (absSpeedY>=MODERATE_SPEEDY || speedX>=lowestSpeed || toOutterEdge<=W*2.5) ? -turn : steer;
//				acc *= CONSTANT_SPEED_ACC;
				if (brake==0 && relativePosMovement<-0.001 && relativeAngle<-0.001 && !canGoToLastSeg) {
					acc *= (speedX>first_speed-tW || !maxTurn) ? 0 : 1;
//					brake = 0.05;
				}
			} else if (!isSafeToAccel && relativeAngleMovement>-0.001 && relativeTargetAngle<-0.001)
				acc = CONSTANT_SPEED_ACC*0.5;//*/
			
//			if (acc>0 && !canGoToLastSeg && mLastY>0 &&  a>TURNANGLE && relativeAngleMovement<-0.001 && relativeTargetAngle>0) {
//				acc = 0;				
//			}
			//			if (absSpeedY>=MODERATE_SPEEDY){ 
			//				acc = (absSpeedY>=HIGH_SPEEDY || speed<targetSpeed && toOutterEdge>=GAP) ? 1 : 0.5;				
			//			} 
//			if ((absSpeedY>=HIGH_SPEEDY || speedX<=lowestSpeed && relativePosMovement>0) && relativeAngleMovement>0 && relativeAngle>0 && relativeTargetAngle>0 && speedX<targetSpeed+5){
//				acc *= INCREASE_ONE;
//
//			} else if (relativePosMovement<0 && relativeAngleMovement<0){
//				//				if (relativeAngle<0 && !canGoToLastSeg)
//				//					acc = 0;				
//			}			
//		}


		/*if (absSpeedY>=VERY_HIGH_SPEEDY){
			steer = (relativeAngle>0) ? 0 : turn;
			acc = (relativeAngle>0 && speed<targetSpeed) ? 1 : 0;
			brake = 0;
		}//*/
		//		else if (acc>0 && relativeSteer<-0.001 && relativeAngle>=0.001 && relativeAngleMovement>=0 && relativePosMovement>=0)
		//			steer = (steer<0) ? -1 : 1;



		//		if (absSpeedY>VERY_HIGH_SPEEDY) 
		//			acc = 1;
		//		else if (absSpeedY<absLastSpeedY && relativeTargetAngle>0 && relativePosMovement<0 && relativeAngle>0){			
		//			steer = -turn;			
		//			acc = CONSTANT_SPEED_ACC;			
		//			if (acc>0) brake = 0;
		//		} 

		if (steer>1) 
			steer = 1;
		if (steer<-1) steer = -1;
		if (acc<0) acc = Math.min(acc, -0.05);
		lastAcc = acc;
		lastTargetSpeed = targetSpeed;
		lastTargetAngle = angle;
		if (acc<0) {
			brake = -acc;
			acc = 0;				
		}
		return acc;
	}
	private static final double steerAtSpeed(double x){
		if (x>=216)
			return 0.405956540491453-0.00402398400604155*x+1.46983630854209e-05*x*x-1.88619693222940e-08*x*x*x;
		if (x>180)
			return -6.40122354928717+0.100933617693404*x-0.000519289942888996*x*x+8.79221413082921e-07*x*x*x;
		if (x>140)
			return 1.31349334978100-0.0158496396133958*x+6.53860943510914e-05*x*x-8.64166930377492e-08*x*x*x;		
		return 1.00309570103181-0.0115995398441519*x+4.70782598803805e-05*x*x-6.49876327849716e-08*x*x*x;	
	}
	private static final void testing(NewCarState cs,EdgeDetector ed){		
		int trackDataSz = 0;				
		int trackMidSz = 0;		
		int len = NewCarState.sz;
		double[] l = NewCarState.l;
		int[] types = NewCarState.type;
		Vector2D[] center = NewCarState.center;
		double[] radiusL = NewCarState.radiusL;
		double[] radiusR = NewCarState.radiusR;
		double[] SXL = NewCarState.sXL;
		double[] SXR = NewCarState.sXR;
		double[] SYL = NewCarState.sYL;
		double[] SYR = NewCarState.sYR;
		double[] EXL = NewCarState.eXL;
		double[] EXR = NewCarState.eXR;
		double[] EYL = NewCarState.eYL;
		double[] EYR = NewCarState.eYR;
		double[] Dist = NewCarState.dist;
		double[] Arc = NewCarState.arc;
		Vector2D[] vertex = NewCarState.vertex;
		double px = cs.posX;
		double py = cs.posY;
		final int STRT = 3;
		final int LFT = 2;
		//		final int RGT = 1;
		int tp = types[0];
		
		double sxL = Math.round((vertex[0].x-px)*PRECISION)/PRECISION;
		double syL = vertex[0].y-py;
		//		double sxR = cs.vertex[1].x-px;
		//		double syR = cs.vertex[1].y-py;
		double exL = Math.round((vertex[2].x-px)*PRECISION)/PRECISION;
		double eyL = Math.round((vertex[2].y-py)*PRECISION)/PRECISION;
		//		double exR = cs.vertex[3].x-px;
		//		double eyR = cs.vertex[3].y-py;		
		double a = 0;
		if (tp==STRT){			
			double dx = Math.round((exL-sxL)*PRECISION)/PRECISION;
			double dy = Math.round((eyL-syL)*PRECISION)/PRECISION;		
			a = PI_2-Math.atan2(dy,dx);
		} else {
			double dx = Math.round((center[0].x-px)*PRECISION)/PRECISION;
			double dy = Math.round((center[0].y-py)*PRECISION)/PRECISION;
			//			dx = Math.round(dx*PRECISION)/PRECISION;
			//			dy = Math.round(dy*PRECISION)/PRECISION;
			a = (tp==LFT) ? Math.PI-Math.atan2(dy, dx) : -Math.atan2(dy, dx);
			a = Math.round(a*PRECISION)/PRECISION;			
		} 
		//		boolean ok = false;
		TrackSegment seg = null;

		for (int i=0;i<len;++i){
			int type = (int)types[i];	
			double centerx = Math.round((center[i].x-px)*PRECISION)/PRECISION;
			double centery = Math.round((center[i].y-py)*PRECISION)/PRECISION;
			double cos = Math.cos(a);
			double sin = Math.sin(a);
			//			Vector2D c = new Vector2D(centerx * cos - centery* sin, centerx*sin + centery*cos);
			double cx = centerx * cos - centery* sin;
			double cy = centerx*sin + centery*cos;
			double length = l[i];			
			double rL = Math.round((radiusL[i])*PRECISION)/PRECISION;
			double rR = Math.round((radiusR[i])*PRECISION)/PRECISION;
			double sXL = Math.round((SXL[i]-px)*PRECISION)/PRECISION;
			double sYL = Math.round((SYL[i]-py)*PRECISION)/PRECISION;
			double eXL = Math.round((EXL[i]-px)*PRECISION)/PRECISION;
			double eYL = Math.round((EYL[i]-py)*PRECISION)/PRECISION;
			double sXR = Math.round((SXR[i]-px)*PRECISION)/PRECISION;
			double sYR = Math.round((SYR[i]-py)*PRECISION)/PRECISION;
			double eXR = Math.round((EXR[i]-px)*PRECISION)/PRECISION;
			double eYR = Math.round((EYR[i]-py)*PRECISION)/PRECISION;
			double dist = Math.round((Dist[i])*PRECISION)/PRECISION;

			double p1x = sXL*cos-sYL*sin;
			double p1y = sXL*sin + sYL * cos;			
			double p2x = eXL*cos - eYL*sin;
			double p2y = eXL*sin +eYL *cos;
			double p3x = sXR*cos-sYR*sin;
			double p3y = sXR*sin+sYR*cos;
			double p4x = eXR*cos-eYR*sin;
			double p4y = eXR*sin+eYR*cos;
			p1x =Math.round(p1x*PRECISION)/PRECISION;
			p1y =Math.round(p1y*PRECISION)/PRECISION;
			p2x =Math.round(p2x*PRECISION)/PRECISION;
			p2y =Math.round(p2y*PRECISION)/PRECISION;
			p3x =Math.round(p3x*PRECISION)/PRECISION;
			p3y =Math.round(p3y*PRECISION)/PRECISION;
			p4x =Math.round(p4x*PRECISION)/PRECISION;
			p4y =Math.round(p4y*PRECISION)/PRECISION;
			double arc = Arc[i];
			//			if (cs.type.length>3 && (int)(cs.type[0])==RGT) 
			//				ok = true;

			if (type==STRT || rL==0) {
				type = TrackSegment.STRT;								
			} else if (type==LFT) {
				type = TrackSegment.LFT;
			} else {
				type = TrackSegment.RGT;
				arc = -arc;
			}

			double startX = (p1x+p3x)*0.5;
			double startY = (p1y+p3y)*0.5;
			double endX = (p2x+p4x)*0.5;
			double endY = (p2y+p4y)*0.5;
			startX =Math.round(startX*PRECISION)/PRECISION;
			startY =Math.round(startY*PRECISION)/PRECISION;
			endX =Math.round(endX*PRECISION)/PRECISION;
			endY =Math.round(endY*PRECISION)/PRECISION;
			if (type!=0 && Math.abs(endX-startX)<TrackSegment.EPSILON*0.1) type = 0;
			double r = (rL+rR)/2;			
			TrackSegment ts = trackDataArr[trackDataSz++]; 
			TrackSegment rts = trackDataArr[trackDataSz++]; 
			ts.copy(type,cx,cy,length,dist,rL,arc,p1x,p1y,p2x,p2y);
			rts.copy(type,cx,cy,length,dist,rR,arc,p3x,p3y,p4x,p4y);			
			seg = trackMidArr[trackMidSz++];
			seg.copy(type,cx,cy,length,dist,r,arc,startX,startY,endX,endY);			 
			//			if (i==0){
			//				if (debug) System.out.print"Current Segment : "+seg);
			//				if (debug) System.out.print"Deflection Angle : "+(PI_2-a));
			//			}
			//			if (i==1)
			//				if (debug) System.out.print"Next Segment : "+seg);			

		}		

		//		checkVector(cs,oal);
		/*for (int i=0;i<ed.numpoint;++i){
			TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, ed.x.getDouble(i), ed.y.getDouble(i));
			oal.add(ts);
		}//*/
		//		if (time>=4.78)
		//			if (debug) System.out.print);
		/*TrackSegment.drawTrack(trackData, "Track "+cs.distFromStart+" "+time);			
		try {
			Thread.sleep(400);
		} catch (Exception e) {
			// TODO: handle exception
		}//*/
		//		
		trackData.size(trackDataSz);
		trackMid.size(trackMidSz);
	}
	//return 0,1: center ,2 radius of best path, 3,4 coordinate of next waypoint
	public final static boolean tryGotoPoint(Vector2D carDirection,double speedRadius,int turn,double px,double py,double directionx,double directiony,double dSpeed,int fromSeg,int toSeg,double[] tmp,boolean checkOptimum){
		if (toSeg==fromSeg) return false;
		DANGER = false;		
		Segment t = trArr[ trIndx[0] ];
		Segment lastSeg = toSeg>fromSeg ? trArr[ trIndx[toSeg-1] ] : null;
		//		double safeDist = (lastSeg.type==0) ? lastSeg.start.y : lastSeg.center.y;
		double lr = (lastSeg.type==0) ? Segment.MAX_RADIUS : lastSeg.radius;
		double tr = (t.type==0) ? Segment.MAX_RADIUS : t.radius;
		double first_speed = (t==null || t.type==0) ? Double.MAX_VALUE : speedAtRadius(t.radius);
		int wEdge = 0;		
		double ang = Vector2D.angle(0,1,px,py-1);
		DANGER = lastSeg.end.y<=FAST_MARGIN && turnLeft==turnRight && turnLeft!=0 && turnLeft!=lastSeg.type || Math.abs(ang)>=MAX_ANGLE;

		//		Segment lowestSeg = null;
		double lowestRad = 1000;
		for (int i = trSz-1;i>=0;--i){
			Segment tt = trArr[ trIndx[i]];
			if (tt.type!=0 && tt.radius<lowestRad){
				lowestRad = tt.radius;
				//				lowestSeg = tt;				
			}			
		}				
		double lowestSpeed = speedAtRadius(lowestRad);
		double last_speed = (lastSeg==null || lastSeg.type==0) ? Double.MAX_VALUE : speedAtRadius(lastSeg.radius);	
		//		double relativeAngleMovement = (curAngle-lastAngle)*turn;
		double relativeAngle = curAngle*turn;
		//		double relativeHeading = carDirection.x*turn;
		//		double relativePosMovement = (curPos-lastPos)*turn;
		boolean notMaxTurn = relativeAngle>0 && speed<lowestSpeed-tW || (Math.abs(speedY)<MODERATE_SPEEDY && lastSeg.type!=0 && speed<last_speed+10);
		maxTurn = false;
		if (!notMaxTurn){
			tryMaxTurn(carDirection, speedRadius, turn);
		} 

		if (lastSeg!=t && (t.type==0 || tr>=lr) && lr>speedRadius){ 		
			Vector2D cntr = null;
			double rad = 0;
			double dx = 0;
			double dy = 0;
			boolean found = false;
			for (int j= trSz-1;j>=0;--j){
				Segment s = trArr[ trIndx[j]];		
				if (s==t) break;
				if (!DANGER && py>35){
					tmp[2] = lr;
					if (toMiddle*turn<0 || carDirection.x*turn<0 || speedRadius>100){		 				
						tmp[3] = (s.radius>=120 || s.radius>100 && t.type!=0) ? (turn*toMiddle>0 && s.radius<120) ? toMiddle : toMiddle+s.type*0.5*W : toMiddle+s.type*0.5*W;
						tmp[4] = py;
					} else {
						tmp[3] = lastSeg.end.x;
						tmp[4] = lastSeg.end.y;
					}
					return true;
				}
				if (s.type!=0){
					Segment seg = (s.type==1) ? s.rightSeg : s.leftSeg;
					cntr = seg.center;					
					rad = (s.radius>100) ? s.radius : seg.radius+W;					
					if (rad<speedRadius) continue;
					if (rad>200) {
						dx = s.end.x;
						dy = s.end.y;
					} else {
						double d = cntr.length();			
						if (d>rad){//outside
							Geom.ptTangentLine(0, 0, cntr.x, cntr.y, rad, tmp);
							dx = (s.type==TURNRIGHT) ? tmp[0] : tmp[2];
							dy = (s.type==TURNRIGHT) ? tmp[1] : tmp[3];
						} else {
							rad-=0.5*W;
							if (d>rad){//outside
								Geom.ptTangentLine(0, 0, cntr.x, cntr.y, rad, tmp);
								dx = (s.type==TURNRIGHT) ? tmp[0] : tmp[2];
								dy = (s.type==TURNRIGHT) ? tmp[1] : tmp[3];
							} else {
								rad-=0.5*W;
								if (d>rad){//outside
									Geom.ptTangentLine(0, 0, cntr.x, cntr.y, rad, tmp);
									dx = (s.type==TURNRIGHT) ? tmp[0] : tmp[2];
									dy = (s.type==TURNRIGHT) ? tmp[1] : tmp[3];
									dx-=turn*0.5*W;
								} else continue;
								//							dy = py*0.5;						
								//							double dby = cntr.y-dy;
								//							double dbx = Math.sqrt(rad*rad - dby*dby);								
								//							dx = (cntr.x>0) ? cntr.x-dbx : cntr.x+dbx;
							}
						}
					}
					if (isCutLine(0, 0, dx, dy, 0, trSz, tmp, 0)==0){
						found = true;
						lr = rad;
						break;
					} else continue;
				} else {
					double rx = s.end.x;
					double ry = s.end.y;																
					dx = rx-s.start.x;
					dy = ry - s.start.y;
					double bestCx = 0;
					double bestCy = 0;
					double bestRad = 0;
					double startX = 0;
					double startY = 0;

					double d = Math.sqrt(rx*rx+ry*ry);			
					if (d<speedRadius){
						int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
						if (sz>0){
							double nrx = tmp[0];
							double nry = tmp[1];
							if (sz>2 && nry<tmp[3]){
								nrx = tmp[2];
								nry = tmp[3];
							}
							dx = nrx - rx;
							dy = nry - ry;
							d = Math.sqrt(dx*dx+dy*dy);
							double ratio = (d+1)/d;
							rx += dx*ratio;
							ry += dy*ratio;
						} 
					}

					Geom.getLineLineIntersection(rx, ry, rx+dx, ry+dy, 0, 0, 0, 1, tmp);			
					double oy = tmp[1];
					if (oy>0){												
						Geom.getCircle3(rx, ry, rx+dx, ry+dy, 0, 0, 0, 1, tmp);			
						bestCx = tmp[0];
						bestCy = tmp[1];
						bestRad = Math.sqrt(tmp[2]);
						Geom.getLineLineIntersection(bestCx, bestCy, bestCx-bestRad, bestCy, 0, 0, 0, 1, tmp);
						startX = tmp[0];
						startY = tmp[1];
					}
					boolean ok1 = true;
					//					int wE = 0;
					if (oy<=0 || bestRad<=speedRadius || (isCut(bestCx, bestCy, bestRad, startX, startY, rx, ry, 0, trSz, tmp,GAP))!=0){						
						ok1 = false;	
						int cutEdge = (int)tmp[2];
						Segment cutSeg = (cutEdge>=0 && cutEdge<trSz) ? trArr[ trIndx[cutEdge] ] : null;

						if (cutSeg==null || cutSeg.type==0){
							for (int i =1;i<trSz;++i){
								cutSeg = trArr[ trIndx[i] ];
								if (cutSeg.type==0) continue;
								break;
							}
						}

						if (cutSeg!=null && cutSeg.type!=0){
							Geom.ptTangentLine(0, 0, cutSeg.center.x, cutSeg.center.y, cutSeg.radius-tW+W,tmpBuf);
							double mx = (cutSeg.type==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
							double my = (cutSeg.type==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];
							Geom.getLineLineIntersection(0, 0, 0, 1, rx, ry, rx+dx, ry+dy, tmpBuf);
							double oox = tmpBuf[0];
							double ooy = tmpBuf[1];
							double rdx = rx - oox;
							double rdy = ry - ooy;
							double rd = Math.sqrt(rdx*rdx+rdy*rdy);
							double rox = 0;
							double roy = ooy - rd;
							rox += rx;
							roy += ry;
							rox*=0.5;
							roy*=0.5;
							int sz = Geom.findCenter(oox, ooy, 0, 0, rox, roy, mx, my, tmpBuf);
							if (sz==2){
								bestCx = tmpBuf[0];
								bestCy = tmpBuf[1];							
								bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null));
							} else if (sz>2){
								bestCx = tmpBuf[0];
								bestCy = tmpBuf[1];
								if (bestCy>ooy || (bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null)))<speedRadius ){
									bestCx = tmpBuf[2];
									bestCy = tmpBuf[3];
									bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null));
								}								
							}
							if (bestRad>speedRadius){
								startY = ry*0.2;
								double dby = bestCy-startY;
								double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
								startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
							}
							ok1 = bestRad>speedRadius && isCut(bestCx, bestCy, bestRad, 0, 0, rx, ry, 0, trSz, tmp, GAP)==0;
						}
					}
					Geom.getCircle3(0, 0, 0, 1, rx, ry, rx+dx, ry+dy,tmp);			
					double bCx = tmp[0];
					double bCy = tmp[1];
					double bRad = Math.sqrt(tmp[2]);					
					boolean ok2 = isCut(bCx, bCy, bRad, 0, 0, rx, ry, 0, trSz, tmp, GAP)==0;
					if (ok2){
						if (!ok1 || bRad>bestRad){
							bestCx = bCx;
							bestCy = bCy;
							bestRad = bRad;
							startY = ry*0.33;
							double dby = bCy-startY;
							if (Math.abs(bRad)<Math.abs(dby)) continue;
							double dbx = Math.sqrt(bRad*bRad - dby*dby);							
							startX = (bCx>0) ? bCx-dbx : bCx+dbx;
						}
					}
					if (startY<=0){
						d = Math.sqrt(bestCx*bestCx+bestCy*bestCy);
						if (d>bestRad){
							Geom.ptTangentLine(0, 0, bestCx, bestCy, bestRad, tmp);
							startX = (turn==TURNRIGHT) ? tmp[0] : tmp[2];
							startY = (turn==TURNRIGHT) ? tmp[1] : tmp[3];
						} else {
							startY = ry*0.3;
							double dby = bestCy-startY;
							double dbx = Math.sqrt(bestRad*bestRad - dby*dby);								
							startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;
						}
					} 
					if (startY<0) continue;
					lr = bestRad;
					tmp[0] = bestCx;
					tmp[1] = bestCy;
					dx = startX;
					dy = startY;
//					TrackSegment ts = TrackSegment.createTurnSeg(bestCx, bestCy, bestRad, startX, startY, rx, ry);
//					trackData.add(ts);
					found = true;
					break;
				}//end of if
			}		
			if (found){
				if (cntr!=null){
					tmp[0] = cntr.x;
					tmp[1] = cntr.y;
				}
				tmp[2] = lr;
				tmp[3] = dx;
				tmp[4] = dy;
//				TrackSegment ts = TrackSegment.createTurnSeg(tmp[0],tmp[1], lr, dx, dy,px,py);
//				trackData.add(ts);
				return true;
			}
		}		
		double bestCx = 0;
		double bestCy = 0;
		double bestRad = 0;
		double bestSx = tmp[3];
		double bestSy = tmp[4];
		double directCx = 0;
		double directCy = 0;
		double directRad = 0;
		double directSx = 0;
		double directSy = 0;
		boolean canGotoStartPoint = false;
		boolean isOutside = false;

		//			if (lastSeg.type==0 || lastSeg.radius<100 && lastSeg.radius<speedRadius+90){
		if (checkOptimum && !DANGER && (lastSeg.type!=0 && lastSeg.radius<100 && speed<last_speed+80)){	
			//				int j = toSeg-2;
			//				for (;j>=0;--j){
			//					t = trArr[ trIndx[j] ];
			//					if (t.type!=lastSeg.type) break;
			//				}
			if (t!=null){
				if (t!=lastSeg && optimalPath(speedRadius,t, lastSeg, turn, tmp)){						
					bestCx = tmp[0];
					bestCy = tmp[1];
					bestRad = tmp[2];
					if (lastSeg.type==0 || bestRad>=lastSeg.radius){
						double startX = tmp[3];
						double startY = tmp[4];
						double dx = startX - bestCx;
						double dy = startY - bestCy;
						//				draw = true;
						//						wEdge= bestCy>py || bestCy<0 || startY<0 ? -1 : isCut(bestCx, bestCy, bestRad, startX,startY,px,py,0, trSz, tmp);
						double distToCenter = Math.sqrt(bestCx*bestCx+bestCy*bestCy);
						isOutside = distToCenter>=bestRad;
						if (startY>0 || isOutside){					
							if (startY<0){
								Geom.ptTangentLine(0, 0, bestCx, bestCy, bestRad, tmp);
								startX = (turn==TURNRIGHT) ? tmp[0] : tmp[2];
								startY = (turn==TURNRIGHT) ? tmp[1] : tmp[3];
								canGotoStartPoint = canGotoPoint(carDirection, speedRadius, startX,	 startY);
								if (canGotoStartPoint) {
									tmp[0] = bestCx;
									tmp[1] = bestCy;
									tmp[2] = bestRad;
									tmp[3] = bestSx;
									tmp[4] = bestSy;
									return true;
								}
							} else canGotoStartPoint = canGotoPoint(carDirection, speedRadius, startX,	 startY);
							double ddx = startX - px;
							double ddy = startY - py;
							double d = Math.sqrt(ddx*ddx+ddy*ddy);
							boolean isOk = startY>0 && canGotoStartPoint && (isOutside || canGoDirect(carDirection, speedRadius, turn, startX, startY, startX+dx, startY+dy,bestRad+d,fromSeg,toSeg,tmp)==0); 								
							if (isOk){											
								bestCx = tmp[0];
								bestCy = tmp[1];
								bestRad = tmp[2];
								bestSx = tmp[3];
								bestSy = tmp[4];						
								tmp[0] = bestCx;
								tmp[1] = bestCy;
								tmp[2] = bestRad;
								tmp[3] = bestSx;
								tmp[4] = bestSy;
								return true;						
							} else if (canGotoStartPoint){
								double angle = carDirection.angle(startX,startY);								
								int tp = angle>0 ? 1 :(angle==0) ? 0 : -1;
								if (tp*turn>=0 || t.type*turn<=0) {
									tmp[0] = bestCx;
									tmp[1] = bestCy;
									tmp[2] = bestRad;
									tmp[3] = startX;
									tmp[4] = startY;
									return true;	
								}
							}
						}
					}
				}
			}
		}

		Segment cutSeg = null;						
		for (int i =trSz-1;i>=1;--i){
			cutSeg = trArr[ trIndx[i] ];
			if (cutSeg.type==0 || cutSeg.type==Segment.UNKNOWN) continue;						
			if (cutSeg!=null && cutSeg.type!=0){
				double rad = (cutSeg.radius>150) ? cutSeg.radius-tW+W*2 : cutSeg.radius-tW+W;
				if (cutSeg.center.length()<=rad){
					rad = cutSeg.radius-tW+GAP;
					if (cutSeg.center.length()<=rad){
						Geom.getLineLineIntersection(0, 0, 0, 1, cutSeg.end.x, cutSeg.end.y, cutSeg.start.x, cutSeg.start.y, tmpBuf);								
						double startX = tmpBuf[0];
						double startY = tmpBuf[1];

						double d = Math.sqrt(startX*startX+startY*startY);
						tmp[0] = cutSeg.center.x;
						tmp[1] = cutSeg.center.y;											
						//							tmp[2] = dSpeed+d*3;
						if (speedRadius>rad && speedRadius<90){
							if (d>=20 && rad<60) 
								d *=3;
							else if (d>15) d*=2;
						}
						tmp[2] = Math.max(rad,dSpeed)+d;
						tmp[3] = startX;
						tmp[4] = startY;
						break;//*/								
					}
				}
				if (Geom.ptTangentLine(0, 0, cutSeg.center.x, cutSeg.center.y,  rad ,tmpBuf)==0) continue;
				double mx = (cutSeg.type==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
				double my = (cutSeg.type==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];
				//						double d = Math.sqrt(mx*mx+my*my);
				double rx = lastSeg.end.x;
				double ry = lastSeg.end.y;		
				double dx = rx-lastSeg.start.x;
				double dy = ry - lastSeg.start.y;
				Geom.getLineLineIntersection(0, 0, 0, 1, rx, ry, rx+dx, ry+dy, tmpBuf);
				double oox = tmpBuf[0];
				double ooy = tmpBuf[1];
				double rdx = rx - oox;
				double rdy = ry - ooy;
				double rd = Math.sqrt(rdx*rdx+rdy*rdy);
				double rox = 0;
				double roy = ooy - rd;
				rox += rx;
				roy += ry;
				rox*=0.5;
				roy*=0.5;
				int sz = Geom.findCenter(oox, ooy, 0, 0, rox, roy, mx, my, tmpBuf);
				if (sz!=0){
					if (sz==2){
						bestCx = tmpBuf[0];
						bestCy = tmpBuf[1];							
						bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null));
					} else if (sz>2){
						bestCx = tmpBuf[0];
						bestCy = tmpBuf[1];
						if (bestCy>ooy || (bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null)))<speedRadius ){
							bestCx = tmpBuf[2];
							bestCy = tmpBuf[3];
							bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null));
						} else bestRad = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, bestCx, bestCy, null));								
					}
					double startX = rx;
					double startY = ry*0.5;

					double dby = bestCy-startY;
					double dbx = Math.sqrt(bestRad*bestRad - dby*dby);							
					startX = (bestCx>0) ? bestCx-dbx : bestCx+dbx;							

					if (bestRad>=lowestRad && isCut(bestCx, bestCy, bestRad, 0, 0, rx, ry, 0, trSz, tmp, GAP)==0){
//						TrackSegment ts = TrackSegment.createTurnSeg(bestCx, bestCy, bestRad, 0, 0, rx, ry);
//						//								trackData.add(ts);
//						ts = TrackSegment.createTurnSeg(bestCx, bestCy, bestRad, 0, 0, startX, startY);
//						trackData.add(ts);
						tmp[0] = bestCx;
						tmp[1] = bestCy;
						//								if (speedRadius>rad && speedRadius<90){
						//									if (d>=20 && rad<60) 
						//										d *=3;
						//									else if (d>15) d*=2;
						//								}
						//								tmp[2] = dSpeed;
						tmp[2] = bestRad;
						//								tmp[2] = Math.max(bestRad,dSpeed+3*d);
						tmp[3] = startX;
						tmp[4] = startY;
						return true;
					}
				}
			}				
		}

		wEdge = canGoDirect(carDirection, speedRadius, turn, px,py,px-directiony,py+directionx,dSpeed,fromSeg,toSeg,tmp);
		if (wEdge==0) return true;
		//Check if it's a good solution


		/*Geom.getCircle3(px, py, px+directionx, py+directiony, 0, 0, 0, 1, tmp);			
		directCx = tmp[0];
		directCy = tmp[1];
		directRad = Math.sqrt(tmp[2]);
		if (lastSeg.type==0 || directRad>=lastSeg.radius){
			Geom.ptLineDistSq(0, 0, 0, 1, directCx, directCy, tmp);
			directSx = tmp[0];
			directSy = tmp[1];				
			wEdge=(speedRadius-directRad>Math.sqrt(directSx*directSx+directSy*directSy)) ? -1 : isCut(directCx, directCy, directRad,0,directCy,px,py, fromSeg, toSeg, tmp,GAP);
			if (wEdge==0){				
				if (directSy<=0){
					double d = Math.sqrt(directCx*directCx+directCy*directCy);
					if (d>directRad){
						Geom.ptTangentLine(0, 0, directCx, directCy, directRad, tmp);
						directSx = (directionx>0) ? tmp[0] : tmp[2];
						directSy = (directionx>0) ? tmp[1] : tmp[3];
					} 
					//						else {
					//							directSx = 0;
					//							directSy = 1;
					//						}
				} 

				if (directSy>=0){
					TrackSegment ts = TrackSegment.createTurnSeg(directCx, directCy, directRad, 0, directCy, px, py);
					trackData.add(ts);	
					tmp[0] = directCx;
					tmp[1] = directCy;
					tmp[2] = directRad;
					tmp[3] = directSx;
					tmp[4] = directSy;
					return true;	
				}
			}
		}			
		//*/


		//				Geom.getCircle3(px, py, px+directionx, py+directiony, toMiddle, 0, toMiddle, 1, tmp);			
		//				directCx = tmp[0];
		//				directCy = tmp[1];
		//				directRad = Math.sqrt(tmp[2]);
		//				if (lastSeg.type==0 || directRad>=lastSeg.radius){
		//					Geom.ptLineDistSq(toMiddle, 0, toMiddle, 1, directCx, directCy, tmp);
		//					directSx = tmp[0];
		//					directSy = tmp[1];				
		//					wEdge=(speedRadius-directRad>Math.sqrt(directSx*directSx+directSy*directSy)) ? -1 : isCut(directCx, directCy, directRad,0,directCy,px,py, fromSeg, toSeg, tmp,0);
		//					if (wEdge==0){				
		//						if (directSy<=0){
		//							double d = Math.sqrt(directCx*directCx+directCy*directCy);
		//							if (d>bestRad){
		//								Geom.ptTangentLine(0, 0, directCx, directCy, directRad, tmp);
		//								directSx = (edgeDetector.turn==TURNRIGHT) ? tmp[0] : tmp[2];
		//								directSy = (edgeDetector.turn==TURNRIGHT) ? tmp[1] : tmp[3];
		//							}
		//						}
		//		
		//						if (directSy>=0){
		//							TrackSegment ts = TrackSegment.createTurnSeg(directCx, directCy, directRad, 0, directCy, px, py);
		//							trackData.add(ts);	
		//							tmp[0] = directCx;
		//							tmp[1] = directCy;
		//							tmp[2] = directRad;
		//							tmp[3] = directSx;
		//							tmp[4] = directSy;
		//							return true;	
		//						}
		//					}
		//				}			
		//									
		//Simple but effective in case all other solutions fail or are not good
		if (lastSeg.type!=0 && lastSeg.type!=Segment.UNKNOWN){
			boolean rs = true;
			Segment seg = (lastSeg.type==1) ? lastSeg.rightSeg : lastSeg.leftSeg;
			Vector2D cntr = seg.center;
			double cnx = cntr.x;
			double cny = cntr.y;
			double rad = seg.radius+GAP;
			double d = cntr.length();
			double dx = 0;
			double dy = 0;			
			if (d>rad){//outside
				Geom.ptTangentLine(0, 0, cntr.x, cntr.y, rad, tmp);
				dx = (lastSeg.type==TURNRIGHT) ? tmp[0] : tmp[2];
				dy = (lastSeg.type==TURNRIGHT) ? tmp[1] : tmp[3];
			} else {				
				//				double nx = turn*carDirection.y;	
				//				double ny = turn*-carDirection.x;			
				//				double ox = nx*speedRadius;
				//				double oy = ny*speedRadius;
				//				
				//				double lx = lastSeg.end.x - ox;
				//				double ly = lastSeg.end.y - oy;
				//				double l = speedRadius/Math.sqrt(lx*lx+ly*ly);
				//				lx = ox + lx*l;
				//				ly = oy + ly*l;				
				//				wEdge = isCut(ox, oy, speedRadius,0,0,lx,ly, fromSeg, toSeg, tmp,GAP);
				//				if (wEdge==0){
				//					dx = lx;
				//					dy = ly;
				//				}
				//				dx = carDirection.x;
				//				dy = carDirection.y;
				dx = (seg.start.y>10) ? seg.start.x - cnx : seg.end.x -cnx;
				dy = (seg.start.y>10) ? seg.start.y - cny : seg.end.y - cny;
				rad = seg.radius +GAP;
				d = rad/Math.sqrt(dx*dx+dy*dy);
				dx = cnx + dx*d;
				dy = cny + dy*d;
				rs = false;
			}
			tmp[0] = cnx;
			tmp[1] = cny;
			tmp[2] = rad;
			tmp[3] = dx;
			tmp[4] = dy;
//			TrackSegment ts = TrackSegment.createTurnSeg(cntr.x, cntr.y, rad, 0, 0, dx, dy);
//			trackData.add(ts);
			return rs;
		}
		return false;
	}
	public final static boolean tryMaxTurn(Vector2D carDirection,double speedRadius,int turn){
		Segment t = trArr[ trIndx[0] ];
		Segment lastSeg = trSz>0 ? trArr[ trIndx[trSz-1] ] : null;
		//		double safeDist = (lastSeg.type==0) ? lastSeg.start.y : lastSeg.center.y;
		//		double lr = (lastSeg.type==0) ? Segment.MAX_RADIUS : lastSeg.radius;
		//		double tr = (t.type==0) ? Segment.MAX_RADIUS : t.radius;
		double first_speed = (t==null || t.type==0) ? Double.MAX_VALUE : speedAtRadius(t.radius);
		int wEdge = 0;


		//		Segment lowestSeg = null;
		double lowestRad = 1000;
		for (int i = trSz-1;i>=0;--i){
			Segment tt = trArr[ trIndx[i]];
			if (tt.type!=0 && tt.radius<lowestRad){
				lowestRad = tt.radius;
				//				lowestSeg = tt;				
			}			
		}						

		double lowestSpeed = speedAtRadius(lowestRad);
		double last_speed = (lastSeg==null || lastSeg.type==0) ? Double.MAX_VALUE : speedAtRadius(lastSeg.radius);	
		//		double relativeAngleMovement = (curAngle-lastAngle)*turn;
		double relativeAngle = curAngle*turn;
		//		double relativeHeading = carDirection.x*turn;
		double relativePosMovement = (curPos-lastPos)*turn;
//		if (relativePosMovement>0) return true;
		//		if (trSz==1 && (relativePosMovement>-0.001 || relativeAngle>0.001)) return false;				
		//		if (relativeAngle>0 && speed<lowestSpeed-tW || (Math.abs(speedY)<MODERATE_SPEEDY && lastSeg.type!=0 && speed<last_speed+10)) return false;

//		if (t.type*turn>=0){
			double nx = turn*carDirection.y;	
			double ny = -turn*carDirection.x;			
			double ox = nx*speedRadius;
			double oy = ny*speedRadius;

			double lx = lastSeg.end.x - ox;
			double ly = lastSeg.end.y - oy;
			double l = speedRadius/Math.sqrt(lx*lx+ly*ly);
			lx = ox + lx*l;
			ly = oy + ly*l;
			int wEdge1 = 0;
			double gap = toOutterEdge>GAP ? GAP : toOutterEdge*0.5;
			if (toInnerEdge<0){				
				wEdge = isCut(ox, oy, speedRadius,0,0,lx,ly, 0, trSz, tmp,gap);
				int indx = (int)(tmp[2]);				
				t = (wEdge!=0) ? trArr[ trIndx[indx]] : null;
				if (wEdge==0 || wEdge==turn && (t.type==0 || t.type==turn) ){													
					double sp = speedRadius+1;
					ox = nx*sp;
					oy = ny*sp;					
					lx = lastSeg.end.x - ox;
					ly = lastSeg.end.y - oy;
					l = sp/Math.sqrt(lx*lx+ly*ly);
					lx = ox + lx*l;
					ly = oy + ly*l;
					wEdge = isCut(ox, oy, sp,0,0,lx,ly, 0, trSz, tmp,W);
					indx = (int)(tmp[2]);				
					t = (wEdge!=0) ? trArr[ trIndx[indx]] : null;
					if ((wEdge==0 || wEdge==turn && (t.type==0 || t.type==turn)) || Math.abs(speedY)<=MODERATE_SPEEDY){
						tmp[2] = sp;
						tmp[0] = ox;
						tmp[1] = oy;										
						tmp[3] = lx;
						tmp[4] = ly;
						maxTurn = true;
						return true;
					}
					
				} 
			}
//		}

			turn = -turn;
			nx = turn*carDirection.y;	
			ny = -turn*carDirection.x;			
			ox = nx*speedRadius;
			oy = ny*speedRadius;

			lx = lastSeg.end.x - ox;
			ly = lastSeg.end.y - oy;
			l = speedRadius/Math.sqrt(lx*lx+ly*ly);
			lx = ox + lx*l;
			ly = oy + ly*l;
			gap = toInnerEdge<-GAP ? GAP : toOutterEdge*0.5;
			if (toInnerEdge<0){				
				wEdge1 = isCut(ox, oy, speedRadius,0,0,lx,ly, 0, trSz, tmp,gap);
				int indx = (int)(tmp[2]);				
				t = (wEdge1!=0) ? trArr[ trIndx[indx]] : null;
				if (wEdge1==0 || wEdge1==-turn && (t.type==0 || t.type==-turn)){													
					double sp = speedRadius+1;
					ox = nx*sp;
					oy = ny*sp;					
					lx = lastSeg.end.x - ox;
					ly = lastSeg.end.y - oy;
					l = sp/Math.sqrt(lx*lx+ly*ly);
					lx = ox + lx*l;
					ly = oy + ly*l;
					wEdge1 = isCut(ox, oy, sp,0,0,lx,ly, 0, trSz, tmp,W);
					indx = (int)(tmp[2]);				
					t = (wEdge1!=0) ? trArr[ trIndx[indx]] : null;
					if ((wEdge1==0 || wEdge1==turn && (t.type==0 || t.type==-turn)) || Math.abs(speedY)<=MODERATE_SPEEDY){
						tmp[2] = sp;					
						tmp[0] = ox;
						tmp[1] = oy;										
						tmp[3] = lx;
						tmp[4] = ly;
						maxTurn = true;
						return true;
					}
				}
			}
		maxTurn = (wEdge!=wEdge1);
		return false;
	}

	static final double verify(Vector2D[] v,int to,Vector2D[] opoints,int otherTo,Segment edge,int wh,double toMiddle,double tW,double nraced){		
		if (to<3 && otherTo<3 || canGoVeryFast) {
			if (edge!=null && edge.type!=Segment.UNKNOWN) return edge.type*edge.radius;
			return -1;
		}				
		double r = -1;
		//		double max = 100;		
		//		double[] cntrx = CircleDriver2.cntrx;
		//		double[] cntry = CircleDriver2.cntry;
		int[] nums = CircleDriver2.nums;		
		int[] turns = CircleDriver2.turns;
		//		double[] realRads = CircleDriver2.realRads;
		double[] rads = CircleDriver2.rads;
		int indx = 0;
		int[] map =  tmpMap;
		int[] lnum = CircleDriver2.lnum;
		int[] rnum = CircleDriver2.rnum;
		double bestR=0;
		double mincy = 100;
		int bestTp = 0;		
		final double E = TrackSegment.EPSILON*0.1;
		if (debug) System.out.println("Create array end time : "+(System.nanoTime()-ti)/1000000+" ms.");

		for (int i = 0;i<to-2;++i){
			Vector2D p1 = v[i];
			double x1 = p1.x;
			double y1 = p1.y;
			for (int j = i+1;j<to-1;++j){
				Vector2D p2 = v[j];
				double x2 = p2.x;
				double y2 = p2.y;
				for (int k = j+1;k<to;++k){
					Vector2D p3 = v[k];													
					double x3 = p3.x;
					double y3 = p3.y;

					double ax = x2 - x1;  // first compute vectors
					double ay = y2 - y1;  // a and c
					double cx = x1 - x3;
					double cy = y1 - y3;

					double aPerpDOTc = ax * cy - ay * cx;

					if (aPerpDOTc == 0){						
						continue;
					}

					double bx = x3 - x2;
					double by = y3 - y2;
					double bDOTc = bx * cx + by * cy;

					double qo = bDOTc / aPerpDOTc;
					double sy = y1 + (ay + qo * ax) / 2.0d; // (sx, sy)					
					double sx = x1 + (ax - qo * ay) / 2.0d; // found center of circle										
					double dx = x1 - sx;
					double dy = y1 - sy;
					r = Math.sqrt(dx * dx + dy * dy);
					if (r<=Segment.REJECT_VALUE) continue;
					if (r>=Segment.MAX_RADIUS){
						if (Math.abs(x2-x1)<=E && y2-y1>1) return 0;
						if (Math.abs(x3-x1)<=E && y3-y1>1) return 0;
					}
					double absSy = (sy<0) ? -sy : sy;
					
					int tp = 0;
					if (r<Segment.MAX_RADIUS-1){
						double ax0 = x1-sx;
						double ay0 = y1-sy;
						double bx0 = x2-sx;
						double by0 = y2-sy;
						double angle = -Math.atan2(by0, bx0)+Math.atan2(ay0, ax0);
						//						angle = (-Math.PI*2+angle)%(Math.PI*2);
						if (angle<-Math.PI) 
							angle += 2*Math.PI;
						else if (angle>Math.PI) 
							angle -= 2*Math.PI;

						tp = (angle<0) ? -1 : 1;								
					}

					lMap.store(i, j, k, tp, r);
					if (absSy>0.01 && !EdgeDetector.isNoisy) continue;
					int sR = (r<Segment.MAX_RADIUS-1) ? ((int)Math.round(r+wh*tp*tW)) : Segment.MAX_RADIUS-1;										
					if (sR<=Segment.REJECT_VALUE) continue;
					if (tp!=0 && mincy>absSy){
						bestR = r+wh*tp*tW;
						bestTp = tp;
						mincy = bestR;
					}
					if (r<Segment.MAX_RADIUS-1 && (absSy>=0.01 || sy<-0.01)) continue;
					if (r>=Segment.MAX_RADIUS-1 && Math.abs(v[i].x-v[i+1].x)>E) continue;					

					if (sR>0 && sR<=Segment.MAX_RADIUS-1 && map[sR]==0) {											
						//						cntrx[indx] = sx;
						//						cntry[indx] = sy;																			
						//						realRads[indx] = r+wh*tp*tW;
						turns[indx] = tp;
						rads[indx++] = sR;						
					}
					if (sR<=Segment.MAX_RADIUS-1) map[sR]++;
				}
			}
		}


		for (int i = 0;i<otherTo-2;++i){
			Vector2D p1 = opoints[i];			
			double x1 = p1.x;
			double y1 = p1.y;
			for (int j = i+1;j<otherTo-1;++j){				
				Vector2D p2 = opoints[j];
				double x2 = p2.x;
				double y2 = p2.y;
				for (int k = j+1;k<otherTo;++k){
					Vector2D p3 = opoints[k];										
					double x3 = p3.x;
					double y3 = p3.y;

					double ax = x2 - x1;  // first compute vectors
					double ay = y2 - y1;  // a and c
					double cx = x1 - x3;
					double cy = y1 - y3;

					double aPerpDOTc = ax * cy - ay * cx;

					if (aPerpDOTc == 0){						
						continue;
					}

					double bx = x3 - x2;
					double by = y3 - y2;
					double bDOTc = bx * cx + by * cy;

					double qo = bDOTc / aPerpDOTc;
					double sy = y1 + (ay + qo * ax) / 2.0d; // (sx, sy)					
					double sx = x1 + (ax - qo * ay) / 2.0d; // found center of circle										
					double dx = x1 - sx;
					double dy = y1 - sy;
					r = Math.sqrt(dx * dx + dy * dy);
					if (r<=Segment.REJECT_VALUE) continue;	
					if (r>=Segment.MAX_RADIUS){
						if (Math.abs(x2-x1)<=E && y2-y1>1) return 0;
						if (Math.abs(x3-x1)<=E && y3-y1>1) return 0;
					}
					double absSy = (sy<0) ? -sy : sy;					
					int tp = 0;
					if (r<Segment.MAX_RADIUS-1){
						double ax0 = x1-sx;
						double ay0 = y1-sy;
						double bx0 = x2-sx;
						double by0 = y2-sy;
						double angle = -Math.atan2(by0, bx0)+Math.atan2(ay0, ax0);
						//						angle = (-Math.PI*2+angle)%(Math.PI*2);
						if (angle<-Math.PI) 
							angle += 2*Math.PI;
						else if (angle>Math.PI) 
							angle -= 2*Math.PI;

						tp = (angle<0) ? -1 : 1;								
					}

					rMap.store(i, j, k, tp, r);
					if (absSy>0.01 && !EdgeDetector.isNoisy) continue;
					int sR = (r<Segment.MAX_RADIUS-1) ? ((int)Math.round(r-wh*tp*tW)) : Segment.MAX_RADIUS-1;					
					if (sR<=Segment.REJECT_VALUE) continue;
					if (tp!=0 && mincy>absSy){
						bestR = r-wh*tp*tW;
						bestTp = tp;
						mincy = absSy;
					}

					if (r>=Segment.MAX_RADIUS-1 && Math.abs(v[i].x-v[i+1].x)>E) continue;
					if (r<Segment.MAX_RADIUS-1 && (absSy>=0.01 || sy<-0.01)) continue;

					if (sR>0 && sR<=Segment.MAX_RADIUS-1 && map[sR]==0) {												
						//						cntrx[indx] = sx;
						//						cntry[indx] = sy;																				
						//						realRads[indx] = r-wh*tp*tW;
						turns[indx] = tp;
						rads[indx++] = sR;					
					}
					if (sR<=Segment.MAX_RADIUS-1) map[sR]++;
				}
			}
		}

		/*for (int i = 0;i<to;++i){
			System.out.print(v[i].y+" ");
		}
		System.out.println();

		for (int i = 0;i<otherTo;++i){
			System.out.print(opoints[i].y+" ");
		}
		System.out.println();
		if (time>=BREAK_TIME)
			System.out.println();

		System.out.println("-----------");//*/		

		if (indx==0 || (indx==1 && rads[0]>=Segment.MAX_RADIUS-1)){
			if (bestR>Segment.MAX_RADIUS-1){
				bestR = Segment.MAX_RADIUS-1;
			}
			if (bestTp!=0){
				int eb = (int)Math.round(bestR);
				rads[indx] = eb;
				turns[indx++] = bestTp;
				map[eb]++;
				if (Math.abs(eb-bestR)>0.1){
					rads[indx] = Math.round(bestR*100)/100.0d;
					turns[indx++] = bestTp;
				}
			}
		}

		int eR = (edge==null) ? 0 : (int)Math.round(edge.radius);
		if (eR>=Segment.MAX_RADIUS) eR = Segment.MAX_RADIUS-1;
		if (edge!=null && edge.type!=0 && eR>=0 && map[eR]==0){			
			rads[indx] = eR;				
			turns[indx++] = edge.type;						
		}
		if (edge!=null && edge.type!=0 && edge.radius!=eR){
			rads[indx] = edge.radius;				
			turns[indx++] = edge.type;
		}		

		int sz = 0;
		boolean current = true;		
		Vector2D cn = new Vector2D();
		for (int i = 0;i<indx;++i){			
			r = rads[i];						
			int tp = turns[i];
			double ttx = (tp==-1) ? r-toMiddle : r+toMiddle;
			double ty = 0;
			Vector2D last = null;
			Vector2D first = null;			
			if (r>=Segment.MAX_RADIUS-1){
				int j = 0;
				first = null;
				int lIndx = 0;
				for (j = 0;j<to;++j){
					last = v[j];
					if (last.y<0) continue;
					if (first==null){						
						double e = last.x - toMiddle+tW;
						if (e<0) e = -e;
						if (e>=E) break;
						first = last;
						lIndx = j;						
						continue;
					}								
					double e = last.x - first.x;
					if (e<0) e = -e;
					if (e>=E) break;
				}
				last = (j>=1) ? v[j-1] : null;
				if (last!=null && last.y<0) last = null;
				int k = 0;
				Vector2D oLast = null;
				int rIndx = 0;
				Vector2D oFirst = null;
				for (k = 0;k<otherTo;++k){
					oLast = opoints[k];		
					if (oLast.y<0) continue;
					if (oFirst==null){
						double e = oLast.x - toMiddle-tW;
						if (e<0) e = -e;
						if (e>=E) break;
						oFirst = oLast;
						rIndx = k;
						continue;
					}				
					double e = oLast.x - oFirst.x;
					if (e<0) e = -e;
					if (e>=E) break;				
				}
				oLast = (k>=1) ? opoints[k-1] : null;
				if (oLast!=null && oLast.y<0) oLast = null;
				nums[i] = j - lIndx + k - rIndx - 2;
				if (last==null && oLast==null) continue;
				if (last==null && j<to && v[j].y>=0) continue;
				if (oLast==null && k<otherTo && opoints[k].y>=0) continue;
				if (oLast!=null && j<to && oLast.y>v[j].y)	continue;
				if (last!=null && k<otherTo && last.y>opoints[k].y)	continue;
				if (first==last && oFirst==oLast) continue;
				if (nums[i]>0) selections[sz++] = i;
				continue;
			}
			//			double cx = cntrx[i];
			//			double cy = cntry[i];			
			double tx = tp*ttx;
			int j = 0;
			double de = wh*tp*tW;						
			boolean found = false;
			first = null;
			int lIdx = 0;
			Vector2D end = null;
			Vector2D endO = null;
			Vector2D oEnd = null;
			Vector2D oEndO = null;
			for (j = 0;j<to;++j){
				last = v[j];
				ty = last.y;	
				if (ty<0) continue;							
				double dx = tx - last.x;				
				double d = Math.sqrt(dx*dx+ty*ty);
				double e = d-r+de;
				if (e<0) e = -e;
				if (e>=gap) break;
				if (first==null){					
					first = last;
					lIdx = j;									
					continue;
				}							


				Segment.circle(leftMost, last, tx,0, r-de,cn);
				if (!found && (last.y>=1 || cn.y==0)){					
					if (cn.y!=0) {
						if (last.y-1>=first.y){
							Segment.circle(first, last, tx,0, r-de,cn);
							if (cn.y!=0){
								lnum[i] = j-lIdx;											
								found = true;
							}
						} else {
							lnum[i] = j-lIdx;											
							found = true;
						}
					} else {
						if (last.y-1>=first.y){													
							Segment.circle(first, last, tx,0, r-de,cn);
							if (cn.y!=0){
								lnum[i] = j-lIdx;											
								found = true;
							}
						}
					}

				} 
				if (!found){
					double t = (r+de)/d;
					double ry = ty*t;
					double rx = tx-dx*t;										
					endO = new Vector2D(rx,ry);
					end = last;						
				}
			}
			last = (j>=1) ? v[j-1] : null;
			if (!found && last!=null && first!=null && last.y>first.y){
				lnum[i] = j-1-lIdx;
			}
			int k = 0;
			Vector2D oLast = null;
			int rIdx = 0;
			found = false;
			Vector2D oFirst = null;			
			for (k = 0;k<otherTo;++k){
				oLast = opoints[k];	
				ty = oLast.y;		
				if (ty<0) continue;
				double dx = tx - oLast.x;		
				double d = Math.sqrt(dx*dx+ty*ty);
				double e = d-r-de;
				if (e<0) e = -e;
				if (e>=gap) break;
				if (oFirst==null){											
					oFirst = oLast;
					rIdx = k;										
					continue;
				}
				Segment.circle(rightMost, oLast, tx,0, r+de,cn);
				if (!found && (oLast.y>=1 || cn.y==0)){					
					if (cn.y!=0) {
						if (oLast.y-1>=oFirst.y){					
							Segment.circle(oFirst, oLast, tx,0, r+de,cn);
							if (cn.y!=0){
								rnum[i] = k-rIdx;											
								found = true;
							}
						} else {
							rnum[i] = k-rIdx;						
							found = true;
						}
					} else {
						if (oLast.y-1>=oFirst.y){					
							Segment.circle(oFirst, oLast, tx,0, r+de,cn);
							if (cn.y!=0){
								rnum[i] = k-rIdx;											
								found = true;
							}
						} 
					}
				}

				if (!found){
					double t = (r-de)/d;
					double ry = ty*t;
					double rx = tx-dx*t;
					oEndO = new Vector2D(rx,ry);
					oEnd = oLast;
				}
			}
			oLast = (k>=1) ? opoints[k-1] : null;
			if (!found && oLast!=null && oFirst!=null && oLast.y>oFirst.y){
				rnum[i] = k-1-rIdx;
			}			
			int ln = lnum[i]+lIdx-1;
			int rn = rnum[i]+rIdx-1;
			boolean ok = true;			
			if (last!=null && oLast!=null){
				double dx = last.x-tx;
				double dy = last.y;
				double d = Math.sqrt(dx*dx+dy*dy);				
				double ry = dy*(r+de)/d;				
				if (k<otherTo && ry>opoints[k].y){
					ok = false;					
				} else if (ry<oLast.y){
					dx = oLast.x-tx;
					dy = oLast.y;
					d = Math.sqrt(dx*dx+dy*dy);					
					ry = dy*(r-de)/d;
					if (j<to && ry>v[j].y) ok = false; 
				}						
			}			

			if (ok){
				if (end==null && ln>=0){
					end = v[ln];
					double dx = end.x-tx;
					double dy = end.y;
					double d = Math.sqrt(dx*dx+dy*dy);
					double t = (r+de)/d;
					double ry = dy*t;
					double rx = tx+dx*t;
					endO = new Vector2D(rx,ry);
				}

				if (oEnd==null && rn>=0){
					oEnd = opoints[rn];
					double dx = oEnd.x-tx;
					double dy = oEnd.y;
					double d = Math.sqrt(dx*dx+dy*dy);
					double t = (r-de)/d;
					double ry = dy*t;
					double rx = tx+dx*t;
					oEndO = new Vector2D(rx,ry);
				}
				if (end!=null && oEnd!=null){				
					if (endO.y>oEnd.y){
						while (endO.y>oEnd.y){
							int jj = 0;
							for (jj=rn+1;jj<otherTo;++jj){
								if (opoints[jj].y>endO.y) break;
							}
							if (jj==rn+1) break;
							ln--;
							if (ln<lIdx) break;
							end = v[ln];					
							double dx = end.x-tx;
							double dy = end.y;
							double d = Math.sqrt(dx*dx+dy*dy);	
							double t = (r+de)/d;
							double ry = dy*t;
							double rx = tx+dx*t;
							endO = new Vector2D(rx,ry);
						}
						lnum[i] = ln-lIdx+1;
					} else {
						while (oEndO.y>end.y){
							int jj = 0;
							for (jj=ln+1;jj<to;++jj){
								if (v[jj].y>oEndO.y) break;
							}
							if (jj==ln+1) break;
							rn--;
							if (rn<rIdx) break;
							oEnd = opoints[rn];					
							double dx = oEnd.x-tx;
							double dy = oEnd.y;
							double d = Math.sqrt(dx*dx+dy*dy);
							double t = (r-de)/d;
							double ry = dy*t;
							double rx = tx+dx*t;
							oEndO = new Vector2D(rx,ry);
						}
						rnum[i] = rn - rIdx+1;
					}
				} 

				if (edge!=null && r==edge.radius && rnum[i]+lnum[i]<1) 
					current = false;
				else if (edge!=null && r==edge.radius && (k-rIdx>=3 || j-lIdx>=3) && map[eR]==0) current = false;
				nums[i] = j+k-2-lIdx-rIdx;
			}
			if (ok && (nums[i]>0 || indx==1)){
				selections[sz++] = i;
				//				System.out.println(r+"    "+first+"    "+last+"   "+oFirst+"    "+oLast);
			}
		}		

		int idx = 0;		
		if (sz==1){
			int i = selections[0];
			idx = i;
			r = rads[i];			
		} else if (sz>0){
			int maxS = -1;
			int num = -1;			
			int er = 0;
			for (int i = 0;i<sz;++i){
				int j = selections[i];
				double rr = rads[j];
				int lNum = lnum[j];
				int rNum = rnum[j];
				int total = lNum+rNum+nums[j];
				int mr = (int)Math.round(rr);
				if (mr>=Segment.MAX_RADIUS) mr = Segment.MAX_RADIUS-1;
				if (er>=Segment.MAX_RADIUS) er = Segment.MAX_RADIUS-1;
				if (maxS<total){
					maxS = total;						
					r = rr;		
					idx = j;
					num = nums[j];
					er = mr;										
				} else if (maxS==total) {
					if (num<nums[j]){
						num = nums[j];
						r = rr;
						idx = j;
						er = mr;
					} else if (map[er]<map[mr] || (map[er]==map[mr] && rr==bestR)){
						r = rr;
						idx = j;
						er = mr;
					}
					//					if (edge!=null && edge.radius==rr){
					//						r = rr;
					//					};
				}
			}				
		}

		for (int i =0;i<indx;++i){
			int er = (int)Math.round(rads[i]);
			if (er>=Segment.MAX_RADIUS) er = Segment.MAX_RADIUS-1;
			if (er>=0) map[er] = 0;
		}

		int tp = turns[idx];

		//		System.out.println("Create array end time : "+(System.nanoTime()-ti)+" ms.");
		if (edge!=null && sz>0){
			int sr = (r>=Segment.MAX_RADIUS-1) ? Segment.MAX_RADIUS-1 : (int)(Math.round(r));

			if (sr==eR || (sr>=Segment.MAX_RADIUS-1 && edge.type==0)){
				if (edge.map!=null) {

					if (sr>=0) edge.map[sr]++;
				}
				if (sr==eR) r=edge.radius;
			} else {				
				if (edge.map!=null && edge.type!=Segment.UNKNOWN){
					if (edge.map[sr]==0){
						edge.appearedRads[edge.radCount++] = sr;
						edge.leftSeg.radCount = edge.radCount;
						edge.rightSeg.radCount = edge.radCount;
					}
					if (sr>=0) edge.map[sr]++;				
				}
				if (edge.map!=null && edge.type!=Segment.UNKNOWN && sr>=0 && eR>=0 && edge.map[sr]>=edge.map[eR]){
					//					if (edge.type!=0){
					//						if (r<Segment.MAX_RADIUS-1) 
					//							edge.radius = r;
					//						else edge.radius = 0;
					//						edge.type = tp;
					//					}
				} else if (edge.type!=0&& edge.type!=Segment.UNKNOWN && edge.end!=null && nraced<=edge.end.y && current){ 					
					r = edge.radius;
					tp = edge.type;
				}
			}						
		} else if (edge!=null && edge.type!=Segment.UNKNOWN && sz==0 && nraced<=edge.end.y && (current || eR>=0 && edge.map[eR]>3)){					
			r = edge.radius;
			tp = edge.type;
		} else if (sz==0 && edge!=null){
			Vector2D last = null;
			int li = 0;
			for (int i = 0;i<to;++i){
				last =  v[i];
				li = i;
				if (last!=null && last.y>0) break;
			}
			Vector2D first = (li>0) ? v[li-1] : null;
			if (first==null || (first.y<0 && last!=null && last.y>1)) first = leftMost;

			Vector2D oLast = null;
			int ri = 0;
			for (int i = 0;i<otherTo;++i){
				oLast =  opoints[i];
				ri = i;
				if (oLast!=null && oLast.y>0) break;
			}
			Vector2D oFirst = (ri>0) ? opoints[ri-1] : null;
			if (oFirst==null || (oFirst.y<0 && oLast!=null && oLast.y>1)) oFirst = rightMost;

			Vector2D lst = null;
			Vector2D fst = null;
			if (last!=null && oLast!=null && last.y-first.y>=1 && oLast.y-oFirst.y>=1){
				if (last.y>oLast.y){
					lst = oLast;
					fst = oFirst;
					if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.rightSeg.end.y) return edge.type*edge.radius;
				} else {
					lst = last;
					fst = first;
					if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.leftSeg.end.y) return edge.type*edge.radius;
				}
			} else if (last!=null && last.y-first.y>=1){
				lst = last;
				fst = first;
				if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.leftSeg.end.y) return edge.type*edge.radius;
			} else if (oLast!=null && oLast.y-oFirst.y>=1){
				lst = oLast;
				fst = oFirst;
				if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.rightSeg.end.y) return edge.type*edge.radius;
			} else {
				if (last==null || (oLast!=null && last.y<oLast.y)){
					lst = oLast;
					fst = oFirst;
					if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.rightSeg.end.y) return edge.type*edge.radius;
				} else {
					lst = last;
					fst = first;
					if (edge!=null && edge.type!=Segment.UNKNOWN && fst.y>edge.leftSeg.end.y) return edge.type*edge.radius;
				}
			}

			if (fst==null || lst==null){
				if (to>=2){
					fst = v[0];
					lst = v[li];
				} else {
					fst = opoints[0];
					lst = opoints[ri];
				}
			}


			double ox = (fst.x+lst.x)*0.5d;
			double oy = (fst.y+lst.y)*0.5d;
			double nx = fst.y - oy  ;
			double ny = ox - fst.x;

			Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, 1, 0, tmp);
			double dx = tmp[0]-toMiddle;
			double dy = tmp[1];
			double dd = Math.sqrt(dx*dx+dy*dy);
			r = Math.round(dd*100.0d)/100.0d;			
			double cx = tmp[0];
			double cy = tmp[1];
			double ax = fst.x-cx;
			double ay = fst.y-cy;
			double bx = lst.x-cx;
			double by = lst.y-cy;
			double angle = -Math.atan2(by, bx)+Math.atan2(ay, ax);			
			if (angle<-Math.PI) 
				angle += 2*Math.PI;
			else if (angle>Math.PI) 
				angle -= 2*Math.PI;

			tp = (angle<0) ? -1 : 1;		
			if (r>Segment.MAX_RADIUS-1 && edge.type!=Segment.UNKNOWN || r<=Segment.REJECT_VALUE || tp!=edge.type && lst.y-fst.y<2) {
				return edge.type*edge.radius;				
			}
		}


		//		Vector2D cnt = new Vector2D(cntrx[idx],cntry[idx]);
		////		Vector2D p1 = new Vector2D(-tW+toMiddle,0);
		//		Vector2D p1 = firstP[idx];
		//		Vector2D p2 = lastP[idx];
		//		for (int i = 1;i<to;++i){			
		//			Vector2D o = new Vector2D(0.5d*(p1.x+p2.x),0.5d*(p1.y+p2.y));
		//			Vector2D n = new Vector2D(o.x-p1.x,o.y-p1.y).orthogonal();
		//			double[] rr = new double[3];
		//			Geom.getLineLineIntersection(o.x, o.y, o.x+n.x, o.y+n.y, 0, 0, 1, 0, rr);
		//			double dx = rr[0]-p1.x;
		//			double dy = rr[1]-p1.y;
		//			double dd = Math.sqrt(dx*dx+dy*dy);
		//			double radius = Math.round(dd*10.0d)/10.0d;
		//			System.out.print((radius+tW)+"   ");
		//		}
		if (debug) System.out.println(sz+"     "+r+"   "+(System.nanoTime()-ti)/1000000);		

		return tp*r;
	}
	double AllowTime = 60;
	net.sourceforge.jFuzzyLogic.rule.Variable angle,dist,speedx,speedy,pos,steering,currentAngle,slipSpeed,turning;
	net.sourceforge.jFuzzyLogic.rule.Variable angleB,distB,speedxB,speedyB,posB,currentAngleB,slipSpeedB,difference,spaceRemain;

	Vector2D centerOfTurn = new Vector2D();
	int count = -1;
	double currentSpeedRadius;
	boolean detected = false;
	double distRaced = 0;
	Edge edge = null;
	boolean followedPath = false;
	FuzzyRuleSet fuzzyRuleSet,fuzzyRuleSetB;

	Vector2D highestPoint = null;
	Vector2D highestPointOnOtherEdge = null;

	double leftX;
	List<Vector2D> lhps = null;

	double mass = carmass;	
	double maxSpeed = 0;

	/*private static final double distance(Vector2D p1,Vector2D p2,Vector2D center,double radius){
		Vector2D v1 = new Vector2D(p1.x-center.x,p1.y-center.y);
		Vector2D v2 = new Vector2D(p2.x-center.x,p2.y-center.y);

		double angle = Vector2D.angle(v1, v2);
		if (angle<-Math.PI) 
			angle += 2*Math.PI;
		else if (angle>Math.PI) 
			angle -= 2*Math.PI;

		return radius*Math.abs(angle);
	}//*/


	/*private void checkVector(NewCarState cs,ObjectArrayList<TrackSegment> oal){
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
	}//*/

	double middleX;


	double mu = 0;

	double nextRadius;

	//	private final static int isCut(double ox,double oy,double radius,double startX,double startY,double endX,double endY,int fromSeg, int toSeg,double[] tmp){
	//		return isCut(ox, oy, radius, startX, startY, endX, endY, fromSeg, toSeg, tmp,W);
	//	}

	Vector2D oldPoint = null;


	Vector2D optimalPoint = new Vector2D();

	Edge other = null;

	double ox=0;

	double prevTW = 0;

	double raced = 0;

	double radiusL = 0;

	double radiusOfTurn=-1;

	double radiusR = 0;

	double rDist = -1;

	boolean recording = false;

	/*private static final int binarySearchFromTo(Segment[] list, double key, int from, int to) {			
		Segment val = list[from];
		int cmp = (val.end!=null && val.start!=null && key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (val.end!=null && key>val.end.y) ? -1 : 1; 
		if (cmp==0) 
			return from;
		else if (cmp>0) return -from-1;
		val = list[to];
		cmp = (val.end!=null && val.start!=null && key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (val.end!=null && key>val.end.y) ? -1 : 1;
		if (cmp==0) 
			return to;
		else if (cmp<0) return -(to+2);
		while (from <= to) {
			int mid =(from + to)/2;
			val = list[mid];
			cmp = (val.end!=null && val.start!=null && key<=val.end.y+SMALL_MARGIN && key>=val.start.y-SMALL_MARGIN) ? 0 : (val.end!=null && key>val.end.y) ? -1 : 1;

			if (cmp < 0) from = mid + 1;
			else if (cmp > 0) to = mid - 1;
			else return mid; // key found
		}
		return -(from + 1);  // key not found.
	}//*/

	double rightX;



	Segment seg = null;

	String str = "";

	//	private static Vector2D[] backArr = new Vector2D[1000];
	int stuck =0;



	/*private static final boolean checkFirst(Vector2D[] points,int from,int to,double r,ObjectArrayList<Vector2D> sE){		
		double[] rr = new double[3];		
		int index = 0;
		Vector2D p1 = points[from+index++];
		Vector2D p2 = points[from+index++];
		Vector2D p3 = points[from+index++];
		int num = to - from;
		if (num>=3){
			boolean isCircle = Geom.getCircle(p1,p2,p3, rr);
			if (!isCircle) 
				return false;
			else {
				double rad = Math.sqrt(rr[2]);
				double diff = Math.abs(rad-r);				
				if (diff>1.5 || Math.abs(rr[1])>=10*TrackSegment.EPSILON) 
					return false;

				if (num>=4){					
					double nrad = Geom.estimateCircle(points, from+1, to, rr);					
					if (Math.abs(nrad-r)>1 || Math.abs(rr[1])>=10*TrackSegment.EPSILON) return false;
				}

				if (num>=5){
					double nrad = Geom.estimateCircle(points, from+1, to, rr);
					if (Math.abs(nrad-r)>1 || Math.abs(rr[1])>=10*TrackSegment.EPSILON) return false;
				}
			}			
		}
		return true;
	}//*/

	/*private final void calculateRadius(Vector2D[] left, int sL,Vector2D[] right,int sR){
		for (int i = 0;i<sL-2;++i){
			Vector2D p1 = left[i];
			double x1 = p1.x;
			double y1 = p1.y;			
			for (int k = i+2;k<sL;++k){
				Vector2D p3 = left[k];
				maxAppearL[i][k] = 0;
				bestFitRadL[i][k] = 0;
				bestIntFitRadL[i][k] = 0;
				int[] map = Segment.tmpMap;
				int[] appear = Segment.tmpAppear;
				int max = 0;
				int count = 0;
				for (int j = i+1;j<k;++j){
					Vector2D p2 = left[j];
					double x2 = p2.x;
					double y2 = p2.y;				

					double x3 = p3.x;
					double y3 = p3.y;

					double ax = x2 - x1;  // first compute vectors
					double ay = y2 - y1;  // a and c
					double cx = x1 - x3;
					double cy = y1 - y3;

					double aPerpDOTc = ax * cy - ay * cx;

					if (aPerpDOTc == 0){
						allRadiusLeft[i][j][k] = -1;
						continue;
					}

					double bx = x3 - x2;
					double by = y3 - y2;
					double bDOTc = bx * cx + by * cy;

					double qo = bDOTc / aPerpDOTc;
					double sy = y1 + (ay + qo * ax) / 2.0d; // (sx, sy)					
					double sx = x1 + (ax - qo * ay) / 2.0d; // found center of circle										
					double dx = x1 - sx;
					double dy = y1 - sy;
					double r = Math.sqrt(dx * dx + dy * dy);
					allRadiusLeft[i][j][k] = (r>=Segment.MAX_RADIUS-1) ? Segment.MAX_RADIUS-1 : r;
					allCntrxL[i][j][k] = sx;
					allCntryL[i][j][k] = sy;
					int tp = 0;
					if (r<Segment.MAX_RADIUS-1){
						double ax0 = x1-sx;
						double ay0 = y1-sy;
						double bx0 = x2-sx;
						double by0 = y2-sy;
						double angle = -Math.atan2(by0, bx0)+Math.atan2(ay0, ax0);
//						angle = (-Math.PI*2+angle)%(Math.PI*2);
						if (angle<-Math.PI) 
							angle += 2*Math.PI;
						else if (angle>Math.PI) 
							angle -= 2*Math.PI;

						tp = (angle<0) ? -1 : 1;								
					}
					allTpL[i][j][k] = tp;
					int rr = (int)Math.round(r-tW);
					if (rr<=Segment.REJECT_VALUE) continue;
					if (tp<0) 
						rr = Segment.MAX_RADIUS+rr;
					else if (tp==0) rr = 0;
					if (map[rr]==0) appear[count++] = rr;
					map[rr]++;
					if (map[rr]>max){
						max = map[rr];
						if (rr%Segment.MAX_RADIUS!=bestIntFitRadL[i][k]){
							bestIntFitRadL[i][k] = rr % Segment.MAX_RADIUS;
							bestFitRadL[i][k] = (tp==0) ? 0 : r;
							bestTpL[i][k] = tp;
						} 
					}					
				}//enf of for j

				maxAppearL[i][k] = max;
				if (bestTpL[i][k]!=0){
					int rr = bestIntFitRadL[i][k];
					int tp = bestTpL[i][k];
					if (max>1){
						double sr = 0;
						for (int j = i+1;j<k;++j){
							if (tp==allTpL[i][j][k] && Math.round(allRadiusLeft[i][j][k]-tW) ==rr){
								sr += allRadiusLeft[i][j][k];
							}
						}
						bestFitRadL[i][k] = sr/max;
					}
				}

				for (int j = count-1;j>=0;--j){
					int rr = appear[j];
					map[rr] = 0;					
				}
			}
		}

		for (int i = 0;i<sR-2;++i){
			Vector2D p1 = right[i];
			double x1 = p1.x;
			double y1 = p1.y;
			for (int k = i+2;k<sR;++k){
				Vector2D p3 = right[k];
				maxAppearR[i][k] = 0;
				bestFitRadR[i][k] = 0;
				bestIntFitRadR[i][k] = 0;
				int[] map = Segment.tmpMap;
				int[] appear = Segment.tmpAppear;
				int max = 0;
				int count = 0;
				for (int j = i+1;j<k;++j){
					Vector2D p2 = right[j];
					double x2 = p2.x;
					double y2 = p2.y;								
					double x3 = p3.x;
					double y3 = p3.y;

					double ax = x2 - x1;  // first compute vectors
					double ay = y2 - y1;  // a and c
					double cx = x1 - x3;
					double cy = y1 - y3;

					double aPerpDOTc = ax * cy - ay * cx;

					if (aPerpDOTc == 0){
						allRadiusRight[i][j][k] = -1;
						continue;
					}

					double bx = x3 - x2;
					double by = y3 - y2;
					double bDOTc = bx * cx + by * cy;

					double qo = bDOTc / aPerpDOTc;
					double sy = y1 + (ay + qo * ax) / 2.0d; // (sx, sy)					
					double sx = x1 + (ax - qo * ay) / 2.0d; // found center of circle										
					double dx = x1 - sx;
					double dy = y1 - sy;
					double r = Math.sqrt(dx * dx + dy * dy);
					allRadiusRight[i][j][k] = (r>=Segment.MAX_RADIUS-1) ? Segment.MAX_RADIUS-1 : r;
					allCntrxR[i][j][k] = sx;
					allCntryR[i][j][k] = sy;
					int tp = 0;
					if (r<Segment.MAX_RADIUS-1){
						double ax0 = x1-sx;
						double ay0 = y1-sy;
						double bx0 = x2-sx;
						double by0 = y2-sy;
						double angle = -Math.atan2(by0, bx0)+Math.atan2(ay0, ax0);
//						angle = (-Math.PI*2+angle)%(Math.PI*2);
						if (angle<-Math.PI) 
							angle += 2*Math.PI;
						else if (angle>Math.PI) 
							angle -= 2*Math.PI;

						tp = (angle<0) ? -1 : 1;								
					}
					allTpR[i][j][k] = tp;
					int rr = (int)Math.round(r-tW);
					if (rr<=Segment.REJECT_VALUE) continue;
					if (tp<0) 
						rr = Segment.MAX_RADIUS+rr;
					else if (tp==0) rr = 0;
					if (map[rr]==0) appear[count++] = rr;
					map[rr]++;
					if (map[rr]>max){
						max = map[rr];
						if (rr%Segment.MAX_RADIUS!=bestIntFitRadR[i][k]){
							bestIntFitRadR[i][k] = rr % Segment.MAX_RADIUS;
							bestFitRadR[i][k] = (tp==0) ? 0 : r;
							bestTpR[i][k] = tp;
						} 
					}					
				}//enf of for j

				maxAppearR[i][k] = max;
				if (bestTpR[i][k]!=0){
					int rr = bestIntFitRadR[i][k];
					int tp = bestTpR[i][k];
					if (max>1){
						double sr = 0;
						for (int j = i+1;j<k;++j){
							if (tp==allTpR[i][j][k] && Math.round(allRadiusRight[i][j][k]-tW) ==rr){
								sr += allRadiusRight[i][j][k];
							}
						}
						bestFitRadR[i][k] = sr/max;
					}
				}


				for (int j = count-1;j>=0;--j){
					int rr = appear[j];
					map[rr] = 0;					
				}
			}
		}		
	}//*/

	double targetRadius = 	220;

	int tIndex = -1;

	double workingWidth = 0;

	/**
	 * 
	 */
	double x =0 ;

	double y = 0;


	CircleDriver2() {
		// TODO Auto-generated constructor stub
		//		ignoredExisting = true;
		//		storeNewState = false;
		int i = 0;
		int j = 0;
		lMap.setWhich(-1);
		rMap.setWhich(1);
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

		track = ObjectArrayList.wrap(new Segment[1000], 0);

		//		mapLS.size(0);
		//		mapLE.size(0);
		//		mapRS.size(0);
		//		mapRE.size(0);
		//		trackData.size(0);
		//		track.size(0);
		//		trackMid.size(0);
		//		l.size(0);
		//		r.size(0);
		//		sRight.size(0);
		//		sLeft.size(0);
		//		strt.size(0);
		//		lTmp.size(0);
		//		tE.size(0);
		//		tr.size(0);
		//		lm.size(0);
		//		rm.size(0);
		//		EdgeDetector.newLeft.size(0);
		//		EdgeDetector.newRight.size(0);
		//		Segment.oal.size(0);
		//		Segment.ial.size(0);
		//		Segment.ial1.size(0);		
		if (debug) System.out.println(""+l+r+trackData+trackMid+trackE+lTmp+tE+lm+rm+track+temp+MyLMA.partialDerivative+MyLMA.dy+ol);		
		track.listIterator();
		track.subList(0, 0);
		trackData.listIterator();		
		trackData.subList(0, 0);
		//		tr.listIterator();
		//		tr.subList(0, 0);		
		l.subList(0, 0);
		r.subList(0, 0);				
	}

	//	private static final double radiusAtSpeed(double speedkmh){
	//		if (speedkmh<=205)
	//			return 0.0820619978719424+0.00541730524763449*speedkmh+0.00464210551818003*speedkmh*speedkmh-1.20686439387299e-05*speedkmh*speedkmh*speedkmh; 
	//		if (speedkmh<274)
	//			return -1192.18464416069+16.6397488904336*speedkmh-0.0740555508240852*speedkmh*speedkmh+0.000114411488519385*speedkmh*speedkmh*speedkmh;
	//		if (speedkmh<285)
	//			return 125181.064069584-1325.79717496569*speedkmh+4.67785276024091*speedkmh*speedkmh-0.00549045392467163*speedkmh*speedkmh*speedkmh;			
	//
	//		return -83508.7895089672+887.064242540164*speedkmh-3.14306813772447*speedkmh*speedkmh+0.00372277142586774*speedkmh*speedkmh*speedkmh;
	//	}

	//	private static final double speedAtRadius(double x){
	//		if (x>=500) return Double.MAX_VALUE;
	//		if (x>=170)
	//			return 85.5247084519036+2.01905340962707*x-0.00646416884272083*x*x+7.29293433335612e-06*x*x*x;
	//		if (x>=100)
	//			return -139.760581424782+6.04756169365199*x-0.0303044500612258*x*x+5.3961109358751e-05*x*x*x;
	//		if (x>=60)
	//			return 202.745158728743-4.48739317463141*x+0.0755898471248578*x*x-0.00029146122705404*x*x*x;
	//		if (x>=20)
	//			return 26.7657503764833+2.55982229567786*x-0.0171415320257696*x*x+0.000108671654261182*x*x*x;		
	//		return -0.00728601622351035+7.7247995418915*x+-0.360701254346654*x*x+0.00788103445639814*x*x*x;		
	//	}

	private final void combine(Segment first,Vector2D[] left, int sL,Vector2D[] right,int sR){
		long ti = (debug) ? System.nanoTime() : 0;			
		//		calculateRadius(left, sL, right, sR);		
		double oldRad = (first==null) ? 0 : first.radius;
		int oldTp = (first==null) ? 0 : first.type;
		Segment lseg = (first ==null) ? null : first.leftSeg;
		boolean okay = lseg==null || lseg.startIndex==0 ||lseg.points[lseg.startIndex-1].y<0;
		Segment rseg = (first ==null) ? null : first.rightSeg;
		okay = okay && (rseg==null || rseg.startIndex==0 || rseg.points[rseg.startIndex-1].y<0);		
		double firstRad = (okay && first!=null && first.type!=Segment.UNKNOWN && first.type!=0 && first.center!=null && first.center.y==0 && first.radius<=Segment.MAX_RADIUS-1 &&  first.radius>=0 && first.map[(int)Math.round(first.radius)]>3 && first.end.y>1) ? first.type*first.radius : verify(left,sL, right, sR, first, -1, toMiddle, tW,nraced);
		long endTime = (debug) ? (System.nanoTime()-ti)/1000000 : 0;
		if (CircleDriver2.debug && endTime>=1) System.out.println("End verify : "+endTime+"   at "+CircleDriver2.time+" s.    ");
		int tp = (firstRad<0) ? -1 : (firstRad==0 || firstRad>=Segment.MAX_RADIUS-1) ? 0 : 1;
		firstRad = (tp<0) ? -firstRad : firstRad;				
		double rrL = 0;
		double rrR = 0;
		l0.type = Segment.UNKNOWN;
		r0.type = Segment.UNKNOWN;
		Segment snd = (trSz>1) ? trArr[ trIndx[1] ] : null;
		boolean checkFirst = false;
		boolean isFirstSeg = first!=null && isFirstSeg(first);

		double tx = 0;
		double ty = 0;
		double rA = 0;
		double rAx = 0;
		double rAy = 0;
		double rB = 0;
		double rBx = 0;
		double rBy = 0;

		Vector2D ph = (prevEdge==null) ? null : prevEdge.highestPoint;				
		double phl = (ph==null) ? EdgeDetector.MAX_DISTANCE : 0;
		if (ph!=null){
			double px = ph.x;
			double py = ph.y;
			phl = Math.sqrt(px*px+py*py);
		}
		if (first==null){			
			prevEdge.highestPoint = null;
			ph = null;
			phl = EdgeDetector.MAX_DISTANCE;
			first = trArr[ 0 ];			
			getFirstSegment(left, sL, right, sR, tp, toMiddle, firstRad, l0, r0);
			if (l0.type!=Segment.UNKNOWN && r0.type!=Segment.UNKNOWN) {
				trIndx[0] = 0;
				occupied[0] = 1;
				createSegment(l0, r0, first);
				trSz = 1;				
				checkFirst = true;
			} else {
				int sI = (int)firstRad-5;
				int eI = sI+10;
				int step = 1;				
				for (int i = sI;i!=eI;i+=step){
					firstRad = i;
					getFirstSegment(left, sL, right, sR, tp, toMiddle, firstRad, l0, r0);
					if (l0.type==Segment.UNKNOWN || r0.type==Segment.UNKNOWN) 
						continue;
					else break;
				}
				if (l0.type!=Segment.UNKNOWN && r0.type!=Segment.UNKNOWN) {
					trIndx[0] = 0;
					occupied[0] = 1;
					createSegment(l0, r0, first);
					trSz = 1;					
					checkFirst = true;
				}
			}
		} else if (trSz>0){										
			Vector2D center =  null;
			double toMiddle = Math.round((-tW*edgeDetector.curPos)*PRECISION)/PRECISION;
			double prevToMiddle = Math.round((-prevTW*prevEdge.curPos)*PRECISION)/PRECISION;
			double ax = Math.round((toMiddle-prevToMiddle)*PRECISION)/PRECISION;			
			if (tp==0 && Math.abs(first.end.x-first.start.x)>TrackSegment.EPSILON*0.1){
				int tpe = first.end.x-first.start.x>0 ? 1 : -1;
				getFirstSegment(left, sL, right, sR, tpe, toMiddle, firstRad, l0, r0);
			} else getFirstSegment(left, sL, right, sR, tp, toMiddle, firstRad, l0, r0);
			if (l0.type==Segment.UNKNOWN || r0.type==Segment.UNKNOWN) {// || (l0.num<3 && r0.num<3 && first.radius!=firstRad && first.end.y>=nraced)
				if (firstRad!=first.radius  && tp!=0){											
					int sr = (firstRad>=Segment.MAX_RADIUS-1 || tp==0) ? Segment.MAX_RADIUS-1 : (int)Math.round(firstRad);
					if (first.map!=null){						
						if (first.map[sr]==0){
							first.appearedRads[first.radCount++] = sr;
							first.leftSeg.radCount = first.radCount;
							first.rightSeg.radCount = first.radCount;
						}
						first.map[sr]++;
					}
					getFirstSegment(left, sL, right, sR, oldTp, toMiddle, oldRad, l0, r0);
					if (l0.type==Segment.UNKNOWN ||r0.type==Segment.UNKNOWN){						
						if (snd!=null && snd.type==tp){
							getFirstSegment(left, sL, right, sR, snd.type, toMiddle, snd.radius, l0, r0);								
						}
						if (l0.type==Segment.UNKNOWN ||r0.type==Segment.UNKNOWN){
							if (oldTp==0 && snd!=null && snd.type!=Segment.UNKNOWN) {
								firstRad = snd.radius;
								tp = snd.type;
							} else if (oldTp==0 && !inTurn){
								firstRad = oldRad;
								tp = 0;
								inTurn = false;
								return;
							}

							int sI = (int)firstRad-5;
							int eI = sI+10;
							int step = 1;
							if (snd!=null && snd.type!=Segment.UNKNOWN){
								if (snd.radius>firstRad && snd.radius<firstRad+10){
									step = -1;
									eI = sI;
									sI = eI + 20;
								}
							}
							for (int i = sI;i!=eI;i+=step){
								firstRad = i;
								getFirstSegment(left, sL, right, sR, tp, toMiddle, firstRad, l0, r0);
								if (l0.type==Segment.UNKNOWN || r0.type==Segment.UNKNOWN) 
									continue;
								else break;
							}
						}							
						if (l0.type==Segment.UNKNOWN ||r0.type==Segment.UNKNOWN){
							first.radius = oldRad;
							firstRad = oldRad;
						}
					} else if (oldTp==0 && Math.abs(first.end.x-first.start.x)<E){
						inTurn = false;
						return;
					}
				} 

				if (first.type!=0 && firstRad==first.radius){					
					tp = first.type;
					double rotate = (first.type!=0 && first.center!=null) ? first.type*Math.round(nraced*PRECISION/firstRad)/PRECISION : 0;										
					for (int i = trSz-1;i>=0;--i){
						Segment t = trArr[ trIndx[i] ];
						Segment l = t.leftSeg;
						Segment r = t.rightSeg;
						l.translate(ax, 0);
						if (rotate!=0) l.rotate(rotate, first.center.x, first.center.y);
						r.translate(ax, 0);
						if (rotate!=0) r.rotate(rotate, first.center.x, first.center.y);
					}
					if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
						ph.x+=ax;
						if (rotate!=0) ph.rotate(rotate, first.center.x, first.center.y);																	
					}
					checkFirst = true;					

					Segment lSeg = first.leftSeg;
					Segment rSeg = first.rightSeg;
					if (lSeg.end.y<0 || rSeg.end.y<0){
						lSeg.end = new Vector2D(leftMost);
						rSeg.end = new Vector2D(rightMost);
					}
					boolean fst = true;
					if (sL>0){
						for (int i = 0;i<sL;++i){
							Vector2D vv = left[i];
							if (fst && vv.y>=lSeg.start.y-SMALL_MARGIN && vv.y<=lSeg.end.y+SMALL_MARGIN){								
								fst = false;
								lSeg.startIndex = i;								
							}

							if (vv.y>lSeg.end.y+SMALL_MARGIN){
								if (fst) lSeg.startIndex = i;
								lSeg.endIndex = i-1;
								break;
							} else if (i==sL-1){
								if (fst){
									lSeg.startIndex = sL;
									lSeg.endIndex = i;
								} else lSeg.endIndex = i;
								break;
							}							
						}
						lSeg.num = lSeg.endIndex+1-lSeg.startIndex;
					} else {
						lSeg.startIndex = 0;
						lSeg.endIndex = -1;
						lSeg.num = 0;
					}


					fst = true;
					if (sR>0){
						for (int i = 0;i<sR;++i){
							Vector2D vv = right[i];
							if (fst && vv.y>=rSeg.start.y-SMALL_MARGIN && vv.y<=rSeg.end.y+SMALL_MARGIN){								
								fst = false;
								rSeg.startIndex = i;								
							}
							if (vv.y>rSeg.end.y+SMALL_MARGIN){
								if (fst) rSeg.startIndex = i;
								rSeg.endIndex = i-1;
								break;
							} else if (i==sR-1){
								if (fst){
									rSeg.startIndex = sR;
									rSeg.endIndex = i;
								} else rSeg.endIndex = i;
								break;
							}
						}
						rSeg.num = rSeg.endIndex+1-rSeg.startIndex;
					} else {
						rSeg.startIndex = 0;
						rSeg.endIndex = -1;
						rSeg.num = 0;
					}

					double ttx = (tp==-1) ? toMiddle-first.radius : first.radius+toMiddle;
					if (lSeg.type!=0 && isFirstSeg){
						if (lSeg.center!=null){
							lSeg.center.x = ttx;
							lSeg.center.y = 0;
						} else lSeg.center = new Vector2D(ttx,0);
						if (rSeg.center!=null){
							rSeg.center.x = ttx;
							rSeg.center.y = 0;
						} else rSeg.center = new Vector2D(ttx,0);
						first.center.x = ttx;
						first.center.y = 0;
					}
					if (l0.type==Segment.UNKNOWN || r0.type==Segment.UNKNOWN){
						l0.copy(first.leftSeg);
						r0.copy(first.rightSeg);
					}
				}							
			}
			//				Segment l0 = (ll!=null && ll.size()>0) ? ll.get(0) : null;
			rrL = (l0==null || l0.type==0 || l0.type==Segment.UNKNOWN) ? 0 : l0.radius-l0.type*tW;
			//				Segment r0 = (rrr!=null && rrr.size()>0) ? rrr.get(0) : null;
			rrR = (r0==null || r0.type==0 || r0.type==Segment.UNKNOWN) ? 0 : r0.radius+r0.type*tW;
			if (debug) System.out.println("Here and then "+(System.nanoTime()-ti)/1000000);						

			double ly = (l0!=null && l0.type!=0 && l0.center!=null) ? l0.center.y : 0;
			if (ly<0) ly = -ly;
			double ry = (r0!=null && r0.type!=0 && r0.center!=null) ? r0.center.y : 0;
			if (ry<0) ry = -ry;			

			if (!checkFirst){
				if (first!=null && first.type!=0 && first.center!=null && (first.center.y!=0 || first.end.y<=nraced)&& ((l0!=null && l0.type==0 && l0.start!=null && r0.start!=null && Math.abs(l0.start.x-l0.end.x)<0.1*TrackSegment.EPSILON) || (r0!=null && r0.type==0 && r0.start!=null && r0.end!=null && Math.abs(r0.start.x-r0.end.x)<0.1*TrackSegment.EPSILON))){
					center = first.center;				 				
					double angle = 0;
					int sndIndx = 1;
					double dist_to_end = 0;
					if (snd!=null && snd.type==0){
						if (trSz==2){
							trSz--;
							occupied[ trIndx[trSz] ] =0;														
							createSegment(l0, r0, first);
							checkFirst = true;							
							trSz = Segment.reUpdate(trArr,trSz, tW);													
							return;
						}
						double dy = snd.end.y - snd.start.y;
						double dx = snd.end.x - snd.start.x;
						angle = Math.atan2(dy, dx);
						if (angle<-Math.PI) 
							angle += 2*Math.PI;
						else if (angle>Math.PI) 
							angle -= 2*Math.PI;						
						angle = first.type*Math.round((PI_2- angle)*PRECISION)/PRECISION;		
						dist_to_end = (first==null || first.center==null) ? 0 : Math.round(first.radius*angle*PRECISION)/PRECISION;
					} else {
						for (int i = 1;i<trSz;++i){
							Segment t = trArr[ trIndx[i] ];
							if (t.type!=0) continue;
							double dy = t.end.y - t.start.y;
							double dx = t.end.x - t.start.x;
							angle = Math.atan2(dy, dx);
							if (angle<-Math.PI) 
								angle += 2*Math.PI;
							else if (angle>Math.PI) 
								angle -= 2*Math.PI;						
							angle = first.type*Math.round((PI_2- angle)*PRECISION)/PRECISION;
							sndIndx = i;
							break;
						}						
						dist_to_end = (first==null || first.center==null) ? 0 : Math.round(first.radius*angle*PRECISION)/PRECISION;
						checkFirst = true;
						createSegment(l0, r0, first);
					}					
					if (dist_to_end<0){
						for (int i = 1;i<trSz;++i)
							occupied[ trIndx[i] ] = 0;
						trSz =1;
						createSegment(l0, r0, first);
						checkFirst = true;						
						trSz = Segment.reUpdate(trArr,trSz, tW);											
						return;
					}
					//					rot = AffineTransform.getRotateInstance(angle,center.x,center.y);
					boolean ok1 =(l0!=null && l0.type==0 && Math.abs(l0.start.x-l0.end.x)<0.1*TrackSegment.EPSILON);
					boolean ok2 = (r0!=null && r0.type==0 && Math.abs(r0.start.x-r0.end.x)<0.1*TrackSegment.EPSILON);
					double st = nraced-dist_to_end;					
					if (st<0){
						firstRad = first.radius;
						tp = first.type;
						st = 0;
						angle = tp*Math.round(nraced*PRECISION/firstRad)/PRECISION;
						//						rot = AffineTransform.getRotateInstance(angle,center.x,center.y);
						ok1 = false;
						ok2 = false;
					}
					//					translate = AffineTransform.getTranslateInstance(ax, -st);										
					for (int i =trSz-1;i>=0;--i){
						if (i==0 && checkFirst) continue;
						Segment lll = trArr[ trIndx[i] ];												
						Segment lSeg = lll.leftSeg;
						Segment rSeg = lll.rightSeg;
						if (angle!=0) {
							lSeg.rotate(angle,center.x,center.y);
							rSeg.rotate(angle,center.x,center.y);
						}
						lSeg.translate(ax, -st);																						
						rSeg.translate(ax, -st);																											
						int[] map = lll.map;
						for (int j = lll.radCount-1;j>=0;--j){
							int rr = lll.appearedRads[j];
							map[rr] = 0;
							lll.appearedRads[j] = 0;
						}
						int rr = (lll.type==0 || lll.radius>=Segment.MAX_RADIUS) ? Segment.MAX_RADIUS-1 : (int)Math.round(lll.radius);
						lll.map[rr] = 1;
						lll.appearedRads[0] = rr;
						lll.radCount = 1;
						lSeg.radCount = 1;
						rSeg.radCount = 1;

					}
					if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
						if (angle!=0) ph.rotate(angle,center.x,center.y);
						ph.x+=ax;
						ph.y-=st;																		
					}

					if (ok1 && ok2) {
						if (first.upper!=null){
							first.upper = null;
							if (snd!=null) snd.lower = null;
						}						
						for (int i = 0;i<sndIndx;++i)
							occupied[ trIndx[i] ] = 0;
						if ((trSz-=sndIndx)>0) {
							System.arraycopy(trIndx, sndIndx, trIndx, 0, trSz);
							checkFirst = false;
						}
					}
					first = (trSz>0) ? trArr[ trIndx[0] ] : null;
					if (first!=null){
						Segment lSeg = first.leftSeg;
						Segment rSeg = first.rightSeg;
						if (lSeg.end.y<0 || rSeg.end.y<0){
							lSeg.end = new Vector2D(leftMost);
							rSeg.end = new Vector2D(rightMost);
						}
					}
				} else if (first!=null && first.type==0){
					if (l0!=null && l0.type!=Segment.UNKNOWN && l0.end!=null && l0.start!=null && l0.end.y-l0.start.y>=1 && (l0.type==0 && Math.abs(l0.start.x-l0.end.x)<TrackSegment.EPSILON*0.1) || (l0.type==Segment.UNKNOWN && Math.abs(first.start.x-first.end.x)<=E)){
						inTurn = false;
						return;
					} else if (l0.type==0){
						if (first.upper!=null){
							first.upper = null;
							if (snd!=null) snd.lower = null;
						}				
						occupied[ trIndx[0] ] = 0;
						if (--trSz>0) System.arraycopy(trIndx, 1, trIndx, 0, trSz);
						checkFirst = false;
						inTurn = true;
						if (snd!=null && snd.type!=0 && snd.center!=null){
							double ttx = (snd.type==-1) ? toMiddle-snd.radius : snd.radius+toMiddle;

							double st = Math.round(snd.center.y*PRECISION)/PRECISION;
							double b = Math.round((nraced-st)*PRECISION)/PRECISION;
							first.end.y+=st;
							//							first.reCalLength();																										
							double rotate = Math.round(snd.type*b/snd.radius*PRECISION)/PRECISION;
							for (int i =trSz-1;i>=0;--i){
								if (i==0 && checkFirst) continue;
								Segment lll = trArr[ trIndx[i] ];								
								Segment lSeg = lll.leftSeg;
								Segment rSeg = lll.rightSeg;
								lSeg.translate(ax,-st);
								rSeg.translate(ax,-st);
								if (rotate!=0) {
									lSeg.rotate(rotate, ttx, 0);
									rSeg.rotate(rotate, ttx, 0);
								}								
								//								lSeg.transform(translate);
								//								lSeg.transform(rot);
								//								rSeg.transform(translate);
								//								rSeg.transform(rot);
								int[] map = lll.map;
								for (int j = lll.radCount-1;j>=0;--j){
									int rr = lll.appearedRads[j];
									map[rr] = 0;
									lll.appearedRads[j] = 0;
								}
								int rr = (lll.type==0 || lll.radius>=Segment.MAX_RADIUS) ? Segment.MAX_RADIUS-1 : (int)Math.round(lll.radius);
								lll.map[rr] = 1;
								lll.appearedRads[0] = rr;
								lll.radCount = 1;
								lSeg.radCount = 1;
								rSeg.radCount = 1;
							}

							if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
								ph.x+=ax;
								ph.y-=st;
								if (rotate!=0) ph.rotate(rotate, ttx, 0);
								//								translate.transform(ph, ph);
								//								rot.transform(ph, ph);								

							}							
							first = (trSz>0) ? trArr[ trIndx[0] ] : null;	
						}

					} else if (l0!=null && l0.type!=0 && l0.type!=Segment.UNKNOWN){					
						int oldRR = (snd==null || snd.type==Segment.UNKNOWN) ? 0 : (int)Math.round(snd.radius);			
						if (oldRR>Segment.MAX_RADIUS-1) oldRR = Segment.MAX_RADIUS-1;
						if (snd!=null && ((l0!=null && l0.type==snd.type && (l0.type==0 || Math.abs(rrL-snd.radius)<0.5)) || (r0!=null && r0.type==snd.type && (r0.type==0 || Math.abs(rrR-snd.radius)<0.5 ))) ){
							int rr = (int)Math.round(rrL);
							if (rrL!=snd.radius && l0.type!=0){								
								if (oldRR!=rr){
									int[] map = snd.map;
									if (map[rr]==0){
										snd.appearedRads[snd.radCount++] = rr;
										snd.leftSeg.radCount = snd.radCount;
										snd.rightSeg.radCount = snd.radCount;
									}
									map[rr] = map[oldRR];

								}
								snd.radius = rrL;
								snd.leftSeg.radius = l0.radius;
								snd.rightSeg.radius = rrL*2-l0.radius;
								Segment.circle(snd.start, snd.end, snd.center.x,snd.center.y, rrL,snd.center);
								snd.leftSeg.center = snd.center;
								snd.rightSeg.center = snd.center;
							}
						} else if (snd!=null){
							int rr = (int)Math.round(rrL);							
							int[] map = snd.map;
							if (map[rr]==0){
								snd.appearedRads[snd.radCount++] = rr;
								snd.leftSeg.radCount = snd.radCount;
								snd.rightSeg.radCount = snd.radCount;
							}
							map[rr]++;
							if (map[rr]>=map[oldRR]){
								snd.radius = rrR;
								snd.leftSeg.radius = rrL*2-r0.radius;
								snd.rightSeg.radius = r0.radius;
								if (snd.type!=0 && snd.center!=null) {									
									Segment.circle(snd.start, snd.end, snd.center.x,snd.center.y, rrL,snd.center);
								}
								snd.leftSeg.center = snd.center;
								snd.rightSeg.center = snd.center;
							}
						}
						if (snd!=null && snd.type!=0 && snd.center!=null){
							double ttx = (snd.type==-1) ? toMiddle-snd.radius : snd.radius+toMiddle;

							double st = Math.round(snd.center.y*PRECISION)/PRECISION;
							double b = Math.round((nraced-st)*PRECISION)/PRECISION;
							first.end.y+=st;
							//							first.reCalLength();																										
							double rotate = Math.round(snd.type*b/snd.radius*PRECISION)/PRECISION;

							//							translate = AffineTransform.getTranslateInstance(ax, -st);
							//							rot = AffineTransform.getRotateInstance(rotate, center.x, 0);
							if (first.upper!=null){
								first.upper = null;
								snd.lower = null;
							}				
							occupied[ trIndx[0] ] = 0;
							if (--trSz>0) {
								System.arraycopy(trIndx, 1, trIndx, 0, trSz);
								checkFirst = false;
							}


							for (int i =trSz-1;i>=0;--i){
								if (i==0 && checkFirst) continue;
								Segment lll = trArr[ trIndx[i] ];								
								Segment lSeg = lll.leftSeg;
								Segment rSeg = lll.rightSeg;
								lSeg.translate(ax,-st);
								rSeg.translate(ax,-st);
								if (rotate!=0) {
									lSeg.rotate(rotate, ttx, 0);
									rSeg.rotate(rotate, ttx, 0);
								}								
								//								lSeg.transform(translate);
								//								lSeg.transform(rot);
								//								rSeg.transform(translate);
								//								rSeg.transform(rot);
								int[] map = lll.map;
								for (int j = lll.radCount-1;j>=0;--j){
									int rr = lll.appearedRads[j];
									map[rr] = 0;
									lll.appearedRads[j] = 0;
								}
								int rr = (lll.type==0 || lll.radius>=Segment.MAX_RADIUS) ? Segment.MAX_RADIUS-1 : (int)Math.round(lll.radius);
								lll.map[rr] = 1;
								lll.appearedRads[0] = rr;
								lll.radCount = 1;
								lSeg.radCount = 1;
								rSeg.radCount = 1;
							}

							if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
								ph.x+=ax;
								ph.y-=st;
								if (rotate!=0) ph.rotate(rotate, ttx, 0);
								//								translate.transform(ph, ph);
								//								rot.transform(ph, ph);								

							}							
							first = (trSz>0) ? trArr[ trIndx[0] ] : null;
							if (first!=null){
								if (first.center==null){ 
									first.center = new Vector2D(ttx,0);
								} else {
									first.center.x = ttx;
									first.center.y = 0;
								}
								Segment lSeg = first.leftSeg;
								if (lSeg.center==null){ 
									lSeg.center = new Vector2D(ttx,0);
								} else {
									lSeg.center.x = ttx;
									lSeg.center.y = 0;
								}
								Segment rSeg = first.rightSeg;
								if (rSeg.center==null){ 
									rSeg.center = new Vector2D(ttx,0);
								} else {
									rSeg.center.x = ttx;
									rSeg.center.y = 0;
								}
							}
						} else {
							//							for (int i = 0;i<trSz;++i)
							//								occupied[ trIndx[i] ] = 0;
							//							trSz = 1;
							//							trIndx[0] = 0;
							//							occupied[0] = 1;
							//							first = trArr[ trIndx[0] ];
							createSegment(l0, r0, first);							
							checkFirst = true;
						}
						inTurn = true;
					}					
				} else if (first!=null && first.type!=0 && (first!=null && l0!=null && l0.type==first.type && Math.abs(rrL-first.radius)<0.5) || (first!=null && r0!=null && r0.type==first.type && Math.abs(rrR-first.radius)<0.5)){										
					inTurn = true;
					if (first.radius!=rrL){
						int oldRR = (int)Math.round(first.radius);
						int rr = (int)Math.round(rrL);
						if (oldRR!=rr){
							int[] map = first.map;
							if (map[rr]==0){
								first.appearedRads[first.radCount++] = rr;
								first.leftSeg.radCount = first.radCount;
								first.rightSeg.radCount = first.radCount;
							}
							map[rr] = map[oldRR];

						}
						first.leftSeg.radius = l0.radius;
						first.rightSeg.radius = r0.radius;
						first.radius = rrL;
						first.center.x = l0.center.x - ax;
						first.center.y = 0;
						first.leftSeg.center.x = first.center.x;
						first.leftSeg.center.y = 0;
						first.rightSeg.center.x = r0.center.x - ax;
						first.rightSeg.center.y = 0;						
					}
					double rotate = l0.type*Math.round(nraced*PRECISION/rrL)/PRECISION;
					rA = rotate;
					rAx = first.center.x;
					rAy = first.center.y;
					//					translate = AffineTransform.getTranslateInstance(ax, 0);									

					for (int i=trSz-1;i>=0;--i){
						if (i==0 && checkFirst) continue;
						Segment t = trArr[ trIndx[i] ];
						Segment lSeg = t.leftSeg;
						Segment rSeg = t.rightSeg;
						if (rA!=0){							
							lSeg.rotate(rA,rAx,rAy);
							rSeg.rotate(rA,rAx,rAy);																		
						}
						lSeg.translate(ax, 0);
						rSeg.translate(ax, 0);
						//						lSeg.transform(rot);
						//						lSeg.transform(translate);
						//						rSeg.transform(rot);
						//						rSeg.transform(translate);

					}

					if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
						if (rA!=0){
							ph.rotate(rA,rAx,rAy);							
						}
						ph.x+=ax;
						//						rot.transform(ph, ph);
						//						if (rot2!=null) rot2.transform(ph, ph);
						//						translate.transform(ph, ph);
					}

					first = (trSz>0) ? trArr[ trIndx[0] ] : null;					
					if (first!=null && first.type!=0){
						double ttx = (first.type==-1) ? toMiddle-first.radius : first.radius+toMiddle;
						Segment lSeg = first.leftSeg;
						Segment rSeg = first.rightSeg;
						lSeg.center.x = ttx;
						rSeg.center.x = ttx;
						first.center.x = ttx;
						first.center.y = 0;
						lSeg.center.y = 0;
						rSeg.center.y = 0;
						if (lSeg.end.y<0 || rSeg.end.y<0){
							lSeg.end = new Vector2D(leftMost);
							rSeg.end = new Vector2D(rightMost);
						}
					}


				} else if (first!=null && first.type!=0 && first.center!=null && trSz>1){								
					boolean isFirst = (first.center!=null && first.center.y==0 && first.end.y>nraced && tp==first.type) || (l0!=null && ((Math.abs(rrL-first.radius)<0.5 && l0.type==first.type)));
					double b = 0;					
					//					if (l0!=null && r0!=null && l0.type==Segment.UNKNOWN && r0.type==Segment.UNKNOWN && !isFirst){
					//						center = new Vector2D(Math.round((first.type*first.center.length()+ax)*PRECISION)/PRECISION,0);
					//						double distL = (l0==null) ? 0 : l0.start.distance(center)-first.type*tW;
					//						double distR = (r0==null) ? 0 : r0.start.distance(center)+first.type*tW;
					//						if (Math.abs(distL-first.radius)<1 && Math.abs(distR-first.radius)<1)
					//							isFirst = true;																
					//					}				

					double angle = 0;
					double dist_to_end = 0;
					double d2e = 0;
					//					if (!isFirst  && first.center!=null){						
					//						angle = Vector2D.angle(first.center.negated(), first.end.minus(first.center));
					//						if (angle<-Math.PI) 
					//							angle += Math.PI;
					//						else if (angle>Math.PI) 
					//							angle -= Math.PI;
					//	
					//						if (angle<=Math.PI && angle>=CircleDriver2.PI_2) angle = Math.PI - angle;
					//						angle = Math.round(Math.abs(angle)*PRECISION)/PRECISION;
					//						d2e = Math.round(first.radius*angle*PRECISION)/PRECISION;
					//					}							

					if (isFirst)	{
						if (first.type!=0 && rrL!=first.radius){
							int rr = (int)Math.round(first.radius);
							int rrl = (int)Math.round(rrL);
							if (l0.type!=first.type){ 
								first.map[rr]--;
								if (first.map[rr]==0){
									createSegment(l0, r0, first);
									checkFirst = true;
								}
							} else {
								if (first.map[rrl]==0){
									first.appearedRads[first.radCount++] = rrl;
									first.leftSeg.radCount = first.radCount;
									first.rightSeg.radCount = first.radCount;
								}
								first.map[rrl]++;
								if (rrl!=rr && first.map[rrl]>=first.map[rr]){
									first.radius = rrL;
									first.leftSeg.copy(l0);
									first.rightSeg.copy(r0);
									Segment.toMiddleSegment(l0, first, -1, tW);
									checkFirst = true;
								}
							}
						}
						double rotate = first.type*Math.round(nraced*PRECISION/first.radius)/PRECISION;
						//						if (rrL==rrR && rrL==first.radius) {
						//							center = (Math.abs(l0.center.y)<Math.abs(r0.center.y)) ? l0.center : r0.center;
						//						} else center = (Math.abs(rrL-first.radius)<0.5) ? Segment.circle(l0.points[0],l0.points[l0.num-1],l0.center,first.radius+l0.type*tW) 
						//								: (Math.abs(rrR-first.radius)<0.5) ? Segment.circle(r0.points[0],r0.points[r0.num-1],r0.center,first.radius-r0.type*tW) : (first.center==null) ? null : new Vector2D(first.center);

						center = first.center;
						if (!checkFirst && center!=null){
							//							translate = AffineTransform.getTranslateInstance(ax, 0);
							//							rot = AffineTransform.getRotateInstance(rotate, first.center.x, first.center.y);
							Segment lSeg = first.leftSeg;
							Segment rSeg = first.rightSeg;
							rA = rotate;
							rAx = first.center.x;
							rAy = first.center.y;
							lSeg.rotate(rA,rAx,rAy);
							rSeg.rotate(rA,rAx,rAy);
							tx = ax;
							ty = 0;
							lSeg.translate(ax, 0);
							rSeg.translate(ax, 0);
							//							lSeg.transform(rot);
							//							if (rot2!=null) lSeg.transform(rot2);
							//							lSeg.transform(translate);
							//							rSeg.transform(rot);
							//							if (rot2!=null) rSeg.transform(rot2);
							//							rSeg.transform(translate);
							checkFirst = true;

							if (lSeg.start.y>l0.start.y){
								createSegment(l0, r0, first);								
								rAx = first.center.x - ax;
								rAy = first.center.y;
								rA = rotate;
								//								rot = AffineTransform.getRotateInstance(rotate, center.x, center.y);
							} else {
								boolean fst = true;
								if (sL>0){
									for (int i = 0;i<sL;++i){
										Vector2D vv = left[i];
										if (fst && vv.y>=lSeg.start.y-SMALL_MARGIN && vv.y<=lSeg.end.y+SMALL_MARGIN){											
											fst = false;
											lSeg.startIndex = i;											
										}
										if (vv.y>lSeg.end.y+SMALL_MARGIN){
											if (fst) lSeg.startIndex = i;
											lSeg.endIndex = i-1;
											break;
										} else if (i==sL-1){
											if (fst){
												lSeg.startIndex = sL;
												lSeg.endIndex = i;
											} else lSeg.endIndex = i;
											break;
										}
									}
									lSeg.num = lSeg.endIndex+1-lSeg.startIndex;
								} else {
									lSeg.startIndex = 0;
									lSeg.endIndex = -1;
									lSeg.num = 0;
								}


								fst = true;
								if (sR>0){
									for (int i = 0;i<sR;++i){
										Vector2D vv = right[i];
										if (fst && vv.y>=rSeg.start.y-SMALL_MARGIN && vv.y<=rSeg.end.y+SMALL_MARGIN){											
											fst = false;
											rSeg.startIndex = i;											
										}
										if (vv.y>rSeg.end.y+SMALL_MARGIN){
											if (fst) rSeg.startIndex = i;
											rSeg.endIndex = i-1;
											break;
										} else if (i==sR-1){
											if (fst){
												rSeg.startIndex = sR;
												rSeg.endIndex = i;
											} else rSeg.endIndex = i;
											break;
										}
									}
									rSeg.num = rSeg.endIndex+1-rSeg.startIndex;
								} else {
									rSeg.startIndex = 0;
									rSeg.endIndex = -1;
									rSeg.num = 0;
								}								
								double ttx = (first.type==-1) ? toMiddle-first.radius : first.radius+toMiddle;
								lSeg.center.x = ttx;
								lSeg.center.y = 0;
								rSeg.center.x = ttx;
								rSeg.center.y = 0;
								first.center.x  = ttx;
								first.center.y = 0;
								l0.copy(lSeg);
								r0.copy(rSeg);
							}
						}
					} else if (first!=null && first.type!=0 && snd!=null && first.center!=null){							
						if ((l0!=null && l0.type==snd.type && (l0.type==0 || Math.abs(rrL-snd.radius)<0.5)) || (r0!=null && r0.type==snd.type && (r0.type==0 || Math.abs(rrR-snd.radius)<0.5 )) ){
							if (rrL!=snd.radius && l0.type!=0){
								snd.radius = rrL;
								snd.leftSeg.radius = l0.radius;
								snd.rightSeg.radius = r0.radius;
								Segment.circle(snd.start, snd.end, snd.center.x,snd.center.y, rrL,snd.center);
								snd.leftSeg.center = snd.center;
								snd.rightSeg.center = snd.center;
							}

							if (l0.type!=0){
								angle = Vector2D.angle(first.center.negated(), snd.center.minus(first.center));
								if (angle<-Math.PI) 
									angle += Math.PI;
								else if (angle>Math.PI) 
									angle -= Math.PI;

								if (angle<=Math.PI && angle>=CircleDriver2.PI_2) 
									angle = Math.PI - angle;						
								angle = Math.round(Math.abs(angle)*PRECISION)/PRECISION;								
								dist_to_end = Math.round(first.radius*angle*PRECISION)/PRECISION;
								if (dist_to_end>nraced){
									dist_to_end = nraced;									
									angle = Math.round(nraced*PRECISION/rrL)/PRECISION;
									createSegment(l0, r0, first);																
									first.center.x = l0.center.x - ax;
									first.center.y = 0;
									first.leftSeg.center.x = first.center.x;
									first.leftSeg.center.y = 0;
									first.rightSeg.center.x = r0.center.x - ax;
									first.rightSeg.center.y = 0;									
								}
							} else if (tp==0){
								double dy = snd.end.y - snd.start.y;
								double dx = snd.end.x - snd.start.x;
								angle = Math.atan2(dy, dx);
								if (angle<-Math.PI) 
									angle += 2*Math.PI;
								else if (angle>Math.PI) 
									angle -= 2*Math.PI;						
								angle = first.type*Math.round((PI_2- angle)*PRECISION)/PRECISION;					
							}
						} else {
							dist_to_end = d2e;
							checkFirst = true;
						}
						center = l0.center;						
						if (tp!=0) b = (dist_to_end!=0) ? Math.round(snd.type*(nraced - dist_to_end)/rrL*PRECISION)/PRECISION :
							Math.round(l0.type*(nraced - dist_to_end)/rrL*PRECISION)/PRECISION;
						tx = ax;
						ty = 0;
						rA = first.type*angle;
						rAx = first.center.x;
						rAy = first.center.y;
						if (b!=0){
							rB = b;
							rBx = center.x;
							rBy = center.y;
						}
						//						translate = AffineTransform.getTranslateInstance(ax, 0);							
						//						rot = AffineTransform.getRotateInstance(first.type*angle,first.center.x,first.center.y);
						//						if (b!=0) rot2 = AffineTransform.getRotateInstance(b, center.x, center.y);
						if (!checkFirst){ 
							if (first.upper!=null){
								first.upper = null;
								if (snd!=null) snd.lower = null;
							}						
							occupied[ trIndx[0] ] = 0;
							if (--trSz>0) System.arraycopy(trIndx, 1, trIndx, 0, trSz);
						} else {
							createSegment(l0, r0, first);						
						}
					}					
					for (int i = 0;i<trSz;++i){
						if (i==0 && checkFirst) continue;
						Segment t = trArr[ trIndx[i] ];						
						if (t!=null) {
							Segment lSeg = t.leftSeg;
							Segment rSeg = t.rightSeg;
							if (rA!=0){
								lSeg.rotate(rA,rAx,rAy);
								rSeg.rotate(rA,rAx,rAy);
							}
							lSeg.translate(tx, ty);
							rSeg.translate(tx, ty);
							if (rB!=0){
								lSeg.rotate(rB,rBx,rBy);
								rSeg.rotate(rB,rBx,rBy);
							}
							//							t.leftSeg.transform(rot);
							//							t.leftSeg.transform(translate);
							//							if (rot2!=null) t.leftSeg.transform(rot2);							
							//							t.rightSeg.transform(rot);
							//							t.rightSeg.transform(translate);
							//							if (rot2!=null) t.rightSeg.transform(rot2);							
						}
					}


					if (ph!=null && phl<EdgeDetector.MAX_DISTANCE) {
						if (rA!=0) ph.rotate(rA,rAx,rAy);
						ph.x+=tx;
						ph.y+=ty;
						if (rB!=0) ph.rotate(rB,rBx,rBy);
						//						if (rot2!=null) rot2.transform(ph, ph);						
					}

					first = (trSz>0) ? trArr[ trIndx[0] ] : null;
					if (first!=null && first.type!=0){
						if (first.leftSeg.center==null){
							first.leftSeg.center = new Vector2D(l0.center);
						} else if (l0.center!=null) first.leftSeg.center.copy(l0.center);
						if (first.rightSeg.center==null){
							first.rightSeg.center = new Vector2D(r0.center);
						} else if (r0.center!=null) first.rightSeg.center.copy(r0.center);
						//						first.center.copy(first.leftSeg.center);
						first.center = first.leftSeg.center;						
					}
				} else if (trSz<2 && first!=null && first.type!=0){
					createSegment(l0, r0, first);
					checkFirst = true;					
					for (int i = 1;i<trSz;++i)
						occupied[ trIndx[i] ] = 0;
					trSz = 1;											
				}
			}


			//			if (left!=null) createStartEndArray(left, 0, sL, tr,l, -1, tW);
			//			if (right!=null) createStartEndArray(right, 0, sR, tr,CircleDriver2.r, 1, tW);

			int jL = 0;
			int jR = 0;
			for (int i = 0;i<trSz;++i){
				if (i==0 && checkFirst) continue;
				Segment t = trArr[ trIndx[i] ];
				Segment l = t.leftSeg;
				Segment r = t.rightSeg;
				double my = l.start.y;		
				if (sL>0){
					for (;jL<sL;++jL){
						Vector2D p = left[jL];
						if (p.y>=my-SMALL_MARGIN)						
							break;					
					}
					l.startIndex = jL;
					my = l.end.y;
					for (;jL<sL;++jL){
						Vector2D p = left[jL];
						if (p.y>my+SMALL_MARGIN)						
							break;					
					}
					l.endIndex = jL-1;
					l.num = jL-l.startIndex;
				} else {
					l.startIndex = 0;
					l.endIndex = -1;
					l.num = 0;
				}


				my = r.start.y;		
				if (sR>0){
					for (;jR<sR;++jR){
						Vector2D p = right[jR];
						if (p.y>=my-SMALL_MARGIN)						
							break;					
					}
					r.startIndex = jR;
					my = r.end.y;
					for (;jR<sR;++jR){
						Vector2D p = right[jR];
						if (p.y>my+SMALL_MARGIN)						
							break;					
					}
					r.endIndex = jR-1;
					r.num = jR-r.startIndex;
				} else {
					r.startIndex = 0;
					r.endIndex = -1;
					r.num = 0;
				}
			}

			if (first!=null && first.type!=0 && first.type!=UNKNOWN && first.radius==rrL && rrL==rrR){
				Segment lSeg = first.leftSeg;
				Segment rSeg = first.rightSeg;				
				snd = (trSz>1) ? trArr[trIndx[1]] : null;
				Segment sndL = (snd==null) ? null : snd.leftSeg;
				Segment sndR = (snd==null) ? null : snd.rightSeg;
				//				lSeg.center = l0.center;
				//				rSeg.center = r0.center;

				double tw = tW+tW;
				//				boolean ok = false;
				if (tw<0) tw = -tw;
				if (l0.start!=null && lSeg.start.y>l0.start.y){
					lSeg.start = l0.start;
					lSeg.startIndex = l0.startIndex;
					lSeg.num = lSeg.endIndex-lSeg.startIndex+1;
					Segment.reSynchronize(lSeg, rSeg, 0, rSeg.endIndex+1, 1, tw);
				}
				if (r0.start!=null && rSeg.start.y>r0.start.y){
					rSeg.start = r0.start;
					rSeg.startIndex = r0.startIndex;
					rSeg.num = rSeg.endIndex-rSeg.startIndex+1;
					Segment.reSynchronize(rSeg, lSeg, 0, lSeg.endIndex+1, -1, tw);
				}
				if (l0.end!=null && lSeg.end.y<=l0.end.y){
					if (l0.num>1){
						if (lSeg.end.y!=l0.end.y){
							lSeg.end = l0.end;
							lSeg.endIndex = l0.endIndex;
							lSeg.num = lSeg.endIndex+1-lSeg.startIndex;						
							Segment.reSynchronize(lSeg, rSeg, rSeg.startIndex, r0.endIndex+1, 1, tw);
							//							ok = true;
						}
						while (sndL!=null && (sndL.type==Segment.UNKNOWN || sndL.start.y<=lSeg.end.y)){
							if (sndL.type != Segment.UNKNOWN && lSeg.end.y<sndL.end.y){
								sndL.startIndex = lSeg.endIndex+1;
								sndL.start = new Vector2D(left[sndL.startIndex]);
								sndL.num = sndL.endIndex+1-sndL.startIndex;
								Segment.reSynchronize(sndL, sndR, sndR.startIndex, sndR.endIndex+1, 1, tw);
								if (sndL.num<2 && sndR.num<2){
									sndL.type = Segment.UNKNOWN;
									sndR.type = Segment.UNKNOWN;
									occupied[ trIndx[1] ] = 0;									
									if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
									trSz++;
								}
								snd = (trSz<=1) ? null : trArr[ trIndx[1] ];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
								break;
							} else {
								occupied[ trIndx[1] ] = 0;									
								if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
								trSz++;
								snd = (trSz<=1) ? null : trArr[ trIndx[1] ];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
							}
						}										
					} else {						
						Vector2D lStart = l0.end;												
						if (lSeg.end.y<lStart.y) {
							lSeg.end = l0.end;
							lSeg.endIndex = l0.endIndex;
							lSeg.num = lSeg.endIndex+1-lSeg.startIndex;
							Segment.reSynchronize(lSeg, rSeg, rSeg.startIndex, r0.endIndex+1, 1, tw);
							//							ok = true;
						}														

						while (sndL!=null && (sndL.type==Segment.UNKNOWN || sndL.start.y<=lSeg.end.y)){
							if (sndL.type!=Segment.UNKNOWN && lSeg.end.y<sndL.end.y){
								sndL.startIndex = lSeg.endIndex+1;
								sndL.start = new Vector2D(left[sndL.startIndex]);
								sndL.num = sndL.endIndex+1-sndL.startIndex;
								Segment.reSynchronize(sndL, sndR, sndR.startIndex, sndR.endIndex+1, 1, tw);
								if (sndL.num<2 && sndR.num<2){
									sndL.type = Segment.UNKNOWN;
									sndR.type = Segment.UNKNOWN;
									occupied[ trIndx[1] ] = 0;									
									if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
									trSz++;
								}
								snd = (trSz<=1) ? null : trArr[trIndx[1]];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
								break;
							} else {
								occupied[ trIndx[1] ] = 0;									
								if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
								trSz++;
								snd = (trSz<=1) ? null : trArr[trIndx[1]];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
							}
						}						

					}
				}

				if (r0.end!=null && rSeg.end.y<=r0.end.y){
					if (r0.num>1){
						if (rSeg.end.y!=r0.end.y){
							rSeg.end = r0.end;
							rSeg.endIndex = r0.endIndex;
							rSeg.num = rSeg.endIndex+1-rSeg.startIndex;
							Segment.reSynchronize(rSeg, lSeg, lSeg.startIndex, l0.endIndex+1, -1, tw);
							//							ok = true;
						}						
						while (sndR!=null && (sndR.type==Segment.UNKNOWN || sndR.start.y<=rSeg.end.y)){
							if (sndR.type!=Segment.UNKNOWN && rSeg.end.y<sndR.end.y){
								sndR.startIndex = rSeg.endIndex+1;
								sndR.start = new Vector2D(right[sndR.startIndex]);
								sndR.num = sndR.endIndex+1-sndR.startIndex;
								Segment.reSynchronize(sndR, sndL, sndL.startIndex, sndL.endIndex+1, -1, tw);
								if (sndL.num<2 && sndR.num<2){
									sndL.type = Segment.UNKNOWN;
									sndR.type = Segment.UNKNOWN;
									occupied[ trIndx[1] ] = 0;									
									if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
									trSz++;
								}
								snd = (trSz<=1) ? null : trArr[trIndx[1]];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
								break;
							} else {
								occupied[ trIndx[1] ] = 0;									
								if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
								trSz++;
								snd = (trSz<=1) ? null : trArr[trIndx[1]];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
							}
						}
					} else {						
						Vector2D rStart = r0.end;						
						if (rSeg.end.y!=rStart.y) {
							rSeg.end = rStart;
							rSeg.endIndex = r0.endIndex;
							rSeg.num = rSeg.endIndex+1-rSeg.startIndex;
							Segment.reSynchronize(rSeg, lSeg, lSeg.startIndex, l0.endIndex+1, -1, tw);
							//							ok = true;
						}

						while (sndR!=null && (sndR.type==Segment.UNKNOWN || sndR.start.y<=rSeg.end.y)){
							if (sndR.type!=Segment.UNKNOWN && rSeg.end.y<sndR.end.y){
								sndR.startIndex = rSeg.endIndex+1;
								sndR.start = new Vector2D(right[sndR.startIndex]);
								sndR.num = sndR.endIndex+1-sndR.startIndex;
								Segment.reSynchronize(sndR, sndL, sndL.startIndex, sndL.endIndex+1, -1, tw);
								if (sndL.num<2 && sndR.num<2){
									sndL.type = Segment.UNKNOWN;
									sndR.type = Segment.UNKNOWN;
									occupied[ trIndx[1] ] = 0;									
									if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
									trSz++;
								}
								snd = (trSz<=1) ? null : trArr[ trIndx[1] ];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
								break;
							} else {
								occupied[ trIndx[1] ] = 0;									
								if ((trSz-=2)>0) System.arraycopy(trIndx, 2, trIndx, 1, trSz);
								trSz++;
								snd = (trSz<=1) ? null : trArr[trIndx[1]];
								sndL = (trSz<=1) ? null : snd.leftSeg;
								sndR = (trSz<=1) ? null : snd.rightSeg;
							}
						}						
					}
				}

			}								
		}		

//		Vector2D highest = (sL>0) ? left[sL-1] : null;		
		Vector2D highestPoint = edgeDetector.highestPoint;
		Vector2D highest = highestPoint;
//		if (sR>0){
//			if (highest==null || highest.y<right[sR-1].y) highest = right[sR-1];
//		}
//		if (highest==null || highestPoint!=null && highest.y<highestPoint.y) highest = highestPoint;
		boolean done = false;
		if (trSz>0){
			Segment lSeg = trArr[ trIndx[trSz-1] ].leftSeg;
			int numL = sL - lSeg.endIndex -1;
			if (highest==null || lSeg.end.y>highest.y) highest = lSeg.end;
			lSeg = trArr[trIndx[trSz-1]].rightSeg;
			int numR = sL - lSeg.endIndex -1;
			if (highest==null || lSeg.end.y>highest.y) highest = lSeg.end;

			if (numL>1 || numR>1){
				edgeDetector.lSize = sL;
				edgeDetector.rSize = sR;

				edgeDetector.double_check(highest);
				sL = edgeDetector.lSize;
				sR = edgeDetector.rSize;
				for (int i =trSz-1;i>=0;--i) {
					Segment mid = trArr[ trIndx[i] ];
					mid.updated = (inTurn) ? true : false;
					mid.leftSeg.done = false;
					mid.rightSeg.done = false;
					mid.leftSeg.updated = mid.updated;
					mid.rightSeg.updated = mid.updated;
				}

				if (inTurn) {
					trSz = Segment.reUpdate(trArr,trSz, tW);
					done = true;
					highest = highestPoint;
					if (trSz>0) {
						lSeg = trArr[ trIndx[trSz-1] ].leftSeg;					
						if (highest==null || lSeg.end.y>highest.y) highest = lSeg.end;
						lSeg = trArr[trIndx[trSz-1]].rightSeg;					
						if (highest==null || lSeg.end.y>highest.y) highest = lSeg.end;
					}
				}
			}

		}

		boolean added = false;		
		ph = prevEdge.highestPoint;		
		if (highestPoint!=null && highestPoint.length()<EdgeDetector.MAX_DISTANCE && edgeDetector.whichE==0 && trSz>0 && ph!=null){				
			int whichE = edgeDetector.whichE;				
			Vector2D lower = (highestPoint !=null && ph!=null && highestPoint.y<ph.y) ? highestPoint : (highestPoint==null || ph==null) ? null : ph;	
			int whichL = (highestPoint !=null && lower==highestPoint) ? whichE : (lower==null) ? 0 : prevEdge.whichE;
			Vector2D higher = (highestPoint !=null && ph!=null && highestPoint.y<ph.y) ? ph : (highestPoint==null) ? ph : highestPoint;
			int whichH = (highestPoint!=null && ph!=null && higher==ph) ? prevEdge.whichE : (highestPoint==null) ? prevEdge.whichE : whichE;
			double dd = ph!=null && highestPoint!=null ? ph.distance(highestPoint) : 0;
			if (turn!=0 && whichH==0 && whichL==0 && ph!=null && highestPoint!=null && ph.length()<99 && highestPoint.length()<99 && dd<trackWidth-EdgeDetector.CERTAIN_DIST && dd>0.5) {				
//				if (relativeAngleMovement>0) {
//					if (highestPoint.y>ph.y) {
//						whichH = whichL = (highestPoint.x-ph.x)*turn<0 ? -turn : 0;
//					} else whichH = whichL = (highestPoint.x-ph.x)*turn>0 ? turn : 0;
//				} else if (highestPoint.y<ph.y) {
//					whichH = whichL = (highestPoint.x-ph.x)*turn>0 ? turn : 0;
//				} else whichH = whichL = (highestPoint.x-ph.x)*turn<0 ? -turn : 0;
				if ((highestPoint.y-ph.y)*(highestPoint.x-ph.x)<0)
					whichH = whichL = 1;
				else whichH = whichL = -1;
				if (whichH!=0) turn = -whichH;
				edgeDetector.turn = turn;
				guessTurn = false;
			} else if (whichL==0 || whichH==0){
				EdgeDetector.guessHighestPointEdge(lower, whichL, higher, whichH, prevEdge.left, prevEdge.lSize, prevEdge.right,prevEdge.rSize, trackWidth,out);
				whichL = out[0];
				whichH = out[1];
			}
			sL = edgeDetector.lSize;
			sR = edgeDetector.rSize;
			if (whichH!=0 || whichL!=0) {					
				if (higher!=null && highestPoint==higher){				
					edgeDetector.whichE = whichH;
				} else if (lower!=null && highestPoint==lower){				
					edgeDetector.whichE = whichL;
				}
	
				whichE = edgeDetector.whichE;
				if (edgeDetector.whichEdgeAhead==0 && whichE!=0){
					if (edgeDetector.currentPointAhead.x==highestPoint.x && edgeDetector.currentPointAhead.y==highestPoint.y)
						edgeDetector.whichEdgeAhead = whichE;					
				}
				if (whichE!=0){
	
					if (whichE==-1 && left!=null){ 											
						sL = EdgeDetector.join(highestPoint, left, sL, trArr,trIndx,trSz,-1);
						added = true;
						//						calculateRadius(left, sL, right, sR);
					} else if (right!=null) {						
						sR = EdgeDetector.join(highestPoint, right, sR, trArr,trIndx,trSz,1);
						added = true;
						//						calculateRadius(left, sL, right, sR);
					}									
				}							
	
			} //end of if
		}

		edgeDetector.lSize = sL;
		edgeDetector.rSize = sR;

		if (highest!=null) edgeDetector.double_check(highest);
		sL = edgeDetector.lSize;
		sR = edgeDetector.rSize;
		for (int i =trSz-1;i>=0;--i) {
			Segment mid = trArr[ trIndx[i] ];
			mid.updated = (inTurn) ? true : false;
			mid.leftSeg.done = false;
			mid.rightSeg.done = false;
			mid.leftSeg.updated = mid.updated;
			mid.rightSeg.updated = mid.updated;
		}

		if (inTurn && (!done || added) ) trSz = Segment.reUpdate(trArr,trSz, tW);


		first = (trSz==0) ? null : trArr[trIndx[0] ];
		if (first!=null && first.type==0 && Math.abs(first.start.x-first.end.x)<EPSILON) inTurn = false;			
	}


	private final void display(){
		if (!draw) return;
		try{
			/*
			if (edge!=null && edge.size>edge.index+3){
				Vector2D p1 = edge.get(edge.index+1);
				Vector2D p2 = edge.get(edge.index+2);
				Vector2D p3 = edge.get(edge.index+3);
				double[] r = new double[3];
				Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
				r[2] = Math.sqrt(r[2]);
				QuickCircleFitter cf = new  QuickCircleFitter(new double[]{r[0],r[1]},edge.allPoints.elements(),edge.index+1,edge.size-1);
				cf.fit();
				Vector2D c = cf.getEstimatedCenter();
				double rr = cf.getEstimatedRadius();
				TrackSegment ts = TrackSegment.createTurnSeg(c.x, c.y, rr, c.x-rr, c.y, c.x, c.y-rr);
				trackE.add(ts);
			}*/

			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			TrackSegment.drawTrack(trackData,"E "+nf.format(time)+" a",true);
			
			
//			drawEstimate("E "+nf.format(time)+" b");
//			TrackSegment.drawTrack(trackE,"E "+nf.format(time)+" r");
			//			if (cntr!=null)TrackSegment.circle(cntr.x, cntr.y, rr, series);
			//			if (centerOfTurn!=null) TrackSegment.circle(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, series);
			//			EdgeDetector.drawEdge(series, "E "+nf.format(time)+" c");
			Thread.sleep(300);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static TrackSegment toTs(Segment t){
		if (t.type==0){
			return TrackSegment.createStraightSeg(0, t.start.x, t.start.y, t.end.x, t.end.y);			
		} else {
			return TrackSegment.createTurnSeg(t.center.x, t.center.y, t.radius, t.start.x, t.start.y, t.end.x, t.end.y);			
		}
	}
	
	public static TrackSegment toTs2(Segment t,double startX,double startY,double endX,double endY){
		if (t.type==0){
			return TrackSegment.createStraightSeg(0, startX,startY,endX,endY);			
		} else {
			return TrackSegment.createTurnSeg(t.center.x, t.center.y, t.radius, startX,startY,endX,endY);			
		}
	}
	
	public static void drawSeg(XYPlot xyPlot,Segment s,double startX,double startY,double endX,double endY,Stroke bs){
		if (s.type==0)
			TrackSegment.drawLine(xyPlot, startX, startY, endX+0.001, endY+0.001,bs);
		else TrackSegment.drawArc(xyPlot, s.center.x,s.center.y,s.radius, startX, startY, endX, endY,bs);
	}
	
	public static void drawSeg(XYPlot xyPlot,Segment s,double startX,double startY,double endX,double endY){
		if (s.type==0)
			TrackSegment.drawLine(xyPlot, startX, startY, endX, endY);
		else TrackSegment.drawArc(xyPlot, s.center.x,s.center.y,s.radius, startX, startY, endX, endY);
	}
	
	public static void drawEndSeg(XYPlot xyPlot,Segment s,Vector2D p,double len){
		double dx = 0;
		double dy = 0;
		double d= 0;
		len*=0.5;
		BasicStroke bs = new BasicStroke();
		if (s.type==0){
			dy = p.x-s.start.x;
			dx = p.y-s.start.y;
			d = Math.sqrt(dx*dx+dy*dy);			
			TrackSegment.drawLine(xyPlot, p.x+dx*len/d, p.y+dy*len/d, p.x+0.001-dx*len/d, p.y+0.001-dy*len/d,bs);			
		} else {
			dx = p.x-s.center.x;
			dy = p.y-s.center.y;
			d = Math.sqrt(dx*dx+dy*dy);
			TrackSegment.drawLine(xyPlot, p.x+dx*len/d, p.y+dy*len/d, p.x+0.001-dx*len/d, p.y+0.001-dy*len/d,bs);
		}
	}
	
	public static void drawUL(XYPlot xyPlot,Segment s){
		BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 
                1.0f, new float[] {2.0f, 10.0f}, 0.0f);
		if (s.upper!=null){
			drawSeg(xyPlot,s,s.end.x,s.end.y,s.upper.x,s.upper.y,bs);			
			drawEndSeg(xyPlot, s, s.upper, 1);
		}
		
		if (s.lower!=null){
			drawSeg(xyPlot,s,s.lower.x,s.lower.y,s.start.x,s.start.y,bs);
			drawEndSeg(xyPlot, s, s.lower, 1);
		}
////		 final CircleDrawer cd = new CircleDrawer(Color.red, new BasicStroke(1.0f), null);	       	       
//	       final XYPointerAnnotation pointer = new XYPointerAnnotation("", x, y,
//	                                                              angle);
//	       pointer.setTipRadius(0);
//	       pointer.setArrowWidth(5);
//	       xyPlot.addAnnotation(pointer);
	}
	
	public static void drawUL(XYPlot xyPlot,Segment s,Vector2D p){
		BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 
                1.0f, new float[] {2.0f, 10.0f}, 0.0f);
		if (p.y>=s.end.y){
			drawSeg(xyPlot,s,s.end.x,s.end.y,p.x,p.y,bs);			
			drawEndSeg(xyPlot, s, p, 1);
		} else if (p.y<=s.start.y){
			drawSeg(xyPlot,s,p.x,p.y,s.start.x,s.start.y,bs);
			drawEndSeg(xyPlot, s, p, 1);
		}
////		 final CircleDrawer cd = new CircleDrawer(Color.red, new BasicStroke(1.0f), null);	       	       
//	       final XYPointerAnnotation pointer = new XYPointerAnnotation("", x, y,
//	                                                              angle);
//	       pointer.setTipRadius(0);
//	       pointer.setArrowWidth(5);
//	       xyPlot.addAnnotation(pointer);
	}
	
	public static void drawCar(XYPlot xyPlot){		
		TrackSegment.drawLine(xyPlot, 0, 0, carDirection.x*10, carDirection.y*10);
		
	}

	public void drawEstimate(final String title){			
		XYSeries series = new XYSeries("Curve");		
		XYDataset xyDataset = new XYSeriesCollection(series);
		
		/*int nL = edgeDetector.lSize;
		int nR = edgeDetector.rSize;
		Vector2D[] left = edgeDetector.left;
		Vector2D[] right = edgeDetector.right;
		for (int i=0;i<nL;++i){
			series.add(left[i].x, left[i].y);
		}
		
		for (int i=0;i<nR;++i){
			series.add(right[i].x, right[i].y);
		}
		
		series.add(edgeDetector.highestPoint.x,edgeDetector.highestPoint.y);//*/

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot("", "x", "y", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.getDomainAxis().setRange(-30.0,50.0);
		xyPlot.getRangeAxis().setRange(-5.0,75.0);
		
		ObjectArrayList<TrackSegment> ts = new ObjectArrayList<TrackSegment>(trSz*2);
		TrackSegment tseg;
		for (int i = 0;i<trSz;++i){
			Segment t = trArr[ trIndx[i] ];
			t = t.leftSeg;
			if (t.type==0){
				tseg = TrackSegment.createStraightSeg(0, t.start.x, t.start.y, t.end.x, t.end.y);
				ts.add(tseg);
				t = t.opp;
				tseg = TrackSegment.createStraightSeg(0, t.start.x, t.start.y, t.end.x, t.end.y);
				ts.add(tseg);
			} else {
				tseg = TrackSegment.createTurnSeg(t.center.x, t.center.y, t.radius, t.start.x, t.start.y, t.end.x, t.end.y);
				ts.add(tseg);
				t = t.opp;
				tseg = TrackSegment.createTurnSeg(t.center.x, t.center.y, t.radius, t.start.x, t.start.y, t.end.x, t.end.y);
				ts.add(tseg);
			}
		}//*/
				
						
		TrackSegment.addEdgeArrow(xyPlot, ts);		
		
		for (int i = 0 ;i<trSz;++i){
			Segment t = trArr[ trIndx[i] ];
			if (t.upper==null && t.lower==null) {
				if (i<trSz-1){
					Segment next = trArr[ trIndx[i+1]];
					Vector2D pt = new Vector2D();
					if (Segment.isConnected(t.leftSeg,next.leftSeg,tW,pt)){
						drawUL(xyPlot, t.leftSeg, pt);
						drawUL(xyPlot, next.leftSeg, pt);
						if (Segment.isConnected(t.rightSeg,next.rightSeg,-tW,pt)){
							drawUL(xyPlot, t.rightSeg, pt);
							drawUL(xyPlot, next.rightSeg, pt);
						}
					}
				}
			}
			
			drawUL(xyPlot,t.leftSeg);
			drawUL(xyPlot,t.rightSeg);
		}
		
		Segment lastSeg = trArr[trIndx[1]].rightSeg;
		BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 
                1.0f, new float[] {2.0f, 10.0f}, 0.0f);
		if (lastSeg.type!=0) TrackSegment.drawCircle(xyPlot, lastSeg.center.x, lastSeg.center.y, lastSeg.radius,bs);
		
		double[] rs = new double[6];
		if (lastSeg.type!=0) Geom.ptTangentLine(0, 0, lastSeg.center.x, lastSeg.center.y, lastSeg.radius,rs);
		double tx = rs[0];
		double ty = rs[1];
		TrackSegment.drawText(xyPlot, "Last Segment", lastSeg.end.x, (lastSeg.end.y+lastSeg.start.y)*0.5, 12);
		TrackSegment.drawText(xyPlot, "+", tx, ty, 24);
		TrackSegment.drawText(xyPlot, "P", tx+2, ty, 12);
		TrackSegment.drawCircle(xyPlot, tx, ty, 1);
		TrackSegment.drawText(xyPlot, "+", tx-GAP, ty, 24,Color.RED);
		TrackSegment.drawText(xyPlot, "Target",tx-GAP, ty+2,12);
		TrackSegment.drawCircle(xyPlot, tx-GAP, ty, 1,Color.RED);
		TrackSegment.drawLine(xyPlot, 0, 0, tx, ty,bs);
//		TrackSegment.drawArrowLabel(xyPlot, "P", tx, ty, Math.PI, 12);
//		TrackSegment.drawText(xyPlot, "m", tx*0.5, ty*0.5-1, 12);
		
		
		drawCar(xyPlot);
		
//		double speedRadius = radiusAtSpeed(speed);
//		double nx = turn*speedV.y;	
//		double ny = turn*-speedV.x;			
//		double ox = nx*speedRadius;
//		double oy = ny*speedRadius;
		
		
//		TrackSegment.drawCircle(xyPlot, ox, oy, speedRadius,bs,Color.BLUE);
//		Geom.getLineSegCircleIntersection(tx, ty, ox, oy, ox, oy, speedRadius, rs);
//		double px = rs[0];
//		double py = rs[1];
//		TrackSegment.drawLine(xyPlot, tx,ty,px,py,Color.RED);


		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(500, 500);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();

	}

	
	public static void drawEndpoint(final String title){
		XYSeries series = new XYSeries("Curve");		
//		for (int i=0;i<numPointLeft;++i){
//		series.add(leftEgdeX[i], leftEgdeY[i]);
//		}

		/*for (int i = 0;i<trSz;++i){
			Segment t = trArr[ trIndx[i] ];
			t = t.leftSeg;
			series.add(t.start.x,t.start.y);
			series.add(t.end.x,t.end.y);
			t = t.opp;
			series.add(t.start.x,t.start.y);
			series.add(t.end.x,t.end.y);
		}//*/



//		for (int i=0;i<numPointRight;++i){
//		series.add(rightEgdeX[i], rightEgdeY[i]);
//		}
		
		double Ax = -20;
		double Ay = 0;
		double Bx = -20.001;
		double By = 10;
		
		double Cx = -15;
		double Cy = 32;		
		double Dx = -10;
		double Dy = 35;
		
		double[] rs = new double[6];
		Geom.getLineLineIntersection(Ax, Ay, Bx, By, Cx, Cy, Dx, Dy, rs);
		double Ex = rs[0];
		double Ey = rs[1];
		series.add(Cx,Cy);
		series.add(Dx,Dy);
		series.add(Ex,Ey);

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot("", "x", "y", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.getDomainAxis().setRange(-30.0,20.0);
		xyPlot.getRangeAxis().setRange(-10.0,40.0);
		BasicStroke bs = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 
              1.0f, new float[] {2.0f, 10.0f}, 0.0f);
		TrackSegment.drawLine(xyPlot, Ax, Ay, Bx, By);
		
		TrackSegment.drawLine(xyPlot, Cx, Cy, Dx, Dy,bs);
		TrackSegment.drawLine(xyPlot, Cx, Cy, Ex, Ey,bs);
		
		TrackSegment.drawLine(xyPlot, Bx, By, Ex, Ey,bs);
		
		Geom.getCircle2(Cx, Cy, Dx, Dy,Ax, Ay, Bx, By, rs);
		double cnx = rs[0];
		double cny = rs[1];
		double r = Math.sqrt(rs[2]);
		
		Geom.getLineLineIntersection(cnx, cny, cnx-1, cny, Ax, Ay, Bx, By, rs);
		double Mx = rs[0];
		double My = rs[1];
		
		TrackSegment.drawLine(xyPlot, Mx, My, cnx, cny+0.001,bs);
		TrackSegment.drawText(xyPlot, "r", (cnx+Mx)*0.5, My+1, 14);
		TrackSegment.drawText(xyPlot, "O", Ex, Ey+1, 14);
		TrackSegment.drawText(xyPlot, "p", Ax-1, (Ay+By)*0.5, 14);
		TrackSegment.drawCircle(xyPlot, cnx, cny, r);
		TrackSegment.drawText(xyPlot, "C", Mx-1, My, 14);
		TrackSegment.drawText(xyPlot, "A", Cx, Cy+1, 14);
		TrackSegment.drawText(xyPlot, "B", Dx, Dy+1,14);
		TrackSegment.drawArrowLabel(xyPlot, "start ", Ax, Ay, 0.75*Math.PI, 14);
		TrackSegment.drawArrowLabel(xyPlot, "end ", Bx, By, -0.75*Math.PI, 14);
		series.add(cnx,cny);
		
		/*for (int i = 0;i<trSz;++i){
			Segment t = trArr[ trIndx[i] ];
			
			if (i==1){
				Segment prev = trArr[ trIndx[i-1] ];
				Vector2D point = new Vector2D();
				series.add(prev.center.x,prev.center.y);
				if (Segment.isConnected(prev, t, tW, point)){
					TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, point.x, point.y,bs);
					Line2D line = new Line2D.Double(point.x, point.y, t.end.x, t.end.y);
					XYShapeAnnotation lineAnnotation = new XYShapeAnnotation(line,bs,Color.GRAY);
					xyPlot.addAnnotation(lineAnnotation);
//					TrackSegment.drawArc(xyPlot, t.center.x, t.center.y, t.radius, point.x, point.y,t.end.x,t.end.y,bs);
				}
			}
			t = t.leftSeg;
//			if (t.type==0){
////				line(t.startX, t.startY, t.endX, t.endY, series);
//				Line2D line = new Line2D.Double(t.start.x, t.start.y, t.end.x, t.end.y);
//				XYShapeAnnotation lineAnnotation = new XYShapeAnnotation(line,bs,Color.GRAY);
//				xyPlot.addAnnotation(lineAnnotation);			
//				
//			} 
			if (i==1){					

//				Ellipse2D circle = new Ellipse2D.Double(cnx-w, cny-w, w*2, w*2);
//				XYShapeAnnotation arcAnnotation = new XYShapeAnnotation(circle,bs,Color.GRAY);
//				xyPlot.addAnnotation(arcAnnotation);
				if (i==1){
					Segment prev = trArr[ trIndx[i-1] ].leftSeg;
					Vector2D point = new Vector2D();
					if (Segment.isConnected(prev, t, tW, point)){
						TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, point.x, point.y);
						Line2D line = new Line2D.Double(point.x, point.y, t.end.x, t.end.y);
						XYShapeAnnotation lineAnnotation = new XYShapeAnnotation(line);
						xyPlot.addAnnotation(lineAnnotation);
//						TrackSegment.drawArc(xyPlot, cnx, cny, t.radius, point.x, point.y,t.end.x,t.end.y);
					} else TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, prev.end.x, prev.end.y);
//					series.add(t.start.x,t.start.y);
//					series.add(t.end.x,t.end.y);
				} 
//				else {
//					series.add(cnx-t.type*w,0);
//				}
			}
			
			t = t.opp;
//			if (t.type==0){
////				line(t.startX, t.startY, t.endX, t.endY, series);
//				Line2D line = new Line2D.Double(t.start.x, t.start.y, t.end.x, t.end.y);
//				XYShapeAnnotation lineAnnotation = new XYShapeAnnotation(line,bs,Color.GRAY);
//				xyPlot.addAnnotation(lineAnnotation);					
//			}
			if (i==1){					
					
					if (i>0){
						Segment prev = trArr[ trIndx[i-1] ].rightSeg;
						Vector2D point = new Vector2D();
						if (Segment.isConnected(prev, t, tW, point)){
							TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, point.x, point.y);
							Line2D line = new Line2D.Double(point.x, point.y, t.end.x, t.end.y);
							XYShapeAnnotation lineAnnotation = new XYShapeAnnotation(line);
							xyPlot.addAnnotation(lineAnnotation);
//							TrackSegment.drawArc(xyPlot, cnx, cny, t.radius, point.x, point.y,t.end.x,t.end.y);
						} else TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, prev.end.x, prev.end.y);
//						series.add(t.start.x,t.start.y);
//						series.add(t.end.x,t.end.y);
					}
//					Ellipse2D circle = new Ellipse2D.Double(cnx-w, cny-w, w*2, w*2);
//					XYShapeAnnotation arcAnnotation = new XYShapeAnnotation(circle,bs,Color.GRAY);
//					xyPlot.addAnnotation(arcAnnotation);
//					if (i>0){
//						Segment prev = trArr[ trIndx[i-1] ].rightSeg;
//						Vector2D point = new Vector2D();
//						if (Segment.isConnected(prev, t, tW, point)){
//							TrackSegment.drawArc(xyPlot, prev.center.x, prev.center.y, prev.radius, prev.start.x, prev.start.y, point.x, point.y);
//							series.add(point.x,point.y);
//						}
////						series.add(t.start.x,t.start.y);
//						series.add(t.end.x,t.end.y);
//					} else {
//						series.add(cnx-t.type*w,0);
//					}
			}
		}
		
		TrackSegment.addEdge(xyPlot, trackData);

		
		Segment first = trArr[trIndx[0]];
		/*double cnx = first.center.x;
		double cny = first.center.y;
		double r = first.radius;
		bs = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 
	              1.0f, new float[] {2.0f, 2.0f}, 0.0f);
		TrackSegment.drawLine(xyPlot, px, py, cnx, cny, bs);
		
		double[] rs = new double[6];
		Geom.getLineSegCircleIntersection(px, py, cnx, cny, cnx, cny, r,rs);
		double mx = rs[0];
		double my = rs[1];
		double rad = Geom.distance(cnx, cny, px, py);
		TrackSegment.drawArc(xyPlot, cnx, cny, rad , px, py, cnx+rad, 0,bs);
		
		bs = new BasicStroke(3);
		TrackSegment.drawLine(xyPlot, 0, 0, cnx+rad, 0.0001,bs);
		
		Geom.getLineSegCircleIntersection((toMiddle+px)*0.5, py*0.5, cnx, cny, cnx, cny, r,rs);
		
		TrackSegment.drawArrowLabel(xyPlot, " Origin ", 0, 0, -0.5*Math.PI,9);
		
		TrackSegment.drawArrowLabel(xyPlot, "Center of turn", cnx, cny, -0.25*Math.PI,9);
		
		TrackSegment.drawArrowLabel(xyPlot, "Track line", -20, 32.5, -0.35*Math.PI,9);
		
		TrackSegment.drawText(xyPlot, " Op ", px, py+1,14);
		
		TrackSegment.drawArrowLabel(xyPlot, " \u03B4 ", rs[0], rs[1], Math.PI,14);
		TrackSegment.drawArrowLabel(xyPlot, " \u03B8 ", (cnx+rad)*0.5, 0, -Math.PI*0.25,14);		
		TrackSegment.drawArc(xyPlot, cnx, cny, r, mx, my, toMiddle, 0,bs);
		series.add(0,0);//*/

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(500, 500);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();


	}


	private final void draw(Vector2D centerOfTurn,double radiusOfTurn,String s){

		if (draw){			
			//			EdgeDetector.drawEdge(edgeDetector, "E"+time+" "+radiusOfTurn+" "+radiusL+""+radiusR+" a");
			XYSeries series = new XYSeries("Curve");
			//			if (centerOfTurn!=null) {
			//			series.add(centerOfTurn.x,centerOfTurn.y);
			//			series.add(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
			//			}
			//			if (highestPoint!=null) series.add(highestPoint.x,highestPoint.y);			
			Vector2D[] lArr = edgeDetector.left;
			Vector2D[] rArr = edgeDetector.right;
			int sL = edgeDetector.lSize;
			int sR= edgeDetector.rSize;		
			for (int i=sL-1;i>=0;--i){	
				Vector2D v = lArr[i]; 
				if (v!=null) series.add(v.x, v.y);					
			}

			if (edgeDetector.whichE==0) series.add(edgeDetector.highestPoint.x,edgeDetector.highestPoint.y);
			for (int i=sR-1;i>=0;--i){
				Vector2D v = rArr[i];
				if (v!=null) series.add(v.x, v.y);
			}


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
				e.printStackTrace();
			}
			//			meta=1;
		}

	}



	public final void drive(NewCarState cs,CarControl cc) {
		// TODO Auto-generated method stub

		long ti = (debug) ? System.nanoTime() : 0;
		maxTurn = true;
		point2Follow = null;		
		brake = 0.0d;		
		acc = 1;				
		curAngle = cs.angle;	
		speedX = cs.speedX;
		speedY = cs.speedY;
		speed = Math.sqrt(speedX*speedX+speedY*speedY);
		rpm = cs.rpm;
		if (time>=BREAK_TIME)
			System.out.println();
//		if (debug) 
			System.out.println("**************** E"+time+" "+distRaced+" ****************  "+turn+"      "+edgeDetector.whichE+"     "+canGoToLastSeg);//
		if (isStuck(cs) && reserveCount<100){			
			double signPos = (curPos<0) ? -1 :1;
			relativeAngleMovement = (turn==0 || turn==2) ? -signPos*(curAngle-lastAngle) : (curAngle-lastAngle)*turn;
			relativeAngle = (turn==0 || turn==2) ? -signPos*curAngle : curAngle*turn;
//			double relativeHeading = (turn==0 || turn==2) ? -signPos*carDirection.x : carDirection.x*turn;
			relativePosMovement = (turn==0 || turn==2) ? -signPos*(curPos-lastPos) : (curPos-lastPos)*turn;		
//			double relativeSteer = (turn==0 || turn==2) ? -steer : -steer*turn;				
//			double relativeCurPos = (turn==0 || turn==2) ? curPos : curPos*turn;	
			if (debug){
				System.out.println("CurAngle is "+curAngle);
				System.out.println("LastAngle is "+lastAngle);
				System.out.println("CurPos is "+curPos);
				System.out.println("reserveMCount is "+reserveMCount);
				System.out.println("reserveCount is "+reserveCount);
				System.out.println("SpeedX is "+speedX);
			}
			cc.brake = 0;
			cc.gear = -1;
			double absAngle = Math.abs(curAngle);
			double absLastAngle = Math.abs(lastAngle);
			double absSpeedX = Math.abs(speedX);
			if (absAngle>PI_2){
				steer = -Math.signum(curAngle/steerLock);
			} else {				
				steer = -Math.signum(curAngle/steerLock);
//				if (absAngle>absLastAngle) reserveMCount++;
//				reserveMCount--;
//				if (reserveMCount==30) reserveMCount = 0;
			}
			if (speedX>LOW_SPEEDY || speedX<-LOW_SPEEDY) 
				reserveMCount = 0;
			else if (speedX<=2){
				if (speedX>-2 && speedX<=0.5) reserveMCount++;
				if (reserveMCount>15) {
					reserveCount = 100;
					stuck = 0;
				}
			}
			cc.accel = (speedX>-10) ? 1 : 0;
			if (speedX>-5) {
				steer = 0;
			}
			
			cc.steer = steer;
			if (cc.steer<-1) 
				cc.steer = -1;
			else if (cc.steer>1)
				cc.steer = 1;
			cc.meta = 0;
			lastRPM = rpm;
			reserveCount++;
			
			if (reserveCount>=95 && absAngle > MAX_UNSTUCK_ANGLE && (speedX<-2 || speedX>=0 || speedX<lastSpeedX-0.5)){
				reserveCount = 0;
				stuck = MAX_UNSTUCK_COUNT;
			}
			
			
			lastRelativeAngle = relativeAngle;
			lastRelativeAngleMovement = relativeAngleMovement;
			lastRelativePosMovement = relativePosMovement;
			lastRelativeTargetAngle = relativeTargetAngle;
			lastSteer = steer;
			lastPos = curPos;
			lastAngle = curAngle;
			lastSpeedY = speedY;
			lastSpeedX = speedX;
			lastRPM = rpm;
			lastBrkCmd = brake;
			lastGear = gear;	
			lastAcc = acc;
			lastDistToEstCircle = distToEstCircle;
			
			return;
		} else if (reserveCount>=50){			
			stuck = 0;
		}

		reserveCount = 0;
		reserveMCount = 0;


		curPos = -cs.trackPos;		
		//		double dP = curPos-lastPos;
		

		double sin = Math.sin(curAngle);
		double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(curAngle);		
		if (cos==-1.0) {
			carDirection.x = 0;
			carDirection.y = -1;
		} else if (cos!=1){			
			carDirection.x = sin;
			carDirection.y = cos;
		} else {
			carDirection.x = 0;
			carDirection.y = 1;
		}

		speedV.x = carDirection.x * speedX;
		speedV.y = carDirection.y * speedX;
		double lx = carDirection.y;
		double ly = carDirection.x;
		//		double tp = (carDirection.x<0) ? -speedY : speedY;
//		double absSpeedY = Math.abs(speedY);
//		double sp = (absSpeedY>=HIGH_SPEEDY) ? 0 : (absSpeedY>=MODERATE_SPEEDY) ? speedY*0.5 : speedY;
		speedV.x -= lx*speedY;
		speedV.y -= ly*speedY;
		

		//		carDirection = trackDirection.rotated(-curAngle);
		//		double speed = cs.getSpeed();
		double l = speedV.length();
		speedV.x /= l;
		speedV.y /= l;
		
		speedV.x = carDirection.x;
		speedV.y = carDirection.y;
		currentSpeedRadius = radiusAtSpeed(speedX);
		if (currentSpeedRadius<0) currentSpeedRadius = 0;
		time = cs.getLastLapTime();
		distRaced = cs.distRaced;
		//		realRad = cs.radius;

		if (debug && time>=BREAK_TIME)
			System.out.println();
		edgeDetector.init(cs,trackWidth);				
		prevTW = tW;
		tW =  Math.round(edgeDetector.trackWidth)*0.5d;
		if (tW<0) tW = prevTW;

		if (edgeDetector.trackWidth<=0 && prevEdge!=null)
			edgeDetector.trackWidth = prevEdge.trackWidth;
		trackWidth = edgeDetector.getTrackWidth();//must keep this line
		workingWidth = trackWidth-2*W;			



		double tW = Math.round(edgeDetector.trackWidth)*0.5d;
		toMiddle =Math.round(-tW*edgeDetector.curPos*PRECISION)/PRECISION;
		if (Math.abs(curPos)>1 && trSz>0){
			Segment first = trArr[ trIndx[0]];
			turn = (first.type!=0) ?first.type : turn;
		}
		toInnerEdge = (turn==0 || turn==2) ? -toMiddle-tW : curPos*tW*turn - tW;
		toOutterEdge = toInnerEdge + tW*2;		
		mass = carmass + cs.getFuel();
		leftMost.x = toMiddle-tW;
		rightMost.x = toMiddle+tW;
//		testing(cs,edgeDetector);
		
		double[] wheelSpinVel = cs.wheelSpinVel;
//		double slip = 0;
//		for (int i = 0; i < 4; i++) {
//			slip += wheelSpinVel[i] * wheelRadius[i];
//		}
//		slip = speedX/3.6- slip/4.0f;
		slip = (wheelSpinVel[REAR_LFT]+wheelSpinVel[REAR_RGT])*wheelRadius[REAR_LFT]*0.5*3.6-speedX;
		balance = wheelSpinVel[REAR_LFT] * wheelRadius[REAR_LFT]+ wheelSpinVel[REAR_RGT] * wheelRadius[REAR_RGT]+wheelSpinVel[FRONT_LFT] * wheelRadius[FRONT_LFT]+wheelSpinVel[FRONT_RGT] * wheelRadius[FRONT_RGT];
		balance = balance*0.25*3.6-speedX;
		isOffBalance = (slip>0 && balance<0) || (slip<0 && balance<0 && balance<slip*2);

		/*if ((time>=BREAK_TIME)){
			//			if (time>=10.23 && time<=10.24){
			//
			//				ControlPath cp=new ControlPath();
			//				cp.addPoint(PointFactory.createPoint(0, 0));	
			//				cp.addPoint(PointFactory.createPoint(edgeDetector.currentPointAhead.x, edgeDetector.currentPointAhead.y));	
			//				Vector2D v = r.get(r.size()-1).end;
			//				cp.addPoint(PointFactory.createPoint(v.x,v.y));
			//				cp.addPoint(PointFactory.createPoint(edgeDetector.highestPoint.x, edgeDetector.highestPoint.y));
			//				GroupIterator gi = new GroupIterator("0:n-1", cp.numPoints());
			//				MyBezierCurve bs = new MyBezierCurve(cp,gi);			
			//				MultiPath mp = new ShapeMultiPath();	
			//				bs.appendTo(mp);			
			//				double[] pp= new double[3];
			//				double u = 1.0;
			//				pp[2] = u;			
			//				bs.eval(pp);
			//				x = pp[0];
			//				y = pp[1]; 
			//				System.out.println("Value : "+"("+x+","+y+")");
			//				pp[0] = 0;
			//				pp[1] = 0;
			//				bs.df(pp);			
			//				System.out.println("First Derivative : "+"("+pp[0]+","+pp[1]+")");
			//				double dx = pp[0];
			//				double dy = pp[1];
			//				pp[0] = 0;
			//				pp[1] = 0;
			//				bs.ddf(pp);			
			//				System.out.println("Second Derivative : "+"("+pp[0]+","+pp[1]+")");
			//				double ddx = pp[0];
			//				double ddy = pp[1];
			//				System.out.println("Radius : "+bs.radiusAt(pp[2]));
			//				double[] tmp = bs.osculatingCircleAt(u);
			//				rr = tmp[2];
			//				//			double d = dx*dx+dy*dy;
			//				//			double g = (dx*ddy-ddx*dy);
			//				//			cntr = new Vector2D(x-d*dy/g,y+d*dx/g);
			//				cntr = new Vector2D(tmp[0],tmp[1]);
			//				double[] rrr = new double[3];
			//				Geom.getCircle2(v, edgeDetector.highestPoint, new Vector2D(0,0), carDirection, rrr);
			//				rr = Math.sqrt(rrr[2]);
			//				cntr = new Vector2D(rrr[0],rrr[1]);
			////				double oldx = 0;
			////				double oldy = 0;
			////				for (int i = 0;i<mp.getNumPoints();++i){
			////					double[] p = mp.get(i);
			////					TrackSegment ts = TrackSegment.createStraightSeg(0, p[0], p[1], oldx, oldy);
			////					trackData.add(ts);
			////					oldx = p[0];
			////					oldy = p[1];
			////				}
			//			}
			//			double[] rrr = new double[3];
			//			Geom.getCircle3(new Vector2D(0,0), carDirection, edgeDetector.currentPointAhead, edgeDetector.highestPoint, rrr);

			draw=true;
			//			draw = false;
			if (draw) {
				store();
				//			TrackSegment ts = (isTurning) ? TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, 0, 0, optimalPoint.x, optimalPoint.y) : TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, cntr.x-turn*rr, cntr.y, optimalPoint.x, optimalPoint.y);
				//				TrackSegment ts = TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, cntr.x+rr, cntr.y, cntr.x-rr, cntr.y); 
				//				trackData.add(ts);

				TrackSegment seg = TrackSegment.createStraightSeg(0,0, 0, carDirection.x*20, carDirection.y*20);
				trackData.add(seg);
				trackE.add(seg);
				//			ts = TrackSegment.createTurnSeg(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, centerOfTurn.x-turn*radiusOfTurn, centerOfTurn.y, optimalPoint.x, optimalPoint.y);
				//			treackData.add(ts);			
				//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusSmall, center.x-turn*radiusSmall, center.y, center.x, center.y+radiusSmall);
				//			trackData.add(ts);
				//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusLarge, center.x-turn*radiusLarge, center.y, center.x, center.y+radiusSmall);
				//			trackData.add(ts);
			}

			if (draw) display();
			draw=false;
		}//*/
		if (!flyingHazard && speedX>50 && (speedX<lastSpeedX || startFlying>0 && speedX<lastSpeedX+0.1 || slip>15) && slip>10 && slip>0 && lastSlip>0 && balance>0){
			if (startFlying==0) 
				startFlying = time;
			else if (time-startFlying>0.2) {
				flyingHazard = true;	
				startFlying = 0;					
			}
		} else if (flyingHazard){
			if ((slip<lastSlip-3 || slip<=10) && !landed){//landing				
				landed = true;
			} else if (landed){
				if (slip<10){
					if (startLanding==0) 
						startLanding = time;
					else if (time-startLanding>0.2){
						startLanding = 0;
						landed = false;
						flyingHazard = false;
					}
				}
			}
		} else if (!flyingHazard && startFlying>0) 
			startFlying = 0;
//		else if (!flyingHazard && lastSlip>10 && slip<10 && slip<lastSlip-5 && lastAcc>0){
//			landed = true;
//			flyingHazard = true;
//			startLanding = time;
//		}
		gear = getGear(cs);
		steer = steerControl(cs);	
		if (debug) System.out.println("Time after steerControl taken : "+(System.nanoTime()-ti)/1000000);


		//		double s = steerAtSpeed(speed);		

		//		maxSpeed = 60;
		if (curPos<=1 && curPos>=-1) speedControl(cs, maxSpeed,mustPassPoint);
		if (debug) System.out.println("Time taken : "+(System.nanoTime()-ti)/1000000);
		//		if (Math.abs(steer)>s){							
		//		acc = 0;
		//		}

		//		if (acc>=0 && isTurning && dP*turn<0){
		//		if (relativeCurPos<=WIDTHDIV-0.1)
		//		acc = (2/(1+Math.exp(-10)) - 1);			
		//		if (relativeCurPos<=WIDTHDIV-0.15){
		//		acc = 0;
		//		}

		//		acc = 0;
		//		}

		//		if (acc<0) {
		//			brake = -acc;
		//			acc = 0;
		//		}
		//		if (brake==1){			
		//			acc = 1;
		//		}
		//		if (isTurning) brake=0;
		//		if (time>=5)
		//		System.out.println();

		//		if (isTurning && relativeCurPos>=0)
		//		steer = gotoPoint(cs, trackDirection);


		//		steer = lastSteer + 50 * (steer - lastSteer) * 0.005;

		//		if ((steer*carDirection.x>0 || steer*lastSteer)&& Math.abs(steer)>0.3) steer*=0.1;
//		if (flying) 
//			steer = 0;
		//			steer *= 0.1;
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
		if (debug && time>=BREAK_TIME){
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMinimumFractionDigits(2);
			nf.setMaximumFractionDigits(2);
			drawEstimate("E "+nf.format(time)+" b");
		}
		if (debug) System.out.println(cs.radiusl+"    r    "+cs.radiusr+"    r     "+cs.radius);
		if (debug) System.out.println("Must pass point  "+mustPassPoint+"     Optimal point  "+optimalPoint);		
		if (steer<-1) 
			steer = -1;
		else if (steer>1) steer = 1;
				
		
		if (speedX<0 && Math.abs(curAngle)<PI_2)
			steer = -steer;
		else if (speedX<3 && gear>0)
			steer = (speedX>=0) ? 0 : -Math.signum(steer);
																						
		lastRelativeAngle = relativeAngle;
		lastRelativeAngleMovement = relativeAngleMovement;
		lastRelativePosMovement = relativePosMovement;
		lastRelativeTargetAngle = relativeTargetAngle;
		lastSteer = steer;
		lastPos = curPos;
		lastAngle = curAngle;
		lastSpeedY = speedY;
		lastSpeedX = speedX;
		lastRPM = rpm;
		lastBrkCmd = brake;
		lastGear = gear;	
		lastAcc = acc;
		lastDistToEstCircle = distToEstCircle;		

		cc.accel = acc;
		cc.brake = brake;
		cc.gear = gear;
		cc.steer = steer;
		cc.meta = 0;						
		if (debug) System.out.println("CurPos :  "+curPos);
		if (debug) System.out.println("SpeedX :  "+speedX+"     SpeedY  :   "+speedY);		
		if (debug) System.out.println(wheelSpinVel[REAR_LFT]*wheelRadius[REAR_LFT]*3.6+"    "+wheelSpinVel[REAR_RGT]*wheelRadius[REAR_RGT]*3.6+"   "+wheelSpinVel[FRONT_LFT]*wheelRadius[FRONT_LFT]*3.6+"   "+wheelSpinVel[FRONT_RGT]*wheelRadius[FRONT_RGT]*3.6+"    "+slip+"     "+balance);
		lastBalance = balance;
		lastSlip = slip;
		if (debug) System.out.println("Time taken : "+(System.nanoTime()-ti)/1000000);		
	}


	/*private static final double brakeDistAtSpeed(double x){
		return -0.30220189119619+0.0403949586208308*x+0.00221104316193883*x*x-3.56417513475425e-06*x*x*x;
		//		return -0.596348768823269+0.051461029864198*x+0.00135309496429643*x*x-2.1617586962591e-06*x*x*x;			
	}//*/

	private final void estimateBestPath(){
		double width = trackWidth * (1-WIDTHDIV);
		isTurning = true;
		drift = false;
		double radiusLarge = (edgeRadius+trackWidth/2)-width-W;
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
		radiusOfTurn = 3.414*(radiusLarge-0.707*radiusSmall);
		canGoAtCurrentSpeed = true;
		Vector2D center = edgeDetector.center;
		//		if (center==null) return;
		double cx = (center==null) ? 0 : center.x;
		double cy = (center==null) ? 0 : center.y;
		Segment curSegment = (trSz>0) ? trArr[ trIndx[0] ] : null;
		Segment lastSeg = (trSz>0) ? trArr[ trIndx[trSz-1]] : null;	
		double last_speed = (lastSeg==null || lastSeg.type==0) ? Double.MAX_VALUE : speedAtRadius(lastSeg.radius);		
		double first_speed = (curSegment==null || curSegment.type==0) ? Double.MAX_VALUE : speedAtRadius(curSegment.radius);
		double speedRadius = this.currentSpeedRadius;			
		double nx = speedV.y;
		double ny = -speedV.x;
		double temp = speedRadius*turn;
		double ox = nx*temp;
		double oy = ny*temp;
		//		double px = 0;
		//		double py = 0;			
		//		int whichEdgeAhead = edgeDetector.whichEdgeAhead;
		double rx = 0;
		double ry = 0;
		double dx = 0;
		double dy = 0;
//		if (time>=BREAK_TIME){			
//			TrackSegment tss = TrackSegment.createTurnSeg(ox, oy, speedRadius, 0, 0, ox, oy+speedRadius);
//			trackData.add(tss);
//		}
		if ((trSz==1 && lastSeg.type==0 && isFirstSeg(lastSeg)) || (trSz>1 && curSegment.type==0 && isFirstSeg(curSegment) && lastSeg.type==0 && isFirstSeg(lastSeg))){
			double mx = toMiddle;
			double my = lastSeg.end.y;
			if (turn!=0){
				if (my>40) 
					mx = toMiddle-turn*(0.5*W-tW);
				else if (my>FAST_MARGIN) {
					if (toMiddle*turn<0) 
						mx = toMiddle;
					else mx = toMiddle-turn*W;
				} else mx = toMiddle+turn*W;
			}
			if (mustPassPoint==null) 
				mustPassPoint = new Vector2D( mx,my);
			else {
				mustPassPoint.x = mx;
				mustPassPoint.y = my;
			}
			rr = Segment.MAX_RADIUS;
			centerOfTurn = null;
			return;
		}

		boolean tooFast = false;
		boolean possibleTooFast = false;
		Segment nextSlowSeg = null;
		int lowestSegIndx = 0;
		//		boolean safe = false;
		Segment lowestSeg = null;		
		double lowestSpeed = Double.MAX_VALUE;
		for (int i = trSz-1;i>=0;--i){
			Segment t = trArr[ trIndx[i]];
			double segSpeed = (t.type==0) ? 1000 : speedAtRadius(t.radius);
			double startY = Math.max(t.start.y,0);
			if (!possibleTooFast){ 
				possibleTooFast = startY<40 && speedX-segSpeed-startY>FAST_MARGIN;
				nextSlowSeg = t;
			}
			if (lowestSpeed>segSpeed){
				//				rad = t.radius;
				lowestSeg = t;
				lowestSegIndx = i;
				lowestSpeed = segSpeed;
			}			
			//			if (t.type!=0 && t.radius>speedRadius) safe = true; 
		}		

		if (lowestSegIndx>=0) lowestSeg = trArr [ trIndx[lowestSegIndx] ];

		double startY = (lastSeg==null) ? 0 : (!inTurn && lastSeg.type!=0 && trSz==2 && lastSeg.lower!=null) ? Math.max(lastSeg.center.y,0) : Math.max(lastSeg.start.y,0);
		double lastSpeed = (lastSeg==null || lastSeg.type==0) ? 1000 : speedAtRadius(lastSeg.radius);		

		tooFast = trSz==1 && speedX-lastSpeed>FAST_MARGIN || startY<40 && speedX-lastSpeed-startY>FAST_MARGIN;
		if (tooFast){
			nextSlowSeg = lastSeg;
		} else if (trSz>1 && speedX-lastSpeed-startY>FAST_MARGIN)
			tooFast = possibleTooFast;			


		//		turn = (trSz<=1) ? edgeDetector.t-urn : (lastSeg.type!=0) ? lastSeg.type : (lastSeg.end.x>0) ? 1 : -1;
		//		edgeDetector.turn = turn;		

//		double relativePosMovement = (curPos-lastPos)*turn;		
//		double relativeSpeedY = speedY*turn;
//		double relativeAngleMovement = (curAngle-lastAngle)*turn;
//		double relativeAngle = curAngle*turn;		

		if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && speedX>last_speed && (lastSeg.start.y<30 || tooFast && nextSlowSeg.start.y<30 || lastSeg.start.y<40 && speed>200)){			
			//			Segment lastS = (lastSeg.type==1) ? lastSeg.rightSeg : lastSeg.leftSeg;
			double rad = (lastSeg.type==0 || lastSeg.radius>100) ? lastSeg.radius -GAP : lastSeg.radius-tW+GAP;
			if (Geom.ptTangentLine(0, 0, lastSeg.center.x, lastSeg.center.y, rad,tmpBuf)>0){
				double mx = (lastSeg.type==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
				double my = (lastSeg.type==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];				
//				TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, mx, my);
//				trackData.add(ts);
				double d = Math.sqrt(mx*mx+my*my);
				double ddx = mx - ox;
				double ddy= my - oy;
				double dist2Cntr = Math.sqrt(ddx*ddx+ddy*ddy)-2;
				//				double s = steerAtSpeed(speed)*EdgeDetector.steerLock;
				//				double angle = Vector2D.angle(carDirection.x, carDirection.y, mx, my);
				//				if (angle>Math.PI) angle-=Math.PI*2;
				//				if (angle<-Math.PI) angle+=Math.PI*2;
				int wEdge = isCut(ox, oy, rad,0,0,mx,my, 0, trSz, tmpBuf,GAP);
				if (wEdge==0 && (speedRadius>rad+d ||  speedRadius>=dist2Cntr)){
					if (mustPassPoint==null) 
						mustPassPoint = new Vector2D( mx,my);
					else {
						mustPassPoint.x = mx;
						mustPassPoint.y = my;
					}				
					DANGER = true;
					if (d<45) {
						Segment first = trArr[trIndx[0]];
						int tp = first.type;												
						drift = Math.abs(speedY)>HIGH_SPEEDY || speedX>lowestSpeed+FAST_MARGIN+d || speedX>=lowestSpeed && speedX>=first_speed && speedX>lowestSpeed+FAST_MARGIN || toOutterEdge<SAFE_EDGE_MARGIN && tp*turn>0 && lastSteer*tp<=0 && ((lastPos-curPos)*tp>0 || curAngle*tp<0 );
					}
					if (d>50) 
						d*=2;
					else d = 0;
					rr = (drift) ? Math.min(speedRadius,rad+d) : (lowestSeg==null || lowestSeg.start.y>15) ? radiusAtSpeed(lowestSpeed+FAST_MARGIN) : Math.max(speedRadius,radiusAtSpeed(lowestSpeed));
					if (cntr==null){
						cntr = new Vector2D(lastSeg.center);
					} else cntr.copy(lastSeg.center);				
					return;			
				}
			}
			//			int wEdge = Geom.getArcArcIntersection(ox, oy, speedRadius, 0, 0, ox, oy+speedRadius, lastS.czenter.x,lastS.center.y,lastS.radius,lastS.start.x,lastS.start.y,lastS.end.x,lastS.end.y,tmp);		
		}  

		boolean caution = (lastSeg!=null && turnLeft==turnRight && turnLeft!=0 && turnLeft!=lastSeg.type);

		if (trSz>1){				
			Segment lastS = (lastSeg.type==1) ? lastSeg.rightSeg : lastSeg.leftSeg;			
			if (!caution && trSz>=2 && lastS.type!=0 && lastSeg.type!=Segment.UNKNOWN && lastS.center!=null && lastS.radius<80){
				double ttx = (lastS.type==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
				double tty = (lastS.type==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;		
				Vector2D cnt = lastS.center;				
				double cnx = cnt.x;
				double cny = cnt.y;				
				double wdth = (lastSeg.radius);
				rx =  cnx-ttx*wdth;
				ry = cny-tty*wdth;

				double ddy = lastS.end.y - cny;
				double ddx = lastS.end.x - cnx;
				double dd = (lastS.radius+W)/lastS.radius;
				double rry = cny+ ddy*dd;
				if (ry<rry){
					ry = rry;
					rx = cnx+ddx*dd;
				}

				dx = ry - cny;
				dy = cnx - rx;
			} else if (lastSeg.type!=Segment.UNKNOWN){				
				rx = lastSeg.end.x;
				ry = lastSeg.end.y;
				dx = rx - lastSeg.start.x;
				dy = ry - lastSeg.start.y;
				double tx = (lastSeg.type!=0) ? rx - lastSeg.center.x : lastSeg.end.y - lastSeg.start.y;
				double ty = (lastSeg.type!=0) ? ry - lastSeg.center.y : lastSeg.end.x - lastSeg.start.x;
				double t = Math.sqrt(tx*tx+ty*ty);
				int tp = (lastSeg.type==0) ? (rx>0) ? 1 : -1 : lastSeg.type;
				if (turnLeft!=0 || turnRight!=0){
					double ratio = GAP/t;
					if (caution){
						ratio = (ry>FAST_MARGIN) ? GAP/t : -GAP/t;
					} else if (turnLeft== turn || turnRight==turn){
						ratio = 0.75*W/t;
					}
					if (lastSeg.type==0){
						rx += tp*tx*ratio;
						ry += tp*ty*ratio;
					} else {
						rx -= tx*ratio;
						ry -= ty*ratio;
					}
				}								
			}
			double d = Math.sqrt(rx*rx+ry*ry);			
			if (((lastS.type==0 && curSegment.type!=0) || (curSegment.type!=0 && curSegment.radius<lastS.radius)) && d<speedRadius){
				int sz = Geom.getLineCircleIntersection(rx, ry, rx+dx, ry+dy, 0, 0, speedRadius, tmp);
				if (sz>0){
					double nrx = tmp[0];
					double nry = tmp[1];
					if (sz>2 && nry<tmp[3]){
						nrx = tmp[2];
						nry = tmp[3];
					}
					dx = nrx - rx;
					dy = nry - ry;
					d = Math.sqrt(dx*dx+dy*dy);
					double ratio = (d+1)/d;
					rx += dx*ratio;
					ry += dy*ratio;
				} 
			}
			/*if (time>=BREAK_TIME){			
				Segment t = trArr[ trIndx[1]];
				Geom.ptTangentLine(0, 0, t.center.x, t.center.y, t.radius-tW+W,tmpBuf);
				double mx = (edgeDetector.turn==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
				double my = (edgeDetector.turn==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];
				Geom.getLineLineIntersection(0, 0, 0, 1, rx, ry, rx+dx, ry+dy, tmpBuf);
				ox = tmpBuf[0];
				oy = tmpBuf[1];
				double rdx = rx - ox;
				double rdy = ry - oy;
				double rd = Math.sqrt(rdx*rdx+rdy*rdy);
				double rox = 0;
				double roy = oy - rd;
				rox += rx;
				roy += ry;
				rox*=0.5;
				roy*=0.5;
				Geom.findCenter(ox, oy, 0, 0, rox, roy, mx, my, tmpBuf);
				double cnx = tmpBuf[2];
				double cny = tmpBuf[3];
				rr = Math.sqrt(Geom.ptLineDistSq(0, 0, 0, 1, cnx, cny, null));
				System.out.println(rr+"     "+isCut(cnx, cny, rr, 0, 0, rx, ry, 0, trSz, tmpBuf, 0));
				TrackSegment ts = TrackSegment.createTurnSeg(cnx, cny, rr, 0, 0, rx, ry);
				trackData.add(ts);
			}//*/
			double rad = lastSeg.type==0 ? Segment.MAX_RADIUS-1 : lastSeg.radius;
			boolean canGo = tryGotoPoint(speedV, speedRadius, turn, rx, ry, dx, dy,rad, 0, trSz, tmp,true);
			if (canGo){				

				//					TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, tmp[3], tmp[4]);
				//					trackData.add(ts);
				//					draw = true;
				if (mustPassPoint==null) 
					mustPassPoint = new Vector2D( tmp[3],tmp[4]);
				else {
					mustPassPoint.x = tmp[3];
					mustPassPoint.y = tmp[4];
				}
				//					if (mustPassPoint.y<=1) mustPassPoint.x = 0;				
				centerOfTurn = null;
				cntr = new Vector2D(tmp[0],tmp[1]);				
				rr = tmp[2];
			} else {
				double directSx = 0;
				double directSy = 0;
				for (int j=trSz-1;j>=0;--j){
					lastSeg = trArr[ trIndx[j]];
					lastS = (lastSeg.type==1) ? lastSeg.rightSeg : lastSeg.leftSeg;			
					if (lastSeg.type!=0 && lastSeg.type!=Segment.UNKNOWN){
						double dist = lastS.center.length();					
						if (dist>=lastS.radius+GAP){
							Geom.ptTangentLine(0, 0, lastS.center.x, lastS.center.y, lastS.radius+GAP, tmp);
							directSx = (lastSeg.type==TURNRIGHT) ? tmp[0] : tmp[2];
							directSy = (lastSeg.type==TURNRIGHT) ? tmp[1] : tmp[3];							
						} else continue; 

					} else {
						directSx = edgeDetector.highestPoint!=null ? edgeDetector.highestPoint.x : lastSeg.end.x;
						directSy = edgeDetector.highestPoint!=null ? edgeDetector.highestPoint.y : lastSeg.end.y;					
						//					draw = true;
					}
					if (isCutLine(0, 0, directSx, directSy, 0, trSz, tmp, GAP)!=0) 
						continue;
					else break;
				}
				if (directSx!=0 || directSy!=0){				
					if (mustPassPoint==null) 
						mustPassPoint = new Vector2D(directSx,directSy);
					else {
						mustPassPoint.x = directSx;
						mustPassPoint.y = directSy;
					}
				} else if (mustPassPoint!=null){
					directSx = mustPassPoint.x;
					directSy = mustPassPoint.y;
				} else {
					directSx = turn;
					directSy = 0;
				}
//				TrackSegment ts = TrackSegment.createStraightSeg(0, 0, 0, directSx, directSy);
//				trackData.add(ts);

				//					if (mustPassPoint.y<=1) mustPassPoint.x = 0;
				centerOfTurn = null;
				cntr = (lastS.type!=0 && lastS.type!=Segment.UNKNOWN) ? new Vector2D(lastS.center.x,lastS.center.y) : null;
				rr = (lastS.type==0) ? (lowestSpeed>=speedX) ? speedRadius+1 : speedRadius-3 : lastSeg.radius;
				dx = ox - directSx;
				dy = oy-directSy;
				canGoAtCurrentSpeed = canGoAtCurrentSpeed && (Math.sqrt(dx*dx+dy*dy)>=speedRadius);
				//				draw = true;
			}
			if (optimalPoint==null) 
				optimalPoint = new Vector2D(rx,ry);
			else {
				optimalPoint.x = rx;
				optimalPoint.y = ry;
			}
			return;			
		} else if (trSz==1){//if trsz==1
			Segment lastS = trArr [trIndx[0]];
			double gap = (lowestSpeed<speedX || lastS.radius<=60) ? GAP : (lastS.radius<=90) ? W : (lastS.radius<=120) ? 1.5*W : 2*W;
			double rad = (lastS.type==0) ? Segment.MAX_RADIUS-1 : lastS.radius-tW+gap;
			if (lastS.type!=0 && lastS.type!=Segment.UNKNOWN){
				Vector2D cnt = lastS.center;			
				double cnx = cnt.x;
				double cny = cnt.y;				
				if (lastS.center.length()>rad && Geom.ptTangentLine(0, 0, cnx, cny, rad,tmpBuf)>0){
					double mx = (lastSeg.type==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
					double my = (lastSeg.type==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];									
					if (mustPassPoint==null) 
						mustPassPoint = new Vector2D( mx,my);
					else {
						mustPassPoint.x = mx;
						mustPassPoint.y = my;
					}				
					DANGER = true;					
					Segment first = trArr[trIndx[0]];
					int tp = first.type;												
					drift = Math.abs(speedY)>HIGH_SPEEDY || speedX>lowestSpeed+FAST_MARGIN || speedX>lowestSpeed && toOutterEdge<SAFE_EDGE_MARGIN && lastSteer*tp<=0 && ((lastPos-curPos)*tp>0 || curAngle*tp<0 );

					rr = (tooFast || Math.abs(curPos)>1 || Math.abs(speedY)>HIGH_SPEEDY || toOutterEdge<SAFE_EDGE_MARGIN) ? rad+tW : radiusAtSpeed(lowestSpeed+FAST_MARGIN);
					if (cntr==null){
						cntr = new Vector2D(lastSeg.center);
					} else cntr.copy(lastSeg.center);
					//					double ddx = ox - mx;
					//					double ddy = oy-my;
					//					double dist = Math.sqrt(ddx*ddx+ddy*ddy);
					//					canGoAtCurrentSpeed = rad+tW*2-dist-3>=0;
					canGoAtCurrentSpeed = true;
					//					if (turn!=0 && carDirection.x*turn<-0.001) tryMaxTurn(carDirection, rr, turn);
					return;							
				}

				mustPassPoint.x = 0;
				mustPassPoint.y = 1;
				canGoAtCurrentSpeed =  true;
				rr = rad;
				if (true) return;


				//				if (speed>=lowestSpeed && relativeAngle<0 || toInnerEdge<-GAP && relativeAngle<0.1){
				//					if (mustPassPoint==null) 
				//						mustPassPoint = new Vector2D( turn,0);
				//					else {
				//						mustPassPoint.x = turn;
				//						mustPassPoint.y = 0;
				//					}
				//					rr = lastSeg.radius;
				//					return;
				//				}

				if (!caution && lastS.radius<120){												
					//				double wdth = (lastS.radius+W);
					rx =  cnx;
					ry = cny+lastS.radius+tW-0.5*W;
				} else {
					rx = lastSeg.end.x;
					ry = lastSeg.end.y;
					double tx = (lastSeg.type!=0) ? rx - lastSeg.center.x : lastSeg.end.y - lastSeg.start.y;
					double ty = (lastSeg.type!=0) ? ry - lastSeg.center.y : lastSeg.end.x - lastSeg.start.x;
					double t = Math.sqrt(tx*tx+ty*ty);
					int tp = (lastSeg.type==0) ? (rx>0) ? 1 : -1 : lastSeg.type;
					if (turnLeft!=0 || turnRight!=0){
						double ratio = W/t;
						if (caution){
							ratio = (ry>FAST_MARGIN) ? W/t : -W/t;
						} else if (turnLeft== turn || turnRight==turn){
							ratio = 0.75*W/t;
						}
						if (lastSeg.type==0){
							rx += tp*tx*ratio;
							ry += tp*ty*ratio;
						} else {
							rx -= tx*ratio;
							ry -= ty*ratio;
						}
					}

				}

				double mx = 0;
				double my = 0;
				if (isFirstSeg(lastSeg)){
					dx = cnx+turn*rad;
					dy = cny;
					my = ry*0.5;//

				} else {
					double ttx = (lastS.type==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
					double tty = (lastS.type==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;						
					double wdth = lastS.radius-tW+0.5*W;
					dx =  cnx-ttx*wdth;
					dy = cny-tty*wdth;
					my = dy*0.5;//					
				}
				//				Geom.getCircle(0,0,rx,ry,dx,dy,tmp);
				//				rr = Math.sqrt(tmp[2]);//*/
				rr = rad;
				//				cnx = tmp[0];
				//				cny = tmp[1];
				double dby = cny-my;
				mx = cnx-turn*Math.sqrt(rr*rr-dby*dby);

				int numInterSec = Geom.getLineCircleIntersection(0, 0, mx,my, cnx, cny, rr,tmp);
				boolean isCut = false;
				if (numInterSec==2){					
					if (tmp[1]>0) {
						isCut = true;
						mx = tmp[0];
						my = tmp[1];
					}
				} else if (numInterSec>2){					
					if (tmp[1]>0 && tmp[3]>0){				
						isCut = true;
						mx = tmp[0];
						my = tmp[1];
						if (my<0 || my>tmp[3] && tmp[3]>0) {
							mx = tmp[2];
							my = tmp[3];
						}
					} else {
						mx = carDirection.x;
						my = carDirection.y;
						isCut = false;
					}
				}

				if (isCut){
					dby = cnx-mx;
					my = cny+Math.sqrt(rr*rr-dby*dby);
				}

				////				

				//				if (!caution && cnt.length()>rad){					 
				//					Geom.ptTangentLine(0, 0, lastS.center.x, lastS.center.y, rad, tmp);					
				//					dx = (edgeDetector.turn==TURNRIGHT) ? tmp[0] : tmp[2];
				//					dy = (edgeDetector.turn==TURNRIGHT) ? tmp[1] : tmp[3];
				//				} else {


				//				}

				DANGER = (caution && lastSeg.end.y<=FAST_MARGIN);
				if (!DANGER){
					double ang = Vector2D.angle(0,1,rx,ry-1);
					DANGER = Math.abs(ang)>=MAX_ANGLE;
				}
//				TrackSegment ts = TrackSegment.createTurnSeg(cnx, cny, rr, 0, 0, mx, my);
//				trackData.add(ts);
//				ts = TrackSegment.createStraightSeg(0, 0, 0, mx, my);
//				trackData.add(ts);

				if (mustPassPoint==null) 
					mustPassPoint = new Vector2D( mx,my);
				else {
					mustPassPoint.x = mx;
					mustPassPoint.y = my;
				}
				if (optimalPoint==null) 
					optimalPoint = new Vector2D(x,ry);
				else {
					optimalPoint.x = rx;
					optimalPoint.y = ry;
				}
				//					if (mustPassPoint.y<=1) mustPassPoint.x = 0;
				centerOfTurn = null;
				cntr = new Vector2D(cnx,cny);
				rr = Math.max(rr, lastSeg.radius);								
			} else {
				rx = lastSeg.end.x;
				ry = lastSeg.end.y;
				dx = rx-lastSeg.start.x;
				dy = ry - lastSeg.start.y;
			}
			return;
		}
		

		if (inTurn){						
			//			Vector2D t = null;
			double tx = 0;
			double ty = 0;
			if (curSeg!=null && curSeg.type==0){
				//				t = (turn==TURNRIGHT) ? new Vector2D(SIN_PI_4,-SIN_PI_4) : new Vector2D(-SIN_PI_4,-SIN_PI_4);
				tx = (turn==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
				ty = (turn==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;
				if (mustPassPoint==null) 
					mustPassPoint = new Vector2D( cx-(tx*radiusSmall),cy-(ty*radiusSmall));
				else {
					mustPassPoint.x = cx-(tx*radiusSmall);
					mustPassPoint.y = cy-(ty*radiusSmall);
				}				
				//				mustPassPoint = center.minus(t.times(radiusSmall));
				double rr = radiusOfTurn-radiusSmall;
				if (centerOfTurn==null) 
					centerOfTurn = new Vector2D(cx+tx*rr,cy+ty*rr);
				else {
					centerOfTurn.x = cx+tx*rr;
					centerOfTurn.y = cy+ty*rr;
				}
				//				centerOfTurn = center.plus(t.times(radiusOfTurn-radiusSmall));		
			} else if (curSeg!=null){
				double d = distRaced - curSeg.dist;
				double arc = Math.PI/4-d/curSeg.radius;
				double sign = (curSeg.center!=null && curSeg.center.x<0) ? 1 : -1;
				double tmx = sign;
				double tmy = 0;
				double angle = -turn*arc;
				double sin = Math.sin(angle);
				double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(angle);				
				if (cos==-1.0) {
					tmx = -tmx;
					tmy = -tmy;
				} else if (cos!=1){
					double tmpx = tx;
					tmx = tmx * cos - tmy * sin;
					tmy = tmpx * sin + tmy * cos;
				}
				double tt = Math.sqrt(tmx*tmx+tmy*tmy);
				double tl = radiusSmall/tt;
				//				t = new Vector2D(sign,0).rotated(-turn*arc).normalised();
				if (mustPassPoint==null) 
					mustPassPoint = new Vector2D(cx+tmx*tl,cy+tmy*tl);
				else {
					mustPassPoint.x = cx+tmx*tl;
					mustPassPoint.y = cy+tmy*tl;
				}
				//				mustPassPoint = center.plus(t.times(radiusSmall));
				tl = (radiusOfTurn-radiusSmall)/tt;
				if (centerOfTurn==null)
					centerOfTurn = new Vector2D(cx - tmx*tl,cy - tmy*tl);
				else {
					centerOfTurn.x = cx - tmx*tl;
					centerOfTurn.y = cy - tmy*tl;
				}
				//				centerOfTurn = center.minus(t.times(radiusOfTurn-radiusSmall));
			}
			if (tx==0 && ty==0){
				optimalPoint = edgeDetector.highestPoint;
				if (mustPassPoint==null) 
					mustPassPoint = new Vector2D(0,1);
				else {
					mustPassPoint.x = 0;
					mustPassPoint.y = 1;
				}				
			} else {
				double angle = -turn*Math.PI*0.25;
				double sin = Math.sin(angle);
				double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(angle);				
				if (cos==-1.0) {
					tx = -tx;
					ty = -ty;
				} else if (cos!=1){
					double tmpx = tx;
					tx = tx * cos - ty * sin;
					ty = tmpx * sin + ty * cos;
				}
				//				t = t.rotated(-turn*Math.PI*0.25);
				if (optimalPoint==null) 
					optimalPoint = new Vector2D(centerOfTurn.x+tx*radiusOfTurn,centerOfTurn.y+ty*radiusOfTurn);
				else {
					optimalPoint.x = centerOfTurn.x+tx*radiusOfTurn;
					optimalPoint.y = centerOfTurn.y+ty*radiusOfTurn;
				}
				//				optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));
			}
			return;
		}		
		if (center==null) return;		
		double tx = (turn==TURNRIGHT) ? SIN_PI_4 : -SIN_PI_4;
		double ty = (turn==TURNRIGHT) ? -SIN_PI_4 : -SIN_PI_4;
		double tt = Math.sqrt(tx*tx+ty*ty);
		double tl = radiusSmall/tt;
		//		t = new Vector2D(sign,0).rotated(-turn*arc).normalised();
		if (mustPassPoint==null) 
			mustPassPoint = new Vector2D(cx-tx*tl,cy-ty*tl);
		else {
			mustPassPoint.x = cx-tx*tl;
			mustPassPoint.y = cy-ty*tl;
		}
		tl = (radiusOfTurn-radiusSmall)/tt;
		if (centerOfTurn==null){
			centerOfTurn = new Vector2D(cx + tx*tl,cy + ty*tl);
		} else {
			centerOfTurn.x = cx + tx*tl;
			centerOfTurn.y = cy + ty*tl;
		}
		//		Vector2D t = (turn==TURNRIGHT) ? new Vector2D(SIN_PI_4,-SIN_PI_4) : new Vector2D(-SIN_PI_4,-SIN_PI_4);					
		//		centerOfTurn = center.plus(t.times(radiusOfTurn-radiusSmall));
		//		mustPassPoint = center.minus(t.times(radiusSmall));
		//		optimalPoint = centerOfTurn.plus(new Vector2D(0,1).times(radiusOfTurn));
		if (centerOfTurn!=null){
			if (optimalPoint==null) 
				optimalPoint = new Vector2D(centerOfTurn.x,centerOfTurn.y+radiusOfTurn);
			else {
				optimalPoint.x = centerOfTurn.x;
				optimalPoint.y = centerOfTurn.y+radiusOfTurn;
			}
		}
	}

	/*private static final double steerAtRadius(double x){
		if (x>=100)
			return 0.0810250510703418-0.000750740530082075*x+2.89854924506364e-06*x*x-3.95924129765923e-09*x*x*x;
		if (x>=70)
			return -1.36459470940743+0.0582094289481451*x-0.000746679722815475*x*x+3.04328497896615e-06*x*x*x;
		if (x>=50)
			return -0.485794442078747+0.0364424208448416*x-0.000642936741947271*x*x+3.44706692338737e-06*x*x*x;

		return 1.02589841472933-0.0615964400025362*x+0.00163249602462468*x*x-1.46149044885214e-05*x*x*x;		
	}//*/

	/*private static final Vector2D higherPoint(Vector2D p1,Vector2D p2,Vector2D center){		
		double v1 = Math.abs(p1.minus(center).angle());
		double v2 = Math.abs(p2.minus(center).angle());	
		if (v1>=PI_2){
			v1 = Math.PI-v1;
			v2 = Math.PI-v2;
		}
		return (v1<v2) ? p2 : p1;
	}//*/

	/*private final Vector2D getCurrentHighestSeenPoint(Edge left,Edge right){

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

	private final Vector2D getNextHighestSeenPoint(Edge left,Edge right){		
		Vector2D p1 = (left.nextCenter==null) ? null : left.getHighestPoint();			
		Vector2D p2 = (right.nextCenter==null) ? null : right.getHighestPoint();

		if (p1==null) return p2;
		if (p2==null) return p1;
		if (Math.abs(left.nextRadius-right.nextRadius)-trackWidth<1){
			return higherPoint(p1,p2,left.center);
		}
		return null;
	}//*/

	/*private static final void estimateStartEnd(Segment s,double currentDist,double toMiddle){
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
	}//*/

	/*private final int analyze(Segment[] track,int from,int to){
		for (int i=from;i<to;++i){
			Segment s = track[i];			
			Segment t = (i<to-1) ?track[i+1] : null;
			while (t!=null && (s.type==t.type && Math.abs(s.radius-t.radius)<3)){
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
				int idx = i+2;
				occupied[ trIndx[idx-1] ] = 0;
				if ((to-=idx)>0) Segment.copySegment(track, idx, trArr, idx-1, to);
				to+=i+1;				
				s.dist = Math.min(s.dist, t.dist);
				s.length = Math.max(s.dist+s.length, t.dist+t.length) - s.dist;

				int dmax = Segment.double2int(s.radius);
				int maxn = s.map[dmax];
				for (int r = 0;r<Segment.MAX_RADIUS;++r){
					if (t.map[r]==0) continue;
					int nr = s.map[r];
					nr = Math.max(t.map[r],nr);
					s.map[r] = nr;
					if (nr>maxn){
						dmax = r;
						maxn = nr;
					}
				}
				if (s.center==null){
					s.start = t.start;
					s.end = t.end;
					s.center = t.center;
				}
				if (dmax!=Segment.double2int(s.radius)){
					if (dmax<0 && dmax<Segment.MAX_RADIUS){
						dmax = -dmax;
						//						Double2IntMap m = new Double2IntOpenHashMap();
						for (int j=0;j<Segment.MAX_RADIUS;++j)
							if (s.map[j]!=0) s.map[j] = -s.map[j];
						s.map = null;						
						s.type = -s.type;
					}
					s.radius = dmax;
					s.arc = s.type*s.length/s.radius;
					if (dmax<Segment.MAX_RADIUS) s.type=0;
					if (s.type!=0 && s.start!=null && s.center!=null){
						double rr = s.radius - s.type*trackWidth/2;
						double d_2 = s.start.distance(s.end);
						Vector2D p = s.start.plus(s.end);
						Vector2D n = s.end.minus(s.start).orthogonal().normalised();
						if (n.dot(s.center.minus(p))<0) n = n.negated();
						s.center = p.plus(n.times(rr*rr-d_2*d_2));
					}
				}				
				s.arc = s.type*s.length/s.radius;
				if (i==to-1) return to;
				t = track[i+1];								
			}					
		}
		return to;
	}//*/

	/*private final void storeTrack(EdgeDetector ed,CarState cs,ObjectArrayList<Segment> ol,ObjectArrayList<Segment> or){
		long ti = System.nanoTime();
		double dist = cs.getDistRaced();				

		int olSz = ol.size();
		int orSz = or.size();
		Segment[] olArr = ol.elements();
		Segment[] orArr = or.elements();
		for (int i=0;i<olSz;++i){
			Segment s = olArr[i];
			if (i==0){
				double sign = (s.start.y<0) ? -1 : 1;
				double d = (s.type!=0) ? sign*distance(new Vector2D(toMiddle,0), s.start, s.center, s.radius) : s.start.y;
				s.dist = dist+d;
			} 
			if (i<ol.size()-1){
				Segment t = ol.get(i+1);
				Segment.estimateDist(s, t);
			}
		}

		olSz = analyze(olArr,0,olSz);
		for (int i=0;i<orSz;++i){
			Segment s = orArr[i];
			if (i==0){
				double sign = (s.start.y<0) ? -1 : 1;
				double d = (s.type!=0) ? sign*distance(new Vector2D(toMiddle,0), s.start, s.center, s.radius) : s.start.y;
				s.dist = dist+d;
			} 
			if (i<or.size()-1){
				Segment t = or.get(i+1);
				Segment.estimateDist(s, t);
			}
		}
		orSz = analyze(orArr,0,orSz);
		if (debug) System.out.println("Time Store begin "+(System.nanoTime()-ti));

		tIndex = 0;
		int i = 0;
		for (int j =0;j<olSz;++j){
			Segment s = olArr[j];
			if (s.dist+s.length<=dist) continue;
			ar[i++] = s.dist;
			ar[i++] = s.dist+s.length;			
		}

		for (int j =0;j<orSz;++j){
			Segment s = orArr[j];
			if (s.dist+s.length<=dist) continue;
			ar[i++] = s.dist;
			ar[i++] = s.dist+s.length;
		}
		Arrays.quicksort(ar, 0, i-1);
		int len = i;
		int k=tIndex;
		int j=0;
		int ri =0;


		//		lTmp = ObjectArrayList.wrap(nsegAr, 0);
		//		tE = ObjectArrayList.wrap(nsegArr, 0);
		lTmp.size(0);
		tE.size(0);
		if (debug) System.out.println("Time Store start "+(System.nanoTime()-ti));

		for (i=0;i<len-1;++i){
			double d = ar[i];
			double e = ar[i+1];
			Segment s = null;
			Segment tmp = null;			
			int which = -2;
			while (j<olSz){
				s = olArr[j];
				if (s.dist<=d && s.dist+s.length>=e){					
					tmp = new Segment(s);															
					tmp.dist = d;
					tmp.length = e-d;
					tmp.arc = tmp.type*tmp.length/tmp.radius;
					which = -1;																				
				}
				if (s.dist+s.length>=e) break;
				if (s.dist+s.length<=e) j++;
			}//end of while
			//			System.out.println("Time Store "+i+" 0: "+(System.nanoTime()-ti));

			while (ri<orSz){				
				s = orArr[ri];
				if (s.dist<=d && s.dist+s.length>=e){
					if (tmp==null){
						tmp = new Segment(s);
						//						System.out.println("Time Store "+i+" 1 : "+(System.nanoTime()-ti));
						tmp.dist = d;
						tmp.length = e-d;
						tmp.arc = tmp.type*tmp.length/tmp.radius;
						which = 1;
					} else {						
						int dmax = Segment.double2int(tmp.radius);
						int maxn = tmp.map[dmax];
						if (tmp.type==s.type || s.type==0 || tmp.type==0){
							for (int r = 0;r<Segment.MAX_RADIUS;++r){
								if (s.map[r]==0) continue;
								int nr = s.map[r];
								nr += tmp.map[r];
								tmp.map[r] = nr;								
								if (nr>maxn){
									dmax = r;
									maxn = nr;
								}
							}

							if (dmax!= Segment.double2int(tmp.radius)){							
								tmp.radius = (int)Segment.int2double(dmax);
								tmp.arc = tmp.type*tmp.length/tmp.radius;
								if (tmp.type!=0 && tmp.type==s.type){									
									double rr = tmp.radius - which*tmp.type*trackWidth/2;
									double d_2 = tmp.start.distance(tmp.end)*0.5;
									Vector2D p = tmp.start.plus(tmp.end).times(0.5);
									Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
									if (n.dot(tmp.center.minus(p))<0) n = n.negated();
									tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
								} else tmp.center=null;
								if (dmax==Segment.MAX_RADIUS) 
									tmp.type=0;
								else tmp.type = s.type;

							}
						} else {
							for (int r = 0;r<Segment.MAX_RADIUS;++r){
								if (s.map[r]==0) continue;
								int nr = s.map[r];
								if (r==Segment.MAX_RADIUS-1)
									nr += tmp.map[r];
								else nr -= tmp.map[r];
								tmp.map[r] = -nr;								
								if (Math.abs(nr)>Math.abs(maxn)){
									dmax = (r==Segment.MAX_RADIUS-1) ? r : -r;
									maxn = nr;
								}
							}
							if (dmax!=tmp.radius){							
								tmp.radius = Math.abs(dmax);					
								if (dmax<0){
									tmp.type = -tmp.type;									
									for (int r = 0;r<Segment.MAX_RADIUS;++r){
										if (tmp.map[r]==0) continue;
										tmp.map[r] = - tmp.map[r];									
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
				}
				if (s.dist+s.length>=e) break;
				if (s.dist+s.length<=e) ri++;
			}//end of while
			//			System.out.println("Time Store "+i+" 1b : "+(System.nanoTime()-ti));


			if (trSz>0 && tmp!=null){
				while (k<trSz){
					s = trArr[k];
					double dd = Math.max(d,s.dist);
					double ee = Math.min(e,s.dist+s.length);
					double ll = ee-dd;
					if ((s.dist<=d && s.dist+s.length>=e) || (ll>0 && ll/(e-d)>0.8)){
						if (tmp==null){
							tmp = new Segment(s);				
							tmp.dist = d;
							tmp.length = e-d;
							tmp.arc = tmp.type*tmp.length/tmp.radius;
						} else {
							int dmax = Segment.double2int(tmp.radius);
							int maxn = tmp.map[dmax];
							if (tmp.type==s.type || s.type==0 || tmp.type==0){
								for (int r = 0;r<Segment.MAX_RADIUS;++r){
									if (s.map[r]==0) continue;
									int nr = s.map[r];
									nr += tmp.map[r];
									tmp.map[r] = nr;								
									if (nr>maxn){
										dmax = r;
										maxn = nr;
									}
								}
								if (dmax!=Segment.double2int(tmp.radius)){							
									tmp.radius = (int)Segment.int2double(dmax);
									tmp.arc = tmp.type*tmp.length/tmp.radius;
									if (tmp.type!=0 && tmp.type==s.type && tmp.center!=null){
										double rr = tmp.radius - which*tmp.type*trackWidth/2;
										double d_2 = tmp.start.distance(tmp.end)*0.5;
										Vector2D p = tmp.start.plus(tmp.end).times(0.5);
										Vector2D n = tmp.end.minus(tmp.start).orthogonal().normalised();
										if (n.dot(tmp.center.minus(p))<0) n = n.negated();
										tmp.center = p.plus(n.times(Math.sqrt(rr*rr-d_2*d_2)));
									} else tmp.center = null;
									if (dmax==Segment.MAX_RADIUS) 
										tmp.type=0;
									else tmp.type = s.type;

								}
							} else {
								for (int r = 0;r<Segment.MAX_RADIUS;++r){
									if (s.map[r]==0) continue;
									int nr = s.map[r];
									if (r==Segment.MAX_RADIUS-1)
										nr += tmp.map[r];
									else nr -= tmp.map[r];
									tmp.map[r] = -nr;									
									if (nr>=maxn){
										dmax = (Double.isInfinite(r)) ? r : -r;
										maxn = nr;
									}
								}
								if (dmax!=Segment.double2int(tmp.radius)){							
									tmp.radius = (int)Segment.int2double(Math.abs(dmax));
									tmp.arc = tmp.type*tmp.length/tmp.radius;
									if (dmax<0){
										tmp.type = -tmp.type;										
										for (int r = 0;r<Segment.MAX_RADIUS;++r){
											if (tmp.map[r]==0) continue;
											tmp.map[r] = -tmp.map[r];
										}										
									}
									if (tmp.type!=0 && tmp.center!=null){
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
				if (tmp!=null) lTmp.add(tmp);
			} else {//end of if
				if (tmp!=null) lTmp.add(tmp);
			}
			//			System.out.println("Time Store"+i+": "+(System.nanoTime()-ti));
		}//end of for

		if (tE!=null && tE.size()>0) lTmp.addAll(tE);
		int ltmpSz = lTmp.size();
		if (trSz>0) {			
			if ((trSz-=k)>0) Segment.copySegment(trArr, k, trArr, tIndex+ltmpSz, trSz);
			trSz+=ltmpSz;
			Segment.copySegment(lTmp.elements(), 0, trArr, tIndex, ltmpSz);									
		} else if (ltmpSz>0){
			Segment.copySegment(lTmp.elements(), 0, trArr, trSz, ltmpSz);
			trSz+=ltmpSz;
		}
		trSz = analyze(trArr, tIndex,trSz);
		System.out.println("Time Store: "+(System.nanoTime()-ti));
	};//*/



	private final void estimatePath(Vector2D hh){
		if (edgeDetector==null || edgeDetector.center==null) return;
		Vector2D center = edgeDetector.center;
		double width = trackWidth * (1-WIDTHDIV);		
		double radiusSmall = (edgeRadius-trackWidth/2)+width+W/2;
		//		double radiusLarge = (edgeRadius+trackWidth/2)-width-W;		
		//		if (centerOfTurn!=null && hh!=null){
		//			t = hh.minus(centerOfTurn).normalized();
		//			optimalPoint = centerOfTurn.plus(t.times(radiusOfTurn));				
		//		}


		if (centerOfTurn!=null){
			rr = radiusOfTurn;
			cntr = centerOfTurn;
			double cx = centerOfTurn.x;
			double cy = centerOfTurn.y;
			if (!isTurning) isTurning = (Math.sqrt(cx*cx+cy*cy)<radiusOfTurn);

			if (isTurning){
				int len = Geom.ptTangentLine(0, 0, center.x,center.y, radiusSmall,tmp);
				Vector2D point = (len==0) ? null : (len>2 && tmp[3]>tmp[1]) ? new Vector2D(tmp[2],tmp[3]) : new Vector2D(tmp[0],tmp[1]);
				if (point!=null && (mustPassPoint==null || mustPassPoint.y<3)) 
					mustPassPoint = point;

				Segment last = (trSz<=0) ? null : trArr[ trIndx[trSz-1]  ];				
				if (trSz==1){
					if (point!=null && optimalPoint!=null){
						//						if (optimalPoint==null) optimalPoint = point;
						Geom.getCircle2(mustPassPoint.x,mustPassPoint.y, optimalPoint.x,optimalPoint.y, 0,0, carDirection.x,carDirection.y, tmp);
						rr = Math.sqrt(tmp[2])-1;
						double ox = tmp[0];
						double oy = tmp[1];
						double nx = -carDirection.y;
						double ny = carDirection.x;											

						Geom.getLineLineIntersection(ox, oy, ox+nx, oy+ny, 0, 0, carDirection.x, carDirection.y, tmp);
						//if (point==null) 
						//point = new Vector2D(tmp[0],tmp[1]);
						//else {
						point.x = tmp[0];
						point.y = tmp[1];
						//}
						if (tmp[1]>0){
							cntr.x = ox;
							cntr.y = oy;
						} else {
							rr = radiusOfTurn;
							cntr = centerOfTurn;
						}
					} 
				} else if (inTurn && curSeg!=null && curSeg.type!=0 && trSz>0 && last!=null && last.type!=0 && last.type!=curSeg.type && l!=null){
					//						Segment last = tr.get(tr.size()-1);
					Vector2D v = last.end;
					//						Vector2D v0 = l.get(l.size()-1).start;
					Vector2D hp = edgeDetector.highestPoint;
					Geom.getCircle3(0,0, carDirection.x,carDirection.y, hp.x,hp.y,v.x,v.y, tmp);
					cntr.x = tmp[0];
					cntr.y = tmp[1];
					rr = Math.sqrt(tmp[2])-1;
				} else if (inTurn && curSeg!=null && last!=null && curSeg.type!=0 && trSz>0 && last.type==0 && last.type!=curSeg.type && l!=null){
					//						Segment last = tr.get(tr.size()-1);

					cntr = centerOfTurn;
					rr = radiusOfTurn;
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

		if (debug) System.out.println("mustP : "+mustPassPoint+"   Highest  "+edgeDetector.highestPoint+"   optimalP "+optimalPoint);
	}
	
	private static boolean isConfirmed(Segment s) {
		return Segment.isConfirmed(s.leftSeg, -1, tW) || Segment.isConfirmed(s.rightSeg, 1, tW); 
	}
	
	private final static boolean canGoToCircle(Vector2D carDirection, double speedX,double speedY,int type,double cnx,double cny,double radius) {		
		double speedRadius = radiusAtSpeed(speedX);
		double[] rs = new double[4];
		if (Geom.ptTangentLine(0, 0, cnx, cny, radius, rs)>0) {						
			double dx = (type==TURNRIGHT) ? rs[0] : rs[2];
			double dy = (type==TURNRIGHT) ? rs[1] : rs[3];
			double nx = type*carDirection.y;	
			double ny = -type*carDirection.x;			
			double ox = nx*speedRadius;
			double oy = ny*speedRadius;
			if (dy>0){
				dx -= ox;
				dy -= oy;					
				return (Math.sqrt(dx*dx+dy*dy)>=speedRadius);
			}
		}		
		return false;
	}
	
	private final static double goToCircle(Vector2D carDirection, double speedX,double speedY,int type,double cnx,double cny,double radius) {		
		double speedRadius = radiusAtSpeed(speedX);
		double[] rs = new double[4];
		if (Geom.ptTangentLine(0, 0, cnx, cny, radius, rs)>0) {						
			double dx = (type==TURNRIGHT) ? rs[0] : rs[2];
			double dy = (type==TURNRIGHT) ? rs[1] : rs[3];
			double nx = type*carDirection.y;	
			double ny = -type*carDirection.x;			
			double ox = nx*speedRadius;
			double oy = ny*speedRadius;
			if (dy>0){
				dx -= ox;
				dy -= oy;					
				return (Math.sqrt(dx*dx+dy*dy)-speedRadius);
			}
		}		
		return 0;
	}
	
	private static double faster(double targetSpeed,double m) {
		if (targetSpeed<70) 			
			return (m>40) ? targetSpeed+25+tW+1.5*m : (m>30) ? targetSpeed+25+tW+m : targetSpeed+25+tW+0.35*m;
		return (m>40) ? targetSpeed+FAST_MARGIN+tW+1.5*m : (m>30) ? targetSpeed+FAST_MARGIN+tW+m : targetSpeed+FAST_MARGIN+tW+0.35*m;
	}
	
	private static double maxAbs(double a,double b) {
		if (a>=0) return (b>=0) ? a>=b ? a : b : a>=-b ? a : b;
		return (b>=0) ? b>=-a ? b : a : (-b>=-a) ? b : a;
	}
	
	private static double minAbs(double a,double b) {
		if (a>=0) return (b>=0) ? a<=b ? a : b : a<=-b ? a : b;
		return (b>=0) ? b<=-a ? b : a : (-b<=-a) ? b : a;
	}
	
	private double deltaX(double mmx,double mmy,double m,double lowestSpeed,Segment lastSeg,double a,double b, double absSpeedY){
		double far =(speedX>lowestSpeed) ? lastSeg.type*(Math.max(tW*2-lgap,W)) : lastSeg.type*(Math.max(tW*2-lgap-W,W));
		if (m>35 && (distToEstCircle>-W*1.5 && b<TURNANGLE*0.75 || distToEstCircle>-W) && (speed<lowestSpeed+Math.max(m, FAST_MARGIN)*2 || distToEstCircle>=0 && b<TURNANGLE|| b<TURNANGLE*0.5)){
			return (m>35 && distToEstCircle<-GAP) ? lastSeg.type*(Math.max(tW-lgap,W)) :  far;
		} else if (m>25 && (distToEstCircle>-tW || distToEstCircle>-W) && (a>b || a<TURNANGLE) && (speed<lowestSpeed+Math.max(m, FAST_MARGIN)*3 || distToEstCircle>=0 && b<TURNANGLE || b<TURNANGLE*0.5)){
			return (m<35 && b<TURNANGLE*0.75 && distToEstCircle>-W) ? far : lastSeg.type*(Math.max(tW-lgap,W));
		} else if (m>20 || distToEstCircle>=0){
			if ((canGoToLastSeg || distToEstCircle>=0) && absSpeedY<=10)
				return (m>20) ? lastSeg.type*W : lastSeg.type*GAP;
			return distToEstCircle>-W && absSpeedY<10 && speed<lowestSpeed+Math.max(m, FAST_MARGIN)*3 || distToEstCircle>=0 ? lastSeg.type*GAP : 0;
		}
		return 0;
	}
	
	private double VERY_FAST(double lowestSpeed,double m){
		return (canGoToLastSeg || canGoAtCurrentSpeed || distToEstCircle>-W) 
				? lowestSpeed + FAST_MARGIN +tW+ m*1.5 : lowestSpeed + Math.max(FAST_MARGIN +tW,m)+ m;					
	}
	
	private double FAST(double lowestSpeed,double m){
		return (Math.abs(speedY)<MODERATE_SPEEDY && (canGoToLastSeg || canGoAtCurrentSpeed)) 
				? lowestSpeed + Math.max(FAST_MARGIN +tW,m)+ m : lowestSpeed + Math.max(m*2,FAST_MARGIN+tW + m*0.5);					
	}
	
	private double MODERATE(double lowestSpeed,double m){
		return (canGoToLastSeg || canGoAtCurrentSpeed) 
				? lowestSpeed + FAST_MARGIN +tW+ m*0.5 : lowestSpeed + FAST_MARGIN+tW + m*0.35;
	}
	
	private boolean isDangerous(double speedX,double lowestSpeed,double m){
		if (lowestSpeed<=90) return speedX>lowestSpeed+100;
		if (m>40 && speedX>VERY_FAST(lowestSpeed, m)) return true;
		if (m>FAST_MARGIN+tW && speedX>lowestSpeed+m*2+15) return true;		
		return speedX>FAST(lowestSpeed, m)+20;
	}
	
	private double danger(double a,double b,double absSpeedY,double steer,double lSteer){
		if (distToEstCircle<-GAP && b>TURNANGLE*0.5 || b>TURNANGLE*0.75 && distToEstCircle<0)
			return distToEstCircle>=0 || b<TURNANGLE/1.5 ? 0 : (distToEstCircle>=-GAP && absSpeedY<10) 
					? lastDistToEstCircle>distToEstCircle || a>TURNANGLE/1.5? (steer+lSteer)*0.5 : 0 
					: (distToEstCircle<-W || lastDistToEstCircle>distToEstCircle) ? (steer*lSteer<0) ? 0 : maxAbs(lSteer,steer) : steer*lSteer>=0 ? (steer+lSteer)*0.5 : 0;
		else if (a>0 && b>TURNANGLE*0.75){
			return (distToEstCircle<0) ? lSteer : (a>TURNANGLE/1.5 && absSpeedY<MODERATE_SPEEDY) ? (b>TURNANGLE) ? lSteer : lSteer*0.5 : 0;
		} else if (a>b && distToEstCircle<0)
			steer = (lSteer+steer)*0.5;
		return steer;
	}
	
	private boolean watchout(double a,double b){
		return distToEstCircle<0 && lastDistToEstCircle>distToEstCircle && b>TURNANGLE/1.5 && (a>TURNANGLE*0.5 || distToEstCircle<-W || distToEstCircle<-GAP && b>TURNANGLE || distToEstCircle<0 && relativeAngle<0);
	}
	
	private static boolean isNoNewSeg(Segment lastSeg){
		return edgeDetector.highestPoint==null || lastSeg!=null && lastSeg.type!=Segment.UNKNOWN && edgeDetector.highestPoint.y<=Math.max(lastSeg.leftSeg.end.y, lastSeg.rightSeg.end.y);
	}
	
	private static double stableSteer(double steer){
		double bal = Math.atan2(speedY, speedX);
		if (relativeAngleMovement>0.01){
			if (relativeAngleMovement>0.02 || relativeAngleMovement>lastRelativeAngleMovement) 
				return (steer*turn<=0) ? 
						(distToEstCircle>-GAP*0.5) ? 0 : steer 
						: relativeAngleMovement<0.02 && (slip>10 || landed || flyingHazard) ? bal*2 : turn;
			if (lastRelativeAngleMovement-relativeAngleMovement>0.01 || lastRelativeAngleMovement>0.02) 
				return (steer*turn>=0) 
						? (distToEstCircle<GAP*0.5) ? 0 : steer 
						: relativeAngleMovement>0.02 && (slip>10 || landed || flyingHazard) ? steer :-turn;
		} else if (relativeAngleMovement<-0.01){
			if (relativeAngleMovement<-0.02 || relativeAngleMovement<lastRelativeAngleMovement) 
				return (steer*turn>=0) 
						? (distToEstCircle<GAP*0.5) ? 0 : steer 
						: relativeAngleMovement<lastRelativeAngleMovement ? -turn : (slip>10 || landed || flyingHazard) ? steer :-turn;
			if (lastRelativeAngleMovement-relativeAngleMovement<-0.01 || lastRelativeAngleMovement<-0.02) 
				return (steer*turn<=0) 
						? (distToEstCircle>GAP*0.5) ? 0 : steer 
						: relativeAngleMovement<-0.02 && (slip>10 || landed || flyingHazard) ? steer : turn;
		} 
		return steer;
	}
	
	private static double forceSteer(double steer){
		double bal = Math.atan2(speedY, speedX);		
		if (relativeAngleMovement>0.01){
			if (relativeAngleMovement>0.02 || relativeAngleMovement>lastRelativeAngleMovement) 
				return (steer*turn<0) ? 0 : relativeAngleMovement<0.02 && (slip>10 || landed || flyingHazard) ? bal*2 : turn;
			if (lastRelativeAngleMovement-relativeAngleMovement>0.01 || lastRelativeAngleMovement>0.02) 
				return (steer*turn>=0) ? steer :  relativeAngleMovement>-0.02 && (slip>10 || landed || flyingHazard) ? -turn*0.15 :-turn;
		} else if (relativeAngleMovement<-0.01){
			if (relativeAngleMovement<-0.02 || relativeAngleMovement<lastRelativeAngleMovement) 
				return (steer*turn>0) ? 0 : relativeAngleMovement>-0.02 && (slip>10 || landed || flyingHazard) ? -turn*0.15 : -turn;
			if (lastRelativeAngleMovement-relativeAngleMovement<-0.01 || lastRelativeAngleMovement<-0.02) 
				return (steer*turn<=0) ? steer : relativeAngleMovement<0.02 && (slip>10 || landed || flyingHazard) ? bal*2 : turn;
		} 
		return steer;
	}
	
	private double simpleSteer(double steer,double absSpeedY,double absLastSpeedY){
//		hazard = 2;
		if (steer*turn<0) return (relativeAngleMovement<-0.01) 
				? -turn 
				:(relativeAngleMovement<-0.001) ? steer : (relativeAngleMovement<0.01) ? steer*0.5 
					: (relativeAngleMovement>0.02)
						? 0 
						: 0;
		return steer*0.5;
	}

	private final double fuzzySteering(CarState cs){
		//		double curPos = -cs.trackPos;
		double speedX = cs.speedX;
		double speedY = cs.speedY;		
		double speed = Math.sqrt(speedX*speedX+speedY*speedY);
		double absLastSpeedY = Math.abs(lastSpeedY);
		Segment first = trArr[trIndx[0]];
		isSafeToAccel = false;
		
		final double N = 1.0;					
		if (turn==STRAIGHT) {
			followedPath = false;
			maxSpeed = Double.MAX_VALUE;			
			recording = false;	
			isTurning = false;			
			lastSteer = curAngle/steerLock; 
			return lastSteer;
		}				

		double speedRadius = radiusAtSpeed(speed);
		double nx = turn*speedV.y;	
		double ny = turn*-speedV.x;			
		double ox = nx*speedRadius;
		double oy = ny*speedRadius;
		double mx = mustPassPoint.x;
		double my = mustPassPoint.y;
		double dx = ox - mx;
		double dy = oy - my;
		double rad = speedRadius;
		if (canGoAtCurrentSpeed && trSz>1) canGoAtCurrentSpeed = (Math.sqrt(dx*dx+dy*dy)>=rad);
		//		if (!canGoAtCurrentSpeed)			
		//			mustPassPoint.copy(mx,my);			


		double s = steerAtSpeed(speedX);		
		maxAngle = s*EdgeDetector.steerLock;
		//		if (highestPointEdge==0){//cannot decide the highest point belong to which edge
		//			prevSteer = gotoPoint(cs, trackDirection)+curPos/10;
		//			if (Math.abs(prevSteer)>=s*N)
		//				prevSteer = (prevSteer<0) ? -s*N : s*N;
		//			return prevSteer;
		//		}

		steer = Double.NaN;		
		//		if (edgeDetector.center==null){			
		//			lastSteer = gotoPoint(cs, trackDirection);
		//			if (Math.abs(lastSteer)>=N*s)
		//				lastSteer = (lastSteer<0) ? -s*N : s*N;
		//			//			if (turn!=UNKNOWN)
		//			//			prevSteer+=(0.3-curPos)*turn/25;
		//			return lastSteer;
		//		}
		double absSpeedY = Math.abs(speedY);
		//		double straightDist = (edge!=null && other!=null) ? other.straightDist : 0;

		//		double oldTargetRadius = targetRadius;				

		//		if (!isTurning){
		//			targetRadius = radiusOfTurn;
		//			//			targetRadius = Math.min(Math.min(radiusOfTurn, edgeRadius-trackWidth/2),nextRadius-trackWidth/2);
		//			//			steer = gotoPoint(cs, trackDirection)+(curPos+WIDTHDIV/4*turn)/20;
		//			steer = (centerOfTurn!=null) ? steerAtRadius(cs, centerOfTurn.x, centerOfTurn.y, radiusOfTurn-W) : gotoPoint(cs, trackDirection);						
		//		} else {
		//			if (nextRadius<=edgeRadius+trackWidth/2)
		//			targetRadius = Math.min(rr,radiusOfTurn);
		targetRadius = rr;
		//			else targetRadius = nextRadius;//Math.min(rr,edgeRadius+trackWidth/2);
		//			if (nextRadius>=rr)
		//				targetRadius = Math.max(nextRadius, rr)-trackWidth/4;
		//			else 
		//			targetRadius = Math.max(nextRadius,63);
		//			targetRadius = 63;

		//			targetRadius = Math.max(nextRadius, rr)-trackWidth/4;
		//			steer = (cntr!=null) ? steerAtRadius(cs, cntr.x, cntr.y, rr) : gotoPoint(cs, mustPassPoint);
		//			steer = gotoPoint(cs, mustPassPoint);
		//			steer = gotoPoint(cs, edgeDetector.highestPoint.plus(edge.getHighestPoint()).times(0.5));
		//			if (Math.abs(steer)>=s*N)
		//				steer = (steer<0) ? -s*N : s*N;

		//		}
		//		if (edgeRadius>100){
		//			targetRadius = Math.min(targetRadius, edgeRadius);			
		//		}

		//		if (Math.abs(steer)>s && (steer*speedY>0 || !DANGER)){
		//			steer = (steer<0) ? -s : s;
		//		}		

		draw = false;			
		//		if (absSpeedY>10 && relativeCurPos<0 && (speedY-lastSpeedY)*turn>0){
		//			steer =(steer<0) ? -s : s;
		//		}
		//		if (absSpeedY>30) {
		//			steer = -steer;
		//		}
		//		double minSpeed = speedAtRadius(edgeRadius);		
		int lowestSegIndx = 0;				
		Segment lowestSeg = null;		
		//		Segment highestSeg = null;
		rad = 1000;
		double highestRad = 0;
		Segment nextSlowSeg = null;
		Segment tt = null;

		for (int i = trSz-1;i>=0;--i){
			Segment t = trArr[ trIndx[i]];			
			if (t.type!=0 && t.radius<rad){
				rad = t.radius;
				lowestSeg = t;
				lowestSegIndx = i;				
			}

			if (t.type==0 || t.radius>highestRad){
				highestRad = (t.type==0) ? 1001 : t.radius;
				//				highestSeg = t;				
			}
		}				
		double highestSpeed = speedAtRadius(highestRad);
		double lowestSpeed = speedAtRadius(rad);				
		double first_speed = (first==null || first.type==0) ? Double.MAX_VALUE : speedAtRadius(first.radius);
		Segment lastSeg = (trSz>0) ? trArr[ trIndx[trSz-1]] : null;
		Segment lastS = (lastSeg==null) ? null : (lastSeg.type==TURNRIGHT) ?lastSeg.rightSeg : lastSeg.leftSeg;
		Segment lastO = (lastSeg==null) ? null : (lastSeg.type!=TURNRIGHT) ?lastSeg.rightSeg : lastSeg.leftSeg;
		double last_speed = (lastSeg==null || lastSeg.type==0) ? Double.MAX_VALUE : speedAtRadius(lastSeg.radius);		
		double relativeSpeedY = speedY*turn;		
		max = maxTurn || trSz>0 && lastSeg!=null && tryMaxTurn(speedV, speedRadius, turn);
		//		double relativeHeading = carDirection.x*turn;


		//		if (steer*turn>0 && relativePosMovement<0 && relativeAngle<0 && absSpeedY<MODERATE_SPEEDY) 
		//			steer = 0;
		canGoToLastSeg = false;				
		double dist = (lastSeg==null) ? 0 : (!inTurn && lastSeg.type!=0 && trSz==2 && lastSeg.lower!=null) ? Math.max(lastSeg.center.y,0) : Math.max(lastSeg.start.y,0);		
		rad = speedRadius;
		mLastX = 0;
		mLastY = 0;
		int i = 0;
		lgap = 0;
		Segment t = null;
		Segment prv = null;
		distToEstCircle = 0;
		boolean certain = true;
		boolean seenNewSeg = !isNoNewSeg(lastSeg);
		for (i = trSz-1;i>=0;--i){
			t = trArr[ trIndx[i]];		
			prv = first;			
			if (t.type!=0){
				double gap = 0;
				t = (t.type==TURNRIGHT) ? t.rightSeg : t.leftSeg;
				if (prv!=null) prv = (t.type==TURNRIGHT) ? prv.rightSeg : prv.leftSeg;
				//Vector2D cntr = t.center;
				double cnx = t.center.x;
				double cny = t.center.y;				
				double d = t.center.length();
				dx = 0;
				dy = 0;
				double sp = speedAtRadius(t.radius);
				double len = Geom.ptTangentDist(0, 0, cnx, cny, t.radius);
				boolean ok = true;
				if (speedX<lowestSpeed-20 && len>10 && (edgeDetector.highestPoint==null || edgeDetector.highestPoint.length()>=EdgeDetector.MAX_DISTANCE || !seenNewSeg || edgeDetector.highestPoint.length()>=60)) {
					gap = (edgeDetector.highestPoint==null || toInnerEdge<=-W*2 && !seenNewSeg) ? W*3 : W*2;
					while ((len=Geom.ptTangentDist(0, 0, cnx, cny, t.radius+gap))==0 && gap>W) gap-=0.25;					
					ok = false;
						
				}
				if (ok || possibleChangeDirection) {
					gap = (t.radius<=60) ? GAP : (t.radius<=90) ? W : (t.radius<=120) ? 1.5*W : 2*W;
					if (sp<speed-FAST_MARGIN-tW-len || sp<speed-FAST_MARGIN && toInnerEdge+gap>-0.2) {
						nextSlowSeg = t;
						gap = Math.min(gap,GAP);
					} else if (sp<speed-15 && first_speed>sp || possibleChangeDirection)
						gap = Math.min(gap,W);
					
					if (toInnerEdge<0)
					while ((sp<speedX || relativePosMovement<-0.001) && toInnerEdge>-gap  || d>t.radius && d<t.radius+gap) gap*= 0.5; 
				}
				
				double rd = t.radius+gap;
				if (d>rd){//outside
					Geom.ptTangentLine(0, 0, cnx, cny, rd, tmp);
					dx = (t.type==TURNRIGHT) ? tmp[0] : tmp[2];
					dy = (t.type==TURNRIGHT) ? tmp[1] : tmp[3];
					
					if (t.start.y>0 && t.start.y<dist) dist = t.start.y;
					if (dy>0){
						d = rd/t.radius;
						if (prv!=null && i>0 && dy<prv.end.y) {
							certain = false;
							if (prv.upper!=null && prv.upper.y<t.start.y) {
								dx = prv.upper.x - cnx;
								dy = prv.upper.y -cny;
								dx = cnx+dx*d;
								dy = cny+dy*d;
							} else if (first_speed<sp) {
								dy = (prv.end.y+t.start.y)*0.5 - cny;
								dx = -t.type*Math.sqrt(t.radius*t.radius-dy*dy);
								dx = cnx+dx*d;
								dy = cny+dy*d;
								d = Math.max(prv.start.y,dy*0.5);
								dx *= dy/d;
								dy = d;
							}													
						} 
//						else if (prv!=null && i==trSz-1 && dy>prv.end.y){
//							dx = prv.end.x;
//							dy = prv.end.y;
//						}
						if (mLastY==0){						
							mLastX = dx;
							mLastY = dy;
							tt = t;
							dx -= ox;
							dy -= oy;
							lgap = gap;
							if (gap<=GAP) {
								distToEstCircle = (Math.sqrt(dx*dx+dy*dy)-speedRadius);								
							} else {
								Geom.ptTangentLine(0, 0, cnx, cny, t.radius+GAP, tmp);
								dx = (t.type==TURNRIGHT) ? tmp[0] : tmp[2];
								dy = (t.type==TURNRIGHT) ? tmp[1] : tmp[3];
																
								if (dy>0){
									if (prv!=null && i>0 && dy<prv.end.y) {
										if (prv.upper!=null) {
											dx = prv.upper.x - cnx;
											dy = prv.upper.y -cny;								
										} else {
											dy = (prv.end.y+t.start.y)*0.5 - cny;
											dx = -t.type*Math.sqrt(t.radius*t.radius-dy*dy);
										}
										d = (t.radius+GAP)/t.radius;
										dx = cnx+dx*d;
										dy = cny+dy*d;
									}
									dx -= ox;
									dy -= oy;
									distToEstCircle = (Math.sqrt(dx*dx+dy*dy)-speedRadius);
								}
								
							}
							canGoAtCurrentSpeed = (distToEstCircle>=0);							
							if (canGoAtCurrentSpeed && !certain) continue;
						} else {
							dx -= ox;
							dy -= oy;
						}
						double x = Math.sqrt(dx*dx+dy*dy)-rad;
						if (distToEstCircle<0 || !certain){							
							if (x>=0){
								if (distToEstCircle<0){
									distToEstCircle = x;
									mLastX = dx+ox;
									mLastY = dy+oy;
									lgap = gap;
								}
							} else if (!certain) 
								distToEstCircle = (distToEstCircle>0) ? x :minAbs(distToEstCircle, x);
						}
						canGoAtCurrentSpeed = (x>=0);
						
					} else canGoAtCurrentSpeed = false;
				} else {
					/*canGoAtCurrentSpeed = speedRadius<t.radius+tW && Vector2D.distance(ox, oy, ex, ey)>=rad+tW && (max || toInnerEdge>=-W*0.5);
					if (canGoAtCurrentSpeed && (i==trSz-1 || lastS.type==t.type && lastS.radius>=t.radius) ){						
						mLastX = ex;
						mLastY = ey;
						canGoToLastSeg = true;
						tt = t;
						lgap = gap;
					}
					if (i==trSz-1) lgap = gap;//*/
					canGoAtCurrentSpeed = false;
					continue;
				}
				
				/*if (!canGoAtCurrentSpeed && speedRadius<t.radius+tW && (max || toInnerEdge>=-W*0.5) && (d = Vector2D.distance(ox, oy, ex, ey))>speedRadius){
//					d = Vector2D.distance(ex, ey, ox, oy);
					canGoAtCurrentSpeed = d>=rad+tW;
					if (canGoAtCurrentSpeed && (i==trSz-1 || lastS.type==t.type && lastS.radius>=t.radius)){						
						mLastX = ex;
						mLastY = ey;
						canGoToLastSeg = true;
						tt = t;
						lgap = gap;
						continue;
					}
				}//*/
				
				if (i==trSz-1) 
					canGoToLastSeg = canGoAtCurrentSpeed;
				if (canGoAtCurrentSpeed || certain) break;
			}
		}

		/*if (i<trSz-1) {			
			t = trArr[ trIndx[trSz-1]];
			if (t.type!=0){
				t = (t.type==TURNRIGHT) ? t.rightSeg : t.leftSeg;
				//Vector2D cntr = t.center;
				double cnx = t.center.x;
				double cny = t.center.y;
				double rd = t.radius+GAP;
				double d = t.center.length();
				dx = 0;
				dy = 0;										
				if (d>rd){//outside
					Geom.ptTangentLine(0, 0, cnx, cny, rd, tmp);
					dx = (t.type==TURNRIGHT) ? tmp[0] : tmp[2];
					dy = (t.type==TURNRIGHT) ? tmp[1] : tmp[3];
					mLastX = dx;
					mLastY = dy;
					if (dy>0){
						dx -= ox;
						dy -= oy;					
						canGoToLastSeg = (Math.sqrt(dx*dx+dy*dy)>=rad);								
					} else canGoToLastSeg = false;
				}		
			}

		}//*/


		//		if (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.length()>95 && absSpeedY<=HIGH_SPEEDY){
		//			steer = gotoPoint(cs, edgeDetector.highestPoint);
		//			if (speed<lowestSpeed) {
		//				steer = (steer+lastSteer)*0.5;
		//			}
		//			
		//			maxSpeed = speed + 10;
		//			return steer;
		//		}
		
		double a = (mLastY==0) ? 0 : turn*Vector2D.angle(carDirection.x,carDirection.y,mLastX, mLastY);
		double b = 0;
		double c = 0;
		if (trSz>0 && tt!=null && tt.type!=0 && tt.center.length()>tt.radius){
			Geom.ptTangentLine(0, 0, tt.center.x, tt.center.y, tt.radius, tmp);
			dx = (tt.type==TURNRIGHT) ? tmp[0] : tmp[2];
			dy = (tt.type==TURNRIGHT) ? tmp[1] : tmp[3];
			a =  turn*Vector2D.angle(carDirection.x, carDirection.y, dx,dy);
			b = turn*Vector2D.angle(0, 1, dx,dy);			
		} else b = (mLastY==0) ? 0 : turn*Vector2D.angle(0,1,mLastX, mLastY);
		c = Math.max(a,b);
		
		boolean mustTurn = lastSeg!=null && lastSeg.type!=0 && lastSeg.radius<30 ? a>=TURNANGLE*2 : a>=TURNANGLE || !canGoToLastSeg && a>=TURNANGLE*0.5;
			
		double angle = (lastSeg!=null && (lastSeg.type==0 || speedRadius<lastSeg.radius) || mLastX==0 && mLastY==0) ? Vector2D.angle(speedV.x, speedV.y, mustPassPoint.x, mustPassPoint.y) 
				: Vector2D.angle(speedV.x, speedV.y, mLastX,mLastY);
		if (angle>Math.PI) angle-=Math.PI*2;
		if (angle<-Math.PI) angle+=Math.PI*2;
//		if (lastSeg!=null && (lastSeg.type==0 || speedRadius<lastSeg.radius)) angle*=0.5;
		relativeTargetAngle = angle*turn;
		boolean isFast = speed>highestSpeed || speed>last_speed;
		double m = Math.sqrt(mLastX*mLastX+mLastY*mLastY);	
		
		/*if (canGoVeryFast && m<20 && a>0 && b>0 && absSpeedY<HIGH_SPEEDY){
			steer = (relativeAngleMovement<-0.01) ? -turn : simpleSteer(-turn*a/steerLock, absSpeedY, absLastSpeedY);//gotoPoint(cs, edgeDetector.highestPoint);
			maxSpeed = speedX+2;
			return 0;
		}//*/
		
		
		boolean possiblyGoFast = false;
		if (distToEstCircle>-tW){
			for (i = 0;i<trSz;++i){
				t = trArr[trIndx[i]];
				if (t.start.y>20) break;				
				if (t.type==0 || speedAtRadius(t.radius)>=speedX+15){
					possiblyGoFast = (t.start.y<=10 || toOutterEdge>Math.max(tW,SAFE_EDGE_MARGIN) || toOutterEdge>=tW && relativeAngle>=-0.01 && relativeAngleMovement>0.001 && absSpeedY<HIGH_SPEEDY ) ? true : false;
					break;
				}
			}
		}
		double expectedSpeed = lowestSpeed;
		if (possiblyGoFast) 
			expectedSpeed = last_speed;
		else if (absSpeedY<HIGH_SPEEDY && trSz>1){
			if (canGoToLastSeg)
				expectedSpeed = last_speed;
			else if (lowestSeg!=null && lowestSegIndx==0 && lowestSeg.end.y<=10 && (toOutterEdge>Math.max(tW,SAFE_EDGE_MARGIN) || distToEstCircle>-W)){
				expectedSpeed = highestSpeed;
				for (i = 1;i<trSz;++i){
					t = trArr[trIndx[i]];
					double sp = speedAtRadius(t.radius);
					if (expectedSpeed>sp) expectedSpeed = sp;
				}
			}
		}
		
		if (debug){
			System.out.println("a =  "+a);
			System.out.println("b =  "+b);
			System.out.println("c =  "+c);
			System.out.println("m =  "+m);
			System.out.println("toOutterEdge =  "+toOutterEdge);
			System.out.println("toInnerEdge =  "+toInnerEdge);
			System.out.println("FlyingHazard =  "+flyingHazard);
			System.out.println("StartFlying =  "+startFlying);
			System.out.println("Landed =  "+landed);
			System.out.println("StartLanding =  "+startLanding);
			System.out.println("maxTurn =  "+maxTurn);
			System.out.println("PossibleChangeDirection =  "+possibleChangeDirection);		
			System.out.println("--------CHU Y VAO DAY------ "+distToEstCircle);
		}
		Vector2D highest = edgeDetector.highestPoint;
		Vector2D ahead = edgeDetector.currentPointAhead;
		boolean isDanger = isDangerous(speedX, lowestSpeed, m);
		boolean dangerSlip = dangerSlip();
		double bal = Math.atan2(speedY, speedX);
		
		if (trSz>1 && turn!=0 && m<100 && lastSeg.type!=0 && lastSeg.type!=Segment.UNKNOWN && lastSeg.center!=null && turn==lastSeg.type){
			double lSteer = gotoPoint(cs, mLastX,mLastY);
			if (!inTurn){
				for (i = 1;i<trSz;++i){
					nextSlowSeg = trArr[trIndx[i]];
					if (nextSlowSeg.type!=0 && nextSlowSeg.type!=Segment.UNKNOWN) break;
				}
//				double cnx = nextSlowSeg.center.x;
				double cny = nextSlowSeg.center.y;
				//				double dd = (!inTurn) ? tW+tW-W : tW-W;
				double mmx = 0;				
				double mmy = 0;
				if ((isFast && speed>lowestSpeed-m || canGoToLastSeg && m>30) &&(speed>lowestSpeed+Math.max(FAST_MARGIN,m)*2.5 && m>15 && c<=TURNANGLE*2 || c<=TURNANGLE*2 && m>20 || cny>30  || (m>35 || cny>25) && canGoToLastSeg) || m>45){					
//					if (m>5 && m<30 && speed<lowestSpeed+Math.max(FAST_MARGIN,m)*2.5){
//						mmx = mLastX;
//						mmy = mLastY;
//						mLastX = mmx;
//						mLastY = mmy;
//						if (mmy>15)
//							mmx -= lastSeg.type*W;
//						if (canGoToLastSeg || canGoAtCurrentSpeed){ 
//							steer = gotoPoint(cs, mmx,mmy);
//							maxSpeed = lowestSpeed + FAST_MARGIN + m*0.5;
//							if (m<15 && steer*turn>0) steer = 0;
//						} else {
//							steer = gotoPoint(cs, mmx,mmy);
//							maxSpeed = lowestSpeed + FAST_MARGIN + m*0.5;
//						}
//												
//						//						if (steer *turn<0 && canGoToLastSeg) 
//						//							steer =0;						
//
//
//					} else {
//						mmy = cny;
//						//						mmx = toMiddle - turn*(tW-W);
//						if (speed<150) t
//							mmx = cnx - turn*(nextSlowSeg.radius+tW-W);
//						else mmx = cnx - turn*(nextSlowSeg.radius+GAP);
//						mLastX = mmx;
//						mLastY = mmy;
						//						double angle = Math.abs(Vector2D.angle(carDirection.x,carDirection.y,0, 1));
						mmx = mLastX;
						mmy = mLastY;						
						
						mmx -= deltaX(mmx,mmy,m, lowestSpeed, lastSeg, a, b, absSpeedY);
								
						//						else if (angle<0.15) 
						steer = gotoPoint(cs, mmx,mmy);

//						if (canGoToLastSeg || canGoAtCurrentSpeed) 
//							maxSpeed = (m>40) ? lowestSpeed + Math.min(FAST_MARGIN +tW+ m*1.5,100) : lowestSpeed + Math.min(FAST_MARGIN+tW + m,100);
//						else maxSpeed = (m>40) ? lowestSpeed + Math.min(FAST_MARGIN +tW+ m,100) : lowestSpeed + FAST_MARGIN +tW+ m*0.5;
						maxSpeed = (m>40 && distToEstCircle>-W) ? VERY_FAST(expectedSpeed, m) : (distToEstCircle<-W && isDanger || distToEstCircle<-W*1.5) ? MODERATE(expectedSpeed, m) : FAST(expectedSpeed, m);
						if (relativeSpeedY>MODERATE_SPEEDY) maxSpeed = Math.min(maxSpeed, faster(expectedSpeed, m));
//					}
											
					
					mustPassPoint.x = mmx;
					mustPassPoint.y = mmy;
					isSafeToAccel = true;
					angle = Vector2D.angle(speedV.x, speedV.y, mLastX,mLastY);
					if (angle>Math.PI) angle-=Math.PI*2;
					if (angle<-Math.PI) angle+=Math.PI*2;
//					if (lastSeg!=null && (lastSeg.type==0 || speedRadius<lastSeg.radius)) angle*=0.5;
					relativeTargetAngle = angle*turn;
					//					if (speed>=150) steer*=0.5;
					if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad && isConfirmed(lastSeg) && lastSeg.end.y-lastSeg.start.y>3) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
					
					if (a>0 && steer*turn<0 && isDanger){
						steer = danger(a, b, absSpeedY, steer, lSteer);
						if (steer*turn>=0 && distToEstCircle<0 && speedX>=maxSpeed+15){
							steer = (relativeAngleMovement<-0.001) ? -turn : (relativeAngleMovement<0.01) ? lSteer : lSteer*0.5;
						}
					} else
					if (a<0 && steer*turn<0) {
						steer = -1.5*a*turn/steerLock;						
					} else 
					if (a<0 && steer*turn>0){
						if (absSpeedY>MODERATE_SPEEDY && (relativeAngleMovement<-0.001 && lastRelativeAngleMovement>0 || relativeAngleMovement<0.01 && lastRelativeAngleMovement>0.01)){
							steer = 0;
							hazard = 2;
						} else if (relativePosMovement>0.01 || relativeAngleMovement>0.001) 
							steer = (relativeAngleMovement<-0.01) ? steer*0.5 : (relativeAngleMovement<-0.001) ? steer : turn;
						else if (relativeSpeedY>0) 
							steer *= (absSpeedY>HIGH_SPEEDY) ? 0.3 : (absSpeedY>MODERATE_SPEEDY) ? 0.5 : 1;						
//						else steer = turn;p
						if (canGoToLastSeg && a>-0.1)
							maxSpeed = Math.min(Math.max(last_speed+m*2,speedX-1),maxSpeed);
						else if (!maxTurn || relativePosMovement>0.01 && toInnerEdge>-W || relativePosMovement>0.001 && relativeAngleMovement>0.01
								|| a<-0.1 && relativePosMovement>0.001 && relativeAngleMovement<-0.001 || a<-0.3) maxSpeed = Math.max(Math.min(maxSpeed,speedX-1),lowestSpeed-tW);					
					} else if (steer*turn>0){
						if (isDanger){
							steer = danger(a, b, absSpeedY, steer, lSteer);
							if (steer*turn<=0 && b>TURNANGLE*0.5 && distToEstCircle<0 && speedX>maxSpeed+15 && (distToEstCircle<lastDistToEstCircle || distToEstCircle<-GAP)){
								steer = stableSteer(-turn);
								hazard = 2;
							} else if (steer*turn>0 && b>TURNANGLE*0.5 && distToEstCircle>0 && speedX>maxSpeed+15) 
								steer = 0;
							else if (steer*turn>=0){
								steer = (distToEstCircle<GAP && b>TURNANGLE*0.5) ? 0 : steer;
								hazard = 2;
							}
						} else if (isOffBalance && a>0 && distToEstCircle>=-W)
							steer = (m<30 && distToEstCircle<-GAP && a>TURNANGLE/1.5 || b>TURNANGLE && distToEstCircle<-GAP || speedX>maxSpeed+10 && distToEstCircle<0) 
								? (Math.abs(lSteer)>Math.abs(steer)) ? (lSteer+steer)*0.5 : 0 
								: (relativeAngleMovement<-0.01 || b>TURNANGLE || distToEstCircle<0 && (relativeAngleMovement>0.01 || speedX>maxSpeed+10)) ? lSteer*0.5 : steer*0.85;
						else if ((!canGoToLastSeg || speed>maxSpeed) && (distToEstCircle<-W && b>TURNANGLE*0.75 || distToEstCircle<-W*1.5 || speed>maxSpeed+10 && distToEstCircle<0 && c>TURNANGLE*0.75 || speed>maxSpeed+15 && distToEstCircle<0 || speed>maxSpeed && b>TURNANGLE && distToEstCircle<0)){ 
//							if (relativeAngle<-0.001){
//								steer = relativeAngle*turn/steerLock;
//							} else if (relativeTargetAngle>0.001 && relativeAngleMovement<-0.001 && relativePosMovement<-0.001){
//								if (slip>0) 
//									steer = lSteer;
//								else steer = (steer+lSteer)*0.3;
//							} else steer = (distToEstCircle<-W*1.5) ? 0 :steer;
							steer = (speed>maxSpeed+10 && distToEstCircle<0 && b<TURNANGLE*1.5 && last_speed<85) ? steer :lSteer;
							
//							if (speed>maxSpeed && steer*turn>0) steer=0;
						}  else if (speedX>faster(last_speed,m) && (distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle && lastSteer*turn>=0 || b>TURNANGLE*0.75 && absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<-0.001)){ 
							steer = (relativeAngleMovement<-0.01 && b>TURNANGLE*0.5) ? -turn : 0;
							hazard = 2;	
						} else if (relativeSpeedY>0) 
							steer = (relativeAngleMovement<-0.01 || b>TURNANGLE*0.75 && speedX>maxSpeed+10 && absSpeedY>MODERATE_SPEEDY && m>15) ? 0 : steer*0.5;
						
					} else if (relativeTargetAngle>0 && steer*turn>0 && !canGoAtCurrentSpeed && !canGoToLastSeg)
						steer = 0;

					if (maxSpeed>speedX && (steer*turn<0 && relativeAngle<0 && distToEstCircle<=-W*1.5 || !maxTurn && relativeAngle*first.type<0)) maxSpeed = speedX-1;
					
					if (steer*turn<0 && a>0 && distToEstCircle>-W && distToEstCircle>lastDistToEstCircle)
						steer = (relativeAngle>0 && (a<TURNANGLE/1.5 && distToEstCircle>-GAP || !isDanger && distToEstCircle>0)) 
								? (maxSpeed<speedX-10 && absSpeedY>=MODERATE_SPEEDY) ? (relativeAngleMovement<-0.01) ? -turn : (relativeAngleMovement<-0.001 || b>TURNANGLE && distToEstCircle<0 && speedX>maxSpeed+20) 
										? lSteer : (relativeAngleMovement>0.01) 
												? (isDanger) ? steer : 0 : lSteer*0.5 : (isDanger) ? steer : 0 
								: (distToEstCircle<-W || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle || distToEstCircle<0 && m<20) && ((a>TURNANGLE || isDanger && a>TURNANGLE*0.5) && b>TURNANGLE*0.75) ? lSteer : 
							(b>TURNANGLE && a>TURNANGLE*0.5 && speedX>=maxSpeed+15 && m<15) ? maxAbs(steer, lSteer) : steer;
					else if (steer*turn<0 && distToEstCircle<0 && (relativeAngleMovement<-0.001 || distToEstCircle<lastDistToEstCircle) && relativePosMovement<-0.001)
						steer = (relativePosMovement<-0.01 || relativeAngleMovement<-0.01 || speedX>lowestSpeed) ? -turn : steer;
					else if (steer*turn<0 && !isDanger && distToEstCircle>-W && isOffBalance && absSpeedY<10)
						steer = (distToEstCircle<-GAP) ? distToEstCircle>lastDistToEstCircle ? maxAbs(steer,lSteer*0.5) : maxAbs(steer,lSteer)  
													   : minAbs(steer, lSteer)*0.5;
					
//					if (b>TURNANGLE && a>0 && isDanger && distToEstCircle<0 && distToEstCircle<lastDistToEstCircle){
//						steer = -turn;
//						hazard = 2;
//					} else 
					if (b>TURNANGLE && speedX>maxSpeed && a>0 && steer*turn>0)
						steer = (distToEstCircle>0 && distToEstCircle>lastDistToEstCircle) ? (absSpeedY>=MODERATE_SPEEDY) ? lSteer : (m>20 && absSpeedY<10) ? steer : 0 : (distToEstCircle>0 && (a<TURNANGLE/1.5 || absSpeedY>MODERATE_SPEEDY)) ? 0 : (isDanger && (a>TURNANGLE*0.75 || lastDistToEstCircle>distToEstCircle && distToEstCircle<-GAP)) ? lSteer 
								: (distToEstCircle<-GAP || a>TURNANGLE) ? (lSteer+steer)*0.5 : (m>25) ? steer : 0;
					else if (b>TURNANGLE/1.5 && (distToEstCircle<-(W*1.5) || distToEstCircle<-W && m>30 && speedX>lowestSpeed + FAST_MARGIN +tW+ m) && relativeSpeedY>0) {
						steer = (absSpeedY<10 && distToEstCircle>-W) ? maxAbs(steer,lSteer) 
								: (relativeAngleMovement<0.01) ? -turn : relativeAngleMovement>0.02 && relativeAngleMovement>lastRelativeAngleMovement ? steer*0.5 : steer;
					} else if (watchout(a, b))
						steer = (speedX<lowestSpeed-15 && a>0 && distToEstCircle>-tW) ? minAbs(steer,lSteer*0.5) : (m>30 && speedX<MODERATE(lowestSpeed, m) && a<TURNANGLE) ? relativeAngle>0 ? 0  : maxAbs(steer,lSteer*0.5) : 
							(distToEstCircle<-W || (distToEstCircle<-GAP || distToEstCircle<0 && lastSteer*turn<0 && relativeAngleMovement<-0.001) && isDanger || b>TURNANGLE && a>TURNANGLE/1.5 && distToEstCircle<-GAP || b>TURNANGLE && isDanger && speedX>maxSpeed+20 && distToEstCircle<0) ? distToEstCircle<lastDistToEstCircle 
									? (canGoVeryFast) ? lSteer :-turn 
									: (lastSteer+steer)*0.5
								: steer;
					
					if (steer*turn>=0 && speedX<maxSpeed && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle && (c>TURNANGLE*0.75 && a>=TURNANGLE*0.5 || distToEstCircle<-GAP)))
						steer = (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer, lSteer) :  (steer+lSteer)*0.5;
					else if (steer*turn<0 && maxSpeed>speedX && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle && (b>TURNANGLE*0.75 || distToEstCircle<-GAP) || b>TURNANGLE*0.75 && a>=TURNANGLE*0.5))
						steer = (relativeSpeedY<0) ? (steer*lSteer>0) ? minAbs(steer, lSteer) : steer 
												   : (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer, lSteer) : maxAbs(steer, lSteer*0.5);
						
					if ((m<20 || a>TURNANGLE*0.75) && relativeSpeedY>0 && isFast && a>TURNANGLE*0.5 && b>TURNANGLE && relativePosMovement>0 && distToEstCircle<-GAP){
						steer = (m<20 || m<25 && distToEstCircle<-GAP) ? -turn : maxAbs(lSteer,steer);
					} else
					if (steer*turn<=0 && (distToEstCircle<-GAP || relativeAngle<0 && lastDistToEstCircle>distToEstCircle && distToEstCircle<0) && (isDanger || absSpeedY>=MODERATE_SPEEDY)) 
						steer = (isOffBalance && distToEstCircle<-GAP || relativeSpeedY<0) ? maxAbs(steer,lSteer*0.5) : (distToEstCircle>-GAP || lastDistToEstCircle<distToEstCircle && distToEstCircle>-W) ? steer : maxAbs(steer,lSteer);
					
					if (relativeSpeedY<0 && steer*turn<0 && speedX>maxSpeed) steer = minAbs(steer,lSteer)*0.5;
					if (!canGoVeryFast && (relativeAngleMovement<-0.001 && relativePosMovement<-0.01 || relativeAngleMovement<-0.01 && relativePosMovement<0) && speedX>first_speed && !flyingHazard && slip<20) steer = -turn;
					if (speedX<maxSpeed && distToEstCircle>0 && relativeAngleMovement>0.01){
						steer = relativeSpeedY>0 && steer*turn>=0 && relativeAngleMovement>lastRelativeAngleMovement && a<TURNANGLE*0.5 ? turn : steer;
//						hazard = 2;
					} else if (speedX<maxSpeed && steer*turn<0 && relativeAngleMovement>0.01 && distToEstCircle<0){
						steer = distToEstCircle<-GAP ? steer * 2 : steer;
						hazard = 2;
					}
					if (steer*turn>0 && speedX<lowestSpeed-15 && a>0)
						steer = 0;
					return steer;
				} else if (m>5 && speed>lowestSpeed-m && speed<lowestSpeed+Math.max(m, FAST_MARGIN)*3 && canGoToLastSeg && mLastY>0){					
//					double cx = lastSeg.center.x;
//					double cy = lastSeg.center.y;
//					double lx = -lastSeg.type * SIN_PI_4;
//					double ly = SIN_PI_4;					
//					double r = (lastSeg.radius-GAP);
//					mLastX = cx + lx*r;
//					mLastY = cy + ly*r;
					maxSpeed = (m>40 && distToEstCircle>-W) ? VERY_FAST(expectedSpeed, m) : (distToEstCircle<-W && isDanger || distToEstCircle<-W*1.5) ? MODERATE(expectedSpeed, m) : FAST(expectedSpeed, m);
					if (relativeSpeedY>MODERATE_SPEEDY) maxSpeed = Math.min(maxSpeed, faster(expectedSpeed, m));
					mmx = mLastX;
					mmy = mLastY;					
					mmx -= deltaX(mmx,mmy,m, lowestSpeed, lastSeg, a, b, absSpeedY);
					mLastX = mmx;
					isSafeToAccel = true;
					mustPassPoint.x = mLastX;
					mustPassPoint.y = mLastY;	
					angle = Vector2D.angle(speedV.x, speedV.y, mLastX,mLastY);
					if (angle>Math.PI) angle-=Math.PI*2;
					if (angle<-Math.PI) angle+=Math.PI*2;
//					if (lastSeg!=null && (lastSeg.type==0 || speedRadius<lastSeg.radius)) angle*=0.5;
					relativeTargetAngle = angle*turn;
					steer = gotoPoint(cs, mustPassPoint);
					
					if (a>0 && steer*turn<0 && isDanger){
						steer = danger(a, b, absSpeedY, steer, lSteer);
						if (steer*turn>=0 && distToEstCircle<0 && speedX>=maxSpeed+15){
							steer = (relativeAngleMovement<-0.001) ? -turn : (relativeAngleMovement<0.01) ? lSteer : lSteer*0.5;
						}
					} else
					if (a<0 && steer*turn<0) {
						steer = -1.5*a*turn/steerLock;						
					} else
					if (a<0 && steer*turn>0){
						if (absSpeedY>MODERATE_SPEEDY && (relativeAngleMovement<-0.001 && lastRelativeAngleMovement>0 || relativeAngleMovement<0.01 && lastRelativeAngleMovement>0.01)){
							steer = 0;
							hazard = 2;
						} else if (relativePosMovement>0.01 || relativeAngleMovement>0.001) 
							steer = (relativeAngleMovement<-0.01) ? steer*0.5 : (relativeAngleMovement<-0.001) ? steer : turn;
						else if (relativeSpeedY>0) 
							steer *= (absSpeedY>HIGH_SPEEDY) ? 0.3 : (absSpeedY>MODERATE_SPEEDY) ? 0.5 : 1;	
//						else steer = turn;p
						if (canGoToLastSeg && a>-0.1)
							maxSpeed = Math.min(Math.max(last_speed+m*2,speedX-1),maxSpeed);
						else if (!maxTurn || relativePosMovement>0.01 && toInnerEdge>-W || relativePosMovement>0.001 && relativeAngleMovement>0.01
								|| a<-0.1 && relativePosMovement>0.001 && relativeAngleMovement<-0.001 || a<-0.3) maxSpeed = Math.max(Math.min(maxSpeed,speedX-1),lowestSpeed-tW);										
					} else if (steer*turn>0){
						if (isDanger){
							steer = danger(a, b, absSpeedY, steer, lSteer);
							if (steer*turn<=0 && b>TURNANGLE*0.5 && distToEstCircle<0 && speedX>maxSpeed+15 && (distToEstCircle<lastDistToEstCircle || distToEstCircle<-GAP)){
								steer = stableSteer(-turn);
								hazard = 2;
							} else 
							if (steer*turn>0 && b>TURNANGLE*0.5 && distToEstCircle>0 && speedX>maxSpeed+15) 
								steer = 0;
							else if (steer*turn>=0){
								steer = (distToEstCircle<GAP && b>TURNANGLE*0.5) ? 0 : steer;
								hazard = 2;
							}
//							if (steer*turn>=0 && b>TURNANGLE*0.5) 
//								steer = (absSpeedY>MODERATE_SPEEDY) ? lSteer : (absSpeedY>10) ? lSteer*0.5 : 0;
						} else 
						if (isOffBalance && a>0)
							steer = (m<30 && distToEstCircle<-GAP && a>TURNANGLE/1.5 || b>TURNANGLE && distToEstCircle<-GAP || speedX>maxSpeed+10 && distToEstCircle<0) 
								? (Math.abs(lSteer)>Math.abs(steer)) ? (lSteer+steer)*0.5 : 0 
								: (relativeAngleMovement<-0.01 || b>TURNANGLE || distToEstCircle<0 && (relativeAngleMovement>0.01 || speedX>maxSpeed+10)) ? lSteer*0.5 : steer*0.85;
						else if ((!canGoToLastSeg || speed>maxSpeed) && (distToEstCircle<-W && b>TURNANGLE*0.75 || distToEstCircle<-W*1.5 || speed>maxSpeed+10 && distToEstCircle<0 && c<TURNANGLE*0.75 || speed>maxSpeed+15 && distToEstCircle<0)){
							if (speed>maxSpeed+10 && distToEstCircle<0 && b<TURNANGLE*1.5 && last_speed<85){
								steer *= 0.75;
							} else
							if (relativeAngle<-0.001){
								steer = relativeAngle*turn/steerLock;
							} else if (relativeTargetAngle>0.001 && relativeAngleMovement<-0.001 && relativePosMovement<-0.001){
								if (slip>0) 
									steer = lSteer;
								else steer = (steer+lSteer)*0.3;
							} else steer = (distToEstCircle<-W*1.5 || b>TURNANGLE) ? (steer+lSteer)*0.5 :steer;
							
//							if (speed>maxSpeed && steer*turn>0) steer=0;
						}  else if (speedX>faster(last_speed,m) && (distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle && lastSteer*turn>=0 || b>TURNANGLE*0.75 && absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<-0.001)){ 
							steer = (relativeAngleMovement<-0.01 && b>TURNANGLE*0.5) ? -turn : 0;
							hazard = 2;	
						} else if (relativeSpeedY>0) 
							steer = (relativeAngleMovement<-0.01 || b>TURNANGLE*0.75 && speedX>maxSpeed+10 && absSpeedY>MODERATE_SPEEDY && m>15) ? 0 : steer*0.5;					
					}

					if (steer*turn>=0 && speedX<maxSpeed && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle && (c>TURNANGLE*0.75 && a>=TURNANGLE*0.5 || distToEstCircle<-GAP)))
						steer = (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer,lSteer) :  (steer+lSteer)*0.5;
					else 
					if (steer*turn<0 && maxSpeed>speedX && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle || b>TURNANGLE/1.5 && a>=TURNANGLE*0.5))
						steer = (relativeSpeedY<0) ? (steer*lSteer>0) ? minAbs(steer, lSteer) : steer 
								   : (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer, lSteer) : maxAbs(steer, lSteer*0.5);
								   
					if ((m<20 || a>TURNANGLE*0.75) && relativeSpeedY>0 && isFast && a>TURNANGLE*0.5 && b>TURNANGLE && relativePosMovement>0 && distToEstCircle<-GAP){
						steer = (m<20 || m<25 && distToEstCircle<-GAP) ? -turn : maxAbs(lSteer,steer);
					} else
					if (steer*turn<=0 && relativePosMovement>0 && (distToEstCircle<-GAP || relativeAngle<0 && distToEstCircle<0 && lastDistToEstCircle>distToEstCircle) && (isDanger || absSpeedY>=MODERATE_SPEEDY)) 
						steer = (isOffBalance && distToEstCircle<-GAP || relativeSpeedY<0) ? maxAbs(steer,lSteer*0.5) : (distToEstCircle>-GAP || lastDistToEstCircle<distToEstCircle && distToEstCircle>-W) ? steer : maxAbs(steer,lSteer);
										
					if (relativeSpeedY<0 && steer*turn<0 && speedX>maxSpeed) steer = minAbs(steer,lSteer)*0.5;
					if ((relativeAngleMovement<-0.001 && relativePosMovement<-0.01 && !canGoVeryFast || relativeAngleMovement<-0.01 && relativePosMovement<0) && speedX>first_speed && !flyingHazard && slip<20) steer = -turn;
					if (speedX<maxSpeed && distToEstCircle>0 && relativeAngleMovement>0.01){
						steer = relativeSpeedY>0 && steer*turn>=0 && relativeAngleMovement>lastRelativeAngleMovement && a<TURNANGLE*0.5 ? turn : steer;
//						hazard = 2;
					} else if (speedX<maxSpeed && steer*turn<0 && relativeAngleMovement>0.01 && distToEstCircle<0){
						steer = distToEstCircle<-GAP ? steer * 2 : steer;
						hazard = 2;
					}
					if (steer*turn>0 && speedX<lowestSpeed-15 && a>0)
						steer = 0;
					return steer;
				}
			} else if (speed>lowestSpeed-m && (m>5 &&  mLastY>0 || c<TURNANGLE)){				
				if (m>20 && canGoToLastSeg && c<TURNANGLE*2 || m>45 || m>25 && c<TURNANGLE*2 && distToEstCircle>=-(W*2)){
//					double cx = lastSeg.center.x;
//					double cy = lastSeg.center.y;
//					double lx = lastSeg.start.x - cx;
//					double ly = lastSeg.start.y - cy;
//					double l = lastSeg.radius;
//					double r = (lastSeg.radius-GAP)/l;
//					mustPassPoint.x = lastSeg.start.x;
//					mustPassPoint.y = lastSeg.start.y;
					double mmx = mLastX;
					double mmy = mLastY;									
					mmx -= deltaX(mmx,mmy,m, lowestSpeed, lastSeg, a, b, absSpeedY);

					mLastX = mmx;
					isSafeToAccel = true;
					mustPassPoint.x = mLastX;
					mustPassPoint.y = mLastY;					
//					maxSpeed = last_speed + FAST_MARGIN + m*0.35;
					
					maxSpeed = (m>40 && distToEstCircle>-W) ? VERY_FAST(expectedSpeed, m) : 
						canGoToLastSeg || canGoAtCurrentSpeed || distToEstCircle>=0 || speedX>FAST(expectedSpeed, m) 
							|| distToEstCircle>-W && (relativeAngleMovement>0.01 || relativeAngleMovement>0.001 && relativeSpeedY<0) ?  FAST(expectedSpeed, m) : MODERATE(expectedSpeed,m);
					if (relativeSpeedY>MODERATE_SPEEDY) maxSpeed = Math.min(maxSpeed, faster(expectedSpeed, m));
					
					if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad && isConfirmed(lastSeg) && lastSeg.end.y-lastSeg.start.y>3) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
//					if (canGoToLastSeg || canGoAtCurrentSpeed) 
//						maxSpeed = (m>40) ? lowestSpeed + Math.min(FAST_MARGIN +tW+ m*1.5,100) : 
//							(speedX>lowestSpeed + Math.min(FAST_MARGIN +tW+ m,100)) ? lowestSpeed + FAST_MARGIN +tW+ m : lowestSpeed + FAST_MARGIN +tW+ m*0.5;
//					else maxSpeed = (m>40) ? lowestSpeed + Math.min(FAST_MARGIN +tW+ m*1.5,100) : 
//						(speedX>lowestSpeed + FAST_MARGIN +tW+ m) ? lowestSpeed + Math.min(FAST_MARGIN +tW+ m,100) : lowestSpeed + FAST_MARGIN +tW+ m*0.5;
					//					if (speed>=150) steer*=0.5;
					//					double angle = (mLastX==0 && mLastY==0) ? Vector2D.angle(carDirection.x, carDirection.y, mustPassPoint.x, mustPassPoint.y) 
					//							: Vector2D.angle(carDirection.x, carDirection.y, mLastX,mLastY);
					//					if (angle>Math.PI) angle-=Math.PI*2;
					//					if (angle<-Math.PI) angle+=Math.PI*2;
					//					double relativeTargetAngle = angle*turn;
											
					steer = gotoPoint(cs, mustPassPoint);
					angle = Vector2D.angle(speedV.x, speedV.y, mustPassPoint.x,mustPassPoint.y);
					if (angle>Math.PI) angle-=Math.PI*2;
					if (angle<-Math.PI) angle+=Math.PI*2;
//					if (lastSeg!=null && (lastSeg.type==0 || speedRadius<lastSeg.radius)) angle*=0.5;
					relativeTargetAngle = angle*turn;
					if (steer*lastSteer<=0 && a>0){
						if (absSpeedY>=MODERATE_SPEEDY || distToEstCircle<-W || c>TURNANGLE) 
							steer = (relativeAngle<0 && isFast && m<25) ? (steer+lastSteer)*0.5 
									: (speedX>last_speed+Math.max(FAST_MARGIN+tW,m) && relativeAngle>=0) 
										? (c>TURNANGLE || distToEstCircle<-W && lastDistToEstCircle>distToEstCircle) 
												? (relativeSpeedY<0) 
														? distToEstCircle<0 && lastDistToEstCircle>distToEstCircle ? lSteer*0.5 : 0 
														: distToEstCircle>=-GAP || canGoToLastSeg ? steer*turn<0 || isDanger && absSpeedY>=MODERATE_SPEEDY ? 0 : steer : lSteer 
												: speedX>=FAST(last_speed, m) && (absSpeedY>=MODERATE_SPEEDY || speedX>highestSpeed) ? relativePosMovement<-0.001 ? -turn :lSteer : steer  
										: steer * lSteer>0 ? steer 
												: c>TURNANGLE || absSpeedY>=MODERATE_SPEEDY && speedX>last_speed && b>TURNANGLE? maxAbs(steer,lSteer*0.5) : steer;
						else steer = speedX>faster(last_speed,m) 
								? speedX>faster(last_speed,m)+10 && toInnerEdge<-GAP && b>TURNANGLE*0.75 && a>0 && m>10 || distToEstCircle<-GAP && b>TURNANGLE/1.5 || distToEstCircle<0 && isDanger 
										? (steer*lSteer>0) ? maxAbs(steer, lSteer) : steer 
										: (distToEstCircle>0) 
											? (relativeAngleMovement>0.015 || relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement) ? turn : steer*turn>0 ? steer : 0
											: 0
								: (m<25 && speedX>last_speed+Math.max(FAST_MARGIN+tW,m)) 
									? 0 
									: (b>TURNANGLE/1.5) 
										? (distToEstCircle<0) 
												? (speedX<lowestSpeed && distToEstCircle>-tW) ? lSteer*0.3 : lSteer 
												: (steer+lSteer)*0.5 
										: (speedX<lowestSpeed) 
											? 0  
											: steer;
//						if (b<TURNANGLE && a<b && steer*lastSteer<=0 && lastSteer*turn<0 || distToEstCircle>0 || b<TURNANGLE*0.5) hazard = 2;
					}
					
					if (a>0 && steer*turn<0 && isDanger){
						steer = danger(a, b, absSpeedY, steer, lSteer);
						if (steer*turn>=0 && distToEstCircle<0 && speedX>=maxSpeed+15){
							steer = (relativeAngleMovement<-0.001) ? -turn : (relativeAngleMovement<0.01) ? lSteer : lSteer*0.5;
						}
					} else
					if (a<0 && steer*turn<0) {
						steer = -1.5*a*turn/steerLock;						
					} else 
					if (a<0 && steer*turn>0){
						if (absSpeedY>MODERATE_SPEEDY && (relativeAngleMovement<-0.001 && lastRelativeAngleMovement>0 || relativeAngleMovement<0.01 && lastRelativeAngleMovement>0.01)){
							steer = 0;
							hazard = 2;
						} else if (relativePosMovement>0.01 || relativeAngleMovement>0.001) 
							if (speedX>maxSpeed+15 || relativeAngleMovement>0.001 || relativePosMovement>0.01) 
								steer = (relativeAngleMovement<-0.02) ? -turn : (relativeAngleMovement<-0.01) ? 0 : turn;
							else if (relativeAngleMovement<-0.01 || a<0) 
								steer *= 0.5;
							else steer = 0;
						else if (relativeSpeedY>0) 
							steer *= (absSpeedY>HIGH_SPEEDY) ? 0.3 : (absSpeedY>MODERATE_SPEEDY) ? 0.5 : 1;	
//						else steer = turn;p
						if (canGoToLastSeg && a>-0.1)
							maxSpeed = Math.min(Math.max(last_speed+m*2,speedX-1),maxSpeed);
						else if (!maxTurn || relativePosMovement>0.01 && toInnerEdge>-W || relativePosMovement>0.001 && relativeAngleMovement>0.01
								|| a<-0.1 && relativePosMovement>0.001 && relativeAngleMovement<-0.001 || a<-0.3) maxSpeed = Math.max(Math.min(maxSpeed,speedX-1),lowestSpeed-tW);
					} else if (steer*turn>0){
						if (isDanger){
							steer = danger(a, b, absSpeedY, steer, lSteer);
							if (steer*turn<=0 && b>TURNANGLE*0.5 && distToEstCircle<0 && speedX>maxSpeed+15 && (distToEstCircle<lastDistToEstCircle || distToEstCircle<-GAP)){
								steer = stableSteer(-turn);
								hazard = 2;
							} else 
							if (steer*turn>0 && b>TURNANGLE*0.5 && distToEstCircle>0 && speedX>maxSpeed+15) 
								steer = 0;
							else if (steer*turn>=0){
								steer = (distToEstCircle<GAP && b>TURNANGLE*0.5) ? 0 : steer;
								hazard = 2;
							}
						} else
						if (isOffBalance && a>0)
							steer = (m<30 && distToEstCircle<-GAP && a>TURNANGLE/1.5 || b>TURNANGLE && distToEstCircle<-GAP || speedX>maxSpeed+10 && distToEstCircle<0) 
								? (Math.abs(lSteer)>Math.abs(steer)) ? (lSteer+steer)*0.5 : 0 
								: (relativeAngleMovement<-0.01 || b>TURNANGLE || distToEstCircle<0 && (relativeAngleMovement>0.01 || speedX>maxSpeed+10)) ? 0 : steer;
						else if ((!canGoToLastSeg || speed>maxSpeed) && (distToEstCircle<-W && b>TURNANGLE*0.75 || distToEstCircle<-W*1.5 
								|| speed>maxSpeed+10 && distToEstCircle<0 && c>TURNANGLE*0.75 || speed>maxSpeed+15 && distToEstCircle<0 || speed>maxSpeed && b>TURNANGLE && distToEstCircle<0)){
							if (speed>maxSpeed+10 && distToEstCircle<0 && b<TURNANGLE*1.5 && last_speed<85){
								steer *= 0.75;
							} else if (relativeAngle<-0.001){
								steer = -relativeAngle*turn/steerLock;
							} else if (relativeTargetAngle>0.001 && relativeAngleMovement<-0.001 && relativePosMovement<-0.001){
								if (slip>0) 
									steer = lSteer;
								else steer = (steer+lSteer)*0.3;
							} else steer = (distToEstCircle<-W*1.5 || b>TURNANGLE) ? (steer+lSteer)*0.5 :steer;
							
//							if (speed>maxSpeed && steer*turn>0) steer=0;
						}  else if (speedX>faster(last_speed,m) && (distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle && lastSteer*turn>=0 || b>TURNANGLE*0.75 && absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<-0.001)){ 
							steer = (relativeAngleMovement<-0.01 && b>TURNANGLE*0.5) ? -turn : 0;
							hazard = 2;	
						} else if (relativeSpeedY>0) 
							steer = (relativeAngleMovement<-0.01 || b>TURNANGLE*0.75 && speedX>maxSpeed+10 && absSpeedY>MODERATE_SPEEDY && m>15) ? 0 : steer*0.5;				
					}
					//					if (canGoToLastSeg) steer = (steer+lastSteer)*0.5;
//					if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
//					if (steer*turn<0 && (relativeAngleMovement<-0.001 || relativePosMovement<-0.001))
//						steer = steer*1.5;
					
					if (maxSpeed>speedX && (steer*turn<0 && relativeAngle<0 && distToEstCircle<=-W*1.5 || !maxTurn && relativeAngle*first.type<0)) maxSpeed = speedX-1;
					
					if (steer*turn<0 && distToEstCircle>-W && distToEstCircle>lastDistToEstCircle)
						steer = (relativeAngle>0 && (a<TURNANGLE/1.5 && distToEstCircle>-GAP || !isDanger && distToEstCircle>0)) 
							? (maxSpeed<speedX-10 && absSpeedY>=MODERATE_SPEEDY) ? (relativeAngleMovement<-0.01) ? -turn : (relativeAngleMovement<-0.001 || b>TURNANGLE && distToEstCircle<0 && speedX>maxSpeed+20) ? lSteer 
									: (relativeAngleMovement>0.01) ? (isDanger) ? steer : 0 : lSteer*0.5 : (isDanger) ? steer : 0 
							: (distToEstCircle<-W || distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle || distToEstCircle<0 && m<20) && ((a>TURNANGLE || isDanger && a>TURNANGLE*0.5) && b>TURNANGLE*0.75) ? lSteer : 
								(b>TURNANGLE && a>TURNANGLE*0.5 && speedX>=maxSpeed+15 && m<15) ? maxAbs(steer, lSteer) : steer; 

					else if (steer*turn<0 && distToEstCircle<0 && (relativeAngleMovement<-0.001 || distToEstCircle<lastDistToEstCircle) && relativePosMovement<-0.001)
						steer = (relativePosMovement<-0.01 || relativeAngleMovement<-0.01 || speedX>lowestSpeed) ? -turn : steer;
					else if (steer*turn<0 && !isDanger && distToEstCircle>-W && isOffBalance && absSpeedY<10)
						steer = (distToEstCircle<-GAP) ? distToEstCircle>lastDistToEstCircle ? maxAbs(steer,lSteer*0.5) : maxAbs(steer,lSteer) 
													   : minAbs(steer, lSteer)*0.5;
					
					if (b>TURNANGLE && speedX>maxSpeed && a>0 && steer*turn>0)
						steer = (distToEstCircle>0 && distToEstCircle>lastDistToEstCircle) ? (absSpeedY>=MODERATE_SPEEDY) ? lSteer : (m>20 && absSpeedY<10) ? steer : 0 : (distToEstCircle>0 && (a<TURNANGLE/1.5 || absSpeedY>MODERATE_SPEEDY)) ? 0 : (isDanger && (a>TURNANGLE*0.75 || lastDistToEstCircle>distToEstCircle && distToEstCircle<-GAP)) ? lSteer 
								: (distToEstCircle<-GAP || a>TURNANGLE) ? (lSteer+steer)*0.5 : (m>25) ? steer : 0;
					else if (b>TURNANGLE/1.5 && (distToEstCircle<-(W*1.5) || distToEstCircle<-W && m>30 && speedX>lowestSpeed + FAST_MARGIN +tW+ m) && relativeSpeedY>0) {
						steer = (absSpeedY<10 && distToEstCircle>-W) ? maxAbs(steer,lSteer) 
								: (relativeAngleMovement<0.01) ? -turn : relativeAngleMovement>0.02 && relativeAngleMovement>lastRelativeAngleMovement ? steer*0.5 : steer;
					} else if (watchout(a, b)){
						steer = (speedX<lowestSpeed-15 && a>0 && distToEstCircle>-tW) ? minAbs(steer,lSteer*0.5) : (m>30 && speedX<MODERATE(lowestSpeed, m) && a<TURNANGLE) ? relativeAngle>0 ? 0  : maxAbs(steer,lSteer*0.5) : 
							(distToEstCircle<-W || (distToEstCircle<-GAP || distToEstCircle<0 && lastSteer*turn<0 && relativeAngleMovement<-0.001) && isDanger || b>TURNANGLE && a>TURNANGLE/1.5 && distToEstCircle<-GAP || b>TURNANGLE && isDanger && speedX>maxSpeed+20 && distToEstCircle<0) ? distToEstCircle<lastDistToEstCircle 
									? (canGoVeryFast) ? lSteer :-turn 
									: (lastSteer+steer)*0.5
								: steer;
					} else if (a>TURNANGLE/1.5 && distToEstCircle<0 && steer*turn>0 && speedX<maxSpeed){
						steer = (distToEstCircle<-GAP || b>TURNANGLE*0.75) ? (lSteer+steer)*0.5 : (a>TURNANGLE*0.75 && relativeAngle<0) ? (steer+relativeAngle*turn/steerLock)*0.5 : 0;
					}
					
					if (steer*turn>=0 && speedX<maxSpeed && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle && (c>TURNANGLE*0.75 && a>=TURNANGLE*0.5 || distToEstCircle<-GAP)))
						steer = (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer,lSteer) :  (steer+lSteer)*0.5;
					else 
					if (steer*turn<0 && maxSpeed>speedX && speedX>last_speed && distToEstCircle<0 && (distToEstCircle<lastDistToEstCircle && (b>TURNANGLE*0.75 || distToEstCircle<-GAP) || b>TURNANGLE*0.75 && a>=TURNANGLE*0.5))
						steer = (relativeSpeedY<0) ? (steer*lSteer>0) ? minAbs(steer, lSteer) : steer 
								   : (distToEstCircle<-GAP || c>=TURNANGLE) ? maxAbs(steer, lSteer) : maxAbs(steer, lSteer*0.5);
								   
					if ((m<20 || a>TURNANGLE*0.75) && relativeSpeedY>0 && isFast && a>TURNANGLE*0.5 && b>TURNANGLE && relativePosMovement>0 && distToEstCircle<-GAP){
						steer = (m<20 || m<25 && distToEstCircle<-GAP) ? -turn : maxAbs(lSteer,steer);
					} else
					if (steer*turn<=0 && relativePosMovement>0 && (distToEstCircle<-GAP || relativeAngle<0 && distToEstCircle<0 && lastDistToEstCircle>distToEstCircle) && (isDanger || absSpeedY>=MODERATE_SPEEDY)) 
							steer = (isOffBalance && distToEstCircle<-GAP || relativeSpeedY<0) ? maxAbs(steer,lSteer*0.5) : maxAbs(steer,lSteer);
																	
					if (relativeSpeedY<0 && steer*turn<0 && speedX>maxSpeed) steer = minAbs(steer,lSteer)*0.5;
					if (!canGoVeryFast && (relativeAngleMovement<-0.001 && relativePosMovement<-0.01|| relativeAngleMovement<-0.01 && relativePosMovement<0) && speedX>first_speed && !flyingHazard && slip<20) steer = -turn;
					if (speedX<maxSpeed && distToEstCircle>0 && relativeAngleMovement>0.01){
						steer = relativeSpeedY>0 && steer*turn>=0 && relativeAngleMovement>lastRelativeAngleMovement && a<TURNANGLE*0.5 ? turn : steer;
//						hazard = 2;
					} else if (speedX<maxSpeed && steer*turn<0 && relativeAngleMovement>0.01 && distToEstCircle<0){
						steer = distToEstCircle<-GAP ? steer * 2 : steer;
						hazard = 2;
					}
					
					if (steer*turn>0 && speedX<lowestSpeed-15 && a>0)
						steer = 0;

					return steer;
				}
			}//*/
		} else if (lastSeg!=null && turn!=0 && lastSeg.type!=0 && lastSeg.type!=turn && first.type!=turn && edgeDetector.highestPoint!=null){
			Vector2D p1 = edgeDetector.highestPoint;			
			angle = Vector2D.angle(carDirection.x, carDirection.y,p1.x, p1.y);
			if (angle>Math.PI) angle-=Math.PI*2;
			if (angle<-Math.PI) angle+=Math.PI*2;
			relativeTargetAngle = angle*turn;
			if (relativeTargetAngle>0 && p1.length()<50){
				steer = gotoPoint(cs, lastO.end);
				if (lastBrkCmd>0 || lastAcc==0) 
					maxSpeed = speed-1;
				else maxSpeed = speed;
				return steer;
			}
		}				

		//		steer = (maxTurn && relativeTargetAngle>0) ? -turn : gotoPoint(cs, mustPassPoint);
		
//		if (tt!=null && tt.type!=0 && tt.type!=Segment.UNKNOWN && !canGoToLastSeg && !canGoAtCurrentSpeed && relativePosMovement>0 && speedX>lowestSpeed+m) {
//			boolean canGo = canGoToCircle(carDirection, speedX, speedY,tt.type, tt.center.x, tt.center.y, tt.radius+GAP);
//			if (canGo) {
//				System.out.println("CanGo");
//			}
//			canGo = canGoToCircle(carDirection, speedX-m*0.5, speedY,tt.type, tt.center.x, tt.center.y, tt.radius+GAP);
//		}

		if (m>=100) {
			steer = gotoPoint(cs, highest);
		} else if (!inTurn && speedX<lowestSpeed && turn!=Segment.UNKNOWN && lastSeg!=null && lastSeg.type!=Segment.UNKNOWN && lastSeg.type!=turn) {
			if (lastSeg.type==0 && isFirstSeg(lastSeg)) {
				double lSteer = curAngle/steerLock;
				double delta = (-0.3-curPos*turn)*turn*0.5;
				steer = lSteer - delta;
				if (steer*delta<0) steer = -delta;
				if (Math.abs(steer)<0.15) {
					double signPos = (delta<0) ? 1 :-1;
					double deflect = Math.abs(curPos);
					deflect*=signPos;
					steer += deflect;
				}
			} else if (lastSeg.type==0 && (lastSeg.end.x-lastSeg.start.x)*turn>0){
				steer = gotoPoint(cs, highest);
			} else steer = lastSteer;
		} else			
		if (trSz>1 && !isSafeToAccel && mLastX!=0 && mLastY>0 && mustPassPoint.x*mLastX<0){ 
//			if (canGoToLastSeg) 
//				steer = gotoPoint(cs, mustPassPoint);
//			else 
				steer = gotoPoint(cs, mLastX, mLastY);
		} else if (mLastY!=0) 
			steer = (!isOffBalance && distToEstCircle<-GAP && speedX>lowestSpeed-tW && relativeSpeedY>=0 && !isSafeToAccel && relativeTargetAngle>0 && inTurn && mLastY>0 && mLastY<15 && !canGoAtCurrentSpeed && !canGoToLastSeg && maxTurn  && absSpeedY<MODERATE_SPEEDY && toInnerEdge<-GAP) ? -turn : gotoPoint(cs, mLastX,mLastY );
		else steer = gotoPoint(cs, highest);
		
//		if (Double.isNaN(steer)) steer = (edgeDetector.highestPoint==null) ? lastSteer : gotoPoint(cs, edgeDetector.highestPoint);		

		if (!isSafeToAccel && isFast &&steer*turn>0 && relativeTargetAngle>0 && mLastY>0){
			steer = gotoPoint(cs, mLastX, mLastY);
			if (steer*turn<=0){
				steer = (relativePosMovement<-0.001 || !canGoToLastSeg && !canGoAtCurrentSpeed && speedX>faster(lowestSpeed,m) && toInnerEdge<-GAP*0.5) ? -turn : steer;
				hazard = 2;
			}
		} 
				
		if (debug) System.out.println("Initial steer : "+steer);
		double lSteer = steer;
			
		if (inTurn && steer*turn>0 && !isFast && a>0){
			steer = (relativeAngle>0) ? (isOffBalance) ? steer*0.3 : steer : (distToEstCircle<0) ? 0 : steer;
//			steer = 0;
//		} else if (inTurn && steer*turn<0 && speedX<lowestSpeed-tW && absSpeedY<=LOW_SPEEDY && (toInnerEdge<=-W || toInnerEdge<=-1.5 *W && lowestSeg.radius>90 || toInnerEdge<=-2*W && lowestSeg.radius>120)){
//			steer *= 1;
		} else if (steer*turn>0 && a<=TURNANGLE && relativeAngleMovement>0 && absSpeedY<absLastSpeedY-1.5 ){
			steer = turn;
//		} else if (steer*turn>0 && a<-0.001){
//			steer = -turn*a/steerLock;
		} else if (steer*turn<0 && a>=TURNANGLE && (relativeSpeedY<=0 && canGoToLastSeg && m<10)){
			steer = -turn;
		} else if (steer*turn<0 && a>0 && a<TURNANGLE && relativeSpeedY<0 && m>10 && isFast){
			 if (!canGoToLastSeg && !canGoAtCurrentSpeed){
				 if (a<TURNANGLE*0.5 && (relativeAngleMovement<0 || lastSteer*turn>0 || m>25) ) {
					 if (m>25 || relativeTargetAngle<0) 
						 steer = (lastSteer*turn>0) ? (relativeAngleMovement<0) ? lastSteer : turn : turn;
					 else steer = 0;
				 } else if (a<TURNANGLE*0.5)
					 steer = 0;
				 else if (relativeAngleMovement>0){
					 if (absSpeedY<absLastSpeedY-1.5 && (m>15 || a<=TURNANGLE*0.35))
						 steer = turn;
					 if (lastSteer*turn>0 && m>15 || m>25)
						 steer = (lastSteer*turn>0) ? lastSteer : steer*0.5;
//					 else steer = 0;
				 }
			 } else if (a<TURNANGLE && (relativeAngleMovement<0 || lastSteer*turn>0 || m>25) ) {
				 if (m>25 || relativeTargetAngle<0) 
					 steer = (distToEstCircle<-W && lastDistToEstCircle>distToEstCircle) ? 0 
							 : (relativeSpeedY<0) ? relativeTargetAngle<0 ? steer*turn>0 ? steer*1.5 : steer*0.5 : steer*0.5 
									 : (lastSteer*turn>0) ? (relativeAngleMovement<0) ? lastSteer : turn : turn;
				 else steer = 0;
			 } else if (a<TURNANGLE)
				 steer = 0;
			 else if (relativeAngleMovement>0){
				 if (absSpeedY<absLastSpeedY-1.5)
					 steer = (lastSteer*turn>0) ? turn :0;
				 if (lastSteer*turn>0 && m>15 || m>25)
					 steer = (lastSteer*turn>0) ? lastSteer : steer*0.5;
//				 else steer = 0;
			 }
		} else if (relativeTargetAngle>0 && mustTurn && toInnerEdge<-GAP){
//			steer = (relativeSpeedY<0 && speedX<lowestSpeed+FAST_MARGIN+tW+m || isOffBalance && !canGoToLastSeg && absSpeedY<MODERATE_SPEEDY &&steer*turn<0 && speedX<lowestSpeed) 
//					? (relativeAngleMovement>0.001 && relativePosMovement>0.001) ? steer*0.5 : steer 
//					: (distToEstCircle>-GAP || distToEstCircle>-W && distToEstCircle>lastDistToEstCircle || distToEstCircle>-W && isOffBalance && (relativeAngleMovement>0 && m>15 || relativePosMovement>0.01) || speedX<lowestSpeed || relativePosMovement>0 && relativeAngle>0.01 && relativeAngleMovement>0.001 || mLastY==0 || canGoToLastSeg || canGoAtCurrentSpeed || steer*turn>0 || m>20 && slip<0 || speedX>lowestSpeed && m>15 && a<TURNANGLE && relativeAngleMovement>0 
//						|| relativeSpeedY>=HIGH_SPEEDY && absSpeedY>absLastSpeedY || speedX<last_speed && distToEstCircle>-tW && relativePosMovement>-0.01 && relativeAngleMovement>-0.01) 
//						? steer 
//						: (m>15 && relativePosMovement>0 && absSpeedY<LOW_SPEEDY && relativeAngle>0 && distToEstCircle>-W) ? steer*1.5 : (-turn+steer)*0.5;
		} else if (relativeTargetAngle>0 && steer*relativeTargetAngle>0 && steer*turn>0 && mLastY>0){
			if (absSpeedY>=MODERATE_SPEEDY || toInnerEdge<-1.5*W) 
				steer = (relativeAngle<0) ? -turn*relativeTargetAngle/steerLock : steer;
			else steer = (steer+lastSteer)*0.5;
//		} else if (trSz>1 && inTurn && steer*turn>0 && lastSeg!=null && first!=null && lastSeg.type==turn && first.type!=turn && canGoToLastSeg && !mustTurn && speed>last_speed){
//			steer *= 2;
		}
		
		
		
//		double hy = edgeDetector.highestPoint==null ? 0 : edgeDetector.highestPoint.y;

		if (!canGoToLastSeg && !canGoAtCurrentSpeed && absSpeedY>LOW_SPEEDY && toInnerEdge<-W && steer*turn<0 && speedX>highestSpeed+FAST_MARGIN+tW && m<25 && distToEstCircle<-GAP && lastDistToEstCircle>distToEstCircle && (relativePosMovement<-0.001 || relativeAngle<-0.001 || relativeAngleMovement<-0.001))
			steer = -turn;
//		else if (!canGoToLastSeg && speedX>lowestSpeed+FAST_MARGIN+tW+m && (relativeAngleMovement<-0.001 || relativeAngle<-0.001) && relativeSpeedY>0)
//			steer*=2;
//		if (highest!=null && highest.y>0 &&( lastS !=null && lastS.type!=turn && relativeAngle<0 && (steer==0 || steer*turn>0) && (lastSeg.end.y<15 || highest.y<40)))
//			steer = gotoPoint(cs, highest)*0.5;
		
		if (speedX>lowestSpeed && turn!=Segment.UNKNOWN && turn!=-2 && highest!=null && ahead!=null && highest.y>0 && ahead.y<highest.y && (highest.x-ahead.x)*turn>0){
			if (steer*turn>0) steer = gotoPoint(cs, highest);
		}
		
		//		
		//		if (relativeAngle<0 && first.type==turn && absSpeedY<MODERATE_SPEEDY && (steer==0 || steer*turn>0))
		//			steer = -turn;
		/*if (canGoToLastSeg && canGoAtCurrentSpeed && m>10 && relativeAngleMovement>0 && relativePosMovement>0 && relativeTargetAngle>0.001 && steer*turn<0)
			steer = (possibleChangeDirection) ? (isOffBalance) ? steer*0.5 : (lastSteer+steer)*0.25 
					: (isOffBalance) ? (steer+lastSteer)*0.5 : steer*0.5;
		else if (isOffBalance && relativePosMovement>0.001 && relativeTargetAngle>0){
			if (relativeAngleMovement<0) { 
				steer = (canGoToLastSeg) ? steer*0.75 : (speedX<faster(lowestSpeed,m) || relativeSpeedY<LOW_SPEEDY) ? steer : (relativePosMovement>0.01) ? steer*0.5 : -turn;
			} else steer = (canGoToLastSeg) ? steer*0.5 : (speedX<faster(lowestSpeed,m)) ? steer*0.4 : steer*0.5;
//		} else if (!canGoToLastSeg && !canGoAtCurrentSpeed && (relativeAngleMovement<0 || speedX<faster(lowestSpeed,m)) && relativeTargetAngle>0 && relativeSpeedY>0){
//			steer = -turn;
		} //*/
//		else if (!canGoToLastSeg && steer*turn<0 && inTurn && relativePosMovement>0 && speedX>last_speed && absSpeedY<MODERATE_SPEEDY && relativeSpeedY>0 && balance>0)
//			steer = -turn;
			
			
		
		if (isFast && inTurn && mLastY>0 && relativeTargetAngle>0 && !canGoToLastSeg && lastSeg.type==turn && absSpeedY>=HIGH_SPEEDY && absSpeedY>absLastSpeedY && toInnerEdge<-GAP){
			steer = (relativeAngle<0.2 && relativeAngleMovement<0.01) ? -turn : steer;			
			if (lastAcc>0) {
				maxSpeed = Math.min(maxSpeed,speedX-1);		
				return steer;			
			}
		} else if ((isFast || relativeAngle<0) && relativeTargetAngle>0 && !canGoToLastSeg && relativeSpeedY>=0 && (absSpeedY<MODERATE_SPEEDY || absSpeedY<HIGH_SPEEDY && absSpeedY<absLastSpeedY) && (toInnerEdge<-GAP || relativeAngle<-0.001) &&  steer*turn<0 && (!inTurn || first.type==turn) && (relativePosMovement<=-0.001 || relativeAngleMovement<=-0.001 || isFast && steer*turn<=0 && relativeTargetAngle>0 )){
			if (isFast && !isSafeToAccel && relativeAngle<0 && (relativePosMovement<-0.001 && (relativeAngleMovement<-0.001 || relativeAngle<-0.1))){
				if (steer*turn<=0) {
//					maxSpeed = speedX-1;
					steer = -turn;
//					return steer;
				}			
			} else if (isFast && relativeTargetAngle>0.001 && (relativeAngleMovement<0.001 || relativePosMovement<-0)) 
				steer = (relativeAngleMovement<-0.01 && !isOffBalance && distToEstCircle<-GAP && absSpeedY>=10) 
					? (relativeAngleMovement<-0.01) ? -turn : (relativeAngleMovement<-0.001) ? steer : (relativeAngleMovement<0.01) ? steer*0.5 : (relativeAngleMovement>0.02) ? 0 : 0 
					: relativeAngleMovement<-0.001 && relativePosMovement<0.002 && lastRelativePosMovement>relativePosMovement ? maxAbs(steer*1.5,lastSteer) : steer;			
		} else if (steer*turn<=0 && (inTurn && !isSafeToAccel && relativeSpeedY>=-LOW_SPEEDY && relativeAngle<0 && relativeTargetAngle>=0 || inTurn && (isFast || !canGoToLastSeg || !maxTurn && toInnerEdge<-GAP) && relativeSpeedY>=0 && relativeAngleMovement<-0.001 && (relativeAngle<0 || relativeTargetAngle>=0 && (relativePosMovement<0 || absSpeedY<absLastSpeedY-1.5 || toInnerEdge<=-GAP && toInnerEdge>=-W)) && edgeDetector.whichEdgeAhead*turn<=0) && (speed>maxSpeed-tW || !canGoToLastSeg || relativeSpeedY<=-MODERATE_SPEEDY && absSpeedY>absLastSpeedY)){
			if (isFast && !isOffBalance && relativeTargetAngle>0 && m<10 && relativePosMovement>0 && relativePosMovement<0.01 && relativeAngleMovement<0 && relativeAngleMovement<lastRelativeAngleMovement && relativePosMovement<lastRelativePosMovement)
				steer = -turn;
			else steer = ((isFast || relativeAngle<-0.001 && speedX>lowestSpeed-tW) && (relativeAngleMovement<-0.001 && m<10 && speedX>=faster(lowestSpeed, m) || a>TURNANGLE*0.5 && m<10 && !canGoToLastSeg || relativePosMovement>0 && slip>0 && !canGoToLastSeg && m<10 || m<5 && a>0 && relativeAngleMovement<-0.001)) 
				? -turn : steer;
			if (!isFast && relativeAngle<0 && relativeSpeedY>0 && relativePosMovement<-0.001 && (relativeAngleMovement<-0.001 || relativeAngle<-0.1)){			
//					maxSpeed = speedX-1;
					steer = distToEstCircle<0 || !maxTurn ? -turn : steer;
//					return steer;				
			} else if ((isFast || speedX>first_speed || relativeAngle<-0.001) && relativePosMovement<-0.01 && relativeAngleMovement<-0.001)
				steer = -turn;
		} else if (isFast && steer*turn<=0 && relativeSpeedY>=0 && (!canGoAtCurrentSpeed || m<20) && relativeTargetAngle>=0 && relativePosMovement>=0 && (toInnerEdge<-W || relativeAngleMovement<-0.001 && mLastY>0)){
//			steer = -turn;
			if (!canGoAtCurrentSpeed && !canGoToLastSeg && toInnerEdge<-lgap)
				steer = (relativePosMovement<-0.001) ? -turn : (relativePosMovement<0.01 && lastRelativePosMovement>relativePosMovement || lastRelativeAngleMovement>relativeAngleMovement) ? steer*1.5 : steer;
			else if (tt!=null && tt.type!=0 && m<10 && distToEstCircle<0){
				steer = (distToEstCircle>-GAP && lastDistToEstCircle<distToEstCircle) ? steer : -turn;
			} else if (relativeAngleMovement<0) {
				steer = (relativePosMovement<-0.001 || m<10 && speedX>faster(lowestSpeed, m)) ? -turn : (relativePosMovement<0.01 && lastRelativePosMovement>relativePosMovement || lastRelativeAngleMovement>relativeAngleMovement) ? steer*1.5 : steer;
			}
//			if (lastAcc>0 && (Math.abs(speed-maxSpeed)<=3 || absSpeedY>MODERATE_SPEEDY && absSpeedY>absLastSpeedY)){
//				maxSpeed = Math.min(maxSpeed,speedX-1);
//				return steer;
//			}		
		} else if (relativeSpeedY>0 && relativePosMovement<-0.01 && relativeTargetAngle>0 && (absSpeedY>=MODERATE_SPEEDY || absSpeedY<absLastSpeedY-1.5)) {
			steer = (relativeAngleMovement<0.001) ? -turn : steer*0.5;
	    } else if (inTurn && trSz>0 && (m<10 || toInnerEdge>=-Math.max(lgap,GAP)) && (lastS.type!=0 && lastS.type==turn || tt!=null && tt.type==turn) && relativeTargetAngle<=0 && a>0 && (isFast || steer*relativeTargetAngle>=0)){
			if (tt==null || tt.type!=turn) tt = lastS;
			double cnx = tt.center.x;
			double cny = tt.center.y;
			double cLen = 0;
								
			boolean canGo= true;
			boolean ok = false;
			for (i = trSz-1;i>=0;--i){
				t = trArr[ trIndx[i]];

				if (t.type==turn){
					t = (t.type==TURNRIGHT) ? t.rightSeg : t.leftSeg;						
					cnx = t.center.x;
					cny = t.center.y;
					if (t.center.length()>t.radius+GAP*0.5){
						Geom.ptTangentLine(0, 0, cnx, cny, t.radius+GAP*0.5, tmp);
						dx = (lastS.type==TURNRIGHT) ? tmp[0] : tmp[2];
						dy = (lastS.type==TURNRIGHT) ? tmp[1] : tmp[3];
						angle =  Vector2D.angle(carDirection.x, carDirection.y, dx,dy);								
						if (angle>Math.PI) angle-=Math.PI*2;
						if (angle<-Math.PI) angle+=Math.PI*2;			
						relativeTargetAngle = angle*turn;
						mustPassPoint.x = dx;
						mustPassPoint.y = dy;			
						if (dy>0){
							dx -= ox;
							dy -= oy;		
							double dd = Math.sqrt(dx*dx+dy*dy);
							canGo = (dd>=rad+W);						
						};
						ok = true;
						break;
					}
				}
			}
			
			if (!ok && trSz>1) relativeTargetAngle *= 0.5;
			
			if (ok && tt!=null){			
				ok = false;
				cLen = Math.sqrt(cnx*cnx+cny*cny);
//				double fCnx = t.center.x;
//				double fCny = t.center.y;
//				double fLen = Math.sqrt(fCnx*fCnx+fCny*fCny);
				Segment tS = (tt.type==TURNRIGHT) ? tt.rightSeg : tt.leftSeg;
//				int n = Geom.getCircleCircleIntersection(cnx, cny, t.radius, ox, oy, rad, tmpBuf);			
				Geom.ptTangentLine(0, 0, cnx, cny, tt.radius-GAP, tmp);
//				double fx = (t.type==TURNRIGHT) ? tmp[0] : tmp[2];
//				double fy = (t.type==TURNRIGHT) ? tmp[1] : tmp[3];			
				if (isFast && tS!=null && turn==tS.type && cLen<=tS.radius+W*1.5){
					if (canGoToLastSeg){
						if (relativeTargetAngle>=0){
							if (relativeAngleMovement>=0 && relativePosMovement>0){
								steer = 0;
								ok = true;
							}
						}									
					}
					
					/*if (!ok && n>2){
						double x1 = tmpBuf[0];
						double y1 = tmpBuf[1];
						double x2 = tmpBuf[2];
						double y2 = tmpBuf[3];
						if (Math.max(y1,y2)<=lastS.end.y){
							steer = -turn;
							ok = true;
						} 
					}//*/
				}
			} else {
				ok = (a<0);
				relativeTargetAngle = a;
				if (ok){
//					steer = (relativeSpeedY>=MODERATE_SPEEDY || relativeAngleMovement<-0.001) ? 0 : -turn*relativeTargetAngle/steerLock;
					steer = 0;
					if (steer==0) maxSpeed = speedX-1;
				}
			}
			if (!ok){
//				if (a>0 && relativeAngleMovement<-0.001 && !canGo && isFast && m<10){
//					steer = -turn;
//				} else 
				if (m<10 && relativePosMovement>-0.001 && relativePosMovement<0.01 && (absSpeedY>MODERATE_SPEEDY || absSpeedY<absLastSpeedY && relativeAngleMovement<-0.001 && distToEstCircle<=0) && speedX>lowestSpeed+m && lastAcc==1 && toInnerEdge<0){
					steer = -turn;
				} else if (relativePosMovement<0 || !isOffBalance && a>0 && !canGo){
					if (m<10 && isFast && toInnerEdge<-GAP && !isOffBalance && balance>0){
						steer = (speedX>last_speed && (distToEstCircle<0 || relativeAngleMovement<-0.01)) ? -turn : -turn*a/steerLock;
					} else if (canGoToLastSeg && relativePosMovement>0.001 && a>0) {
						steer = relativeAngleMovement<-0.001 ? (isFast && m<10 && steer*turn>0) ? 0 : steer*1.5 : steer; 
					} else steer = (relativePosMovement>0 && (absSpeedY>HIGH_SPEEDY || relativeSpeedY>0 && absSpeedY>LOW_SPEEDY && absSpeedY>absLastSpeedY+1.5)) ? 0 : 
						(isFast && speedX>expectedSpeed+15) ? -turn : (relativeAngleMovement>0.01) ? bal*0.5 : (relativeAngleMovement>0) ? 0 : -turn*a*0.5/steerLock;
						hazard = 2;
//					steer = -turn;
//					maxSpeed = Math.min(maxSpeed,speedX+1);
//					return steer;
				} else if (relativeAngleMovement>=0)
					steer = (relativeTargetAngle<0 && a>0 && !isOffBalance) ? 0 : -turn*relativeTargetAngle*0.5/steerLock;
				else if (relativeTargetAngle>=0 && relativeAngleMovement<=0) 
					steer = (relativeSpeedY>0 && toInnerEdge<-GAP) ? -turn*relativeTargetAngle/steerLock : 0;				
				else if (relativeAngleMovement<0)
					steer = (relativeSpeedY>=0 && toInnerEdge<-GAP && (a>=0 || toInnerEdge<-W)) ? -turn*a/steerLock : (relativeSpeedY<0 && m>5) ? -turn*relativeTargetAngle/steerLock :0;
				
//				if (steer*turn>0) steer *= 2;
										
				/*if (isFast && lastAcc>0 && relativeTargetAngle>0 && (absSpeedY>MODERATE_SPEEDY || relativePosMovement<-0.001 || relativeAngleMovement<-0.001) && absSpeedY>absLastSpeedY){
					if (lastAcc>INCREASE_ONE){
						maxSpeed = Math.min(maxSpeed,speedX+1);
						return steer;
					} else if (lastAcc>CONSTANT_SPEED_ACC){
						maxSpeed = Math.min(maxSpeed,speedX);
						return steer;
					} else {
						maxSpeed = Math.min(maxSpeed,speedX-1);
						return steer;
					}
				}//*/
			}
		} else if (inTurn && trSz>0 && absSpeedY>MODERATE_SPEEDY && (m<10 || toInnerEdge>=-Math.max(lgap,GAP)) && (lastS.type!=0 && lastS.type==turn || tt!=null && tt.type==turn) && relativeTargetAngle<=0 && a<0 && (isFast || steer*relativeTargetAngle>=0) && speedX>first_speed && relativePosMovement<-0.001 && relativeAngle>0)
			steer = (speedX>first_speed+10 && steer*turn<0) ? (relativeAngleMovement>0.02) ? bal : (relativeAngleMovement>0.01) ? steer : -turn : (steer*turn>0) ? 0 : steer;
//		else if (steer*turn>0 && steer*lastSteer<0 && a>0 && toInnerEdge<-lgap){
//			double tmp =  -turn*a*0.5/steerLock;
//			if (isOffBalance)
//				steer = 0;
//			else if (m<10) {
//				if (Math.abs(tmp)<lastSteer*0.5)
//					steer = tmp;
//				else steer = lastSteer*0.5;
//			}
//		}
				
//		if (inTurn && relativePosMovement<0 && first.type==turn && !canGoToLastSeg && !canGoAtCurrentSpeed && toOutterEdge<SAFE_EDGE_MARGIN && relativeAngle<0 && (absSpeedY>MODERATE_SPEEDY || toOutterEdge<W))
//			steer = (-turn*relativeTargetAngle/steerLock+ gotoPoint(cs, 0, 1))*0.5;
		
		//		if (!canGoToLastSeg && (absSpeedY<MODERATE_SPEEDY || absSpeedY<HIGH_SPEEDY && absSpeedY<absLastSpeedY) && first.type==turn && speed>last_speed+Math.max(FAST_MARGIN,mLastY) && lastSeg.start.y<10 )
		//			steer = -turn;
		if (!flyingHazard){
			if (relativePosMovement>0 && distToEstCircle<0 && speedX>=maxSpeed && m<15 && relativeAngleMovement<0.01){
				steer = (relativeAngleMovement<0.001 || lastRelativeAngleMovement>=0.01) ? -turn : steer;
				hazard = 2;
			}
			if (relativeAngle<-0.001 && relativePosMovement<-0.01 && toOutterEdge<=GAP)
				steer = -turn;
			else if (relativeSpeedY>0 && speedX>maxSpeed &&  (relativeAngle<-0.1 || relativeAngleMovement<-0.001 && relativeAngle<-0.001) && relativePosMovement<-0.01)
				steer = -turn;
			else if (m<20 && relativeSpeedY>0 && isFast && absSpeedY>MODERATE_SPEEDY && a>0 && b>TURNANGLE && relativePosMovement>0 && distToEstCircle<GAP*0.5){
				steer = simpleSteer(steer,absSpeedY,absLastSpeedY);				
				if (speedX>=maxSpeed+15 && (distToEstCircle<0 || distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle && relativeAngleMovement<-0.01)){
					steer = (relativeAngleMovement>0.02) 
								? 0 
								:(relativeAngleMovement>0.01) 
									?  (relativeAngleMovement<lastRelativeAngleMovement) 
										?  -turn
										: (distToEstCircle<-W) ? steer : 0
									: (speedX>maxSpeed+25 || relativeAngleMovement<0.001) ? -turn : steer;
					hazard = 2;
				}
			} else if (m<15 && relativeSpeedY>0 && isFast && a>TURNANGLE/1.5 && b>TURNANGLE*0.75 && relativePosMovement>0 && distToEstCircle<0){
				steer = (a>TURNANGLE*0.5 || distToEstCircle<-GAP) ? -turn : (-turn+steer)*0.5;
			} else if (relativePosMovement<-0.001 && relativeAngleMovement<-0.001 && speedX>expectedSpeed && absSpeedY<HIGH_SPEEDY){
				steer = relativeSpeedY>0 && (relativeAngleMovement<-0.01 || speedX>lowestSpeed && !isSafeToAccel) ? -turn : lastSteer*steer>=0 && isFast ? maxAbs(lastSteer, steer*1.5) : ((absSpeedY<LOW_SPEEDY || absSpeedY>absLastSpeedY || toOutterEdge>SAFE_EDGE_MARGIN && absSpeedY<MODERATE_SPEEDY) && speedX<last_speed || relativeSpeedY<0) ? steer :-turn;
				if (relativeAngleMovement<0.001) hazard = 2;
			} else if (speedX>lowestSpeed && a>0 && (absSpeedY>=MODERATE_SPEEDY || relativePosMovement<-0.01 || relativeAngleMovement<-0.001) && relativePosMovement<-0.001){
				steer = (distToEstCircle>0 && relativeAngleMovement>0) ? steer : relativeAngle>0 && absSpeedY<MODERATE_SPEEDY && absSpeedY<absLastSpeedY && (seenNewSeg || trSz>1 && last_speed>first_speed 
						|| lastAcc>CONSTANT_SPEED_ACC && speedX<last_speed+m*2 || speedX<last_speed+Math.max(m,tW) && distToEstCircle>-tW) ? maxAbs(steer,maxAbs(lastSteer, steer)+turn*0.06) 
									: (relativeSpeedY>0) 
									? speedX>lowestSpeed && relativeAngleMovement<-0.01 && !canGoVeryFast ? -turn : steer 
								: steer;
				hazard = 2;
			} else if (!canGoVeryFast && speedX>lowestSpeed-15 && steer*turn<0 && relativeSpeedY>=10 && b>TURNANGLE && a>TURNANGLE*0.75 && m<20 && relativePosMovement>0 && distToEstCircle<-GAP)
				steer = -turn;
			else if (steer*turn<0 && relativeSpeedY>0 && ((distToEstCircle<-(W*1.5) || (distToEstCircle<-W && lastDistToEstCircle>distToEstCircle || distToEstCircle<-GAP && absSpeedY>MODERATE_SPEEDY) && b>TURNANGLE) || distToEstCircle<-W && m>30 && speedX>lowestSpeed + FAST_MARGIN +tW+ m && lastDistToEstCircle>distToEstCircle)) {
				steer = (absSpeedY>MODERATE_SPEEDY || relativeAngleMovement<-0.001) ? -turn : (steer*lastSteer>=0) ? maxAbs(lastSteer,steer*1.5):steer*1.5;
			} else if (steer*turn<0 && relativeSpeedY>0 && relativeTargetAngle>0 && relativePosMovement>0 && distToEstCircle<-W && speedX<lowestSpeed && speedX>lowestSpeed-15) {
				steer =  -turn;
			} else if (steer*turn<0 && relativePosMovement>0 && relativeAngle>0 && a>0) {
				if (m<10 && relativePosMovement>0.01 && distToEstCircle>0 && relativeAngleMovement>-0.01){
					steer = (relativeAngleMovement>-0.001) ? 0 : steer;
				 	hazard = 2;
				} else if (relativeSpeedY<-LOW_SPEEDY && absSpeedY<absLastSpeedY && relativeTargetAngle>0) {
					steer = (distToEstCircle>-W) ? 0 : steer*0.5;
				} else if (speedX<lowestSpeed-15 && distToEstCircle<0 && lastDistToEstCircle<0 && !canGoToLastSeg && (lastDistToEstCircle>distToEstCircle  || b>TURNANGLE && a>TURNANGLE && distToEstCircle<-GAP))
					steer = (relativeAngleMovement<-0.01) ? -turn : relativeSpeedY<LOW_SPEEDY || speedX<lowestSpeed && distToEstCircle>-W ? maxAbs(steer, lastSteer) : (m<10 && distToEstCircle>0) ? maxAbs(steer, lastSteer)-turn*0.06 : -turn;
				else if (speedX>last_speed && relativeAngleMovement>0 && absSpeedY<MODERATE_SPEEDY && (distToEstCircle>-GAP*0.5 && lastDistToEstCircle<distToEstCircle || distToEstCircle>=0))
					steer = (distToEstCircle>lastDistToEstCircle && (m>15 || !isFast)) ? (toInnerEdge<-GAP) ? steer : 0 : 
						(m<10 && isFast && speedX>highestSpeed+tW*2 && (toInnerEdge<-GAP || distToEstCircle<0)) ? -turn : (steer*lastSteer>0) ? maxAbs(steer,lastSteer) : steer;
				else if (steer*turn<0 && speedX>last_speed && (relativeAngleMovement<0 && distToEstCircle<-GAP || speedX>maxSpeed+10 && distToEstCircle<0 || toInnerEdge<=-GAP && a>0 && relativeAngleMovement<-0.01) && m<20){
					if (distToEstCircle<0 && !canGoVeryFast && (relativeAngleMovement<-0.01 || relativeAngleMovement<0 || distToEstCircle<lastDistToEstCircle || b>TURNANGLE && speedX>maxSpeed+10)){
						steer = -turn;
						hazard = 2;
					} else steer =(relativeAngleMovement<-0.001 && speedX>=maxSpeed+10 && distToEstCircle<lastDistToEstCircle && distToEstCircle<0 || relativeAngleMovement<-0.01 && (relativeAngle<0.001 || relativeSpeedY>LOW_SPEEDY)) 
							? (relativeAngleMovement<-0.01) ? -turn : (relativeAngleMovement<-0.001) ? steer : (relativeAngleMovement<0.01) ? steer*0.5 : (relativeAngleMovement>0.02) ? 0 : 0
							:lastSteer*steer<0 ? steer : (relativeSpeedY>0) ? steer : steer*0.5;
				}else if (steer*turn<0 && relativeSpeedY>0 && speedX>faster(last_speed,m) && distToEstCircle<0 && m<30 && b>TURNANGLE)
					steer = relativeAngleMovement<0.01 && (distToEstCircle<-GAP || relativePosMovement<0.02 && relativeAngleMovement<0) ? -turn 
							: steer;//CHU Y VAO DAY 
				else if (steer*turn<0 && relativeSpeedY>0 && speedX>last_speed+m && distToEstCircle<0 && m<30 && speedX>faster(lowestSpeed,m))
					steer = (m<=20 || distToEstCircle<-GAP || distToEstCircle<0 && distToEstCircle<lastDistToEstCircle) && (distToEstCircle<0 || distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle && relativeAngleMovement<-0.001) 
						? (canGoVeryFast) ? steer : -turn 
						: (relativeAngleMovement>0.01) 
							? m>10 ? 0 : steer 
							: simpleSteer(steer, absSpeedY, absLastSpeedY);
			} else if (speedX>lowestSpeed && steer*turn<0 && relativeSpeedY>0 && relativePosMovement<0 && speedX>highestSpeed && isNoNewSeg(lastSeg) && (a<b || distToEstCircle<-GAP && lastDistToEstCircle>distToEstCircle)) {
				steer =  -turn;
			} else if (speedX>lowestSpeed && steer*turn<0 && relativeSpeedY>0 && absSpeedY<absLastSpeedY && relativePosMovement<-0.001 && relativeTargetAngle>0 && relativeAngle<0 && relativeAngleMovement<0 && distToEstCircle<-W && lastDistToEstCircle<0 && lastDistToEstCircle>distToEstCircle) {
				steer =  -turn;
//			} else if (speedX>lowestSpeed && steer*turn<0 && relativeSpeedY>0 && relativePosMovement<-0.001 && relativeTargetAngle>0 && relativeAngle<0 && distToEstCircle<0 && lastDistToEstCircle<0 && lastDistToEstCircle>distToEstCircle) {
//				steer = -turn;
//			} else if (speedX>lowestSpeed && steer*turn<0 && relativeSpeedY>0 && relativePosMovement<-0.001 && !isSafeToAccel && relativeTargetAngle>0 && distToEstCircle<0) {
//				steer = -turn; 
			} else if (steer*turn>=0 && a>0 && m<15 && (first==null || first.type==turn) && speedX>lowestSpeed){
				if (m<15 && isFast && (toInnerEdge<-GAP || relativePosMovement<0.001) && a>0 && distToEstCircle<0)
					steer = (speedX>last_speed) ? -turn : -turn*a/steerLock;
				else
				if (m<10 && isFast && toInnerEdge<-GAP && !isOffBalance && balance>0){
					steer = (speedX>last_speed) ? -turn : -turn*a/steerLock;
				} else if (canGoToLastSeg && relativePosMovement>0.001 && a>0 && relativeTargetAngle>0) {
					steer = relativeAngleMovement<-0.001 ? -turn*relativeTargetAngle/steerLock*1.5 : -turn*relativeTargetAngle/steerLock; 
				} else steer = (relativePosMovement>0 && (relativeAngle>0.3 || m<10 && distToEstCircle>GAP || absSpeedY>HIGH_SPEEDY || relativeSpeedY>0 && absSpeedY>LOW_SPEEDY && absSpeedY>absLastSpeedY+1.5)) 
							? distToEstCircle>GAP || distToEstCircle>0 && relativeAngleMovement>0.01 || distToEstCircle>0 && relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement ? 
									(relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>=lastRelativeAngleMovement) ? turn 
											: relativeAngleMovement>0.01 
												? (relativeAngle>0.3) ? turn : (relativeAngleMovement>lastRelativeAngleMovement) ? steer : steer*0.5 
												: a<0 
													? (relativeAngleMovement>0.005 || relativeAngleMovement>lastRelativeAngleMovement) 
														? (relativeAngle>0.3) ? turn : steer
														: (relativeAngleMovement<-0.001 && a>-0.05) 
															? 0 
															: (relativeAngle>0.3) 
																? (relativeAngleMovement>0.001) ? turn : steer*0.5 
																: steer*0.5
													: (relativeAngleMovement>lastRelativeAngleMovement && relativeAngleMovement>0.001) ? steer*0.5 : 0 
									: 0 
							: (isFast && (distToEstCircle<0 || distToEstCircle<GAP*0.5 && relativeAngleMovement<-0.01) || relativePosMovement<-0.001) 
								? -turn 
								: -turn*a/steerLock;
			} else 
			if (steer*turn>=0 && relativeTargetAngle<-0.001 && m<15 && (relativeAngle>0.1 || relativeAngleMovement>0.01) && relativePosMovement>0.01 && startFlying==0 && slip<=10)
				steer = (relativeAngleMovement>0.02 || relativeAngleMovement>0.015 && relativeAngleMovement>=lastRelativeAngleMovement) ? turn 
						: relativeAngleMovement>0.01 
							? (relativeAngleMovement>lastRelativeAngleMovement) ? steer : steer*0.5 
							: (relativeAngleMovement<-0.015 || relativeAngleMovement<-0.01 && relativeAngleMovement<lastRelativeAngleMovement) 
								? 0
								: a<0 ? steer : (relativeAngleMovement>lastRelativeAngleMovement) ? steer*0.5 : 0;
			else if (steer*turn>=0 && (a<0 || toInnerEdge>-GAP || b<TURNANGLE*0.5 && a<TURNANGLE*0.5) && relativeSpeedY<0 && (relativePosMovement>0 && speedX>lowestSpeed || b<0)){
				if (a<0){
					steer = (relativeAngleMovement>-0.01) ? turn : relativeAngleMovement<-0.015 || relativeAngleMovement<lastRelativeAngleMovement ? 0 : (steer*bal>0) ? maxAbs(steer, bal)*0.5 : steer*0.5;
				} else steer = (relativeAngle>-0.15 || b>0 && relativeAngle>-0.25) && (relativeAngleMovement>0.01 || relativePosMovement>0.01 || relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement || b<TURNANGLE*0.5) ? turn : (steer*lastSteer>0) ? (steer+lastSteer)*0.5 
						: relativeAngle<-0.25 || b<0 && relativeAngle<-0.15 ? simpleSteer(-turn*0.5*a/steerLock, absSpeedY, absLastSpeedY) :steer;
			} else if (steer*turn>=0 && m>15 && distToEstCircle>GAP && a>0 && (first==null || first.type==turn) && speedX>lowestSpeed)
				steer = (speedX>maxSpeed+25) ? stableSteer(0) : stableSteer(steer);
			else if (steer*turn>=0 && (m<15 || distToEstCircle>0 && relativeSpeedY<0) && a>0 && relativePosMovement>0.01 && relativeAngleMovement>0.01 && (first==null || first.type!=turn) && speedX>last_speed){
				steer = turn;
			}
			
			if (distToEstCircle>0 && m<20 && steer*turn<=0 && speedX<maxSpeed+20 && relativePosMovement>0 && relativeAngleMovement>0.001 && (relativePosMovement>0.01 || distToEstCircle>lastDistToEstCircle))
				steer = (relativeAngleMovement>0.02 || relativeAngleMovement>0.01 && relativeAngleMovement>lastRelativeAngleMovement) 
					? (steer*bal>0) ? minAbs(bal, steer) : bal*turn>0 ? bal : 0
					: relativeAngleMovement>0.01 ? (steer*bal>0) ? minAbs(bal, steer)*0.5 : bal*turn>0 ? bal*0.5 : 0
				: (a>0 && (distToEstCircle<GAP*0.5 || distToEstCircle>0 && lastDistToEstCircle>distToEstCircle)) 
					? (steer*turn>0) ? 0 : relativeAngleMovement<0 ? minAbs(steer,-turn*0.5*a/steerLock) : 0 
					: (relativeAngleMovement>0.01) ? stableSteer(bal*0.5) : steer;
				
/*			if (speedX<lowestSpeed && a>0 && Math.abs(relativeAngleMovement)<0.01){
//				double sss = -turn*steerAtSpeed(speedX);
				if (steer*turn>0)
					steer = 0;
//				else steer *= 0.3;
				
			}//*/
		}
		
		if (lastSeg!=null && lastSeg.type==turn && steer*turn<0 && speedX<lowestSpeed-15 && absSpeedY<10 && curPos*turn>=0)
			steer = (relativeAngleMovement>0.001) ? minAbs(steer,-turn*steerAtSpeed(lowestSpeed)) : steer*0.5;
		
//		if (steer*turn>0 && speedX<lowestSpeed-15 && a>0)
//			steer = 0;
				
//		p} else if (steer*turn<0 && relativePosMovement>0 && relativeTargetAngle>0 && (distToEstCircle>-GAP*0.5 && lastDistToEstCircle<distToEstCircle || distToEstCircle>=0 && absSpeedY<MODERATE_SPEEDY))
//			steer = 0;

		double mLen = (trSz>1) ? (!inTurn && lastSeg.type!=0 && lastSeg.type!=Segment.UNKNOWN) ? m : (canGoToLastSeg || mLastY>0) ? m : lastSeg.start.y*0.5 : (canGoToLastSeg || mLastY>0) ? m : mustPassPoint.y;
		if (mLastY==0 && trSz>1) {
			mLastX = lastS.start.x;
			mLastY = lastS.start.y;
			mLen = Math.max(mLen, mLastY);

			dx = mLastX - ox;
			dy = mLastY - oy;

			canGoToLastSeg = Math.sqrt(dx*dx+dy*dy)>=rad;
		} 
		else if (trSz>1 && mLastY>=0 && lastS!=null && lastS.type!=0 && relativeAngle>0 && (relativeTargetAngle<0 || relativePosMovement>0)){
			//			if (mLen<lastS.end.y) mLen = lastS.end.y;
			t = trArr[ trIndx[trSz-2]];			
			t = (lastSeg.type==TURNRIGHT) ? t.rightSeg : t.leftSeg;
			if (mLastY<t.end.y || t.upper!=null && mLastY<t.upper.y){
				mLastY = Math.max(mLastY, t.end.y);				
				if (mLen<mLastY) mLen = mLastY;
				if (t.upper!=null){
					dx = t.upper.x - ox;
					dy = t.upper.y - oy;
				} else {
					dx = t.end.x - ox;
					dy = t.end.y - oy;
				}
				canGoToLastSeg = Math.sqrt(dx*dx+dy*dy)>=rad;
			}
		}
		//		if (lastS!=null && first.type==lastS.type && absSpeedY<=MODERATE_SPEEDY && (relativeTargetAngle<0 || relativePosMovement>0) && relativeAngle>0 && mLen<lastS.start.y)
		//			mLen = lastS.start.y;
		//		else 
		//		if (relativeAngle<0 && !canGoToLastSeg && !canGoAtCurrentSpeed && !maxTurn && relativePosMovement<0)
		//			mLen = 0;
		//		if ((canGoToLastSeg || canGoA-tCurrentSpeed) && mLen>18 && absSpeedY>=MODERATE_SPEEDY) steer *=0.5;
		/*if (first!=null && first.type*turn<0 && lastSeg!=null && lastSeg.type!=turn){
			if (toInnerEdge<-tW){
				if (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.x*turn>0) 
					steer = gotoPoint(cs, edgeDetector.highestPoint);
//				else steer *= 0.5;
			};
		}//*/


		//		if (inTurn && steer*turn>0 && (trSz==1 || lastS!=null && lastS.type==turn) && canGoToLastSeg && Math.abs(relativeTargetAngle)>Math.abs(curAngle)){
		//			steer = Math.abs(curAngle/steerLock)*0.5;
		//		}

		//		if (relativeTargetAngle<0 || absSpeedY>MODERATE_SPEEDY) mLen = 0; 

		if (debug) System.out.println("Processing steer : "+steer);

		if (trSz>1 && first.type*turn<0 && lastSeg.type==turn && toInnerEdge<-W){			
			//			if (trSz>2){
			//				for (int i = 1;i<trSz;++i){
			//					Segment seg = trArr[ trIndx[i] ];
			//					if (seg.type==0 || seg.type==first.type) continue;
			//					dist = (seg==null) ? 0 : (!inTurn && seg.type!=0 && trSz==2 && seg.lower!=null) ? Math.max(seg.center.y,0) : Math.max(seg.start.y,0);
			//					break;
			//				}
			//			}
			hazard = 2;
			if (mLastX==0 && mLastY==0)
				dist = (!inTurn && lastSeg.type!=0 && trSz==2 && lastSeg.lower!=null) ? Math.max(lastSeg.center.y,0) : Math.max(lastSeg.start.y,0);
				else dist = mLastY; 
			//			else dist = Math.sqrt(mLastX*mLastX+mLastY*mLastY);
			if (canGoToLastSeg || distToEstCircle>-W && distToEstCircle>lastDistToEstCircle || distToEstCircle>-GAP) 
				maxSpeed = (relativeTargetAngle>0 || m>25 || distToEstCircle>=-GAP && relativePosMovement>0) ? last_speed+FAST_MARGIN+tW+m : Math.min(Math.max(speedX-1,last_speed+m),last_speed+FAST_MARGIN);
			else maxSpeed = Math.min(last_speed+FAST_MARGIN+dist,speed -1);
			if (dist>15 && canGoToLastSeg){//keep turning								
				steer = (dist>20) ? (absSpeedY<MODERATE_SPEEDY && maxTurn && relativeTargetAngle>0 && speed>=maxSpeed && speed>=first_speed && relativeAngle<0 && lastSteer*turn>0 && toOutterEdge>2*W && a<TURNANGLE *3) ? turn :  steer*3 : steer*2;				
				return steer;

			} else if (relativePosMovement>-0.001 && canGoToLastSeg){
				//				steer += (turn*relativePosMovement/(dist*EdgeDetector.steerLock));
				steer = (dist>20) ? (steer*turn<0) ? steer : steer : steer;
			} else if (!canGoToLastSeg){
				if (dist>=20) steer = (absSpeedY<MODERATE_SPEEDY && maxTurn && relativeTargetAngle>0 && speed>=maxSpeed && speed>=first_speed && relativeAngle<0 && lastSteer*turn>0 && toOutterEdge>2*W && a<TURNANGLE *3) ? turn : steer; 
			}

			if (canGoToLastSeg && mLastY>0 && (trSz==1 && first.type!=0 || lastSeg.type!=0) && dist>=15 && relativeAngle>0 && relativePosMovement>0 && relativeAngleMovement>0){
				if (relativeTargetAngle>0) 
					steer *= 0.5;
			}			
//			if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
			return steer;
		} 		


		if (trSz==1 && (relativeSpeedY>0 || relativeAngleMovement<-0.001 && relativeAngle<0.001) && first.type!=0 && absSpeedY<HIGH_SPEEDY && (steer*turn<=0 || Math.abs(steer)<0.01)&& turn!=0 && speedX>first_speed-tW && (relativePosMovement<0.001 || toInnerEdge<-W)){
			//			steer = (canGoAtCurrentSpeed) ? mLen<18 && speed>=lowestSpeed+FAST_MARGIN+mLen && (relativePosMovement>-0.001 || absSpeedY<=MODERATE_SPEEDY) ? -turn : steer : (steer<0) ? -1 : (steer==0) ? 0 : 1;
			//			steer = -turn*s*0.5;
		} else if (trSz==1 && first.end.y<20 && (steer*turn>0 || Math.abs(steer)<0.001) && first.type!=0 && first.type*turn<0 && relativeSpeedY<-0.001){
			steer = (lastSteer*steer<0) ? (Math.abs(lastSteer)>s) ? 0 : lastSteer : 0; 
		} else if (trSz==1 && speedX>lowestSpeed+20 && first.type*turn>0 && toInnerEdge<-GAP && steer*turn>0 && (relativePosMovement<-0.001 || relativeAngleMovement<-0.001))
			steer = (relativeAngleMovement<-0.001 && relativePosMovement<-0.001) ? -turn : (relativePosMovement<-0.001 || relativeAngle<-0.001) ? 0 : steer; 

		//		if (trSz>1 && absSpeedY<HIGH_SPEEDY && canGoAtCurrentSpeed && speedX>first_speed-tW && (first.type*turn<0 || steer*turn>0) && (relativePosMovement<-0.001 || toInnerEdge<-W)){
		//			steer = (first.type*turn<0) ? turn : -turn;
		//		}

//		if (canGoToLastSeg && mLastY>0&& (trSz==1 && first.type!=0 || lastSeg.type!=0) && mLen>=15 && relativeAngle>-0.001 && relativePosMovement>-0.001 && relativeAngleMovement>-0.001){		
//			steer *= 0.5;
//		}

		double d = !inTurn && (nextSlowSeg!=null && nextSlowSeg.center!=null && nextSlowSeg.center.y>=FAST_MARGIN || canGoAtCurrentSpeed || canGoToLastSeg) ? mLen : (canGoAtCurrentSpeed || canGoToLastSeg || max )&& absSpeedY<=HIGH_SPEEDY ? mLen : 
			!canGoAtCurrentSpeed || inTurn && speed>=lowestSpeed && first_speed>lowestSpeed && (relativePosMovement<-0.001 || relativeAngleMovement<-0.001 || absSpeedY>MODERATE_SPEEDY)|| 
			(trSz==1 || toOutterEdge<SAFE_EDGE_MARGIN) && speed>first_speed+10 && relativeAngle<-0.001 && (relativePosMovement<-0.001 || relativeAngleMovement<-0.001)  || !inTurn && (nextSlowSeg==null || lowestSeg==null || nextSlowSeg.radius>lowestSeg.radius || nextSlowSeg.start.y<20) ? 0 : mLen;
//		if (m<d) m = (d+m)*0.5;
			/*if (d==0 && trSz>1 && mLastY>0 && lastSeg!=null && lastSeg.type!=0 && absSpeedY<MODERATE_SPEEDY){
				dx = mLastX - lastS.center.x;
				dy = mLastY - lastS.center.y;
				d = lastSeg.radius/(lastS.radius+GAP);
				dx = lastS.center.x + dx*d;
				dy = lastS.center.y + dy*d;
				dx -= ox;
				dy -= oy;
				if (Math.sqrt(dx*dx+dy*dy)>rad)
					d = m;
				else d = 0;
			}//*/
			//		double d = mLen;

//			if (trSz>1 && isFast && (!max && toInnerEdge<-GAP) && (relativeTargetAngle<0 && relativePosMovement>0 || relativeTargetAngle>0 && relativePosMovement<0)){
//				if (canGoAtCurrentSpeed){
//					maxSpeed = Math.min(maxSpeed,speed-1);
//				} else maxSpeed = Math.min(speed-1, first_speed-tW);
//				return steer;
//			}
			if (lastSeg!=null && (speed<lowestSpeed || canGoToLastSeg || max || trSz>1 && trArr[trIndx[trSz-2]].end.y<=15 || lastSeg.start.y<15 || isConfirmed(lastSeg)) ) 
				targetRadius = (lastSeg.type==0) ? Segment.MAX_RADIUS-1 : lastSeg.radius;
			else if (canGoAtCurrentSpeed && trSz>1 && t!=null && t.type!=0) targetRadius = t.radius;
			double targetSpeed = (targetRadius==0) ? speed : (trSz>1 && (speed<lowestSpeed || canGoAtCurrentSpeed || canGoToLastSeg || max || trSz>1 && trArr[trIndx[trSz-2]].end.y<=15 || lastSeg!=null && lastSeg.start.y<15)) ? speedAtRadius(targetRadius) : lowestSpeed;
			if (speed>=targetSpeed && targetSpeed<last_speed && canGoToLastSeg && speed<last_speed && absSpeedY<MODERATE_SPEEDY && relativeAngle>0 && relativePosMovement>0) 
				targetSpeed = last_speed;

//			if (trSz>1 && absSpeedY<MODERATE_SPEEDY && relativeAngle>0 && steer*turn<0 && toOutterEdge>=W && speed<targetSpeed && speed<last_speed && canGoAtCurrentSpeed && (max || steer==-1 || steer==1) && lastSeg.start.y>10){
//				steer = 0;
//			}

			if (targetSpeed>highestSpeed) targetSpeed = highestSpeed;
			if (inTurn && targetSpeed<last_speed && lastSeg!=null && lastSeg.type==turn && absSpeedY<MODERATE_SPEEDY && relativeAngle>0 && (max || canGoAtCurrentSpeed && first.type==turn || relativePosMovement>0)){
				targetSpeed = last_speed;
			}

			
			if (trSz>1 && last_speed>lowestSpeed && targetSpeed>=last_speed-tW && lastSeg.end.y-lastSeg.start.y<3 && !isConfirmed(lastSeg))
				targetSpeed = lowestSpeed;
			
			if (trSz>1 && speedX>lowestSpeed+FAST_MARGIN && last_speed-lowestSpeed>=5 && last_speed-lowestSpeed<20 && lowestSeg!=null && lowestSeg.type==lastSeg.type)
				targetSpeed = (relativePosMovement>-0.001 && !canGoToLastSeg && (lowestSeg.end.y>10 || distToEstCircle<GAP && distToEstCircle<lastDistToEstCircle) ) ? lowestSpeed - tW 
						: (absSpeedY>MODERATE_SPEEDY || distToEstCircle<-GAP*0.5) ? last_speed-tW*2 : last_speed-tW;
			
			
			
			/*if (inTurn && speed>highestSpeed && (toOutterEdge<SAFE_EDGE_MARGIN || relativeAngle<0) && relativePosMovement<0 && !canGoAtCurrentSpeed && !canGoToLastSeg && lastSeg.type==turn && edgeDetector.highestPoint!=null && edgeDetector.highestPoint.y<=Math.max(lastS.end.y, lastO.end.y)){
				maxSpeed = highestSpeed;
				return steer;
			}//*/
			
			if (speed<targetSpeed-tW && absSpeedY<MODERATE_SPEEDY && relativeAngle>-0.001) {
				maxSpeed = Math.max(speedX+2,targetSpeed-tW);
				return steer;
			}
						
			
			double fast_speed = faster(targetSpeed,m);
//			double faster = (targetSpeed<70) ? targetSpeed+m*0.5+25 : targetSpeed+FAST_MARGIN+tW+m*0.5;
			double faster = targetSpeed+FAST_MARGIN+tW+m*0.5;
//			double fastI = (targetSpeed<70) ? targetSpeed+m*0.5+25 : (relativePosMovement<-0.001 || absSpeedY>MODERATE_SPEEDY && distToEstCircle<0 || distToEstCircle<-W) ? Math.min(faster, fast_speed) : faster;
			double fastI = (relativePosMovement<-0.001 || absSpeedY>MODERATE_SPEEDY && distToEstCircle<0 || distToEstCircle<-W) ? Math.min(faster, fast_speed) : faster;
			if (targetSpeed<120 && targetSpeed>expectedSpeed+1.5*tW && (absSpeedY>=MODERATE_SPEEDY || highestPoint!=null && highestPoint.y<30 || distToEstCircle<-GAP)){
				fast_speed -= 1.5*tW;
				faster -= 1.5*tW;
				fastI -= 1.5*tW;
				targetSpeed-=1.5*tW;
			}//*/
			//		double ofsDist = Math.abs(relativeTargetAngle-relativeAngle)>0.1 && relativeAngle>=0 && curPos*turn>0.3 ? FAST_MARGIN : tW*3;
			double ofsDist = FAST_MARGIN;			
			if (d>FAST_MARGIN || !canGoToLastSeg) d*=0.5;
			
			if (speedX<targetSpeed-tW && absSpeedY<MODERATE_SPEEDY && relativeAngle>0 && tt!=null && tt.type!=Segment.UNKNOWN && edgeDetector.currentPointAhead!=null && edgeDetector.currentPointAhead.y>=Math.max(tt.start.y, tt.opp.start.y)) {
				maxSpeed = targetSpeed;
				return steer;
			}
			
			if (speedX>last_speed+m && relativePosMovement>0 && distToEstCircle<-W*1.5 && distToEstCircle<lastDistToEstCircle && relativeAngle>0 && b>TURNANGLE*2){
				maxSpeed = Math.min(speedX, targetSpeed+m);
			} else if (!canGoToLastSeg && absSpeedY<MODERATE_SPEEDY && relativePosMovement>0.001 && relativeAngle>0 && relativeAngleMovement>0.001 && maxTurn && speedX<lowestSpeed+FAST_MARGIN+tW) {
				maxSpeed = Math.max(speedX+2,fastI);
			} else 
			if (inTurn && trSz>1 && Math.abs(highestSpeed-lowestSpeed)<20){
//				boolean ok = max && absSpeedY<=MODERATE_SPEEDY && tryMaxTurn(carDirection, speedRadius+W, turn);
//				if (ok){
//					double dd = Vector2D.distance(ox,oy,lastSeg.end.x,lastSeg.end.y);
//					ok = dd>=rad+W;
//				}
				if ((relativeAngle>=0.001 && targetSpeed>105 && speedX<fastI && (relativePosMovement>0 && (relativeAngleMovement>-0.01 || distToEstCircle>-GAP) || distToEstCircle>lastDistToEstCircle || distToEstCircle>=-GAP*0.5) && absSpeedY<HIGH_SPEEDY && (distToEstCircle>-W || relativeSpeedY<0 && distToEstCircle>-tW))) {
					maxSpeed = ((absSpeedY>10 || relativeAngleMovement<-0.001) && distToEstCircle<-GAP*0.5 || distToEstCircle<0 && distToEstCircle<lastDistToEstCircle) ? Math.max(Math.min(fast_speed,speedX-1),targetSpeed+FAST_MARGIN) : (speedX<fastI+5 && a<TURNANGLE/1.5) ? Math.max(speedX,Math.min(speedX+2,fastI)) : Math.min(speedX+2,fast_speed);
				} else if (relativePosMovement<-0.001 && (relativeAngle<-0.001 || relativeAngleMovement<-0.001)) {
					if (toOutterEdge>SAFE_EDGE_MARGIN && relativeAngle>-0.1 && last_speed>105)
						maxSpeed = distToEstCircle>-tW && !dangerSlip && (relativeAngle>0.001 || relativeAngleMovement>-0.01) 
							? Math.min(Math.max(speedX-1,targetSpeed+tW*5),targetSpeed+FAST_MARGIN) 
							: (relativeAngle>0 || absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<0) 
								? Math.min(Math.max(speedX-1,last_speed),targetSpeed+FAST_MARGIN) 
								: (last_speed<first_speed) ? Math.min(Math.max(speedX,last_speed),last_speed+m-tW) : Math.min(Math.max(speedX,last_speed),last_speed+m-tW);
					else maxSpeed = toOutterEdge>SAFE_EDGE_MARGIN 
							? (relativeAngle<0 || absSpeedY>MODERATE_SPEEDY && relativeAngleMovement<0) 
									? Math.min(Math.max(speedX-1,last_speed),last_speed-tW) 
									: Math.min(Math.max(speedX-1,last_speed),last_speed+m-tW)
							: (last_speed<first_speed-3 || dangerSlip && relativeAngleMovement<-0.001 || relativePosMovement<-0.02 || relativeAngleMovement<-0.01)  
									? (speedX<last_speed+tW*4 && absSpeedY<MODERATE_SPEEDY) ? Math.max(lowestSpeed,speedX-1)  : Math.min(Math.max(speedX-1,last_speed),last_speed-tW) 
									: Math.max(Math.max(speedX-1,last_speed),last_speed-tW);
				} else if (a>TURNANGLE && edgeDetector.highestPoint!=null && (edgeDetector.highestPoint.y<=lastO.end.y || edgeDetector.whichE*turn<0 && lastO.type!=0 && Vector2D.distance(lastO.center.x,lastO.center.y,edgeDetector.highestPoint.x,edgeDetector.highestPoint.y)>lastO.radius || edgeDetector.highestPoint.y<=lastS.end.y )){
					maxSpeed = (relativePosMovement>0 || toOutterEdge>SAFE_EDGE_MARGIN || absSpeedY<10 && max && relativeAngle>0)  
							? (absSpeedY<MODERATE_SPEEDY) 
									? (highestSpeed<105 && last_speed<100 && last_speed<first_speed) ? Math.min(last_speed-tW+m*0.5, speedX) :Math.max(targetSpeed+FAST_MARGIN,Math.min(targetSpeed+Math.min(d+tW*2,FAST_MARGIN),speedX-1)) 
									: Math.min(targetSpeed+Math.min(d+tW*2,FAST_MARGIN),speedX-1)  
							:last_speed;
//					if (relativePosMovement<0 && speedX>last_speed-tW && maxSpeed>speedX && last_speed<first_speed && !canGoToLastSeg)
//						maxSpeed = Math.min(speedX,last_speed);
				} else maxSpeed = (inTurn && relativeAngle<0 && toOutterEdge<SAFE_EDGE_MARGIN && !max && !canGoToLastSeg) ? targetSpeed-tW : m>30 && (!inTurn || absSpeedY<=MODERATE_SPEEDY && canGoToLastSeg) ? targetSpeed+m*1.5+FAST_MARGIN+tW : 
					(canGoToLastSeg) ? (relativePosMovement>0 && (last_speed>first_speed || last_speed>105 || seenNewSeg && highestPoint!=null && highestPoint.y>50)) ? faster : targetSpeed+m*2-tW :
						(relativeAngle>=0.001 && last_speed>105 && (relativePosMovement>0 || distToEstCircle>lastDistToEstCircle) && absSpeedY<HIGH_SPEEDY && (distToEstCircle>-W || relativeSpeedY<0 && distToEstCircle>-tW)) 
						? ((absSpeedY>10 || relativeAngleMovement<-0.001) && distToEstCircle<-GAP*0.5 || distToEstCircle<0 && distToEstCircle<lastDistToEstCircle) ? Math.max(Math.min(fast_speed,speedX-1),targetSpeed+FAST_MARGIN) : (speedX<fastI+5 && a<TURNANGLE/1.5) ? Math.max(speedX,Math.min(speedX+2,fastI)) : Math.min(speedX+2,fast_speed)
						:relativePosMovement>-0.001 && relativeAngleMovement>=0.001 && absSpeedY<MODERATE_SPEEDY							
							? (speedX<fast_speed && distToEstCircle>lastDistToEstCircle && isOffBalance) ? fast_speed : Math.min(speedX,fast_speed) 
							: (highestSpeed<105 && last_speed<first_speed && (last_speed<100 || first_speed-last_speed>3.5) ) ? Math.min(last_speed-tW+m*0.5, speedX)
									:  (distToEstCircle>-W && relativePosMovement>-0.001) 
										? Math.min(Math.max(Math.max(speedX, targetSpeed+10)-1,targetSpeed-tW),faster)
										: !dangerSlip && (distToEstCircle>-GAP || toOutterEdge>SAFE_EDGE_MARGIN && distToEstCircle>-tW) && (relativeAngle>0.001 || relativeAngleMovement>-0.01) 
											? Math.max(speedX-1,targetSpeed+tW*5)
											: Math.min(speedX-1,fast_speed);			
//				if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.y>40 && lastSeg.end.distance(ox,oy)>=rad) {
//					maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
//				}
				if (relativePosMovement>=-0.001 && relativeAngleMovement>-0.001 && relativeAngle>0.001 && speedX>lowestSpeed+FAST_MARGIN+tW) {
					if (absSpeedY>MODERATE_SPEEDY) maxSpeed = Math.min(speedX-1, maxSpeed);
					if (speedX<faster(lowestSpeed,m)) maxSpeed = Math.max(speedX-1,maxSpeed);
				}
				
				return steer;
			} else 
				if (inTurn && (trSz==1 || trSz>1 && lastS!=null && lastS.type==first.type) && (canGoToLastSeg || canGoAtCurrentSpeed ||relativePosMovement>=0 || toOutterEdge>=SAFE_EDGE_MARGIN || relativeAngle>=-0.1 && toInnerEdge>-W*2)){
					//			d = 0;
					if (trSz>1 && lastS.type!=0 && lastS.radius<=first.radius) 
						ofsDist = FAST_MARGIN;
					if (trSz==1 && inTurn && absSpeedY<MODERATE_SPEEDY && speedX<lowestSpeed && toInnerEdge>=-W*2.5 && relativeAngle>-0.1) {
						maxSpeed = lowestSpeed;
					} else if (last_speed>=105 && (relativeAngle>=0.001 && (relativePosMovement>0 || distToEstCircle>lastDistToEstCircle || distToEstCircle>=-GAP*0.5) && absSpeedY<HIGH_SPEEDY && (distToEstCircle>-W || relativeSpeedY<0 && distToEstCircle>-tW))) {
						maxSpeed = relativePosMovement<-0.001 && (relativeAngleMovement<-0.001 && distToEstCircle<-GAP*0.5 || distToEstCircle<0 && distToEstCircle<lastDistToEstCircle) 
								? absSpeedY>MODERATE_SPEEDY && speedX>highestSpeed+m*2 ? Math.min(speedX-1,targetSpeed+FAST_MARGIN) : Math.max(Math.min(fast_speed,speedX),targetSpeed+FAST_MARGIN) 
								: (speedX<fastI+5 && a<TURNANGLE/1.5) ? Math.max(speedX-1,Math.min(speedX+2,fastI)) : Math.min(speedX+2,fast_speed);
//						if (steer*turn<0 && distToEstCircle<0 && distToEstCircle<lastDistToEstCircle) steer*=1.5;
					} else if (relativeAngle>=0 && (relativePosMovement>0 || toOutterEdge>SAFE_EDGE_MARGIN && (absSpeedY<MODERATE_SPEEDY && distToEstCircle>-W*1.5 || distToEstCircle>-W) || absSpeedY<10 && max)|| canGoToLastSeg || canGoAtCurrentSpeed){						
						if (a>TURNANGLE && edgeDetector.highestPoint!=null && (edgeDetector.highestPoint.y<=lastO.end.y || edgeDetector.whichE*turn<0 && lastO.type!=0 && Vector2D.distance(lastO.center.x,lastO.center.y,edgeDetector.highestPoint.x,edgeDetector.highestPoint.y)<lastO.radius || edgeDetector.highestPoint.y<=lastS.end.y )){
							maxSpeed = (relativePosMovement>0 && canGoToLastSeg) 
								? fast_speed 
								: (relativePosMovement<-0.001 && distToEstCircle>-W ||  distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle || distToEstCircle<-W*1.5) 
									? (absSpeedY<=MODERATE_SPEEDY) ? Math.max(Math.min(fast_speed,speedX),targetSpeed+FAST_MARGIN) : Math.max(Math.min(fast_speed,speedX-1),targetSpeed+FAST_MARGIN) 
									: (speedX<fastI+5 && a<TURNANGLE/1.5) ? Math.max(speedX,Math.min(speedX+2,fastI)) : Math.min(speedX+2,fast_speed);
						} else maxSpeed = (relativePosMovement>0 && (canGoToLastSeg || toInnerEdge>=-lgap || toInnerEdge>=-GAP)) 
							? faster 
							: (toOutterEdge>SAFE_EDGE_MARGIN && relativeAngle>0) ? Math.max(targetSpeed+FAST_MARGIN+tW,Math.min(Math.max(targetSpeed,speedX-1),faster)) : Math.max(targetSpeed+FAST_MARGIN,Math.min(Math.max(targetSpeed,speedX-1),faster));
					} else if (toOutterEdge>SAFE_EDGE_MARGIN && absSpeedY<MODERATE_SPEEDY && distToEstCircle>-W*1.5){
						if (a>TURNANGLE && edgeDetector.highestPoint!=null && (edgeDetector.highestPoint.y<=lastO.end.y || edgeDetector.whichE*turn<0 && lastO.type!=0 && Vector2D.distance(lastO.center.x,lastO.center.y,edgeDetector.highestPoint.x,edgeDetector.highestPoint.y)<lastO.radius || edgeDetector.highestPoint.y<=lastS.end.y )){
							maxSpeed = (relativePosMovement>0 && canGoToLastSeg) ? faster(targetSpeed,m) 
									: (relativePosMovement<0 && trSz>1 && last_speed>first_speed) ? Math.min(last_speed+FAST_MARGIN,Math.max(speedX,last_speed)) 
											: (a<TURNANGLE || distToEstCircle>-W) ? Math.min(speedX,faster) : Math.min(speedX,fast_speed);
						} else maxSpeed = relativePosMovement>0 && (canGoToLastSeg || toInnerEdge>=-lgap || toInnerEdge>=-GAP) ? Math.max(speedX,faster) : 
							(!maxTurn || speedX>highestSpeed && (distToEstCircle<-W && relativePosMovement<0 && relativeAngle<0 || relativePosMovement<-0.01)) ? Math.min(fast_speed,speedX-1) : Math.max(Math.min(fast_speed,speedX),targetSpeed+FAST_MARGIN);
					} else maxSpeed = 
//						(absSpeedY>=HIGH_SPEEDY && absSpeedY<absLastSpeedY || distToEstCircle<-W && lastDistToEstCircle>distToEstCircle) 
//						    ? Math.max(Math.min(speedX-1,targetSpeed+m),targetSpeed) 
//							: 
								(relativePosMovement>0) ? Math.min(speedX,fast_speed) 
										                : toOutterEdge>SAFE_EDGE_MARGIN 
										                			? (absSpeedY<=MODERATE_SPEEDY) ?  Math.min(targetSpeed+FAST_MARGIN,Math.max(Math.min(speedX,fast_speed),targetSpeed)) : Math.min(targetSpeed+FAST_MARGIN,Math.max(Math.min(speedX-1,fast_speed),targetSpeed))  
										                			: Math.max(Math.min(speedX-1,fast_speed),targetSpeed);
//					if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.y>40 && lastSeg.end.distance(ox,oy)>=rad) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
					
					if (relativePosMovement>=-0.001 && relativeAngleMovement>-0.001 && relativeAngle>0.001 && speedX>lowestSpeed+FAST_MARGIN+tW) {
						if (absSpeedY>MODERATE_SPEEDY && !canGoToLastSeg && distToEstCircle<-GAP) maxSpeed = Math.min(speedX-1, maxSpeed);
						if (speedX<faster(lowestSpeed,m)) maxSpeed = Math.max(speedX-1,maxSpeed);
					} 
										
//					else if (!canGoToLastSeg && relativePosMovement>=0 && toInnerEdge<=-GAP && lastRelativePosMovement>relativePosMovement && relativeAngleMovement<-0.001 && speedX>Math.max(last_speed,lowestSpeed)+FAST_MARGIN+tW)
//						maxSpeed = Math.min(speedX-1, maxSpeed);
					return steer;
				} else if (trSz>1 && inTurn && absSpeedY<MODERATE_SPEEDY && last_speed==highestSpeed && relativePosMovement>0 && relativeAngle>0){
					maxSpeed = highestSpeed+m*2;
				} else if (trSz>1 && absSpeedY<MODERATE_SPEEDY && lastSeg.type!=0 && canGoAtCurrentSpeed && lastSeg.center.length()>=rad && toInnerEdge>-W){
					maxSpeed = (mLen<18) ? last_speed+lastSeg.start.y : last_speed+lastSeg.start.y+ofsDist;
				} else if (absSpeedY<HIGH_SPEEDY && speedX<lowestSpeed+ofsDist && toOutterEdge>=SAFE_EDGE_MARGIN){
					maxSpeed = (trSz>1 && (speed>highestSpeed && lowestSeg.start.y<15 || inTurn && highestSpeed-lowestSpeed<15 &&  relativePosMovement<-0.001)) ? lowestSpeed : (relativePosMovement>0 && relativeAngle>-0.1) ? last_speed+ofsDist+d :last_speed+20+d;
				} else maxSpeed = 
					(!canGoAtCurrentSpeed && !canGoToLastSeg && !maxTurn || absSpeedY>=VERY_HIGH_SPEEDY && relativePosMovement<-0.001) 
					? (speedX<first_speed && speedX>last_speed && m>30 && absSpeedY<HIGH_SPEEDY) ? Math.min(speedX, faster) 
							: absSpeedY>=VERY_HIGH_SPEEDY ? targetSpeed : targetSpeed+m 
					: (canGoAtCurrentSpeed || canGoToLastSeg) && (relativePosMovement>-0.001 || relativeAngleMovement>-0.001 && relativeAngle>0.001) 
						? faster(targetSpeed,m) 
						: absSpeedY<=MODERATE_SPEEDY && (trSz==1 || last_speed-first_speed>15 || canGoToLastSeg) && ((relativePosMovement>=0 || relativeAngleMovement>=0 && canGoToLastSeg) && relativeAngle>0.001) 
						  ? (canGoToLastSeg) ? faster(targetSpeed,m) 
								  : (relativeAngle>=0.001 && (relativePosMovement>0 || distToEstCircle>lastDistToEstCircle) && absSpeedY<HIGH_SPEEDY && (distToEstCircle>-W || relativeSpeedY<0 && distToEstCircle>-tW)) 
								  	? (relativePosMovement<-0.001 && distToEstCircle>-W ||  distToEstCircle<-GAP && distToEstCircle<lastDistToEstCircle || distToEstCircle<-W*1.5) ? Math.max(Math.min(fast_speed,speedX-1),targetSpeed+FAST_MARGIN) : (speedX<fastI+5 && a<TURNANGLE/1.5) ? Math.max(speedX,Math.min(speedX+2,fastI)) : Math.min(speedX+2,fast_speed) 
									: targetSpeed+Math.min(m*1.5,FAST_MARGIN+tW) 
						  : (distToEstCircle<-(W*1.5)) 
						  	   ? (maxTurn && speedX<last_speed ) 
						  			   ? last_speed+m*2 
						  			   : (relativePosMovement>-0.001 || relativeAngle>-0.001 && relativePosMovement>-0.01 && distToEstCircle>-tW) 
						  			   		? Math.max(Math.min(speedX-1, faster(targetSpeed,m)),targetSpeed+m) 
						  					: Math.min(speedX-1, targetSpeed+m*2)
							   : (speedX<first_speed && speedX>last_speed && m>30) 
							   		? (absSpeedY>=MODERATE_SPEEDY) ? Math.min(speedX, faster) : Math.min(speedX, faster) 
							   		: (distToEstCircle>lastDistToEstCircle && distToEstCircle<0) 
							   			? (relativePosMovement>-0.001 || speedX<last_speed && absSpeedY<MODERATE_SPEEDY && relativeAngleMovement>-0.001 && relativePosMovement>-0.01 && relativeAngle>-0.05) 
							   					? Math.max(last_speed+m*2,Math.min(speedX,fast_speed))
							   					: (speedX<last_speed && absSpeedY<MODERATE_SPEEDY && relativeAngleMovement>-0.001 && relativePosMovement>-0.01 && relativeAngle>-0.05) ? last_speed+m : Math.max(Math.min(speedX-1,fast_speed),lowestSpeed)  
							   			: (maxTurn) 
							   					? (relativePosMovement>-0.001) 
							   							? (distToEstCircle>=-GAP && absSpeedY<MODERATE_SPEEDY) ? Math.min(fast_speed+tW,Math.max(speedX-1,fast_speed)) : Math.max(Math.min(speedX-1, fast_speed),lowestSpeed+FAST_MARGIN)
							   							: targetSpeed+Math.min(m*2,FAST_MARGIN+tW) 
							   					: speedX-1;
					
					
//					if (inTurn && maxSpeed<speedX && maxSpeed<lowestSpeed+FAST_MARGIN+tW+m && (trSz==1 || lowestSegIndx==trSz-1) && relativePosMovement>0.001 && relativeAngleMovement>0 && relativeAngle>0) {
//						if (maxSpeed>faster(lowestSpeed,m)) 
//							maxSpeed = speedX-1;
//						else maxSpeed = speedX;
//					}
				

			//		if (trSz==1 || Math.abs(highestSpeed-lowestSpeed)>FAST_MARGIN){
			//			if (toOutterEdge>=SAFE_EDGE_MARGIN || canGoAtCurrentSpeed ||canGoToLastSeg || maxTurn || relativeAngle>0 && (relativePosMovement>0 || relativeAngleMovement>0))
			//				maxSpeed = Math.max(maxSpeed, last_speed+FAST_MARGIN+d);
			//		}

			//		if (inTurn && trSz>1 && maxSpeed>=lowestSpeed+10 && first_speed>lowestSpeed) maxSpeed = lowestSpeed+10;
 
//			if (!inTurn && (relativePosMovement>=-0.001 || relativeAngleMovement>-0.001 && relativeAngle>0.001 && absSpeedY<MODERATE_SPEEDY))
//				if (maxSpeed<targetSpeed+ofsDist) maxSpeed = targetSpeed+ofsDist;
			
			if (relativePosMovement>=-0.001 && relativeAngleMovement>-0.001 && relativeAngle>0.001 && speedX>lowestSpeed+FAST_MARGIN+tW) {
				if (absSpeedY>MODERATE_SPEEDY) maxSpeed = Math.min(speedX-1, maxSpeed);
				if (speedX<faster(lowestSpeed,m)) maxSpeed = Math.max(speedX-1,maxSpeed);
			}
			

			//			if (!inTurn && maxSpeed>speed && lowestSpeed<180 && (speed>=200 || speed>=lowestSpeed+ofsDist && lowestSeg.center.y<SAFE_DISTANCE))
			//				maxSpeed = (maxSpeed>200) ? 200 : maxSpeed;

			//			if (absSpeedY<=MODERATE_SPEEDY && toOutterEdge>=tW && (speed<last_speed || speed<last_speed+10 && relativeAngle>-0.001)){
			//				maxSpeed = (speed<last_speed) ? last_speed : speed;
			//			} else 
			if (drift) {			
//				if (curPos*turn>0 && (lastPos-curPos)*turn<=0) maxSpeed = Math.max(maxSpeed,lowestSpeed+ofsDist+d);
				//			if (speed>=maxSpeed && lastSteer*turn<=0 && relativePosMovement>=-0.001 && curPos*turn<0.55) {
				//				if (lastSteer*steer>0 && Math.abs(steer)>Math.abs(lastSteer)){
				//					if (Math.abs(steer)<=0.5) 
				//						steer *=2;
				//					else steer = (steer<0) ? -1 : (steer==0) ? 0 : 1;
				//				} else maxSpeed += (d>20) ? d*2 : d; 
				//			}	
//				if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
				return steer;
			} else if (rr!=0){
				if (turn!=UNKNOWN){				
					if (highestPoint ==null) highestPoint = edgeDetector.highestPoint;
					//					if (maxSpeed>targetSpeed && highestPoint!=null && highestPoint.y<60) maxSpeed = targetSpeed;
					double offset = (lowestSegIndx==0 || lowestSeg==null) ? 0 : lowestSeg.start.y; 
					if (speedX>=first_speed && speedX>=lowestSpeed+offset && lowestSegIndx>0){ 
						if (maxTurn && relativeAngle>-0.001 && relativeAngleMovement>0) 
							maxSpeed = Math.max(maxSpeed,lowestSpeed+offset+ofsDist);
						else if (maxTurn) maxSpeed = Math.max(maxSpeed,lowestSpeed+offset);
					}
					if (d>18 && absSpeedY<=MODERATE_SPEEDY){						
						if (!inTurn && nextSlowSeg!=null && nextSlowSeg.type!=0 && nextSlowSeg.center!=null && mLen<ofsDist ||first.type!=0 && speedX>first_speed && speedX>lowestSpeed && first_speed<lowestSpeed) 
							d = 0;					
						if (!inTurn) 
							if ((canGoAtCurrentSpeed || canGoToLastSeg) && d>FAST_MARGIN){
								d *= 0.5;
							}
							else if (d<20) d*=2;
						if (d>ofsDist) d = ofsDist;
						if (canGoAtCurrentSpeed && canGoToLastSeg) maxSpeed += d;
						//					if (curPos*turn>0.7 && (lastPos-curPos)*turn<=0) maxSpeed = Math.max(maxSpeed,lowestSpeed+ofsDist);
						//					if (speed>=maxSpeed && lastSteer*turn<=0 && (lastPos-curPos)*turn>=0 && curPos*turn<0.55) {
						//						if (Math.abs(steer)<=0.5) 
						//							steer *=2;
						//						else steer = (steer<0) ? -1 : 1;
						//					}
					}

				} else if (!canGoAtCurrentSpeed){
					maxSpeed = lowestSpeed;
					steer = -turn;
				} else maxSpeed = speedX;

				//			if (DANGER && speed>=maxSpeed){
				//				if (inTurn && speed>minSpeed+10){				
				//					maxSpeed = Math.min(speed-1,maxSpeed);
				//				}							
				//				if (first.type!=0 && carDirection.y<=dy/d ){
				//					if (trSz==1 && oldFirstRad==first.radius && Math.abs(rr-radiusAtSpeed(speed))<25){
				//						if (oldTargetRadius<rr || absSpeedY>10) {
				//							maxSpeed = Math.min(maxSpeed,speed-3);				
				//						} else maxSpeed = Math.min(maxSpeed+1,speed+1);
				//					}
				//				}
				//			}								
				//			maxSpeed = Math.max(maxSpeed, minSpeed);
//				if (trSz>1 && lastSeg!=null && lastSeg.type!=0 && lastSeg.end.distance(ox,oy)>=rad) maxSpeed = Math.max(maxSpeed,last_speed+lastSeg.end.y-15);
			} else maxSpeed = 0;

			//		if (absSpeedY>MODERATE_SPEEDY && maxSpeed!=0 && speed>=maxSpeed && steer*turn>0) 
			//			steer=0;
			//		if (speed>=maxSpeed && carDirection.y>=dy/d){
			//			gear--;
			//			if (gear<1) gear = 1;
			//		}
			//		if (time>=8.3){
			//			maxSpeed = 0;			
			//		}

			//		maxSpeed =Math.min(150,maxSpeed);		
			if (debug) System.out.println("Turn radius "+targetRadius+"  MSpeed "+maxSpeed+" Next Radius "+nextRadius+" TRadius "+rr+"  EdgeRadius "+edgeRadius+" speed "+speed);
			if (debug) System.out.println("Steer    "+steer);
			if (Double.isNaN(steer)){			
				followedPath = false;
				recording = false;			
				steer =  lastSteer;
			}				
			//		if (trSz>0) {			
			//			oldFirstRad = first.type==0 ? Segment.MAX_RADIUS : first.radius;
			// 		}				
			return steer;
	}

	public final void init() {
		// TODO Auto-generated method stub		
	}

	private boolean isStuck(CarState cs){
		double speedX = cs.speedX/3.6;
		double angle = cs.angle;
		double curPos = cs.trackPos;
		double absToMiddle = Math.abs(toMiddle);		
		double absAngle = Math.abs(angle); 
		if (curAngle*curPos>0 && toMiddle*Math.signum(curAngle)>0 && (Math.abs(curAngle)<Math.PI*0.75 || absToMiddle>tW)){
			stuck = 0;
			return false;
		} else if (absAngle > MAX_UNSTUCK_ANGLE &&
				Math.abs(speedX) < MAX_UNSTUCK_SPEED &&
				absToMiddle>tW-W*2) {
			if (stuck > MAX_UNSTUCK_COUNT) { 
				return true;
			} else if (speedX>=0 || absAngle<PI_2){
				if (speedX<=0) stuck++;
				if (absToMiddle<tW-W*2 && absAngle<Math.abs(lastAngle)) stuck--;
				if (speedX>=0 && Math.abs(speedX)<2) stuck++;
				return false;
			}
		} else {
			stuck = 0;
			return false;
		}
		return true;
	}


	private final void record(CarState cs){		
		if (!recording) return;
		guessTurn = true;
		possibleChangeDirection = false;
		if (debug) ti = System.nanoTime();			
		//		long time = System.nanoTime();
		nraced = distRaced - raced;		
		nraced = Math.round(nraced*PRECISION)/PRECISION;

		Vector2D[] left = edgeDetector.left;
		Vector2D[] right = edgeDetector.right;
		int sL = edgeDetector.lSize;
		int sR = edgeDetector.rSize;		
		Segment[] trArr = CircleDriver2.trArr;
		lMap.clearAll();
		rMap.clearAll();		
		//		double toMiddle = Math.round((-tW*edgeDetector.curPos)*PRECISION)/PRECISION;
		//		double prevToMiddle = Math.round((-prevTW*prevEdge.curPos)*PRECISION)/PRECISION;
		//		double ax = toMiddle-prevToMiddle;


		//		if (left==null && right==null){
		//			prevEdge = edgeDetector;				
		//			raced = distRaced;		
		//			return;
		//		}
		for (int i =trSz-1;i>=0;--i) {
			Segment mid = trArr[ trIndx[i] ];
			mid.num = 0;
			mid.updated = false;
			mid.leftSeg.updated = false;
			mid.rightSeg.updated = false;
			mid.leftSeg.done = false;
			mid.rightSeg.done = false;
		}

		
		if (time>=BREAK_TIME-1) debug = true;
		
		if (debug){			
	
			for (int i = trSz-1;i>=0;--i){
				Segment t = trArr[ trIndx[i] ];
				int count = 0;
				for (int j = Segment.MAX_RADIUS-1;j>=0;--j){
					if (t.map[j]!=0) 
						count++;
					if (count==t.radCount && count==1 && t.map[j]!=0 && t.type!=0 && j!=(int)Math.round(t.radius))
						break;
					if (t.map[j]<0)
						System.out.println();
					if (count>t.radCount) 
						break;
				}
	
				if (count!=t.radCount){
					System.out.println("Check here");
				}
			}
	
			int count = 0;
			for (int i = 0;i<occupied.length;++i){
				if (occupied[i]!=0) count++;
				if (count>trSz){
					System.out.println();
					break;
				}
			}
	
			if (count!=trSz) {
				System.out.println();
			}
	
			for (int i = 0;i<trSz;++i){
				Segment lSeg = trArr[ trIndx[i] ].leftSeg;
				if (lSeg.num==0) continue;
				if (lSeg.endIndex>=prevEdge.lSize){
					System.out.println();
				}
				if (lSeg.startIndex>=0 && lSeg.start.y>prevEdge.left[lSeg.startIndex].y+SMALL_MARGIN || lSeg.endIndex>=0 && lSeg.end.y<prevEdge.left[lSeg.endIndex].y-SMALL_MARGIN)
					System.out.println();
	
				if (i>0){
					Segment prev = trArr[ trIndx[i-1] ].leftSeg;
					if (prev.endIndex>=lSeg.startIndex || prev.end.y>=lSeg.start.y){
						System.out.println();
					}
				}
			}
	
			for (int i = 0;i<trSz;++i){
				Segment rSeg = trArr[ trIndx[i] ].rightSeg;
				if (rSeg.num==0) continue;
				if (rSeg.endIndex>=prevEdge.rSize){
					System.out.println();
				}
				if (rSeg.startIndex>=0 && rSeg.start.y>prevEdge.right[rSeg.startIndex].y+SMALL_MARGIN || rSeg.endIndex>=0 && rSeg.end.y<prevEdge.right[rSeg.endIndex].y-SMALL_MARGIN)
					System.out.println();
				if (i>0){
					Segment prev = trArr[ trIndx[i-1] ].rightSeg;
					if (prev.endIndex>=rSeg.startIndex || prev.end.y>=rSeg.start.y){
						System.out.println();
					}
				}
			}
		//*/
			if (time>=BREAK_TIME){			
				System.out.println();
			}
		}
		
		double hl = edgeDetector==null || edgeDetector.highestPoint==null ? 0 : edgeDetector.highestPoint.length();
		Vector2D ahead = (edgeDetector==null) ? null : edgeDetector.currentPointAhead;
		double al = (ahead==null) ? 0 : ahead.length();
		canGoVeryFast = edgeDetector.highestPoint!=null && (hl>EdgeDetector.MAX_DISTANCE || inTurn && speedX<260 && hl>80 && al>30) && Math.abs(speedY)<MODERATE_SPEEDY && Math.abs(relativePosMovement)<0.01;
		canGoModerate = canGoVeryFast;
		if (!canGoVeryFast && inTurn &&  edgeDetector.highestPoint!=null && ahead!=null){
			canGoVeryFast = (hl>50 && speedX<160 && relativePosMovement>-0.001);
			if (	hl>80 && (al>30 || relativePosMovement>-0.001) && relativeTargetAngle<0.35 || 
					speedX<260 && hl>55 && hl>al && (al>55 || relativePosMovement>-0.001) ||
					speedX<250 && hl>52 && hl>al && (al>52 || relativePosMovement>-0.001) ||
					speedX<245 && hl>50 && hl>al && (al>50 || relativePosMovement>-0.001) ||
//					speedX<220 && hl>60 && (al>55 || relativePosMovement>-0.001) ||
					speedX<220 && hl>50 && hl>al && (al>50 || relativePosMovement>-0.001) ||
					speedX<200 && hl>50 && hl>al && (al>45 || relativePosMovement>-0.001) ||
					speedX<145 && hl>45 && hl>al && (al>35 || relativePosMovement>-0.001) 
					|| speedX<140 && hl>al && hl>40 && (al>30 || relativePosMovement>-0.001) 
					|| speedX<130 && hl>35 && hl>al && (al>25 || relativePosMovement>-0.001) 
					|| speedX<110 && hl>30 && hl>al && (al>20 || relativePosMovement>-0.001)
					|| speedX<85 && hl>30 && hl>al && (al>15 || relativePosMovement>-0.001) ){
				canGoVeryFast = true;
			}			
		}
		
		if (!canGoVeryFast){
			if (hl>80 && (al>30 || relativePosMovement>-0.001) && relativeTargetAngle<0.35 ||
				hl>=75 && al>=75 && relativeTargetAngle<0.35 ||	
				speedX<260 && hl>55 && (al>53 || relativePosMovement>-0.001) ||
				speedX<250 && hl>52 && (al>52 || relativePosMovement>-0.001) ||
				speedX<245 && hl>50 && (al>45 || relativePosMovement>-0.001) ||
				speedX<220 && hl>50 && (al>45 || relativePosMovement>-0.001) ||
				speedX<200 && hl>50 && (al>35 || relativePosMovement>-0.001) ||
				speedX<145 && hl>40 && (al>30 || relativePosMovement>-0.001) 
			|| speedX<140 && hl>40 && (al>25 || relativePosMovement>-0.001) 
			|| speedX<130 && hl>35 && (al>20 || relativePosMovement>-0.001) 
			|| speedX<110 && hl>30 && (al>15 || relativePosMovement>-0.001)
			|| speedX<85 && hl>30 && (al>10 || relativePosMovement>-0.001))
				canGoModerate = true;
			
		}

		boolean needCheck = true;
		Segment first = trSz==0 ? null : trArr[ trIndx[0] ];			
		if (inTurn || (first!=null && (first.type!=0 || Math.abs(first.end.x-first.start.x)>E))){
			//			l = (left==null) ? null : (inTurn) ?p Segment.segmentize(left, tW) : l;
			//			r -= (right==null) ? null : (inTurn) ? Segment.segmentize(right, -tW) : r;
			needCheck = inTurn;
			inTurn = true;
			lMap.setRowLen(sL);
			rMap.setRowLen(sR);			
			if (!EdgeDetector.isNoisy || !canGoVeryFast){ 
				combine(first, left, sL, right,sR);
			} else {
				for (int i =0;i<trSz;++i){
					occupied[ trIndx[i] ] = 0;					
				}
				trSz = 0;
			}
			if (needCheck)
				needCheck = false;
			else needCheck = !inTurn;
			sL = edgeDetector.lSize;
			sR = edgeDetector.rSize;			
			first = (trSz==0) ? null : trArr[ trIndx[0] ];			
		} else if (trSz==0)
			inTurn = true;
		else if (!inTurn && trSz>1){
			Segment snd = trArr[ trIndx[1] ];
			if ((first!=null && first.type==0 && first.end.y<2) && snd!=null && ((snd.type==0 && Math.abs(snd.start.x-snd.end.x)>E)|| (snd.type!=0 && snd.center!=null && snd.center.y<=nraced))) inTurn = true;

			if (inTurn){
				lMap.setRowLen(sL);
				rMap.setRowLen(sR);
//				combine(first, left, sL, right,sR);
				if (!EdgeDetector.isNoisy || !canGoVeryFast){ 
					combine(first, left, sL, right,sR);
				} else {
					for (int i =0;i<trSz;++i){
						occupied[ trIndx[i] ] = 0;					
					}
					trSz = 0;
				}
				sL = edgeDetector.lSize;
				sR = edgeDetector.rSize;				
				first = (trSz==0) ? null : trArr[ trIndx[0] ];
				needCheck = !inTurn;
			}			

		}				


		//		
		if (!needCheck && !inTurn && (first.type==0 && first.end.y-nraced<1 || !(prevEdge.straightDist<=0  || (nraced<=0 && first!=null && first.type==0 && first.start.y<nraced-1) || nraced<prevEdge.straightDist-0.3 || prevEdge.center!=null && prevEdge.center.y>=nraced))){
			lMap.setRowLen(sL);
			rMap.setRowLen(sR);
			if (!EdgeDetector.isNoisy || !canGoVeryFast){ 
				combine(first, left, sL, right,sR);
			} else {
				for (int i =0;i<trSz;++i){
					occupied[ trIndx[i] ] = 0;					
				}
				trSz = 0;
			}
			sL = edgeDetector.lSize;
			sR = edgeDetector.rSize;			
			first = (trSz==0) ? null : trArr[ trIndx[0] ];
		}

		if (needCheck && !inTurn) {									
			edgeDetector.combine(prevEdge, nraced,trArr,trIndx,trSz);		
			sL = edgeDetector.lSize;
			sR = edgeDetector.rSize;							
			left = edgeDetector.left;
			right = edgeDetector.right;
			lMap.setRowLen(sL);
			rMap.setRowLen(sR);
			if (debug) System.out.println("After combine : "+(System.nanoTime()-ti)/1000000);			
			trSz = Segment.reUpdate(trArr,trSz, tW);	
			if (trSz==0) {
				inTurn = true;		
				if (!EdgeDetector.isNoisy || !canGoVeryFast){ 
					combine(first, left, sL, right,sR);
				} else {
					for (int i =0;i<trSz;++i){
						occupied[ trIndx[i] ] = 0;					
					}
					trSz = 0;
				}
//				combine(first, left, sL, right,sR);
			}
			if (debug) System.out.println("Here : "+(System.nanoTime()-ti)/1000000);
			first = (trSz==0) ? null : trArr[ trIndx[0] ];
			turn = 2;
			int i=0;
			for (i = trSz-1;i>=1;--i){
				Segment s = trArr[ trIndx[i] ];
				int tp = s.type;
				if (tp!=0 && tp!=Segment.UNKNOWN){
					turn = tp;
					break;
				}
			}	
			edgeDetector.turn = turn;
		}
		
		highestPoint = edgeDetector.highestPoint;
		Segment last = (trSz<=0) ? null : trArr[ trIndx[trSz-1]];
		Segment lSeg = (last==null) ? null : last.leftSeg;
		Segment rSeg = (last==null) ? null : last.rightSeg;
		if (turn==2){
			turn = edgeDetector.whichE==0 && (highestPoint==null || highestPoint.length()<EdgeDetector.MAX_DISTANCE) ?  2 : (!inTurn && last!=null && last.type==0 && lSeg.endIndex>=edgeDetector.lSize-1 && rSeg.endIndex>=edgeDetector.rSize-1) ? 0 : (edgeDetector.whichE==1) ? -1 : 1;
			edgeDetector.turn = turn;
			if (turn==0) {
				recording = false;
				return;
			}			
		}		
		

		boolean swap = false;
		for (int i = trSz-1;i>=0;--i){
			int idx = trIndx[i];
			Segment t = trArr[idx];
			if (t.type==Segment.UNKNOWN || t.leftSeg.type==Segment.UNKNOWN || t.rightSeg.type==Segment.UNKNOWN){
				occupied[ idx ] = 0;
				if (trSz -i-1>0) System.arraycopy(trIndx, i+1, trIndx, i, trSz-i-1);
				trSz--;
				swap = true;
			}
		}
		if (swap)
			first = (trSz>0) ? trArr[ trIndx[0]] : null;			



			double sD = edgeDetector.straightDist;
			if (trSz>0 && first.type==0 && sD>0){			
				edgeDetector.straightDist = Math.max(sD, first.end.y);
				sD = edgeDetector.straightDist;
			}


			Segment l0 = (trSz>0) ? first.leftSeg : null;
			Segment r0 = (trSz>0) ? first.rightSeg : null;
			if (sD>0){
				if (l0!=null && l0.type==0 && sD<l0.end.y) sD = l0.end.y;
				if (r0!=null && r0.type==0 && sD<r0.end.y) sD = r0.end.y;
			}

			//		fix(l,sD);
			//		fix(r,sD);

			//		ls = l.size();	
			//		if (right!=null && right.size()>0) createStartEndArray(right.elements(), 0, right.size(), mapRS, mapRE, tr,r,1, tW);

			//		lm = ObjectArrayList.wrap(segAr, 0);
			//		rm = ObjectArrayList.wrap(segArr, 0);		

			if (debug) System.out.println("Time before store: "+(System.nanoTime()-ti)/1000000);
//			turn = edgeDetector.turn;

			Segment lastSeg = (trSz<=0) ? null : trArr[ trIndx[trSz-1] ]; 
			if (trSz>1 && lastSeg.type!=0 && lastSeg.type==turn && toOutterEdge<=GAP && toInnerEdge>=-GAP && (lastSeg.leftSeg.num<2 || lastSeg.rightSeg.num<2)){
				double rad = lastSeg.radius-tW+GAP; 
				if (Geom.ptTangentLine(0, 0, lastSeg.center.x, lastSeg.center.y, rad,tmpBuf)>0){
					double mx = (lastSeg.type==TURNRIGHT) ? tmpBuf[0] : tmpBuf[2];
					//double my = (lastSeg.type==TURNRIGHT) ? tmpBuf[1] : tmpBuf[3];
					if (mx*turn<0) 
						trSz--;
				}
			}

			if (guessTurn) guessTurn();
			first = (trSz<=0) ? null : trArr[ trIndx[0] ];
			lastSeg = (trSz<=0) ? null : trArr[ trIndx[trSz-1] ];			
			if (lastSeg!=null && lastSeg.type!=0 && lastSeg.type!=Segment.UNKNOWN && lastSeg.type!=turn) {
				turn = lastSeg.type;
				possibleChangeDirection= true;
			}
			if (inTurn && lastSeg!=null && lastSeg.type==0 && turn!=0 && turn!=2 && turn!=Segment.UNKNOWN && trSz>1) {
				if (trSz==2 && first.type!=0 && first.type!=turn)
					possibleChangeDirection = true;
				else if (trSz>2) {
					for (int i = trSz-2;i>=0;--i) {
						Segment t = trArr[ trIndx[i]];
						if (t.type!=0) {
							if (t.type!=turn)
								possibleChangeDirection = true;
							break;
						}
					}
				}
			}
			relativeAngleMovement = (turn==0 || turn==2) ? curAngle-lastAngle : (curAngle-lastAngle)*turn;
			relativeAngle = (turn==0 || turn==2) ? curAngle : curAngle*turn;			
			relativePosMovement = (turn==0 || turn==2) ? (curPos-lastPos) : (curPos-lastPos)*turn;					
			sL = edgeDetector.lSize;
			sR = edgeDetector.rSize;
			Segment prev = null;
			if (debug){
				for (int i = 0;i<trSz;++i){
					Segment s = trArr[ trIndx[i] ].leftSeg;
					int sr = (s.type==0 || s.radius>=Segment.MAX_RADIUS-1) ? Segment.MAX_RADIUS-1 : (int)(s.radius-s.type*tW);
					if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
					if (sr<0) continue;
					int sc = (s.map==null || s.type==Segment.UNKNOWN) ? -1 : s.map[sr];
					if (prev==null && s.startIndex>0){
						for (int j=0;j<s.startIndex;++j){
							System.out.print(left[j]+"    ");
						}
						System.out.println();
					} else if (prev!=null && prev.endIndex+1<s.startIndex){
						for (int j=prev.endIndex+1;j<s.startIndex;++j){
							System.out.print(left[j]+"    ");
						}
						System.out.println();
					}
					sb.setLength(0);
					sb.append("l  ");
					s.toBuffer(sb);
					sb.append("  "+sc);
					System.out.println(sb);
					if (i==trSz-1 && sL>s.endIndex+1){
						for (int j=s.endIndex+1;j<sL;++j){
							System.out.print(left[j]+"    ");
						}
						System.out.println();
					}
					prev = s;
				}


				System.out.println("-----------");
				prev = null;
				for (int i = 0;i<trSz;++i){
					Segment s = trArr[ trIndx[i] ].rightSeg;		
					int sr = (s.type==0 || s.radius>=Segment.MAX_RADIUS-1) ? Segment.MAX_RADIUS-1 : (int)(s.radius+s.type*tW);
					if (sr>=Segment.MAX_RADIUS) sr = Segment.MAX_RADIUS-1;
					if (sr<0) continue;
					int sc = (s.map==null || s.type==Segment.UNKNOWN) ? -1 : s.map[sr];
					if (prev==null && s.startIndex>0){
						for (int j=0;j<s.startIndex;++j){
							System.out.print(right[j]+"    ");
						}
						System.out.println();
					} else if (prev!=null && prev.endIndex+1<s.startIndex){
						for (int j=prev.endIndex+1;j<s.startIndex;++j){
							System.out.print(right[j]+"    ");
						}
						System.out.println();
					}
					sb.setLength(0);
					sb.append("r  ");
					s.toBuffer(sb);
					sb.append("  "+sc);
					System.out.println(sb);
					if (i==trSz-1 && sR>s.endIndex+1){
						for (int j=s.endIndex+1;j<sR;++j){
							System.out.print(right[j]+"    ");
						}
						System.out.println();
					}
					prev = s;
				}
			}
			if (inTurn && first!=null && first.type==0 && (l0.num>1 || r0.num>1) && Math.abs(first.start.x-first.end.x)<TrackSegment.EPSILON) inTurn = false;
			if (inTurn && (l!=null || r!=null)){			
				if ((l!=null && l0!=null && l0.type==0 && l0.num>1 && l0.end.y>=1 && Math.abs(l0.end.x-l0.start.x)<TrackSegment.EPSILON) 
						|| (r!=null && r0!=null && r0.type==0 && r0.num>1 && r0.end.y>=1 && Math.abs(r0.end.x-r0.start.x)<TrackSegment.EPSILON)) {
					inTurn = false;
					if (l0!=null && l0.type==0) l0.dist = distRaced + l0.start.y;
					if (r0!=null && r0.type==0) r0.dist = distRaced + r0.start.y;
					//				tr.clear();
				}			
			}


			if (debug) {
				System.out.println("-----------");		
				System.out.println("Time after store: "+(System.nanoTime()-ti)/1000000);
			}

			if (trSz>0){
				Segment s = trArr[ trIndx[0] ];
				temp[4-tempSz++] = s;
				if (tempSz>3) tempSz--;
				double rad = s.radius;
				count = 0;
				if (curSeg==null){
					Segment tmp;
					for (int i = 0;i<tempSz;++i){
						tmp = temp[4-i];
						if (tmp.type==s.type && tmp.radius==rad) count++;
					}
					if (count>=3){
						curSeg = new Segment();
						curSeg.copy(s);
						curSeg.dist = temp[4].dist;
					}
				} else if (rad!=curSeg.radius){
					Segment tmp;
					for (int i = 0;i<tempSz;++i){
						tmp = temp[4-i];
						if (tmp.type==s.type && tmp.radius==rad) count++;
					}				

					if (count>=3){
						curSeg.length = temp[4].dist - curSeg.dist;					
						track.add(curSeg);
						curSeg = new Segment(s);					
						curSeg.dist = temp[4].dist;					
					}
				}
			}


			cntr = null;
			rr=0;
			Vector2D hh = edgeDetector.highestPoint;		
			if (debug) System.out.println("Time after guessturn: "+(System.nanoTime()-ti)/1000000);
			if (edgeDetector.center==null && hh!=null && hh.length()>99.9) turn = 0;
			//		if (time>=5.3)
			//			System.out.println();
			if (debug) {
				if (trSz>0){
					for (int i =0;i<trSz;++i){
						sb.setLength(0);				
						trArr[ trIndx[i] ].toBuffer(sb);				
						System.out.println(sb);
					}
				}
			}


			if (edgeDetector.turn!=UNKNOWN){
				estimateBestPath();			
			}			
			estimatePath(hh);
			if (debug && (draw || (time>=BREAK_TIME))){
				draw=true;			
//				drawEndpoint("geCircle");
				//			draw = false;
				if (draw) {
					store();
					//			TrackSegment ts = (isTurning) ? TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, 0, 0, optimalPoint.x, optimalPoint.y) : TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, cntr.x-turn*rr, cntr.y, optimalPoint.x, optimalPoint.y);
					//				TrackSegment ts = TrackSegment.createTurnSeg(cntr.x, cntr.y, rr, cntr.x+rr, cntr.y, cntr.x-rr, cntr.y); 
					//				trackData.add(ts);

//					TrackSegment seg = TrackSegment.createStraightSeg(0,0, 0, carDirection.x*20, carDirection.y*20);
//					trackData.add(seg);				
//					trackE.add(seg);				
					//			ts = TrackSegment.createTurnSeg(centerOfTurn.x, centerOfTurn.y, radiusOfTurn, centerOfTurn.x-turn*radiusOfTurn, centerOfTurn.y, optimalPoint.x, optimalPoint.y);
					//			trackData.add(ts);			
					//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusSmall, center.x-turn*radiusSmall, center.y, center.x, center.y+radiusSmall);
					//			trackData.add(ts);
					//			ts = TrackSegment.createTurnSeg(center.x, center.y, radiusLarge, center.x-turn*radiusLarge, center.y, center.x, center.y+radiusSmall);
					//			trackData.add(ts);
				}

				if (draw) display();
				draw=false;
			}//*/
			
			prevEdge.copy(edgeDetector);				
			raced = distRaced;
			if (debug) System.out.println("Time : "+(System.nanoTime()-ti)/1000000);
			//		System.out.println(System.nanoTime()-time);	
	}

	public final CarControl restart() {		
		return new CarControl(0,0,0,0,1);
	}



	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#shutdown()
	 */	
	public final CarControl shutdown() {
		// TODO Auto-generated method stub
		System.out.println("Score  : "+distRaced);
		return new CarControl(0,0,0,0,2);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#init()
	 */

	private final void startRecordingEdge(CarState cs){
		//		long time = System.nanoTime();		
		EdgeDetector prevEdge = CircleDriver2.prevEdge;		
		maxTurn = true;
		recording = true;		
		//		double sx = -1;
		//		double sy = -1;
		double nraced = distRaced - raced;
		if (trSz>1 && prevEdge!=null){
			Segment first = trArr[ trIndx[0] ];
			if (first!=null && first.type==0 && nraced<100){
				if (nraced<first.end.y){// && edgeDetector.straightDist>0.5
					record(cs);					
					return;
				} 		
			} 
		} 
		inTurn = true;
		prevEdge.copy(edgeDetector);
		prevEdge.highestPoint = null;
		prevEdge.whichE = 0;

		//		highestPoint = edgeDetector.highestPoint;		
		highestPointOnOtherEdge = null;					

		for (int i = trSz-1;i>=0;--i)
			occupied[ trIndx[i] ] = 0;
		trSz = 0;	
		raced = distRaced;						

		//		System.out.println(System.nanoTime()-time);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#stopCondition(solo.State)
	 */

	//	public final boolean stopCondition(State<NewCarState,CarControl> state) {
	//		//		double posX = state.state.posX;
	//		//		double posY = state.state.posY;
	//		//		double cx = state.state.cx;
	//		//		double cy = state.state.cy;
	//		//		return Geom.distance(posX, posY, cx, cy)<2;
	//		return state.state.getLastLapTime()>=480;
	//		//		return (targetRadius>=300);
	//	}
	//
	//	
	//	public final boolean shutdownCondition(State<NewCarState, CarControl> state){
	//		return (stopCondition(state));
	//	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	
	private final double outOfTrackSteering(double signD,double deflect,double bal,double absSpeedY){
		return absSpeedY>VERY_HIGH_SPEEDY && relativeAngle>0 && relativeAngleMovement>0.001 && relativePosMovement<-0.02 ? -signD : 
			relativeAngle>0.3 ? relativeAngleMovement>-0.001 || relativeAngle>0.35 
					?  relativeAngleMovement>lastRelativeAngleMovement  || relativeAngleMovement>0.001 || relativeAngle>0.35  
							? -signD 
							: relativeAngleMovement<-0.01 ? 0 : relativePosMovement<-0.001 ? bal*0.3 : bal*2 
					: bal*deflect>0 ? -bal : bal 
		: relativeAngleMovement<-0.01 ? toOutterEdge<-GAP 
				? relativeAngle>0.25 
						? toOutterEdge>-W 
								? relativeAngleMovement<lastRelativeAngleMovement ? deflect : 0  
								: relativePosMovement>-0.001 ? bal : 0 
						:  signD 
				: toOutterEdge>-W 
					? signD 
					: relativePosMovement>0.001 
						? relativeAngleMovement>lastRelativeAngleMovement 
							? (relativeAngle>0.15) ? deflect : signD 
							:(relativeAngle>0.25) ? bal*0.5 
									: (relativeAngle>0.2 || absSpeedY>HIGH_SPEEDY && relativeAngle>0) ? deflect 
									:  signD
						: signD
		: (relativeAngle>0.25) 								
			? relativeAngleMovement>0.01 
				? toOutterEdge>-W && relativePosMovement<0 ? -signD 
				: toOutterEdge<-GAP ? relativeAngleMovement<lastRelativeAngleMovement && relativeAngleMovement<0.015
						? relativeAngleMovement<0.01 ? bal : -signD 
						: -signD 
					: relativeAngleMovement<lastRelativeAngleMovement ? bal*0.5 : -signD		
				: toOutterEdge>-GAP ? (relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement) ? bal*0.5 : 0 
						: (relativeAngleMovement>0.001 && relativeAngleMovement<lastRelativeAngleMovement || relativeAngleMovement<0) 
						? (absSpeedY>MODERATE_SPEEDY && absSpeedY<Math.abs(lastSpeedY) && relativePosMovement<0 || relativeAngleMovement<0.001) ? 0 : bal*0.5 
						: -signD  		
		: (relativeAngle>0.15) 
			?  relativeAngleMovement>0.01 
					? toOutterEdge>-W && relativePosMovement<0 
							? relativeAngleMovement<lastRelativeAngleMovement 
									? relativeAngleMovement>0.02 || relativeAngle>0.2 ? -signD :  0 
									: relativeAngleMovement>0.02 || relativeAngle>0.2 ? -signD : relativeAngleMovement>0.015 ? bal : bal*0.5
					: relativeAngleMovement<lastRelativeAngleMovement 							 
						? relativeAngleMovement>0.02 || relativeAngle>0.2 ? -signD : relativeAngleMovement>0.015 ? toOutterEdge>-W ? 0 : bal*0.5 : 0 
						: relativeAngleMovement>0.02 || relativeAngle>0.2 ? -signD : relativeAngleMovement>0.015 
								? toOutterEdge>-W ? bal*0.5 : bal 
								: 0  
			:  toOutterEdge<-W 
				? (relativeAngleMovement>0.001 && relativeAngleMovement<lastRelativeAngleMovement || relativeAngleMovement<0) 
					? (absSpeedY>MODERATE_SPEEDY && absSpeedY<Math.abs(lastSpeedY) && relativePosMovement<0 || relativeAngleMovement<0.001) ? deflect*0.5 : bal*0.3  
					: relativeAngleMovement>0.004 ? bal*0.5 : 0 
			: relativeAngleMovement<-0.001 && relativePosMovement>0 || relativeAngleMovement<lastRelativeAngleMovement  
				? relativeAngleMovement<-0.001 || relativeAngle<0.2 && relativeAngleMovement<lastRelativeAngleMovement ? signD : (relativeAngle<0.2 && relativeAngleMovement<0.015) ? deflect : 0 
				: relativeAngleMovement<-0.001 || relativeAngle<0.2 && relativeAngleMovement<lastRelativeAngleMovement ? signD : (relativeAngle<0.2 && relativeAngleMovement<0.015) ? deflect : 0  
		: (relativeAngleMovement>0.03 && relativeAngle>0) ? -signD : (relativeAngleMovement>0.02 && relativeAngle>0 && toOutterEdge<-GAP) 
					? relativeAngleMovement<lastRelativeAngleMovement || toOutterEdge>-W && relativeAngle<0.1 ? 0 : bal*0.5 
					: (relativeAngleMovement>0.01)
						? toOutterEdge>-W && relativePosMovement<0
//									? CircleDriver2.time<=150 ?
//									 relativeAngleMovement>0.02 ? -signD : relativeAngle<0 ? deflect*0.5 : relativeAngle>0.1 ? bal : 0
								?  relativeAngleMovement>0.02 
										? (relativeAngle>0.1) ? -signD : bal 
										: relativeAngle<0 ? signD : relativeAngle>0.1 ? 0 : deflect
						: relativeAngleMovement<lastRelativeAngleMovement  
							? relativeAngleMovement>0.02 ? -signD : relativeAngleMovement>0.015 ? toOutterEdge>-W || relativeAngle<0 ? 0 : bal*0.5 : deflect								
							: relativeAngleMovement>0.02 ? -signD : relativeAngleMovement>0.015 
									? toOutterEdge>-W || relativeAngle<0? 0 
									: bal*0.5 : 0
				: (relativeAngle>0.1) 
					? (relativeAngleMovement<-0.01) 
							? signD 
							: (relativePosMovement>0) 
								? toOutterEdge<-GAP || relativeAngleMovement<-0.001 
										? signD 
										: toOutterEdge<-GAP ? (absSpeedY>MODERATE_SPEEDY) 
											? signD : deflect 
											: (relativeAngleMovement<0.01 || relativePosMovement<0.001 && relativeAngleMovement<lastRelativeAngleMovement) ? signD : deflect 
								: (relativeAngleMovement<0 || lastRelativeAngleMovement>0.01 && relativeAngleMovement<lastRelativeAngleMovement && lastSteer*deflect>0) 
									? signD 
									: relativeAngleMovement<0.004 || relativeAngleMovement<lastRelativeAngleMovement ? deflect : 0
					: (relativeAngle>0) 
						? (relativeAngleMovement<-0.001) 
							? signD 
							: (relativeAngleMovement>0.008 && relativeAngleMovement>lastRelativeAngleMovement) ? deflect														
						: relativeAngleMovement<0.01 && (relativeAngleMovement<0 || lastRelativeAngleMovement>0.01 && relativeAngleMovement<lastRelativeAngleMovement && lastSteer*deflect>0 || relativeAngleMovement<0.01) ? signD : deflect
				: toOutterEdge>-W && relativeAngle<0.1 || relativeAngleMovement<0.01 && (relativeAngleMovement<0.01 || relativeAngle<0 || relativePosMovement<-0.001 && (relativeAngleMovement<0.006 || lastRelativeAngleMovement>0.01 && relativeAngleMovement<lastRelativeAngleMovement)) 
					? signD 
					: (relativeAngleMovement<-0.001 && lastSteer*deflect>0) ? maxAbs(deflect, lastSteer) : deflect;
	}

	private final double steerControl(CarState cs){											
		double signPos = (curPos<0) ? -1 :1;
		double deflect = Math.min(Math.abs(steerAtSpeed(cs.getSpeed())),Math.abs(curPos*0.1));

		deflect*=signPos;
		double relativeSpeedY = (turn==0 || turn==2) ? -Math.abs(speedY): turn*speedY;
		double absSpeedY = Math.abs(speedY);
		double absLastSpeedY = Math.abs(lastSpeedY);

		double lSteer = curAngle/steerLock;
		//All relative variables: >=0: correct direction;  <0 wrong direction
		relativeAngleMovement = (turn==0 || turn==2) ? -signPos*(curAngle-lastAngle) : (curAngle-lastAngle)*turn;
		relativeAngle = (turn==0 || turn==2) ? -signPos*curAngle : curAngle*turn;
//		double relativeHeading = (turn==0 || turn==2) ? -signPos*carDirection.x : carDirection.x*turn;
		relativePosMovement = (turn==0 || turn==2) ? -signPos*(curPos-lastPos) : (curPos-lastPos)*turn;		
//		double relativeSteer = (turn==0 || turn==2) ? -steer : -steer*turn;				
		double relativeCurPos = (turn==0 || turn==2) ? curPos : curPos*turn;	

		double bal = Math.atan2(speedY, speedX);
		if (Math.abs(toMiddle)>=tW){
			double signD = Math.signum(deflect);	
//			int correctTurn = signD<0 ? -1 : 1;
			/*if (turn==0 || turn==2 || turn!=correctTurn){
				relativeAngleMovement = (curAngle-lastAngle)*correctTurn;
				relativeAngle = curAngle*correctTurn;
				relativePosMovement = (curPos-lastPos)*correctTurn;		//						
				relativeCurPos = curPos*correctTurn;
				turn = correctTurn;
				toInnerEdge = curPos*tW*turn - tW;
				toOutterEdge = toInnerEdge + tW*2;
			}//*/
			
			if (turn!=0 && turn!=2 && relativePosMovement*toInnerEdge*(Math.abs(curPos)-Math.abs(lastPos))<0){
				relativeCurPos = -relativeCurPos;
				relativeSpeedY = -relativeSpeedY;
				relativeAngle = -relativeAngle;
				relativeAngleMovement = -relativeAngleMovement;
				relativePosMovement = -relativePosMovement;
				turn = -turn;
				toInnerEdge = (turn==0 || turn==2) ? -toMiddle-tW : curPos*tW*turn - tW;
				toOutterEdge = toInnerEdge + tW*2;	
			}
			if (debug){
				System.out.println("xxxxxxxxxxxxxxxxx");
				System.out.println("RelativeTargetAngle : "+relativeTargetAngle);
				System.out.println("RelativePosMovement :  "+relativePosMovement);
				System.out.println("RelativeAngle : "+relativeAngle);
				System.out.println("RelativeAngleMovement :  "+relativeAngleMovement);
				System.out.println("CanGoToLastSeg:  "+canGoToLastSeg);
				System.out.println("CanGoAtCurrentSpeed:  "+canGoAtCurrentSpeed);
				System.out.println("toInnerEdge:  "+toInnerEdge);
				System.out.println("toOutterEdge:  "+toOutterEdge);
				System.out.println("FlyingHazard =  "+flyingHazard);
				System.out.println("StartFlying =  "+startFlying);
				System.out.println("Landed =  "+landed);
				System.out.println("StartLanding =  "+startLanding);
			}
			Segment first = (trSz==0) ? null : trArr[ trIndx[0] ];
			Segment snd = (trSz<2) ? null : trArr[ trIndx[1] ];
			double first_speed = first!=null ? first.type==0 ? 1000 : speedAtRadius(first.radius) : 0;
			boolean isFast = (first!=null && speedX>first_speed+10);
			int tp = (first==null || first.type==0) ? turn : first.type;
			
			
			if (toInnerEdge<0 && relativeAngle<-0.5 && relativeAngleMovement<-0.01 || toInnerEdge>0 && relativeAngle>0.5 && relativeAngleMovement>0.01){
				gear = -1;
				steer = -signD;
				brake = 0;
				acc = (speedX<-10) ? CONSTANT_SPEED_ACC*0.25 : 0.6;
				return steer;				
			}
			
			if (flyingHazard && !landed){				
					steer =  (relativeAngle>0) ? deflect*0.2 : deflect*0.5; 
						
				if (toInnerEdge<0){					
					steer = outOfTrackSteering(signD, deflect, bal, absSpeedY);					
					if (relativePosMovement<-0.001 && (toOutterEdge<=-GAP || relativeAngle<0.001) ) 
						acc = (relativePosMovement<-0.01 || toOutterEdge>=-GAP) ? CONSTANT_SPEED_ACC*0.25 : Math.max(CONSTANT_SPEED_ACC*0.5,minAbs(acc, lastAcc)*0.95);
					if (relativeAngleMovement<0.001 && steer*deflect>0 && relativeAngle<0.2 || relativeAngleMovement>0.3 && relativeAngleMovement>0.01)
						acc = CONSTANT_SPEED_ACC*0.25;
					if (relativeAngle>0.2 && relativeAngle<0.3 || relativeAngle>0.25 && relativeAngleMovement<-0.001) 
						acc = 1;
					else if (relativeAngle>0.3 && relativeAngleMovement>-0.001 || relativeAngle>0.25 && relativeAngleMovement>0.01 || relativeAngle>0.15 && relativeAngleMovement>0.015)
						acc = CONSTANT_SPEED_ACC*0.25;
					return steer;
				} else if (relativeAngle<0 && relativeAngleMovement>0.01 && steer*turn<=0){
					steer = 0;
					acc = 1;
				}
				brake = 0;

			}
			if (tp!=0 && tp!=Segment.UNKNOWN && tp!=2 && tp!=0){
				if (toInnerEdge>0 && toInnerEdge<W   && isFast && (relativeAngleMovement<-0.01 || relativeAngleMovement<-0.001 && absSpeedY<absLastSpeedY || relativePosMovement<0.001 && (relativeAngle<0 || relativePosMovement<-0.001))){
					steer = relativeAngleMovement<-0.02 || relativeAngleMovement<-0.01 && relativeAngle<0.04 || relativeAngle<0 || relativePosMovement<-0.001 
							|| relativePosMovement<0.001 && (relativeAngleMovement<0 || relativePosMovement<lastRelativePosMovement) 
							|| toInnerEdge<GAP*0.5 && relativePosMovement<0.005 && speedX>first_speed+10 && relativeAngleMovement<-0.001 
							? speedX<150 ? relativeAngleMovement<-0.01 || relativeAngle<-0.1 ? 0 : deflect*0.5  : -tp 
							: relativeAngleMovement<-0.001 ? 0 : bal*0.5 ;
					acc = 0;
					
					if (speedX<first_speed+FAST_MARGIN){
						acc = 1;
					}
					if (speedX<first_speed+tW*3 && relativeAngle>0 && steer*tp<0) 
						steer = 0;
					/*if (relativeAngle>0.1){
						if (relativeAngleMovement<lastRelativeAngleMovement){
							steer = (relativeAngleMovement>0.01) 
										? signD
										: (relativeAngleMovement>0) 
											? deflect 
											: (relativeAngleMovement<-0.01) ? relativeAngle>0.15 ? deflect : 0 : 0;										  
						} else if (relativeAngleMovement>0.01) 
							steer = -signD;
						else if (relativeAngleMovement>0){
							steer = relativePosMovement>0 ? deflect : 0;
						} else if (relativeAngleMovement<-0.01)
							steer = relativeAngle>0.15 ? deflect : 0;
							else steer = 0;
					}//*/
					if (acc>0 && (relativeAngleMovement<-0.01 || relativePosMovement<-0.01 || relativePosMovement<-0.001 && (relativeAngleMovement<-0.001 || relativeAngleMovement<lastRelativeAngleMovement && relativeAngleMovement<0.01) ))
						acc *= (relativeAngle<-0.001) ? CONSTANT_SPEED_ACC*0.25 : CONSTANT_SPEED_ACC*0.25;
					else if (acc ==0) acc = CONSTANT_SPEED_ACC*0.25;
//					if (relativeAngle<-0.001 && toInnerEdge<=GAP*0.5) acc = 1;
					brake = 0;
					return steer;
				} else if (toInnerEdge>0 && speedX>first_speed+10){
					steer = toInnerEdge>W && (relativePosMovement>0.01 && relativeAngle>0 ||  relativePosMovement>0.001) 
							? (relativeAngleMovement<-0.01) 
								? relativeAngle>-0.05 ? deflect : 0 
								: relativeAngle>0 ? (relativePosMovement>0.01 || relativeAngle>0.15 || relativeAngleMovement>0.001) ? signD : deflect 
										: speedX>150 ? curAngle*0.5/steerLock : deflect 
						: relativePosMovement>-0.001 && speedX>first_speed+10 && toInnerEdge<GAP 
							? relativeAngleMovement<-0.01 || relativeAngle<0 || relativePosMovement<0.001 || toInnerEdge<GAP*0.5 && relativePosMovement<0.005 && speedX>first_speed+FAST_MARGIN
									? -tp 
									: relativeAngleMovement>0.01 
										? relativeAngleMovement>lastRelativeAngleMovement || relativeAngleMovement>0.015 || relativeAngle>0.05 ? signD : deflect  
										: 0 
						: (relativePosMovement<-0.01 || relativePosMovement<-0.001 && toInnerEdge<GAP)
							? relativeAngleMovement>0.01  
								? relativeAngleMovement>lastRelativeAngleMovement || relativeAngleMovement>0.015 || relativeAngle>0.05 ? signD : deflect  
								: speedX<150 ? relativeAngleMovement<-0.01 || relativeAngle<-0.1 ? 0 : deflect*0.5  : -tp 
						: relativeAngle<0 
							? (relativePosMovement<-0.001 && first_speed>150) 
								? -tp 
								: relativeAngle>-0.05 ? deflect : toInnerEdge<GAP ? 0 : deflect*0.5 
						: relativeAngle>0.3 || relativeAngleMovement>0.25 && relativeAngleMovement>-0.001 || relativeAngleMovement>0.15 && relativeAngleMovement>0.01 ? signD : deflect;
					if (toInnerEdge<GAP && steer==-tp)
						acc = (relativeAngleMovement>0 && relativeAngleMovement>lastRelativeAngleMovement) ? 1 : CONSTANT_SPEED_ACC*0.25;
					else acc = (isFast) ? CONSTANT_SPEED_ACC*0.25 : 1;
//					if (relativeAngle<-0.001 && toInnerEdge<=GAP*0.5) acc = 1;
	//				if (speedX<=maxSpeed){
	//					if (speedX<=maxSpeed-1.5)
	//						acc = 1;
	//					else acc = 2/(1+Math.exp(speedX - maxSpeed-1)) - 1;
	//				}
	//				if (acc>0 && (relativeAngleMovement<-0.01 || relativePosMovement<-0.01))
	//					acc *= (relativeAngle<-0.001) ? CONSTANT_SPEED_ACC*0.25 : CONSTANT_SPEED_ACC*0.25;
	//				else if (acc ==0) acc = CONSTANT_SPEED_ACC*0.25;
	//				acc = Math.max(acc,CONSTANT_SPEED_ACC);
					brake = 0;
					return steer;
				} else if (toInnerEdge>0 && speedX<first_speed){
					steer = toInnerEdge>GAP ? (relativeAngle>0.1) 
									? relativePosMovement>0 || relativeAngleMovement>0 ? signD : deflect
									: (relativeAngle>0) ? relativePosMovement>0 || relativeAngleMovement>0 ? signD : (relativeAngleMovement<-0.01) ? 0 : deflect*0.5
								: (relativeAngle>-0.1) 
									? (relativeAngleMovement<-0.01) ? 0 : deflect 
									: (relativeAngleMovement<-0.01) 
										? -signD   									
										: (relativeAngle>-0.15)
											? relativeAngleMovement>0 && toInnerEdge>W || relativeAngleMovement>0.01 ? relativeAngleMovement>0.01 ? signD : deflect : 0											
											: (relativeAngleMovement>0 && toInnerEdge<W || relativeAngle<-0.25) ? -signD : (relativeAngle<-0.2 && relativeAngleMovement<0.01) ? bal : 0 
							: (relativePosMovement<0)? deflect 
								: (relativeAngle>0 && relativeAngleMovement>0.001) ? Math.signum(lSteer) :
									(relativeAngle<0) ? relativeAngle>-0.05 ? deflect*0.5 : 0
										:signD;
					if (toInnerEdge<W && relativePosMovement>0.001 && steer*lastSteer>0) steer = maxAbs(steer, lastSteer);
					acc = speedX>50 && (slip>10 || relativeAngle<0 && (absSpeedY>MODERATE_SPEEDY || relativeAngleMovement>-0.001) ) ? CONSTANT_SPEED_ACC*0.25: 1;
					brake = 0;
					return steer;
				}
			}
//			if ((absSpeedY-absLastSpeedY>4 && relativeAngleMovement>0.03 || relativeSpeedY>=0 && (absSpeedY>absLastSpeedY+1.5 && relativePosMovement>=0 || relativePosMovement<0 && absSpeedY>absLastSpeedY  && absSpeedY>=MODERATE_SPEEDY) && (speed<maxSpeed+tW && lastSteer*steer>0) && relativeAngleMovement>0) && (lastSteer*turn<0 && Math.abs(lastSteer)<1 || lastSteer*turn>0 && lastSteer==Math.signum(speedY) && relativePosMovement>0 && lastAcc>0) || !isSafeToAccel && absSpeedY>=HIGH_SPEEDY && absSpeedY>absLastSpeedY && (!inTurn || lastSteer*turn>0) && relativeAngleMovement>0){				
//				steer = (flyingHazard && !landed) ? 0 :(relativeTargetAngle<0 || (absSpeedY>=HIGH_SPEEDY  && relativeAngleMovement>0 ||  absSpeedY-absLastSpeedY>4)) ? Math.signum(speedY) : (relativeAngle>0 && absSpeedY>=MODERATE_SPEEDY) ? steer*0.5 : steer;
//				acc = 1;
//				brake = 0;
//				return steer;
//			}			
			
			if (absSpeedY<HIGH_SPEEDY){
				if (speedX<maxSpeed){
					if (speedX<=maxSpeed-1.5)
						acc = 1;
					else acc = 2/(1+Math.exp(speedX - maxSpeed-1)) - 1;
					if (relativeAngle<0 && speedX>50 && relativePosMovement<-0.01 && relativeAngleMovement<-0.001 || turn!=0 && turn!=2 && (toOutterEdge<0 && relativeAngle<0.1 && relativePosMovement<-0.02 || relativePosMovement<-0.03 || relativePosMovement>0.03)) 
						acc = 0;
					else if (acc>0 && speedX>50 && relativePosMovement<0.001 && relativeAngleMovement<lastRelativeAngleMovement && toOutterEdge<0 && relativeAngle>0 && relativeAngle<0.1 && relativeAngleMovement>0)
						acc *= CONSTANT_SPEED_ACC;
					brake = 0;
				} else if (speed>maxSpeed+tW || absSpeedY>HIGH_SPEEDY){
					if (maxSpeed<0){
						brake = 0;
						acc = 1;
					} else {
						brake = (speed<maxSpeed+10 || maxSpeed<50) ? 0 : (speed*speed-maxSpeed*maxSpeed)/(speed*speed);
						acc = (speedX<40) ? 1 : 0;
					}
				}
								
				if (turn!=2 && turn!=Segment.UNKNOWN){
					if (tp==0)
						steer = (relativePosMovement<-0.01 && relativeAngle<-0.001) 
						? relativeAngleMovement>0.03 ? -signD : relativeAngleMovement>0.02 ? 0 : relativeAngleMovement>0.01 ? deflect : signD 
						: (relativePosMovement<0.001 && absSpeedY<MODERATE_SPEEDY) 
							? deflect 
							: relativeAngle>0.2 
								? relativeAngleMovement>0.01 || relativeAngle>0.25 
									? -signD
									: bal
							: relativeAngle>0.15 
								? relativeAngleMovement>0.01 
									? -signD
									: relativeAngle>0.1 
										? 0
										: deflect								
								: (relativeAngleMovement>0.02) ? -signD 
									: (relativeAngle>0.15 || relativeAngle>0.1 && relativePosMovement>0 && absSpeedY>=MODERATE_SPEEDY && relativeAngleMovement>0.001) 
										? relativeAngleMovement>0.01 ? -signD : bal*0.5 
										: (relativeAngle>0.05) 
											? deflect*0.5 
											: relativeAngle>0 
												? deflect
												: (relativeAngleMovement<-0.01 || relativeAngle<-0.1) ? signD : (relativeAngleMovement>0.01) ? deflect*0.5 : deflect;
					else if (toInnerEdge<0){
						if (relativePosMovement<-0.001 && toOutterEdge<=-GAP && relativeAngleMovement<-0.01) acc = (relativePosMovement<-0.01) ? CONSTANT_SPEED_ACC*0.25 : Math.max(CONSTANT_SPEED_ACC*0.5,minAbs(acc, lastAcc)*0.95);
//						if (toOutterEdge>-GAP && (relativeAngle<0 || relativeAngleMovement<0) && relativePosMovement<-0.001) 
//							acc = CONSTANT_SPEED_ACC*0.25;
//						
//						if (CircleDriver2.time>=330){
							if (toOutterEdge>-GAP && (relativeAngle<0 || relativeAngleMovement<0) && relativePosMovement<-0.001 && speedX>50) 
								acc = CONSTANT_SPEED_ACC*0.25;
							else if (relativePosMovement<-0.001 && relativeAngleMovement<-0.01 || relativeAngle<0.1 && speedX>50)
								acc = CONSTANT_SPEED_ACC*0.25;
							else if (toOutterEdge>-W && relativeAngle>0.1 && relativeAngle<0.15 && relativeAngleMovement>0.01 && relativeAngleMovement<0.015)
								acc = 1;
//						}
							
						if (relativeAngleMovement>lastRelativeAngleMovement && (relativePosMovement<lastRelativePosMovement || relativePosMovement<0.001 && relativePosMovement>-0.001)
								|| relativePosMovement<lastRelativePosMovement && relativeAngle>0 && absSpeedY<absLastSpeedY && absSpeedY>10)
							acc *= CONSTANT_SPEED_ACC;
						
						if (speedX>50 && (relativeAngleMovement<0.001 && steer*deflect>0 && relativeAngle<0.2 || relativeAngle<0.15 || relativeAngle>0.3 && relativeAngleMovement>0.001))
							acc = CONSTANT_SPEED_ACC*0.25;
						if (relativeAngle>0.2 && relativeAngle<0.3 || relativeAngle>0.25 && relativeAngleMovement<-0.001) acc = 1;
						if (acc==1 && slip>15 && relativePosMovement<lastRelativePosMovement && relativePosMovement<0.001 || relativeAngle>0.1 && relativeAngleMovement>0.015 || relativeAngle>0.2 && relativeAngleMovement>0.01 || relativeAngle>0 && relativeAngleMovement>0.02) acc = CONSTANT_SPEED_ACC*0.25;
						
						if (bal*deflect>0) bal = -bal;
						steer = outOfTrackSteering(signD, deflect, bal, absSpeedY);
					} else steer = (relativeAngle<-0.15 && relativeAngleMovement<-0.001) ? -signD : (relativeAngle<-0.1) ? bal*0.5 :(relativeAngle<-0.05 || absSpeedY>=MODERATE_SPEEDY && relativeAngle<0) ? (speedX>150 && relativePosMovement<-0.001) ? 0 : deflect*0.5 : lastSteer*deflect>0 && relativeAngle>=0 ? maxAbs(lastSteer, deflect) 
							: (relativePosMovement<-0.01) ? relativeAngleMovement<-0.01 || relativeAngle<0 && toInnerEdge<GAP ? -signD : 0 : deflect*0.5;
				} else {
					steer = (relativeAngle>0.15) ? (relativeAngleMovement>0.03) ? 2*curAngle/steerLock : (relativeAngleMovement>0) ? 0.5*curAngle/steerLock : 0 
							: (relativeAngle>0.1)
								? (relativeAngleMovement>0 && relativePosMovement>0.001) ? 0 : deflect
								: (relativeAngle<0 && relativeAngleMovement<0.01) ? signD 
										: (relativeAngleMovement>0.02) ? (relativeAngle<0.1) ? -signD : bal 
												: (relativeAngleMovement>0.015) 
													? bal 
													: (relativeAngleMovement>0.01) ? (relativeAngleMovement<lastRelativeAngleMovement) ? deflect : 0:  deflect;
					return steer;
				}
				
//				if (steer*deflect>0) steer = deflect*0.5;
				/*if (steer*turn<=0  && (flyingHazard || startFlying>0 || slip>15)){
					if (relativeAngle>=0){
//						steer = (absSpeedY<MODERATE_SPEEDY && relativePosMovement>0.001 && relativeAngle>=0.2 && relativeAngleMovement<-0.001 && relativeAngleMovement>-0.01) ? 0 : relativeAngle<0.3 && (relativeAngle>0.25 && relativeAngleMovement>0.001 || relativePosMovement>0.001 && relativeAngle>0.2 && relativeAngleMovement>0.01 && toOutterEdge>=-W) ?
//								bal*0.5 : steer;
					} else steer = (startFlying>0 || !flyingHazard) 
							? (relativeAngle>0) ? steer*0.5 : steer 
							: (!landed || relativeAngle>=0) ? steer*0.2 : (relativeAngleMovement<-0.01 && toInnerEdge<0) ? signD : steer;
				}//*/
				if (toInnerEdge<0) return steer;
				/*if (inTurn && relativeAngleMovement<0 && speedY*lastSpeedY>0){
					if ((relativeSpeedY>0 && (absSpeedY<absLastSpeedY && steer*turn>=0|| absSpeedY<absLastSpeedY-1.5 || absSpeedY<absLastSpeedY-1.2 && relativePosMovement<0) || relativeSpeedY<0 && absSpeedY>absLastSpeedY+1.5)   && absSpeedY>=LOW_SPEEDY && relativeTargetAngle>=0) {
						steer = (relativeTargetAngle>0 && (relativeSpeedY>0 || absSpeedY>HIGH_SPEEDY)) ? -turn : 0;
//						steer = -turn;
						if (!canGoToLastSeg && !canGoAtCurrentSpeed) acc *= CONSTANT_SPEED_ACC;
					}
				} else if (inTurn && relativeAngleMovement>0 && speedY*lastSpeedY>0 && (relativeSpeedY<0 && absSpeedY<absLastSpeedY-1.5 || relativeSpeedY>0 && absSpeedY>absLastSpeedY+1.5) && absSpeedY>=LOW_SPEEDY && relativeTargetAngle<=0){
					steer = (relativeTargetAngle<0 && (relativeSpeedY<0 || absSpeedY>HIGH_SPEEDY) ) ? turn : 0;
					if (!canGoToLastSeg && !canGoAtCurrentSpeed) acc *= CONSTANT_SPEED_ACC;
				}  else if (inTurn && relativeSpeedY<0 && relativeAngle<0 && relativeAngleMovement<0 && absSpeedY>absLastSpeedY+1.5 && relativeTargetAngle>=0){
					if (canGoToLastSeg) steer = (relativeCurPos<0) ? 0 : steer;
				} else if (inTurn && relativeSpeedY>0 && relativeAngle>0 && relativeAngleMovement>0 && absSpeedY>absLastSpeedY+1.5 && relativeTargetAngle>=0){
					if (canGoToLastSeg) steer = (relativeCurPos>0) ? 0 :-turn;
				}//*/
//				if (relativePosMovement<-0.001 && relativeAngle<-0.001 && (relativeAngleMovement<-0.001 || relativeAngle<-0.01))
//					acc *= 0.4;
				return steer;
			}
			
			recording = false;			
			if (first!=null && trSz>1){				
				if (first.type==0 && snd.type!=0 && snd.center!=null && first.end.y<snd.center.y-1){
					double d = Math.sqrt(Geom.ptLineDistSq(first.start.x, first.start.y, first.end.x, first.end.y, snd.center.x, snd.center.y, null));
					d = Math.round(d-snd.type*tW)+snd.type*tW;
					if (d==snd.radius){
						first.end.y = Math.max(snd.center.y-1,first.end.y);
						prevEdge.straightDist = Math.max(first.end.y, prevEdge.straightDist);
						first.reCalLength();
					}

				}
			}

			if (absSpeedY>=MODERATE_SPEEDY || absSpeedY>Math.abs(lastSpeedY)+4){
				acc = (relativeAngle>0) ? toInnerEdge>0 ? speedX<50 ? 0.5 : CONSTANT_SPEED_ACC*0.25 
						: (relativePosMovement<-0.01 && (relativeAngleMovement>0 || relativePosMovement<lastRelativePosMovement) || speedX<50) 
								? 1 
								: (relativePosMovement<-0.001 || toOutterEdge<-GAP) && relativeAngleMovement<-0.001 ? CONSTANT_SPEED_ACC*0.5 : 1 
						: (speedX<50) ? 1 : CONSTANT_SPEED_ACC*0.25;				
				if (relativePosMovement<-0.02 && acc>CONSTANT_SPEED_ACC*0.25) acc = relativePosMovement<-0.03 || toOutterEdge<0 && relativeAngle<0? 0 : acc*CONSTANT_SPEED_ACC;
				
				if (speedX>50 && (relativeAngleMovement<0.001 && steer*deflect>0 && relativeAngle<0.2 || relativeAngle<0.15 || relativeAngle>0.3 && relativeAngleMovement>0.001))
					acc = (relativeAngleMovement>lastRelativeAngleMovement && relativeAngle>0.2 || relativeAngle<0.15 && relativeAngleMovement<0) ? CONSTANT_SPEED_ACC*0.25 : acc;
				if (relativeAngle>0.2 && relativeAngle<0.3 || relativeAngle>0.25 && relativeAngleMovement<-0.001) acc = 1;
				if (acc==1 && speedX>50 && slip>15 && relativePosMovement<lastRelativePosMovement && relativePosMovement<0.001) acc = CONSTANT_SPEED_ACC*0.5;				
				
				if (absSpeedY>HIGH_SPEEDY || absSpeedY>Math.abs(lastSpeedY)+4) 
					steer = absSpeedY>VERY_HIGH_SPEEDY && relativeAngle>0 && relativeAngleMovement>0.001 && relativePosMovement<-0.02 ? -signD 
							:(relativeAngle>0.3) ? (relativeAngleMovement>0.001 || relativeAngleMovement>0.001 && relativeAngleMovement>lastRelativeAngleMovement || relativeAngleMovement>-0.001 && relativeAngle>0.35) 
									? -signD 
									: (relativeAngleMovement>0.001 || relativeAngleMovement>-0.01 && relativeAngleMovement>lastRelativeAngleMovement) ? bal*0.5 : 0 
										: (relativeAngle>0.25) 
											? (relativeAngleMovement<-0.01 && toOutterEdge<-GAP) 
													? relativePosMovement>-0.001 ? (relativeAngleMovement>0.01) ? -signD : bal : signD 
													: (toOutterEdge<-GAP && (relativeAngleMovement<-0.001  || relativeAngleMovement<lastRelativeAngleMovement )) 
														? relativeAngleMovement<-0.001 ? deflect : (relativeAngleMovement>0.01) ? -signD : 0 
															: relativePosMovement>0 
																? relativeAngleMovement>0.01 
																		? -signD 
																		: (relativeAngleMovement>0.001 || relativeAngleMovement>-0.01 && relativeAngleMovement>lastRelativeAngleMovement) ? bal*0.5 : 0
																: relativeAngleMovement>0.01 ? -signD : 0
													:(relativeAngle>0.2) 
														? (relativeAngleMovement<-0.01 && toOutterEdge<-GAP) 
																? relativePosMovement>-0.001 ? 0 : signD
																: (toOutterEdge<-GAP && (relativeAngleMovement<-0.001  || relativeAngleMovement<lastRelativeAngleMovement )) 
																	? relativeAngleMovement<-0.001 ? deflect : (relativeAngleMovement>0.01) ? -signD : 0 
																	: relativePosMovement>0 
																		? relativeAngleMovement>0.01 ? -signD : bal
																		: relativeAngleMovement>0.015 ? -signD :relativeAngleMovement>0.01 ? bal : 0
																			
														: (relativeAngle>0 && (relativeAngle<0.2 || relativeAngleMovement<-0.001)) 
															? (relativeAngle<0.3 && relativeAngleMovement<-0.01 && toOutterEdge<-GAP) 
																	? stableSteer(signD) 
																	: relativeAngle>0.1 && relativeAngleMovement>0.01 || relativeAngle>0 && relativeAngleMovement>0.02 
																		? (toOutterEdge>-W && relativeAngle<0.15) ? signD : -signD 
																: (relativeAngleMovement<-0.001) ? signD : stableSteer(deflect) 
															: (relativeAngleMovement<-0.02 || relativeAngleMovement<0.001 && relativeAngle<0.1) 
																? signD  
																: (relativeAngle<0) ? signD : deflect;
				else steer = curAngle/steerLock+deflect;
				
				if (acc>=CONSTANT_SPEED_ACC && speedX>50 && (relativeAngle>0.25 && steer*signD<0 || relativeAngle>0.35 || relativeAngle>0.2 && relativeAngleMovement>0.01 || relativeAngle>0.15 && relativeAngleMovement>0.015)&& relativeAngleMovement>0.001 ) 
					acc = CONSTANT_SPEED_ACC*0.25;
				else if (relativeAngle>0.35 && relativeAngleMovement<-0.001 && steer*signD<=0)
					acc = 1;
				return steer;
			}
			
			if (tp==0 || !inTurn){
				return  curAngle/steerLock+deflect;
			}
//			double relativeAngleMovement = (curAngle-lastAngle)*tp;
//			double relativeAngle = curAngle*tp;
//			//			double relativeHeading = carDirection.x*tp;
//			//			double relativePosMovement = (curPos-lastPos)*tp;														
//			double relativeSpeedY = tp*speedY;

			double steer = (absSpeedY>=HIGH_SPEEDY) ? tp-deflect : (first!=null && first.type*curPos>0 && speed>speedAtRadius(first.radius)) ? (relativeAngle>=-0.001 || relativeAngleMovement>=0.001) ? ((relativeSpeedY<-0.001 || absSpeedY<=LOW_SPEEDY)&& trSz>1 && trArr[ trIndx[trSz-1] ].type==0) ? curAngle/steerLock : -deflect : -tp-deflect : (curAngle*tp>0) ? 0 : curAngle/steerLock; 
			return (relativeCurPos>0 && Math.abs(toMiddle)<tW+W*1.5) ? curAngle/steerLock : steer+deflect;
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
			return curAngle/steerLock+deflect*0.5;
		}


		if (turn==STRAIGHT){
			recording = false;
			isTurning = false;
			maxSpeed = Double.MAX_VALUE;
			inTurn = false;
			if (trSz>0){
				for (int i = trSz-1;i>=0;--i)
					occupied[ trIndx[i] ] = 0;
				trSz = 0;
			}
			//			if (l!=null && l.size()>0) l.size(0);
			//			if (r!=null && r.size()>0) r.size(0);
			steer = (absSpeedY>=MODERATE_SPEEDY && relativeAngle>0 || relativeAngle>0.15) ? (absSpeedY>MODERATE_SPEEDY && relativeAngle<0.15) ? 0 :  bal*0.5 : lSteer+deflect;
//			if (Math.abs(steer)<0.01) steer = 0; 
//			smoothSteering();
			return steer;
		}

		if (!recording) {
			startRecordingEdge(cs);			
			maxSpeed = Double.MAX_VALUE;
			steer = (absSpeedY>10) ? lSteer : lSteer + deflect*0.15;
			return steer;
		}

		if (turn==UNKNOWN){
			//			if (maxSpeed<150) 
			maxSpeed = Double.MAX_VALUE;
			//			else maxSpeed = cs.speedX-1;
			isTurning = false;	
			inTurn = false;
			double sign = (curPos<0) ? -1 : 1;
			double offset = (Math.abs(relativeCurPos)>0.1) ? relativeCurPos : sign*0.15;
			if (edgeDetector.highestPoint!=null && edgeDetector.highestPoint.y<75 && speedX>200) offset*=2;
			steer = (absSpeedY>10) ? lSteer : (absSpeedY<LOW_SPEEDY) ? lSteer + offset*0.75: lSteer + offset;
//			if (isOffBalance) {
//				System.out.println();
//				steer *=0.1;
//				steer = 0;
//			}
//			if (hazard==s(0) smoothSteering();
			return steer;
		}


		return fuzzySteering(cs);
	}

	private final void store(){
		series = new XYSeries("Curve");		
		int trackESz = 0;
//		Vector2D[] lArr = edgeDetector.left;
//		Vector2D[] rArr = edgeDetector.right;
//		int sL = edgeDetector.lSize;
//		int sR= edgeDetector.rSize;
		Vector2D[] lArr = EdgeDetector.nleft;
		Vector2D[] rArr = EdgeDetector.nright;
		int sL = edgeDetector.nLsz;
		int sR= edgeDetector.nRsz;
		for (int i=sL-1;i>=0;--i){	
			Vector2D v = lArr[i]; 
			if (v!=null) {
				series.add(v.x, v.y);
				TrackSegment ts = TrackSegment.createStraightSeg(0, v.x, v.y, v.x, v.y);
				trackEArr[trackESz++] = ts;
			}

		}

		if (edgeDetector.highestPoint!=null) {
			series.add(edgeDetector.highestPoint.x,edgeDetector.highestPoint.y);
			TrackSegment ts = TrackSegment.createStraightSeg(0, edgeDetector.highestPoint.x, edgeDetector.highestPoint.y, edgeDetector.highestPoint.x, edgeDetector.highestPoint.y);
			trackEArr[trackESz++] = ts;
		}
		for (int i=sR-1;i>=0;--i){
			Vector2D v = rArr[i];
			if (v!=null) {
				series.add(v.x, v.y);
				TrackSegment ts = TrackSegment.createStraightSeg(0, v.x, v.y, v.x, v.y);
				trackEArr[trackESz++] = ts;
			}
		}
		TrackSegment.line(0, 0, carDirection.x*20, carDirection.y*20, series);

		/****
		Segment[] lArray = l.elements();
		int ls = l.size();
		Segment[] rArray = r.elements();
		int rs = r.size();
		TrackSegment.line(0, 0, carDirection.x*20, carDirection.y*20, series);
		if (l!=null)
			for (int i = ls-1;i>=0;--i){
				Segment s = lArray[i];
				if (s!=null && s.type!=Segment.UNKNOWN) trackEArr[trackESz++] = new TrackSegment(s);
			}

		if (r!=null)
			for (int i = rs-1;i>=0;--i){
				Segment s = rArray[i];
				if (s!=null && s.type!=Segment.UNKNOWN) trackEArr[trackESz++] = new TrackSegment(s);
			}
		//		for (Segment edge : l){
		//			if (edge!=null && edge.center!=null){
		//				Vector2D p1 = edge.get(edge.index+1);
		//				Vector2D p2 = (edge.nIndex!=-1) ? edge.get(edge.nIndex-1) : edge.getHighestPoint();
		//				TrackSegment ts1 = TrackSegment.createTurnSeg(edge.center.x, edge.center.y, edge.radius, p1.x, p1.y, p2.x, p2.y);
		//				trackE.add(ts1);
		//				if (edge.nIndex!=-1 && edge.nextCenter!=null){
		//					p1 = (edge.index<edge.size-1) ? edge.get(edge.index+1):edge.getHighestPoint();
		//					p2 = edge.getHighestPoint();
		//					ts1 = TrackSegment.createTurnSeg(edge.nextCenter.x, edge.nextCenter.y, edge.nextRadius, p1.x, p1.y, p2.x, p2.y);
		//					trackE.add(ts1);
		//				}
		//				p1 = edge.get(0);
		//				p2 = edge.get(edge.index);
		//				if (p1!=null && p2!=null) {
		//					ts1 = TrackSegment.createStraightSeg(0, p1.x, p1.y, p2.x, p2.y);
		//					trackE.add(ts1);
		//				}
		//	
		//			}
		//		}
		 * 
		 */

		/*if (other!=null && other.center!=null ){
			Vector2D p1 = (other.index<other.size-1) ? other.get(other.index+1):other.getHighestPoint();
			Vector2D p2 = (other.nIndex!=-1) ? other.get(other.nIndex-1) : other.getHighestPoint();
			Segment ts1 = new Segment(p1.x, p1.y, p2.x, p2.y,other.center.x, other.center.y, other.radius,0,false);
			trackEArr[trackESz++] = ts1;
			if (other.nIndex!=-1 && other.nextCenter!=null){
				p1 = (other.nIndex<other.size) ? other.get(other.nIndex) : other.getHighestPoint();
				p2 = other.getHighestPoint();
				ts1 = new Segment(p1.x, p1.y, p2.x, p2.y,other.nextCenter.x, other.nextCenter.y, other.nextRadius,0,false);
				trackEArr[trackESz++] = ts1;				
			}
			p1 = other.get(0);
			p2 = other.get(other.index);
			if (p1!=null && p2!=null) {
				ts1 = new Segment(0, p1.x, p1.y, p2.x, p2.y,0,0,Double.MAX_VALUE,false);
				trackEArr[trackESz++] = ts1;
			}
		}//*/
		trackE.size(trackESz);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */




}
