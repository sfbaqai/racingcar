package jaolho.data.lma;

import solo.Vector2D;

public final class QuickCircleFitter extends QuickLMA<Vector2D> {	
	private double avrgR = 0;
	private double dJdx = 0;
	private double dJdy = 0;
	/**
	 * @param parameters
	 * @param x
	 * @param y
	 * @param weights
	 * @param from
	 * @param to
	 */
	public QuickCircleFitter(double[] parameters, Vector2D[] x, double[] y,
			double[] weights, int from, int to) {
		super(parameters, x, y, weights, from, to);
		// TODO Auto-generated constructor stub
	}
	
	public QuickCircleFitter(double[] parameters, Vector2D[] x, int from, int to) {		
		super(parameters, x, new double[to], from, to);
		
	}
	
	public QuickCircleFitter(double[] parameters, Vector2D[] x) {		
		super(parameters, x, new double[x.length]);
		
	}

	/**
	 * @param parameters
	 * @param x
	 * @param y
	 * @param weights
	 */
	public QuickCircleFitter(double[] parameters, Vector2D[] x, double[] y,
			double[] weights) {
		super(parameters, x, y, weights);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 * @param x
	 * @param y
	 * @param from
	 * @param to
	 */
	public QuickCircleFitter(double[] parameters, Vector2D[] x, double[] y,
			int from, int to) {
		super(parameters, x, y, from, to);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 * @param x
	 * @param y
	 */
	public QuickCircleFitter(double[] parameters, Vector2D[] x, double[] y) {
		super(parameters, x, y);
		// TODO Auto-generated constructor stub
	}

	private double getAverageR(double[] params){
		if (!paramChanged) return avrgR;
		double total = 0;
		double totalX = 0;
		double totalY = 0;			
		
		for (int i=from;i<to;++i){
			double x  = params[0]-xDataPoints[i].x;
			double y  = params[1]-xDataPoints[i].y;
			double d = Math.sqrt(x*x+y*y);
			totalX +=  x/d;
			totalY +=  y/d;
			total +=  d;
		}
		avrgR = total/size;
		dJdx = totalX/size;
		dJdy = totalY/size;
		paramChanged = false;
		return avrgR;
	}

	
	@Override
	public double getPartialDerivate(Vector2D xInput, double[] params, int paramIndex) {
		if (paramIndex==2)
			return -1;		
		double dx = params[0]-xInput.x;
		double dy = params[1]-xInput.y;
		double d = Math.sqrt(dx*dx+dy*dy);
		if (paramChanged){
			double total = 0;
			double totalX = 0;
			double totalY = 0;			
			
			for (int i=from;i<to;++i){
				double x  = params[0]-xDataPoints[i].x;
				double y  = params[1]-xDataPoints[i].y;
				double dd = Math.sqrt(x*x+y*y);
				totalX +=  x/dd;
				totalY +=  y/dd;
				total +=  dd;
			}
			avrgR = total/size;
			dJdx = totalX/size;
			dJdy = totalY/size;
			paramChanged = false;
		}
		switch (paramIndex){
//		case 0:return dx/d;
//		case 1:return dy/d;
		case 0:return dx/d-dJdx;
		case 1:return dy/d-dJdy;		
		}
		return -1;
	}

	@Override
	public double getY(Vector2D x, double[] params) {
		// TODO Auto-generated method stub
		double dx = x.x-params[0];
		double dy = x.y-params[1];
		return Math.sqrt(dx*dx+dy*dy)-getAverageR(params);

	}

	public final Vector2D getEstimatedCenter(){
		return new Vector2D(parameters[0],parameters[1]);
	}
	
	public final double getEstimatedRadius(){
		return getAverageR(parameters);
	}

}
