package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: Modulus(x1,x2) = x1 % x2   
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffModulus extends MffFunction {

	/** Constructor */
	public MffModulus(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 2 ) throw new RuntimeException("Function Modulus needs two (and only two) arguments: x ^ y");
		return values[0] % values[1];
	}

	public String toString() {
		if( terms == null ) return "";
		return "( " + terms[0].toString() + " % " + terms[1].toString() + " )";
	}

}
