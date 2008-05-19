package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: ln(x1)
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffLn extends MffFunction {

	/** Constructor */
	public MffLn(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 1 ) throw new RuntimeException("Function Ln needs only one argument: ln(x)");
		return Math.log(values[0]);
	}

	public String toString() {
		if( terms == null ) return "";
		return "ln( " + terms[0].toString() + " )";
	}

}
