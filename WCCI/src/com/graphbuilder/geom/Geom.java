package com.graphbuilder.geom;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import jaolho.data.lma.QuickCircleFitter;
import solo.Vector2D;


/**
Geom contains static methods for calculating intersections, angles, areas and distances.
*/
public final class Geom {

	public static final Object PARALLEL = new Object();
	public static final Object INTERSECT = new Object();

	private Geom() {}

	/**
	The getAngle method returns the angle between (x, y) and (originX, originY).
	The value returned will be in the range [0.0 : 2 * Math.PI).  If the point x, y
	overlaps the origin then 0.0 is returned.  If the point has a positive x and zero y
	then the value returned is 0.0.  If the point has a negative x and zero y then the
	value returned is Math.PI.  If the point has a zero x and positive y then the value
	returned is Math.PI / 2.  If the point has a zero x and negative y then the value
	returned is 3 * Math.PI / 2.
	*/
	public static double getAngle(double originX, double originY, double x, double y) {

		double adj = x - originX;
		double opp = y - originY;
		double rad = 0.0;

		if (adj == 0) {
			if (opp == 0) return 0.0;
			else rad = Math.PI / 2;
		}
		else {
			rad = Math.atan(opp / adj);
			if (rad < 0) rad = -rad;
		}

		if (x >= originX) {
			if (y < originY) rad = 2 * Math.PI - rad;
		}
		else {
			if (y < originY) rad = Math.PI + rad;
			else rad = Math.PI - rad;
		}
		return rad;
	}

	/**
	Returns the angle between the origin and the specified point.

	@see #getAngle(double,double,double,double)
	*/
	public static double getAngle(Point2d origin, Point2d p) {
		return getAngle(origin.getX(), origin.getY(), p.getX(), p.getY());
	}

	/**
	The ptLineDistSq method returns the distance between the line formed by (x1, y1), (x2, y2) and
	the point (x, y).  An array of length >= 3 can be passed in to obtain additional information.
	If the array is not null, then the closest point on the line to the given point is stored in
	index locations 0 and 1.  The parametric value is stored in index location 2.
	*/
	public static double ptLineDistSq(double x1, double y1, double x2, double y2, double x, double y, double[] result) {
		double run = x2 - x1;
		double rise = y2 - y1;
		double t = 0.0;
		double f = run * run + rise * rise;

		if (f != 0)
			t = (run * (x - x1) + rise * (y - y1)) / f;

		double nx = x1 + t * run;
		double ny = y1 + t * rise;

		if (result != null) {
			result[0] = nx;
			result[1] = ny;
			result[2] = t;
		}

		double dx = x - nx;
		double dy = y - ny;
		return dx * dx + dy * dy;
	}

	/**
	The ptSegDistSq method returns the distance between the line segment formed by (x1, y1), (x2, y2) and
	the point (x, y).  An array of length >= 3 can be passed in to obtain additional information.  If the
	array is not null, then the closest point on the line segment to the given point is stored in index
	locations 0 and 1.  The parametric value is stored in index location 2 and its value is >= 0 && <= 1.
	*/
	public static double ptSegDistSq(double x1, double y1, double x2, double y2, double x, double y, double[] result) {
		double run = x2 - x1;
		double rise = y2 - y1;
		double t = 0.0;
		double f = run * run + rise * rise;

		if (f != 0)
			t = (run * (x - x1) + rise * (y - y1)) / f;

		if (t < 0) t = 0.0;
		else if (t > 1) t = 1.0;

		double nx = x1 + t * run;
		double ny = y1 + t * rise;

		if (result != null) {
			result[0] = nx;
			result[1] = ny;
			result[2] = t;
		}

		double dx = x - nx;
		double dy = y - ny;
		return dx * dx + dy * dy;
	}


	/**
	Computes the distance between a line (a, b) and a point (c) in n-dimensions.  Arrays a, b, and c must
	have length greater or equal to n.  Array d must have length greater than n.  The location of the closest
	point on the line is stored in d.  The parametric value is stored at index location n in d.
	*/
	public static double ptLineDistSq(double[] a, double[] b, double[] c, double[] d, int n) {
		for (int i = 0; i < n; i++)
			d[i] = b[i] - a[i];

		double f = 0;
		for (int i = 0; i < n; i++)
			f = f + d[i] * d[i];

		double t = 0.0;

		if (f != 0) {
			double g = 0;
			for (int i = 0; i < n; i++)
				g = g + d[i] * (c[i] - a[i]);

			t = g / f;
		}

		for (int i = 0; i < n; i++)
			d[i] = a[i] + t * d[i];

		d[n] = t;

		double distSq = 0;
		for (int i = 0; i < n; i++) {
			double h = c[i] - d[i];
			distSq = distSq + h * h;
		}

		return distSq;
	}

	/**
	Computes the distance between a line segment (a, b) and a point (c) in n-dimensions.  Arrays a, b, and c must
	have length greater or equal to n.  Array d must have length greater than n.  The location of the closest
	point on the line is stored in d.  The parametric value is stored at index location n in d, and its value is
	in the range [0, 1].
	*/
	public static double ptSegDistSq(double[] a, double[] b, double[] c, double[] d, int n) {
		for (int i = 0; i < n; i++)
			d[i] = b[i] - a[i];

		double f = 0;
		for (int i = 0; i < n; i++)
			f = f + d[i] * d[i];

		double t = 0.0;

		if (f != 0) {
			double g = 0;
			for (int i = 0; i < n; i++)
				g = g + d[i] * (c[i] - a[i]);

			t = g / f;
		}

		if (t < 0.0) t = 0.0;
		else if (t > 1.0) t = 1.0;

		for (int i = 0; i < n; i++)
			d[i] = a[i] + t * d[i];

		d[n] = t;

		double distSq = 0;
		for (int i = 0; i < n; i++) {
			double h = c[i] - d[i];
			distSq = distSq + h * h;
		}

		return distSq;
	}


	/**
	Calculates the intersection location of the two lines formed by (x1, y1), (x2, y2) and (x3, y3), (x4, y4).
	If the lines are determined to be parallel, then Geom.PARALLEL is returned and no further computations
	are done.  If the lines are not parallel, then the intersection location is stored in index locations 0 and 1
	of the specified array.  The parametric value is stored in index location 2.  If there is an intersection then
	the returned value is Geom.INTERSECT.
	*/
	public static Object getLineLineIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double[] result) {
		double bx = x2 - x1;
		double by = y2 - y1;
		double dx = x4 - x3;
		double dy = y4 - y3;

		double b_dot_d_perp = bx * dy - by * dx;

		if (b_dot_d_perp == 0)
			return PARALLEL;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double t = (cx * dy - cy * dx) / b_dot_d_perp;

		if (result != null) {
			result[0] = x1 + t * bx;
			result[1] = y1 + t * by;
			result[2] = t;
		}

		return INTERSECT;
	}

	/**
	Calculates the intersection location of the line formed by (x1, y1), (x2, y2) and the line segment formed
	by (x3, y3), (x4, y4).  If the line and line segment are determined to be parallel, then Geom.PARALLEL is
	returned and no further computations are done.  If the line segment does not cross the line, then null is
	returned and no further computations are done.  Otherwise the intersection location is stored in index
	locations 0 and 1 of the specified array.  The parametric value with respect to the line segment is stored
	in index location 2.  If there in an intersection then the returned value is Geom.INTERSECT.
	*/
	public static Object getLineSegIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double[] result) {
		double bx = x2 - x1;
		double by = y2 - y1;
		double dx = x4 - x3;
		double dy = y4 - y3;

		double b_dot_d_perp = bx * dy - by * dx;

		if (b_dot_d_perp == 0)
			return PARALLEL;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double u = (cx * by - cy * bx) / b_dot_d_perp;
		if (u < 0 || u > 1) return null;

		if (result != null) {
			result[0] = x3 + u * dx;
			result[1] = y3 + u * dy;
			result[2] = u;
		}

		return INTERSECT;
	}


	/**
	Calculates the intersection location of the line segments formed by (x1, y1), (x2, y2) and (x3, y3), (x4, y4).
	If the line segments are determined to be parallel, then Geom.PARALLEL is returned and no further computations
	are done.  If the segments do not cross each other then null is returned and no further computations are done.
	Otherwise the intersection location is stored in index locations 0 and 1 of the specified array.  The parametric
	value with respect to the first line segment is stored in index location 2.  If there is an intersection, then
	the returned value is Geom.INTERSECT.
	*/
	public static Object getSegSegIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double[] result) {
		double bx = x2 - x1;
		double by = y2 - y1;
		double dx = x4 - x3;
		double dy = y4 - y3;

		double b_dot_d_perp = bx * dy - by * dx;

		if (b_dot_d_perp == 0)
			return PARALLEL;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double t = (cx * dy - cy * dx) / b_dot_d_perp;

		if (t < 0 || t > 1) return null;

		double u = (cx * by - cy * bx) / b_dot_d_perp;

		if (u < 0 || u > 1) return null;

		if (result != null) {
			result[0] = x1 + t * bx;
			result[1] = y1 + t * by;
			result[2] = t;
		}

		return INTERSECT;
	}
	
	
	/**
	Calculates the intersection location of the line segments formed by (x1, y1), (x2, y2) and (x3, y3), (x4, y4).
	If the line segments are determined to be parallel, then Geom.PARALLEL is returned and no further computations
	are done.  If the segments do not cross each other then null is returned and no further computations are done.
	Otherwise the intersection location is stored in index locations 0 and 1 of the specified array.  The parametric
	value with respect to the first line segment is stored in index location 2.  If there is an intersection, then
	the returned value is Geom.INTERSECT.
	*/
	public static int getSegSegIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double[] result) {
		double bx = x2 - x1;
		double by = y2 - y1;
		double dx = x4 - x3;
		double dy = y4 - y3;

		double b_dot_d_perp = bx * dy - by * dx;

		if (b_dot_d_perp == 0)
			return 0;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double t = (cx * dy - cy * dx) / b_dot_d_perp;

		if (t < 0 || t > 1) return 0;

		double u = (cx * by - cy * bx) / b_dot_d_perp;

		if (u < 0 || u > 1) return 0;

		if (result != null) {
			result[0] = x1 + t * bx;
			result[1] = y1 + t * by;
			result[2] = t;
		}

		return 2;
	}



	public static boolean getCircle(Vector2D v1, Vector2D v2, Vector2D v3, double[] result) {
		return getCircle(v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, result);
	}

	/**
	Computes the circle formed by three points (x1, y1), (x2, y2) and (x3, y3).  If the points are
	collinear, then false is returned and no further computations are done.  If the points are not
	collinear, then the specified array is used to store the result.  The center of the circle is
	stored in index locations 0 and 1, and the radius squared is stored in index location 2.  True
	is returned if the points are not collinear.
	*/
	public static boolean getCircle(double x1, double y1, double x2, double y2, double x3, double y3, double[] result) {

		double ax = x2 - x1;  // first compute vectors
		double ay = y2 - y1;  // a and c
		double cx = x1 - x3;
		double cy = y1 - y3;

		double aPerpDOTc = ax * cy - ay * cx;

		if (aPerpDOTc == 0)
			return false;

		double bx = x3 - x2;
		double by = y3 - y2;
		double bDOTc = bx * cx + by * cy;

		double qo = bDOTc / aPerpDOTc;
		double sx = x1 + (ax - qo * ay) * 0.5; // found center of circle
		double sy = y1 + (ay + qo * ax) * 0.5; // (sx, sy)

		double dx = x1 - sx;
		double dy = y1 - sy;
		double rSquared = dx * dx + dy * dy; // radius of the circle squared

		if (result != null) {
			result[0] = sx;
			result[1] = sy;
			result[2] = rSquared;
		}

		return true;
	}
	
	public static double estimateCircle(ObjectArrayList<Vector2D> ol,double[] r){
		if (ol==null || ol.size()<3) return -1;
		if (r==null) r = new double[3];
		Geom.getCircle(ol.get(0), ol.get(1), ol.get(2), r);
		r[2] = Math.sqrt(r[2]);
		if (ol.size()==3) return r[2];
		QuickCircleFitter cf = new QuickCircleFitter(new double[]{r[0],r[1]}, ol.elements(),0,ol.size()-1);
		cf.fit();
		Vector2D c = cf.getEstimatedCenter();
		if (c!=null){
			r[0] = c.x;
			r[1] = c.y;
			r[2] = cf.getEstimatedRadius();
		}
		return r[2];
	}

	public static double estimateCircle(ObjectArrayList<Vector2D> ol,int from,int to,double[] r){
		if (ol==null || ol.size()<3) return -1;
		if (r==null) r = new double[3];
		Geom.getCircle(ol.get(0+from), ol.get(1+from), ol.get(2+from), r);
		r[2] = Math.sqrt(r[2]);
		if (to-from==3) return r[2];
		QuickCircleFitter cf = new QuickCircleFitter(new double[]{r[0],r[1]}, ol.elements(),from,to-1);
		cf.fit();
		Vector2D c = cf.getEstimatedCenter();
		if (c!=null){
			r[0] = c.x;
			r[1] = c.y;
			r[2] = cf.getEstimatedRadius();
		}
		return r[2];
	}
	
	public static double estimateCircle(Vector2D[] ol,int from,int to,double[] r){
		if (ol==null || ol.length<3) return -1;
		if (r==null) r = new double[3];
		Geom.getCircle(ol[0+from], ol[1+from], ol[2+from], r);
		r[2] = Math.sqrt(r[2]);
		if (to-from==3) return r[2];
		QuickCircleFitter cf = new QuickCircleFitter(new double[]{r[0],r[1]}, ol,from,to-1);
		cf.fit();
		Vector2D c = cf.getEstimatedCenter();
		if (c!=null){
			r[0] = c.x;
			r[1] = c.y;
			r[2] = cf.getEstimatedRadius();
		}
		return r[2];
	}

	public static double estimateCircle(Vector2D[] ol,double[] r){
		if (ol==null || ol.length<3) return -1;
		if (r==null) r = new double[3];
		Geom.getCircle(ol[0], ol[1], ol[2], r);
		r[2] = Math.sqrt(r[2]);
		if (ol.length==3) return r[2];
		QuickCircleFitter cf = new QuickCircleFitter(new double[]{r[0],r[1]}, ol,0,ol.length-1);
		cf.fit();
		Vector2D c = cf.getEstimatedCenter();
		if (c!=null){
			r[0] = c.x;
			r[1] = c.y;
			r[2] = cf.getEstimatedRadius();
		}
		return r[2];
	}
	/**
	Returns the area^2 of the triangle formed by three points (x1, y1), (x2, y2) and (x3, y3).
	*/
	public static double getTriangleAreaSq(double x1, double y1, double x2, double y2, double x3, double y3) {
		double ax = x1 - x2;
		double ay = y1 - y2;

		double bx = x2 - x3;
		double by = y2 - y3;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double a = (ax * ax + ay * ay) * 0.5;
		double b = (bx * bx + by * by) * 0.5;
		double c = (cx * cx + cy * cy) * 0.5;

		// the following two if statements increase the numerical stability in the
		// case of "needle" triangles.  'a' is made to be the smallest number.

		if (b < a) {
			double t = a;
			a = b;
			b = t;
		}

		if (c < a) {
			double t = a;
			a = c;
			c = t;
		}

		double d = (a + (b - c)) *0.5;
		return a * b - d * d;
	}

	/**
	Returns the area^2 of the triangle formed by the 3 side-lengths 'a', 'b' and 'c'.

	@throws IllegalArgumentException if the side-lengths are less than 0 or cannot form a triangle.
	*/
	public static double getTriangleAreaSq(double a, double b, double c) {
		// implementation based on notes from: http://http.cs.berkeley.edu/~wkahan/Triangle.pdf
		// 1. constraint checking is done
		// 2. numbers are sorted such that a >= b >= c
		// 3. stable equation is used to compute the area

		if (a < 0)
			throw new IllegalArgumentException("a >= 0 required");
		if (b < 0)
			throw new IllegalArgumentException("b >= 0 required");
		if (c < 0)
			throw new IllegalArgumentException("c >= 0 required");

		if (a > b+c)
			throw new IllegalArgumentException("a <= b + c required");
		if (b > a+c)
			throw new IllegalArgumentException("b <= a + c required");
		if (c > a+b)
			throw new IllegalArgumentException("c <= a + b required");

		if (a < c) {
			double t = c;
			c = a;
			a = t;
		}

		if (b < c) {
			double t = c;
			c = b;
			b = t;
		}

		if (a < b) {
			double t = b;
			b = a;
			a = t;
		}

		return (a+(b+c))*(c-(a-b))*(c+(a-b))*(a+(b-c)) / 16.0;
	}
	
	public static double[] getLineCircleIntersection(double x1,double y1,double x2,double y2,double x,double y,double r){
		x2 -= x;
		x1 -= x;
		y2 -= y;
		y1 -= y;
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		
		double D = x1*y2-x2*y1;
		double sign = (dy<0) ? -1 :1;	
		double delta = r*r*dr*dr-D*D;
		if (delta<0) return null;
		double[] result = (delta==0) ? new double[2] : new double[4];
		
		delta = Math.sqrt(delta);
		double dr2 = dr*dr;		
		if (delta==0){
			result[0] = D*dy/dr2+x;
			result[1] = -D*dx+y;
			return result;
		}
		double px1 = (D*dy+sign*dx*delta)/dr2;
		double px2 = (D*dy-sign*dx*delta)/dr2;		
		double py1 = (-D*dx+Math.abs(dy)*delta)/dr2;
		double py2 = (-D*dx-Math.abs(dy)*delta)/dr2;
		result[0] = px1+x;
		result[1] = py1+y;
		result[2] = px2+x;
		result[3] = py2+y;
		return result;
	}
	
	public static int getLineCircleIntersection(double x1,double y1,double x2,double y2,double x,double y,double r,double[] result){
		x2 -= x;
		x1 -= x;
		y2 -= y;
		y1 -= y;
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		
		double D = x1*y2-x2*y1;
		double sign = (dy<0) ? -1 :1;	
		double delta = r*r*dr*dr-D*D;
		if (delta<0) return 0;		
		
		delta = Math.sqrt(delta);
		double dr2 = dr*dr;		
		if (delta==0){
			result[0] = D*dy/dr2+x;
			result[1] = -D*dx+y;
			return 2;
		}
		double px1 = (D*dy+sign*dx*delta)/dr2;
		double px2 = (D*dy-sign*dx*delta)/dr2;		
		double py1 = (-D*dx+Math.abs(dy)*delta)/dr2;
		double py2 = (-D*dx-Math.abs(dy)*delta)/dr2;
		result[0] = px1+x;
		result[1] = py1+y;
		result[2] = px2+x;
		result[3] = py2+y;
		return 4;
	}
	
	public static double[] getLineArcIntersection(double x1,double y1,double x2,double y2,double x,double y,double r,double arc,double sx,double sy,double ex,double ey){
		double[] rr = getLineCircleIntersection(x1, y1, x2, y2, x, y, r);
		if (rr==null)
			return null;
		
		Vector2D p1 = new Vector2D(rr[0],rr[1]);
		Vector2D c = new Vector2D(x,y);
		double a = Vector2D.angle(new Vector2D(sx-x,sy-y),p1.minus(c));
		
		DoubleArrayList dal = new DoubleArrayList();
		
		if (a*arc>=0 && Math.abs(a)<=Math.abs(arc)) {
			dal.add(rr[0]);
			dal.add(rr[1]);
		} else if (a*arc<0 && Math.abs(a)+Math.abs(arc)>=Math.PI*2) {
			dal.add(rr[0]);
			dal.add(rr[1]);
			
		}
		if (rr.length==2 && dal.size()==0)
			return null;
		
		p1 = new Vector2D(rr[2],rr[3]);		
		a = Vector2D.angle(new Vector2D(sx-x,sy-y),p1.minus(c));
		if (a*arc>=0 && Math.abs(a)<=Math.abs(arc)) {
			dal.add(rr[2]);
			dal.add(rr[3]);
		} else if (a*arc<0 && Math.abs(a)+Math.abs(arc)>=Math.PI*2) {
			dal.add(rr[2]);
			dal.add(rr[3]);
			
		}
						
		return dal.toDoubleArray();			
	}
	
	
	public static double[] getSegArcIntersection(double x1,double y1,double x2,double y2,double x,double y,double r,double arc,double sx,double sy,double ex,double ey){
		double[] rr = new double[6]; 
		getLineSegCircleIntersection(x1, y1, x2, y2, x, y, r,rr);		
		
		Vector2D p1 = new Vector2D(rr[0],rr[1]);
		Vector2D c = new Vector2D(x,y);
		double a = Vector2D.angle(new Vector2D(sx-x,sy-y),p1.minus(c));		
		
		DoubleArrayList dal = new DoubleArrayList();
		
		if (a*arc>=0 && Math.abs(a)<=Math.abs(arc)) {
			dal.add(rr[0]);
			dal.add(rr[1]);
		} else if (a*arc<0 && Math.abs(a)+Math.abs(arc)>=Math.PI*2) {
			dal.add(rr[0]);
			dal.add(rr[1]);
			
		}
		if (rr.length==2 && dal.size()==0)
			return null;
		if (rr.length==2) return dal.toDoubleArray();

		p1 = new Vector2D(rr[2],rr[3]);		
		a = Vector2D.angle(new Vector2D(sx-x,sy-y),p1.minus(c));
		if (a*arc>=0 && Math.abs(a)<=Math.abs(arc)) {
			dal.add(rr[2]);
			dal.add(rr[3]);
		} else if (a*arc<0 && Math.abs(a)+Math.abs(arc)>=Math.PI*2) {
			dal.add(rr[2]);
			dal.add(rr[3]);
			
		}
						
		return (dal.size()==0) ? null : dal.toDoubleArray();		
	}
	
	public static boolean isPointInArc(double px,double py,double x,double y,double r,double sx,double sy,double ex,double ey){				
		double a = Vector2D.angle(sx-x,sy-y,px-x,py-y);				
		double arc = Vector2D.angle(sx-x,sy-y,ex-x,ey-y);
		if (a>Math.PI) a -= Math.PI*2;
		if (a<-Math.PI) a+=Math.PI*2;
		if (arc>Math.PI) arc -= Math.PI*2;
		if (arc<-Math.PI) arc+=Math.PI*2;
		
		if (a*arc>=0 && Math.abs(a)<=Math.abs(arc)) {
			return true;
		} else if (a*arc<0 && Math.abs(a)+Math.abs(arc)>=Math.PI*2) 
			return true;
			
		return false;
	}

	
	public static int getSegArcIntersection(double x1,double y1,double x2,double y2,double x,double y,double r,double sx,double sy,double ex,double ey,double[] rr){
		if (rr==null)
			return 0;
		int sz = getLineSegCircleIntersection(x1, y1, x2, y2, x, y, r,rr);
		if (sz==0) return 0;		
		
		double px  = rr[0];
		double py = rr[1];
		int size = 0;
		if (((px-x1)*(px-x2)<=0 || (py-y1)*(py-y2)<=0) && isPointInArc(px, py, x, y, r, sx, sy, ex, ey)){
			size = 2;
		}
								
		if (sz==2) return size;
		
		px = rr[2];
		py = rr[3];		
		if (((px-x1)*(px-x2)<=0 || (py-y1)*(py-y2)<=0) && isPointInArc(px, py, x, y, r, sx, sy, ex, ey)){
			rr[size++] = px;
			rr[size++] = py;			
		}
						
		return size;		
	}
	
	/*public static double[] getLineSegCircleIntersection(double x1,double y1,double x2,double y2,double x,double y,double r){
		x2 -= x;
		x1 -= x;
		y2 -= y;
		y1 -= y;
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		
		double D = x1*y2-x2*y1;
		double sign = (dy<0) ? -1 :1;	
		double delta = r*r*dr*dr-D*D;
		if (delta<0) return null;
		
		
		delta = Math.sqrt(delta);
		double dr2 = dr*dr;		
		if (delta==0){
			double px1 = (D*dy)/dr2;
			double py1 = (-D*dx)/dr2;
			if (new Vector2D(px1-x1,py1-y1).dot(new Vector2D(px1-x2,py1-y2))<0){
				double[] result = new double[2];
				result[0] = px1+x;
				result[1] = py1+y;
				return result;
			}
			return null;
		}
		double px1 = (D*dy+sign*dx*delta)/dr2;
		double px2 = (D*dy-sign*dx*delta)/dr2;		
		double py1 = (-D*dx+Math.abs(dy)*delta)/dr2;
		double py2 = (-D*dx-Math.abs(dy)*delta)/dr2;
		DoubleArrayList result = new DoubleArrayList();
		if (new Vector2D(px1-x1,py1-y1).dot(new Vector2D(px1-x2,py1-y2))<0){			
			result.add(px1+x);
			result.add(py1+y);			
		}
		
		if (new Vector2D(px2-x1,py2-y1).dot(new Vector2D(px2-x2,py2-y2))<0){			
			result.add(px2+x);
			result.add(py2+y);			
		}
		
		return (result.size()==0) ? null : result.toDoubleArray();
	}//*/
	
	public static int getLineSegCircleIntersection(double x1,double y1,double x2,double y2,double x,double y,double r,double[] result){
		x2 -= x;
		x1 -= x;
		y2 -= y;
		y1 -= y;
		double dx = x2-x1;
		double dy = y2-y1;
		double dr = Math.sqrt(dx*dx+dy*dy);
		
		double D = x1*y2-x2*y1;
		double sign = (dy<0) ? -1 :1;	
		double delta = r*r*dr*dr-D*D;
		if (delta<0) return 0;
		
		
		delta = Math.sqrt(delta);
		double dr2 = dr*dr;		
		if (delta==0){
			double px1 = (D*dy)/dr2;
			double py1 = (-D*dx)/dr2;
			if ((px1-x1)* (px1-x2) + (py1-y1)*(py1-y2)<0){				
				result[0] = px1+x;
				result[1] = py1+y;
				return 2;
			}
			return 0;
		}
		double px1 = (D*dy+sign*dx*delta)/dr2;
		double px2 = (D*dy-sign*dx*delta)/dr2;		
		double py1 = (-D*dx+Math.abs(dy)*delta)/dr2;
		double py2 = (-D*dx-Math.abs(dy)*delta)/dr2;
		int sz = 0;
		if ((px1-x1)* (px1-x2) + (py1-y1)*(py1-y2)<0){			
			result[sz++] = px1+x;
			result[sz++] = py1+y;			
		}
		
		if ((px2-x1)*(px2-x) +(py2-y1)*(py2-y2)<0){			
			result[sz++] = px2+x;
			result[sz++] = py2+y;			
		}
		
		return sz;
	}
	
	public static double distance(double x1,double y1,double x2,double y2){
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	};
	
	public static double[] getCircleCircleIntersection(double x1,double y1,double r,double x2,double y2,double R){				
		double tx = x2-x1;
		double ty = y2-y1;
		double d = Math.sqrt(tx*tx+ty*ty);
		if (d>r+R) return null;
		double one_d = 1/d;
		tx*=one_d;
		ty*=one_d;		
		if (d==r+R){
			double[] rs = new double[2];			
			double px = x1 + tx*r;
			double py = y1 + ty*r;			
			rs[0] = px;
			rs[1] = py;
			return rs;
		}
		double a = (d*d+r*r-R*R)*0.5*one_d;
				
		double nx = -ty;
		double ny = tx;			
		double px = x1+tx*a;
		double py = y1+ty*a;
		double h = Math.sqrt(r*r-a*a);
		if (Double.isNaN(h)) return null;		
		double[] rs = new double[4];		
		rs[0] = px + nx*h;
		rs[1] = py + ny*h;
		rs[2] = px - nx*h;
		rs[3] = py - ny*h;
		return rs;
	}
	public static int getCircleCircleIntersection(double x1,double y1,double r,double x2,double y2,double R,double[] rs){				
		double tx = x2-x1;
		double ty = y2-y1;
		double d = Math.sqrt(tx*tx+ty*ty);
		if (d>r+R) return 0;
		double one_d = 1/d;
		tx*=one_d;
		ty*=one_d;		
		if (d==r+R){						
			double px = x1 + tx*r;
			double py = y1 + ty*r;			
			rs[0] = px;
			rs[1] = py;
			return 2;
		}
		double a = (d*d+r*r-R*R)*0.5*one_d;
				
		double nx = -ty;
		double ny = tx;			
		double px = x1+tx*a;
		double py = y1+ty*a;
		double h = Math.sqrt(r*r-a*a);
		if (Double.isNaN(h)) return 0;					
		rs[0] = px + nx*h;
		rs[1] = py + ny*h;
		rs[2] = px - nx*h;
		rs[3] = py - ny*h;
		return 4;
	}
	
	public static int getArcArcIntersection(double x1,double y1,double r,double s1x,double s1y,double e1x,double e1y,double x2,double y2,double R,double s2x,double s2y,double e2x,double e2y,double[] rs){
		double dx = x1-x2;
		double dy = y1-y2;
		double d = Math.sqrt(dx*dx+dy*dy);
		if (d>r+R) return 0;
		double tx = x2-x1;
		double ty = y2-y1;
		tx/=d;
		ty/=d;
		
		if (d==r+R){						
			double px = x1 + tx*r;
			double py = y1 + ty*r;		
			if (isPointInArc(px, py, x1, y1, r, s1x, s1y, e1x, e1y) && isPointInArc(px, py, x2, y2, R, s2x, s2y, e2x, e2y)){
				rs[0] = px;
				rs[1] = py;
				return 2;
			}
			return 0;
		}
		double a = (d*d+r*r-R*R)/(2*d);
				
		double nx = -ty;
		double ny = tx;			
		double px = x1+tx*a;
		double py = y1+ty*a;
		double h = Math.sqrt(r*r-a*a);
		if (Double.isNaN(h)) return 0;					
		double p1x = px + nx*h;
		double p1y = py + ny*h;
		double p2x = px - nx*h;
		double p2y = py - ny*h;
		int sz = 0;
		if (isPointInArc(p1x, p1y, x1, y1, r, s1x, s1y, e1x, e1y) && isPointInArc(p1x, p1y, x2, y2, R, s2x, s2y, e2x, e2y)){
			rs[sz++] = p1x;
			rs[sz++] = p1y;			
		}
		if (isPointInArc(p2x, p2y, x1, y1, r, s1x, s1y, e1x, e1y) && isPointInArc(p2x, p2y, x2, y2, R, s2x, s2y, e2x, e2y)){
			rs[sz++] = p2x;
			rs[sz++] = p2y;			
		}
		return sz;
	}
	
	public static double[] getArcCircleIntersection(double x1,double y1,double x2,double y2,double arc,double x,double y,double R){
		double d = distance(x1, y1, x, y);
		double r = distance(x1, y1, x2, y2);		
		if (d>r+R) return null;
		Vector2D t = new Vector2D(x-x1,y-y1).normalized();
		Vector2D s = new Vector2D(x2-x1,y2-y1);
		if (d==r+R){
			double a = s.angle(t);
			if (a*arc>0 && a/arc<1){
				double[] rs = new double[2];
				Vector2D p = new Vector2D(x1,y1).plus(t.times(r));
				rs[0] = p.x;
				rs[1] = p.y;
				return rs;
			}
		}
		double a = (d*d+r*r-R*R)/(2*d);
		
		Vector2D n = t.orthogonal();
		Vector2D point = new Vector2D(x1,y1).plus(t.times(a));
		double h = Math.sqrt(r*r-a*a);
		
		Vector2D p1 = point.plus(n.times(h));
		Vector2D p2 = point.plus(n.times(-h));		
		DoubleArrayList rs = new DoubleArrayList();
		Vector2D s1 = new Vector2D(p1.x()-x1,p1.y()-y1);		
		double a1 = s.angle(s1);		
		if (a1*arc>0 && a1/arc<1){
			rs.add(p1.x());
			rs.add(p1.y());
		}
		if (p1.equals(p2)) return (rs.size()==0) ? null : rs.toDoubleArray();
		Vector2D s2 = new Vector2D(p2.x()-x1,p2.y()-y1);
		double a2 = s.angle(s2);
		
		if (a2*arc>0 && a2/arc<1){
			rs.add(p2.x());
			rs.add(p2.y());
		}
		return (rs.size()==0) ? null : rs.toDoubleArray();
		
	}
	
	//calculate the point on the circle centre at (x0,y0) radius a which make a tangent line with x,y
	//x,y must be outside of the circle otherwise null is returned
	public static Vector2D[] ptTangentLine(double x,double y,double x0,double y0,double a){
		double dx = x0-x;
		double dy = y0-y;
		double d = dx*dx+dy*dy;
		if (d<=a*a) return null;
		
		double r = Math.sqrt(d - a*a);
		double[] p = getCircleCircleIntersection(x, y, r, x0, y0, a);
		if (p==null) return null;		
		Vector2D p1 = new Vector2D(p[0],p[1]);
		if (p.length<=2) return new Vector2D[]{p1};
		Vector2D p2 = new Vector2D(p[2],p[3]);
		return new Vector2D[]{p1,p2};
	}
	
	
	//calculate the point on the circle centre at (x0,y0) radius a which make a tangent line with x,y
	//x,y must be outside of the circle otherwise null is returned
	public static int ptTangentLine(double x,double y,double x0,double y0,double a,double[] rs){
		double dx = x0-x;
		double dy = y0-y;
		double d = dx*dx+dy*dy;
		if (d<=a*a) return 0;
		
		double r = Math.sqrt(d - a*a);
		return getCircleCircleIntersection(x, y, r, x0, y0, a,rs);		
	}
	
	
	public static void getCircle2(double ax0,double ay0,double bx0,double by0, double cx0,double cy0,double dx0,double dy0,double[] r){		 
		Object ret = Geom.getLineLineIntersection(ax0, ay0, bx0, by0, cx0, cy0, dx0, dy0, r);
		if (ret==Geom.INTERSECT && r!=null){
			double ox = r[0];
			double oy = r[1];			
			double ax = ax0-ox;
			double ay = ay0-oy;
			double bx = bx0-ox;
			double by = by0-oy;
			double da = ax*ax+ay*ay;
			double db = bx*bx+by*by;
			double dist = Math.sqrt(Math.sqrt(da*db));
			double cx = cx0-ox;
			double cy = cy0-oy;
			double l = Math.sqrt(cx*cx+cy*cy);
			if (l==0) {
				r[0] = 0;
				r[1] = 0;
				r[2] = 0;
			} else {
				double cl = dist/l;
				cx *=cl;
				cy *=cl;
				cx+=ox;
				cy+=oy;
				getCircle(ax0, ay0, bx0, by0,cx, cy, r);				
			}
		}		
	}
	
	//find a circle goes thru points a,b and touches the line c ,d
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public static void getCircle2(Vector2D a,Vector2D b, Vector2D c,Vector2D d,double[] r){		 
		Object ret = Geom.getLineLineIntersection(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y, r);
		if (ret==Geom.INTERSECT && r!=null){
			double ox = r[0];
			double oy = r[1];			
			double ax = a.x-ox;
			double ay = a.y-oy;
			double bx = b.x-ox;
			double by = b.y-oy;
			double da = ax*ax+ay*ay;
			double db = bx*bx+by*by;
			double dist = Math.sqrt(Math.sqrt(da*db));
			double cx = c.x-ox;
			double cy = c.y-oy;
			double l = Math.sqrt(cx*cx+cy*cy);
			if (l==0) {
				r[0] = 0;
				r[1] = 0;
				r[2] = 0;
				//return;
			} else {
				double cl = dist/l;
				cx *=cl;
				cy *=cl;
				cx+=ox;
				cy+=oy;
				getCircle(a.x, a.y, b.x, b.y,cx, cy, r);
				
			}
		}
		//return null;
	}
	
	//find a circle that goes through point a, touches the line a,b and  the line c ,d, in the direction towards d
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public static void getCircle3(Vector2D a,Vector2D b, Vector2D c,Vector2D d,double[] r){		 
		Object ret = Geom.getLineLineIntersection(a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y, r);
//		Vector2D rs = null;
		if (r!=null && ret==Geom.INTERSECT){
//			Vector2D o = new Vector2D(r[0],r[1]);
			double ox = r[0];
			double oy = r[1];
			double dax = ox-a.x;
			double day = oy-a.y;
			double da = dax*dax+day*day;
			
			double dx = d.x-ox;
			double dy = d.y-oy;
			double dd = dx*dx+dy*dy;
			double ratio = Math.sqrt(da/dd);
			double tx = ox + dx*ratio;
			double ty = oy + dy*ratio;
			double px = (a.x+tx)*0.5;
			double py = (a.y+ty)*0.5;
			double nx = oy-a.y;
			double ny = a.x-ox;			
//			Vector2D t = o.plus(d.minus(o).normalised().times(da));
//			Vector2D p = a.plus(t).times(0.5);
//			Vector2D n = a.minus(o).orthogonal();
			Geom.getLineLineIntersection(a.x, a.y, a.x+nx, a.y+ny, ox, oy, px, py, r);
//			rs = new Vector2D(r[0],r[1]);			
			r[2] = ptLineDistSq(ox, oy, a.x, a.y, r[0],r[1], null);
		}		
	}
	
	public static boolean getCircle3(double ax,double ay,double bx,double by,double cx,double cy,double dx,double dy,double[] r){		 
		Geom.getLineLineIntersection(ax, ay, bx, by, cx, cy, dx, dy, r);
//		Vector2D rs = null;
		if (r!=null){
//			Vector2D o = new Vector2D(r[0],r[1]);
			double ox = r[0];
			double oy = r[1];
			double dax = ox-ax;
			double day = oy-ay;
			double da = dax*dax+day*day;
			
			double ddx = dx-ox;
			double ddy = dy-oy;
			double dd = ddx*ddx+ddy*ddy;
			double ratio = Math.sqrt(da/dd);
			double tx = ox + ddx*ratio;
			double ty = oy + ddy*ratio;
			double px = (ax+tx)*0.5;
			double py = (ay+ty)*0.5;
			double nx = oy-ay;
			double ny = ax-ox;			
//			Vector2D t = o.plus(d.minus(o).normalised().times(da));
//			Vector2D p = a.plus(t).times(0.5);
//			Vector2D n = a.minus(o).orthogonal();
			if (Geom.getLineLineIntersection(ax, ay, ax+nx, ay+ny, ox, oy, px, py, r)==Geom.PARALLEL) return false;
//			rs = new Vector2D(r[0],r[1]);		
			r[2] = ptLineDistSq(ox, oy, ax, ay, r[0],r[1], null);
			return true;
		}		
		return false;
	}
	
	//find a circle that goes through point a, b and  touches the circle center,radius
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public final static int getCircle4(double Ax,double Ay,double Bx,double By, double cx,double cy,double rad,double[] r){
		Ax -= cx;
		Ay -= cy;
		Bx -= cx;
		By -= cy;
		double mx = (Ax+Bx)*0.5;
		double my = (Ay+By)*0.5;		
		double dx = mx - Ax;
		double dy = my - Ay;
		double m = Math.sqrt(dx*dx+dy*dy);
		double a = (Math.abs(Ax-Bx)<0.00001) ? 0 : (Bx-Ax)/(Ay-By);
		double b = my-a*mx;	
		double x0 = mx;
		double C = (rad+(m*m+x0*x0*(1+a*a)-b*b)/rad)*0.5;
		double D = (a*b+x0+a*a*x0)/rad;
		C -= D*x0;
		double cofA = (D*D-1-a*a);
		double cofB = -2*C*D;
		double cofC = C*C-m*m;
		double delta = cofB*cofB - 4*cofA*cofC;
		int j = 0;
		if (delta>=0){
			double u = (-cofB+Math.sqrt(delta))*0.5/cofA;
			double r1 = Math.abs(-C+D*u);			
			double x1 = x0+u;
			double y1 = a*x1+b;							
			if (Math.abs( Math.sqrt(x1*x1+y1*y1)-Math.abs(rad-r1))<0.001){
				r[0] = x1+cx;
				r[1] = y1+cy;
				r[2] = r1;
				j++;
			}
			if (delta>0){
				u = -(cofB+Math.sqrt(delta))*0.5/cofA;
				r1 = Math.abs(-C+D*u);
				x1 = x0 +u;
				y1 = a*x1+b;								
				if (Math.abs(Math.sqrt(x1*x1+y1*y1)-Math.abs(rad-r1))<0.001){
					r[j*3] = x1+cx;
					r[j*3+1] = y1+cy;
					r[j*3+2] = r1;
					j++;
				} 
			}		
		}
		return j;
	}
	
	//find a circle that goes through point a, b and  touches the circle center,radius
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public final static int getCircle4(final Vector2D a0,final Vector2D b0, final Vector2D center,double rad,double[] r){
		return getCircle4(a0.x,a0.y, b0.x,b0.y, center.x,center.y, rad, r);
	}
	
	
	//find a circle that goes through point a, b and  touches the circle center,radius
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public final static int getCircle5(double Ax,double Ay,double Bx,double By, double cx,double cy,double rad,double[] r){
		Ax -= cx;
		Ay -= cy;
		Bx -= cx;
		By -= cy;
		double mx = (Ax+Bx)*0.5;
		double my = (Ay+By)*0.5;		
		double dx = mx - Ax;
		double dy = my - Ay;
		double m = Math.sqrt(dx*dx+dy*dy);
		double a = (Math.abs(Ax-Bx)<0.00001) ? 0 : (Bx-Ax)/(Ay-By);
		double b = my-a*mx;	
		double x0 = mx;
		
		
		double C = (rad+(m*m+x0*x0*(1+a*a)-b*b)/rad)*0.5;
		double D = (a*b+x0+a*a*x0)/rad;
		C -= D*x0;
//		double v = C-D*u;
//		double pp = Math.sqrt(m*m+(1+a*a)*u*u);
//		System.out.println(v);
//		System.out.println((D*D-1-a*a)*u*u-2*C*D*u+C*C-m*m);
//		System.out.println(v*v-pp*pp);
		double cofA = (D*D-1-a*a);
		double cofB = -2*C*D;
		double cofC = C*C-m*m;
		double delta = cofB*cofB - 4*cofA*cofC;
		int j = 0;
		if (delta>=0){
			double u = (-cofB+Math.sqrt(delta))*0.5/cofA;
			double r1 = Math.abs(-C+D*u);			
			double x1 = x0+u;
			double y1 = a*x1+b;								
			if (Math.abs(Math.sqrt(x1*x1+y1*y1)-Math.abs(rad+r1))<0.001){
				r[0] = x1+cx;
				r[1] = y1+cy;
				r[2] = r1;
				j++;
			}
			if (delta>0){
				u = -(cofB+Math.sqrt(delta))*0.5/cofA;
				r1 = Math.abs(-C+D*u);
				x1 = x0 +u;
				y1 = a*x1+b;				
				if (Math.abs(Math.sqrt(x1*x1+y1*y1)-Math.abs(rad+r1))<0.001){
					r[j*3] = x1+cx;
					r[j*3+1] = y1+cy;
					r[j*3+2] = r1;
					j++;
				} 
			}		
		}
		return j;
	}

	//find a circle that goes through point a, b and  touches the circle center,radius
	//return the point on c,d that the circle goes thru a,b touches at
	//additional info stores on r(a circle centered at (r[0],r[1]) and radius squared r[2])
	public final static int getCircle5(Vector2D a0,Vector2D b0, Vector2D center,double rad,double[] r){
		return getCircle5(a0.x,a0.y, b0.x,b0.y, center.x,center.y, rad, r);
	}
	
	public final static int getCircleSpcial(double cx0,double cy0,double R,double x1,double y1,double tx,double ty,double[] rs){
		double x0 = cx0-x1;
		double y0 = cy0-y1;
		tx -=x1;
		ty -=y1;
		double a = ty/tx;
		double C = x0+a*y0;
		double D = x0*x0+y0*y0+R*R;
		double A = C*C-R*R*(1+a*a);
		double B = C*(D-2*R*R);
		double E = D*D*0.25-R*R*(x0*x0+y0*y0);
		double delta = B*B-4*A*E;
		if (delta<0) return 0 ;
		double x = 0;
		double twoA = 2*A;
		if (delta==0){
			x = B/twoA;
			rs[0] = x+x1;
			rs[1] = a*a+y1;
			return 2;
		}
		
		x = (B-Math.sqrt(delta))/twoA;
		double y = a*x;	
		double k = Math.sqrt(x*x+y*y);
		double dx = x-x0;
		double dy = y-y0;
		double d = Math.sqrt(dx*dx+dy*dy)-R;
		if (Math.abs(d-k)<0.5){
			rs[0] = x+x1;
			rs[1] = y+y1;
			x = (B+Math.sqrt(delta))/twoA;
			y = a*x;			
			rs[2] = x+x1;
			rs[3] = y+y1;			
		} else {		
			rs[2] = x+x1;
			rs[3] = y+y1;
			x = (B+Math.sqrt(delta))/twoA;
			y = a*x;
			rs[0] = x+x1;
			rs[1] = y+y1;			
		}
		return 4;
	}
	
	//Find a point on the line created by (ox,oy), (x2,y2) such that distance from that point to the line (ox,oy), (x1,y1) = distance to the point x0,y0
	public final static int findCenter(double ox,double oy,double x1,double y1,double x2,double y2,double x0,double y0,double[] tmp){
		//Find equation of (ox,oy)(x1,y1)		
		double b = ox - x1;
		double a = y1 - oy;
		double c = -(a*y1+b*x1);
		
		double a1 = (y2-oy)/(x2-ox);
		double b1 = y2 - a1*x2;
		
		double a_2 = a*a;
		double b_2 = b*b;
		double aplusb = a_2+b_2;
		double C = aplusb*x0+a*c;
		double D  = aplusb*y0+b*c;
		double E = aplusb*(x0*x0+y0*y0)-c*c;
		
		double A = a*a1+b;
		A*=A;
		double B = a_2*a1*b1-a*b*b1-C-a1*D;
		double F = a_2*b1*b1-2*b1*D+E;
		double delta = B*B-A*F;
		if (delta<0) return 0;
		double x = 0;
		double y = 0;
		if (delta==0){
			x = -B/A;
			y = a1*x+b1;
			tmp[0] = x;
			tmp[1] = y;
			return 2;
		}
		
		double sqrtDelta = Math.sqrt(delta);
		x = (-B-sqrtDelta)/A;
		y = a1*x+b1;
		tmp[0] = x;
		tmp[1] = y;
		x = (-B+sqrtDelta)/A;
		y = a1*x+b1;
		tmp[2] = x;
		tmp[3] = y;
		return 4;
	}
}