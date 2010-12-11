package jaolho.data.lma;

import solo.Vector2D;

public final class QuickLineFitter extends QuickLMA<Vector2D> {		
	/**
	 * @param parameters
	 * @param x
	 * @param y
	 * @param weights
	 * @param from
	 * @param to
	 */
	public QuickLineFitter(double[] parameters, Vector2D[] x, double[] y,
			double[] weights, int from, int to) {
		super(parameters, x, y, weights, from, to);
		// TODO Auto-generated constructor stub
	}
	
	public QuickLineFitter(double[] parameters, Vector2D[] x, int from, int to) {		
		super(parameters, x, new double[to], from, to);
		
	}
	
	public QuickLineFitter(double[] parameters, Vector2D[] x) {		
		super(parameters, x, new double[x.length]);
		
	}

	/**
	 * @param parameters
	 * @param x
	 * @param y
	 * @param weights
	 */
	public QuickLineFitter(double[] parameters, Vector2D[] x, double[] y,
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
	public QuickLineFitter(double[] parameters, Vector2D[] x, double[] y,
			int from, int to) {
		super(parameters, x, y, from, to);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 * @param x
	 * @param y
	 */
	public QuickLineFitter(double[] parameters, Vector2D[] x, double[] y) {
		super(parameters, x, y);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public double getPartialDerivate(Vector2D xInput, double[] params, int paramIndex) {			
		switch (paramIndex){
		case 0:return xInput.x;
		case 1:return 1;		
		}
		return 1;
	}

	@Override
	public double getY(Vector2D x, double[] params) {
		// TODO Auto-generated method stub			
		return x.y - (params[0]*x.x+params[1]);

	}

	public final double getA(){
		return parameters[0];
	}
	
	public final double getB(){
		return parameters[1];
	}

}
