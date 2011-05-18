/**
 * 
 */
package solo;

import jaolho.data.lma.LMAFunction;

/**
 * @author kokichi3000
 *
 */
public final class LineFitter extends LMAFunction {
	
	private MyLMA cf = null;
	private double[] initialParams = null;
	public LineFitter(double[] initialGuess,double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[2][len];
		System.arraycopy(xx, 0, data[0], 0, len);
		System.arraycopy(yy, 0, data[1], 0, len);
		initialParams = initialGuess;
		cf = new MyLMA(this,initialGuess,data);		
	}
	
	public LineFitter(double[] initialGuess,Vector2D[] v){
		int len = v.length;
		double[][] data = new double[2][len];
		for (int i=0;i<len;++i){
			data[0][i]=v[i].x();
			data[1][i]=v[i].y();
		}
		initialParams = initialGuess;
		cf = new MyLMA(this,initialGuess,data);
	}
	
	public LineFitter(double[] initialGuess,double[] xx,double[] yy,int fromIndex){
		int len = xx.length;
		double[][] data = new double[2][len];
		System.arraycopy(xx, fromIndex, data[0], 0, len-fromIndex);
		System.arraycopy(yy, fromIndex, data[1], 0, len-fromIndex);				
		initialParams = initialGuess;
		cf = new MyLMA(this,initialGuess,data);
	}
	
	public LineFitter(double[] initialGuess,Vector2D[] v,int fromIndex,int endIndex){
		int len = endIndex-fromIndex+1;
		double[][] data = new double[2][len];		
		for (int i=0;i<len;++i){
			int k = i+fromIndex; 
			if (v[k]!=null) {
				data[0][i]=v[k].x;
				data[1][i]=v[k].y;
			}
		}
		initialParams = initialGuess;
		cf = new MyLMA(this,initialGuess,data);
	}

	
	
	public final void setData(Vector2D[] v){
		int len = v.length;
		double[][] data = new double[2][len];
		for (int i=0;i<len;++i){
			data[0][i]=v[i].x();
			data[1][i]=v[i].y();
		}		
		cf = new MyLMA(this,initialParams,data);
	}
		
	
	public final void setData(double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[2][len];
		System.arraycopy(xx, 0, data[0], 0, len);
		System.arraycopy(yy, 0, data[1], 0, len);		
		cf = new MyLMA(this,initialParams,data);
	}
	/**
	 * @return the initialParams
	 */
	public double[] getInitialParams() {
		return initialParams;
	}

	/**
	 * @param initialParams the initialParams to set
	 */
	public final void setInitialParams(double[] initialParams) {
		this.initialParams = initialParams;
	}

	public final double[] fit(){
		cf.maxIterations = 15;
		cf.minDeltaChi2 = 1e-8;
		cf.fit();		
		return cf.parameters.clone();
	}
	
	public final double[] fit(double lambda,double minDeltaChi2,int maxIterations){		
		cf.fit(lambda, minDeltaChi2, maxIterations);
		return cf.parameters.clone();
	}

	/**
	 * 
	 */
	public LineFitter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jaolho.data.lma.LMAFunction#getPartialDerivate(double, double[], int)
	 */
	@Override
	public final double getPartialDerivate(double x, double[] a, int parameterIndex) {
		// TODO Auto-generated method stub
		switch (parameterIndex) {
			case 0: return x;
			case 1: return 1;
		}
		throw new RuntimeException("No such parameter index: " + parameterIndex);

	}

	/* (non-Javadoc)
	 * @see jaolho.data.lma.LMAFunction#getY(double, double[])
	 */
	@Override
	public final double getY(double x, double[] a) {
		// TODO Auto-generated method stub
		return a[0] * x + a[1];
	}
	
	public final double getA(){
		return cf.parameters[0];
	}
	
	public final double getB(){
		return cf.parameters[1];
	}

}
