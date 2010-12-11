package jaolho.data.lma;

import jaolho.data.lma.LMAMatrix.InvertException;
import jaolho.data.lma.implementations.JAMAMatrix;

import java.util.Arrays;

import solo.CircleDriver2;


public abstract class QuickLMA<T> {
	boolean paramChanged = true;
	int size;
	static int len = 100;
	static int paramLen = 2;
	public static double[][] partialDerivative = new double[len][paramLen];
	public static double[] dy = new double[len];

	/** Set true to print details while fitting. */
	public boolean verbose = false;
	/** 
	 * The model function to be fitted, y = y(x[], a[]),
	 * where <code>x[]</code> the array of x-values and <code>a</code>
	 * is the array of fit parameters.
	 */
	
	/** 
	 * The array of fit parameters (a.k.a, the a-vector).
	 */
	public double[] parameters;
	/** 
	 * Measured y-data points for which the model function is to be fitted,
	 * yDataPoints[j] = y(xDataPoints[j], a[]).
	 */
	public double[] yDataPoints;
	/** 
	 * Measured x-data point arrays for which the model function is to be fitted,
	 * yDataPoints[j] = y(xDataPoints[j], a[]).
	 * xDataPoints.length must be equal to yDataPoints.length and
	 * xDataPoints[].length must equal to the fit function's dimension.
	 */
	public T[] xDataPoints;	
	/** 
	 * Weights for each data point. The merit function is:
	 * chi2 = Sum[(y_i - y(x_i;a))^2 * w_i].
	 * For gaussian errors in datapoints, set w_i = (sigma_i)^-2.
	 */
	public double[] weights;
	
	public LMAMatrix alpha;
	public double[] beta;
	public double[] da;
	public double lambda = 0.001;
	public double lambdaFactor = 10;
	public double incrementedChi2;	
	public int iterationCount;
	public double chi2;
	int from;
	int to;
	
	// default end conditions
	public double minDeltaChi2 = 1e-8;
	public int maxIterations = 15;
	
	public QuickLMA(double[] parameters, T[] x,double[]y,int from,int to) {		
		this.parameters = parameters;
		this.xDataPoints = x;
		this.yDataPoints = y;
		weights = new double[yDataPoints.length];
		Arrays.fill(weights, 1);
		this.from = from;
		this.to = to;
		size = to-from;
		beta = new double[parameters.length];
		da = new double[parameters.length];
		alpha = new JAMAMatrix(parameters.length, parameters.length);
		if (len<yDataPoints.length || paramLen<parameters.length) {
			if (len<yDataPoints.length) {
				len = yDataPoints.length;
				dy = new double[len];
			}
			if (paramLen<parameters.length) paramLen = parameters.length;
			partialDerivative = new double[len][paramLen];
		}
	}
	
	public QuickLMA(double[] parameters, T[] x,double[]y) {		
		this.parameters = parameters;
		this.xDataPoints = x;
		this.yDataPoints = y;
		weights = new double[yDataPoints.length];
		Arrays.fill(weights, 1);
		from = 0;
		to = y.length;
		size = to-from;
		beta = new double[parameters.length];
		da = new double[parameters.length];
		alpha = new JAMAMatrix(parameters.length, parameters.length);
		if (len<yDataPoints.length || paramLen<parameters.length) {
			if (len<yDataPoints.length) {
				len = yDataPoints.length;
				dy = new double[len];
			}
			if (paramLen<parameters.length) paramLen = parameters.length;
			partialDerivative = new double[len][paramLen];
		}
	}
	
	public QuickLMA(double[] parameters, T[] x,double[]y,double[] weights,int from,int to) {		
		this.parameters = parameters;
		this.xDataPoints = x;
		this.yDataPoints = y;
		this.weights = weights;
		this.from = from;
		this.to = to;
		size = to-from;
		beta = new double[parameters.length];
		da = new double[parameters.length];
		alpha = new JAMAMatrix(parameters.length, parameters.length);
		if (len<yDataPoints.length || paramLen<parameters.length) {
			if (len<yDataPoints.length) {
				len = yDataPoints.length;
				dy = new double[len];
			}
			if (paramLen<parameters.length) paramLen = parameters.length;
			partialDerivative = new double[len][paramLen];
		}
	}

	
	public QuickLMA(double[] parameters, T[] x,double[]y,double[] weights) {		
		this.parameters = parameters;
		this.xDataPoints = x;
		this.yDataPoints = y;
		this.weights = weights;
		from = 0;
		to = y.length;
		size = to-from;
		beta = new double[parameters.length];
		da = new double[parameters.length];
		alpha = new JAMAMatrix(parameters.length, parameters.length);
		if (len<yDataPoints.length || paramLen<parameters.length) {
			if (len<yDataPoints.length) {
				len = yDataPoints.length;
				dy = new double[len];
			}
			if (paramLen<parameters.length) paramLen = parameters.length;
			partialDerivative = new double[len][paramLen];
		}
	}
	

	public void fit() throws InvertException {
		iterationCount = 0;						
				
		double dChi2 = 0;
		int n = parameters.length;
		double result = 0;
		chi2 = 0;
		paramChanged = true;	
		for (int i = from; i < to; i++) {
			dy[i] = yDataPoints[i] - getY(xDataPoints[i], parameters);
			chi2 += weights[i] * dy[i] * dy[i];
		}
		if (Double.isNaN(chi2)) throw new RuntimeException("INITIAL PARAMETERS ARE ILLEGAL.");
		int noImprovementCount = 0;
		do {		
			for (int i = from; i < to; i++) {				
				for (int j = 0;j<n;++j)
					partialDerivative[i][j] = getPartialDerivate(xDataPoints[i], parameters, j); 
			}							
			if (verbose || CircleDriver2.time>=CircleDriver2.BREAK_TIME) System.out.println(iterationCount + ": chi2 = " + chi2 + ", " + Arrays.toString(parameters)+"   "+dChi2);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					result = 0;
					for (int k = from; k < to; k++) {
						result += 
							weights[k] * partialDerivative[k][i] * partialDerivative[k][j];							 
					}
					// Marquardt's lambda addition
					if (i == j) result *= (1 + lambda);
					alpha.setElement(i, j, result);
				}
			}
			
			for (int i = 0; i < n; i++) {
				result = 0;
				for (int j = from; j < to; j++) {
					result += 
						weights[j] * dy[j] * partialDerivative[j][i];						
				}
				beta[i] = result;
			}
			dChi2 = incrementedChi2 - chi2;
			try {
				alpha.invert(); // throws InvertException if matrix is singular
				alpha.multiply(beta, da);			
				
				for (int i=0;i<n;++i)
					if (Double.isNaN(da[i])) return;
				
				for (int i = 0; i < n; i++) 
					parameters[i] += da[i];
				
				incrementedChi2 = 0;
				paramChanged = true;
				for (int i = from; i < to; i++) {
					dy[i] = yDataPoints[i] - getY(xDataPoints[i], parameters);					
					// check if NaN occurred
					if (Double.isNaN(dy[i])){
						incrementedChi2 = Double.NaN;
						break;
					}
					incrementedChi2 += weights[i] * dy[i] * dy[i]; 
				}
				
				// The guess results to worse chi2 or NaN - make the step smaller
				boolean isNaN = Double.isNaN(incrementedChi2); 
				if (!isNaN){
					dChi2 = incrementedChi2 - chi2;					
				}
				if (dChi2>=0 || isNaN) {
					lambda *= lambdaFactor;
					for (int i = 0; i < n; i++) 
						parameters[i] -= da[i];
					paramChanged = true;					
					for (int i = from; i < to; i++) 
						dy[i] = yDataPoints[i] - getY(xDataPoints[i], parameters);
					noImprovementCount++;
				}
				// The guess results to better chi2 - move and make the step larger
				else {
					lambda /= lambdaFactor;
					chi2 = incrementedChi2;
					noImprovementCount = 0;
				}
			}
			catch (LMAMatrix.InvertException e) {
				// If the error happens on the last round, the fit has failed - throw the error out
				if (iterationCount == maxIterations) throw e;
				// otherwise make the step smaller and try again
//				if (verbose) {
//					System.out.println(e.getMessage());
//				}
				lambda *= lambdaFactor;
				noImprovementCount++;
				continue;
			}			
			iterationCount++;
		} while (!		(Math.abs(dChi2) < minDeltaChi2 || iterationCount > maxIterations || noImprovementCount>=5));		
	}
	
	
	public abstract double getY(T x,double[] params);
	public abstract double getPartialDerivate(T x,double[] params,int paramIndex);

}
