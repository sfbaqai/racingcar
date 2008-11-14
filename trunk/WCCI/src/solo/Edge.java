/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import cern.colt.list.DoubleArrayList;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class Edge {
	public final static double ANGLEACCURACY =100.0d;
	public final static int NUM_POINTS=150;
	public final static double DELTA = 0.001;
	ObjectArrayList<Vector2D> allPoints = null;
	DoubleArrayList x = null;
	DoubleArrayList y = null;
	DoubleArrayList allLengths = null;	
	Object2IntMap<Vector2D> p2l = null;
	double straightDist = 0;
	double dmin = Double.MAX_VALUE;
	int size =0;	
	double totalLength =0;
	Vector2D center = null;
	double radius = Double.MAX_VALUE;
	int index = 0;

	public Edge(){
	}

	public double calculateRadius(){
//		int index = y.binarySearch(straightDist);
//		if (index<0) index = -index;
		if (index<size && index>=0) return calculateRadius(index);
		return -1;
	}
	
	public boolean isStraightLine(Vector2D p1,Vector2D p2,Vector2D p3){
		double[] r = new double[3];
		boolean isCirle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
		if (!isCirle || r[2]>=100000) return true;
		return false;
	}
	
	public boolean isPointOnEdge(Vector2D hh){
		double d = 0;
		if (dmin==Double.MAX_VALUE){
			d = calculateRadius();
			if (d==-1) return false;
		} else d = dmin;
		Vector2D p1 = allPoints.get(size-1);
		Vector2D p2 = allPoints.get(size-2);
		Vector2D p3 = allPoints.get(size-3);
		if (d<=0.0001){//exactly correct guess
			double dd = (radius!=Double.MAX_VALUE) ? center.distance(hh)-radius : Geom.ptLineDistSq(p1.x, p1.y, p2.x, p2.y, hh.x, hh.y, null);
			if (radius!=Double.MAX_VALUE) dd*=dd;
			if (dd<=0.01) return true;
		}
	
		if (isStraightLine(p1, p2, hh))	return true;
		double[] r = new double[3];
		boolean isCircle = Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r);
		if (isCircle){
			double dd = hh.distance(new Vector2D(r[0],r[1]))-Math.sqrt(r[2]);
			if (dd>=-1) return true;			
		}
		
		return false;//false means UNKNOWN
	}
	
	public double radiusNextSeg(double[] r){
		if (size<3) return -1;
		if (dmin<0.0001 && center!=null){
			r[0] = center.x;
			r[1] = center.y;
			r[2] = radius*radius;
			return radius;
		}
		Vector2D p1 = allPoints.get(size-1);
		Vector2D p2 = allPoints.get(size-2);
		Vector2D p3 = allPoints.get(size-3);				
		
		if (Geom.getCircle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, r)){
			double d = Math.sqrt(r[2]); 
			return (d<10) ? radius : d;			
		}
		return Double.MAX_VALUE;				
	}

	public double calculateRadius(int index){
//		Vector2D pp = allPoints.get(index+1); 
//		double x0 = pp.x;
//		double y0 = pp.y;
		if (straightDist<allPoints.get(size-1).y && size>index+3){					
			Vector2D point = allPoints.get(size-1);
			if (dmin<0.0001 && center!=null){
				double d = center.distance(point)-radius;
				if (d*d<0.001){
					dmin+=d*d;
					return dmin;
				}
			}
			Vector2D highestPoint = allPoints.get(index+1);
			double[] r = new double[3];				
			Vector2D vmin = null;
			double rmin = -1;
			for (int i =index+2;i<size;++i){
				if (i==size-1) continue;
				Vector2D startTurn = allPoints.get(i);				
				boolean isCircle = Geom.getCircle(startTurn.x, startTurn.y, point.x, point.y, highestPoint.x, highestPoint.y, r);
				if (isCircle){
					double rr = Math.sqrt(r[2]);
					double x = r[0];
					double y = r[1];
					if (y>=highestPoint.y) continue;

					double s = 0;
					for (int j=index+2;j<size;++j){
						if (j==i || j==size-1) continue;
						Vector2D p = allPoints.get(j);
						double dx = p.x-x;
						double dy = p.y-y;
						double d = Math.sqrt(dx*dx+dy*dy)-rr;
						s += d*d;
					}
					if (dmin>s){
						dmin = s;
						vmin = new Vector2D(x,y);
						rmin = rr;
					}

					if (dmin<=0.0001)
						break;
				} else {//3 points in a line
					double s = 0;
					for (int j=index+2;j<size;++j){
						if (j==i || j==size-1) continue;
						Vector2D p = allPoints.get(j);
						double d = Geom.ptLineDistSq(startTurn.x, startTurn.y, point.x, point.y, p.x, p.y, null);
						s += d;
					}
					if (dmin>s){
						dmin = s;
						vmin = new Vector2D(Double.MAX_VALUE,Double.MAX_VALUE);
						rmin = Double.MAX_VALUE;
					}

					if (dmin<0.0001)
						break;					
				}
			}//end of for
			if (vmin!=null && rmin>=10){
				this.center = vmin;
				this.radius = rmin;
			} else dmin = Double.MAX_VALUE;
			if (dmin<Double.MAX_VALUE)
				return dmin;
		}		
		return -1;
	}

	public Edge(double[] xx,double[] yy){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		size = xx.length;
		int sz = Math.max(size, NUM_POINTS);

		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);
		straightDist = 0;

		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double x0 = xx[0];
		double len = 0;
//		int index = 0;
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			aP[i] = p;
			len += p.distance(prev);
			aL[i] = len;
			p2l.put(p, i);						
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) {
				straightDist = y;
				index = i;
			}
		}
		allPoints = ObjectArrayList.wrap(aP);		
		allLengths = new DoubleArrayList(aL);
		totalLength = len;		
		allLengths.setSize(size);
		allPoints.size(size);
		x.setSize(size);
		y.setSize(size);

//		calculateRadius(index);
	}

	public Edge(double[] xx,double[] yy,int size){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);
		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);
		straightDist = 0;		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double len = 0;
		double x0 = xx[0];
//		int index = 0;
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			aP[i] = p;
			len += p.distance(prev);
			aL[i] = len;
			p2l.put(p, i);									
			prev = p;			
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) {
				straightDist = y;
				index = i;
			}
		}		
		totalLength = len;
		allPoints = ObjectArrayList.wrap(aP,size);		
		allLengths = new DoubleArrayList(aL);
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
//		calculateRadius(index);
	}

	public Edge(Vector2D[] v,int size){
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);

		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);						
		allPoints = ObjectArrayList.wrap(v,size);


		Vector2D prev = v[0];
		double x0 = prev.x;
		double len = 0;
		straightDist = 0;
//		int index = 0;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			len += p.distance(prev);
			aL[i] = len;						
			p2l.put(p, i);									
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) {
				straightDist = y;
				index = i;
			}
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
		allPoints.size(size);
//		calculateRadius(index);
	}

	public Edge(Vector2D[] v){
		size = v.length;
		int sz = Math.max(size, NUM_POINTS);

		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2IntOpenHashMap<Vector2D>(sz);					
		allPoints = ObjectArrayList.wrap(v,size);

		straightDist = 0;
		Vector2D prev = v[0];
		double len = 0;
		double x0 = prev.x;
//		int index = 0;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			len += p.distance(prev);
			aL[i] = len;					
			p2l.put(p, i);									
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) {
				straightDist = y;
				index = i;
			}
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
//		calculateRadius(index);
	}

	public final Vector2D getHighestPoint(){
		return allPoints.get(size-1);
	}

	public final Vector2D getLowestPoint(){
		return allPoints.get(0);
	}

	public final Vector2D locatePointAtLength(double length){
		if (allPoints==null || allLengths==null || size<2 || length<0) return null;		
		int index = allLengths.binarySearch(length);				
		Vector2D[] allPoints = this.allPoints.elements();
		if (index>=0)
			return new Vector2D(allPoints[index]);


		if (index<0) index = -index+1;
		Vector2D t = null;
		Vector2D p = null;
		if (index>=size){
			t = allPoints[size-1].minus(allPoints[size-2]).normalized();
			p = allPoints[size-1];
			return p.plus(t.times(length-totalLength));
		} else {
			t = allPoints[index].minus(allPoints[index-1]).normalized();
			p = allPoints[index-1];
		}

		return p.plus(t.times(length-allLengths.getQuick(index-1)));
	}

	public final Vector2D estimatePointOnEdge(double length,Vector2D hP){
		if (size<2) return null;
		Vector2D lastPoint = allPoints.get(size-1);			
		if (length<totalLength || hP==null || hP.equals(lastPoint))
			return locatePointAtLength(length);
		double d = length-totalLength;		

		Vector2D t = hP.minus(lastPoint).normalized();		
		return lastPoint.plus(t.times(d));		
	}

	public boolean isStraight(Vector2D[] points){
		if (points==null || points.length<2) return false;
		int len = points.length;
		if (len==2) return true;
		Vector2D v0 = points[0];
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i=1;i<len;++i){
			Vector2D v = points[i];
			double a = v.minus(v0).angle();
			if (a<min) min = a;
			if (a>max) max = a;
		}
		return (Math.abs(max-min) < 0.1);
	}

	public final int turn(){
		double sumx = 0;
		double[] xx = x.elements();
		calculateRadius();
//		if (radius>350)
//			return MyDriver.UNKNOWN;
		for (int i=0;i<size;++i) sumx +=xx[i];
		double mean = sumx/size;
		double highestx = xx[size-1];		
		if (highestx>mean+DELTA)
			return MyDriver.TURNRIGHT;
		if (highestx<mean-DELTA)
			return MyDriver.TURNLEFT;			

		return MyDriver.STRAIGHT;
	}


	public final void removeLastPoint(){
		size--;
		Vector2D lastPoint = allPoints.get(size);		
		if (straightDist==lastPoint.y ) straightDist=(size>=1) ? allPoints.get(size-1).y : 0;
		p2l.remove(lastPoint);
		allPoints.remove(size);
		x.remove(size);
		y.remove(size);
		allLengths.remove(size);		
	}

	public final void append(Vector2D p){
		Vector2D lastPoint = allPoints.get(size-1);
		size++;
		this.x.add(p.x);
		this.y.add(p.y);		
		this.allPoints.add(p);
		totalLength += p.distance(lastPoint);		
		this.allLengths.add(totalLength);
		this.p2l.put(p, size-1);
		double x0 = x.getQuick(0);
		if (straightDist==lastPoint.y && straightDist<p.y && p.x<=x0+DELTA && p.x>=x0-DELTA) {
			straightDist=p.y;
			index++;
		}
//		if (index<size) calculateRadius(index);
	}

	public final Vector2D get(int index){
		if (index<0 || index>=size)
			return null;		
		return allPoints.get(index);
	}

	public final static void drawEdge(Edge edge,final String title){			
		XYSeries series = new XYSeries("Curve");

		for (int i=0;i<edge.size;++i){
			Vector2D v = edge.get(i);
			series.add(v.x,v.y);
		}

		if (edge.center!=null){
//			if (edge.radius<550) TrackSegment.circle(edge.center.x, edge.center.y, edge.radius, series);
			series.add(edge.center.x, edge.center.y);
		}


		XYDataset xyDataset = new XYSeriesCollection(series);


		// Create plot and show it
		final JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		chart.getXYPlot().getDomainAxis().setRange(-50.0,50.0);
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

	public final static void drawEdge(Edge edge,XYSeries series){					

		for (int i=0;i<edge.size;++i){
			Vector2D v = edge.get(i);
			series.add(v.x,v.y);
		}

		if (edge.center!=null){
			TrackSegment.circle(edge.center.x, edge.center.y, edge.radius, series);
		}
		series.add(edge.center.x, edge.center.y);

	}


}
