package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: Sum(x1,x2,x3,...,xn) = x1 + x2 + x3 + ... + xn 
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffSum extends MffFunction {

	/**
	 * Constructor
	 */
	public MffSum(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		double sum = 0;
		for( int i = 0; i < values.length; i++ )
			if( !Double.isNaN(values[i]) ) sum += values[i];

		return sum;
	}

	public String toString() {
		if( terms == null ) return "";
		String out = "";
		for( int i = 0 ; i < terms.length ; i++ ) {
			out += terms[i].toString();
			if( i < terms.length -1 ) out += " + ";
 		}
		
		return "( " + out + " )";
	}

}
