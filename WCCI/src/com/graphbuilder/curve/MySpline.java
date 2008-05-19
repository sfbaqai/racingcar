/**
 * 
 */
package com.graphbuilder.curve;

import solo.Vector2D;

/**
 * @author kokichi3000
 *
 */
class MySpline extends NaturalCubicSpline {

	/**
	 * @param cp
	 * @param gi
	 */
	int n;//number of points
	
	public MySpline(ControlPath cp, GroupIterator gi) {
		super(cp, gi);
		n = gi.getGroupSize();		
		// TODO Auto-generated constructor stub
	}

	public double[] eval(double t, int ci) {
		double[] p = new double[2];
		double[][] data = getData();
		int n = 2; // dimension				
		double t2 = t * t;
		double t3 = t2 * t;		
		int j = 0;
		for (int i = 0; i < n; i++)
			p[i] = data[j++][ci] + data[j++][ci] * t + data[j++][ci] * t2 + data[j++][ci] * t3;
		return p;
	}
	
	public Vector2D evaluate(double t, int ci) {
		double[] p = new double[2];
		double[][] data = getData();
		int n = 2; // dimension				
		double t2 = t * t;
		double t3 = t2 * t;		
		int j = 0;
		for (int i = 0; i < n; i++)
			p[i] = data[j++][ci] + data[j++][ci] * t + data[j++][ci] * t2 + data[j++][ci] * t3;
		return new Vector2D(p[0],p[1]);
	}
	
	public double firstDerivative(double t, int ci) {
		double[] p = new double[2];
		double[][] data = getData();
		int n = 2; // dimension				
		double t2 = t * t;
				
		int j = 0;
		for (int i = 0; i < n; i++){
			j++;
			p[i] = data[j++][ci] + 2*data[j++][ci] * t + 3*data[j++][ci] * t2;
		}
		return (p[0]==0)? Double.POSITIVE_INFINITY :  p[1]/p[0];
	}
	
	public double secondDerivative(double t, int ci) {
		double[] p = new double[2];
		double[][] data = getData();
		int n = 2; // dimension						
				
		int j = 0;
		for (int i = 0; i < n; i++){
			j++;
			j++;
			p[i] =  2*data[j++][ci] + 6*data[j++][ci] * t;
		}
		return (p[0]==0)? Double.POSITIVE_INFINITY :  p[1]/p[0];
	}
	
}
