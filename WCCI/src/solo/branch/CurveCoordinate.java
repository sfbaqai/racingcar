/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrays;

import java.awt.geom.AffineTransform;
import java.util.Arrays;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public class CurveCoordinate {	
	Double2ObjectSortedMap<Vector2D> lMap;
	private static final double ANGLEACCURACY =100.0d;
	double[] allLengths;	
	Vector2D[] allPoints;
	double width;
	int len;


	public CurveCoordinate() {
		// TODO Auto-generated constructor stub
	}
	CurveCoordinate(Vector2D[] allPoints,double[] allLengths){
		this.allPoints = allPoints;
		this.allLengths = allLengths;
		len = allPoints.length;
	}

	/**
	 * Copy Constructor
	 *
	 * @param curveCoordinate a <code>CurveCoordinate</code> object
	 */
	public CurveCoordinate(CurveCoordinate curveCoordinate) 
	{
		this.lMap = curveCoordinate.lMap;
		this.len = curveCoordinate.len;
		this.allLengths = curveCoordinate.allLengths;
		this.allPoints = curveCoordinate.allPoints;
//		if (lMap!=null) {
//			len = lMap.size();
//			allLengths = new double[len];
////			allLengths = lMap.keySet().toDoubleArray();
////			lMap.values().toArray(allPoints);
//			DoubleSortedSet ds  = lMap.keySet();
//			allPoints = new Vector2D[len];
//			int i=0;
//			for (double d:ds){				
//				allLengths[i] = d;
//				allPoints[i++] = lMap.get(d);
//			}			
//		}
		this.width = curveCoordinate.width;
	}

	public CurveCoordinate(	Double2ObjectSortedMap<Vector2D> map, double width) {				
		lMap = map;
		if (lMap!=null) {
			len = lMap.size();
			DoubleSortedSet ds  = lMap.keySet();			
			allLengths = new double[len];
			allPoints = new Vector2D[len];
			int i=0;
			for (double d:ds){				
				allLengths[i] = d;
				allPoints[i++] = lMap.get(d);
			}
		}
		this.width = width;
	}
	
	public double lengthToPoint(Vector2D p){
		if (p==null) return -1;
		int i=0;
		int j = 0;
		int len = allPoints.length;	
		double min = Double.MAX_VALUE;
		for (i=0;i<len;++i){
			double dist = allPoints[i].distance(p); 
			if (dist<min){
				min = dist;
				j = i;
			}
		}
		return allLengths[j];		
	}
	
	public int insertionPos(double len){
		int index = Arrays.binarySearch(allLengths, len);
		if (index>=0) return index;
		return -index-1;
	}
	
	public double lengthToPointAtHeight(double h){
		if (h<=0) return 0;
		int i=0;
		int len = allPoints.length;
		for (i=0;i<len;++i)
			if (allPoints[i].y>=h)
				break;
		
		if (allPoints[i].y==h){			
			return allLengths[i];
		}
		Vector2D t = allPoints[i].minus(allPoints[i-1]).normalized();
		h -= allPoints[i-1].y;
		return allLengths[i-1]+allPoints[i-1].distance(t.times(h/t.y));		
	}
	
	public Vector2D locatePointAtHeight(double h){
		if (h<0) return null;
		int i=0;
		int len = allPoints.length;
		for (i=0;i<len;++i)
			if (allPoints[i].y>=h)
				break;
		
		if (allPoints[i].y==h){			
			return allPoints[i];
		}
		
		if (i==0) return allPoints[0];
		Vector2D t = allPoints[i].minus(allPoints[i-1]).normalized();
		h -= allPoints[i-1].y;
		return allPoints[i-1].plus(t.times(h/t.y));		
	}
	
	
	public Vector2D locatePointAtDistance(double distance){
		if (len<2) return null;				
		Vector2D o = new Vector2D(0,0);
		Vector2D[] allPoints = this.allPoints;
		if (allPoints[0].distance(o)>distance)
			return null;
		int i=0;
		for (i=len-1;i>=0;--i){
			if (allPoints[i].distance(o)==distance)
				return allPoints[i];
			else if (allPoints[i].distance(o)<distance)
				break;
		}
		
		
		double l = allPoints[i].distance(o);
		Vector2D p = allPoints[i];
		Vector2D t = (i>=len-1 && len>=2) ?  p.minus(allPoints[i-1]).normalized() : allPoints[i+1].minus(p).normalized();
		Vector2D dummy = p.plus(t.times(Math.abs(distance-l)));
		double[] rs = Geom.getLineCircleIntersection(p.x, p.y, dummy.x, dummy.y, o.x, o.y, distance);
		if (rs.length==2) return new Vector2D(rs[0],rs[1]);
		Vector2D p1 = new Vector2D(rs[0],rs[1]);
		if (p1.minus(p).dot(t)>=0) return p1;
		return new Vector2D(rs[2],rs[3]);
	
	}

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

	//return the coordinates of the point at distance = length from the origin in the curve axis
	public Vector2D[] locatePointAtLength(double length,Vector2D p,Vector2D t,Vector2D n){
		if (p==null || allLengths==null)
			return null;
		int index = Arrays.binarySearch(allLengths, length);
		if (index>0){			
			p.copy(allPoints[index]);
			if (index<len-1){
				if (t!=null) t.copy(allPoints[index+1].minus(p).normalized());				
			} else {
				if (t!=null) t.copy(p.minus(allPoints[index-1]).normalized());				
			}
			if (n!=null) n.copy(t.orthogonal());

			return ObjectArrays.copy(allPoints, index, len-index);
		}
		if (index<0) index = -index;
		if (index>=len){
			if (len>0) p.copy(allPoints[len-1]);
			if (t ==null) t = new Vector2D(0,0);
			t.copy(p.minus(allPoints[len-2]).normalized());
			p.copy(p.plus(t.times(length-allLengths[len-1])));
			return null;
		}		
		p.copy(allPoints[index]);
		if (index<len-1){
			if (t!=null) 
				t.copy(allPoints[index+1].minus(p).normalized());
			else t = allPoints[index+1].minus(p).normalized();
		} else {
			if (t!=null) 
				t.copy(p.minus(allPoints[index-1]).normalized());
			else t = p.minus(allPoints[index-1]).normalized();
		}
		if (n!=null) n.copy(t.orthogonal());
		p.copy(p.plus(t.times(length-allLengths[index])));		
		Vector2D[] rs = new Vector2D[len-index];
		rs[0]=p;
		for (int i=1;i<rs.length;++i){
			rs[i] = allPoints[index+i];
		}
		return rs;
	}
			


	public static Vector2D intersects(Vector2D x,Vector2D y,Vector2D[] v,ObjectArrayList<Vector2D> theRest){
		if (v==null)
			return null;
		int len = v.length;
		double[] rs = new double[3];
		Vector2D point = null;
		int i=0;
		for (i=0;i<len-1;++i){			
			if (Geom.getLineSegIntersection(x.x, x.y, y.x, y.y, v[i].x, v[i].y, v[i+1].x, v[i+1].y,rs) == Geom.INTERSECT){
				point = new Vector2D(rs[0],rs[1]);
				break;
			}
		}
		if (point==null || theRest==null) return point;
		theRest.add(point);
		for (int j=i+1;j<len;++j)
			theRest.add(new Vector2D(v[j].x,v[j].y));		
		return point;

	}

	public static Vector2D[] convertToCarPerspective(Vector2D[] v,Vector2D t){
		if (v==null) return null;		
		double angle = Vector2D.angle(new Vector2D(0,1), t);
		AffineTransform at =new AffineTransform();
		at.rotate(angle);
		at.translate(-v[0].x, -v[0].y);
		Vector2D[] rs = v.clone();
		at.transform(v, 0, rs, 0, v.length);
		return rs;
	}
	
	public static Vector2D[] convertToCarPerspective(ObjectArrayList<Vector2D> v,Vector2D t){
		if (v==null) return null;
		Vector2D[] vv = new Vector2D[v.size()];
		vv = v.toArray(vv);		
		double angle = Vector2D.angle(new Vector2D(0,1), t);
		AffineTransform at =new AffineTransform();
		at.rotate(angle);
		Vector2D p = v.get(0);
		at.translate(-p.x, -p.y);
		Vector2D[] rs = new Vector2D[v.size()];
		
		ObjectArrays.fill(rs, new Vector2D(0,0));
		at.transform(vv, 0, rs, 0, v.size());
		return rs;
	}
	
	public static AffineTransform getTransformMatrixToCarPerspective(ObjectArrayList<Vector2D> v,Vector2D t){
		if (v==null) return null;				
		double angle = Vector2D.angle(new Vector2D(0,1), t);
		AffineTransform at =new AffineTransform();
		at.rotate(angle);
		Vector2D p = v.get(0);
		at.translate(-p.x, -p.y);
		return at;
	}
	
	public static AffineTransform getTransformMatrixToCarPerspective(Vector2D[] v,Vector2D p,Vector2D t){
		if (v==null) return null;				
//		System.out.println(t);
//		t = v[1].minus(v[0]);
		AffineTransform at =new AffineTransform();
		if (Math.abs(t.y)>0.98) {
			at.translate(-p.x, -p.y);
			return at;
		}
		
		double angle = -Vector2D.angle(new Vector2D(0,1),t);
		angle=Math.round(angle*ANGLEACCURACY)/ANGLEACCURACY;
		System.out.println(angle+"    "+t);
		if (angle!=0)
			System.out.println(new ObjectArrayList<Vector2D>(v));
		
		at.rotate(angle);						
		at.translate(-p.x, -p.y);
		return at;
	}



	/**
	 * @return the x
	 */
	/**
	 * @return the lMap
	 */
	public Double2ObjectSortedMap<Vector2D> getLMap() {
		return lMap;
	}
	/**
	 * @param map the lMap to set
	 */
	public void setLMap(Double2ObjectSortedMap<Vector2D> map) {
		lMap = map;
	}
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	@Override
	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "CurveCoordinate ( "
			+ super.toString() + TAB	        
			+ "lMap = " + this.lMap + TAB
			+ "width = " + this.width + TAB
			+ " )";

		return retValue;
	}


}
