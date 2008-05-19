/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2DoubleRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2DoubleSortedMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.graphbuilder.curve.ControlPath;
import com.graphbuilder.curve.Curve;
import com.graphbuilder.curve.GroupIterator;
import com.graphbuilder.curve.NaturalCubicSpline;
import com.graphbuilder.curve.ShapeMultiPath;

/**
 * @author kokichi3000
 *
 */
public class EdgeDetector {


	public final static double DELTA = 0.001;

	public final static double PI_2 = Math.PI/2;

	public final static double UNKNOWN_ANGLE = -2;
	public final static double DELTATIME = 0.002;
	public final static double MAXSTEERSPEED = Math.PI*2/3;
	public final static double ANGLEACCURACY =100.0d; 
	ControlPath cp=null;

	double trackWidth=0;
	double distanceToTurn=0;
	double curPos=0;
	cern.colt.list.DoubleArrayList allAngles;
	double[] allDistances;
	double[] allX;
	double[] allY;
	double distRaced;

	cern.colt.list.DoubleArrayList x = null;//x is axis along track axis
	cern.colt.list.DoubleArrayList y = null;

	final static int NUM_POINTS=2000;
	DoubleArrayList leftEdgeX = null;
	DoubleArrayList leftEdgeY = null;
	DoubleArrayList rightEdgeX = null;
	DoubleArrayList rightEdgeY = null;
	CurveCoordinate leftCC = null;
	CurveCoordinate rightCC = null;
	double[] leftRange = new double[2];
	double[] rightRange = new double[2];
	int numPointLeft = 0;
	int numPointRight = 0;
	public double pointx,pointy;
	public double angleToPoint;	
	Double2DoubleSortedMap angleDistMap = null;
	Double2ObjectSortedMap<double[]> polar2Catesian = null;	
	double curAngle;
	double maxY;

	int firstIndexMax = -1,lastIndexMax = -1;
	JFrame jf;
	double maxDistance=-1;
	public double leftStraight = -1;
	public double rightStraight = -1;
	public double straightDist =-1;

	public EdgeDetector() {		
	}

	public EdgeDetector(EdgeDetector ed){
		x=ed.x;
		y=ed.y;
		numPointLeft = ed.numPointLeft;
		maxY = ed.maxY;
		distRaced = ed.distRaced;
		numPointRight = ed.numPointRight;
		leftStraight = ed.leftStraight;
		rightStraight = ed.rightStraight;
		straightDist = ed.straightDist;
		firstIndexMax = ed.firstIndexMax;
		lastIndexMax = ed.lastIndexMax;
		pointx = ed.pointx;
		pointy = ed.pointy;
		distanceToTurn = ed.distanceToTurn;
		trackWidth = ed.trackWidth;
		curPos = ed.curPos;
		curAngle = ed.curAngle;
		polar2Catesian = new Double2ObjectRBTreeMap<double[]>(ed.polar2Catesian);
		angleDistMap = new Double2DoubleRBTreeMap(ed.angleDistMap);
		angleToPoint = ed.angleToPoint;
		maxDistance = ed.maxDistance;
		allAngles = (ed.allAngles==null) ? null : ed.allAngles.copy();
		allX = (ed.allX==null) ? null : DoubleArrays.copy(ed.allX);
		allY = (ed.allY==null) ? null : DoubleArrays.copy(ed.allY);
		allDistances = (ed.allDistances==null) ? null : DoubleArrays.copy(ed.allDistances);
		leftEdgeX = (ed.leftEdgeX==null) ? null : new DoubleArrayList(ed.leftEdgeX);
		leftEdgeY = (ed.leftEdgeY==null) ? null : new DoubleArrayList(ed.leftEdgeY);
		rightEdgeX = (ed.rightEdgeX==null) ? null : new DoubleArrayList(ed.rightEdgeX);
		rightEdgeY = (ed.rightEdgeY==null) ? null : new DoubleArrayList(ed.rightEdgeY);
		leftRange = (ed.leftRange==null) ? null : DoubleArrays.copy(ed.leftRange);
		rightRange = (ed.rightRange==null) ? null : DoubleArrays.copy(ed.rightRange);
		leftCC = (ed.leftCC==null) ? null : new CurveCoordinate(ed.leftCC);
		rightCC = (ed.rightCC==null) ? null : new CurveCoordinate(ed.rightCC);
	}

	public EdgeDetector(CarState cs) {
		// TODO Auto-generated constructor stub

		init(cs);
	}

	public void init(CarState cs){
		double[] tracks = cs.getTrack();
		curPos = -cs.getTrackPos();
		curAngle = cs.getAngle();
		maxDistance=-1;
		firstIndexMax = -1;
		lastIndexMax = -1;		
		angleDistMap = null;
		polar2Catesian = null;
		allDistances = null;
		allAngles = null;
		allX = null;
		allY = null;
		leftEdgeX = null;
		leftEdgeY = null;
		rightEdgeX = null;
		rightEdgeY = null;
		leftCC = null;
		rightCC = null;
//		cp=null;
//		cp = new ControlPath();
		angleDistMap = null;
		polar2Catesian = null;
		maxY = -1;
		x=null;
		y=null;
		distRaced = cs.distRaced;

		angleDistMap = new Double2DoubleRBTreeMap(); 		
		polar2Catesian = new Double2ObjectRBTreeMap<double[]>();		

		if (Math.abs(cs.angle)<0.001){
			trackWidth = Math.round((tracks[0]+tracks[18])*Math.cos(cs.angle));
//			System.out.println(tracks[0]+"  "+tracks[18]+"  "+trackWidth);
		}
		x = new cern.colt.list.DoubleArrayList(150);//x is axis along track axis
		y = new cern.colt.list.DoubleArrayList(150);

		leftStraight=-1;
		rightStraight=-1;
		double toLeft = toLeftEdge();
		double toRight = toRightEdge();
	
		for (int i=0;i<19;++i){
			double  angle = Math.PI-SimpleDriver.ANGLE_LK[i]-cs.angle;			
			double x =Math.round(tracks[i]* Math.cos(angle)*10000.0d)/10000.0d;
			double y =Math.round(tracks[i]* Math.sin(angle)*10000.0d)/10000.0d;	
			this.x.add(x);
			this.y.add(y);			

//			if (tracks[i]<100) {				
			angle=Math.round((Math.PI-angle)*ANGLEACCURACY)/ANGLEACCURACY;
			polar2Catesian.put(angle, new double[]{x,y});			
			angleDistMap.put(angle, tracks[i]);			
//			}

//			cp.addPoint(PointFactory.createPoint(x, y));
			if (maxDistance<tracks[i])
				maxDistance = tracks[i];

			if (maxY<y){
				maxY = y;
				firstIndexMax =i;
				lastIndexMax =i;
				pointx=x;
				pointy=y;
				angleToPoint=PI_2-angle;
			} else if (maxY==y){
				lastIndexMax =i;
			}

//			if (i>=1 && SimpleDriver.ANGLE_LK[i]+cs.angle>=PI_2 && SimpleDriver.ANGLE_LK[i-1]+cs.angle<PI_2){				
//				distanceToTurn = (SimpleDriver.ANGLE_LK[i]+cs.angle-PI_2<PI_2-SimpleDriver.ANGLE_LK[i-1]-cs.angle)? 
//						y*Math.cos(SimpleDriver.ANGLE_LK[i]+cs.angle-PI_2):this.y.getDouble(i-1)*Math.cos(PI_2-SimpleDriver.ANGLE_LK[i-1]-cs.angle);
//			} else if (i==0 && cs.angle>PI_2){
//				distanceToTurn = y*Math.cos(cs.angle-PI_2);
//			}

			double r = x+toLeft;
			if (Math.abs(r)<=DELTA && leftStraight<y)
				leftStraight = y;

			r = x+toRight;
			if (Math.abs(r)<=DELTA && rightStraight<y)
				rightStraight = y;

		}
		
		straightDist = (leftStraight>rightStraight) ? rightStraight : leftStraight;
	}


	public void combine(EdgeDetector ed,double ax,double ay){
		long time = System.currentTimeMillis();
		int len=ed.x.size();
		maxDistance = -1;
		lastIndexMax = -1;
		firstIndexMax = -1;
		leftStraight = -1;
		rightStraight = -1;
		straightDist = -1;
		if (this.trackWidth<=0) this.trackWidth = ed.trackWidth;
		double oldTrackWidth = ed.trackWidth;
		double scale = this.trackWidth/oldTrackWidth;		
		double[] xx = ed.x.elements();
		double[] yy = ed.y.elements();		
		DoubleSortedSet ds = this.angleDistMap.keySet();
		
//		int index =0;		
		for (int i=0;i<len;++i){			
			double x = xx[i]*scale;
			double y = yy[i];
			if (Math.sqrt(x*x+y*y)>=99) continue;
			x+=ax;
			y+=ay;
			if (y<0 || y<straightDist) continue;
			double dist = Math.sqrt(x*x+y*y);
			
			double  angle = Math.PI-Math.atan2(y,x);
			if (angle<0 || angle>Math.PI) continue;
			angle=Math.round(angle*ANGLEACCURACY)/ANGLEACCURACY;
//			System.out.println(x+"   "+y+"     "+angle);
//			this.x.add(Math.round(x*10000.0d)/10000.0d);
//			this.y.add(Math.round(y*10000.0d)/10000.0d);			
//			if ( angleDistMap.containsKey(angle)) continue;
			if ( ds.contains(angle)) continue;
//			if (i<ed.firstIndexMax){//belong to Left Edge, enforce monotonity				
//				double[] angles = ds.toDoubleArray();
//				index = -Arrays.binarySearch(angles,angle)-1;
//				if (index>=0 && index<ds.size()){
//					double[] point = this.polar2Catesian.get(angles[index]);
//					if (point[1]>y) continue;
//				} else continue;
//			} else if (i>ed.lastIndexMax){//belong to Left Edge, enforce monotonity
//				
//				double[] angles = ds.toDoubleArray();
//				index = -Arrays.binarySearch(angles,angle);
//				if (index>=0 && index<ds.size()){
//					double[] point = this.polar2Catesian.get(angles[index]) ;
//					if (point[1]>y) continue;
//				} else continue;
//			}			
			this.polar2Catesian.put(angle, new double[]{Math.round(x*10000.0d)/10000.0d,Math.round(y*10000.0d)/10000.0d});
			this.angleDistMap.put(angle, dist);						
		}

							
		System.out.println(System.currentTimeMillis()-time);
		len = this.polar2Catesian.size();		
//		this.x.clear();
//		this.y.clear();
//		x = null;
//		y = null;
//		this.x = new DoubleArrayList(len);
//		this.y = new DoubleArrayList(len);
		x.ensureCapacity(len);
		y.ensureCapacity(len);		
		x.setSize(len);
		y.setSize(len);		
		xx = x.elements();
		yy = y.elements();		
				

		int i=0;
//		double prev=0;	
		maxY = -1;

		double x=0,y=0;
//		double prevy = 0;				
		double toLeft = toLeftEdge();
		double toRight = toRightEdge();		
		ds = angleDistMap.keySet();
		
		for (double angle:ds){
			double[] m = polar2Catesian.get(angle);							
			x = m[0];
			y = m[1];

//			this.x.add(m[0]);
//			this.y.add(m[1]);
			xx[i] = x;
			yy[i] = y;			

//			cp.addPoint(PointFactory.createPoint(x, y));
			double dist = angleDistMap.get(angle);
			if (maxDistance<dist)
				maxDistance = dist;

			if (maxY<y){
				maxY = y;
				firstIndexMax =i;
				pointx=x;
				pointy=y;
				angleToPoint=PI_2-angle;
				lastIndexMax =i;
			} else if (maxY==y){
				lastIndexMax =i;
			}		
//			if (i>=1 && angle>=PI_2 && prev<PI_2){				
//				distanceToTurn = (angle-PI_2<PI_2-prev)? 
//						y*Math.cos(angle-PI_2):prevy*Math.cos(PI_2-prev);
//			} else if (i==0 && angle>=PI_2)
//				distanceToTurn = y*Math.cos(angle-PI_2);
//			prev = angle;

			double r = x+toLeft;
			if (Math.abs(r)<=DELTA && leftStraight<y)
				leftStraight = y;

			r = x+toRight;
			if (Math.abs(r)<=DELTA && rightStraight<y)
				rightStraight = y;
			i++;
//			prevy=y;
		}//end of for		

		straightDist = (leftStraight>rightStraight) ? rightStraight : leftStraight;
	}
	
	public static Vector2D[] mainEdge(EdgeDetector edgeDetector,DoubleArrayList x,DoubleArrayList y,Double2ObjectSortedMap<Vector2D> lMap){
		if (edgeDetector==null) return null;
		int turn = edgeDetector.estimateTurn();
		double[] mainEdgeX = (turn==MyDriver.TURNRIGHT) ? edgeDetector.getLeftEdgeX() : reverse(edgeDetector.getRightEdgeX());
		double[] mainEdgeY = (turn==MyDriver.TURNRIGHT) ? edgeDetector.getLeftEdgeY() : reverse(edgeDetector.getRightEdgeY());
		
//		EdgeDetector.estimateEdge(mainEdgeX, mainEdgeY, x, y, lMap);		
//		return Vector2D.toVector2D(x.toDoubleArray(),y.toDoubleArray());
		return Vector2D.toVector2D(mainEdgeX,mainEdgeY);
	}
	
	public static Vector2D[] middleTrack(EdgeDetector edgeDetector,DoubleArrayList x,DoubleArrayList y,Double2ObjectSortedMap<Vector2D> lMap){
		if (edgeDetector==null)
			return null;
		int turn = edgeDetector.estimateTurn();

		DoubleArrayList xx=new DoubleArrayList();
		DoubleArrayList yy=new DoubleArrayList();
		DoubleArrayList nx = new DoubleArrayList();
		DoubleArrayList ny = new DoubleArrayList();
		
//		System.out.println("************Start**************");
//		EdgeDetector.drawEdge(edgeDetector.getEdges(),"E"+edgeDetector.distRaced+"a");
		if (turn==MyDriver.TURNRIGHT){
			EdgeDetector.estimateEdge(edgeDetector.getLeftEdgeX(), edgeDetector.getLeftEdgeY(), xx, yy, null);
			edgeDetector.createNewEdgeFromAscendingEdge(xx.toDoubleArray(), yy.toDoubleArray(), nx, ny, edgeDetector.trackWidth/2);			
		} else {			
			EdgeDetector.estimateEdge(edgeDetector.getRightEdgeX(), edgeDetector.getRightEdgeY(), xx, yy, null);
			edgeDetector.createNewEdgeFromDescendingEdge(xx.toDoubleArray(), yy.toDoubleArray(), nx, ny, -edgeDetector.trackWidth/2);
						
		}
		
//		edgeDetector.drawEdge(xx.toDoubleArray(),yy.toDoubleArray(),"E"+edgeDetector.distRaced+"a");
//		System.out.println(xx);
//		System.out.println(yy);
//		System.out.println(nx);
//		System.out.println(ny);
		EdgeDetector.estimateEdge(nx.toDoubleArray(), ny.toDoubleArray(), xx,yy, lMap);
		if (x!=null && y!=null) {
			x.addAll(nx);
			y.addAll(ny);
//			EdgeDetector.drawEdge(Vector2D.toVector2D(xx.toDoubleArray(),yy.toDoubleArray()), "E"+edgeDetector.distRaced+"c");
		} else {									
//			EdgeDetector.drawEdge(Vector2D.toVector2D(xx.toDoubleArray(),yy.toDoubleArray()), "E"+edgeDetector.distRaced+"c");
		}
		
//		try {
//			Thread.sleep(500);	
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		if (x==null || x.size()==0) return null;		
		return Vector2D.toVector2D(nx.toDoubleArray(), ny.toDoubleArray());

	}
	
	
	public static Vector2D[] estimateEdge(EdgeDetector ed,int type){
		if (ed==null) return null;
		DoubleArrayList xx = new DoubleArrayList();
		DoubleArrayList yy = new DoubleArrayList();
		if (type==-1){//Left Edge
			estimateEdge(ed.getLeftEdgeX(), ed.getLeftEdgeY(), xx, yy, null);
		} else estimateEdge(ed.getRightEdgeX(), ed.getRightEdgeY(), xx, yy, null);
		return Vector2D.toVector2D(xx.toDoubleArray(), yy.toDoubleArray());	
	}

	
	public void combine(EdgeDetector ed,double distRaced){
		if (trackWidth<=0) trackWidth = ed.trackWidth;
		if (distRaced<=ed.straightDist){			
			combine(ed, -toLeftEdge()+ed.toLeftEdge(), -distRaced);
			return;
		}
	}



	public int estimateTurn(){						
		boolean all=true;
		double toRight = toRightEdge();
		double[] x = this.x.elements();
		for (int i=firstIndexMax;i<=lastIndexMax;++i){
			double r = x[i]+toRight;
			if (!(Math.abs(r)<=DELTA || Math.abs(r-trackWidth)<=DELTA)){
				all = false;
			} else if (r>0){
				return MyDriver.TURNRIGHT;			
			} else if (r<-trackWidth)
				return MyDriver.TURNLEFT;
		}
		if (all)
//			if (maxDistance>=99)
				return MyDriver.STRAIGHT;
//			else return MyDriver.UNKNOWN;

		double sum=0;		
		for (int i=0;i<firstIndexMax;++i){
			sum += x[i];
		}
		double meanL = Math.round(sum/firstIndexMax*10000)/10000.0d;
		sum=0;
		int len = this.x.size();
		for (int i=lastIndexMax+1;i<len;++i){
			sum+=x[i];
		}
		double meanR = Math.round(sum/(len-1-lastIndexMax)*10000.0)/10000.0d;

		if (firstIndexMax<=0 || lastIndexMax>=len-1)
			return MyDriver.UNKNOWN;
		double rl = x[firstIndexMax-1];
		double rr = x[lastIndexMax+1];

		if (Math.abs(rl-meanL)<=DELTA && Math.abs(rr-meanR)<=DELTA){
			if (maxDistance>=99) {		
				return MyDriver.STRAIGHT;
			}
			return MyDriver.UNKNOWN;
		}
		if (meanR<=0 || rl<meanL-DELTA || meanR>rr+DELTA)
			return MyDriver.TURNLEFT;
		
		return MyDriver.TURNRIGHT;
	}


	public void constructEdge(){		
		if (leftCC != null || rightCC != null)
			return;
		
		double[] xx = x.elements();
		double[] yy = y.elements();
		int len = this.x.size();		
		leftCC = (firstIndexMax>=0 && firstIndexMax<len) ? createCCL(xx, yy, 0, firstIndexMax) : null;		
		rightCC = (lastIndexMax<len-1 && lastIndexMax>-1) ? createCCR(xx, yy, lastIndexMax+1, len-lastIndexMax-1) : null;
	}		


	public static void estimateEdge(double[] x,double[] y,DoubleArrayList ex,DoubleArrayList ey,Double2ObjectSortedMap<Vector2D> lMap){
		if (x==null || y==null || x.length!=y.length || x.length==0)
			return;
		estimateEdge(x, y, 0, x.length-1, ex, ey, lMap);
	}

	//start index to end index
	public static void estimateEdge(double[] x,double[] y,int start,int end,DoubleArrayList ex,DoubleArrayList ey,Double2ObjectSortedMap<Vector2D> lMap){		
		if (x==null || y==null || x.length<=end || y.length<=end ||start<0 || start>end)
			return;
		ControlPath cp = new ControlPath();
		for (int i=start;i<=end;++i)
			cp.addPoint(PointFactory.createPoint(x[i], y[i]));

		int len = end-start+1;
		Curve c = new NaturalCubicSpline(cp,new GroupIterator(0+":"+(len-1),len));
		ShapeMultiPath mp = new ShapeMultiPath();

		c.appendTo(mp);

		int n = mp.getNumPoints();		
		double length = 0;		
		double prevx = Double.NEGATIVE_INFINITY;
		double prevy = Double.NEGATIVE_INFINITY;
		
		if (lMap==null && (ex==null || ey==null)) return;
		for (int i=0;i<n;++i){
			double[] p = mp.get(i);
			double xx = p[0];
			double yy = p[1];
			if (Math.abs(prevx-xx)<DELTA && Math.abs(prevy-yy)<DELTA)//nearly the same point
				continue;
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
						
			double dx = (prevx==Double.NEGATIVE_INFINITY) ? 0 :xx-prevx;
			double dy = (prevy==Double.NEGATIVE_INFINITY) ? 0 :yy-prevy;			
			
			prevx = xx;
			prevy = yy;
			xx = Math.round(xx*10000.0d)/10000.0d;
			yy = Math.round(yy*10000.0d)/10000.0d;
			if (ex!=null) ex.add(xx);
			if (ey!=null) ey.add(yy);

			length += Math.sqrt(dx*dx+dy*dy);
			if (lMap!=null) lMap.put(length, new Vector2D(xx,yy));
		}				
	}
	
	public static MySpline estimateEdge(double[] x,double[] y){
		return estimateEdge(x, y,0,x.length-1);
	}
	
	public static MySpline estimateEdge(double[] x,double[] y,int start,int end){		
		if (x==null || y==null || x.length<=end || y.length<=end ||start<0 || start>end)
			return null;
		ControlPath cp = new ControlPath();
		for (int i=start;i<=end;++i)
			cp.addPoint(PointFactory.createPoint(x[i], y[i]));

		int len = end-start+1;
		MySpline c = new MySpline(cp,new GroupIterator(0+":"+(len-1),len));
		ShapeMultiPath mp = new ShapeMultiPath();

		c.appendTo(mp);
		return c;
	}



	private int estimateEdge(double[] x,double[] y,int start,int len,DoubleArrayList ex,DoubleArrayList ey,Double2ObjectSortedMap<Vector2D> lMap,double[] range){		
//		ControlPath cp = new ControlPath();
//		int len = x.length;
//		for (int i=0;i<len;++i)
//			cp.addPoint(PointFactory.createPoint(x[i], y[i]));
//
//		
//		Curve c = new NaturalCubicSpline(cp,new GroupIterator(0+":"+(len-1),len));
//		ShapeMultiPath mp = new ShapeMultiPath();
//
//		c.appendTo(mp);
//
//		int n = mp.getNumPoints();
		if (lMap==null && (ex==null || ey==null)) return 0;
		int n = len+start;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
//		double defaultReturnValue = angleDistMap.defaultReturnValue();//		
		double prevx = Double.NEGATIVE_INFINITY;
		double prevy = Double.NEGATIVE_INFINITY;
		double length = 0;
		
		
		for (int i=start;i<n;++i){			
			double xx = x[i];
			double yy = y[i];
			if (Math.abs(prevx-xx)<DELTA && Math.abs(prevy-yy)<DELTA)//nearly the same point
				continue;
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
			
			double dx = (prevx==Double.NEGATIVE_INFINITY) ? 0 :xx-prevx;
			double dy = (prevy==Double.NEGATIVE_INFINITY) ? 0 :yy-prevy;
			prevx = xx;
			prevy = yy;
//			double dist = Math.sqrt(xx*xx+yy*yy);						
//			alpha=Math.round(alpha*ANGLEACCURACY)/ANGLEACCURACY;

			if (alpha<min)
				min = alpha;
			if (alpha>max)
				max = alpha;
			
			xx = Math.round(xx*10000.0d)/10000.0d;
			yy = Math.round(yy*10000.0d)/10000.0d;
			if (ex!=null) ex.add(xx);
			if (ey!=null) ey.add(yy);

			length += Math.sqrt(dx*dx+dy*dy);
			if (lMap!=null) lMap.put(length, new Vector2D(xx,yy));						

//			double val = angleDistMap.get(alpha);
//			if (val ==defaultReturnValue || val>dist+DELTA){
//				angleDistMap.put(alpha, dist);
//				polar2Catesian.put(alpha, new double[]{xx,yy});
//			}				
		}				
//		System.out.println(polar2Catesian.keySet());
		range[0]=min;
		range[1]=max;

		return len;
	}
	
	private CurveCoordinate createCCL(double[] x,double[] y,int start,int len){		
		if (x==null || y==null) return null;
		int n = len+start;
		double prevx = x[start];
		double prevy = y[start];
		double length = 0;
		Vector2D[] allPoints = new Vector2D[len];
		double[] allLengths = new double[len];
		
		int j=0;
		for (int i=start;i<n;++i){			
			double xx = x[i];
			double yy = y[i];
			double dx = xx-prevx;
			double dy = yy-prevy;
			prevx = xx;
			prevy = yy;
			
			xx = Math.round(xx*10000.0d)/10000.0d;
			yy = Math.round(yy*10000.0d)/10000.0d;

			length += Math.sqrt(dx*dx+dy*dy);
			allPoints[j] = new Vector2D(xx,yy);
			allLengths[j++] = length;									
		}				
		
		return new CurveCoordinate(allPoints,allLengths);
	}
	
	private CurveCoordinate createCCR(double[] x,double[] y,int start,int len){		
		if (x==null || y==null) return null;
		int n = len+start-1;
		double prevx = x[n];
		double prevy = y[n];
		double length = 0;
		Vector2D[] allPoints = new Vector2D[len];
		double[] allLengths = new double[len];
		
		int j=0;
		for (int i=n;i>=start;--i){			
			double xx = x[i];
			double yy = y[i];
			double dx = xx-prevx;
			double dy = yy-prevy;
			prevx = xx;
			prevy = yy;
			
			xx = Math.round(xx*10000.0d)/10000.0d;
			yy = Math.round(yy*10000.0d)/10000.0d;

			length += Math.sqrt(dx*dx+dy*dy);
			allPoints[j] = new Vector2D(xx,yy);
			allLengths[j++] = length;									
		}				
		
		return new CurveCoordinate(allPoints,allLengths);
	}



	public double slope(int index,double[] ex,double[] ey){
		double deltaY = (index==0) ? ey[1]-ey[0] : ey[index]-ey[index-1];
		double deltaX = (index==0) ? ex[1]-ex[0] : ex[index]-ex[index-1];
		return (deltaX==0)?(deltaY>0)?Double.POSITIVE_INFINITY:Double.NEGATIVE_INFINITY:deltaY/deltaX;
	}

	public static void createNewEdgeFromAscendingEdge(double[] ex,double[] ey,DoubleArrayList x,DoubleArrayList y,double dist){//- dist : Left,Up; +dist : Right,Down
		if (ex==null || ey==null || ex.length!=ey.length || x==null || y==null ||ex.length<1)
			return;
		int len = ex.length;
		x.ensureCapacity(len);
		y.ensureCapacity(len);
		double prevx=ex[0]+dist;
		double prevy=ey[0];
		x.add(prevx);
		y.add(prevy);
		double ly = ey[0];
		double lx = ex[0];

		for (int i=1;i<len;++i){
			double x0 = ex[i];
			double y0 = ey[i];
			double deltaY = y0-ly;
			double deltaX = x0-lx;
			if (deltaY<0) continue;

			Vector2D tangent = new Vector2D(deltaX,deltaY).normalized();		
			Vector2D N = tangent.orthogonal();
			Vector2D rs = new Vector2D(x0,y0).plus(N.times(-dist));
			boolean found = false;
			if (prevx <rs.x+DELTA && prevy<rs.y+DELTA){
				
				prevx = rs.x;
				prevy = rs.y;
				x.add(prevx);
				y.add(prevy);
				lx = x0;
				ly = y0;				
				found =true;
			}

			if (i<len-1){
				double dy = ey[i+1]-y0;
				double dx = ex[i+1]-x0;

				while (dx*deltaX+dy*deltaY<0 && i<len-2){										
					i++;					
					dy = ey[i+1]-y0;
					dx = ex[i+1]-x0;
				}//angle between two vector are >pi/2 then ignore the next point

				if (!found){
					tangent = new Vector2D(dx,dy).normalized();		
					N = tangent.orthogonal();
					rs = new Vector2D(x0,y0).plus(N.times(-dist));
					if (prevx <rs.x+DELTA && prevy<rs.y+DELTA){
						prevx = rs.x;
						prevy = rs.y;
						x.add(prevx);
						y.add(prevy);
						lx = ex[i];
						ly = ey[i];
					}
				}

			}

		}//end of for		


	}

	public static void createNewEdgeFromDescendingEdge(double[] ex,double[] ey,DoubleArrayList x,DoubleArrayList y,double dist){//- dist : Left,Up; +dist : Right,Down
		if (ex==null || ey==null || ex.length!=ey.length || x==null || y==null)
			return;
		int len = ex.length;
		x.ensureCapacity(len);
		y.ensureCapacity(len);
		if (len<1) return;
		double prevx=ex[len-1]+dist;
		double prevy=ey[len-1];
		x.add(prevx);
		y.add(prevy);
		double ly = ey[len-1];
		double lx = ex[len-1];

		for (int i=len-2;i>=0;--i){			
			double x0 = ex[i];
			double y0 = ey[i];
			double deltaY = y0-ly;
			double deltaX = x0-lx;
			if (deltaY<0) continue;
			Vector2D tangent = new Vector2D(deltaX,deltaY).normalized();		
			Vector2D N = tangent.orthogonal();
			Vector2D rs = new Vector2D(x0,y0).plus(N.times(-dist));
			boolean found = false;
			if (prevx >rs.x-DELTA && prevy < rs.y+DELTA){				
				found = true;
				prevx = rs.x;
				prevy = rs.y;
				x.add(prevx);
				y.add(prevy);		
				lx = ex[i];
				ly = ey[i];
			} 


			if (i>0){
				double dy = ey[i-1]-y0;
				double dx = ex[i-1]-x0;

				while (dx*deltaX+dy*deltaY<0 && i>1){										
					i--;					
					dy = ey[i-1]-y0;
					dx = ex[i-1]-x0;
				}//angle between two vector are >pi/2 then ignore the next point

				if (!found){
					tangent = new Vector2D(dx,dy).normalized();		
					N = tangent.orthogonal();
					rs = new Vector2D(x0,y0).plus(N.times(-dist));
					if (prevx >rs.x-DELTA && prevy < rs.y+DELTA){
						prevx = rs.x;
						prevy = rs.y;
						x.add(prevx);
						y.add(prevy);
						lx = ex[i];
						ly = ey[i];
					}
				}

			}

		}//end of for		

	}
	
	public static Vector2D getHighestPointLeft(int turn,double[] leftX,double[] leftY,double[] rightX,double[] rightY,double trackWidth){
		if (leftX.length<=0 && rightX.length<=0) return null;
		int n=leftX.length-1;
		double highestl = (n>=0) ? leftY[n] : -1;
		double highestr = (rightX.length>0) ? rightY[0] : -1;
		if (turn==MyDriver.STRAIGHT){						
			if (n>=0) return new Vector2D(leftX[0],Math.max(highestl, highestr));
		}
		
		if (highestl>=highestr)
			return (n<0) ? null : new Vector2D(leftX[n],leftY[n]);
		DoubleArrayList x = new DoubleArrayList(leftX);
		DoubleArrayList y = new DoubleArrayList(leftY);
		EdgeDetector.createNewEdgeFromDescendingEdge(rightX, rightY, x, y, -trackWidth);
		n = x.size()-1;
		if (n<0) return null;
		Double2ObjectSortedMap<Vector2D> polar2Catesian = new Double2ObjectRBTreeMap<Vector2D>();
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=0;i<=n;++i){			
			double xx = x.getDouble(i);
			double yy = y.getDouble(i);
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
			double dist = Math.sqrt(xx*xx+yy*yy);
			alpha=Math.round(alpha*EdgeDetector.ANGLEACCURACY)/EdgeDetector.ANGLEACCURACY;

			if (alpha<min)
				min = alpha;
			if (alpha>max)
				max = alpha;

			
			if (polar2Catesian.containsKey(alpha)){
				Vector2D p = polar2Catesian.get(alpha);
				if (p.length()>dist)
					polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));
			} else polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));				
		}				

		return polar2Catesian.get(max);
		
	}
	
	public static Vector2D getHighestPointLeft(int turn,DoubleList leftX,DoubleList leftY,DoubleList rightX,DoubleList rightY,double trackWidth){
		if (leftX.size()<=0 && rightX.size()<=0) return null;
		int n=leftX.size()-1;
		double highestl = (n>=0) ? leftY.getDouble(n) : -1;
		double highestr = (rightX.size()>0) ? rightY.getDouble(0) : -1;
		if (turn==MyDriver.STRAIGHT){								
			if (n>=0) return new Vector2D(leftX.getDouble(0),Math.max(highestl, highestr));			
		}
		
		if (highestl>=highestr)			
			return (n<0) ? null : new Vector2D(leftX.getDouble(n),leftY.getDouble(n));
		
		DoubleArrayList x = new DoubleArrayList(leftX);
		DoubleArrayList y = new DoubleArrayList(leftY);
		EdgeDetector.createNewEdgeFromDescendingEdge(rightX.toDoubleArray(), rightY.toDoubleArray(), x, y, -trackWidth);
		n = x.size()-1;
		if (n<0) return null;
		Double2ObjectSortedMap<Vector2D> polar2Catesian = new Double2ObjectRBTreeMap<Vector2D>();
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=0;i<=n;++i){			
			double xx = x.getDouble(i);
			double yy = y.getDouble(i);
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
			double dist = Math.sqrt(xx*xx+yy*yy);
			alpha=Math.round(alpha*EdgeDetector.ANGLEACCURACY)/EdgeDetector.ANGLEACCURACY;

			if (alpha<min)
				min = alpha;
			if (alpha>max)
				max = alpha;

			
			if (polar2Catesian.containsKey(alpha)){
				Vector2D p = polar2Catesian.get(alpha);
				if (p.length()>dist)
					polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));
			} else polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));				
		}				
		
		return polar2Catesian.get(max);
		
	}

	
	public static Vector2D getHighestPointRight(int turn,double[] leftX,double[] leftY,double[] rightX,double[] rightY,double trackWidth){
		if (leftX.length<=0 && rightX.length<=0) return null;
		int n=leftX.length-1;
		double highestl = (n>=0) ? leftY[n] : -1;
		double highestr = (rightX.length>0) ? rightY[0] : -1;
		if (turn==MyDriver.STRAIGHT){			
			if (rightX.length>0) return  new Vector2D(rightX[0],Math.max(highestl, highestr));
		}
		
		if (highestr>=highestl)
			return (rightX.length<=0) ? null : new Vector2D(rightX[0],rightY[0]);
		DoubleArrayList x = new DoubleArrayList(rightX);
		DoubleArrayList y = new DoubleArrayList(rightY);
		EdgeDetector.createNewEdgeFromAscendingEdge(leftX, leftY, x, y, trackWidth);		
		n = x.size()-1;
		if (n<0) return null;
		Double2ObjectSortedMap<Vector2D> polar2Catesian = new Double2ObjectRBTreeMap<Vector2D>();
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=0;i<=n;++i){			
			double xx = x.getDouble(i);
			double yy = y.getDouble(i);
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
			double dist = Math.sqrt(xx*xx+yy*yy);
			alpha=Math.round(alpha*EdgeDetector.ANGLEACCURACY)/EdgeDetector.ANGLEACCURACY;

			if (alpha<min)
				min = alpha;
			if (alpha>max)
				max = alpha;

			
			if (polar2Catesian.containsKey(alpha)){
				Vector2D p = polar2Catesian.get(alpha);
				if (p.length()>dist)
					polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));
			} else polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));				
		}
				
		return polar2Catesian.get(min);
		
	}

	public static Vector2D getHighestPointRight(int turn,DoubleList leftX,DoubleList leftY,DoubleList rightX,DoubleList rightY,double trackWidth){
		if (leftX.size()<=0 && rightX.size()<=0) return null;
		int n=leftX.size()-1;
		double highestl = (n>=0) ? leftY.getDouble(n) : -1;
		double highestr = (rightX.size()>0) ? rightY.getDouble(0) : -1;		
		if (turn==MyDriver.STRAIGHT){			
			if (rightX.size()>0) return  new Vector2D(rightX.getDouble(0),Math.max(highestl, highestr));
		}
		
		if (highestr>=highestl)
			return (rightX.size()<=0) ? null : new Vector2D(rightX.getDouble(0),rightY.getDouble(0));
		DoubleArrayList x = new DoubleArrayList(rightX);
		DoubleArrayList y = new DoubleArrayList(rightY);
		EdgeDetector.createNewEdgeFromAscendingEdge(leftX.toDoubleArray(), leftY.toDoubleArray(), x, y, trackWidth);		
		n = x.size()-1;
		if (n<0) return null;
		Double2ObjectSortedMap<Vector2D> polar2Catesian = new Double2ObjectRBTreeMap<Vector2D>();
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=0;i<=n;++i){			
			double xx = x.getDouble(i);
			double yy = y.getDouble(i);
			if (yy<=0) continue;
			double alpha = Math.PI-Math.atan2(yy,xx);
			if (alpha>Math.PI || alpha<0) continue;
			double dist = Math.sqrt(xx*xx+yy*yy);
			alpha=Math.round(alpha*EdgeDetector.ANGLEACCURACY)/EdgeDetector.ANGLEACCURACY;

			if (alpha<min)
				min = alpha;
			if (alpha>max)
				max = alpha;

			
			if (polar2Catesian.containsKey(alpha)){
				Vector2D p = polar2Catesian.get(alpha);
				if (p.length()>dist)
					polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));
			} else polar2Catesian.put(alpha, new Vector2D(Math.round(xx*10000.0d)/10000.0d,Math.round(yy*10000.0d)/10000.0d));				
		}
				
		return polar2Catesian.get(min);
		
	}


	public double toMiddle(){
		if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return Math.round(trackWidth/2*curPos*10000.0d)/10000.0d;
	}

	public double toLeftEdge(){
		if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return Math.round(trackWidth/2*(1+curPos)*10000.0d)/10000.0d;
	}

	public double toRightEdge(){
		if (trackWidth<=0 || curPos<-1 || curPos>1) return Double.NaN;
		return -Math.round(trackWidth/2*(1-curPos)*10000.0d)/10000.0d;
	}

	/**
	 * @return the trackWidth
	 */
	public double getTrackWidth() {
		return trackWidth;
	}

	/**
	 * @param trackWidth the trackWidth to set
	 */
	public void setTrackWidth(double trackWidth) {
		this.trackWidth = trackWidth;
	}

	/**
	 * @return the x
	 */
	public double[] getX() {
		return x.copy().elements();
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double[] x) {	
		this.x.elements(x);
	}

	/**
	 * @return the y
	 */
	public double[] getY() {
		return y.copy().elements();
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double[] y) {		
		this.y.elements(y);
	}

	/**
	 * @return the firstIndexMax
	 */
	public int getFirstIndexMax() {
		return firstIndexMax;
	}

	/**
	 * @param firstIndexMax the firstIndexMax to set
	 */
	public void setFirstIndexMax(int firstIndexMax) {
		this.firstIndexMax = firstIndexMax;
	}

	/**
	 * @return the maxDistance
	 */
	public double getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @param maxDistance the maxDistance to set
	 */
	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	double SimSteerUpdate(double oldSteer,double steer){		
		/* input control */		
		steer *= SimpleDriver.steerLock;
		double stdelta = steer - oldSteer;
		double sign = (stdelta<0) ?-1:1;

		if ( Math.abs(stdelta/DELTATIME) > MAXSTEERSPEED ){
			steer = sign * MAXSTEERSPEED * DELTATIME + oldSteer;
		};


		return steer;		
	}


	public void drawEdge(double[] x,double[] y,final String title){			
		XYSeries series = new XYSeries("Curve");

//		for (int i=0;i<numPointLeft;++i){
//		series.add(leftEgdeX[i], leftEgdeY[i]);
//		}

		for (int i=0;i<x.length;++i){			
			series.add(x[i],y[i]);
		}

//		for (int i=0;i<numPointRight;++i){
//		series.add(rightEgdeX[i], rightEgdeY[i]);
//		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-20.0,90.0);
		chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();

//		ChartPanel chartPanel = new ChartPanel(chart);
//		jf.setContentPane(chartPanel);
//		jf.setPreferredSize(new Dimension(600,400));
//		jf.setMinimumSize(new Dimension(600,400));
//		jf.setVisible(true);

	}
	
	
	public static void drawEdge(ObjectList<Vector2D> vs,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (Vector2D v:vs){			 
			series.add(v.x,v.y);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-20.0,90.0);
		chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();
	}
	
	public static void drawEdge(Vector2D[] vs,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (int i=0;i<vs.length;++i){			
			series.add(vs[i].x,vs[i].y);
		}

		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-20.0,90.0);
		chart.getXYPlot().getRangeAxis().setRange(-20.0,100.0);

		Thread p = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					BufferedImage image = chart.createBufferedImage(600, 400);
					ImageIO.write(image, "png", new File(title+".png"));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});

		p.start();
	}



	/* compute the radius given three points */
	double radius(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		double dx1 = x2 - x1;
		double dy1 = y2 - y1;
		double dx2 = x3 - x2;
		double dy2 = y3 - y2;

		//double z = (dy1*dx2 - dx1*dy2);
		double z = dx1*dy2 - dy1*dx2;
		double sign = (z<0)?-1:1;

		if (z != 0.0) {
			double k = (dx2*(x3-x1) - dy2*(y1-y3))/z;
			return sign*Math.sqrt((1.0+k*k)*(dx1*dx1+dy1*dy1))/2.0;
		} else {
			return Double.MAX_VALUE;
		}
	}



//	double estimateDistance(double angle){
//		if (angle>leftRange[1] && angle<rightRange[0])
//			return 100;
//
//		if (allAngles==null) return 0.0d;
//
//		int len = allAngles.length;
//		int insertPoint = java.util.Arrays.binarySearch(allAngles,angle);
//		if (insertPoint>=0) return allDistances[insertPoint];
//
//		insertPoint = -insertPoint-1;		
//		if (insertPoint==0) 
//			return -1;
//
//		if (insertPoint>=len)
//			return -1;
//
//		double a = allAngles[insertPoint]; 
//		double b = allAngles[insertPoint-1];
//		return (a+b>angle+angle) ? allDistances[insertPoint-1] : allDistances[insertPoint];
//	}

	/**
	 * @return the distanceToTurn
	 */
	public double getDistanceToTurn() {
		return distanceToTurn;
	}

	/**
	 * @param distanceToTurn the distanceToTurn to set
	 */
	public void setDistanceToTurn(double distanceToTurn) {
		this.distanceToTurn = distanceToTurn;
	}
	
	public Vector2D[] getEdges(){
		return Vector2D.toVector2D(x.elements(),y.elements());
	}

	public Vector2D[] getLeftEdge(){
		return Vector2D.toVector2D(getLeftEdgeX(), getLeftEdgeY());
	}
	
	public Vector2D[] getRightEdge(){
		return Vector2D.toVector2D(getRightEdgeX(), getRightEdgeY());
	}

	public double[] getLeftEdgeX(){
		return (leftEdgeX==null) ? x.partFromTo(0, firstIndexMax-1).elements() : leftEdgeX.toDoubleArray();
	}

	public double[] getLeftEdgeY(){
		return (leftEdgeY==null) ? y.partFromTo(0, firstIndexMax-1).elements() : leftEdgeY.toDoubleArray();
	}

	public double[] getRightEdgeX(){
		int len = x.size();		
		return (rightEdgeX==null) ? x.partFromTo(lastIndexMax+1, len-1).elements() : rightEdgeX.toDoubleArray();
	}

	public double[] getRightEdgeY(){
		int len = y.size();		
		return (rightEdgeY==null) ? x.partFromTo(lastIndexMax+1, len-1).elements() : rightEdgeY.toDoubleArray();
	}
	
	public static double[] reverse(double[] ar){
		if (ar==null) return null;
		int len = ar.length/2;
		int l = ar.length-1;
		for (int i=0;i<len;++i){
			double tmp = ar[i];
			ar[i] = ar[l-i];
			ar[l-i] = tmp;
		}
		return ar;
	}
	
	public static DoubleArrayList reverse(DoubleArrayList ar){
		if (ar==null) return null;
		DoubleArrayList rs = new DoubleArrayList();
		int len = ar.size();
		int l = len-1;
		for (int i=0;i<len;++i){
			rs.add(ar.getDouble(l-i));
		}
		
		return rs;
	}


}
