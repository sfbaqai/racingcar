package com.graphbuilder.geom;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
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
		double sx = x1 + (ax - qo * ay) / 2; // found center of circle
		double sy = y1 + (ay + qo * ax) / 2; // (sx, sy)

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

		double a = (ax * ax + ay * ay) / 2;
		double b = (bx * bx + by * by) / 2;
		double c = (cx * cx + cy * cy) / 2;

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

		double d = (a + (b - c)) / 2;
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
	
	public static double[] getLineSegCircleIntersection(double x1,double y1,double x2,double y2,double x,double y,double r){
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
	}
	
	public static double distance(double x1,double y1,double x2,double y2){
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	};
	
	public static double[] getCircleCircleIntersection(double x1,double y1,double r,double x2,double y2,double R){
		double d = distance(x1, y1, x2, y2);
		if (d>r+R) return null;
		Vector2D t = new Vector2D(x2-x1,y2-y1).normalized();
		if (d==r+R){
			double[] rs = new double[2];
			Vector2D p = new Vector2D(x1,y1).plus(t.times(r));
			rs[0] = p.x();
			rs[1] = p.y();
			return rs;
		}
		double a = (d*d+r*r-R*R)/(2*d);
		
		Vector2D n = t.orthogonal();
		Vector2D point = new Vector2D(x1,y1).plus(t.times(a));
		double h = Math.sqrt(r*r-a*a);
		
		Vector2D p1 = point.plus(n.times(h));
		Vector2D p2 = point.plus(n.times(-h));
		double[] rs = new double[4];		
		rs[0] = p1.x();
		rs[1] = p1.y();
		rs[2] = p2.x();
		rs[3] = p2.y();
		return rs;
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
				rs[0] = p.x();
				rs[1] = p.y();
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
		Vector2D p2 = new Vector2D(p[2],p[3]);
		return new Vector2D[]{p1,p2};
	}
}