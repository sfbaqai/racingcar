package net.sourceforge.jFuzzyLogic.defuzzifier;

import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Center of gravity for functions defuzzyfier
 * 
 * @author pcingola@users.sourceforge.net
 */
public class DefuzzifierCenterOfGravityFunctions extends DefuzzifierDiscrete {

	public DefuzzifierCenterOfGravityFunctions(Variable variable) {
		super(variable);
	}

	/** Deffuzification function */
	public double defuzzify() {
		double x, y, sum = 0, sumWeight = 0;
		for( Iterator it = iterator(); it.hasNext(); ) {
			Double xD = (Double) it.next();
			y = getDiscreteValue(xD);
			x = xD;
			sumWeight += x * y;
			sum += y;
		}

		if( sum != 0 ) return sumWeight / sum;
		return Double.NaN;
	}

	/** Reset values */
	public void reset() {
		discreteValues = new HashMap<Double, Double>();
	}

	public String toStringFCL() {
		return "METHOD : COGF;";
	}
}
