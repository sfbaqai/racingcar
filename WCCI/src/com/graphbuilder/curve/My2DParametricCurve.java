/**
 * 
 */
package com.graphbuilder.curve;

/**
 * @author kokichi3000
 *
 */
public abstract class My2DParametricCurve extends ParametricCurve {

	public My2DParametricCurve(ControlPath cp, GroupIterator gp) {
		super(cp, gp);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.graphbuilder.curve.ParametricCurve#eval(double[])
	 */	
	abstract public double[] eval(double t);
	abstract public double[] df(double t);
	abstract public double[] ddf(double t);
	public final double radiusAt(double t){
		double[] p = df(t);
		double dx = p[0];
		double dy = p[1];
		p = ddf(t);
		double ddx = p[0];
		double ddy = p[1];
		double d = dx*dx+dy*dy;
		return Math.sqrt(d*d*d)/Math.abs(dx*ddy-dy*ddx);
	}
	
	public final double[] osculatingCircleAt(double t){
		double r[] = new double[3];	
		double[] p = df(t);
		double dx = p[0];
		double dy = p[1];
		p = ddf(t);
		double ddx = p[0];
		double ddy = p[1];
		double d = dx*dx+dy*dy;
		p = eval(t);
		double f = p[0];
		double g = p[1];
		double z  = dx*ddy-dy*ddx;
		r[2] = Math.sqrt(d*d*d)/Math.abs(z);		
		r[0] = f - d*dy/z;
		r[1] = g + d*dx/z;
		return r;
	}

}
