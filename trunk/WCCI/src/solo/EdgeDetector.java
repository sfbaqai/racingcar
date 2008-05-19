/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.graphbuilder.curve.ControlPath;

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
	double curPos=0;	
	double distRaced;	

	public final static int NUM_POINTS=150;
	double curAngle;
	double maxY;

	Edge left;
	Edge right;
	DoubleArrayList x = null;
	DoubleArrayList y = null;
	int numpoint = 0;
	int firstIndexMax = -1,lastIndexMax = -1;
	
	double maxDistance=-1;
	public double leftStraight = -1;
	public double rightStraight = -1;
	public double straightDist =-1;
	Vector2D highestPoint = null;
	Double2ObjectSortedMap<Vector2D> polar2Cartesian = null;
	int turn;

	/**
	 * Copy Constructor
	 *
	 * @param edgeDetector a <code>EdgeDetector</code> object
	 */
	public EdgeDetector(EdgeDetector edgeDetector) 
	{
	    this.cp = edgeDetector.cp;
	    this.trackWidth = edgeDetector.trackWidth;
	    this.curPos = edgeDetector.curPos;
	    this.distRaced = edgeDetector.distRaced;
	    this.curAngle = edgeDetector.curAngle;
	    this.maxY = edgeDetector.maxY;
	    this.left = edgeDetector.left;
	    this.right = edgeDetector.right;
	    this.firstIndexMax = edgeDetector.firstIndexMax;
	    this.lastIndexMax = edgeDetector.lastIndexMax;
	    this.maxDistance = edgeDetector.maxDistance;
	    this.leftStraight = edgeDetector.leftStraight;
	    this.rightStraight = edgeDetector.rightStraight;
	    this.straightDist = edgeDetector.straightDist;
	    this.highestPoint = edgeDetector.highestPoint;
	    this.x = edgeDetector.x;
	    this.y = edgeDetector.y;
	    this.numpoint = edgeDetector.numpoint;
	    this.turn = edgeDetector.turn;
	    this.polar2Cartesian = edgeDetector.polar2Cartesian;
	}


	public EdgeDetector() {		
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
		distRaced = cs.distRaced;			

		if (Math.abs(cs.angle)<0.001)
			trackWidth = Math.round((tracks[0]+tracks[18])*Math.cos(cs.angle));
		
		double[] x = new double[NUM_POINTS];//x is axis along track axis
		double[] y = new double[NUM_POINTS];
		double[] rx = new double[NUM_POINTS];//x is axis along track axis
		double[] ry = new double[NUM_POINTS];

		leftStraight=-1;
		rightStraight=-1;	
		numpoint = 19;
	
		for (int i=0;i<19;++i){
			double  angle = Math.PI-SimpleDriver.ANGLE_LK[i]-cs.angle;						
			angle=Math.round(angle*ANGLEACCURACY)/ANGLEACCURACY;			
			double xx =Math.round(tracks[i]* Math.cos(angle)*10000.0d)/10000.0d;
			double yy =Math.round(tracks[i]* Math.sin(angle)*10000.0d)/10000.0d;
			polar2Cartesian.put(angle, new Vector2D(xx,yy));
			x[i] = xx;
			y[i] = yy;
			rx[18-i] = xx;
			ry[18-i] = yy;

			if (maxDistance<tracks[i])
				maxDistance = tracks[i];

			if (maxY<yy){
				maxY = yy;
				firstIndexMax =i;
				lastIndexMax =i;				
			} else if (maxY==yy){
				lastIndexMax =i;
			}
		}
		this.x = DoubleArrayList.wrap(x, 19);
		this.y = DoubleArrayList.wrap(y, 19);
		highestPoint = new Vector2D(x[firstIndexMax],y[firstIndexMax]);
		left = (firstIndexMax>0) ? new Edge(x,y,firstIndexMax) : null;
		right = (lastIndexMax<18) ? new Edge(rx,ry,18-lastIndexMax) : null;
		int turnL = left.turn();
		int turnR = right.turn();
		double d = turnL * turnR; 
		if (d > 0){
			turn = turnL;
		} else if (d == 0){
			turn = (turnL==0) ? turnR : turnL;
		} else turn = MyDriver.UNKNOWN;
		
		if (turn==MyDriver.STRAIGHT && highestPoint.length()<99)
			turn = MyDriver.UNKNOWN;
		
		straightDist = (left.straightDist>right.straightDist) ? right.straightDist : left.straightDist;
	}

	
	public void combine(EdgeDetector ed,double distRaced){
		if (distRaced>ed.straightDist) return;		
		if (trackWidth<=0) {
			trackWidth = ed.trackWidth;
		}
		double ax = -toLeftEdge()+ed.toLeftEdge();
				 		
		long time = System.currentTimeMillis();
		int len=ed.x.size();
				
		double oldTrackWidth = ed.trackWidth;
		double scale = this.trackWidth/oldTrackWidth;		
		double[] xx = ed.x.elements();
		double[] yy = ed.y.elements();		
		DoubleSortedSet ds = this.polar2Cartesian.keySet();
		
		
		for (int i=0;i<len;++i){			
			double x = xx[i]*scale;
			double y = yy[i];
			if (Math.sqrt(x*x+y*y)>=99) continue;
			x += ax;
			y -= distRaced;
			if (y<0 || y<straightDist) continue;						
			double  angle = Math.PI-Math.atan2(y,x);
			if (angle<0 || angle>Math.PI) continue;
			angle=Math.round(angle*ANGLEACCURACY)/ANGLEACCURACY;
			if ( ds.contains(angle)) continue;
			this.polar2Cartesian.put(angle, new Vector2D(x,y));					
		}
				

		leftStraight=-1;
		rightStraight=-1;	
		numpoint = polar2Cartesian.size();
		
		int sz = (numpoint<NUM_POINTS) ? NUM_POINTS : numpoint*2;
		x.ensureCapacity(sz);
		y.ensureCapacity(sz);
		double[] rx = new double[sz];
		double[] ry = new double[sz];
		x.size(numpoint);
		y.size(numpoint);
		xx = x.elements();
		yy = y.elements();				
		
		left = null;
		right = null;
	
		int i = 0;
		
		for (double angle:ds){
			Vector2D v = polar2Cartesian.get(angle);		
			xx[i] = v.x;
			yy[i] = v.y;
			rx[18-i] = v.x;
			ry[18-i] = v.y;
			
			double dist = v.length();

			if (maxDistance<dist)
				maxDistance = dist;

			if (maxY<v.y){
				maxY = v.y;
				firstIndexMax =i;
				lastIndexMax =i;				
			} else if (maxY==v.y){
				lastIndexMax =i;
			}
		}		
		highestPoint = new Vector2D(xx[firstIndexMax],yy[firstIndexMax]);
		left = (firstIndexMax>0) ? new Edge(xx,yy,firstIndexMax) : null;
		right = (lastIndexMax<18) ? new Edge(rx,ry,18-lastIndexMax) : null;
		int turnL = left.turn();
		int turnR = right.turn();
		double d = turnL * turnR; 
		if (d > 0){
			turn = turnL;
		} else if (d == 0){
			turn = (turnL==0) ? turnR : turnL;
		} else turn = MyDriver.UNKNOWN;
		
		if (turn==MyDriver.STRAIGHT && highestPoint.length()<99)
			turn = MyDriver.UNKNOWN;
		
		straightDist = (left.straightDist>right.straightDist) ? right.straightDist : left.straightDist;


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
		return x.elements();
	}

	
	/**
	 * @return the y
	 */
	public double[] getY() {
		return y.elements();
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

		
	public Vector2D[] getEdges(){
		return Vector2D.toVector2D(x.elements(),y.elements());
	}

	public Edge getLeftEdge(){
		return left;
	}
	
	public Edge getRightEdge(){
		return right;
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
