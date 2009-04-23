package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: log(x1)   
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffLog extends MffFunction {

	/** Constructor */
	public MffLog(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		if( values.length != 1 ) throw new RuntimeException("Function Ln needs only one argument: log(x)");
		return Math.log10(values[0]);
	}

	@Override
	public String toString() {
		if( terms == null ) return "";
		return "log( " + terms[0].toString() + " )";
	}

}
