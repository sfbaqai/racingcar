package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: sin(x1)
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffSin extends MffFunction {

	/** Constructor */
	public MffSin(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 1 ) throw new RuntimeException("Function Sin needs only one argument: sin(x)");
		return Math.sin(values[0]);
	}

	public String toString() {
		if( terms == null ) return "";
		return "sin( " + terms[0].toString() + " )";
	}

}
