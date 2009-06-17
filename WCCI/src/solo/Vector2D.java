package solo;

import java.awt.geom.Point2D;

import com.graphbuilder.geom.Point2d;

public final class Vector2D extends java.awt.geom.Point2D implements Point2d,java.io.Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4297697783012622105L;
	public double x;
	public double y;
	public boolean certain = false;
	
	
	/**
	 * Copy Constructor
	 *
	 * @param vector2D a <code>Vector2D</code> object
	 */
	public Vector2D(Vector2D vector2D) 
	{
	    this.x = vector2D.x;
	    this.y = vector2D.y;
	    this.polar = vector2D.polar;
	}
	@Override	
	public double getX() {
		// TODO Auto-generated method stub
		return x;
	}
	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return y;
	}
	@Override
	public void setLocation(double x, double y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}

	boolean polar;      // at this point, this just prints it in polar
	
	public Vector2D(){
		x = 0;
		y = 0;
		polar = false;	
	}

	public Vector2D(double x, double y) {
		this(x, y, false);
	}

	/**
	 * @param x x coordinate or distance if polar == true
	 * @param y y coordinate or angle in radians if polar == true
	 * @param polar indicates if x and y should be interpreted as distance and angle
	 **/
	public Vector2D(double x, double y, boolean polar) {
		if (polar) {
			this.x = x * Math.cos(y);
			this.y = x * Math.sin(y);
		}
		else {
			this.x = x;
			this.y = y;
		}
	}

	public double x() { return x; }
	public double y() { return y; }

	public double angle() { return Math.atan2(y, x); }
	public double length() { return Math.sqrt(x*x + y*y); }
	
	public void copy(Vector2D v){		
		x = v.x;
		y = v.y;
	}
	
	public Vector2D scale(double xs,double ys) {
		return new Vector2D(xs * x, ys * y);
	}

	public Vector2D plus(Vector2D vector) {
		return new Vector2D(x + vector.x, y + vector.y);
	}
	public Vector2D minus(Vector2D vector) {
		return new Vector2D(x - vector.x, y - vector.y);
	}
	
	public Vector2D times(double scaler) {
		return new Vector2D(scaler * x, scaler * y);
	}

	public double dot(Vector2D that) {
		return this.x * that.x + this.y * that.y;
	}
	
	
	public double det(Vector2D that) {
		return this.x * that.y - this.y * that.x;
	}
	
	public static double angle(Vector2D a, Vector2D b)	{
//	  double cosine = (a.x * b.x + a.y * b.y) / Math.sqrt((a.x*a.x+a.y*a.y) * (b.x*b.x+b.y*b.y));
//	  // rounding errors might make dotproduct out of range for cosine
//	  if (cosine > 1) cosine = 1;
//	  else if (cosine < -1) cosine = -1;
//	 
//	  if ((a.x * b.y - a.y * b.x) < 0)
//	    return -Math.acos(cosine);
//	  else
//	    return Math.acos(cosine);
		return -Math.atan2(b.y, b.x)+Math.atan2(a.y, a.x);
	}
	
	public double angle(Vector2D b)	{
//		  double cosine = (x * b.x + y * b.y) / Math.sqrt((x*x+y*y) * (b.x*b.x+b.y*b.y));
//		  // rounding errors might make dotproduct out of range for cosine		  
//		  if (cosine > 1) cosine = 1;
//		  else if (cosine < -1) cosine = -1;
//		 
//		  if ((x * b.y - y * b.x) < 0)
//		    return -Math.acos(cosine);
//		  else
//		    return Math.acos(cosine);
		return -Math.atan2(b.y, b.x)+Math.atan2(y, x);
		}


	/**
	 * Rotates this vector around the origin.
	 *
	 * @param angle by which to rotate in degrees
	 */
	public Vector2D rotated(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return new Vector2D(x * cos - y * sin, x * sin + y * cos);
	}


	/**
	 * Projects this onto the given vector <code>normalisedOn</code>
	 * which assumed to be normalised.  The operation is <code>on *
	 * (this dot on)</code>.
	 */
	public Vector2D projectedOn(Vector2D normalised) {
		return normalised.times(dot(normalised));
	}


	/**
	 * Compares the two x values and the then the two y values to see
	 * if they are identical.
	 */
	@Override
	public boolean equals(Object thatObject) {
		if (thatObject instanceof Vector2D) {
			Vector2D that = (Vector2D) thatObject;
			return this.x == that.x && this.y == that.y;
		}
		else return false;
	}

	@Override
	public int hashCode() { return (int) (x + y); }

	/**
	 * This returns the Vector2D that is orthogonal to this and to the
	 * left.  Technically, if there were a z axis defined by x-axis
	 * cross y-axis then the result of this function is z-axis cross
	 * this.
	 **/
	public Vector2D orthogonal() { return new Vector2D(-y, x); }

	/**
	 * Produces a unit vector version of this.  In other words, a
	 * vector that points in the same direction but has a length of
	 * one.  This is the same as <code>normalised()</code>.
	 **/
	public Vector2D normalized() {
		double length = length();
		return length > 0.0 ? times(1.0d / length) : this;
	}
	/**
	 * Produces a unit vector version of this.  In other words, a
	 * vector that points in the same direction but has a length of
	 * one.  This is the same as <code>normalized()</code>.
	 **/
	public Vector2D normalised() { return normalized(); }

	/**
	 * A vector of the same length in the opposite direction.  "That's
	 * it, we'll reverse the polarity from minus to plus and plus to
	 * minus!".
	 **/
	public Vector2D negated() {
		double xx =  (x==0) ? 0 : -x;
		double yy = (y==0) ? 0 : -y;
		return new Vector2D(xx, yy); 
	}
	

	public static Vector2D[] toVector2D(double[] x,double[] y){
		if (x==null || y==null || x.length!=y.length || x.length==0)
			return null;
		int len = x.length;
		Vector2D[] v = new Vector2D[len];
		for (int i=0;i<len;++i)
			v[i] = new Vector2D(x[i],y[i]);
		return v;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#clone()
	 */
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distance(java.awt.geom.Point2D)
	 */
	@Override
	public double distance(Point2D arg0) {
		// TODO Auto-generated method stub
		double dx =x-arg0.getX();
		double dy =y-arg0.getY();
		return Math.sqrt(dx*dx+dy*dy);
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distance(double, double)
	 */
	@Override
	public double distance(double arg0, double arg1) {
		// TODO Auto-generated method stub
		double dx =x-arg0;
		double dy =y-arg1;
		return Math.sqrt(dx*dx+dy*dy);
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distanceSq(java.awt.geom.Point2D)
	 */
	@Override
	public double distanceSq(Point2D arg0) {
		// TODO Auto-generated method stub
		double dx =x-arg0.getX();
		double dy =y-arg0.getY();
		return dx*dx+dy*dy;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distanceSq(double, double)
	 */
	@Override
	public double distanceSq(double arg0, double arg1) {
		// TODO Auto-generated method stub
		double dx =x-arg0;
		double dy =y-arg1;
		return dx*dx+dy*dy;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(java.awt.geom.Point2D)
	 */
	@Override
	public void setLocation(Point2D arg0) {
		// TODO Auto-generated method stub
		super.setLocation(arg0);
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
	    final String TAB = ",";
	    
	    String retValue = "";
	    
	    retValue = "("+this.x + TAB
	        + this.y  
	        + ") ";
	
	    return retValue;
	}

	
}

