package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion

 * Nop : No Operation (just return first term as value)
 * Function: Nop(x1) = x1  
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffNop extends MffFunction {

	/**
	 * Constructor
	 */
	public MffNop(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		for( int i = 0; i < values.length; i++ )
			if( !Double.isNaN(values[i]) ) return values[i];

		return Double.NaN;
	}

	@Override
	public String toString() {
		if( terms == null ) return "";
		return "( " + terms[0].toString() + " )";
	}

}
