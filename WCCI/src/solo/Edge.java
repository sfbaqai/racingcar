/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import cern.colt.list.DoubleArrayList;

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
				
		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];
		p2l = new Object2DoubleOpenHashMap<Vector2D>(sz);			
		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double x0 = xx[0];
		double len = 0;
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			aP[i] = p;
			aL[i] = len;
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) straightDist = y; 
		}
		allPoints = ObjectArrayList.wrap(aP);		
		allLengths = new DoubleArrayList(aL);
		totalLength = len;		
		allLengths.setSize(size);
		allPoints.size(size);
		x.setSize(size);
		y.setSize(size);		
	}
	
	public Edge(double[] xx,double[] yy,int size){
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);
		double[] aL = new double[sz];
		Vector2D[] aP = new Vector2D[sz];		
		p2l = new Object2DoubleOpenHashMap<Vector2D>(sz);
		straightDist = 0;		
		Vector2D prev = new Vector2D(xx[0],yy[0]);
		double len = 0;
		double x0 = xx[0];
		for (int i=0;i<size;++i){
			double x = xx[i];
			double y = yy[i];
			Vector2D p =new Vector2D(x,y);
			aP[i] = p;
			aL[i] = len;
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;			
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) straightDist = y;
		}		
		totalLength = len;
		allPoints = ObjectArrayList.wrap(aP,size);		
		allLengths = new DoubleArrayList(aL);
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);		
	}
	
	public Edge(Vector2D[] v,int size){
		this.size = size;
		int sz = Math.max(size, NUM_POINTS);
		
		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2DoubleOpenHashMap<Vector2D>(sz);						
		allPoints = ObjectArrayList.wrap(v,size);
		
		
		Vector2D prev = v[0];
		double x0 = prev.x;
		double len = 0;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			aL[i] = len;						
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) straightDist = y;
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);
		allPoints.size(size);
	}
	
	public Edge(Vector2D[] v){
		size = v.length;
		int sz = Math.max(size, NUM_POINTS);
		
		double[] xx = new double[sz];
		double[] yy = new double[sz];
		double[] aL = new double[sz];		
		p2l = new Object2DoubleOpenHashMap<Vector2D>(sz);					
		allPoints = ObjectArrayList.wrap(v,size);
		
		
		Vector2D prev = v[0];
		double len = 0;
		double x0 = prev.x;
		for (int i=0;i<size;++i){
			Vector2D p = v[i];
			double x = p.x;
			double y = p.y;			
			xx[i] = x;
			yy[i] = y;
			aL[i] = len;					
			p2l.put(p, len);						
			len += p.distance(prev);
			prev = p;
			if (straightDist<y && x<=x0+DELTA && x>=x0-DELTA) straightDist = y;
		}
		x = new DoubleArrayList(xx);
		y = new DoubleArrayList(yy);
		allLengths = new DoubleArrayList(aL);
		totalLength = len;
		x.setSize(size);
		y.setSize(size);
		allLengths.setSize(size);		
	}
	
	public Vector2D getHighestPoint(){
		return allPoints.get(size-1);
	}

	public Vector2D getLowestPoint(){
		return allPoints.get(0);
	}
	
	public Vector2D locatePointAtLength(double length){
		if (allPoints==null || allLengths==null || size<2 || length<0) return null;		
		int index = allLengths.binarySearch(length);				
		Vector2D[] allPoints = this.allPoints.elements();
		if (index>=0)
			return allPoints[index];
		
					
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
	
	public Vector2D estimatePointOnEdge(double length,Vector2D hP){
		if (size<2) return null;
		if (length>=totalLength-0.3 || length<=totalLength+0.3)
			return allPoints.get(size-1);
		if (length<totalLength || hP==null)
			return locatePointAtLength(length);
		double d = length-totalLength;		
		Vector2D lastPoint = allPoints.get(size-1);		
		Vector2D t = hP.minus(lastPoint).normalized();		
		return lastPoint.plus(t.times(d));		
	}
	
	public int turn(){
		double sumx = 0;
		double[] xx = x.elements();
					
		for (double a:xx) sumx +=a;
		double mean = sumx/size;
		double highestx = xx[size-1];
		
		if (highestx>mean+DELTA)
			return MyDriver.TURNRIGHT;
		if (highestx<mean-DELTA)
			return MyDriver.TURNLEFT;			
				
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
		double x0 = x.getQuick(0);
		if (straightDist==lastPoint.y && straightDist<p.y && p.x<=x0+DELTA && p.x>=x0-DELTA) straightDist=p.y;
	}
	
	public Vector2D get(int index){
		if (index<0 || index>=size)
			return null;
		return allPoints.get(index);
	}
}
