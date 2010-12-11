/**
 * 
 */
package com.graphbuilder.curve;

import com.graphbuilder.math.PascalsTriangle;

/**
 * @author kokichi3000
 *
 */
public class MyBezierCurve extends My2DParametricCurve {


	// a[] is required to compute (1 - t)^n starting from the last index.
	// The idea is that all Bezier curves can share the same array, which
	// is more memory efficient than each Bezier curve having its own array.
	private static double[] a = new double[0];
	private static double[] b = new double[0];
	private static double[][] q = null;

	private double t_min = 0.0;
	private double t_max = 1.0;
	private int sampleLimit = 1;

	/**
	 * @param cp
	 * @param gi
	 */
	public MyBezierCurve(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void eval(double[] p) {
		double t = p[p.length - 1];
		p[0] = 0;
		p[1] = 0;
		int numPts = gi.getGroupSize();

		if (a==null || numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;
		int n = numPts -1;	
		b = B(n,a,t);
		gi.set(0, 0);
		
		for  (int i =0;i<=n;++i){			
			double[] d = cp.getPoint(gi.next()).getLocation();
			for (int j = 0; j < p.length - 1; j++)
				p[j] += d[j] * b[i];
		}

	}
		
	public double[][] Q(int n){
		if (q==null || q.length<n) q = new double[n][];
		gi.set(0, 0);
		double[] old = null;
		for  (int i =0;i<n;++i){
			double[] d = cp.getPoint(gi.next()).getLocation();
			if (old==null){
				old = d;
				d = cp.getPoint(gi.next()).getLocation();
			}
			if (q[i]==null || q[i].length<d.length) q[i] = new double[d.length];
			for (int j = 0;j<d.length;++j)
				q[i][j] = n*(d[j]-old[j]);
			old = d;
		}
		return q;
	}
	
	public void df(double[] p) {
		double t = p[p.length - 1];

		int numPts = gi.getGroupSize();

		if (numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;

		int i = 0;
		q = Q(numPts-1);
		b = B(numPts-2,a,t);
		for  (i =0;i<numPts-1;++i){			
			for (int j = 0; j < p.length - 1; j++)
				p[j] += q[i][j] * b[i];
		}

	}
	
	
	public double[] B(int n,double[] a,double t){
		if (b==null || b.length!=n+1) b = new double[n+1];
		double tt = 1;		
		
		for (int i = 0;i<=n;++i){
			b[i] = PascalsTriangle.nCr(n, i)*tt*a[n-i];
			tt*=t;
			
		}
		return b;
	}
	
	public void ddf(double[] p) {
		double t = p[p.length - 1];

		int numPts = gi.getGroupSize();

		if (numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;

		int i = 0;
		q = Q(numPts-1);
		b = B(numPts-3,a,t);
		for  (i =0;i<numPts-2;++i){			
			for (int j = 0; j < p.length - 1; j++)
				p[j] += (q[i+1][j]-q[i][j]) * b[i]*(numPts-2);
		}
	}


	@Override
	public int getSampleLimit() {
		return sampleLimit;
	}

	/**
	Sets the sample-limit.  For more information on the sample-limit, see the
	BinaryCurveApproximationAlgorithm class.  The default sample-limit is 1.

	@throws IllegalArgumentException If sample-limit < 0.
	@see com.graphbuilder.curve.BinaryCurveApproximationAlgorithm
	@see #getSampleLimit()
	*/
	public void setSampleLimit(int limit) {
		if (limit < 0)
			throw new IllegalArgumentException("Sample-limit >= 0 required.");

		sampleLimit = limit;
	}

	/**
	Specifies the interval that the curve should define itself on.  The default interval is [0.0, 1.0].

	@throws IllegalArgumentException If t_min > t_max.
	@see #t_min()
	@see #t_max()
	*/
	public void setInterval(double t_min, double t_max) {
		if (t_min > t_max)
			throw new IllegalArgumentException("t_min <= t_max required.");

		this.t_min = t_min;
		this.t_max = t_max;
	}

	/**
	Returns the starting interval value.

	@see #setInterval(double, double)
	@see #t_max()
	*/
	public double t_min() {
		return t_min;
	}

	/**
	Returns the finishing interval value.

	@see #setInterval(double, double)
	@see #t_min()
	*/
	public double t_max() {
		return t_max;
	}

	/**
	The only requirement for this curve is the group-iterator must be in range or this method returns quietly.
	*/
	@Override
	public void appendTo(MultiPath mp) {
		if (!gi.isInRange(0, cp.numPoints())) return;

		int n = mp.getDimension();

		double[] d = new double[n + 1];
		d[n] = t_min;
		eval(d);

		if (connect)
			mp.lineTo(d);
		else
			mp.moveTo(d);

		BinaryCurveApproximationAlgorithm.genPts(this, t_min, t_max, mp);
	}

	@Override
	public void resetMemory() {
		if (a.length > 0)
			a = new double[0];
	}

	@Override
	public double[] ddf(double t) {
		// TODO Auto-generated method stub
		double[] p = new double[3];

		int numPts = gi.getGroupSize();

		if (numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;

		int i = 0;
		q = Q(numPts-1);
		b = B(numPts-3,a,t);
		for  (i =0;i<numPts-2;++i){			
			for (int j = 0; j < p.length - 1; j++)
				p[j] += (q[i+1][j]-q[i][j]) * b[i]*(numPts-2);
		}
		return p;
	}

	@Override
	public double[] df(double t) {
		// TODO Auto-generated method stub
		double[] p = new double[3];
		int numPts = gi.getGroupSize();

		if (numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;

		int i = 0;
		q = Q(numPts-1);
		b = B(numPts-2,a,t);
		for  (i =0;i<numPts-1;++i){			
			for (int j = 0; j < p.length - 1; j++)
				p[j] += q[i][j] * b[i];
		}
		return p;
	}

	@Override
	public double[] eval(double t) {
		// TODO Auto-generated method stub
		double[] p = new double[3];		
		int numPts = gi.getGroupSize();

		if (a==null || numPts > a.length)
			a = new double[2 * numPts];

		a[0] = 1;		
		double one_minus_t = 1.0 - t;

		for (int i = 1; i < numPts;++i)
			a[i] = a[i-1] * one_minus_t;
		int n = numPts -1;	
		b = B(n,a,t);
		gi.set(0, 0);
		
		for  (int i =0;i<=n;++i){			
			double[] d = cp.getPoint(gi.next()).getLocation();
			for (int j = 0; j < p.length - 1; j++)
				p[j] += d[j] * b[i];
		}
		return p;
	}

}
