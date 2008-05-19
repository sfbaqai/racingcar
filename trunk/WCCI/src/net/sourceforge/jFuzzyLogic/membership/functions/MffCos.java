package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: cos(x1)
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffCos extends MffFunction {

	/** Constructor */
	public MffCos(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 1 ) throw new RuntimeException("Function Exp needs only one argument: cos(x)");
		return Math.cos(values[0]);
	}

	public String toString() {
		if( terms == null ) return "";
		return "cos( " + terms[0].toString() + " )";
	}

}
