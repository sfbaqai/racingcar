package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: Exp(x1) = e^x1   
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffExp extends MffFunction {

	/** Constructor */
	public MffExp(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 1 ) throw new RuntimeException("Function Exp needs only one argument: exp(x)");
		return Math.exp(values[0]);
	}

	public String toString() {
		if( terms == null ) return "";
		return "exp( " + terms[0].toString() + " )";
	}

}
