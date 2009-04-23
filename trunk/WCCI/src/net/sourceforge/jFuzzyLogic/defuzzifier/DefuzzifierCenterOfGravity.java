package net.sourceforge.jFuzzyLogic.defuzzifier;

import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Center of gravity defuzzyfier
 * @author pcingola@users.sourceforge.net
 */
public class DefuzzifierCenterOfGravity extends DefuzzifierContinuous {

	public DefuzzifierCenterOfGravity(Variable variable) {
		super(variable);
	}

	/** Deffuzification function */
	@Override
	public final double defuzzify() {
		double x = min, sum = 0, weightedSum = 0;

		// Calculate integrals (approximated as sums)
		double[] values = this.values;
		double stepSize=this.stepSize;
		for( int i = 0,len=values.length; i < len; i++, x += stepSize ) {
			sum += values[i];
			weightedSum += x * values[i];
		}

		// No sum? => this variable has no active antecedent
		return ( sum <= 0 )?Double.NaN:(weightedSum / sum);
	}

	@Override
	public String toStringFCL() {
		return "METHOD : COG;";
	}
}
