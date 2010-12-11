package solo;

import java.util.Arrays;

import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAFunction;
import jaolho.data.lma.LMAMatrix;
import jaolho.data.lma.LMAMultiDimFunction;
import jaolho.data.lma.LMAMatrix.InvertException;

public final class MyLMA extends LMA {
	static int len = 100;
	static int paramLen = 2;
	public static double[][] partialDerivative = new double[len][paramLen];
	public static double[] dy = new double[len];

	public MyLMA(LMAFunction function, double[] parameters,
			double[][] dataPoints) {
		super(function, parameters, dataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAFunction function, double[] parameters,
			double[][] dataPoints, double[] weights) {
		super(function, parameters, dataPoints, weights);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAFunction function, float[] parameters, float[][] dataPoints) {
		super(function, parameters, dataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAFunction function, float[] parameters,
			float[][] dataPoints, float[] weights) {
		super(function, parameters, dataPoints, weights);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, float[] parameters,
			float[][] dataPoints) {
		super(function, parameters, dataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, double[] parameters,
			double[][] dataPoints) {
		super(function, parameters, dataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, double[] parameters,
			float[] yDataPoints, float[][] xDataPoints) {
		super(function, parameters, yDataPoints, xDataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, double[] parameters,
			double[] yDataPoints, double[][] xDataPoints) {
		super(function, parameters, yDataPoints, xDataPoints);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, float[] parameters,
			float[][] dataPoints, float[] weights, LMAMatrix alpha) {
		super(function, parameters, dataPoints, weights, alpha);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, double[] parameters,
			double[][] dataPoints, double[] weights, LMAMatrix alpha) {
		super(function, parameters, dataPoints, weights, alpha);
		// TODO Auto-generated constructor stub
	}

	public MyLMA(LMAMultiDimFunction function, double[] parameters,
			double[] yDataPoints, double[][] xDataPoints, double[] weights,
			LMAMatrix alpha) {
		super(function, parameters, yDataPoints, xDataPoints, weights, alpha);
		// TODO Auto-generated constructor stub
	}

	@Override
	public final void fit() throws InvertException {
		iterationCount = 0;		
		if (len<yDataPoints.length || paramLen<parameters.length) {
			if (len<yDataPoints.length) {
				len = yDataPoints.length;
				dy = new double[len];
			}
			if (paramLen<parameters.length) paramLen = parameters.length;
			partialDerivative = new double[len][paramLen];
		}		
				
		double dChi2 = 0;
		int n = parameters.length;
		double result = 0;
		chi2 = 0;
		CircleFitter.changed = true;	
		for (int i = 0; i < yDataPoints.length; i++) {
			dy[i] = yDataPoints[i] - function.getY(xDataPoints[i], parameters);
			chi2 += weights[i] * dy[i] * dy[i];
		}
		if (Double.isNaN(chi2)) throw new RuntimeException("INITIAL PARAMETERS ARE ILLEGAL.");
		int noImprovementCount = 0;
		do {		
			for (int i = 0; i < yDataPoints.length; i++) {				
				for (int j = 0;j<n;++j)
					partialDerivative[i][j] = function.getPartialDerivate(xDataPoints[i], parameters, j); 
			}							
			if (verbose || CircleDriver2.time>=CircleDriver2.BREAK_TIME) System.out.println(iterationCount + ": chi2 = " + chi2 + ", " + Arrays.toString(parameters)+"   "+dChi2);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					result = 0;
					for (int k = 0; k < yDataPoints.length; k++) {
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
				for (int j = 0; j < yDataPoints.length; j++) {
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
				CircleFitter.changed = true;
				for (int i = 0; i < yDataPoints.length; i++) {
					dy[i] = yDataPoints[i] - function.getY(xDataPoints[i], parameters);					
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
					CircleFitter.changed = true;					
					for (int i = 0; i < yDataPoints.length; i++) 
						dy[i] = yDataPoints[i] - function.getY(xDataPoints[i], parameters);
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

	
}
