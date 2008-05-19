package net.sourceforge.jFuzzyLogic.optimization;

import java.util.ArrayList;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Paramtric optimization using 'jump' criteria for fuzzy rule sets
 *
 * Tries to optimize parameters (fuzzy sets parameters and rule's weights) using a 'jump' algorithm.
 * Jump optimization: Simply tries different parameters 'jumping' (changing the parameter a specified amount). 
 * 
 * Note: This is NOT a very sofisticated algorithm (actually looks a lot like 'random search'), but if 
 * you are not in a hurry, most of the time works better than Gradient descent.
 * 
 * @author pcingola@users.sourceforge.net
 */
public class OptimizationDeltaJump {

	/** Debug mode? */
	public static boolean debug = false;
	/** Default Max number of iterations */
	private static int DEFAULT_MAX_ITERATIONS = 100;
	/** Default Max 'line search' number of iterations */
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

	public OptimizationDeltaJump(FuzzyRuleSet fuzzyRuleSet, ErrorFunction errorFunction, ArrayList<Parameter> parameterList) {
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
		OptimizationDeltaJump.debug = debug;
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

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
	 * Estimate jump 
	 * @param parameterNumber : Parameter number to estimate
	 * @param errro0 : Function value at X_0
	 * @return
	 */
	private double jump(int parameterNumber, double error0) {
		Parameter param = parameterList.get(parameterNumber);
		double epsilon = param.getEpsilon();
		double paramValue = param.get();
		double error1;

		// Increase parameter (in this dimention)
		if( param.set(paramValue + epsilon) ) {
			// Parameter changed? => re evaluate
			error1 = errorFunction.evaluate(fuzzyRuleSet);
			// Restore old parameter's value
			param.set(paramValue);
			// Good jump? (error decresed?) => ok
			if( error1 < error0 ) return epsilon;
		}

		// Dicrease parameter (in this dimention)
		if( param.set(paramValue - epsilon) ) {
			// Parameter changed? => re evaluate
			error1 = errorFunction.evaluate(fuzzyRuleSet);
			// Restore old parameter's value
			param.set(paramValue);
			// Good jump? (error decresed?) => ok
			if( error1 < error0 ) return -epsilon;
		}

		return 0;
	}

	/**
	 * Gradient optimization for error functions
	 * Gradient descent algorithm
	 * It also does a line search to get a good 'next point' on each iteration
	 * @param verbose : Be verbose (recomended, because it takes some time to converge)
	 */
	public void optimize(boolean verbose) {
		int iterNum, paramNum, lineIterNum, len = parameterList.size();
		double alpha, error, error0 = 0, errorItStart = 0, paramValue, jump, deltaErrorIt, bestAlpha, bestAlphaError;

		//---
		// Iterate
		//---
		for( iterNum = 0; iterNum < maxIterations; iterNum++ ) {
			// For each parameter (dimention)
			for( paramNum = 0; paramNum < len; paramNum++ ) {
				// Calculate error funtion at 'X_0' (starting point)
				error0 = errorFunction.evaluate(fuzzyRuleSet);
				if( paramNum == 0 ) errorItStart = error0;

				// Store parameter's value
				Parameter parameter = parameterList.get(paramNum);

				// Estimate universe (if not already done)
				if( parameter instanceof ParameterMembershipFunction ) ((ParameterMembershipFunction) parameter).getVariable().estimateUniverse();

				paramValue = parameter.get();
				if( verbose ) System.out.println("\tIteration: " + iterNum + "\tParameter: " + paramNum + " (" + parameter.getName() + ")\tError: " + error0);

				// Calculate 'jump' size / direction
				jump = jump(paramNum, error0);

				if( jump != 0 ) { // Can jump?
					//---
					// Line search: Iterate until we get a suitable alpha (jump step size)
					//---
					alpha = 1; // We set initial alpha value to 1.0
					bestAlpha = 0; // Best alpha
					bestAlphaError = error0; // Error using 'bestAlpha'
					for( lineIterNum = 0; lineIterNum < maxLineSearchIterations; lineIterNum++ ) {
						// Advance towards 'jump' direction (step = alpha * jump)
						if( parameter.set(paramValue + alpha * jump) ) { // Can set parameter?
							// Evaluate new point (is it better than before?)
							error = errorFunction.evaluate(fuzzyRuleSet);
							if( verbose ) System.out.println("\t\tLine iteration:" + lineIterNum + ", alpha:" + alpha + ", Delta_Error:" + (error - error0));

							// Better error (smaller)? => Ok, try using a bigger alpha
							if( error < error0 ) {
								if( error < bestAlphaError ) { // Store 'best' alpha so far
									bestAlphaError = error;
									bestAlpha = alpha;
								}
								alpha *= 2; // Increase alpha's size
							} else break; // Error is bigger? => break loop 
						} else break; // Cannot set parameter? => break loop
					}
					
					// Set parameter to 'best' value found in "line search"
					parameter.set(paramValue + bestAlpha * jump);

					if( verbose ) {
						if( lineIterNum >= maxLineSearchIterations ) System.out.println("\t\tWARNING: Too many line iterations! This may point a desing error in your fuzzy system.");
						if( paramValue != parameter.get() ) System.out.println("\t\tParameter before: " + paramValue + "\t after: " + parameter.get() + "\tbest alpha: " + bestAlpha);
					}
				}
			}

			// Calculate full itearation's delta error
			deltaErrorIt = error0 - errorItStart;
			if( verbose ) System.out.println("----- Delta error (full iteration): " + deltaErrorIt + " -----");
			if( deltaErrorIt >= 0 ) {
				if( verbose ) System.out.println("Can't get any better! Giving up");
				return;
			}
		}

		Gpr.debug(debug, "Max number of iterations reached");
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
