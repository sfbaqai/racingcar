/**
 * 
 */
package solo;

import java.util.Arrays;

import cern.colt.list.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.Double2DoubleSortedMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleRBTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * @author kokichi3000
 *
 */
public class Edge {
	public final static double ANGLEACCURACY =100.0d;
	public final static int NUM_POINTS=150;
	public final static double DELTA = 0.001;
	ObjectArrayList<Vector2D> allPoints = null;
	DoubleArrayList x = null;
	DoubleArrayList y = null;
	DoubleArrayList allLengths = null;	
	Object2DoubleMap<Vector2D> p2l = null;
	double straightDist = 0;
	
	int size =0;	
	double totalLength =0;
	
	public Edge(){
	}
	
	public Edge(double[] xx,double[] yy){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		size = xx.length;
		int sz = Math.max(size, NUM_POINTS);
		allPoints = new ObjectArrayList<Vector2D>(sz);
		allLengths = new DoubleArrayList(sz);		
		p2l = new Object2DoubleRBTreeMap<Vector2D>();			
		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double len = 0;
		for (int i=0;i<sz;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			allPoints.add(p);
			allLengths.setQuick(i, len);
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
		}
		totalLength = len;
	}
	
	public Edge(double[] xx,double[] yy,int size){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);
		allPoints = new ObjectArrayList<Vector2D>(sz);
		allLengths = new DoubleArrayList(sz);		
		p2l = new Object2DoubleRBTreeMap<Vector2D>();		
		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double len = 0;
		for (int i=0;i<sz;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			allPoints.add(p);
			allLengths.setQuick(i, len);
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
		}
		totalLength = len;
	}
	
	public Edge(Vector2D[] v,int size){
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);
		
		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2DoubleRBTreeMap<Vector2D>();						
		allPoints = ObjectArrayList.wrap(v);
		
		
		Vector2D prev = v[0];
		double len = 0;
		for (int i=0;i<sz;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			aL[i] = len;
			allPoints.add(p);			
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
	}
	
	public Edge(Vector2D[] v){
		size = v.length;
		int sz = Math.max(size, NUM_POINTS);
		
		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2DoubleRBTreeMap<Vector2D>();					
		allPoints = ObjectArrayList.wrap(v);
		
		
		Vector2D prev = v[0];
		double len = 0;
		for (int i=0;i<sz;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			aL[i] = len;
			allPoints.add(p);			
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
	}
	
	public Vector2D getHighestPoint(){
		return allPoints.get(size-1);
	}

	public Vector2D getLowestPoint(){
		return allPoints.get(0);
	}
	
	public Vector2D locatePointAtLength(double length){
		if (allPoints==null || allLengths==null || size<2) return null;		
		int index = allLengths.binarySearch(length);				
		Vector2D[] allPoints = this.allPoints.elements();
		if (index>0)
			return allPoints[index];
		
					
		if (index<0) index = -index+1;
		Vector2D t = null;
		Vector2D p = null;
		if (index>=size){
			t = allPoints[size-1].minus(allPoints[size-2]).normalized();
			p = allPoints[size-1];
		} else {
			t = allPoints[index].minus(allPoints[index-1]).normalized();
			p = allPoints[index-1];
		}
		
		return p.plus(t.times(length-allLengths.getQuick(size-1)));
	}
	
	public Vector2D estimatePointOnEdge(double length,Vector2D hP){
		if (size<2) return null;
		if (length<=totalLength || hP==null)
			return locatePointAtLength(length);
		double d = length-totalLength;		
		Vector2D lastPoint = allPoints.get(size-1);		
		Vector2D t = hP.minus(lastPoint).normalized();		
		return lastPoint.plus(t.times(d));		
	}
	
	public int turn(){
		double sumx = 0;
		double[] xx = x.elements();
		double[] yy = y.elements();
		for (int i=0;i<size;++i){
			double a = xx[i];
			sumx += a;
			double mean = sumx/(i+1);
			if (a>mean+DELTA)
				return MyDriver.TURNRIGHT;
			if (a<mean-DELTA)
				return MyDriver.TURNLEFT;
			if (straightDist<yy[i])straightDist = yy[i];
		}
			
		return MyDriver.STRAIGHT;
	}
	
	public void append(Vector2D p){
		Vector2D lastPoint = allPoints.get(size-1);
		size++;
		this.x.add(p.x);
		this.y.add(p.y);		
		this.allPoints.add(p);
		totalLength += p.distance(lastPoint);		
		this.allLengths.add(totalLength);
		this.p2l.put(p, totalLength);
	}
	
	
}
