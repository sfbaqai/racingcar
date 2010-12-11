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
	    this.certain = vector2D.certain;
	    this.polar = vector2D.polar;
	}
	@Override	
	public final double getX() {
		// TODO Auto-generated method stub
		return x;
	}
	@Override
	public final double getY() {
		// TODO Auto-generated method stub
		return y;
	}
	@Override
	public final void setLocation(double x, double y) {
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
		this.x = x;
		this.y = y;		
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

	public final double x() { return x; }
	public final double y() { return y; }

	public final double angle() { return Math.atan2(y, x); }
	public final double length() { return Math.sqrt(x*x + y*y); }
	
	public final void copy(Vector2D v){	
		x = v.x;
		y = v.y;
		certain = v.certain;
	}
	
	public final void copyValue(Vector2D v){	
		x = v.x;
		y = v.y;		
	}
	
	public final void copy(Vector2D v,boolean cert){	
		x = v.x;
		y = v.y;
		certain = cert;
	}
	
	public final void copy(double vx,double vy){		
		x = vx;
		y = vy;				
	}
	
	public final void copy(double vx,double vy,boolean cert){		
		x = vx;
		y = vy;				
		certain = cert;
	}
	
	public final Vector2D scale(double xs,double ys) {
		return new Vector2D(xs * x, ys * y);
	}

	public final Vector2D plus(Vector2D vector) {
		return new Vector2D(x + vector.x, y + vector.y);
	}
	public final Vector2D minus(Vector2D vector) {
		return new Vector2D(x - vector.x, y - vector.y);
	}
	
	public final Vector2D times(double scaler) {
		return new Vector2D(scaler * x, scaler * y);
	}

	public final double dot(Vector2D that) {
		return this.x * that.x + this.y * that.y;
	}
	
	
	public final double det(Vector2D that) {
		return this.x * that.y - this.y * that.x;
	}
	
	public static final double angle(Vector2D a, Vector2D b)	{
		return -Math.atan2(b.y, b.x)+Math.atan2(a.y, a.x);
	}
	
	public static final double angle(double ax,double ay,double bx,double by)	{
		return -Math.atan2(by, bx)+Math.atan2(ay, ax);
	}

	
	public final double angle(Vector2D b)	{
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
	
	public final double angle(double bx,double by)	{
		return -Math.atan2(by, bx)+Math.atan2(y, x);
		}


	/**
	 * Rotates this vector around the origin.
	 *
	 * @param angle by which to rotate in degrees
	 */
	public final Vector2D rotated(double angle) {
		double sin = Math.sin(angle);
		double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(angle);
		if (cos==1.0) return new Vector2D(x,y);
		if (cos==-1.0) return new Vector2D(-x,-y);
		return new Vector2D(x * cos - y * sin, x * sin + y * cos);
	}
	
	public final void rotate(double angle,double cx,double cy) {
		double sin = Math.sin(angle);
		double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(angle);
		x -= cx;
		y -= cy;
		if (cos==-1.0) {
			x = -x;
			y = -y;
		} else if (cos!=1){
			double tx = x;
			x = x * cos - y * sin;
			y = tx * sin + y * cos;
		}		

		x+=cx;
		y+=cy;		
	}


	/**
	 * Projects this onto the given vector <code>normalisedOn</code>
	 * which assumed to be normalised.  The operation is <code>on *
	 * (this dot on)</code>.
	 */
	public final Vector2D projectedOn(Vector2D normalised) {
		return normalised.times(dot(normalised));
	}


	/**
	 * Compares the two x values and the then the two y values to see
	 * if they are identical.
	 */
	@Override
	public final boolean equals(Object thatObject) {
		if (thatObject instanceof Vector2D) {
			Vector2D that = (Vector2D) thatObject;
			return this.x == that.x && this.y == that.y;
		}
		else return false;
	}

	@Override
	public final int hashCode() { return (int) (x + y); }

	/**
	 * This returns the Vector2D that is orthogonal to this and to the
	 * left.  Technically, if there were a z axis defined by x-axis
	 * cross y-axis then the result of this function is z-axis cross
	 * this.
	 **/
	public final Vector2D orthogonal() { return new Vector2D(-y, x); }

	/**
	 * Produces a unit vector version of this.  In other words, a
	 * vector that points in the same direction but has a length of
	 * one.  This is the same as <code>normalised()</code>.
	 **/
	public final Vector2D normalized() {
		double x = this.x;
		double y = this.y;
		double length = Math.sqrt(x*x + y*y);
		if (length==0) return this;
		double d = 1.0d/length;		
		return new Vector2D(x*d, y*d);
	}
	/**
	 * Produces a unit vector version of this.  In other words, a
	 * vector that points in the same direction but has a length of
	 * one.  This is the same as <code>normalized()</code>.
	 **/
	public final Vector2D normalised() {
		double x = this.x;
		double y = this.y;
		double length = Math.sqrt(x*x + y*y);
		if (length==0) return this;
		double d = 1.0d/length;		
		return new Vector2D(x*d, y*d);
	}

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
	

	public static final Vector2D[] toVector2D(double[] x,double[] y){
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
	public final Object clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distance(java.awt.geom.Point2D)
	 */
	
	public final double distance(Vector2D arg0) {
		// TODO Auto-generated method stub
		double dx =x-arg0.x;
		double dy =y-arg0.y;
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	@Override
	public final double distance(Point2D arg0) {
		// TODO Auto-generated method stub
		double dx =x-arg0.getX();
		double dy =y-arg0.getY();
		return Math.sqrt(dx*dx+dy*dy);
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distance(double, double)
	 */
	@Override
	public final double distance(double arg0, double arg1) {
		// TODO Auto-generated method stub
		double dx =x-arg0;
		double dy =y-arg1;
		return Math.sqrt(dx*dx+dy*dy);
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distanceSq(java.awt.geom.Point2D)
	 */
	@Override
	public final double distanceSq(Point2D arg0) {
		// TODO Auto-generated method stub
		double dx =x-arg0.getX();
		double dy =y-arg0.getY();
		return dx*dx+dy*dy;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#distanceSq(double, double)
	 */
	@Override
	public final double distanceSq(double arg0, double arg1) {
		// TODO Auto-generated method stub
		double dx =x-arg0;
		double dy =y-arg1;
		return dx*dx+dy*dy;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Point2D#setLocation(java.awt.geom.Point2D)
	 */
	@Override
	public final void setLocation(Point2D arg0) {
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

