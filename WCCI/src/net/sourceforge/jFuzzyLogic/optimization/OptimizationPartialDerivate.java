package net.sourceforge.jFuzzyLogic.optimization;

import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Paramtric partial derivate optimization for fuzzy rule sets
 * 
 * Tries to optimize parameters (fuzzy sets parameters and rule's weights) using a 'derivate' algorithm.
 * Derivate optimization: Simply calculates derivate and tries to optimize in that direction using a 'line search' algorithm.
 *
 * @author pcingola@users.sourceforge.net
 */
public class OptimizationPartialDerivate {

	/** Alpha for line iterations */
	public static double alphaLineIterations;
	/** Some counuters for statistics */
	public static int countDerivatemTooSmall, countImpovement, countNoImpovement, countMaxIterations, countOptimizations, countIterations, countLineIterations,
			countGoodLineIterations, countBadLineIterations;
	/** Debug mode? */
	public static boolean debug = false;
	/** Max number of iterations */
	private static int DEFAULT_MAX_ITERATIONS = 20;
	/** Maximum 'line search' number of iterations */
	private static int DEFAULT_MAX_LINE_SEARCH_ITERATIONS = 20;

	/** Error function to minimize */
	ErrorFunction errorFunction;
	/** Fuzzy rule set's whose parameters we are optimizing */
	FuzzyRuleSet fuzzyRuleSet;
	/** Max number of iterations */
	int maxIterations;
	/** Maximum 'line search' number of iterations */
	int maxLineSearchIterations;
	/** Parameters to optimize */
	ArrayList<Parameter> parameterList;

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	public OptimizationPartialDerivate(FuzzyRuleSet fuzzyRuleSet, ErrorFunction errorFunction, ArrayList<Parameter> parameterList) {
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
		OptimizationPartialDerivate.debug = debug;
	}

	public static String stats() {
		String stats = "\tTotal Optimizations: " + countOptimizations//
				+ "\n\tNorm Too Small (return cause): " + countDerivatemTooSmall//
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
	 * Estimate partial derivate 
	 * @param parameterNumber : Parameter number to estimate
	 * @param errro0 : Function value at X_0
	 * @return
	 */
	private double derivate(int parameterNumber, double error0) {
		Parameter param = parameterList.get(parameterNumber);
		double epsilon = param.getEpsilon();
		double paramValue = param.get();

		if( param.set(paramValue + epsilon) ) ; // Increase parameter (in this dimention) to estimate derivate 
		else if( param.set(paramValue - epsilon) ) epsilon *= -1; // Cant' increase? => Decrease parameter (in this dimention) to estimate derivate 
		else epsilon = 0; // Cant' increase / decrease => no change

		double error1;
		if( epsilon != 0 ) {
			// Parameter changed? => re evaluate
			error1 = errorFunction.evaluate(fuzzyRuleSet);
			// Restore old parameter's value
			param.set(paramValue);
		} else error1 = error0; // No change

		// Partial derivate
		double derivate = (error0 - error1) / epsilon;
		if( error1 < error0 ) Gpr.debug("CAN DESCENT!!!   error1: " + error1 + "\terror0: " + error0);
		Gpr.debug(debug, "Parameter:" + param.getName() + "\tDerivate: " + derivate + "\tepsilon: " + epsilon);

		// Return derivate
		return derivate;
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
	 * Gradient optimization for error functions
	 * Gradient descent algorithm
	 * It also does a line search to get a good 'next point' on each iteration
	 */
	public void optimize() {
		int len = parameterList.size();
		double alpha, error, error0;

		countOptimizations++;

		//---
		// Iterate partial derivate optimization
		//---
		for( int iterNum = 0; iterNum < maxIterations; iterNum++ ) {
			countIterations++;

			// For each parameter...
			for( int paramNum = 0; paramNum < len; paramNum++ ) {
				// Calculate error funtion at 'X_0' (starting point)
				error0 = errorFunction.evaluate(fuzzyRuleSet);
				Gpr.debug(debug, "Iteration: " + iterNum + "\tParameter: " + paramNum + "\tError: " + error0);
				double paramValue = parameterList.get(paramNum).get();

				// Estimate partial derivate
				double derivate = derivate(paramNum, error0);

				// derivate too small? => Abort
				// if( Math.abs(derivate) <= parameterList.get(paramNum).getEpsilon() ) {
				if( Math.abs(derivate) <= 0.0000001 ) {
					countDerivatemTooSmall++;
					Gpr.debug(debug, "Derivate's abs too small (derivate: " + derivate + " < " + parameterList.get(paramNum).getEpsilon() + ")");
				} else {
					//---
					// Line search: Iterate until we get a suitable alpha (derivate's step size)
					//---
					alpha = 1; // We set initial alpha value to 1.0 and 
					for( int lineIterNum = 0; lineIterNum < maxLineSearchIterations; lineIterNum++ ) {
						countLineIterations++;

						// Advance against derivate's direction (step = -alpha)
						parameterList.get(paramNum).set(paramValue - alpha * derivate);

						// Evaluate new point (is it better than before?)
						error = errorFunction.evaluate(fuzzyRuleSet);
						Gpr.debug(debug, "\tLine iteration: " + lineIterNum + "\talpha: " + alpha + "\tDelta_Error: " + (error - error0));
						if( error < error0 ) {
							countGoodLineIterations++;
							break; // Better? => Continue using this point 
						}
						alpha /= 2; // Worst? => Use a smaller alpha 
						countBadLineIterations++;
					}
				}
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
