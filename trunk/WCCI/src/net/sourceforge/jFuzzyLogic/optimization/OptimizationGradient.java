package net.sourceforge.jFuzzyLogic.optimization;

import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Paramtric Gradient optimization for fuzzy rule sets
 * 
 * Tries to optimize parameters (fuzzy sets parameters and rule's weights) using a 'gradient' algorithm.
 * Gradient optimization: Simply calculates gradient and tries to optimize in that direction using a 'line search' algorithm.
 * 
 * @author pcingola@users.sourceforge.net
 */
public class OptimizationGradient {

	/** Alpha for line iterations */
	public static double alphaLineIterations;
	/** Some counuters for statistics */
	public static int countGradientNormTooSmall, countImpovement, countNoImpovement, countMaxIterations, countOptimizations, countIterations,
			countLineIterations, countGoodLineIterations, countBadLineIterations;
	/** Debug mode? */
	public static boolean debug = false;
	/** Max number of iterations (for gradient optimization) */
	private static int DEFAULT_MAX_ITERATIONS = 20;
	/** Maximum 'line search' number of iterations */
	private static int DEFAULT_MAX_LINE_SEARCH_ITERATIONS = 20;
	/** A small number */
	private static double EPSILON = 0.0000001;

	/** Error function to minimize */
	ErrorFunction errorFunction;
	/** Fuzzy rule set's whose parameters we are optimizing */
	FuzzyRuleSet fuzzyRuleSet;
	/** Max number of iterations (for gradient optimization) */
	int maxIterations;
	/** Maximum 'line search' number of iterations */
	int maxLineSearchIterations;
	/** Parameters to optimize */
	ArrayList<Parameter> parameterList;

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	public OptimizationGradient(FuzzyRuleSet fuzzyRuleSet, ErrorFunction errorFunction, ArrayList<Parameter> parameterList) {
		this.fuzzyRuleSet = fuzzyRuleSet;
		this.errorFunction = errorFunction;
		this.parameterList = parameterList;
		maxLineSearchIterations = DEFAULT_MAX_LINE_SEARCH_ITERATIONS;
		maxIterations = DEFAULT_MAX_ITERATIONS;
	}

	//-------------------------------------------------------------------------
	// Static Methods
	//-------------------------------------------------------------------------

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		OptimizationGradient.debug = debug;
	}

	public static String stats() {
		String stats = "\tTotal Optimizations: " + countOptimizations//
				+ "\n\tGradient Norm Too Small (return cause): " + countGradientNormTooSmall//
				+ "\n\tNo Impovement  (return cause): " + countNoImpovement //
				+ "\n\tMax iterations (return cause): " + countMaxIterations //
				+ "\n\tImpovement (iteration): " + countImpovement //
				+ "\n\tcountIterations: " + countIterations + "\tgood: " + countGoodLineIterations + " / bad: " + countBadLineIterations //
				+ "\n\tAverage good alpha: " + (alphaLineIterations / countGoodLineIterations) //
				+ "\n\tcountLineIterations: " + countLineIterations;
		return stats;
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/**
	 * parameterList ===> parameterValues
	 * @param parameterValues
	 */
	private void copyFromParameterList(double parameterValues[]) {
		for( int j = 0; j < parameterValues.length; j++ )
			parameterValues[j] = parameterList.get(j).get();
	}

	/**
	 * parameterList <=== parameterValues
	 * @param parameterValues
	 */
	private void copyToParameterList(double parameterValues[]) {
		for( int j = 0; j < parameterValues.length; j++ )
			parameterList.get(j).set(parameterValues[j]);
	}

	public ErrorFunction getErrorFunction() {
		return errorFunction;
	}

	public FuzzyRuleSet getFuzzyRuleSet() {
		return fuzzyRuleSet;
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public int getMaxLineSearchIterations() {
		return maxLineSearchIterations;
	}

	public ArrayList<Parameter> getParameterList() {
		return parameterList;
	}

	/**
	 * Estimate gradient
	 * @param portfolioOriginal Portfolio (estimate portfolioOriginal's gradient to weigths)
	 * @param gradient An array where results are stored
	 * @param ev0 funtion evaluated at W0 (starting point to calculate gradient)
	 * @return Gradient's norm (and sets gradient[])
	 */
	private double gradient(double gradient[], double ev0) {
		double norm = 0;
		double e0 = errorFunction.evaluate(fuzzyRuleSet); // Calculate error funtion's "gradient"

		//---
		// For each dimention, estimate partial derivate
		//---
		for( int i = 0; i < parameterList.size(); i++ ) {
			Parameter param = parameterList.get(i);
			double epsilon = param.getEpsilon();
			double paramValue = param.get();

			if( param.set(paramValue + epsilon) ) ; // Increase parameter (in this dimention) to estimate derivate 
			else if( param.set(paramValue - epsilon) ) epsilon *= -1; // Cant' increase? => Decrease parameter (in this dimention) to estimate derivate 
			else epsilon = 0; // Cant' increase / decrease => no change

			double e1;
			if( epsilon != 0 ) {
				// Parameter changed? => re evaluate
				e1 = errorFunction.evaluate(fuzzyRuleSet);
				// Restore old parameter's value
				param.set(paramValue);
			} else e1 = e0; // No change

			// Partial derivate
			gradient[i] = (e0 - e1) / epsilon;
			Gpr.debug(debug, "Parameter:" + param.getName() + "\tDerivate: " + gradient[i] + "\tepsilon: " + epsilon);

			norm += gradient[i] * gradient[i]; // Calculate gradient's norm
		}

		// Return gradient's norm
		return norm;
	}

	/**
	 * Gradient optimization for error functions
	 * Gradient descent algorithm
	 * It also does a line search to get a good 'next point' on each iteration
	 */
	public void optimize() {
		int i, j, k, len;
		len = parameterList.size();
		double gradient[] = new double[len]; // Gradient
		double alpha, norm = 0, error, error0;
		double parameterValues0[] = new double[len];

		countOptimizations++;

		//---
		// Iterate gradient descent
		//---
		for( i = 0; i < maxIterations; i++ ) {
			countIterations++;
			// Calculate error funtion at 'X_0' (starting point)
			error0 = errorFunction.evaluate(fuzzyRuleSet);
			Gpr.debug(debug, "Iteration: " + i + "\tError: " + error0);

			// Save initial parameter values
			copyFromParameterList(parameterValues0);

			// Estimate gradient
			norm = gradient(gradient, error0);

			// Norm too small? => Abort
			if( norm <= EPSILON ) {
				// Restore initial values and quit
				copyToParameterList(parameterValues0);
				countGradientNormTooSmall++;
				Gpr.debug(debug, "Gradient's norm too small => finished (norm: " + norm + ")");
				return;
			}

			//---
			// Line search: Iterate until we get a suitable alpha (gradient's step size)
			//---
			alpha = 1; // We set initial alpha value to 1.0 and 
			for( k = 0; k < maxLineSearchIterations; k++ ) {
				countLineIterations++;

				// Advance against gradient's direction (step = -alpha)
				for( j = 0; j < len; j++ )
					parameterList.get(j).set(parameterValues0[j] - alpha * gradient[j]);

				// Evaluate new point (is it better than before?)
				error = errorFunction.evaluate(fuzzyRuleSet);
				Gpr.debug(debug, "\tLine tteration: " + k + "\talpha: " + alpha + "\tDelta_Error: " + (error - error0));
				if( error < error0 ) {
					alphaLineIterations += alpha;
					countGoodLineIterations++;
					break; // Better? => Continue using this point 
				}
				alpha /= 2; // Worst? => Use a smaller alpha 
				countBadLineIterations++;
			}

			// Can't get a better error function on this line? => Restore old parameters and return
			// (we couldn't improbe, so we are still at W0, next iteration it's going to be the same as this one)
			if( k >= maxLineSearchIterations ) {
				copyToParameterList(parameterValues0);
				countNoImpovement++;
				Gpr.debug(debug, "Couldn't get any improvement => finished");
				return;
			}

			countImpovement++;
		}

		countMaxIterations++; // Number of time 'max iterations was reached
	}

	public void setErrorFunction(ErrorFunction errorFunction) {
		this.errorFunction = errorFunction;
	}

	public void setFuzzyRuleSet(FuzzyRuleSet fuzzyRuleSet) {
		this.fuzzyRuleSet = fuzzyRuleSet;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setMaxLineSearchIterations(int maxLineSearchIterations) {
		this.maxLineSearchIterations = maxLineSearchIterations;
	}

	public void setParameterList(ArrayList<Parameter> parameterList) {
		this.parameterList = parameterList;
	}

}
